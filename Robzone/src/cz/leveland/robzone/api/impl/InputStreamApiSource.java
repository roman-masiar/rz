package cz.leveland.robzone.api.impl;

import java.io.FileInputStream;
import java.util.Map;

import cz.leveland.robzone.api.APIConnectionException;
import cz.leveland.robzone.api.AbstractApiSource;
import cz.leveland.robzone.api.ApiSource;

public class InputStreamApiSource extends AbstractApiSource implements ApiSource{

	
	public InputStreamApiSource(String fileRoot, String importPath) {
		
		super(fileRoot, importPath);	
	}


	@Override
	public FileInputStream getData(int type, Map<String, Object> arguments) throws APIConnectionException {
		
		return getInputStream(type);
	}

}
