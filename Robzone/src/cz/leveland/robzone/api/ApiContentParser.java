package cz.leveland.robzone.api;

import java.util.Map;

public interface ApiContentParser {

	public Object parse(Object data, int requestType, Map<String,Object> args) throws APIParseException;
	public void setup(Integer id);
}
