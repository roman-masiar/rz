package cz.leveland.robzone.controller;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cz.leveland.appbase.database.exceptions.UserAlreadyExistsException;
import cz.leveland.appbase.database.exceptions.WrongAmountException;
import cz.leveland.appbase.database.exceptions.WrongInputException;
import cz.leveland.appbase.modules.AppModule;
import cz.leveland.appbase.util.ResponseObject;
import cz.leveland.robzone.api.APIConnectionException;
import cz.leveland.robzone.api.APIParseException;
import cz.leveland.robzone.database.entity.Invoice;
import cz.leveland.robzone.exception.CCFilterException;
import cz.leveland.robzone.exception.CreditNotFoundException;
import cz.leveland.robzone.exception.OrderNotFoundException;
import cz.leveland.robzone.exception.OrderProcessingException;
import cz.leveland.robzone.exception.OverAllocationException;
import cz.leveland.robzone.exception.PartnerNotFoundException;
import cz.leveland.robzone.exception.ProductNotFoundException;
import cz.leveland.robzone.exception.StockAllocationException;
import cz.leveland.robzone.exception.StockSetCreationException;
import cz.leveland.robzone.exception.UserErrorException;
import cz.leveland.robzone.exception.WrongDataException;
import cz.leveland.robzone.modules.APIAgenda;
import cz.leveland.robzone.modules.ClaimsAgenda;
import cz.leveland.robzone.modules.DeliveryAgenda;
import cz.leveland.robzone.modules.FinanceAgenda;
import cz.leveland.robzone.modules.PartnerAgenda;
import cz.leveland.robzone.modules.ProductAgenda;
import cz.leveland.robzone.modules.StockAgenda;
import cz.leveland.robzone.modules.TradeAgenda;
import cz.leveland.robzone.modules.UserBusiness;







@Controller
public class UserController extends AppModule {
	
	private static final Log logger = LogFactory.getLog(UserController.class);


	
	
    @Autowired
    UserBusiness business;
    
    @Autowired
    TradeAgenda tradeAgenda;

    @Autowired
    StockAgenda stockAgenda;

    @Autowired
    ProductAgenda productAgenda;
    
    @Autowired
    FinanceAgenda financeAgenda;
    
    @Autowired
    PartnerAgenda partnerAgenda;
    
    @Autowired
    DeliveryAgenda deliveryAgenda;
    
    @Autowired
    ClaimsAgenda claimsAgenda;
    
    @Autowired
    APIAgenda apiBlock;
    
    int ix=1;
    
    
    @RequestMapping(value = {NORMAL_PATH+"/test" }, method = RequestMethod.GET)
    @ResponseBody
    public String test() {
    	return "testOk - 1.0 start";
    }
    
    @RequestMapping(value = {NORMAL_PATH+"/signup" }, method = RequestMethod.POST)
    @ResponseBody
    public ResponseObject signUp(@RequestBody Map<String, Object> userData, HttpServletRequest request) {
    //@RequestBody User user
    	try{
    		return  business.signUp(userData, request.getRemoteAddr());
    	} catch (UserAlreadyExistsException ex) {
			return buildErrorResponse("user-exists", STATUS_USER_ERROR);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return buildErrorResponse("Signup failed", STATUS_FAIL);
		}
    }

    
    @RequestMapping(value = {NORMAL_PATH+"/login" }, method = RequestMethod.POST)
    @ResponseBody
    public ResponseObject login(@RequestBody String credentials, HttpServletRequest request) {
    	
    	console("PATH: login");    	
    	ResponseObject loginData = business.login(credentials, request.getRemoteAddr());
    	
    	return loginData;
    	
    }
    
    
    
    @RequestMapping(value = {SEC_PATH+"/profiledata" }, method = RequestMethod.GET)
    @ResponseBody
    public ResponseObject getProfileData() {
    	
    	try {
			return business.getProfileData();
		} catch (WrongAmountException e) {
			
			e.printStackTrace();
			return buildErrorResponse("Profile data failed", STATUS_FAIL);
		}
    }
    
    
    /* ------- BUSINESS PART ----------*/
  
    /* API block */
    @RequestMapping(value = {SEC_PATH+"/read/orders/file" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject readOrdersFile(@RequestBody Map<String,Object> fileData) {
		try {
			return tradeAgenda.importOrders(fileData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return buildErrorResponse("Wrong conversion XML", STATUS_FAIL);
		}
	}

    
	/* partner block */
	@RequestMapping(value = {SEC_PATH+"/partners" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getPartners() {
		return partnerAgenda.getPartners();
	}
	
	@RequestMapping(value = {SEC_PATH+"/customers" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getCustomers() {
		return partnerAgenda.getCustomers();
	}
	
	/* finance block */
	@RequestMapping(value = {SEC_PATH+"/invoices" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getInvoices() {
		return financeAgenda.getInvoices();
	}
	
	@RequestMapping(value = {SEC_PATH+"/invoices/{invoiceId}" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getOneInvoice(@PathVariable("invoiceId") int invoiceId) {
		return financeAgenda.getInvoice(invoiceId);
	}
	
	@RequestMapping(value = {SEC_PATH+"/invoices/items/{invoiceId}" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getOneInvoiceItems(@PathVariable("invoiceId") int invoiceId) {
		return financeAgenda.getInvoiceItems(invoiceId);
	}
	
	@RequestMapping(value = {SEC_PATH+"/credits" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getCredits() {
		return financeAgenda.getCredits();
	}
	
	@RequestMapping(value = {SEC_PATH+"/credits/add" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject addCredit(@RequestBody Map<String,Object> creditData) {
		try {
			return financeAgenda.addCredit(creditData);
		} catch (WrongInputException | OrderProcessingException | OrderNotFoundException e) {
			e.printStackTrace();
			return buildErrorResponse("PÅ™idÃ¡nÃ­ ÃºvÄ›ru selhalo", STATUS_FAIL);			
		}
	}
	
	@RequestMapping(value = {SEC_PATH+"/credits/update" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject updateCredit(@RequestBody Map<String,Object> creditData) {
		try {
			return financeAgenda.updateCreditsFromApi(creditData);			
		} catch (Exception e) {
			e.printStackTrace();
			return buildErrorResponse("Aktualizace úvěru selhala", STATUS_FAIL);			
		}
	}
	
	@RequestMapping(value = {SEC_PATH+"/credits/import" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject importCredits(@RequestBody Map<String,Object> creditData) {
		try {
			return financeAgenda.importCreditsFromApi();
		} catch (Exception e) {
			e.printStackTrace();
			return buildErrorResponse("Import ÃºvÄ›rÅ¯ selhal", STATUS_FAIL);			
		}
	}

	
	
	@RequestMapping(value = {SEC_PATH+"/payments" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getPayments() {
		return financeAgenda.getPayments();
	}
	
	@RequestMapping(value = {SEC_PATH+"/payments/order/{orderNo}" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getPayments(@PathVariable("orderNo") String orderNo) {
		return financeAgenda.getPaymentsByOrderNo(orderNo);
	}
	
	@RequestMapping(value = {SEC_PATH+"/payments/add" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject addPayment(@RequestBody Map<String,Object> paymentData) {
		try {
			return financeAgenda.addPayment(paymentData);
		} catch (WrongInputException | OrderProcessingException | OrderNotFoundException e) {
			e.printStackTrace();
			return buildErrorResponse("PÅ™idÃ¡nÃ­ platby selhalo", STATUS_FAIL);			
		}
	}
	
	@RequestMapping(value = {SEC_PATH+"/payments/import" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject readPayments(@RequestBody Map<String,Object> paymentData) {
		try {
			return financeAgenda.importPaymentsFromApi();
		} catch (Exception e) {
			e.printStackTrace();
			return buildErrorResponse("Pridani plateb selhalo", STATUS_FAIL);			
		}
	}

	@RequestMapping(value = {SEC_PATH+"/invoices/create" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject createInvoices(@RequestBody Map<String,Object> invoiceData) {
		try {
			
			/* no specific order, source: deliveries, pricing: order */
			return financeAgenda.createInvoices(					 
					(Integer)invoiceData.get("orderId"), 
					(int)invoiceData.get("invoiceSource"), 
					(int)invoiceData.get("invoicePrice"));
		} catch (Exception e) {
			e.printStackTrace();
			return buildErrorResponse("Vytvoření faktur selhalo", STATUS_FAIL);			
		}
	}
	
	
	
	/* product block */
	@RequestMapping(value = {SEC_PATH+"/producttypes/nongoods" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getNonGoodsProductTypes() {
		return productAgenda.getNonGoodsProducts();
	}
	
	@RequestMapping(value = {SEC_PATH+"/products/{productId}" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getProduct(@PathVariable("productId") int productId) {
		return productAgenda.getProduct(productId);
	}
	
	@RequestMapping(value = {SEC_PATH+"/products" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getProducts() {
		return tradeAgenda.getProducts();
	}
	@RequestMapping(value = {SEC_PATH+"/products/service" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getServiceProducts() {
		return tradeAgenda.getServiceProducts();
	}
	
	@RequestMapping(value = {SEC_PATH+"/products/set" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getSetProducts() {
		return productAgenda.getSetProducts();
	}
	
	@RequestMapping(value = {SEC_PATH+"/products/add" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject addProduct(@RequestBody Map<String,Object> productData) {
		try {
			return productAgenda.addProduct(productData);
		} catch (WrongInputException e) {
			e.printStackTrace();
			return buildErrorResponse("PÅ™idÃ¡nÃ­ produktu selhalo", STATUS_FAIL);			
		}
	}

	@RequestMapping(value = {SEC_PATH+"/products/update" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject updateProduct(@RequestBody Map<String,Object> productData) {
		try {
			return productAgenda.updateProduct(productData);
		} catch (WrongInputException e) {
			e.printStackTrace();
			return buildErrorResponse("PÅ™idÃ¡nÃ­ produktu selhalo", STATUS_FAIL);			
		}
	}
	
	@RequestMapping(value = {SEC_PATH+"/products/remove" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject removeProduct(@RequestBody Map<String,Object> productData) {
		return productAgenda.removeProduct(productData);
	}
	
		
	@RequestMapping(value = {SEC_PATH+"/outstocks/packed/{warehouseId}" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getOutstocksPacked(@PathVariable("warehouseId") int warehouseId) {
		return stockAgenda.getOutstocksPacked(warehouseId);
	}

	@RequestMapping(value = {SEC_PATH+"/outstocks/unpacked/{warehouseId}" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getOutstocksUnpacked(@PathVariable("warehouseId") int warehouseId) {
		return stockAgenda.getOutstocksUnpacked(warehouseId);
	}
	
	/*
	@RequestMapping(value = {SEC_PATH+"/outstocks/pick" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject pickOutstock(@RequestBody Map<String,Object> outstockData) {
		try {
			return stockAgenda.pickOutstock(outstockData);
		} catch (WrongInputException | WrongDataException e) {
			e.printStackTrace();
			return buildErrorResponse("Vyskladnění selhalo", STATUS_FAIL);	
		} catch (UserErrorException e) {			
			return buildErrorResponse("EAN byl již použit", STATUS_USER_ERROR);
		}
	}
	*/
	
	@RequestMapping(value = {SEC_PATH+"/outstocks/transport" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject askForTransport(@RequestBody Map<String,Object> transportData) {
		try {
			return deliveryAgenda.askForDelivery(transportData);
		} catch (OrderNotFoundException | PartnerNotFoundException e) {
			e.printStackTrace();
			return buildErrorResponse("Požadavek na dopravu selhal", STATUS_FAIL);	
		}
	}
	
	@RequestMapping(value = {SEC_PATH+"/outstocks/packages/{transporterId}" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getPackages(@PathVariable("transporterId") int transporterId) {
		return stockAgenda.getPackages(transporterId);
	}
	
	@RequestMapping(value = {SEC_PATH+"/instocks/{warehouseId}" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getInstocks(@PathVariable("warehouseId") int warehouseId) {
		return stockAgenda.getInstocks(warehouseId);
	}
	
	@RequestMapping(value = {SEC_PATH+"/instocks/{warehouseId}/{stockItemId}" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getInstocks(
			@PathVariable("warehouseId") int warehouseId,
			@PathVariable("stockItemId") int stockItemId
			) {
		return stockAgenda.getInstocks(warehouseId, stockItemId);
	}
	
	@RequestMapping(value = {SEC_PATH+"/instocks/add" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject addInstock(@RequestBody Map<String,Object> instockData) {
		try {
			return stockAgenda.addInstock(instockData);
		} catch (WrongInputException e) {
			e.printStackTrace();
			return buildErrorResponse("Přidání příjemky selhalo", STATUS_FAIL);			
		}
	}
	
	@RequestMapping(value = {SEC_PATH+"/instocks/delete" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject removeInstock(@RequestBody Map<String,Object> instockData) {
		try {
			return stockAgenda.removeInstock(instockData);
		} catch (WrongInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return buildErrorResponse("Smazání příjemky selhalo", STATUS_FAIL);	
		}
	}
	
	
	
	
	
	@RequestMapping(value = {SEC_PATH+"/stockitems/pick" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject getStockItem(@RequestBody Map<String,Object> pickData) {
		try {
			return stockAgenda.pickStockItem(pickData);
		} catch (WrongInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return buildErrorResponse("Nalezen položek k vyskladnění selhalo", STATUS_FAIL);	
		}
	}
	
	@RequestMapping(value = {SEC_PATH+"/stockitems" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getStockItem() {
		return stockAgenda.getStockItems();
	}

	
	
	@RequestMapping(value = {SEC_PATH+"/stocksets/{warehouseId}" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getStockSets(@PathVariable("warehouseId") int warehouseId) {
		return stockAgenda.getStockSets(warehouseId);
	}
	
	@RequestMapping(value = {SEC_PATH+"/stock/{warehouseId}" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getStock(@PathVariable("warehouseId") int warehouseId) {
		return stockAgenda.getStock(warehouseId);
	}
	
	@RequestMapping(value = {SEC_PATH+"/stocksets/add" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject addStockSets(@RequestBody Map<String,Object> stockSetData) {
		try {
			return stockAgenda.addStockSets(stockSetData);
		} catch (WrongInputException | OverAllocationException | StockSetCreationException | StockAllocationException e) {
			e.printStackTrace();
			return buildErrorResponse("Přidání setů selhalo", STATUS_FAIL);			
		}
	}
	
	@RequestMapping(value = {SEC_PATH+"/setitems/{productId}" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getSetItems(@PathVariable("productId") int productId) {
		return productAgenda.getProductSetItems(productId);
	}
	
	@RequestMapping(value = {SEC_PATH+"/setitems/add" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject addSetItem(@RequestBody Map<String,Object> setItemData) {
		try {
			return productAgenda.addSetItem(setItemData);
		} catch (WrongInputException e) {
			e.printStackTrace();
			return buildErrorResponse("Přidání setové položky selhalo", STATUS_FAIL);			
		}
	}
	
	@RequestMapping(value = {SEC_PATH+"/setitems/delete" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject deleteSetItem(@RequestBody Map<String,Object> setItemData) {
		try {
			return productAgenda.deleteSetItem(setItemData);
		} catch (WrongInputException e) {
			e.printStackTrace();
			return buildErrorResponse("Smazání setové položky selhalo", STATUS_FAIL);			
		}
	}
	
	
	/* trade block */
	@RequestMapping(value = {SEC_PATH+"/catalogue" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getCatalogueData() {
		
		return tradeAgenda.getCatalogueData();
	}
	
	@RequestMapping(value = {SEC_PATH+"/orders" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getOrders() {
		
		return tradeAgenda.getOrders(null);
	}
	
	@RequestMapping(value = {SEC_PATH+"/orders/filtered/{filterId}" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getOrdersFiltered(@PathVariable("filterId") int filter) {
		return tradeAgenda.getOrders(filter);
	}
	
	@RequestMapping(value = {SEC_PATH+"/orders/{orderId}" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getOrders(@PathVariable("orderId") int orderId) {
		return tradeAgenda.getOrder(orderId);
	}
	
	
	@RequestMapping(value = {SEC_PATH+"/orders/items/{orderId}" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getOrderItems(@PathVariable("orderId") int orderId) {
		return tradeAgenda.getOrderItems(orderId);
	}
	
	
	@RequestMapping(value = {SEC_PATH+"/orders/items/add" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject addOrderItem(@RequestBody Map<String,Object> itemData) {
		try {
			return tradeAgenda.addOrderItem(itemData);
		} catch (WrongInputException e) {
			e.printStackTrace();
			return buildErrorResponse("Přidíní položky k objednávce selhalo", STATUS_FAIL);			
		}
	}
	@RequestMapping(value = {SEC_PATH+"/orders/items/fromset" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject addOrderItemFromSet(@RequestBody Map<String,Object> setData) {
		try {
			return tradeAgenda.addOrderItemFromSet(setData);
		} catch (WrongInputException e) {
			e.printStackTrace();
			return buildErrorResponse("Vytvoření ze setu selhalo", STATUS_FAIL);			
		}
	}
	@RequestMapping(value = {SEC_PATH+"/orders/items/recalculate" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject recalculateOrderItems(@RequestBody Map<String,Object> orderData) {
		try {
			return tradeAgenda.recalculateOrderItems(orderData);
		} catch (WrongInputException e) {
			e.printStackTrace();
			return buildErrorResponse("Přepočítání cen objednávky selhalo", STATUS_FAIL);			
		}
	}
	@RequestMapping(value = {SEC_PATH+"/orders/items/update" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject updateOrderItem(@RequestBody Map<String,Object> itemData) {
		try {
			return tradeAgenda.updateOrderItem(itemData);
		} catch (WrongInputException e) {
			e.printStackTrace();
			return buildErrorResponse("Aktualizace položky objednávky selhalo", STATUS_FAIL);			
		}
	}
	
	@RequestMapping(value = {SEC_PATH+"/orders/items/delete" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject deleteOrderItem(@RequestBody Map<String,Object> itemData) {
		try {
			return tradeAgenda.deleteOrderItem(itemData);
		} catch (WrongInputException | WrongDataException e) {
			e.printStackTrace();
			return buildErrorResponse("Smazání položky objednávky selhalo", STATUS_FAIL);			
		}
	}
	
	@RequestMapping(value = {SEC_PATH+"/orders/add" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject addOrder(@RequestBody Map<String,Object> orderData) {
		try {
			return tradeAgenda.addOrder(orderData);
		} catch (WrongInputException | CCFilterException e) {
			e.printStackTrace();
			return buildErrorResponse("Přidání objednávky selhalo", STATUS_FAIL);			
		}
	}
	
	
	@RequestMapping(value = {SEC_PATH+"/orders/update" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject updateOrder(@RequestBody Map<String,Object> orderData) {
		try {
			return tradeAgenda.updateOrder(orderData);
		} catch (WrongInputException e) {
			e.printStackTrace();
			return buildErrorResponse("SmazÃ¡nÃ­ poloÅ¾ky objednÃ¡vky selhalo", STATUS_FAIL);			
		}
	}
	
	
	@RequestMapping(value = {SEC_PATH+"/orders/process" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject processOrder(@RequestBody Map<String,Object> orderData) {
		try {
			return tradeAgenda.processOrder(orderData);
		} catch (Exception e) {
			e.printStackTrace();
			return buildErrorResponse("Procesovani poloÅ¾ky objednÃ¡vky selhalo", STATUS_FAIL);			
		}
	}
	
	@RequestMapping(value = {SEC_PATH+"/orders/unknown" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getUnknownProducts(@PathVariable("orderId") int orderId) {
		return tradeAgenda.getUnknownProducts();
	}
	
	@RequestMapping(value = {SEC_PATH+"/orders/changes/{orderId}" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getOrderChanges(@PathVariable("orderId") int orderId) {
		return tradeAgenda.getOrderChanges(orderId);
	}
	
	/* delivery agenda */
	@RequestMapping(value = {SEC_PATH+"/delivery/{warehouseId}" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getDelivery(@PathVariable("warehouseId") int warehouseId) {
		return deliveryAgenda.getDelivery(warehouseId);
	}
	
	
	@RequestMapping(value = {SEC_PATH+"/delivery/one/{deliveryId}" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getOneDelivery(@PathVariable("deliveryId") int deliveryId) {
		try {
			return stockAgenda.getDeliveryDetails(deliveryId);
		} catch (WrongDataException e) {
			return buildErrorResponse("Chybné vstupní údaje", STATUS_FAIL);	
		}
	}
	
	@RequestMapping(value = {SEC_PATH+"/delivery/add" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject addDelivery(@RequestBody Map<String,Object> deliveryData) {
		try {
			return stockAgenda.addDelivery(deliveryData);
		} catch (Exception e) {
			e.printStackTrace();
			return buildErrorResponse("Přidání položky do svozového listu selhalo", STATUS_FAIL);			
		}
	}
	
	@RequestMapping(value = {SEC_PATH+"/delivery/import" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject importDeliveryInfo(@RequestBody Map<String,Object> deliveryData) {
		try {
			return deliveryAgenda.importDeliveryInfo();
		} catch (Exception e) {
			e.printStackTrace();
			return buildErrorResponse("Import info o dopravě selhal", STATUS_FAIL);			
		}
	}
	
	@RequestMapping(value = {SEC_PATH+"/delivery/order/{orderNo}" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getOneDelivery(@PathVariable("orderNo") String orderNo) {
		try {
			return deliveryAgenda.getOrderDelivery(orderNo);
		} catch (WrongDataException | WrongInputException e) {
			return buildErrorResponse("Chybné vstupní údaje", STATUS_FAIL);	
		}
	}
	

	
	/* claim stuff */
	/* claims agenda */
	@RequestMapping(value = {SEC_PATH+"/claims/{claimId}" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getClaim(@PathVariable("claimId") int claimId) {
		return claimsAgenda.getClaim(claimId);
	}

	@RequestMapping(value = {SEC_PATH+"/claim/items/{claimId}" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getClaimItems(@PathVariable("claimId") int claimId) {
		return claimsAgenda.getClaimItems(claimId);
	}
	
	@RequestMapping(value = {SEC_PATH+"/claim/add" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject addClaim(@RequestBody Map<String,Object> claimData) {
		try {
			return claimsAgenda.addClaim(claimData);
		} catch (Exception e) {
			e.printStackTrace();
			return buildErrorResponse("Claim selhal", STATUS_FAIL);			
		}
	}
	/* returned goods stuff */
	@RequestMapping(value = {SEC_PATH+"/returned" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject returnToStock(@RequestBody Map<String,Object> returnData) {
		try {
			return stockAgenda.returnToStock(returnData);
		} catch (Exception e) {
			e.printStackTrace();
			return buildErrorResponse("Return selhal", STATUS_FAIL);			
		}
	}

	/* company / country settings */
	@RequestMapping(value = {SEC_PATH+"/ccfilter/company" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject ccFilterCompany(@RequestBody Map<String,Object> ccData) {
		try {
			return business.ccFilterCompany(ccData);
		} catch (Exception e) {
			e.printStackTrace();
			return buildErrorResponse("Import info o dopravě selhal", STATUS_FAIL);			
		}
	}
	@RequestMapping(value = {SEC_PATH+"/ccfilter/country" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject ccFilterCountry(@RequestBody Map<Integer,Boolean> ccData) {
		try {
			return business.ccFilterCountry(ccData);
		} catch (Exception e) {
			e.printStackTrace();
			return buildErrorResponse("Nastavení vybraných zemí selhalo", STATUS_FAIL);			
		}
	}
	
	/* claims agenda */
	@RequestMapping(value = {SEC_PATH+"/claims" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getClaims() {
		return claimsAgenda.getClaims();
	}

	@RequestMapping(value = {SEC_PATH+"/repairs" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getRepairs() {
		return claimsAgenda.getRepairs();
	}
	
	@RequestMapping(value = {SEC_PATH+"/repairs/{repairId}" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getRepair(@PathVariable("repairId") int repairId) {
		return claimsAgenda.getRepair(repairId);
	}
	
	@RequestMapping(value = {SEC_PATH+"/repair/items/{repairId}" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getRepairItems(@PathVariable("repairId") int repairId) {
		return claimsAgenda.getRepairItems(repairId);
	}
	/*
	@RequestMapping(value = {SEC_PATH+"/repair/relateditems/{deliveryId}/{claimId}" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getRelatedItems(
			@PathVariable("deliveryId") int deliveryId,
			@PathVariable("claimId") int claimId
			) {
		try {
			return claimsAgenda.getRelatedItems(deliveryId, claimId);
		} catch (WrongInputException e) {
			return buildErrorResponse("Čtení related items selhalo", STATUS_FAIL);
		}
	}
	*/
	
	@RequestMapping(value = {SEC_PATH+"/repair/items/add" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject addRepairItem(@RequestBody Map<String,Object> itemData) {
		try {
			return claimsAgenda.addRepairItem(itemData);
		} catch (WrongInputException e) {
			e.printStackTrace();
			return buildErrorResponse("Přidíní položky k objednávce selhalo", STATUS_FAIL);			
		}
	}
	
	@RequestMapping(value = {SEC_PATH+"/repair/items/delete" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject deleteRepairItem(@RequestBody Map<String,Object> itemData) {
		try {
			return tradeAgenda.deleteOrderItem(itemData);
		} catch (WrongInputException | WrongDataException e) {
			e.printStackTrace();
			return buildErrorResponse("Smazání položky opravy selhalo", STATUS_FAIL);			
		}
	}

	@RequestMapping(value = {SEC_PATH+"/repair/finish" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject finishRepair(@RequestBody Map<String,Object> repairData) {
		try {
			return claimsAgenda.finishRepair(repairData);
		} catch (WrongInputException | WrongDataException e) {
			e.printStackTrace();
			return buildErrorResponse("Dokončení opravy selhalo", STATUS_FAIL);			
		}
	}
	
	
	
	@RequestMapping(value = {SEC_PATH+"/repairs/next" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject nextRepair(@RequestBody Map<String,Object> repairData) {
		try {
			return claimsAgenda.nextRepair(repairData);
		} catch (Exception e) {
			e.printStackTrace();
			return buildErrorResponse("Vytvoření opravy selhalo", STATUS_FAIL);			
		}
	}

	@RequestMapping(value = {SEC_PATH+"/servicerates" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject getServiceRates() {
		return claimsAgenda.getServiceRates();
	}


	/* TESTING STUFF */
	
	@RequestMapping(value = {SEC_PATH+"/test/buildstock" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject buildStock(@RequestBody Map<String,Object> buildData) {
		try {
			return stockAgenda.testBuildStock(buildData);
		} catch (Exception e) {
			e.printStackTrace();
			return buildErrorResponse("Vytvoření skladu selhalo", STATUS_FAIL);			
		}
	}
	
	
	
}