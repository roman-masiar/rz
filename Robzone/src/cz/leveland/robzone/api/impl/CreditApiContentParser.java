package cz.leveland.robzone.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import cz.leveland.robzone.api.APIParseException;
import cz.leveland.robzone.api.AbstractApiSource;
import cz.leveland.robzone.api.ApiContentParser;
import cz.leveland.robzone.database.entity.Credit;

public class CreditApiContentParser implements ApiContentParser{

	@Override
	public Object parse(Object creditsString, int type, Map<String, Object> args) throws APIParseException {
		
		try {
			if (type == AbstractApiSource.TYPE_IMPORT) {
			
			    JSONArray creditsJSON = new JSONArray((String)creditsString);
			    List<Credit> credits = new ArrayList<Credit>();
			    
			    for (int i=0; i<creditsJSON.length(); i++) {
			    	JSONObject one = creditsJSON.getJSONObject(i);
			    	Credit credit = new Credit(one);
			    	credits.add(credit);
			    	
			    }
			    return credits;
			
			} else if (type == AbstractApiSource.TYPE_UPDATE) {
				
				    JSONArray creditsJSON = new JSONArray((String)creditsString);
				    List<Credit> credits = new ArrayList<Credit>();
				    
				    for (int i=0; i<creditsJSON.length(); i++) {
				    	JSONObject one = creditsJSON.getJSONObject(i);
				    	Credit credit = new Credit();
				    	credit.fillUpdateData(one);
				    	credits.add(credit);
				    	
				    }
				    return credits;
			} else
				throw new APIParseException();
		} catch (Exception e) {
			throw new APIParseException();
		}
	    
	}

	@Override
	public void setup(Integer id) {
		// TODO Auto-generated method stub
		
	}

	}
