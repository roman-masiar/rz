package cz.leveland.robzone.api.impl;

import java.util.Map;

import cz.leveland.appbase.database.exceptions.WrongInputException;
import cz.leveland.robzone.api.APIConnectionException;
import cz.leveland.robzone.api.AbstractApiSource;
import cz.leveland.robzone.api.ApiSource;

public class PaymentFioApiSource extends AbstractApiSource implements ApiSource{

	
	@SuppressWarnings("unused")
	private String token;
	
	
	public PaymentFioApiSource(String url, String token) {
		super(url);	
		this.token = token;
	}




	@Override
	public String getData(int type, Map<String, Object> arguments) throws APIConnectionException, WrongInputException {

		/*
		String fromDate = (String)arguments.get("from");
		String toDate = (String)arguments.get("to");
		
		String urlWithToken = url.replace("token", token);
		String urlFrom = urlWithToken.replace("date-from", fromDate);
		String urlFinal = urlFrom.replace("date-to", toDate);
		*/
		
		return getDataFromUrl(type);
	}

	 
}

