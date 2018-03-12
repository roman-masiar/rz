package cz.leveland.robzone.api;

import java.util.Map;

import cz.leveland.appbase.database.exceptions.WrongInputException;

public class ApiConnector {

	
	ApiSource source;
	ApiContentParser contentParser;
	Integer requestType = null;
	
	
	
	public ApiConnector(ApiSource source, ApiContentParser contentParser) {
		this.source = source;
		this.contentParser = contentParser;
	}




	public Object getData() throws APIConnectionException, APIParseException, WrongInputException {
		return getData(AbstractApiSource.TYPE_IMPORT, null);
	}
	
	public Object getData(int type) throws APIConnectionException, APIParseException, WrongInputException {
		return getData(type, null);
	}
	
	public Object getData(Map<String,Object> arguments) throws APIConnectionException, APIParseException, WrongInputException {
		return getData(AbstractApiSource.TYPE_IMPORT, arguments);
	}
	
	
	public Object getData(Integer type, Map<String,Object> arguments) throws APIConnectionException, APIParseException, WrongInputException {

		requestType = type;
		Object data = source.getData(type, arguments);
		return contentParser.parse(data, requestType, arguments);
	}
	
	public void setupParser(Integer id) {
		contentParser.setup(id);
	}

	public ApiContentParser getContentParser() {
		return contentParser;
	}
	
	
}
