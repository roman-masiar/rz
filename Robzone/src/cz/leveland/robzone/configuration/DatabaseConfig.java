package cz.leveland.robzone.configuration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.braintreegateway.Dispute.Reason;


import cz.leveland.appbase.database.entity.RequestLog;
import cz.leveland.appbase.database.entity.Settings;
import cz.leveland.robzone.database.entity.Claim;
import cz.leveland.robzone.database.entity.ClaimItem;
import cz.leveland.robzone.database.entity.Company;
import cz.leveland.robzone.database.entity.Country;
import cz.leveland.robzone.database.entity.Credit;
import cz.leveland.robzone.database.entity.Currency;
import cz.leveland.robzone.database.entity.Delivery;
import cz.leveland.robzone.database.entity.DeliveryItem;
import cz.leveland.robzone.database.entity.DeliveryPackage;
import cz.leveland.robzone.database.entity.DiscountItem;
import cz.leveland.robzone.database.entity.InStock;
import cz.leveland.robzone.database.entity.Invoice;
import cz.leveland.robzone.database.entity.InvoiceItem;
import cz.leveland.robzone.database.entity.Order;
import cz.leveland.robzone.database.entity.OrderChange;
import cz.leveland.robzone.database.entity.OrderItem;
import cz.leveland.robzone.database.entity.OutStock;
import cz.leveland.robzone.database.entity.Partner;
import cz.leveland.robzone.database.entity.Payment;
import cz.leveland.robzone.database.entity.Product;
import cz.leveland.robzone.database.entity.ProductCategory;
import cz.leveland.robzone.database.entity.ProductNature;
import cz.leveland.robzone.database.entity.ProductSetItem;
import cz.leveland.robzone.database.entity.ProductType;
import cz.leveland.robzone.database.entity.ServiceRate;
import cz.leveland.robzone.database.entity.StockItem;
import cz.leveland.robzone.database.entity.StockSet;
import cz.leveland.robzone.database.entity.StockSetItem;
import cz.leveland.robzone.database.entity.Unit;
import cz.leveland.robzone.database.entity.UnknownProduct;
import cz.leveland.robzone.database.entity.UserCompany;
import cz.leveland.robzone.database.entity.VatCategory;
import cz.leveland.robzone.database.entity.VatRate;
import cz.leveland.robzone.database.entity.Warehouse;



@Configuration
@EnableTransactionManagement
@ComponentScan({"cz.leveland.appbase"})
public class DatabaseConfig {
	
	
	@Value("${PROFILE}")
	private String activeProfile;

	@Value("${DATABASE}")
	private String database;
	
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		Resource resource;
	     
	    PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer =  new PropertySourcesPlaceholderConfigurer();
	     
	    // get active profile
	    
	    String activeProfile = System.getProperty("PROFILE");
	 
	    // choose different property files for different active profile
	    if ("development".equals(activeProfile)) {
	      resource = new ClassPathResource("development.properties");
	    } else if ("production".equals(activeProfile)) {
	      resource = new ClassPathResource("production.properties");
	    } else if ("testing".equals(activeProfile)) {
	    	resource = new ClassPathResource("testing.properties");
	    } else if ("debug".equals(activeProfile)) {
		  resource = new ClassPathResource("debug.properties");
	    } else if ("production".equals(activeProfile)) {
	      resource = new ClassPathResource("production.properties");
	    } else {
	    	resource = new ClassPathResource("empty.properties");
	    }
	     
	    // load the property file
	    propertySourcesPlaceholderConfigurer.setLocation(resource);
	     
	    return propertySourcesPlaceholderConfigurer;
		
	}
	
	
	
	
	@Bean 
	LocalSessionFactoryBean mySessionFactory () {
		
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		//sessionFactory.setDataSource(dataSource());
		sessionFactory.setAnnotatedClasses(
				cz.leveland.appbase.database.entity.User.class,
				Claim.class,
				ClaimItem.class,
				Country.class,
				Company.class,
				Credit.class,
				Currency.class,
				Delivery.class,
				DeliveryItem.class,
				InStock.class,
				Invoice.class,
				InvoiceItem.class,
				Order.class,
				OrderItem.class,
				OrderChange.class,
				OutStock.class,
				Partner.class,
				Payment.class,
				Product.class,
				ProductCategory.class,
				ProductSetItem.class,
				ProductType.class,
				ProductNature.class,
				Reason.class,
				RequestLog.class,
				Settings.class,
				StockItem.class,
				VatCategory.class,
				VatRate.class,
				Warehouse.class,
				UserCompany.class,
				UnknownProduct.class,
				StockSet.class,
				StockSetItem.class, 
				DeliveryPackage.class,
				DiscountItem.class,
				ServiceRate.class,
				Unit.class
				
				
			);
		
		sessionFactory.setHibernateProperties(hibernateProperties());
		
		return sessionFactory;
	}
	
	private Properties hibernateProperties() {
		
		String dbDatabase = database; //System.getProperty("DB_DATABASE");
		String dbUser = System.getProperty("DB_USER");
		String dbHost = System.getProperty("DB_HOST");
		String dbPassword = System.getProperty("DB_PASSWORD");
		
		System.out.println("Connection to: " + dbHost + "/" + dbDatabase + " as: " + dbUser);
		Properties props = new Properties();
		props.put("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
		props.put("hibernate.connection.url", "jdbc:mysql://"+dbHost+":3306/"+dbDatabase+"?characterEncoding=UTF-8");
		props.put("hibernate.connection.username", dbUser);
		props.put("hibernate.connection.password", dbPassword);
		props.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		props.put("hibernate.show_sql", false);
		props.put("hibernate.connection.CharSet", "utf8");
		props.put("hibernate.connection.characterEncoding", "utf8");
		props.put("hibernate.connection.useUnicode", "true");
		props.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
		props.put("hibernate.jdbc.batch_size", 20);
		props.put("hibernate.connection.provider_class", "org.hibernate.c3p0.internal.C3P0ConnectionProvider");		
		props.put("hibernate.c3p0.min_size", 5);
		props.put("hibernate.c3p0.max_size", 20);
		props.put("hibernate.c3p0.timeout", 300);
		props.put("hibernate.c3p0.max_statements", 50);
		props.put("hibernate.c3p0.idle_test_period", 3000);
		props.put("hibernate.c3p0.connectionCustomizerClassName", "cz.leveland.robzone.configuration.C3p0UseUtf8mb4");
		return props;
		
	}
	
	
	
	@Bean
	HibernateTransactionManager transactionManager () {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(mySessionFactory().getObject());
		return transactionManager;
	}
	
	
	


	
	
	
	
}

