package cz.leveland.robzone.database.entity.dto;

import cz.leveland.robzone.database.entity.AbstractPojo;

public class OrderItemDto extends AbstractPojo {


	private Integer id;
	private int orderId;
	private Integer productId;
	private String productName;
	private String currency;
	private Double vatRate;
	private Double vat;
	private Double orderVatRate;
	private Double orderVat;
	private double qty ;
	private int unit;
	private double unitPrice ;
	private double price ;
	private Double productPrice ;
	private Double discount;
	private Double discountPercent;
	private Double totalPrice;
	private String description;
	private Number invoiceQty;
	
	public OrderItemDto() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(Double productPrice) {
		this.productPrice = productPrice;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getVat() {
		return doubleGetter(vat);
	}

	public void setVat(Double vat) {
		this.vat = vat;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getVatRate() {
		return doubleGetter(vatRate);
	}

	public void setVatRate(Double vatRate) {
		this.vatRate = vatRate;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(Double discountPercent) {
		this.discountPercent = discountPercent;
	}

	public int getUnit() {
		return unit;
	}

	public void setUnit(int unit) {
		this.unit = unit;
	}

	public int getInvoiceQty() {
		return intGetter(invoiceQty);
	}

	public void setInvoiceQty(Number invoiceQty) {
		this.invoiceQty = invoiceQty;
	}

	public double getOrderVatRate() {
		return doubleGetter(orderVatRate);
	}

	public void setOrderVatRate(Double orderVatRate) {
		this.orderVatRate = orderVatRate;
	}

	public Double getOrderVat() {
		return doubleGetter(orderVat);
	}

	public void setOrderVat(Double orderVat) {
		this.orderVat = orderVat;
	}
	
	
	
	
}
