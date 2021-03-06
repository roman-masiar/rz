package cz.leveland.robzone.api.impl;

import java.util.Map;

import cz.leveland.robzone.api.APIConnectionException;
import cz.leveland.robzone.api.AbstractApiSource;
import cz.leveland.robzone.api.ApiSource;

public class TextFileApiSource extends AbstractApiSource implements ApiSource{

	
	public TextFileApiSource(String fileRoot, String importPath) {
		
		super(fileRoot, importPath);	
	}


	@Override
	public String getData(int type, Map<String, Object> arguments) throws APIConnectionException {
		
		return readFile(type);
	}

}
