package cz.leveland.robzone.database.entity.dto;

import java.util.LinkedHashMap;

public class Bundle extends LinkedHashMap<String,Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Integer getInt(String name) {
		return (Integer)get(name);
	}
	public Double getDouble(String name) {
		return (Double)get(name);
	}
	public String getString(String name) {
		return (String)get(name);
	}

}
