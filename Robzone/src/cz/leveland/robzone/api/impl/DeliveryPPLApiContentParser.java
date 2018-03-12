package cz.leveland.robzone.api.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import cz.leveland.robzone.database.entity.dto.DeliveryInfo;
import cz.leveland.robzone.xml.OrderImportConverter;

public class DeliveryPPLApiContentParser extends XMLContentParser implements ApiContentParser{

	
	
	
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
		
		
		return getDeliveryInfo(docEle);
				
	}
	
	
	
	
	public List<DeliveryInfo> getDeliveryInfo (Element docEle) throws APIParseException {
		
		
		List<DeliveryInfo> deliveryInfos = new ArrayList<DeliveryInfo>();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		List<Element> list = parseDocElement(docEle, "MyApiPackageOut");
		for (Element packageElement:list) {
				
			String packNumber = getTextValue(packageElement, "PackNumber");
			String delivDate = getTextValue(packageElement, "DelivDate");
			String backDate = getTextValue(packageElement, "BackDate");
			String note = getTextValue(packageElement, "Note");			
			String name = getSubTextValue(packageElement, "Recipient", "Name");
			
		    Date deliveryDate = null, sendBackDate = null;
			try {
				if (delivDate != null)
					deliveryDate = df.parse(delivDate);
				if (backDate != null)
					sendBackDate = df.parse(backDate);
			} catch (ParseException e) {
				throw new APIParseException();
			}

			NodeList statusesList = packageElement.getElementsByTagName("PackageStatuses");
			Element statuses = (Element) statusesList.item(0);
			//Element lastStatusElement = (Element) statuses.getLastChild();
			NodeList packStatusList = statuses.getElementsByTagName("MyApiPackageOutStatus");
			Integer lastStatus = null;
			if (packStatusList != null && packStatusList.getLength() > 0) {
				Element lastStatusElement = (Element) packStatusList.item(packStatusList.getLength()-1);
			
				
				if(lastStatusElement != null) {				
					String statusStr = getTextValue(packageElement, "StaID");
					lastStatus = convertStatus(Integer.parseInt(statusStr));
				}
			}

			DeliveryInfo deliveryInfo = new DeliveryInfo(0, packNumber, deliveryDate, lastStatus, note, name);
			deliveryInfo.setReturned(sendBackDate);
			deliveryInfos.add(deliveryInfo);			
		}
		
		return deliveryInfos;

	}
	
	

	private int convertStatus(int status) {
		
		switch(status) {
		case 450:
			return DeliveryInfo.STATUS_DELIVERED;
		default:
			return DeliveryInfo.STATUS_RETURNED;
		}
	}



	@Override
	public void setup(Integer companyId) {
		
		
	}



}
