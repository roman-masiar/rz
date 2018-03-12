package cz.leveland.robzone.api.impl;

import cz.leveland.robzone.api.ApiConnector;
import cz.leveland.robzone.api.ApiContentParser;
import cz.leveland.robzone.api.ApiSource;

public class PaymentApiConnector extends ApiConnector {

	public PaymentApiConnector(ApiSource source, ApiContentParser contentParser) {
		super(source, contentParser);
		
	}
}
