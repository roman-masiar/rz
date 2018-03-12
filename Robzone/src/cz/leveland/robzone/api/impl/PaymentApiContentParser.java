package cz.leveland.robzone.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import cz.leveland.robzone.api.APIParseException;
import cz.leveland.robzone.api.ApiContentParser;
import cz.leveland.robzone.database.entity.Payment;

public class PaymentApiContentParser implements ApiContentParser{

	@Override
	public Object parse(Object paymentsString, int type, Map<String, Object> args) throws APIParseException {
		
		
		try {
		    JSONArray paymentsJSON = new JSONArray((String)paymentsString);
		    List<Payment> payments = new ArrayList<Payment>();
		    
		    for (int i=0; i<paymentsJSON.length(); i++) {
		    	JSONObject one = paymentsJSON.getJSONObject(i);
		    	Payment payment = new Payment(one);
		    	payments.add(payment);
		    	
		    }
		    return payments;
		} catch (Exception e) {
			throw new APIParseException();
		}
	    
	}

	@Override
	public void setup(Integer id) {
		// TODO Auto-generated method stub
		
	}
	
	}
