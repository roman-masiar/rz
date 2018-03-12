package cz.leveland.robzone.xml;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.leveland.appbase.database.exceptions.WrongInputException;
import cz.leveland.robzone.database.entity.Country;
import cz.leveland.robzone.database.entity.Currency;
import cz.leveland.robzone.database.entity.Order;
import cz.leveland.robzone.database.entity.OrderItem;
import cz.leveland.robzone.database.entity.Partner;
import cz.leveland.robzone.database.entity.ProductNature;
import cz.leveland.robzone.database.entity.UnknownProduct;
import cz.leveland.robzone.database.entity.dto.ProductDto;
import cz.leveland.robzone.database.entity.dto.ProductTypeDto;

public class OrderImportConverter {

	private Map<String,ProductDto> products = new HashMap<String,ProductDto>();
	private Map<String,ProductTypeDto> nonGoodsTypes = new HashMap<String,ProductTypeDto>();
	private Map<String,Currency> currencies = new HashMap<String,Currency>();
	private Map<String,Country> countries = new HashMap<String,Country>();
	private int  warehouseId;
	
	

	public void setWarehouseId(int warehouseId) {
		this.warehouseId = warehouseId;
	}

	public void setProducts(Map<String,ProductDto> products) {
		this.products = products;
	}
	
	@SuppressWarnings("unchecked")
	public void setConversionData(Map<String,Object> data) {
		//products = (Map<String,ProductDto>)data.get("products");
		nonGoodsTypes = (Map<String,ProductTypeDto>)data.get("nonGoodsTypes");
		currencies = (Map<String,Currency>)data.get("currencies");
		countries = (Map<String,Country>)data.get("countries");
	}
	
	

	@SuppressWarnings("unchecked")
	public Map<String, Object> getOrder(Map<String, Object> order, int countryId) throws WrongInputException {
		
		List<Map<String,String>> orderItemsString = (List<Map<String,String>>)order.get("items");
		Map<String,String> orderHeaderString = (Map<String,String>)order.get("header");
		List<UnknownProduct> unKnownProducts = new ArrayList<UnknownProduct>();
		
		/* --- create order */
		String orderNo = orderHeaderString.get("code");
		String orderDateString = orderHeaderString.get("date");
		String priceString = orderHeaderString.get("price");
		String priceToPayString = orderHeaderString.get("priceToPay");
		String roundingString = orderHeaderString.get("rounding");
		String vatString = orderHeaderString.get("vat");
		
		
		
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
	    Date orderDate;
	    double orderPrice,  priceToPay,  rounding,  vat;
		try {
			orderDate = df.parse(orderDateString);
			
			orderPrice = Double.parseDouble(priceString);
			priceToPay = Double.parseDouble(priceToPayString);
			rounding = Double.parseDouble(roundingString);
			vat = Double.parseDouble(vatString);
			
		} catch (ParseException e) {
			throw new WrongInputException();
		}
		
		/*
		Map<String,Object> orderExtras = getOrderExtras(orderItemsString);
		Integer transportTypeId = (Integer)orderExtras.get("transportTypeId");
		Integer paymentTypeId = (Integer)orderExtras.get("paymentTypeId");
		if (transportTypeId == null || paymentTypeId == null)
			throw new WrongInputException();
		*/
		
		/* --- create partner */
		Partner newPartner = getPartner(orderHeaderString, countryId);
		if (newPartner == null)
			throw new WrongInputException();
		
		String currencyCode = orderHeaderString.get("currency");
		Currency currency = currencies.get(currencyCode);
		if (newPartner == null)
			throw new WrongInputException();
		
		/* id will be decided later */
		Order newOrder =  new Order(orderDate, orderNo, warehouseId, currency.getId(), 0, null);
		newOrder.setPrice(orderPrice);
		newOrder.setPriceToPay(priceToPay);
		newOrder.setVat(vat);
		newOrder.setRounding(rounding);
				
		/* --- crate order items --- */
		List<OrderItem> orderItems = getOrderItems(newOrder, orderItemsString, unKnownProducts);
		
		Map<String, Object> partnerAndOrder = new HashMap<String, Object>();
		partnerAndOrder.put("partner", newPartner);
		partnerAndOrder.put("order", newOrder);
		partnerAndOrder.put("orderItems", orderItems);
		partnerAndOrder.put("unknownProducts", unKnownProducts);
		
		return partnerAndOrder;
	}

	
	
	private List<OrderItem> getOrderItems(Order order, List<Map<String, String>> orderItems, List<UnknownProduct> unKnownProducts) throws WrongInputException {
		
		Integer productId = 0, qty = 0;
		double unitPrice = 0.0, vat = 0.0, vatRate = 0.0;
		List<OrderItem> result = new ArrayList<OrderItem>();
		
		
		for (Map<String,String> one: orderItems) {
			
				String code = one.get("code");
				productId = getProductId(code);
				if(productId == null) { 
					String productName = one.get("name");
					unKnownProducts.add(new UnknownProduct(0, 0, code, productName));
					continue;
				}
				
				qty = Integer.parseInt(one.get("amount"));
				//price = Double.parseDouble(one.get("price"));
				unitPrice = Double.parseDouble(one.get("unitPrice"));
				vat = Double.parseDouble(one.get("vat"));
				vatRate = Double.parseDouble(one.get("vatRate"));
				OrderItem orderItem = new OrderItem(0, productId, null, qty, unitPrice, 0.0, 0.0);
				orderItem.setVat(vat);
				orderItem.setVatRate(vatRate);
				getTansportAndPaymentType(code, order);
				result.add(orderItem);

			
		}
		return result;
	}



	private void getTansportAndPaymentType(String code, Order order) {
		
		if (!nonGoodsTypes.containsKey(code))
			return;
		ProductTypeDto pt = nonGoodsTypes.get(code);
		if (pt.getProductNatureId() == ProductNature.TYPE_PAYMENT)
			order.setPaymentTypeId(pt.getProductTypeId());
		if (pt.getProductNatureId() == ProductNature.TYPE_TRANSPORT)
			order.setTransportTypeId(pt.getProductTypeId());
	}



	public Partner getPartner(Map<String, String> header, int countryId) throws WrongInputException {
		
		String name = header.get("name");
		String email = header.get("email");
		String phone = header.get("phone");
		String city = header.get("city");
		String street = header.get("street");
		String zip = header.get("zip");
		String regNo = header.get("companyId");
		String taxNo = header.get("vatId");
		
		String[] nameSurname = name.split(" ");
		@SuppressWarnings("unused")
		String firstName = ".", familyName = "";
		if (nameSurname.length > 1) {
			firstName = nameSurname[0]; 
			familyName = nameSurname[1];
			if (nameSurname.length > 2)
				familyName += " " + nameSurname[2];
		} else
			firstName = name;
		
		Partner partner = new Partner(countryId, Partner.TYPE_CUSTOMER, name, familyName, regNo, taxNo, city, street, zip, phone, email);
		return partner;
	}
	
	
	
	private  Integer getProductId(String code) {

		if (!products.containsKey(code))
			return null;
		return products.get(code).getId();
	}
	
	
}
