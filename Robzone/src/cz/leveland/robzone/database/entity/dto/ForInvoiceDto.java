package cz.leveland.robzone.database.entity.dto;

import java.io.Serializable;

public class ForInvoiceDto implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	int orderItemId;
	int orderId;
	Integer productId;
	Integer deliveryItemId;
	String name;
	String description;
	
	// ordered qty
	Number ordQty;
	
	// picked qty (in outstocks)
	Number outQty;
	
	// number of delivered packages
	Number sumDeliv;
	
	// number of undelivered packages
	Number sumUndeliv;
	
	// delivery qty (outstocks linked to delivery items)
	Number delQty;
	
	// price from order item
	Double unitPrice;
	Double price;
	Double discount;
	Double totalPrice;
	
	// price from product definition
	Double productUnitPrice;
	Double productPrice;
	
	Double vatRate;
	Double orderVatRate;
	Number outstocks;
	Number deliveries;
	
	public ForInvoiceDto() {
		super();
	}

	public int getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOrdQty() {
		if (ordQty == null)
			return 0;
		return ordQty.intValue();
	}

	public void setOrdQty(Number ordQty) {
		this.ordQty = ordQty;
	}

	public int getOutQty() {
		if (outQty == null)
			return 0;
		return outQty.intValue();
	}

	public void setOutQty(Number outQty) {
		this.outQty = outQty;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Double getVatRate() {
		return vatRate;
	}

	public void setVatRate(Double vatRate) {
		this.vatRate = vatRate;
	}

	public Integer getDeliveryItemId() {
		return deliveryItemId;
	}

	public void setDeliveryItemId(Integer deliveryItemId) {
		this.deliveryItemId = deliveryItemId;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(Double productPrice) {
		this.productPrice = productPrice;
	}

	public Double getProductUnitPrice() {
		return productUnitPrice;
	}

	public void setProductUnitPrice(Double productUnitPrice) {
		this.productUnitPrice = productUnitPrice;
	}

	public int getDelQty() {
		if (delQty == null)
			return 0;
		return delQty.intValue();
	}

	public void setDelQty(Number delQty) {
		this.delQty = delQty;
	}

	public Integer getOutstocks() {
		if (outstocks == null)
			return 0;
		return outstocks.intValue();
	}

	public void setOutstocks(Number outstocks) {
		this.outstocks = outstocks;
	}

	public int getDeliveries() {
		if (deliveries == null)
			return 0;
		return deliveries.intValue();
	}

	public void setDeliveries(Number deliveries) {
		this.deliveries = deliveries;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getSumDeliv() {
		if (sumDeliv == null)
			return 0;
		return sumDeliv.intValue();
	}

	public void setSumDeliv(Number sumDeliv) {
		this.sumDeliv = sumDeliv;
	}

	public int getSumUndeliv() {
		if (sumUndeliv == null)
			return 0;
		return sumUndeliv.intValue();
	}

	public void setSumUndeliv(Number sumUndeliv) {
		this.sumUndeliv = sumUndeliv;
	}

	public Double getOrderVatRate() {
		return orderVatRate;
	}

	public void setOrderVatRate(Double orderVatRate) {
		this.orderVatRate = orderVatRate;
	}
	
	
	

}
