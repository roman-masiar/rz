package cz.leveland.robzone.api.impl;

import java.util.Map;

import cz.leveland.appbase.database.exceptions.WrongInputException;
import cz.leveland.robzone.api.APIConnectionException;
import cz.leveland.robzone.api.AbstractApiSource;
import cz.leveland.robzone.api.ApiSource;

public class CreditFileApiSource extends AbstractApiSource implements ApiSource {
	
	
	public static final String PATH_UPDATE = "creditupdates.json";
	
	public CreditFileApiSource(String fileRoot, String importPath) {

		super(fileRoot, importPath);		
		addPath(TYPE_UPDATE, PATH_UPDATE);
	}

	
	
	@Override
	public Object getData(int type, Map<String, Object> arguments) throws APIConnectionException, WrongInputException {
		
		return readFile(type);
	}

	

}
