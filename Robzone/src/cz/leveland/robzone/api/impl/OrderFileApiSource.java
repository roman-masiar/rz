package cz.leveland.robzone.api.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import cz.leveland.robzone.api.APIConnectionException;
import cz.leveland.robzone.api.AbstractApiSource;
import cz.leveland.robzone.api.ApiSource;

public class OrderFileApiSource extends AbstractApiSource implements ApiSource{

	
	public OrderFileApiSource(String fileRoot, String importPath) {
		
		super(fileRoot, importPath);	
	}


	@Override
	public Document getData(int type, Map<String, Object> arguments) throws APIConnectionException {
		
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			File file = new File(fileRoot + getPath(type));
			InputStream docStream = new FileInputStream(file);
			Document doc = db.parse(docStream);			
			return doc;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
