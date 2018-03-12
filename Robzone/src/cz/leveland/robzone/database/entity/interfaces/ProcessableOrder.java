package cz.leveland.robzone.database.entity.interfaces;

public interface ProcessableOrder {

	Integer getId();
	int getPartnerId();
	String getOrderNo();
	Integer getPaymentTypeId();
	Integer getTransportTypeId();
	
}
