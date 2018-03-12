package cz.leveland.robzone.database.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "claim")
public class Claim implements java.io.Serializable {

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "claimno")
	private String claimNo;

	@Column(name = "companyid")
	private Integer companyId;
	
	@Column(name = "userid")
	private Integer userId;
	
	/*
	@Column(name = "type")
	private int type;
	*/
		
	@Column(name = "solverid")
	private Integer solverId;
	
	@Column(name = "deliveryid")
	private int deliveryId;
	
	@Column(name = "customer")
	private String customer;
	
	@Column(name = "street")
	private String street;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "message")
	private String message;
	
	@Column(name = "created")
	private Date created;	
	
	@Type(type="true_false")
	@Column(name = "finished")
	private boolean finished = false;

	@Column(name = "transporttypeid")
	private Integer transportTypeId;

	@Column(name = "paymenttypeid")
	private Integer paymentTypeId;
	


	
	public Claim() {		
		created = new Date();
	}

	//public Claim(int companyId, int userId, int type, int deliveryId, int qty) {
	public Claim(int companyId, int userId, int deliveryId, String message) {
		
		this.companyId = companyId;
		this.userId = userId;
		this.message = message;
		this.deliveryId = deliveryId;		
		this.created = new Date();
				
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

	/*
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	*/

	public Integer getSolverId() {
		return solverId;
	}

	public void setSolverId(Integer partnerId) {
		this.solverId = partnerId;
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

	public int getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(int deliveryId) {
		this.deliveryId = deliveryId;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
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


	
}