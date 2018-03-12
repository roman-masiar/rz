package cz.leveland.robzone.modules;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.leveland.appbase.database.exceptions.WrongInputException;
import cz.leveland.appbase.util.ResponseData;
import cz.leveland.appbase.util.ResponseObject;
import cz.leveland.robzone.database.entity.Claim;
import cz.leveland.robzone.database.entity.ClaimItem;
import cz.leveland.robzone.database.entity.Delivery;
import cz.leveland.robzone.database.entity.DeliveryItem;
import cz.leveland.robzone.database.entity.Order;
import cz.leveland.robzone.database.entity.OrderChange;
import cz.leveland.robzone.database.entity.OrderItem;
import cz.leveland.robzone.database.entity.Partner;
import cz.leveland.robzone.database.entity.PkVatRate;
import cz.leveland.robzone.database.entity.Product;
import cz.leveland.robzone.database.entity.Unit;
import cz.leveland.robzone.database.entity.VatRate;
import cz.leveland.robzone.database.entity.dto.Bundle;
import cz.leveland.robzone.database.entity.dto.ClaimItemDto;
import cz.leveland.robzone.database.entity.dto.DeliveryDto;
import cz.leveland.robzone.database.entity.dto.InStockDto;
import cz.leveland.robzone.database.entity.dto.InStockProductDto;
import cz.leveland.robzone.database.entity.dto.LaborItemDto;
import cz.leveland.robzone.database.entity.dto.OrderItemDto;
import cz.leveland.robzone.database.impl.ClaimDaoImpl;
import cz.leveland.robzone.database.impl.ClaimItemDaoImpl;
import cz.leveland.robzone.database.impl.DeliveryDaoImpl;
import cz.leveland.robzone.database.impl.DeliveryItemDaoImpl;
import cz.leveland.robzone.database.impl.InstockDaoImpl;
import cz.leveland.robzone.database.impl.OrderDaoImpl;
import cz.leveland.robzone.database.impl.OrderItemDaoImpl;
import cz.leveland.robzone.database.impl.OutstockDaoImpl;
import cz.leveland.robzone.database.impl.PartnerDaoImpl;
import cz.leveland.robzone.database.impl.ProductDaoImpl;
import cz.leveland.robzone.database.impl.ServiceRateDaoImpl;
import cz.leveland.robzone.database.impl.StockItemDaoImpl;
import cz.leveland.robzone.database.impl.VatRateDaoImpl;
import cz.leveland.robzone.exception.StockAllocationException;
import cz.leveland.robzone.exception.WrongDataException;

@Service
public class ClaimsAgenda extends Agenda{

	
	@SuppressWarnings("unused")
	private static final String AGENDA_NAME = "claims";


	private static final int MAX_UNFINISHED_REPAIRS = 2;


	private static final int NEXT_REPAIR_STATUS_OK = 0;
	private static final int NEXT_REPAIR_STATUS_TOO_MANY = 1;
	private static final int NEXT_REPAIR_STATUS_NO_MORE = 2;


	private static final int DEFAULT_LABOUR_VAT_CATEGORY = 1;

	@Autowired
	StockAgenda stockAgenda;

	@Autowired
	TradeAgenda tradeAgenda;

	@Autowired
	ClaimDaoImpl claimDao;

	@Autowired
	ClaimItemDaoImpl claimItemDao;
	
	@Autowired
	OrderItemDaoImpl orderItemDao;

	@Autowired
	DeliveryDaoImpl deliveryDao;
	
	@Autowired
	DeliveryItemDaoImpl deliveryItemDao;
	
	@Autowired
	ProductDaoImpl productDao;

	@Autowired
	PartnerDaoImpl partnerDao;
	
	@Autowired
	StockItemDaoImpl stockItemDao;
	
	@Autowired
	ServiceRateDaoImpl serviceRateDao;
	

	@Autowired
	InstockDaoImpl instockDao;
	
	@Autowired
	OutstockDaoImpl outstockDao;
	
	
	@Autowired
	OrderDaoImpl orderDao;
	
	@Autowired
	VatRateDaoImpl vatRateDao;
	


	public ResponseObject getClaims() {
		
		return buildResponse("claims", claimDao.getAll(getCompanyId()));
	}

	public ResponseObject getRepairs() {
		
		return buildResponse("repairs", claimDao.getRepairs(getUserId()));
	}
	
	public ResponseObject getClaimItems(int claimId) {
		
		return buildResponse("claimItems", claimItemDao.getByClaimIdDto(claimId));
	}


	public ResponseObject getClaim(int claimId) {
		
		Claim claim = claimDao.get(claimId);
		DeliveryDto delivery = deliveryDao.getDeliveryOrderPartner(claim.getDeliveryId());
		Order order = orderDao.get(delivery.getOrderId());
		
		ResponseData result = responseDataResult("delivery", delivery);
		result.put("order", order);
		result.put("claim", claim);
		return buildResponse(result, STATUS_OK);
	}

	public ResponseObject getRepair(int repairId) {
		
		Claim claim = claimDao.get(repairId);
		
		
		DeliveryDto delivery = deliveryDao.getDeliveryOrderPartner(claim.getDeliveryId());
		Order repairOrder = orderDao.getByClaimId(repairId);
		Order purchaseOrder = orderDao.get(delivery.getOrderId());
		List<OrderItemDto> orderItems = orderItemDao.getByOrderIdDto(repairOrder.getId());
		
		ResponseData result = responseDataResult("delivery", delivery);
		result.put("repairOrder", repairOrder);
		result.put("purchaseOrder", purchaseOrder);
		result.put("claim", claim);			
		result.put("orderItems", orderItems);
		return buildResponse(result, STATUS_OK);
	}
	
	private InStockDto getClaimInstock(int repairId) {
		
		List<ClaimItemDto> claimItems = (List<ClaimItemDto>)claimItemDao.getByClaimIdDto(repairId);
		ClaimItemDto first = claimItems.get(0);
		return instockDao.getDto(first.getInstockId());
	}

	public ResponseObject getRepairItems(int repairId) {
		return buildResponse("repairItems", orderItemDao.getByOrderIdDto(repairId));
	}



	public ResponseObject addClaim(Map<String, Object> claimData) throws WrongInputException, WrongDataException {

		
		Partner warrantyPayer = getWarrantyPayer();		

		/* read input */
		@SuppressWarnings("unchecked")
		List<LinkedHashMap<String,Object>> claimItems = (List<LinkedHashMap<String,Object>>)claimData.get("claimItems");		
		int warehouseId = (int)claimData.get("warehouseId");
		int deliveryId = (int)claimData.get("deliveryId");
		int transportTypeId = (int)claimData.get("transportTypeId");
		int paymentTypeId = (int)claimData.get("paymentTypeId");
		String message = (String)claimData.get("message");

		/* get related delivery */
		DeliveryDto delivery = deliveryDao.getDeliveryOrderPartner(deliveryId);
		
		/* create the claim */
		Claim claim = new Claim(getCompanyId(), getUserId(), deliveryId, message);
		claim.setTransportTypeId(transportTypeId);
		claim.setPaymentTypeId(paymentTypeId);
		claim.setCustomer(delivery.getName());
		claim.setCity(delivery.getCity());
		claim.setDeliveryId(delivery.getId());
		claim.setMessage(message);
		claim.setStreet("street");
		claimDao.saveOrUpdate(claim);
		
		/* add claim items / order + order items if needed */
		Order claimOrder = null;
		for (LinkedHashMap<String,Object> one: claimItems) {			
			ClaimItem claimItem = new ClaimItem(claim.getId(), one);
			claimItemDao.saveOrUpdate(claimItem);
			if (claimItem.needsOrder()) {
				if (claimOrder == null) 
					claimOrder = makeClaimOrder(claim, delivery, warehouseId);

				/* if claim item needs to be replaced, put it in the order */
				if (claimItem.getAction() == ClaimItem.ACTION_EXCHANGE) 
					addItemToClaimOrder(claimOrder, claimItem, delivery, one, warrantyPayer);
			}
		}
		return buildResponse("claim", claim);
	}
	
	
	/**
	 * gets a partner which represents currently selected company
	 * used to get payerId for claim order
	 * @return
	 * @throws WrongInputException
	 */
	private Partner getWarrantyPayer() throws WrongInputException {
		/* add items (for repair, exchange) */
		Partner warrantyPayer = partnerDao.findById("Partner", "companyId", getCompanyId());
		if (warrantyPayer == null)
			throw new WrongInputException();
		return warrantyPayer;
	}

	private void addItemToClaimOrder(Order claimOrder, ClaimItem claimItem, DeliveryDto delivery, LinkedHashMap<String,Object> one, Partner payer) throws WrongDataException, WrongInputException {
		
		InStockProductDto instock = instockDao.findProduct(delivery.getCountryId(), (int)one.get("instockId"));
		if (instock == null)
			throw new WrongDataException();

		Product product = productDao.get(instock.getProductId());
		OrderItem orderItem = new OrderItem(claimOrder.getId(), instock.getProductId(), null, claimItem.getQty(), product.getPrice(), 0.0, 0.0);
		if (claimItem.isWarrantyReason()) 
			orderItem.setPayerId(payer.getId());
		
		orderItemDao.saveOrUpdate(orderItem);

	}

	private Order makeClaimOrder(Claim claim, DeliveryDto delivery, int warehouseId) {
		Order claimOrder = Order.getRepairInstance(getUserId(), delivery.getCustomerId(), 
				delivery.getCustomerId(), claim.getId());
		claimOrder.setOrderNo(generateOrderNo(claim));
		claimOrder.setWarehouseId(warehouseId);
		//TODO
		claimOrder.setCurrencyId(901);
		orderDao.saveOrUpdate(claimOrder);
		return claimOrder;
	}

	public ResponseObject nextRepair(Map<String, Object> repairData) {
		
		//int warehouseId = (int)repairData.get("warehouseId");
		ResponseData result = new ResponseData(); 
		int unFinished = claimDao.getUnfinished(getUserId());
		if (unFinished >= MAX_UNFINISHED_REPAIRS) {
			result.put("status", NEXT_REPAIR_STATUS_TOO_MANY);
			return buildResponse(result, STATUS_OK);
		}
		
		Claim claim = claimDao.getNextForRepair(getCompanyId());
		if (claim == null) {
			result.put("status", NEXT_REPAIR_STATUS_NO_MORE);
			return buildResponse(result, STATUS_OK);
		}
		
		claim.setSolverId(getUserId());
		claimDao.saveOrUpdate(claim);
		
		
		/*
		DeliveryDto delivery = deliveryDao.getDeliveryOrderPartner(claim.getDeliveryId());
		Order order = Order.getRepairInstance(getUserId(), delivery.getCustomerId(), claim.getId());
		order.setOrderNo(generateOrderNo(claim));
		order.setWarehouseId(warehouseId);
//TODO
order.setCurrencyId(901);
		orderDao.saveOrUpdate(order);
		*/
		result.put("status", NEXT_REPAIR_STATUS_OK);
		//result.put("orderId", order.getId());
		
		return buildResponse(result, STATUS_OK);
	}

	private String generateOrderNo(Claim claim) {
		
		return (11000000+claim.getId()) + "";
	}

	
	/**
	 * adds an item to repair (actually adds item to claim order)
	 * @param itemData
	 * @return
	 * @throws WrongInputException
	 */
	public ResponseObject addRepairItem(Map<String, Object> itemData) throws WrongInputException {
		
		
		int repairId = (int)itemData.get("repairId");
		int orderId = (int)itemData.get("orderId");
		int productId = (int)itemData.get("productId");
		int qty = (int)itemData.get("qty");
		boolean paid = (boolean)itemData.get("paid");
		if (repairId <= 0 || qty <= 0 )
			throw new WrongInputException("Wrong input data");

		// TODO - more check order vs delivery here
		
		Partner warrantyPayer = getWarrantyPayer();
		OrderItem newItem = createOrderItem(orderId, productId, qty, warrantyPayer, paid);
		
		/*
		int countryId = product.getCountryId();
		PkVatRate pk = new PkVatRate(getCompanyId(), product.getVatCategoryId(), countryId);
		VatRate vatRate = vatRateDao.get(pk);		
		newItem.setVatRate(vatRate.getRate());
		*/
		
		try {
			orderItemDao.saveOrUpdate(newItem);
		} catch (Exception e) {
			logError(AGENDA_NAME, "addOrderItem", "order item couldn't be saved, order id " + repairId);
			throw new WrongInputException();
		}
		return getRepairItems(orderId);
	}

	private OrderItem createOrderItem(int orderId, int productId, int qty, Partner warrantyPayer, boolean paid) throws WrongInputException {
		
		Product product = productDao.get(productId);		
		if (product == null)
			throw new WrongInputException("Wrong input data");
		Double unitPrice = product.getPrice();
		Double discountPercent = 0.0;
		if (unitPrice==null || unitPrice < 0.0)
			throw new WrongInputException("Wrong input data");
		
		double discount = 0.0; 
		OrderItem newItem = new OrderItem(orderId, productId, null, qty, unitPrice, discountPercent, discount);
		if (!paid) {
			newItem.setPayerId(warrantyPayer.getId());
			newItem.setUnitPrice(0.0);
			newItem.setPrice(0.0);
			newItem.setTotalPrice(0.0);
		}
		
		return newItem;

	}

	/**
	 * once the repair is finished by technician, the repair is finished
	 * which means that:
	 * 	- labor items are added
	 *  - transport and payment type are added
	 *  - claim order is processed (allocated to instocks)
	 * @param repairData
	 * @return
	 * @throws WrongInputException
	 * @throws WrongDataException
	 */
	public ResponseObject finishRepair(Map<String, Object> repairData) throws WrongInputException, WrongDataException {
		
		int repairId = (int)repairData.get("repairId");
		Claim claim = claimDao.get(repairId);
		/* get repaired model */
		InStockDto instock = getClaimInstock(repairId);
		
		if (claim == null)
			throw new WrongInputException();
		
		DeliveryDto delivery = deliveryDao.getDeliveryOrderPartner(claim.getDeliveryId());
		
		List<LaborItemDto> labourItemList = addLaborItems(claim.getId(), instock.getStockItemId());
		
		List<ClaimItem> claimItems = claimItemDao.getByClaimId(repairId);
		boolean paid = false;
		for (ClaimItem one: claimItems) {
			if (!one.isWarrantyReason())
				paid = true;
		}
		
		addLaborOrderItems(labourItemList);
		
		Order claimOrder = orderDao.getByClaimId(claim.getId());
		addTransportPayment(claimOrder, claim, delivery, paid);
		
		claim.setFinished(true);
		claimDao.saveOrUpdate(claim);
		
		//processClaimOrder(claimOrder);
		
		return buildResponseOk();
	}


	/**
	 * adds payment and transport to a claim order which is being closed
	 * if order is partly paid by customer, regular transport and payment are added, otherwise zero priced ones
	 * @param claim
	 * @param delivery
	 * @param paid
	 * @throws WrongInputException
	 */
	private void addTransportPayment(Order claimOrder, Claim claim, DeliveryDto delivery, boolean paid) throws WrongInputException {
		
		
		
		Partner warrantyPayer = getWarrantyPayer();
		OrderItem transport = createOrderItem(claimOrder.getId(), claim.getTransportTypeId(), 1, warrantyPayer, paid);
		OrderItem payment = createOrderItem(claimOrder.getId(), claim.getPaymentTypeId(), 1, warrantyPayer, paid);
		
		orderItemDao.saveOrUpdate(transport);
		orderItemDao.saveOrUpdate(payment);
	}

	/**
	 * processes claim order
	 * @param deliveryId
	 * @throws WrongInputException
	 * @throws WrongDataException
	 */
	private void processClaimOrder(Order claimOrder) throws WrongInputException, WrongDataException {

		List<OrderItemDto> relatedItems = orderItemDao.getByOrderIdDto(claimOrder.getId());
		
		if (!relatedItems.isEmpty()) {
			OrderItemDto oi = relatedItems.get(0);
			Order firstOrder = orderDao.get(oi.getOrderId());
			// TODO
			//stockAgenda.updateAvailable(firstOrder.getWarehouseId());
			
			for (OrderItemDto one:relatedItems) {
				try {
					tradeAgenda.processOrder(one.getOrderId());
				} catch (StockAllocationException e) {
					throw new WrongDataException();
				}
			}
		}
		

		
	}


	/**
	 * makes list of service labor items for selected claim and repaired model
	 * the list will then be used for order
	 * @param claimId
	 * @param modelId
	 * @return list of labor items 
	 */
	private List<LaborItemDto> addLaborItems(Integer claimId, int modelId) {
		
		List<LaborItemDto> list = serviceRateDao.getByClaimId(claimId);		
		List<LaborItemDto> laborItems = findLaborItems(list, modelId);
		if (laborItems.isEmpty()) 
			laborItems = findLaborItems(list, null);
		return laborItems;
	}

	/**
	 * filters out labor items based on modelId 
	 * @param list
	 * @param modelId
	 * @return
	 */
	private List<LaborItemDto> findLaborItems(List<LaborItemDto> list, Integer modelId) {

		List<LaborItemDto> resultList = new ArrayList<LaborItemDto>();
		for (LaborItemDto one:list)
			if (one.getModelId() == modelId)
				resultList.add(one);
		return resultList;
	}

	/**
	 * adds labor order items related to repaired parts from list of claim order 
	 * @param labourItemList
	 * @throws WrongDataException
	 * @throws WrongInputException
	 */
	private void addLaborOrderItems(List<LaborItemDto> labourItemList) throws WrongDataException, WrongInputException {
		
		PkVatRate pk = new PkVatRate(getCompanyId(), DEFAULT_LABOUR_VAT_CATEGORY, ccFilter.getOneCurrentCountry(getUserId()));
		VatRate vatRate = vatRateDao.get(pk);
		
		Partner mechanic = partnerDao.findById("Partner", "userId", getUserId());		
		Product labor = productDao.getServiceLabour(mechanic.getId());
		if (labor == null)
			throw new WrongDataException();
		
		for (LaborItemDto one: labourItemList) {
			
			OrderItem orderItem = new OrderItem(one.getOrderId(), labor.getId(), null, one.getRate(), labor.getPrice(), 0.0, 0.0);
			orderItem.setDescription(one.getName());
			orderItem.setUnit(Unit.HOUR);
			orderItem.setVatRate(vatRate.getRate());
			orderItemDao.saveOrUpdate(orderItem);
		}
	}

	/*
	public ResponseObject getRelatedItems(int deliveryId, int currentClaimId) throws WrongInputException {
		
		Delivery delivery = deliveryDao.get(deliveryId);
		if (delivery == null)
			throw new WrongInputException();
		
		
		List<Claim> claims = claimDao.getByDeliveryId(deliveryId);
		List<Integer> claimIds = new ArrayList<Integer>();
		
		// add all except current one 
		for (Claim one:claims)
			if (one.getId() != currentClaimId)
				claimIds.add(one.getId());
		List<OrderItemDto> relatedItems = orderItemDao.getClaimOrderItems(claimIds);
		
		return buildResponse("relatedItems", relatedItems);
	}
	 */

	public ResponseObject getServiceRates() {
		
		return buildResponse("serviceRates", serviceRateDao.getAll(getCompanyId()));
	}


	

	
}

