package cz.leveland.robzone.database.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cz.leveland.robzone.transport.DeliveryRequest;

@Entity
@Table(name = "package")
public class DeliveryPackage implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;
	

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "type")
	private Integer type;
	
	@Column(name = "deliveryid")
	private Integer deliveryId;
	
	@Column(name = "companyid")
	private Integer companyId;
	
	@Column(name = "transporterid")
	private Integer transporterId;
	
	@Column(name = "orderno")
	private String orderNo;
	
	@Column(name = "number")
	private String number;
	
	@Column(name = "status")
	private Integer status;
	
	@Column(name = "delivered")
	private Date delivered;	
	
	@Column(name = "returned")
	private Date returned;	
	
	@Column(name = "note")
	private String note;
	
	
	public DeliveryPackage() {
		super();
	}


	public DeliveryPackage(String orderNo, int transporterId, int type, String packNumber, int companyId) {
		super();
		this.orderNo = orderNo;
		this.transporterId = transporterId;
		this.number = packNumber;
		this.companyId = companyId;
		this.type = type;
	}


	public DeliveryPackage(DeliveryRequest one, int type, String packNumber, int companyId) {
		this(one.getOrder().getOrderNo(), one.getTransporter().getId(), type, packNumber, companyId);
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Integer getDeliveryId() {
		return deliveryId;
	}


	public void setDeliveryId(Integer deliveryId) {
		this.deliveryId = deliveryId;
	}


	public String getOrderNo() {
		return orderNo;
	}


	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}


	public String getNumber() {
		return number;
	}


	public void setNumber(String number) {
		this.number = number;
	}


	public Date getDelivered() {
		return delivered;
	}


	public void setDelivered(Date delivered) {
		this.delivered = delivered;
	}


	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	}


	public Integer getTransporterId() {
		return transporterId;
	}


	public void setTransporterId(Integer transporterId) {
		this.transporterId = transporterId;
	}


	public Integer getCompanyId() {
		return companyId;
	}


	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
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
	
	
	
}