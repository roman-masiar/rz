package cz.leveland.robzone.api.impl;

import java.util.Map;

import cz.leveland.appbase.database.exceptions.WrongInputException;
import cz.leveland.robzone.api.APIConnectionException;
import cz.leveland.robzone.api.AbstractApiSource;
import cz.leveland.robzone.api.ApiSource;

public class OrderSimulApiSource extends AbstractApiSource implements ApiSource{
	
	public OrderSimulApiSource(String url) {
		super(url);	
		
	}

	@Override
	public String getData(int type, Map<String, Object> arguments) throws APIConnectionException, WrongInputException {
		
		return getDataFromUrl(type);
	}
}

