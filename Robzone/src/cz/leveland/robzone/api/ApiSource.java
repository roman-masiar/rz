package cz.leveland.robzone.api;

import java.util.Map;

import cz.leveland.appbase.database.exceptions.WrongInputException;

public interface ApiSource {

	public Object getData(int type, Map<String,Object> arguments) throws APIConnectionException, WrongInputException;
	public void addPath(int type, String path);
}
