package cz.leveland.robzone.database.entity.dto;

import java.util.Date;

public class ClaimDto implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String claimNo;
	private Integer companyId;
	private Integer userId;
	private int type;
	private Integer solverId;
	private int deliveryId;
	private int orderId;
	private String customer;
	private String orderNo;
	private String claimOrderNo;
	private String street;
	private String city;
	private String message;
	private Date created;	
	private boolean finished = false;
	private String solverName;
	private int status;
	private Date delivered;
	
	public ClaimDto() {		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Integer getSolverId() {
		return solverId;
	}

	public void setSolverId(Integer solverId) {
		this.solverId = solverId;
	}

	public int getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(int deliveryId) {
		this.deliveryId = deliveryId;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(Character finished) {
		if (finished == null || finished == 'T' || finished == 't')
			this.finished = true;
		else
			this.finished = false;
	}

	public String getSolverName() {
		return solverName;
	}

	public void setSolverName(String solverName) {
		this.solverName = solverName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getDelivered() {
		return delivered;
	}

	public void setDelivered(Date delivered) {
		this.delivered = delivered;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getClaimOrderNo() {
		return claimOrderNo;
	}

	public void setClaimOrderNo(String claimOrderNo) {
		this.claimOrderNo = claimOrderNo;
	}

	
	
}