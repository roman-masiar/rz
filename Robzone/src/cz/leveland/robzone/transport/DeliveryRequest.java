package cz.leveland.robzone.transport;

import cz.leveland.robzone.database.entity.Order;
import cz.leveland.robzone.database.entity.Partner;

public class DeliveryRequest {

	Order order;
	int numberOfPackages;
	Partner customer;
	double weight;
	int distance;
	Partner transporter;
	
	
	/* is filled when package is made */
	String packageNumber;
	
	
	
	public DeliveryRequest(Order order, int numberOfPackages, Partner customer, Partner transporter) {
		super();
		this.order = order;
		this.numberOfPackages = numberOfPackages;
		this.customer = customer;
		this.transporter = transporter;
	}
	
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public int getNumberOfPackages() {
		return numberOfPackages;
	}
	public void setNumberOfPackages(int numberOfPackages) {
		this.numberOfPackages = numberOfPackages;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}

	public Partner getCustomer() {
		return customer;
	}

	public void setCustomer(Partner customer) {
		this.customer = customer;
	}


	public String getPackageNumber() {
		return packageNumber;
	}

	public void setPackageNumber(String packageNumber) {
		this.packageNumber = packageNumber;
	}

	public Partner getTransporter() {
		return transporter;
	}

	public void setTransporter(Partner transporter) {
		this.transporter = transporter;
	}

	
}
