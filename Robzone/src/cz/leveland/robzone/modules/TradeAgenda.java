package cz.leveland.robzone.modules;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cz.leveland.appbase.database.exceptions.WrongInputException;
import cz.leveland.appbase.util.InputSanitizer;
import cz.leveland.appbase.util.ResponseObject;
import cz.leveland.robzone.api.APIConnectionException;
import cz.leveland.robzone.api.APIParseException;
import cz.leveland.robzone.api.impl.OrderApiConnector;
import cz.leveland.robzone.api.impl.OrderApiContentParser;
import cz.leveland.robzone.database.entity.Order;
import cz.leveland.robzone.database.entity.OrderChange;
import cz.leveland.robzone.database.entity.OrderItem;
import cz.leveland.robzone.database.entity.Partner;
import cz.leveland.robzone.database.entity.PkVatRate;
import cz.leveland.robzone.database.entity.Product;
import cz.leveland.robzone.database.entity.ProductSetItem;
import cz.leveland.robzone.database.entity.UnknownProduct;
import cz.leveland.robzone.database.entity.VatRate;
import cz.leveland.robzone.database.entity.dto.OrderDto;
import cz.leveland.robzone.database.entity.dto.ProductSetItemDto;
import cz.leveland.robzone.database.entity.dto.Progress;
import cz.leveland.robzone.database.impl.DiscountItemDaoImpl;
import cz.leveland.robzone.database.impl.OrderChangeDaoImpl;
import cz.leveland.robzone.database.impl.OrderDaoImpl;
import cz.leveland.robzone.database.impl.OrderItemDaoImpl;
import cz.leveland.robzone.database.impl.PartnerDaoImpl;
import cz.leveland.robzone.database.impl.ProductDaoImpl;
import cz.leveland.robzone.database.impl.ProductSetItemDaoImpl;
import cz.leveland.robzone.database.impl.UnknownProductDaoImpl;
import cz.leveland.robzone.database.impl.VatRateDaoImpl;
import cz.leveland.robzone.exception.CCFilterException;
import cz.leveland.robzone.exception.OrderNotFoundException;
import cz.leveland.robzone.exception.OrderProcessingException;
import cz.leveland.robzone.exception.OverAllocationException;
import cz.leveland.robzone.exception.StockAllocationException;
import cz.leveland.robzone.exception.WrongDataException;
import cz.leveland.robzone.functionblocks.filters.StatusOrderFilter;
import cz.leveland.robzone.provider.DiscountClasses;
import cz.leveland.robzone.pusher.PushManager;
import cz.leveland.robzone.setup.CCFilter;
import cz.leveland.robzone.util.MapCounter;
import cz.leveland.robzone.xml.OrderImportConverter;

@Service
public class TradeAgenda extends Agenda{

	
	@Autowired
	OrderDaoImpl orderDao;

	@Autowired
	OrderItemDaoImpl orderItemDao;
	
	@Autowired
	UnknownProductDaoImpl unknownProductDao;
	
	@Autowired
	StatusOrderFilter orderFilter;
	
	@Autowired
	StockAgenda stockAgenda;

	@Autowired
	PartnerAgenda partnerAgenda;
	
	@Autowired
	FinanceAgenda financeAgenda;
	
	@Autowired
	APIAgenda apiAgenda;
	
	@Autowired
	OrderApiConnector orderApiConnector;
	
	@Autowired
	TransactionComponents transactionComponents;
		
	@Autowired
	ProductDaoImpl productDao;

	@Autowired
	PartnerDaoImpl partnerDao;
	
	@Autowired
	OrderChangeDaoImpl orderChangeDao;
	
	@Autowired
	VatRateDaoImpl vatRateDao;
	
	@Autowired
	ProductSetItemDaoImpl productSetItemDao;
	
	@Autowired
	DiscountClasses discountClasses;
	
	@Autowired
	DiscountItemDaoImpl discountItemDao;
	
	@Autowired
	CatalogueDataProvider catalogueDataProvider;
	
	@Autowired
	CCFilter ccFilter;
	
	@Autowired
	PushManager pushManager;
	
	
	
	public static final boolean PROCESS_ORDER_UPON_CREATION = true;

	private static final String AGENDA_NAME = "trade";

	private static final String[] EMAILS_TO_CHECK = {"email"};
	List<UnknownProduct> unknownProducts = new ArrayList<UnknownProduct>();
	
	
	/*
	@PostConstruct
	public void setup() {
		
	}
	*/
	
	public void setupProviders(int companyId) {

		/*
		catalogueDataProvider.readData(companyId);
		
		((OrderApiContentParser)orderApiConnector.getContentParser())
			.getImportConverter()
			.setConversionData(catalogueDataProvider.getOrderConversionData(companyId));
		*/	
	}
	
	public ResponseObject getProducts() {
		
		return buildResponse("products", productDao.getAll(getCompanyId(), ccFilter.getCurrentCountries(getUserId())));
	}
	public ResponseObject getServiceProducts() {
		
		return buildResponse("serviceProducts", productDao.getServiceProducts(getCompanyId(), ccFilter.getCurrentCountries(getUserId())));
	}

	public ResponseObject getProductSetItems(int productId) {		
		return buildResponse("productSetItems", productSetItemDao.getByProductId(productId));
	}

	
	public ResponseObject getCatalogueData() {		
		return buildResponse("catalogue", catalogueDataProvider.getData(getCompanyId()));
	}

	/**
	 * Gets list of pending orders
	 * orders with at least one item with no record in OutStock
	 * 
	 * @param String filter type
	 * @return list of orders with items which have not been picked yet
	 */
	
	public ResponseObject getOrders(Integer filter) {
		
		return buildResponse("orders", getOrderList());
	}
	
	public ResponseObject getOrderChanges(int orderId) {
		
		return buildResponse("orderChanges", orderChangeDao.getByOrderId(orderId));
	}
	
	public List<OrderDto> getOrderList() {
		
		
		List<OrderDto> orders = orderDao.getPending(getCompanyId(), getCountryIds());
		orderFilter.filter(orders);
		
		return orders;
	}
	
	public ResponseObject getOrderItems(int orderId) {
		
		return buildResponse("orderItems", orderItemDao.getByOrderIdDto(orderId));
	}
	

	/**
	 * called by client
	 * adds an item to a product set
	 * @param setItemData
	 * @return list of product set items
	 * @throws WrongInputException 
	 */
	public ResponseObject addSetItem(Map<String, Object> setItemData) throws WrongInputException {

		Integer productId = (Integer)setItemData.get("productId");
		Integer stockItemId = (Integer)setItemData.get("stockItemId");			
		Integer qty = ((Number)setItemData.get("qty")).intValue();
		
		if (productId == null || productId <= 0 || stockItemId == null || stockItemId == 0 || qty == null || qty <= 0)
			throw new WrongInputException();
		
		ProductSetItem setItem = new ProductSetItem(productId, stockItemId, qty);
		try {
			productSetItemDao.saveOrUpdate(setItem);
		} catch (Exception e) {
			logError(AGENDA_NAME, "addProduct", "product set item couldn't be saved, stockItemId:" + stockItemId + ", sitem: " + stockItemId);
			throw new WrongInputException();
		}
		return getProductSetItems(productId);
	}

	public ResponseObject deleteSetItem(Map<String, Object> setItemData) throws WrongInputException {
		Integer productSetItemId = (Integer)setItemData.get("productSetItemId");
		Integer productId = (Integer)setItemData.get("productId");
		
		if (productSetItemId == null || productSetItemId <= 0 || productId == null || productId <= 0)
			throw new WrongInputException();
		
		
		try {
			ProductSetItem item = productSetItemDao.get(productSetItemId);
			if (item.getProductId() != productId)
				throw new WrongInputException();

			productSetItemDao.delete(productSetItemId);
			
		} catch (Exception e) {
			logError(AGENDA_NAME, "addProduct", "product set item couldn't be deleted, productSetItemId:" + productSetItemId);
			throw new WrongInputException();
		}
		return getProductSetItems(productId);
	}

	
	

	/**
	 * imports batch of orders from XML
	 * @return
	 * @throws WrongInputException 
	 * @throws APIParseException 
	 * @throws APIConnectionException 
	 * @throws CCFilterException 
	 * @throws Exception
	 */
	
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = Exception.class)
	public ResponseObject importOrders(Map<String, Object> importData) throws APIConnectionException, APIParseException, WrongInputException, CCFilterException {

		int warehouseId = (int)importData.get("warehouseId");
		Integer countryId = ccFilter.getOneCurrentCountry(getUserId());
		if (countryId == null)
			throw new CCFilterException();
		
		/* temp to avoid necessity of login */
		catalogueDataProvider.readData(getCompanyId());		
		
		OrderApiContentParser parser = (OrderApiContentParser)orderApiConnector.getContentParser();
		OrderImportConverter converter = parser.getImportConverter();
		converter.setConversionData(catalogueDataProvider.getOrderConversionData(getCompanyId()));
		converter.setProducts(productDao.getProductMap(getCompanyId(), countryId));
		converter.setWarehouseId(warehouseId);
		

		

		unknownProductDao.deleteAll(getCompanyId());
		unknownProducts.clear();

		
		stockAgenda.updateAvailable(warehouseId);
		
		Map<String,Object> importArgs = importArgs("countryId", countryId);
		List<Map<String,Object>> orders = (List<Map<String, Object>>) orderApiConnector.getData(importArgs);
		List<ActionResult> errors = new ArrayList<ActionResult>();
		
		int cnt = 0, good = 0; ;
		for (Map<String,Object> one:orders) {			
			try {				
				ActionResult addResult = null;
				List<UnknownProduct> unknownList = (List<UnknownProduct>)one.get("unknownProducts");
				Order order = (Order)one.get("order");
				unknownProducts.addAll(unknownList);
				if (unknownList.isEmpty()) { 
					addResult = transactionComponents.addNewOrder(one, warehouseId);
					good++;
				} else {
					errors.add(new ActionResult(ActionResult.TYPE_ERROR, 0, "Neznámý produkt v objednávce " + order.getOrderNo()));
				}
				
				cnt++;
				
			} catch (Exception e) {		
				e.printStackTrace();
				ActionResult error = new ActionResult(ActionResult.TYPE_ERROR, "?", "Chybná data objednávky, cnt: " + cnt);
				errors.add(error);
			}
			pushManager.reportProgress(new Progress(orders.size(), cnt,good,errors.size()), getCompanyId());
		}
		
		saveUnkownProducts();
		
		Map<String,Object> result = responseDataResult("orders", getOrderList());
		result.put("errors", errors);
		result.put("unknownProducts", unknownProducts);
		
		return buildResponse(result,STATUS_OK);
	}
	
	
	
	
	

	private void saveUnkownProducts() {

		int cnt=0;
		for(UnknownProduct one:unknownProducts) {  
			unknownProductDao.saveOrUpdate(one);
			if (cnt++ % 20 == 0)
				unknownProductDao.flushAndClear();
		}
		
	}
	
	public ResponseObject recalculateOrderItems(Map<String, Object> orderData) throws WrongInputException {
		
		int orderId = (int)orderData.get("orderId");
		MapCounter mapCounter = new MapCounter();
		List<OrderItem> orderItems = orderItemDao.getByOrderId(orderId);
		for (OrderItem one: orderItems) {
			Product product = productDao.get(one.getProductId());
			one.setDiscountClassId(product.getDiscountClassId());
			if (product.isKeyProduct() && product.getDiscountClassId() != null)
				mapCounter.up(product.getDiscountClassId(), (int)one.getQty());
		}
		double discount;
		for (OrderItem one: orderItems) {
		
			Integer classId = one.getDiscountClassId();
			if (classId == null)
				discount = 0;
			else
				discount = discountClasses.getDiscount(classId, mapCounter.get(classId));			
			one.calculateDiscount(discount);
			orderItemDao.saveOrUpdate(one);
		}
		
		return buildResponseOk();
	}
	
	public ResponseObject addOrderItemFromSet(Map<String, Object> setData) throws WrongInputException {
		
		int productId = (int)setData.get("productId");
		int orderId = (int)setData.get("orderId");
		Product product = productDao.get(productId);
		if (product == null)
			throw new WrongInputException("Wrong input data");
		
		
		List<ProductSetItemDto> setItems = productSetItemDao.getByProductId(productId);
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		for (ProductSetItemDto one: setItems) {
			Product itemProduct = productDao.getByStockItemId(one.getStockItemId());
			OrderItem newItem = new OrderItem(orderId, itemProduct.getId(), null, one.getQty(), itemProduct.getPrice(), 0.0, 0.0);
			orderItemDao.saveOrUpdate(newItem);
			orderItems.add(newItem);
		}
		return buildResponse("orderItems", orderItems);
	}

	/**
	* 
	* @param itemData inserted order item
	* @return new list of order items
	* @throws WrongInputException
	*/
	public ResponseObject addOrderItem(Map<String, Object> itemData) throws WrongInputException {
		
		int orderId = (int)itemData.get("orderId");
		Integer productId = (Integer)itemData.get("productId");
		
		
		String description = (String)itemData.get("description");
		int vatCategory = (int)itemData.get("vatCategory");
		
		int qty = (int)itemData.get("qty");
		//int currencyId = (int)itemData.get("currencyId");
		Double unitPrice = ((Number)itemData.get("unitPrice")).doubleValue();
		Double discountPercent = getMapDoubleZero(itemData, "discountPercent");
		
		if (orderId <= 0
				|| productId != null && productId.intValue() <= 0
				|| (productId == null  && (description==null || description.length() < 2)) 
				|| qty <= 0 || unitPrice==null || unitPrice < 0.0 || discountPercent == null)
			throw new WrongInputException("Wrong input data");
		
		double discount = discountPercent/100 * unitPrice; 
		OrderItem newItem = new OrderItem(orderId, productId, description, qty, unitPrice, discountPercent, discount);

		/* if manual add, then add vatrate */
		double vr = 0.0;
		if (productId == null) {
			PkVatRate pk = new PkVatRate(getCompanyId(), vatCategory, ccFilter.getOneCurrentCountry(getUserId()));
			VatRate vatRate = vatRateDao.get(pk);
			newItem.setVatRate(vatRate.getRate());
		}
										
		try {
			orderItemDao.saveOrUpdate(newItem);
			String itemName = null;
			if (productId != null) {
				Product product = productDao.get(newItem.getProductId());
				if (product == null)
					throw new WrongInputException();
				itemName = product.getName();
			} else 
				itemName = description;
			
			OrderChange change = new OrderChange(orderId, newItem.getId(), getUser().getId(), "nová položka", "", newItem.getQty() + " ks, " + itemName);
			orderChangeDao.saveOrUpdate(change);
		} catch (Exception e) {
e.printStackTrace();			
			logError(AGENDA_NAME, "addOrderItem", "order item couldn't be saved, order id " + orderId);
			throw new WrongInputException();
		}
		return getOrderItems(orderId);
	}

	
	
	/**
	 * 
	 * @param itemData updates existing order item
	 * @return new list of order items
	 * @throws WrongInputException
	 */
	
	public ResponseObject updateOrderItem(Map<String, Object> itemData) throws WrongInputException {
		
		int id = (int)itemData.get("id");		
		int productId = (int)itemData.get("productId");
		int qty = (int)itemData.get("qty");
		Double unitPrice = getMapDoubleZero(itemData, "unitPrice");
		Double discountPercent = getMapDoubleZero(itemData, "discountPercent");
		
		if (id <= 0 || productId <= 0 || qty <= 0 || discountPercent == null)
			throw new WrongInputException("Wrong input data");
		
		
		OrderItem orderItem = orderItemDao.get(id);
		if (orderItem == null) {
			logError(AGENDA_NAME, "update item", "Order item " + id + " not found");
			throw new WrongInputException("Wrong input data");
		}
		List<OrderChange> changes = orderItem.update(getUser().getId(), productId, qty, unitPrice, discountPercent);
		//orderItem.setPrice((double)qty * (unitPrice-discount));
		
		try {
			orderItemDao.saveOrUpdate(orderItem);
			for (OrderChange one:changes)
				orderChangeDao.saveOrUpdate(one);

		} catch (Exception e) {
			logError(AGENDA_NAME, "addOrderItem", "order item couldn't be saved, order item id " + id);
			throw new WrongInputException();
		}
		return getOrderItems(orderItem.getOrderId());
	}
	
	
	
	
	/**
	 * 
	 * @param itemData id of item to be deleted and its order id (for safety double check) 
	 * @return new list of order items
	 * @throws WrongInputException
	 * @throws WrongDataException 
	 */
	
	public ResponseObject deleteOrderItem(Map<String, Object> itemData) throws WrongInputException, WrongDataException {
		
		int orderId = (int)itemData.get("orderId");
		int orderItemId = (int)itemData.get("orderItemId");
		
		
		if (orderId <= 0 || orderItemId <= 0) {
			logError(AGENDA_NAME, "deleteOrderItem", "wrong order id or orderItemId:" + orderId + ", " + orderItemId);
			throw new WrongInputException();
		}
		
		/* safety check - compare given orderid and order item's orderid*/
		OrderItem orderItem = orderItemDao.get(orderItemId);
		if (orderItem.getOrderId() != orderId)
			throw new WrongInputException();
		
		Product product = productDao.get(orderItem.getProductId());
		if (product == null)
			throw new WrongDataException();
		OrderChange change = new OrderChange(orderId, orderItem.getId(), getUser().getId(), "smazání položky",  orderItem.getQty() + " ks, " + product.getName(), "");
		orderChangeDao.saveOrUpdate(change);

		//orderChangeDao.deleteByOrderItemId(orderItemId);		
		orderItemDao.delete(orderItemId);
		return getOrderItems(orderId);
	}

	public ResponseObject getOrder(int orderId) {
		
		return buildResponse("order", orderDao.getDto(orderId, getCompanyId()));
	}


	/**
	 * adds new customer order
	 * @param orderData
	 * @return
	 * @throws WrongInputException
	 * @throws CCFilterException 
	 */
	@Transactional(rollbackFor = Exception.class)
	public ResponseObject addOrder(Map<String, Object> orderData)  throws WrongInputException, CCFilterException {
		
		Integer countryId = ccFilter.getOneCurrentCountry(getUserId());
		if (countryId == null)
			throw new CCFilterException();
		
		String checkResult = InputSanitizer.isMapPlainText(orderData, EMAILS_TO_CHECK);
		if (checkResult.length() > 0) 			
			throw new WrongInputException();
		
		int companyId = getCompanyId();
		String orderNo = (String)orderData.get("orderNo");
		
		if (orderNo == null)
			throw new WrongInputException();
		OrderDto existing = orderDao.getDto(orderNo, companyId);
		if (existing != null)
			throw new WrongInputException();
		
		
		int warehouseId = (int)orderData.get("warehouseId");
		String name = (String)orderData.get("name");
		String familyName = (String)orderData.get("familyName");
		String regNo = (String)orderData.get("regNo");
		String taxNo = (String)orderData.get("taxNo");
		String email = (String)orderData.get("email");
		String phone = (String)orderData.get("phone");
		String city = (String)orderData.get("city");
		String street = (String)orderData.get("street");
		String zip = (String)orderData.get("zip");
		
		Partner partner = new Partner(countryId, Partner.TYPE_CUSTOMER, name, familyName, regNo, taxNo, city, street, zip, phone, email);
		
		partnerAgenda.addPartner(partner);
		
		Order order = new Order(new Date(), orderNo, warehouseId,
				catalogueDataProvider.getCurrencyId(getCompanyId(), countryId),
				partner.getId(), partner.getId());
		orderDao.saveOrUpdate(order);
		OrderDto readOrder = orderDao.getDto(order.getId(), getCompanyId());
		return buildResponse("order", readOrder);
		
	}
	
	
	/**
	 * updates attributes of order
	 * @param orderData
	 * @return
	 * @throws WrongInputException
	 */
	@Transactional(rollbackFor = Exception.class)
	public ResponseObject updateOrder(Map<String, Object> orderData) throws WrongInputException {

		int orderId = (int)orderData.get("id");
		if (orderId <= 0) {
			logError(AGENDA_NAME, "updateOrder", "wrong order id:" + orderId);
			throw new WrongInputException();
		}
		
		Order order = orderDao.get(orderId);
		if (order == null) {
			logError(AGENDA_NAME, "updateOrder", "order not found, id:" + orderId);
			throw new WrongInputException();
		}
		
		Partner partner = partnerDao.get(order.getPartnerId());
		if (partner == null) {
			logError(AGENDA_NAME, "updateOrder", "partner not found for order oid, pid:" + orderId + ", " + order.getPartnerId());
			throw new WrongInputException();
		}
		
		
		String orderNo = (String)orderData.get("orderNo");
		String name = (String)orderData.get("name");
		String familyName = (String)orderData.get("familyName");
		String regNo = (String)orderData.get("regNo");
		String taxNo = (String)orderData.get("taxNo");
		String email = (String)orderData.get("email");
		String phone = (String)orderData.get("phone");
		String city = (String)orderData.get("city");
		String street = (String)orderData.get("street");
		String zip = (String)orderData.get("zip");
		
		
		List<OrderChange> orderChanges = order.update(getUser().getId(), order.getId(), orderNo);
		List<OrderChange> partnerChanges = partner.update(getUser().getId(), order.getId(), name, familyName, regNo, taxNo, email, phone, city, street, zip);
		orderChanges.addAll(partnerChanges);
		
		orderDao.saveOrUpdate(order);
		partnerDao.saveOrUpdate(partner);
		
		for (OrderChange one:orderChanges)
			orderChangeDao.saveOrUpdate(one);
		orderDao.flushAndClear();
		
		return getOrder(orderId);
	}
	
	
	
	
	/**
	 * called from finance module when new payment or credit arrives
	 * attempt to process order whenever new information arrives
	 * when payment, credit or anything else is changed which may trigger order to be allocated
	 * @param orderId ID of order
	 * @throws OrderNotFoundException 
	 * @throws OverAllocationException 
	 * @throws StockAllocationException 
	 * @throws Exception 
	 */
	public void tryToProcessOrder(String orderNo, Integer paymentTypeId) throws OrderProcessingException, OrderNotFoundException, OverAllocationException, StockAllocationException {
		int[] types = new int[1];
		types[0] = paymentTypeId;
		tryToProcessOrder(orderNo, types);
	}
	
	/**
	 * called from finance module when new payment or credit arrives
	 * attempt to process order whenever new information arrives
	 * when payment, credit or anything else is changed which may trigger order to be allocated
	 * @param orderId ID of order
	 * @param paymentTypes array of payment types which trigger processing
	 * @throws StockAllocationException 
	 * @throws OrderProcessingException, OrderNotFoundException 
	 * @throws OverAllocationException 
	 * @throws Exception 
	 */
	public void tryToProcessOrder(String refNo, int[] paymentTypes) throws OrderNotFoundException, StockAllocationException  {
		
		OrderDto order = orderDao.getDto(refNo, getCompanyId());
		if (order == null) {
			logError(AGENDA_NAME, "tryToProcessOrder", "order couldn't be found, order no:" + refNo);
			throw new OrderNotFoundException();
		}
		
		boolean goOn = false;
		for(int type:paymentTypes)
			if (type == order.getPaymentTypeId())
				goOn = true;
		
		if (goOn) {			
			List<OrderItem> orderItems = orderItemDao.getByOrderId(order.getId());
			transactionComponents.processOrder(order, orderItems, order.getWarehouseId());
		}
	}
	
	
	
	
	
	/* ------- client -----*/
	
	
	/**
	 * called by client, only orderId is provided as parameter in orderData
	 * @param orderData
	 * @return
	 * @throws WrongInputException
	 * @throws StockAllocationException
	 */
	public ResponseObject processOrder(Map<String, Object> orderData) throws WrongInputException, StockAllocationException {
		
		int orderId = (int)orderData.get("orderId");
		
		if (orderId <= 0) {
			logError(AGENDA_NAME, "processOrder", "order couldn't be found, order id:" + orderId);
			throw new WrongInputException();
		}
		
		
		OrderDto order = orderDao.getDto(orderId, getCompanyId());
		stockAgenda.updateAvailable(order.getWarehouseId());
		List<OrderItem> orderItems = orderItemDao.getNonAllocatedByOrderId(orderId);
		transactionComponents.processOrder(order, orderItems, order.getWarehouseId());
		
		return getOrders(null);
	}

	
	/**
	 * TODO - temporary
	 * 
	 * direct process of order
	 * @param orderId
	 * @return
	 * @throws WrongInputException
	 * @throws StockAllocationException
	 */
	public void processOrder(int orderId) throws WrongInputException, StockAllocationException {
		
		OrderDto order = orderDao.getDto(orderId, getCompanyId());		
		List<OrderItem> orderItems = orderItemDao.getNonAllocatedByOrderId(orderId);
		transactionComponents.processOrder(order, orderItems, order.getWarehouseId());
	}


	public ResponseObject getUnknownProducts() {
		return buildResponse("unknownProducts", unknownProductDao.getAll(getCompanyId()));
	}

	
	
}
