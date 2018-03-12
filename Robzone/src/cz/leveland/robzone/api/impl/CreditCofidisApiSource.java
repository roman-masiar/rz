package cz.leveland.robzone.api.impl;

import java.util.Map;

import cz.leveland.appbase.database.exceptions.WrongInputException;
import cz.leveland.robzone.api.APIConnectionException;
import cz.leveland.robzone.api.AbstractApiSource;
import cz.leveland.robzone.api.ApiSource;

public class CreditCofidisApiSource extends AbstractApiSource implements ApiSource{

	public static final String PATH_UPDATE = "creditupdates.json";
	@SuppressWarnings("unused")
	private String token;
	
	
	public CreditCofidisApiSource(String url, String token) {
		super(url);	
		this.token = token;
		addPath(TYPE_UPDATE, PATH_UPDATE);
	}




	@Override
	public String getData(int type, Map<String, Object> arguments) throws APIConnectionException, WrongInputException {

		switch(type) {
			case AbstractApiSource.TYPE_IMPORT: url = url + ""; break;
			case AbstractApiSource.TYPE_UPDATE: url = url + ""; break;
		}
		
		return getDataFromUrl(type);
	}

	 
}

