package cz.leveland.robzone.api;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import cz.leveland.robzone.api.ApiContentParser;

public abstract class XMLContentParser implements ApiContentParser{

	
	
	protected List<Element> parseDocElement(Element docEle, String type){
		
		
		List<Element> list = new ArrayList<Element>(); 

		NodeList nl = docEle.getElementsByTagName(type);
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {

				Element el = (Element)nl.item(i);
				list.add(el);

			}
		}
		return list;
	}
	
	protected String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			Node node = el.getFirstChild();
			if (node != null)
				textVal = node.getNodeValue();
		}

		return textVal;
	}
	
	protected String getSubTextValue(Element rootElement, String tagName, String subTagName) {
		NodeList tagList = rootElement.getElementsByTagName(tagName);
		Element tagEle = (Element) tagList.item(0);			
		return getTextValue(tagEle, subTagName);
	}


	
	@SuppressWarnings("unused")
	protected int getIntValue(Element ele, String tagName) {
		//in production application you would catch the exception
		return Integer.parseInt(getTextValue(ele,tagName));
	}
	
	
	

}
