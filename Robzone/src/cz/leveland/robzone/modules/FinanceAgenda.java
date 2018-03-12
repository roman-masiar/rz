package cz.leveland.robzone.modules;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cz.leveland.appbase.database.exceptions.WrongInputException;
import cz.leveland.appbase.util.ResponseData;
import cz.leveland.appbase.util.ResponseObject;
import cz.leveland.robzone.api.APIConnectionException;
import cz.leveland.robzone.api.APIParseException;
import cz.leveland.robzone.api.AbstractApiSource;
import cz.leveland.robzone.api.impl.CreditApiConnector;
import cz.leveland.robzone.api.impl.CreditFileApiSource;
import cz.leveland.robzone.api.impl.PaymentApiConnector;
import cz.leveland.robzone.database.entity.Credit;
import cz.leveland.robzone.database.entity.Invoice;
import cz.leveland.robzone.database.entity.InvoiceItem;
import cz.leveland.robzone.database.entity.Order;
import cz.leveland.robzone.database.entity.Partner;
import cz.leveland.robzone.database.entity.Payment;
import cz.leveland.robzone.database.entity.ProductType;
import cz.leveland.robzone.database.entity.dto.CreditDto;
import cz.leveland.robzone.database.entity.dto.ForInvoiceDto;
import cz.leveland.robzone.database.entity.dto.OrderDto;
import cz.leveland.robzone.database.entity.dto.PaymentDto;
import cz.leveland.robzone.database.entity.dto.Progress;
import cz.leveland.robzone.database.impl.CountryDaoImpl;
import cz.leveland.robzone.database.impl.CreditDaoImpl;
import cz.leveland.robzone.database.impl.InvoiceDaoImpl;
import cz.leveland.robzone.database.impl.InvoiceItemDaoImpl;
import cz.leveland.robzone.database.impl.OrderDaoImpl;
import cz.leveland.robzone.database.impl.OrderItemDaoImpl;
import cz.leveland.robzone.database.impl.PartnerDaoImpl;
import cz.leveland.robzone.database.impl.PaymentDaoImpl;
import cz.leveland.robzone.exception.CCFilterException;
import cz.leveland.robzone.exception.CreditNotFoundException;
import cz.leveland.robzone.exception.OrderNotFoundException;
import cz.leveland.robzone.exception.OrderProcessingException;
import cz.leveland.robzone.exception.ProductNotFoundException;
import cz.leveland.robzone.exception.WrongDataException;
import cz.leveland.robzone.generator.DocumentNumberGenerator;
import cz.leveland.robzone.pusher.PushManager;
import cz.leveland.robzone.setup.CCFilter;


@Service
public class FinanceAgenda extends Agenda{

	@Autowired
	PushManager pushManager;
	
	
	@Autowired
	CountryDaoImpl countryTypeDao;

	@Autowired
	PartnerDaoImpl partnerDao;

	@Autowired
	InvoiceDaoImpl invoiceDao;
	
	@Autowired
	InvoiceItemDaoImpl invoiceItemDao;
	
	@Autowired
	PaymentDaoImpl paymentDao;
	
	@Autowired
	CreditDaoImpl creditDao;

	@Autowired
	OrderDaoImpl orderDao;
	
	@Autowired
	OrderItemDaoImpl orderItemDao;
	
	@Autowired
	TradeAgenda tradeAgenda;
	
	@Autowired
	StockAgenda stockAgenda;
	
	@Autowired
	PaymentApiConnector paymentAPiConnector;
	
	@Autowired
	CreditApiConnector creditAPiConnector;
	
	@Autowired
	CCFilter ccFilter;
	
	@Autowired
	CatalogueDataProvider catalogue;
	
	@Autowired
	DocumentNumberGenerator numberGenerator;

	private static final String AGENDA_NAME = "finance";
	
	private static final int[] TRANSFER_PAYMENT_TYPES = {ProductType.TYPE_PAYMENT_ONLINE, ProductType.TYPE_PAYMENT_WIRE};
	
	@PostConstruct
	public void setup() {
		
	}
	
	
	public ResponseObject getCredits() {
		return buildResponse("credits", getCreditList());
	}
	
	private List<CreditDto> getCreditList() {		
		return creditDao.getAll(getCompanyId());
	}

	public ResponseObject getPayments() {	
		
		return buildResponse("payments", getPaymentList());
	}
	
	
	private List<PaymentDto> getPaymentList() {		
		return paymentDao.getAll();
	}

	
	public ResponseObject getPaymentsByOrderNo(String orderNo) {
		return buildResponse("payments", paymentDao.getByOrderNo(orderNo));
	}
	

	public ResponseObject getInvoices() {
		
		return buildResponse("invoices", invoiceDao.getAll(getCompanyId()));
	}
	
	public ResponseObject getInvoice(int invoiceId) {
		
		return buildResponse("invoice", invoiceDao.get(invoiceId));
	}
	
	public ResponseObject getInvoiceItems(int invoiceId) {
		
		ResponseData result = responseDataResult("invoiceItems", invoiceItemDao.getByInvoiceId(invoiceId));
		result.put("sumByVatRate", invoiceItemDao.getInvoicedSumByVatRate(invoiceId));
		return buildResponse(result);
		
	}


	
	private Map<String,Object> importArgs() {
		
		Map<String,Object> args = new HashMap<String,Object>();
		args.put("from", "2017-06-01");
		args.put("to", "2018-01-01");
		return args;

	}
	
	

	/**
	 * called by client
	 * batch import of payments via bank's API
	 * @return
	 * @throws APIConnectionException
	 * @throws APIParseException
	 * @throws WrongInputException
	 * @throws OrderProcessingException
	 * @throws OrderNotFoundException
	 */
	public ResponseObject importPaymentsFromApi() throws APIConnectionException, APIParseException, WrongInputException, OrderProcessingException, OrderNotFoundException {
		
		@SuppressWarnings("unchecked")
		List<Payment> payments = (List<Payment>) paymentAPiConnector.getData(AbstractApiSource.TYPE_IMPORT, importArgs());
		List<ActionResult> errors = new ArrayList<ActionResult>();
		
		for (Payment one:payments) {
			ActionResult result = addPayment(one);
			if (result != null && result.isError())
				errors.add(result);
		}
		
		Map<String,Object> result = responseDataResult("payments", getPaymentList());
		result.put("errors", errors);
		
		return buildResponse(result,STATUS_OK);
	}
	
	/**
	 * called by client
	 * batch import of credits via credit provider's API
	 * @return
	 * @throws APIConnectionException
	 * @throws APIParseException
	 * @throws WrongInputException
	 * @throws OrderProcessingException
	 * @throws OrderNotFoundException
	 */
	public ResponseObject importCreditsFromApi() throws APIConnectionException, APIParseException, WrongInputException, OrderProcessingException, OrderNotFoundException {
		
		@SuppressWarnings("unchecked")
		List<Credit> credits = (List<Credit>) creditAPiConnector.getData();
		List<ActionResult> errors = new ArrayList<ActionResult>();
		
		for (Credit one:credits) {
			ActionResult result = addCredit(one);
			if (result != null && result.isError())
				errors.add(result);
		}
		
		Map<String,Object> result = responseDataResult("credits", getCreditList());
		result.put("errors", errors);
		
		return buildResponse(result,STATUS_OK);
	}
	

	/**
	 * called by client
	 * batch update of existing credits via credit provider's API
	 * @return ResponseObject updated credit list
	 * @throws CreditNotFoundException 
	 * @throws OrderNotFoundException 
	 * @throws OrderProcessingException 
	 * @throws WrongInputException 
	 * @throws APIParseException 
	 * @throws APIConnectionException 
	 * @throws WrongDataException 
	 * @throws ProductNotFoundException 
	 * @throws CCFilterException 
	 */
	public ResponseObject updateCreditsFromApi(Map<String, Object> creditData) throws WrongInputException, OrderProcessingException, OrderNotFoundException, CreditNotFoundException, APIConnectionException, APIParseException, WrongDataException, ProductNotFoundException, CCFilterException  {
		
		@SuppressWarnings("unchecked")
		List<Credit> creditUpdates = (List<Credit>) creditAPiConnector.getData(CreditFileApiSource.TYPE_UPDATE);
		List<ActionResult> errors = new ArrayList<ActionResult>();
		
		for (Credit one:creditUpdates) {
			
			ActionResult result = updateCredit(one);
			if (result != null && result.isError())
				errors.add(result);
		}
		
		Map<String,Object> result = responseDataResult("credits", getCreditList());
		result.put("errors", errors);
		
		return buildResponse(result,STATUS_OK);
	}
	
	
	
	/**
	 * called by client, manual adding
	 * adds new payment and tries to process related order if it is waiting for TRANSFER PAYMENT (wire or online)
	 * @param paymentData - payer ID, order ID, refNo and amount
	 * @return
	 * @throws WrongInputException
	 * @throws OrderNotFoundException 
	 * @throws OrderProcessingException 
	 */
	
	@Transactional(rollbackFor = Exception.class)
	public ResponseObject addPayment(Map<String, Object> paymentData) throws WrongInputException, OrderProcessingException, OrderNotFoundException {
		
		Integer payerId = (Integer)paymentData.get("payerId");		
		//Integer orderId = (Integer)paymentData.get("orderId");		
		String orderNo = (String)paymentData.get("orderNo");		
		Double amount = ((Number)paymentData.get("amount")).doubleValue();
		Order order = orderDao.getByOrderNo(orderNo);
		stockAgenda.updateAvailable(order.getWarehouseId());
		
		ActionResult actionResult = addPayment(new Payment(orderNo, payerId, amount));
		
		if (actionResult != null && actionResult.isError())
			throw new WrongInputException();
		return getPayments();
		
		
	}
	
	
	/**
	 * used in API import of payments
	 * @param payment
	 * @return
	 * @throws WrongInputException
	 * @throws OrderNotFoundException 
	 * @throws OrderProcessingException 
	 */
	
	
	public ActionResult addPayment(Payment payment) throws WrongInputException, OrderProcessingException, OrderNotFoundException {
		
		OrderDto order = orderDao.getDto(payment.getRefNo(), getCompanyId());
		if (order == null) {
			logError(AGENDA_NAME, "addPayment", "order not found " + payment.getRefNo());
			return new ActionResult(ActionResult.TYPE_ERROR, payment.getRefNo(), "Objedná¡vka k platbě: " + payment.getRefNo() + " nebyla nalezena");
		}
		payment.setOrderId(order.getId());
		
		try {
			paymentDao.saveOrUpdate(payment);
		} catch (Exception e) {
			logError(AGENDA_NAME, "addPayment", "payment couldn't be saved, order id:" + order.getId());
			return new ActionResult(ActionResult.TYPE_ERROR, payment.getRefNo(), "platbu k objednávce " + payment.getRefNo() + " nelze uložit");
		}
		
		try {
			tradeAgenda.tryToProcessOrder(payment.getRefNo(), TRANSFER_PAYMENT_TYPES);
		} catch (Exception e) {
			return new ActionResult(ActionResult.TYPE_ERROR, payment.getRefNo(), "Chyba při pokusu zpracovat objednávku " + order.getOrderNo());
		}
		
		return null;
	}

	/**
	 * called by client, manual adding
	 * adds new payment and tries to process related order if it is waiting for TRANSFER PAYMENT (wire or online)
	 * @param paymentData - payer ID, order ID, refNo and amount
	 * @return
	 * @throws WrongInputException
	 * @throws OrderNotFoundException 
	 * @throws OrderProcessingException 
	 */
	
	
	public ResponseObject addCredit(Map<String, Object> creditData) throws WrongInputException, OrderProcessingException, OrderNotFoundException {
		
		Integer payerId = (int)creditData.get("payerId");		
		String creditNo = (String)creditData.get("creditNo");		
		String refNo = (String)creditData.get("refNo");		
		//Double amount = ((Number)creditData.get("amount")).doubleValue();

		ActionResult actionResult = addCredit(new Credit(payerId, creditNo, refNo, null));
		
		if (actionResult != null && actionResult.isError())
			throw new WrongInputException();
		return getCredits();
			
	}
	
	/**
	 * adds new credit and tries to process related order
	 * @param creditData
	 * @return
	 * @throws WrongInputException
	 * @throws OrderNotFoundException 
	 * @throws OrderProcessingException 
	 */
	
	public ActionResult addCredit(Credit credit) {

		
		OrderDto orderDto = orderDao.getDto(credit.getRefNo(), getCompanyId());
		if (orderDto == null) {
			logError(AGENDA_NAME, "addCredit", "order not found " + credit.getRefNo());
			return new ActionResult(ActionResult.TYPE_ERROR, credit.getRefNo(), "Objednávka k úvěru " + credit.getRefNo() + " nebyla nalezena");
		}
		
		int paymentType = orderDao.getPaymentType(orderDto.getId());
		if (paymentType != ProductType.TYPE_PAYMENT_CREDIT) {
			logError(AGENDA_NAME, "addCredit", "order hasnt credit payment type" + credit.getRefNo());
			return new ActionResult(ActionResult.TYPE_ERROR, credit.getRefNo(), "Objednávka k úvěru " + credit.getRefNo() + " nemá platbu typu splátky");
		}
		
		credit.setOrderId(orderDto.getId());
		credit.setPartnerId(orderDto.getPartnerId());
		
		try {
			creditDao.saveOrUpdate(credit);
		} catch (Exception e) {
			e.printStackTrace();
			logError(AGENDA_NAME, "addCredit", "credit couldn't be saved, refNo:" + credit.getRefNo());
			return new ActionResult(ActionResult.TYPE_ERROR, credit.getRefNo(), "Úvěr k objednávce " + credit.getRefNo() + " nenelze uložit");
		}
		
		try {
			if (orderDto != null) 		
				tradeAgenda.tryToProcessOrder(credit.getRefNo(), ProductType.TYPE_PAYMENT_CREDIT);
		} catch (Exception e){
			return new ActionResult(ActionResult.TYPE_ERROR, credit.getRefNo(), "Chyba při pokusu zpracovat objednÃ¡vku " + orderDto.getOrderNo());
		}
		
		return null;
	}
	
	
	/**
	 * updates credit information - modifies approved & amount attributes and tries to process the order
	 * @param creditData
	 * @return
	 * @throws WrongInputException
	 * @throws OrderNotFoundException 
	 * @throws OrderProcessingException 
	 * @throws CreditNotFoundException 
	 * @throws WrongDataException 
	 * @throws ProductNotFoundException 
	 * @throws CCFilterException 
	 */
	@Transactional(rollbackFor = Exception.class)
	public ActionResult updateCredit(Credit creditUpdate) throws WrongInputException, OrderProcessingException, OrderNotFoundException, CreditNotFoundException, WrongDataException, ProductNotFoundException, CCFilterException {
		
		/*
		Integer countryId = ccFilter.getOneCurrentCountry(getUserId());
		if (countryId == null)
			throw new CCFilterException();
		*/
	
		String creditNo = creditUpdate.getCreditNo();
		Double amount = creditUpdate.getAmount();
		Boolean approved = creditUpdate.getApproved();
		
		if (creditNo == null || amount == null || amount < 0.0d || approved == null)
			return new ActionResult(ActionResult.TYPE_ERROR, creditNo, "ChybnÃ¡ data aktualizace úvěru " + creditNo);
			
		Credit credit = creditDao.getByCreditNo(creditNo);
		if (credit == null)
			return new ActionResult(ActionResult.TYPE_ERROR, creditNo, "Úvěr " + creditNo + " nebyl nalezen");
		
		String orderNo = credit.getRefNo();
			
	
		credit.setApproved(approved);
		credit.setAmount(amount);
		creditDao.saveOrUpdate(credit);
		
		
		Order order = orderDao.getByOrderNo(credit.getRefNo());
		order.setOrigPaymentType(ProductType.TYPE_PAYMENT_CREDIT);
		orderDao.saveOrUpdate(order);
		
		orderItemDao.changePaymentType(order.getId(), ProductType.TYPE_PAYMENT_COD, 
				catalogue.getCodProductId(getCompanyId()));
		
		
		
		try {
			tradeAgenda.tryToProcessOrder(orderNo, ProductType.TYPE_PAYMENT_CREDIT);
		} catch (Exception e) {
			return new ActionResult(ActionResult.TYPE_ERROR, credit.getRefNo(), "Chyba pÅ™i pokusu zpracovat objednÃ¡vku " + credit.getRefNo());
		}
		
		return null;
	}
	
	
	/**
	 * creates invoices on various schemes - batch from deliveries or single for one invoice
	 * @param orderId
	 * @param invoicingSource - order created in standard way from deliveries or directly from orders  
	 * @param invoicingPrice
	 * @return
	 * @throws WrongInputException
	 */
	@Transactional(rollbackFor = Exception.class)
	public ResponseObject createInvoices(Integer orderId, int invoicingSource, int invoicingPrice) throws WrongInputException {
		
		List<ForInvoiceDto> forInvoice = invoiceItemDao.getForInvoice(getCompanyId());
		 
		int good = 0, counter = 0;
		
		List<ActionResult> errors = new ArrayList<ActionResult>();
		
		Map<Integer,List<ForInvoiceDto>> orderMap = new HashMap<Integer,List<ForInvoiceDto>>();
		orderMap = makeOrderMap(forInvoice);
				
		for (Integer oneOrderId: orderMap.keySet()) {

			List<ForInvoiceDto> oneList = orderMap.get(oneOrderId);
			ForInvoiceDto firstItem = oneList.get(0);
			
			/* ORDER ID FILTER */
			if (orderId != null && orderId != firstItem.getOrderId()) 
				continue;
			
			
			/* PROCESS PHASE FILTER */
			switch(invoicingSource) {
			case Invoice.INVOICING_SOURCE_DELIVERY:
				if (firstItem.getDeliveries() == 0 || firstItem.getSumDeliv()==0 || firstItem.getSumUndeliv() > 0) 
					continue;
				break;
			case Invoice.INVOICING_SOURCE_ALLOCATION:
				if (firstItem.getOutstocks() == 0 || firstItem.getDeliveries() > 0)
					continue;
				break;
			case Invoice.INVOICING_SOURCE_ORDER:
				if (firstItem.getOutstocks() > 0)
					continue;
				break;
			default:
				throw new WrongInputException();
			}
			
			ActionResult invoiceResult = createOneInvoice(firstItem.getOrderId(), oneList, invoicingSource, invoicingPrice);
			if (invoiceResult != null) {
				errors.add(invoiceResult);
			} else
					good++;
			counter++;
			
			pushManager.reportProgress(new Progress(forInvoice.size(), counter-1, good, errors.size()), 
					getCompanyId(), PushManager.PUSHER_EVENT_INVOICE_PROGRESS);
			
		}
		
		
		Map<String,Object> result = responseDataResult("invoices", null);
		result.put("errors", errors);
		
		return buildResponse(result,STATUS_OK);
	}


	/**
	 * groups list of items for invoicing to order map
	 * @param forInvoice
	 * @return
	 */
	private Map<Integer, List<ForInvoiceDto>> makeOrderMap(List<ForInvoiceDto> forInvoice) {
		Map<Integer,List<ForInvoiceDto>> orderMap = new HashMap<Integer,List<ForInvoiceDto>>();

		for (ForInvoiceDto one: forInvoice) {
			
			if (!orderMap.containsKey(one.getOrderId()))
				orderMap.put(one.getOrderId(), new ArrayList<ForInvoiceDto>());
		
			orderMap.get(one.getOrderId()).add(one);						
		}
		return orderMap;
		
	}


	private ActionResult createOneInvoice(Integer currentOrderId, List<ForInvoiceDto> oneOrderItems, int invoicingSource, int invoicingPrice) throws WrongInputException {
		
		Order order = orderDao.get(currentOrderId);
		if (order == null) {
			logError(AGENDA_NAME, "createOneinvoice", "order not found " + currentOrderId);
			return new ActionResult(ActionResult.TYPE_ERROR, currentOrderId, "Objednávka " + currentOrderId + " nebyla nalezena");		
		}
		
		Partner customer = partnerDao.get(order.getPayerId());
		if (customer == null) {
			logError(AGENDA_NAME, "createOneinvoice", "partner not found " + order.getPartnerId());
			return new ActionResult(ActionResult.TYPE_ERROR, currentOrderId, "Partner " + order.getPartnerId() + " nebyl nalezen");		
		}
		
		/* TODO - temporary input */
		Date createDate = new Date();
		Date taxDate = new Date();
		int currencyId = catalogue.getCurrencyId(getCompanyId(), customer.getCountryId());
		Integer countryId = ccFilter.getOneCurrentCountry(getUserId());
		if (countryId == null)
			throw new WrongInputException();
		
		Invoice invoice = new Invoice(getCompanyId(), Invoice.TYPE_CUSTOMER, Invoice.SUBTYPE_INVOICE, 
				numberGenerator.getInvoiceNo(countryId), customer.getId(), order.getId(), createDate, taxDate, currencyId);
		invoice.calculatePriceAndVat(oneOrderItems);
		invoiceDao.saveOrUpdate(invoice);
		
		int invCounter = 0;
		for (ForInvoiceDto item:oneOrderItems) {
			
			InvoiceItem invoiceItem = new InvoiceItem(item, invoicingSource, invoicingPrice);
			invoiceItem.setInvoiceId(invoice.getId());
			invoiceItem.setCompanyId(getCompanyId());
			invoiceItemDao.saveOrUpdate(invoiceItem);
			if (++invCounter % 20 == 0)
				invoiceItemDao.flushAndClear();
			
		}
		
		return null;
		
		
	}


	
	public void createCreditNote(int invoiceId, Date taxDate, int countryId) throws WrongInputException, WrongDataException {
		
		Invoice invoice = invoiceDao.get(invoiceId);
		if (invoice == null)
			throw new WrongInputException();
		
		List<InvoiceItem> invoiceItems = invoiceItemDao.getByInvoiceId(invoiceId);
		if (invoiceItems.isEmpty())
			throw new WrongDataException();
		
		String invoiceNo = numberGenerator.getCreditNoteNo(countryId);
		Invoice creditNote = new Invoice(getCompanyId(), Invoice.TYPE_CUSTOMER, Invoice.SUBTYPE_CREDITNOTE, invoiceNo, invoice.getPartnerId(), invoice.getOrderId(), new Date(), taxDate, invoice.getCurrencyId());
		double price = 0.0, vat = 0.0;
		for (InvoiceItem one: invoiceItems) {
			
			price += one.getTotalPrice();
			vat += one.getVat();
		}
		creditNote.setCreated(new Date());
		creditNote.setTaxDate(taxDate);
		creditNote.setTotalPrice(-price);		
		
		creditNote.setVat(-vat);
		
		invoiceDao.saveOrUpdate(creditNote);
	}


	/**
	 * creates credit note for items returned to stock
	 * if there is at least one item without invoice, no credit note is created	 
	 * @param deliveryItemIds - list of delivery item ids to be returned
	 * @param taxDate
	 */
	public void createCreditNote(Map<Integer,Integer> deliveryItemIds, Date taxDate, int countryId) {
		
		double price = 0.0, vat = 0.0;
		Invoice invoice = null;
		for (Integer did:deliveryItemIds.keySet()) {
			InvoiceItem one = invoiceItemDao.getByDeliveryItemId(did);
			if (one == null)
				continue;

			if (invoice == null) 
				invoice = invoiceDao.get(one.getInvoiceId());
			
			price += one.getTotalPrice();
			vat += one.getVat();
		}
		
		if (price == 0.0)
			return;
		
		String invoiceNo = numberGenerator.getCreditNoteNo(countryId);
		Invoice creditNote = new Invoice(getCompanyId(), Invoice.TYPE_CUSTOMER, Invoice.SUBTYPE_CREDITNOTE, invoiceNo, invoice.getPartnerId(), invoice.getOrderId(), new Date(), taxDate, invoice.getCurrencyId());
		creditNote.setCreated(new Date());
		creditNote.setTaxDate(taxDate);
		creditNote.setDueDate(taxDate);
		creditNote.setTotalPrice(-price);				
		creditNote.setVat(-vat);
		creditNote.setInvoiceId(invoice.getId());
		
		invoiceDao.saveOrUpdate(creditNote);
		
	}
	
	
	
}
