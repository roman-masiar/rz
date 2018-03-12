package cz.leveland.robzone.api.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import cz.leveland.appbase.database.exceptions.WrongInputException;
import cz.leveland.robzone.api.APIParseException;
import cz.leveland.robzone.api.ApiContentParser;
import cz.leveland.robzone.api.XMLContentParser;
import cz.leveland.robzone.xml.OrderImportConverter;

public class OrderApiContentParser extends XMLContentParser implements ApiContentParser{

	OrderImportConverter importConverter;
	
	
	@Override
	public Object parse(Object text, int type, Map<String, Object> args) throws APIParseException {
				
		/* type == countryId for partner to be stored */
		
		Document document;
		try {
			String textFile = (String)text;
			InputStream stream = new ByteArrayInputStream(textFile.getBytes(StandardCharsets.UTF_8.name()));
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();	
			//InputSource is = new InputSource(new StringReader(textFile));
			document = db.parse((InputStream)stream);						
		}catch(Exception e) {
			e.printStackTrace();
			throw new APIParseException();
		}
		
		Element docEle = ((Document) document).getDocumentElement();
		List<Element> list = parseDocElement(docEle, "ORDER");
		int countryId = (int)args.get("countryId");
		List<Map<String,Object>> orders = addOrders(list, countryId);
		return orders;		
	}
	
	
	
	/* parse orders */
	public List<Map<String,Object>> addOrders (List<Element> list, int countryId) throws APIParseException {
		
		
		List<Map<String,Object>> orders = new ArrayList<Map<String,Object>>();
		
		for (Element orderElement:list) {
		
			/* create order with header and items */
			Map<String,Object> order = new HashMap<String,Object>();
			Map<String,String> orderHeader = new HashMap<String,String>();
			List<Map<String,String>> orderItems = new ArrayList<Map<String,String>>();
			
			
			String orderCode = getTextValue(orderElement, "CODE");
			String orderDate = getTextValue(orderElement, "DATE");
			
			NodeList currencyNl = orderElement.getElementsByTagName("CURRENCY");
			Element currencyEle = (Element) currencyNl.item(0);
			
			String currency = getTextValue(currencyEle, "CODE");
			
			String orderPrice = getSubTextValue(orderElement, "TOTAL_PRICE", "WITH_VAT");
			String orderVat = getSubTextValue(orderElement, "TOTAL_PRICE", "VAT");
			String orderRounding = getSubTextValue(orderElement, "TOTAL_PRICE", "ROUNDING");
			String priceToPay = getSubTextValue(orderElement, "TOTAL_PRICE", "PRICE_TO_PAY");
			
			NodeList customersNl = orderElement.getElementsByTagName("CUSTOMER");
			Element customerEle = (Element) customersNl.item(0);
			
			String email = getTextValue(customerEle, "EMAIL");
			String phone = getTextValue(customerEle, "PHONE");
			
			NodeList addressNl = orderElement.getElementsByTagName("BILLING_ADDRESS");
			Element addressEle = (Element) addressNl.item(0);
			
			String name = getTextValue(addressEle, "NAME");
			String street = getTextValue(orderElement, "STREET");
			String city = getTextValue(orderElement, "CITY");
			String zip = getTextValue(addressEle, "ZIP");
			String companyId = getTextValue(orderElement, "COMPANY_ID");
			
			
			
			orderHeader.put("date", orderDate);
			orderHeader.put("code", orderCode);
			orderHeader.put("email", email);
			orderHeader.put("phone", phone);
			orderHeader.put("name", name);
			orderHeader.put("street", street);
			orderHeader.put("city", city);
			orderHeader.put("zip", zip);
			orderHeader.put("companyId", companyId);
			orderHeader.put("currency", currency);
			
			orderHeader.put("price", orderPrice);
			orderHeader.put("priceToPay", priceToPay);
			orderHeader.put("rounding", orderRounding);
			orderHeader.put("vat", orderVat);
			
			
			
			
			
			List<Element> itemsList = parseDocElement(orderElement, "ITEM");
			for (Element orderItemElement:itemsList) {
				
				String productType = getTextValue(orderItemElement, "TYPE");
				String productName = getTextValue(orderItemElement, "NAME");
				String amount = getTextValue(orderItemElement, "AMOUNT");				
				String productCode = getTextValue(orderItemElement, "CODE");
				
				NodeList unitPriceNl = orderItemElement.getElementsByTagName("UNIT_PRICE");
				Element unitPriceEle = (Element) unitPriceNl.item(0);
				String unitPrice = getTextValue(unitPriceEle, "WITHOUT_VAT");				
				String vat = getTextValue(unitPriceEle, "VAT");
				String vatRate = getTextValue(unitPriceEle, "VAT_RATE");
				
				NodeList priceNl = orderItemElement.getElementsByTagName("TOTAL_PRICE");
				Element priceEle = (Element) priceNl.item(0);
				String price = getTextValue(priceEle, "WITH_VAT");
				
				
				Map<String,String> orderItem = new HashMap<String,String>();
				orderItem.put("type", productType);
				orderItem.put("name", productName);
				orderItem.put("code", productCode);
				orderItem.put("amount", amount);
				orderItem.put("unitPrice", unitPrice);
				orderItem.put("vat", vat);
				orderItem.put("vatRate", vatRate);

				//orderItem.put("price", price);
				orderItems.add(orderItem);
			}
			
			order.put("header", orderHeader);
			order.put("items", orderItems);
			
			Map<String, Object> partnerAndOrder;
			try {
				partnerAndOrder = importConverter.getOrder(order, countryId);				
			} catch (WrongInputException e) {
				throw new APIParseException();
			}
			orders.add(partnerAndOrder);
		}
		
		return orders;

	}
	
	


	public void setImportConverter(OrderImportConverter importConverter) {
		this.importConverter = importConverter;
	}



	@Override
	public void setup(Integer companyId) {
		
		
	}



	public OrderImportConverter getImportConverter() {
		return importConverter;
	}
	

}
