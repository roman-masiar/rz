package cz.leveland.robzone.database.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.json.JSONException;
import org.json.JSONObject;

import cz.leveland.appbase.database.exceptions.WrongInputException;
import cz.leveland.appbase.util.InputSanitizer;

@Entity
@Table(name = "payment")
public class Payment implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "orderid")
	private int orderId;
	
	@Column(name = "refno")
	private String refNo;	
	
	@Column(name = "payerid")
	private Integer payerId;	
	
	@Column(name = "amount")
	private Double amount;

	@Column(name = "created")
	private Date created;	

	public Payment() {
		super();
	}

	public Payment(String refNo, Integer payerId, Double amount) throws WrongInputException {
		
		if (refNo == null || !InputSanitizer.isPlainText(refNo))
			throw new WrongInputException("Wrong input data");

		
		this.refNo = refNo;
		this.amount = amount;
		this.payerId = payerId;
		this.created = new Date();
	}

	public Payment(JSONObject one) throws JSONException, WrongInputException {
		
		this(one.getString("refNo"), one.getInt("payerId"), one.getDouble("amount"));
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

	public Integer getPayerId() {
		return payerId;
	}

	public void setPayerId(Integer payerId) {
		this.payerId = payerId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}


	

}