package cz.leveland.robzone.configuration;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import cz.leveland.robzone.api.AbstractApiSource;
import cz.leveland.robzone.api.ApiSource;
import cz.leveland.robzone.api.impl.CreditApiConnector;
import cz.leveland.robzone.api.impl.CreditApiContentParser;
import cz.leveland.robzone.api.impl.CreditCofidisApiSource;
import cz.leveland.robzone.api.impl.CreditFileApiSource;
import cz.leveland.robzone.api.impl.DeliveryApiConnector;
import cz.leveland.robzone.api.impl.DeliveryPPLApiContentParser;
import cz.leveland.robzone.api.impl.DeliverySimulApiSource;
import cz.leveland.robzone.api.impl.OrderApiConnector;
import cz.leveland.robzone.api.impl.OrderApiContentParser;
import cz.leveland.robzone.api.impl.OrderSimulApiSource;
import cz.leveland.robzone.api.impl.PaymentApiConnector;
import cz.leveland.robzone.api.impl.PaymentApiContentParser;
import cz.leveland.robzone.api.impl.PaymentFioApiSource;
import cz.leveland.robzone.api.impl.TextFileApiSource;
import cz.leveland.robzone.xml.OrderImportConverter;

@Configuration
@ComponentScan(value = "cz.leveland.robzone")
public class ApiConfiguration {

	private static final String SOURCE_FILE = "file";
	private static final String SOURCE_API = "api";

	@Value("${FILE_ROOT}")
	private String fileRoot;
	
	
	
	/* --- payments */
	@Value("${API_SOURCE_PAYMENTS}")
	private String apiSourcePayments;

	@Value("${PAYMENT_FILE_NAME}")
	private String paymentFileName;
	
	@Value("${PAYMENT_API_URL}")
	private String paymentApiUrl;
	
	@Value("${PAYMENT_TOKEN}")
	private String paymentToken;

	
	/* --- credits */
	@Value("${API_SOURCE_CREDITS}")
	private String apiSourceCredits;
	
	@Value("${CREDIT_FILE_NAME}")
	private String creditFileName;
	
	@Value("${CREDIT_UPDATE_FILE_NAME}")
	private String creditUpdateFileName;	
	
	@Value("${CREDIT_API_URL}")
	private String creditApiUrl;
		
	@Value("${CREDIT_UPDATE_API_URL}")
	private String creditUpdateApiUrl;
	
	@Value("${CREDIT_TOKEN}")
	private String creditToken;
	
	
	/* --- orders */
	@Value("${API_SOURCE_ORDERS}")
	private String apiSourceOrders;
	
	@Value("${ORDER_FILE_NAME}")
	private String orderFileName;
	
	@Value("${ORDER_API_URL}")
	private String orderApiUrl;
		
	/* --- delivery */
	@Value("${API_SOURCE_DELIVERY}")
	private String apiSourceDelivery;
	
	@Value("${DELIVERY_FILE_NAME}")
	private String deliveryFileName;
	
	@Value("${DELIVERY_API_URL}")
	private String deliveryApiUrl;
	
	
	
	
	@Bean
	public OrderApiConnector getOrderApiConnector() {
		
		OrderApiContentParser contentParser = new OrderApiContentParser();
		contentParser.setImportConverter(new OrderImportConverter());
		
		ApiSource orderSource;
		if (apiSourceOrders.equals(SOURCE_FILE))
			orderSource = new TextFileApiSource(fileRoot, orderFileName);
		else if (apiSourceOrders.equals(SOURCE_API))
			orderSource = new OrderSimulApiSource(orderApiUrl);
		else 
			throw new BeanCreationException("Payment connector could not be created, unknown source in config file (*.properties)");
	
		
		return new OrderApiConnector(orderSource,contentParser);		
		
	}
	
	@Bean
	public PaymentApiConnector getPaymentApiConnector() {
		
		ApiSource paymentSource;
		if (apiSourcePayments.equals(SOURCE_FILE))
			paymentSource = new TextFileApiSource(fileRoot, paymentFileName);
		else if (apiSourcePayments.equals(SOURCE_API))
			paymentSource = new PaymentFioApiSource(paymentApiUrl, paymentToken);
		else
			throw new BeanCreationException("Payment connector could not be created, unknown source in config file (*.properties)");
	
		return new PaymentApiConnector(paymentSource,new PaymentApiContentParser());	
	}
	
	@Bean
	public CreditApiConnector getCreditApiConnector() {
		
		ApiSource creditSource;
		if (apiSourcePayments.equals(SOURCE_FILE)) {
			creditSource = new CreditFileApiSource(fileRoot, creditFileName);
			creditSource.addPath(AbstractApiSource.TYPE_UPDATE, creditUpdateFileName);
			
		} else if (apiSourcePayments.equals(SOURCE_API)) {
			creditSource = new CreditCofidisApiSource(creditApiUrl, "");
			creditSource.addPath(AbstractApiSource.TYPE_UPDATE, creditUpdateApiUrl);
		} else
			throw new BeanCreationException("Payment connector could not be created, unknown source in config file (*.properties)");
		
		return new CreditApiConnector(creditSource,new CreditApiContentParser());	
	}
	
	@Bean
	public DeliveryApiConnector getDeliveryApiConnector() {
		
		DeliveryPPLApiContentParser contentParser = new DeliveryPPLApiContentParser();
		
		ApiSource deliverySource;
		if (apiSourceDelivery.equals(SOURCE_FILE))
			deliverySource = new TextFileApiSource(fileRoot, deliveryFileName);
		else if (apiSourceDelivery.equals(SOURCE_API))
			deliverySource = new DeliverySimulApiSource(deliveryApiUrl);
		else 
			throw new BeanCreationException("Delivery connector could not be created, unknown source in config file (*.properties)");
	
		
		return new DeliveryApiConnector(deliverySource,contentParser);		
		
	}
	
	
}
