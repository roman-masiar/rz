package cz.leveland.robzone.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cz.leveland.appbase.database.exceptions.WrongInputException;
import cz.leveland.appbase.util.ResponseData;
import cz.leveland.appbase.util.ResponseObject;
import cz.leveland.robzone.api.APIConnectionException;
import cz.leveland.robzone.api.APIParseException;
import cz.leveland.robzone.api.impl.DeliveryApiConnector;
import cz.leveland.robzone.database.entity.Delivery;
import cz.leveland.robzone.database.entity.DeliveryPackage;
import cz.leveland.robzone.database.entity.Invoice;
import cz.leveland.robzone.database.entity.Order;
import cz.leveland.robzone.database.entity.Partner;
import cz.leveland.robzone.database.entity.dto.DeliveryInfo;
import cz.leveland.robzone.database.entity.dto.DeliveryPackageDto;
import cz.leveland.robzone.database.entity.dto.Progress;
import cz.leveland.robzone.database.impl.DeliveryDaoImpl;
import cz.leveland.robzone.database.impl.DeliveryPackageDaoImpl;
import cz.leveland.robzone.database.impl.InvoiceDaoImpl;
import cz.leveland.robzone.database.impl.OrderDaoImpl;
import cz.leveland.robzone.database.impl.PartnerDaoImpl;
import cz.leveland.robzone.database.impl.ProductDaoImpl;
import cz.leveland.robzone.exception.OrderNotFoundException;
import cz.leveland.robzone.exception.PartnerNotFoundException;
import cz.leveland.robzone.exception.WrongDataException;
import cz.leveland.robzone.pusher.PushManager;
import cz.leveland.robzone.transport.PackNumberGenerator;
import cz.leveland.robzone.transport.PackNumberGeneratorFactory;
import cz.leveland.robzone.transport.DeliveryRequest;

@Service
public class DeliveryAgenda extends Agenda{

	@Autowired
	StockAgenda stockAgenda;
	
	@Autowired
	DeliveryDaoImpl deliveryDao;
	
	@Autowired
	OrderDaoImpl orderDao;
	
	@Autowired
	InvoiceDaoImpl invoiceDao;
	
	@Autowired
	ProductDaoImpl productDao;
	
	@Autowired
	PartnerDaoImpl partnerDao;
	
	@Autowired
	DeliveryApiConnector deliveryApiConnector;
	
	@Autowired
	PushManager pushManager;
	
	@Autowired
	DeliveryPackageDaoImpl deliveryPackageDao;

	
	public ResponseObject getDelivery(int warehouseId) {
		
		return buildResponse("delivery", deliveryDao.getAll(warehouseId));
	}
	public ResponseObject getOne(int deliveryId) {
		
		return buildResponse("oneDelivery", deliveryDao.getOne(deliveryId));
	}

	public ResponseObject importDeliveryInfo() throws APIConnectionException, APIParseException, WrongInputException {
		
		List<String> unknownPackNumbers = new ArrayList<String>();
		@SuppressWarnings("unchecked")
		List<DeliveryInfo> deliveryInfos = (List<DeliveryInfo>) deliveryApiConnector.getData();
		List<ActionResult> errors = new ArrayList<ActionResult>();
		
		int total = deliveryInfos.size(), bad = 0, good = 0, cnt = 0;
		for (DeliveryInfo oneInfo:deliveryInfos) {
			
			DeliveryPackage deliveryPackage = deliveryPackageDao.getByPackageNo(oneInfo.getPackNumber());
			
			if (deliveryPackage == null) {
				unknownPackNumbers.add(oneInfo.getPackNumber());
				bad++;
				continue;
			} 
			deliveryPackage.setDelivered(oneInfo.getDelivered());
			deliveryPackage.setReturned(oneInfo.getReturned());
			deliveryPackage.setStatus(oneInfo.getStatus());
			deliveryPackageDao.saveOrUpdate(deliveryPackage);
			good++; cnt++;
			Progress progress = new Progress(total, cnt, good, bad);
			pushManager.reportDeliveryProgress(progress, getCompanyId());
		}
		
		Map<String,Object> result = responseDataResult("delivery", deliveryDao.getAll(getCompanyId()));
		result.put("errors", errors);
		
		
		return buildResponse(result,STATUS_OK);

	}

	@SuppressWarnings("unchecked")
	@Transactional
	public ResponseObject askForDelivery(Map<String, Object> transportData) throws OrderNotFoundException, PartnerNotFoundException {
		

		List<DeliveryRequest> deliveryRequests = new ArrayList<DeliveryRequest>();
		List<Map<String,Object>> packages = (List<Map<String,Object>>)transportData.get("packages");
		int transporerId = (int)transportData.get("providerId");
		
		Partner transporter = partnerDao.get(transporerId);

		
		for (Map<String,Object> pack:packages) {
			String orderNo = (String)pack.get("orderNo");
			int packs = (int)pack.get("packs");
			
			
			Order order = orderDao.getByOrderNo(orderNo);			
			if (order == null)
				throw new OrderNotFoundException();
			Integer paymentType = orderDao.getPaymentType(order.getId());
			order.setPaymentTypeId(paymentType);
			Partner customer = partnerDao.get(order.getPartnerId());
			if (customer == null)
				throw new PartnerNotFoundException();
			
			List<DeliveryPackage> existingPackages = deliveryPackageDao.getUnlinked(orderNo, getCompanyId());
			int existingNo = existingPackages.size();
			int remainsToBeCreated = packs - existingNo;
			if (remainsToBeCreated <= 0)
				continue;
							
			DeliveryRequest request = new DeliveryRequest(order, remainsToBeCreated, customer, transporter);
			deliveryRequests.add(request);
		}
		
		createDeliveryPackages(transporter, deliveryRequests);
		
		List<DeliveryPackageDto> unlinkedPackages = deliveryPackageDao.getUnlinkedByTransporter(getCompanyId(), transporerId);
		
		ResponseData result = responseDataResult("packages", unlinkedPackages);
		result.put("transporter", transporter);
		return buildResponse(result, STATUS_OK);
	}
	
	
	

	/**
	 * creates delivery packages for every item of transport request list
	 * transporter-dependent number generators are used to generate next pack number
	 * based on history (existing delivery packs for particular transporter) 
	 * @param transporter
	 * @param list
	 */
	
	private void createDeliveryPackages(Partner transporter, List<DeliveryRequest> list) {
		
		PackNumberGenerator generator = PackNumberGeneratorFactory.getInstance(transporter);
		
		for (DeliveryRequest one:list) {
			
			int packType = generator.getPackType(one);
			DeliveryPackage last = deliveryPackageDao.getLast(transporter.getId(), packType);
			String packNumber = last==null ? generator.getFirst(packType) : last.getNumber();
			for (int i=0; i<one.getNumberOfPackages(); i++) {
								
				packNumber = generator.generateNext(packNumber, packType);
				DeliveryPackage pack = new DeliveryPackage(one, packType, packNumber, getCompanyId());
				deliveryPackageDao.saveOrUpdate(pack);		
				one.setPackageNumber(packNumber);
				
			}
		}		
	}

	public ResponseObject getOrderDelivery(String documentNo) throws WrongInputException, WrongDataException {
		
		Order order = orderDao.getByOrderNo(documentNo);
		if (order == null) {
			Invoice invoice = invoiceDao.findByInvoiceNo(documentNo);
			if (invoice == null)
				throw new WrongInputException();
			order = orderDao.get(invoice.getOrderId());
		}
		
		Delivery delivery = deliveryDao.getByOrderId(order.getId());
		return stockAgenda.getDeliveryDetails(delivery.getId());
	}



	
}
