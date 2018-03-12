package cz.leveland.robzone.database.entity.dto;

import java.util.Date;


public class DeliveryInfo implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;
	private int id;
	private int companyId;
	private String packNumber;
	private String note;
	private String name;
	private Date delivered;
	private Date returned;
	private Integer status;
	private int providerId;
	
	public static final int STATUS_DELIVERED = 1;
	public static final int STATUS_RETURNED = 2;
	
	public DeliveryInfo() {
		super();
	}



	public DeliveryInfo(int providerId, String packNumber, Date deliveryDate, Integer status, String note, String name) {
		this.providerId = providerId;
		this.packNumber = packNumber;
		this.delivered = deliveryDate;
		this.status = status;
		this.note = note;
		this.name = name;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public int getCompanyId() {
		return companyId;
	}



	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}



	public String getPackNumber() {
		return packNumber;
	}



	public void setPackNumber(String packNumber) {
		this.packNumber = packNumber;
	}



	public String getNote() {
		return note;
	}



	public void setNote(String note) {
		this.note = note;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public Date getDelivered() {
		return delivered;
	}



	public void setDelivered(Date delivered) {
		this.delivered = delivered;
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



	public int getProviderId() {
		return providerId;
	}



	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}


}