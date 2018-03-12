package cz.leveland.robzone.database.entity.dto;

import java.io.Serializable;
import java.util.Date;

import cz.leveland.robzone.database.entity.interfaces.ProcessableOrder;

public class OrderDto  implements Serializable, ProcessableOrder{


	private static final long serialVersionUID = 1L;

	

	private Integer id;
	private String orderNo;
	private byte type;	
	private String paymentRefNo;
	private int partnerId;
	private Integer transportTypeId;
	private Integer transProdId;
	private Integer warehouseId;
	private String transportName;
	private Integer paymentTypeId;
	private Integer payProdId;
	private String paymentName;
	private Integer origPaymentType;
	private Date created;
	private int status;
	private String phone;
	private String email;
	private String name;
	private String familyName;
	private String city;
	private String street;
	private String zip;
	private int countryId;
	
	private String countryCode;
	private Double webPrice;
	private Double webItemPrice;
	private Double webItemVat;
	private Double paid;
	/*
	private Boolean approved;
	private String creditRefNo;
	*/
	private Double creditAmount;
	
	private Double productPrice;
	private Double priceToPay;
	private String currency;
	private Integer currencyId;
	private Number orderedItems;
	private Number pickedItems;	
	private Number shippedItems;
	private Date delivered;
	private boolean missing;
	private boolean delivOk;
	private boolean returned;
	
	public OrderDto() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public String getPaymentRefNo() {
		return paymentRefNo;
	}

	public void setPaymentRefNo(String paymentRefNo) {
		this.paymentRefNo = paymentRefNo;
	}

	public int getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(int partnerId) {
		this.partnerId = partnerId;
	}

	public Integer getTransportTypeId() {
		return transportTypeId;
	}

	public void setTransportTypeId(Integer transportTypeId) {
		this.transportTypeId = transportTypeId;
	}

	public Integer getPaymentTypeId() {
		return paymentTypeId;
	}

	public void setPaymentTypeId(Integer paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public Double getWebPrice() {
		return webPrice;
	}

	public void setWebPrice(Double webPrice) {
		this.webPrice = webPrice;
	}

	public Double getWebItemPrice() {
		return webItemPrice;
	}

	public void setWebItemPrice(Double webItemPrice) {
		this.webItemPrice = webItemPrice;
	}

	public Double getPaid() {
		return paid;
	}

	public void setPaid(Double paid) {
		this.paid = paid;
	}

	/*
	public Boolean getApproved() {
		return approved;
	}

	
	public void setApproved(Boolean approved) {
		this.approved = approved;
	}
	

	public void setApproved(Character approved) {
		if (approved == null)
			this.approved = null;
		else
			this.approved = approved == 'T' || approved == 't';
	}
	
	public String getCreditRefNo() {
		return creditRefNo;
	}

	public void setCreditRefNo(String creditRefNo) {
		this.creditRefNo = creditRefNo;
	}
	*/

	public Double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(Double productPrice) {
		this.productPrice = productPrice;
	}

	public Double getPriceToPay() {
		return priceToPay;
	}

	public void setPriceToPay(Double priceToPay) {
		this.priceToPay = priceToPay;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Number getOrderedItems() {
		return orderedItems;
	}

	public void setOrderedItems(Number orderedItems) {
		this.orderedItems = orderedItems;
	}

	public Number getPickedItems() {
		return pickedItems;
	}

	public void setPickedItems(Number pickedItems) {
		this.pickedItems = pickedItems;
	}

	public String getTransportName() {
		return transportName;
	}

	public void setTransportName(String transportName) {
		this.transportName = transportName;
	}

	public String getPaymentName() {
		return paymentName;
	}

	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
	}

	public Integer getTransProdId() {
		return transProdId;
	}

	public void setTransProdId(Integer transProdId) {
		this.transProdId = transProdId;
	}

	public Integer getPayProdId() {
		return payProdId;
	}

	public void setPayProdId(Integer payProdId) {
		this.payProdId = payProdId;
	}

	public Number getShippedItems() {
		return shippedItems;
	}

	public void setShippedItems(Number shippedItems) {
		this.shippedItems = shippedItems;
	}

	public Date getDelivered() {
		return delivered;
	}

	public void setDelivered(Date delivered) {
		this.delivered = delivered;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public Double getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(Double creditAmount) {
		this.creditAmount = creditAmount;
	}

	public Integer getOrigPaymentType() {
		return origPaymentType;
	}

	public void setOrigPaymentType(Integer origPaymentType) {
		this.origPaymentType = origPaymentType;
	}

	public boolean isMissing() {
		return missing;
	}

	public void setMissing(Number missing) {
		
		this.missing = missing.intValue() > 0;
	}

	public boolean isDelivOk() {
		return delivOk;
	}

	public void setDelivOk(Number delivOk) {
		this.delivOk = delivOk.intValue() > 0;
	}

	public boolean isReturned() {
		return returned;
	}

	public void setReturned(Number returned) {
		this.returned = returned.intValue() > 0;;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Integer getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
	}

	public Double getWebItemVat() {
		return webItemVat;
	}

	public void setWebItemVat(Double webItemVat) {
		this.webItemVat = webItemVat;
	}

}
