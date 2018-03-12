package cz.leveland.robzone.database.entity.dto;

public class PickStockItemDto {
	
	int orderItemId;
	String productName;
	boolean setProduct;
	int productId;
	int orderId;
	Integer si1id;
	String si1name;
	boolean si1sn;
	Integer si2id;
	String si2name;
	boolean si2sn;
	
	public PickStockItemDto() {
		super();
	}

	public int getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public boolean isSetProduct() {
		return setProduct;
	}

	public void setSetProduct(Character setProduct) {
		if (setProduct == null)
			this.setProduct = false;
		else
			this.setProduct = setProduct == 'T' || setProduct == 't';
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public Integer getSi1id() {
		return si1id;
	}

	public void setSi1id(Integer si1id) {
		this.si1id = si1id;
	}

	public String getSi1name() {
		return si1name;
	}

	public void setSi1name(String si1name) {
		this.si1name = si1name;
	}

	public boolean isSi1sn() {
		return si1sn;
	}

	public void setSi1sn(Character si1sn) {
		if (si1sn == null)
			this.si1sn = false;
		else
			this.si1sn = si1sn == 'T' || si1sn == 't';
	}

	public Integer getSi2id() {
		return si2id;
	}

	public void setSi2id(Integer si2id) {
		this.si2id = si2id;
	}

	public String getSi2name() {
		return si2name;
	}

	public void setSi2name(String si2name) {
		this.si2name = si2name;
	}

	public boolean isSi2sn() {
		return si2sn;
	}

	public void setSi2sn(Character si2sn) {
		
		if (si2sn == null)
			this.si2sn = false;
		else
			this.si2sn = si2sn == 'T' || si2sn == 't';
	}

	
}
