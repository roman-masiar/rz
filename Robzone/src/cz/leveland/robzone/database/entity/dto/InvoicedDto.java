package cz.leveland.robzone.database.entity.dto;

public class InvoicedDto {
	
	private Number qty;
	private double price;
	private double vat;
	private Number vatRate;
	
	public InvoicedDto() {
	}
	public int getQty() {
		return qty.intValue();
	}
	public void setQty(Number qty) {
		this.qty = qty;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getVat() {
		return vat;
	}
	public void setVat(double vat) {
		this.vat = vat;
	}
	public double getVatRate() {
		return vatRate.doubleValue();
	}
	public void setVatRate(Number vatRate) {
		this.vatRate = vatRate;
	}
	
	
}
