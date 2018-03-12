package cz.leveland.robzone.database.entity.dto;

import java.util.Date;


public class CreditDto implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;
	private int id;
	private String creditNo;	
	private String refNo;	
	private Integer orderId;	
	private Integer partnerId;	
	private Date created;	
	private Double amount;
	private String orderNo;
	private Boolean approved;

	


	public CreditDto() {
		super();
	}




	public int getId() {
		return id;
	}




	public void setId(int id) {
		this.id = id;
	}




	public String getRefNo() {
		return refNo;
	}




	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}




	public Integer getOrderId() {
		return orderId;
	}




	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}




	public Integer getPartnerId() {
		return partnerId;
	}




	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}




	public Date getCreated() {
		return created;
	}




	public void setCreated(Date created) {
		this.created = created;
	}




	public Double getAmount() {
		return amount;
	}




	public void setAmount(Double amount) {
		this.amount = amount;
	}




	public String getOrderNo() {
		return orderNo;
	}




	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	
	public Boolean isApproved() {
		return approved;
	}
	
	public void setApproved(Character approved) {
		
		if (approved == null) 
			this.approved = null;
		else		
			this.approved = approved=='T' || approved=='t';
	
	}




	public String getCreditNo() {
		return creditNo;
	}




	public void setCreditNo(String creditNo) {
		this.creditNo = creditNo;
	}

	
}