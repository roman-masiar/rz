package cz.leveland.robzone.database.entity.dto;

import java.util.Date;

public class DeliveryDto implements java.io.Serializable {
 
	/*
	 * select name as name, orderid as orderId, "
				+ "currency as currency, price as totalPrice, outQty as qty 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String countryCode;
	private int countryId;
	private int companyId;
	private int type;
	private int orderId;	
	private int warehouseId;
	private Date shipped;	
	private Date created;	
	private Date delivered;	
	private Date returned;
	private Integer status;
	private int customerId;
	private int partnerId;
	private String name;
	private String email;
	private String phone;
	private String city;
	private String orderNo;
	private String transportName;
	private int transProdId;
	private String currency;
	private String packNumber;
	private double totalPrice;
	private double productItemPrice;
	private Number qty;
	private boolean backStock;
	
	
	public DeliveryDto() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public Date getShipped() {
		return shipped;
	}
	public void setShipped(Date shipped) {
		this.shipped = shipped;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getDelivered() {
		return delivered;
	}
	public void setDelivered(Date delivered) {
		this.delivered = delivered;
	}
	public int getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(int partnerId) {
		this.partnerId = partnerId;
	}

	public String getEmail() {
		return email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getTransportName() {
		return transportName;
	}
	public void setTransportName(String transportName) {
		this.transportName = transportName;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getPackNumber() {
		return packNumber;
	}
	public void setPackNumber(String packNumber) {
		this.packNumber = packNumber;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public double getProductItemPrice() {
		return productItemPrice;
	}
	public void setProductItemPrice(double productItemPrice) {
		this.productItemPrice = productItemPrice;
	}
	public Date getReturned() {
		return returned;
	}
	public void setReturned(Date returned) {
		this.returned = returned;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public int getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(int warehouseId) {
		this.warehouseId = warehouseId;
	}
	public int getTransProdId() {
		return transProdId;
	}
	public void setTransProdId(int transProdId) {
		this.transProdId = transProdId;
	}
	public int getQty() {
		if (qty == null)
			return 0;
		return qty.intValue();
	}
	public void setQty(Number qty) {
		this.qty = qty;
	}
	public boolean isBackStock() {
		return backStock;
	}
	public void setBackStock(Number backStock) {
		this.backStock = backStock != null && backStock.intValue() > 0;
		
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public int getCountryId() {
		return countryId;
	}
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	
}