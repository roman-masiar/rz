package cz.leveland.robzone.modules;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cz.leveland.appbase.database.exceptions.WrongInputException;
import cz.leveland.appbase.util.InputSanitizer;
import cz.leveland.appbase.util.ResponseData;
import cz.leveland.appbase.util.ResponseObject;
import cz.leveland.robzone.database.entity.Delivery;
import cz.leveland.robzone.database.entity.DeliveryItem;
import cz.leveland.robzone.database.entity.DeliveryPackage;
import cz.leveland.robzone.database.entity.InStock;
import cz.leveland.robzone.database.entity.Invoice;
import cz.leveland.robzone.database.entity.InvoiceItem;
import cz.leveland.robzone.database.entity.Order;
import cz.leveland.robzone.database.entity.OrderItem;
import cz.leveland.robzone.database.entity.OutStock;
import cz.leveland.robzone.database.entity.Partner;
import cz.leveland.robzone.database.entity.Product;
import cz.leveland.robzone.database.entity.ProductNature;
import cz.leveland.robzone.database.entity.StockItem;
import cz.leveland.robzone.database.entity.StockSet;
import cz.leveland.robzone.database.entity.StockSetItem;
import cz.leveland.robzone.database.entity.Warehouse;
import cz.leveland.robzone.database.entity.dto.DeliveryDto;
import cz.leveland.robzone.database.entity.dto.DeliveryItemDto;
import cz.leveland.robzone.database.entity.dto.InStockDto;
import cz.leveland.robzone.database.entity.dto.PickStockItemDto;
import cz.leveland.robzone.database.entity.dto.ProductDto;
import cz.leveland.robzone.database.entity.dto.ValuePair;
import cz.leveland.robzone.database.entity.interfaces.ProcessableOrder;
import cz.leveland.robzone.database.impl.DeliveryDaoImpl;
import cz.leveland.robzone.database.impl.DeliveryItemDaoImpl;
import cz.leveland.robzone.database.impl.DeliveryPackageDaoImpl;
import cz.leveland.robzone.database.impl.InstockDaoImpl;
import cz.leveland.robzone.database.impl.InvoiceDaoImpl;
import cz.leveland.robzone.database.impl.OrderDaoImpl;
import cz.leveland.robzone.database.impl.OrderItemDaoImpl;
import cz.leveland.robzone.database.impl.OutstockDaoImpl;
import cz.leveland.robzone.database.impl.PartnerDaoImpl;
import cz.leveland.robzone.database.impl.ProductDaoImpl;
import cz.leveland.robzone.database.impl.ProductSetItemDaoImpl;
import cz.leveland.robzone.database.impl.StockItemDaoImpl;
import cz.leveland.robzone.database.impl.StockSetDaoImpl;
import cz.leveland.robzone.database.impl.StockSetItemDaoImpl;
import cz.leveland.robzone.database.impl.WarehouseDaoImpl;
import cz.leveland.robzone.exception.OrderProcessingException;
import cz.leveland.robzone.exception.OverAllocationException;
import cz.leveland.robzone.exception.StockAllocationException;
import cz.leveland.robzone.exception.StockSetCreationException;
import cz.leveland.robzone.exception.UserErrorException;
import cz.leveland.robzone.exception.WrongDataException;
import cz.leveland.robzone.stock.Allocation;
import cz.leveland.robzone.stock.Allocations;
import cz.leveland.robzone.stock.FreeStockAllocator;

@Service
public class StockAgenda extends Agenda{

	
	private static final String AGENDA_NAME = "stock";

	private static final int DEFAULT_WAREHOUSE = 21;

	private static final int SERIALNO_INSTOCKS_NUMBER = 5;

	private static final boolean ALLOCATE_WHOLE_ORDER = true;

	
	@Autowired
	FinanceAgenda financeAgenda;
	
	@Autowired
	OrderItemDaoImpl orderItemDao;

	@Autowired
	DeliveryDaoImpl deliveryDao;
	
	@Autowired
	InvoiceDaoImpl invoiceDao;
	
	@Autowired
	DeliveryItemDaoImpl deliveryItemDao;
	
	@Autowired
	DeliveryPackageDaoImpl deliveryPackageDao;
	
	@Autowired
	ProductDaoImpl productDao;

	@Autowired
	StockItemDaoImpl stockItemDao;
	
	@Autowired
	StockSetDaoImpl stockSetDao;
	
	@Autowired
	StockSetItemDaoImpl stockSetItemDao;
	
	

	@Autowired
	InstockDaoImpl instockDao;
	
	@Autowired
	OutstockDaoImpl outstockDao;
	
	@Autowired
	ProductSetItemDaoImpl productSetItemDao;
	
	@Autowired
	TransactionComponents transactionComponents;
	
	@Autowired 
	FreeStockAllocator freeStockAllocator;
	
	@Autowired
	OrderDaoImpl orderDao;
	
	@Autowired
	PartnerDaoImpl partnerDao;
	
	@Autowired
	WarehouseDaoImpl warehouseDao;
	
	
	

	/**
	 * called from tradeAgenda - import and processOrder (client called)
	 * used in order item allocation
	 * updates instocks with available quantities for allocation 
	 * and reads product set items to allow for set products allocation
	 * called   
	 */
	
	public void updateAvailable(int warehouseId) {
		freeStockAllocator.init(warehouseId);
	}
	
	

	public ResponseObject getStockItems() {
		
		return buildResponse("stockItems", stockItemDao.getAll(getCompanyId()));
	}

	public ResponseObject getInstocks(int warehouseId) {
		return buildResponse("instocks", instockDao.getByWarehouseId(warehouseId));
	}
	
	public ResponseObject getInstocks(int warehouseId, int stockItemId) {
		return buildResponse("instocks", instockDao.getByWarehouseId(warehouseId, stockItemId));
	}
	
	public ResponseObject getOutstocksPacked(int warehouseId) {
		return buildResponse("outstocksPacked", outstockDao.getPacked(warehouseId));
	}

	public ResponseObject getOutstocksUnpacked(int warehouseId) {
		return buildResponse("outstocksUnpacked", outstockDao.getUnpacked(warehouseId));
	}
	
	public ResponseObject getStockSets(int warehouseId) {
		return buildResponse("stockSets", stockSetDao.getAvailable(warehouseId));
	}
	
	public ResponseObject getStock(int warehouseId) {
		return buildResponse("stock", instockDao.getStock(warehouseId));
	}
	
	public ResponseObject getPackages(int transporterId) {		
		return buildResponse("packages", deliveryPackageDao.getUnlinkedByTransporter(getCompanyId(), transporterId));
	}


	
	

	/**
	 * attempts to allocate order's items to free items on stock (i.e. instock records)
	 * @param order
	 * @param orderItems
	 * @throws OrderProcessingException
	 * @throws StockAllocationException 
	 * @throws WrongDataException 
	 */
	
	public void allocateOrder(ProcessableOrder order, List<OrderItem> orderItems, int warehouseId) throws  StockAllocationException {
		
		System.out.println("Stock processing order " + order.getOrderNo());
		int itemCount = 0;
		Date processed = new Date();
		for (OrderItem one:orderItems) {
			
			
			ProductDto productNature = productDao.getProductNature(one.getProductId());
			if (productNature == null) {				
				logError(AGENDA_NAME, "process order", "product not found, id:" + one.getProductId());
				throw new StockAllocationException();
			}
			
			if (productNature.getProductNatureId() != ProductNature.TYPE_GOODS)
				continue;
			
			one.setProcessed(processed);
			orderItemDao.saveOrUpdate(one);
			
			if (productNature.isSetProduct()) {
				allocateSetProduct(one, productNature.getId(), warehouseId);
				
			} else {
				
				allocateNonSetProduct(one, productNature.getStockItemId(), warehouseId);
			}
			itemCount++;
		}		
		
		/* set order's number of items, i.e. how many are expected to pick and deliver, makes things easy */
		orderDao.updateInt(order.getId(), "items", itemCount);
	}
	
	/**
	 * allocate set product to stock set (instead of instocks)
	 * stock set contains items pointing to instocks and one item with no link (for future product with serialno )
	 * @param one
	 * @param stockItemId
	 * @param warehouseId
	 */
	private void allocateSetProduct(OrderItem one, Integer productId, int warehouseId) {
		
		Integer sourceId = freeStockAllocator.allocateStockSet(productId);
		if (sourceId != null) {
			OutStock outStock = new OutStock(warehouseId, one.getId(), null, sourceId, null,
					getUser().getId(), null, null, 1); 
			outstockDao.saveOrUpdate(outStock);	
		}
	}

	/**
	 * allocates product order item to one or more instocks 
	 * depending on whether it has serial no and how many free qty is available at each instock
	 * @param one
	 * @param stockItemId
	 * @param warehouseId
	 * @throws StockAllocationException 
	 * @throws OrderProcessingException
	 * @throws WrongDataException 
	 */
	private void allocateNonSetProduct(OrderItem one, Integer stockItemId, int warehouseId) throws StockAllocationException {
		
		if (stockItemId == null) {
			logError(AGENDA_NAME, "process order", "stock item ID null, id:");
			throw new StockAllocationException();
		}

		StockItem stockItem = stockItemDao.get(stockItemId);
		if (stockItem == null) {
			logError(AGENDA_NAME, "process order", "stock item not found, id:" + stockItemId);
			throw new StockAllocationException();
		}

		
		if (stockItem.isHasSerialNo()) {
			/* with serial no */
			allocateAvailableSerial(one, stockItemId, warehouseId);
			
		} else {
			
			/* without serial No */
			allocateAvailableNonSerial(one, stockItemId, warehouseId);
		}
	}

	
	/**
	 * used to allocate items without serial number, they can be allocated to multiple instocks
	 * @param one
	 * @param stockItemId
	 * @param warehouseId
	 * @throws StockAllocationException 
	 */
	private void allocateAvailableNonSerial(OrderItem one, Integer stockItemId, int warehouseId) throws StockAllocationException   {
		
		Allocations allocations;
		try {
			int qty = (int) one.getQty();
			allocations = freeStockAllocator.allocateInStockNonSerial(qty, stockItemId);
		} catch (OverAllocationException e) {
			throw new StockAllocationException();
		}
		
		/* if allocated amount != ordered amount, don't create any outstocks */
		if (ALLOCATE_WHOLE_ORDER && allocations.getSum() != one.getQty())
			return;
		
		for (Allocation oneAlloc:allocations) {
			OutStock outStock = new OutStock(warehouseId, one.getId(), oneAlloc.getSourceId(), null, null,
					getUser().getId(), null, null, oneAlloc.getQty());
			outstockDao.saveOrUpdate(outStock);
		}
		
	}

	/**
	 * used to allocate order items with serial products to available instock
	 * produces 1 newly created outstock records linked to instock with serial no
	 * 
	 * @param one
	 * @param stockItem
	 * @param warehouseId
	 * @throws StockAllocationException 
	 * @throws OrderProcessingException 
	 * @throws WrongDataException 
	 */

	private void allocateAvailableSerial(OrderItem one, int stockItemId, int warehouseId) throws StockAllocationException   {
		
		//Integer sourceId = freeStockAllocator.allocateInStockSerial(stockItemId);
		//int availableQty = freeStockAllocator.freeSerialAvailableForOrder(stockItemId, 1);
		int allocatedQty = freeStockAllocator.allocateStockSerial(stockItemId, 1);
		
		if (allocatedQty <= 0) {
			logWarning(AGENDA_NAME, "allocateAvailableSerial", "free serial instocks not available for id:" + stockItemId);
			return;
		}
		
		/* no instock ID is given since serial numbers are not known until picking */
		OutStock outStock = new OutStock(warehouseId, one.getId(), null, null, null,
				getUser().getId(), null, null, 1);		
		outstockDao.saveOrUpdate(outStock);	
		
	}


	

	/**
	 * called by client - mannual receipt of goods to stock, instock record is created
	 * @param instockData
	 * @return
	 * @throws WrongInputException
	 */
	
	@Transactional(rollbackFor = Exception.class)
	public ResponseObject addInstock(Map<String, Object> instockData) throws WrongInputException {
		
		Integer stockItemId = (Integer)instockData.get("stockItemId");				
		Integer warehouseId = (Integer)instockData.get("warehouseId");				
		String info = (String)instockData.get("info");	
		int pickerId = getUser().getId();
		
		Short qty = ((Number)instockData.get("qty")).shortValue();		
		Number numberPrice = ((Number)instockData.get("price"));
		Double price = numberPrice==null ? 0.0 : numberPrice.doubleValue();
		
		checkInstockInput(stockItemId, qty, price);
		
		/* generate serial numbers */
		Long serialStart=null, serialEnd=null;
		
		if (instockData.get("serialFrom") != null)
			serialStart = ((Number)instockData.get("serialFrom")).longValue();
		if (instockData.get("serialTo") != null)
			serialEnd = ((Number)instockData.get("serialTo")).longValue();
				
		
		ResponseObject checkResult = checkQtyVsRange(qty, serialStart, serialEnd);
		if (checkResult != null)
				return checkResult;
					
		/* crate instock records */
		int counter = 0;
		try {
			if (serialStart != null && serialEnd != null) {
				/* bulk */
				
				for (long i=serialStart; i<=serialEnd; i++) {
					String serialNo = ""+i;
					InStock inStock =  new InStock(warehouseId, pickerId, stockItemId, 1, serialNo, info);
					instockDao.saveOrUpdate(inStock);
					counter++;
				}
			} else { 		
				/* individual */
				InStock inStock =  new InStock(warehouseId, pickerId, stockItemId, qty, null, info);
				instockDao.saveOrUpdate(inStock);
			}
		} catch (Exception e) {
			logError(AGENDA_NAME, "addInstock", "error saving instocks, serialStart: " + serialStart + ", counter: " + counter);
			throw new WrongInputException();
		}
		return getInstocks(warehouseId);
	}

	
	/* checks if instock input data is correct */	
	private void checkInstockInput(Integer stockItemId, Short qty, Double price) throws WrongInputException {
		
		if (stockItemId == null && qty == null || qty <=0 || price< 0.0d) {
			logError(AGENDA_NAME, "addInstock", "wrong input data, stockItemId:" + stockItemId + ", qty:" + qty + ", price: " + price );
			throw new WrongInputException();
		}		
	}

	
	 /* checks the range of serial number vs qty and possible overlap with pervious receipt */		
	private ResponseObject checkQtyVsRange(Short qty, Long serialStart, Long serialEnd) {
		
		/* both must be either number or null */
		if (
				serialStart == null && serialEnd != null ||
				serialStart != null && serialEnd == null
				) 
			return buildErrorResponse("Zadejte EAN od-do", STATUS_USER_ERROR);
		
		/* if both are null, no need to check anything */
		if (serialStart == null)
			return null;
		
		/* check range */
		if (serialStart == 0L || serialEnd == 0L || serialEnd < serialStart)
			return buildErrorResponse("Å patnÃ½ rozsah EANÅ¯", STATUS_USER_ERROR);
		
		/* compare range vs qty */
		long range = serialEnd - serialStart + 1;
		if (range != qty)
			return buildErrorResponse("NesedÃ­ poÄ�et a rozsah EANÅ¯, mnoÅ¾stvÃ­ " + qty + ", rozsah " + range, STATUS_USER_ERROR);
		if (serialEnd - serialStart > 5)
			return buildErrorResponse("PÅ™Ã­liÅ¡ mnoho zametaÄ�Å¯", STATUS_USER_ERROR);
		
		return null;
	}

	/**
	 * deletes instock from stock as long as it is not allocated already to outstock
	 * @param instockData
	 * @return
	 * @throws WrongInputException 
	 */
	public ResponseObject removeInstock(Map<String, Object> instockData) throws WrongInputException {
		
		int instockId = (int)instockData.get("instockId");
		InStock inStock = instockDao.get(instockId);
		if (inStock == null)
			throw new WrongInputException();
		instockDao.delete(instockId);
		return getInstocks(inStock.getWarehouseId());
	}


	
	/* ------------------ testing methods ----------------------- */
	
	/**
	 * creates instock items for all available products
	 * @param buildData 
	 * @throws WrongInputException 
	 */
	
	public ResponseObject testBuildStock(Map<String, Object> buildData) throws WrongInputException {
		
		List<StockItem> stockItems = stockItemDao.getAll(getCompanyId());
		int warehouseId = (int)buildData.get("warehouseId");
		int qty = (int)buildData.get("qty");
		
		Warehouse warehouse = warehouseDao.get(warehouseId);
		if (warehouse == null)
			throw new WrongInputException();
		
		int eanBase = 100000, cnt = 0;;
		for (StockItem one:stockItems) {
			InStock instock = null;
			if (one.isHasSerialNo()) {
				
				for (int i=0;i<qty;i++) {
					instock = new InStock(warehouseId, getUser().getId(), one.getId(), 1, "" + (eanBase + cnt), null);
					instockDao.saveOrUpdate(instock);
					if (cnt++ % 20 == 0)
						instockDao.flushAndCLear();
				}
			} else {
				instock = new InStock(warehouseId, getUser().getId(), one.getId(), qty, null, null);
				instockDao.saveOrUpdate(instock);
				if (cnt++ % 20 == 0)
					instockDao.flushAndCLear();
			}
			
			
		}
		return buildResponseOk();
				
	}
	
	/**
	 * adds stocksets created from product sets
	 * @param stockSetData
	 * @return
	 * @throws WrongInputException
	 * @throws StockSetCreationException
	 * @throws StockAllocationException 
	 * @throws OverAllocationException
	 */
	public ResponseObject addStockSets(Map<String,Object> stockSetData) throws WrongInputException, StockSetCreationException, StockAllocationException, OverAllocationException {
		
		Integer warehouseId= (int)stockSetData.get("warehouseId");
		Integer productId= (int)stockSetData.get("productId");
		Integer qty = (int)stockSetData.get("qty");
		
		if (warehouseId == null || productId == null || qty == null || qty <= 0 || productId <= 0)
			throw new WrongInputException();
		
		Warehouse warehouse = warehouseDao.get(warehouseId);
		if (warehouse.getCompanyId() != getCompanyId())
			throw new WrongInputException();
				
		ProductDto productNature = productDao.getProductNature(productId);
		
		Map<Integer,Integer> missing = null;
		if (productNature.isSetProduct())
			missing = transactionComponents.stockSetFromProductSet(productId, qty, warehouseId);
		
		List<ValuePair> missingWithNames = getMissingWithNames(missing);
		
		ResponseData result = responseDataResult("missing", missingWithNames);
				result.put("stockSets", stockSetDao.getAvailable(warehouseId));
		return buildResponse("addResult", result);
	}

	
	
	private List<ValuePair> getMissingWithNames(Map<Integer, Integer> missing) {
		
		if (missing == null || missing.isEmpty())
			return null;
		
		List<ValuePair> res = new ArrayList<ValuePair>(); 
		List<StockItem> list = stockItemDao.getList(missing.keySet());
		for (StockItem one:list) {
			res.add(new ValuePair(one.getName(), missing.get(one.getId())));
		}
		return res;
	}

	
	/**
	 * used to create delivery records meaning the goods has been packed and forwarded to transportation
	 * creates 
	 * @param outstockData
	 * @return
	 * @throws WrongInputException
	 * @throws WrongDataException
	 * @throws UserErrorException
	 */

	
	private int pickOutstock(int warehouseId, int outstockId, String serialNo, List<String> packages) throws WrongInputException, WrongDataException, UserErrorException {
		
		
		if (serialNo != null && (serialNo.length() < 3 || !InputSanitizer.isPlainText(serialNo)))
			throw new WrongInputException();
		
		OutStock outstock = outstockDao.get(outstockId);
		
		if (outstock == null) {
			logError(AGENDA_NAME, "pick outstock", "outstock not found, id:" + outstockId);
			throw new WrongInputException();
		}
		
		if (outstock.getInStockId() != null && outstock.getStockSetId() != null) {
			logError(AGENDA_NAME, "pick outstock", "outstock has both instockId and stockSetId, id:" + outstockId);
			throw new WrongDataException();
		}
		
		if (outstock.getInStockId() == null && (serialNo == null || serialNo.isEmpty())) {
			logError(AGENDA_NAME, "pick outstock", "outstock has neither instockId nor serialNo, id:" + outstockId);
			throw new WrongDataException();
		}
		
		
		InStock instock; 
		boolean serial = outstock.getInStockId() == null;
		if (serial) 
			instock = instockDao.findFreeBySerialNo(serialNo, warehouseId); 
		else
			instock = instockDao.get(outstock.getInStockId());
		
		if (instock == null) {
			logError(AGENDA_NAME, "pick outstock", "instock not found, serial no:" + serialNo + ", instockId: " + outstock.getInStockId());
			throw new WrongInputException();
		}
		
		boolean available = serial ? instockDao.checkAvailabilitySerial(instock.getId()) : instockDao.checkAvailabilityNonSerial(instock.getId());
		if (!available)	{	
			logError(AGENDA_NAME, "pick outstock", "instock already used:" + instock.getId());
			throw new UserErrorException();
		}
		
		Integer deliveryId;
		if (outstock.getStockSetId() == null) {
			
			/* outstock was allocated to instock */			
			deliveryId = allocateOutstockToInstock(outstock, instock, packages);
		} else {
			
			/* outstock was allocated to stockset, no need to check for existence of stockset, database makes sure */
			deliveryId = allocateOutstockToStockSet(outstock, instock, packages);
		} 
	
		
		return deliveryId;
	}
	
	
	/**
	 * allocates outstock to instock
	 * it checks if outstock's orderitem product matches instock product (via stockitem)
	 * @param outstock
	 * @param instock
	 * @param product
	 * @throws WrongDataException
	 * @throws WrongInputException 
	 */
	private Integer allocateOutstockToInstock(OutStock outstock, InStock instock, List<String> packages) throws WrongDataException, WrongInputException {
		
		Product product = productDao.findForOrderItem(outstock.getOrderItemId());
		if (product == null) {
			logError(AGENDA_NAME, "pick outstock", "product not found for outstock :" + outstock.getId());
			throw new WrongInputException();
		}
		

		if (product.getStockItemId().intValue() != instock.getStockItemId().intValue()) {
			logError(AGENDA_NAME, "pick outstock", "outstock product doesn't match instock, id:" + outstock.getId());
			throw new WrongDataException();
		}
		outstock.setInStockId(instock.getId());
		outstockDao.saveOrUpdate(outstock);		
		
		return createDeliveryForOutstock(outstock, packages);
	}
	

	/**
	 * allocates outstock to StockSet
	 * it checks if outstock's orderitem product matches stockset product
	 * @param outstock
	 * @param instock
	 * @param product
	 * @throws WrongInputException
	 * @throws WrongDataException 
	 */
	private Integer allocateOutstockToStockSet(OutStock outstock, InStock instock, List<String> packages) throws WrongInputException, WrongDataException {
		
		StockSet stockSet = stockSetDao.get(outstock.getStockSetId());
		if (stockSet == null) {
			logError(AGENDA_NAME, "pick outstock", "stockset not found, id:" + outstock.getStockSetId());
			throw new WrongInputException();
		}
		
		
		StockItem stockItem = stockItemDao.findForOrderItem(outstock.getOrderItemId());
		if (stockItem.getId() != instock.getStockItemId()) {
			logError(AGENDA_NAME, "pick outstock", "outstock product doesn't match instock, id:" + outstock.getId());
			throw new WrongInputException();
		}
				
		StockSetItem ssi = stockSetItemDao.findEmpty(stockSet.getId());
		if (ssi == null) {
			logError(AGENDA_NAME, "pick outstock", "stockset item not found, id:" + stockSet.getId());
			throw new WrongInputException();
		}
		ssi.setInStockId(instock.getId());
		stockSetItemDao.saveOrUpdate(ssi);
		
		return createDeliveryForOutstock(outstock, packages);
		
	}

	/**
	 * creates delivery record for picked outstock
	 * necessary number of packages are created with delivery
	 * @param outstock
	 * @throws WrongDataException
	 */
	private Integer createDeliveryForOutstock(OutStock outstock, List<String> packages) throws WrongDataException {
		
		OrderItem orderItem = orderItemDao.get(outstock.getOrderItemId());		
		if (orderItem == null) {
			logError(AGENDA_NAME, "deliveryForOutstoci", "orderitem not found for id:" + outstock.getId());
			throw new WrongDataException();
		}
		
		/* check for existence of delivery for given order or create one if needed */
		Delivery delivery = deliveryDao.getByOrderId(orderItem.getOrderId());
		if (delivery == null) {
			delivery = new Delivery(getCompanyId(), orderItem.getOrderId(), Delivery.TYPE_OUT);
			deliveryDao.saveOrUpdate(delivery);
			
		}
		
		
		
		/* generate delivery item for every orderitem in outstocks - outstock will potentially be grouped then by orderItemId */
		int sum = 0;
		List<OutStock> siblings = outstockDao.getSiblings(orderItem.getId());
		for (OutStock one:siblings)
			sum += one.getQty();
		
		/* if outstocks covered all orderItem qty, make delivery */
		if (sum == orderItem.getQty()) {
			
			DeliveryItem deliveryItem = new DeliveryItem(delivery.getId(), getUser().getId());
			deliveryItemDao.saveOrUpdate(deliveryItem);
			
			/* link all outstocks to delivery item */
			for (OutStock one:siblings) {
				one.setDeliveryItemId(deliveryItem.getId());
				outstockDao.saveOrUpdate(one);
			}
		} else
			return null;
		
		return delivery.getId();
	}

	
	public ResponseObject pickStockItem(Map<String,Object> pickData) throws WrongInputException {
	
		int warehouseId = (int)pickData.get("warehouseId");
		int orderId = (int)pickData.get("orderId");
		String barCode = (String)pickData.get("barCode");
		if (!InputSanitizer.isPlainText(barCode))
			throw new WrongInputException();
		
		InStock instock = instockDao.findFreeBySerialNo(barCode, warehouseId);
		List<PickStockItemDto> list = stockItemDao.getPickItems(orderId);
		
		List<Integer> orderItemIds = null;
		int idToFind;
		if (instock != null) {

			idToFind = instock.getStockItemId();
		} else {
			try {
				/* parse stockitemid */
				idToFind = Integer.parseInt(barCode);
			} catch (NumberFormatException e) {
				throw new WrongInputException();
			}
		}
		
		orderItemIds = findOrderItems(list, idToFind);			
		return buildResponse("pickedStockItems", orderItemIds);
		
		
	}

	private List<Integer> findOrderItems(List<PickStockItemDto> list, int id) {
		List<Integer> orderItemIds = new ArrayList<Integer>();
		for (PickStockItemDto one:list) {
			if (one.getSi1id() != null && one.getSi1id() == id || 
					one.getSi2id() != null && one.getSi2id() == id)
				orderItemIds.add(one.getOrderItemId());
		}
		return orderItemIds;
	}

	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = Exception.class)
	public ResponseObject addDelivery(Map<String, Object> deliveryData) throws WrongInputException, WrongDataException, UserErrorException {
		
		//int orderId = (int)deliveryData.get("orderId");
		List<String> packages = (List<String>)deliveryData.get("packages");		
		int warehouseId = (int)deliveryData.get("warehouseId");		
		List<Map<String,Object>> items = (List<Map<String,Object>>)deliveryData.get("items");
		Integer deliveryId = null;
		for (Map<String,Object> item:items) {
			
			boolean hasSerialNo = (boolean)item.get("hasSerialNo");
			int outstockId = (int)item.get("outstockId");
			
			if (hasSerialNo) {
				List<String> serialNos = (List<String>)item.get("picked");
				for (String serialNo:serialNos) {
					int newDeliveryId = pickOutstock(warehouseId, outstockId, serialNo, packages);
					if (deliveryId != null && deliveryId != newDeliveryId)
						throw new WrongDataException();
					deliveryId = newDeliveryId;
				}
			} else
				deliveryId = pickOutstock(warehouseId, outstockId, null, packages);
		}
		
		
		linkPackagesToDelivery(deliveryId, packages);
		
		return buildResponseOk();
	}
	
	private void linkPackagesToDelivery(int deliveryId, List<String> packages) throws WrongDataException {
		
		for (String one: packages) {
			DeliveryPackage dp = deliveryPackageDao.getByPackageNo(one);
			if (dp == null)
				throw new WrongDataException();
			dp.setDeliveryId(deliveryId);
			deliveryPackageDao.saveOrUpdate(dp);
			
		}
	}
	



	/**
	 * returns individual instocks for selected delivery
	 * used to confirm return of goods back to stock
	 * @param returnData
	 * @return
	 * @throws WrongDataException
	 */

	public ResponseObject getDeliveryDetails(int deliveryId) throws WrongDataException {
		
		List<DeliveryItemDto> items = deliveryDao.getOne(deliveryId);
		List<InStockDto> instocks = new ArrayList<InStockDto>();
		for(DeliveryItemDto one:items) {
			int deliveryItemId = one.getId();
			OutStock outstock = outstockDao.getByDelivery(deliveryItemId);
			Integer stockSetId = outstock.getStockSetId();
			if (outstock.getInStockId() != null) {
				/* non-set item */
				InStockDto instock = setupInstock(outstock.getInStockId(), one.getQty(), deliveryItemId);
				instocks.add(instock);
			} else if (stockSetId != null) {
				
				/* set item */
				List<StockSetItem> stockItems = stockSetItemDao.findBySetId(stockSetId);
				for (StockSetItem ssItem: stockItems) {
					InStockDto instock = setupInstock(ssItem.getInStockId(), one.getQty(), deliveryItemId);
					instocks.add(instock);
				}
			}
		}
		ResponseData result = responseDataResult("oneDelivery", instocks);
		result.put("deliveryId", deliveryId);
		return buildResponse(result);
	}
	
	
	

	private InStockDto setupInstock(Integer inStockId, int qty, int deliveryItemId) {
		InStockDto instock = instockDao.getDto(inStockId);
		instock.setQty(qty);
		instock.setDeliveryItemId(deliveryItemId);
		return instock;
	}



	/**
	 * returns item to stock, creates instock linked to returned delivery
	 * @param returnData - target warehouseId + list of maps: qty, instockId and deliveryItemId
	 * @return
	 * @throws WrongDataException
	 * @throws WrongInputException 
	 */
	@SuppressWarnings("unchecked")
	public ResponseObject returnToStock(Map<String, Object> returnData) throws WrongDataException, WrongInputException {
		
		Date taxDate = (Date)returnData.get("taxDate");
// TODO
taxDate = new Date();

		Integer deliveryId = (Integer)returnData.get("deliveryId");
		int warehouseId = (int)returnData.get("warehouseId");
		int userId = getUserId();
		
		Delivery delivery = deliveryDao.get(deliveryId);
		if (delivery == null)
			throw new WrongInputException();
		Order order = orderDao.get(delivery.getOrderId());
		Partner partner = partnerDao.get(order.getPartnerId());
		
		
		List<Map<String, Integer>> returns = (List<Map<String, Integer>>)returnData.get("returned");
		Map<Integer,Integer> deliveryItemIds = new HashMap<Integer,Integer>();
		
		try {
			for(Map<String, Integer> one:returns) {
				/* one return - qty, instockId and deliveryItemId */
				int qty = one.get("qty");				
				if (qty <= 0)
					continue;
				
				int instockId = one.get("instockId");
				int deliveryItemId = one.get("deliveryItemId");
				deliveryItemIds.put(deliveryItemId, qty);
				
				List<OutStock> outstocks = (List<OutStock>) outstockDao.getAllByDelivery(deliveryItemId);
				checkReturnQty(qty, outstocks);
				
				InStock instock = instockDao.get(instockId);				
				if (instock == null)
					throw new WrongDataException();
				InStock newInstock = new InStock(warehouseId, userId, instock.getStockItemId(), qty, instock.getSerialNo(), "vraceno");
				newInstock.setDeliveryItemId(deliveryItemId);
				instockDao.saveOrUpdate(newInstock);
			
				
			}
			
			/* create credit note if needed */
			financeAgenda.createCreditNote(deliveryItemIds, taxDate, partner.getCountryId());
			
		} catch(WrongInputException e) {
			throw new WrongDataException();
		}
		
		
		return buildResponseOk();
	}



	/**
	 * checks if returned amount is smaller or equal to total related outstock amount
	 * @param qty
	 * @param outstocks
	 * @throws WrongInputException
	 */
	private void checkReturnQty(int qty, List<OutStock> outstocks) throws WrongInputException {
		
		int sum = 0;
		for (OutStock one:outstocks)
			sum += one.getQty();
		if (sum > qty)
			throw new WrongInputException();
		
		
	}


	
}
