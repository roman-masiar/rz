package cz.leveland.robzone.database.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.json.JSONException;
import org.json.JSONObject;

import cz.leveland.appbase.database.exceptions.WrongInputException;
import cz.leveland.appbase.util.InputSanitizer;

@Entity
@Table(name = "credit")
public class Credit extends AbstractPojo implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "creditno")
	private String creditNo;	
	
	@Column(name = "refno")
	private String refNo;	
	
	@Column(name = "orderid")
	private Integer orderId;	
	
	@Column(name = "partnerid")
	private int partnerId;	
	
	@Column(name = "created")
	private Date created;	

	@Column(name = "amount")
	private Double amount;

	@Type(type="true_false")
	@Column(name = "approved")
	private Boolean approved;

	public Credit() {
		super();
	}

	public Credit(int partnerId, String creditNo, String refNo, Double amount) throws WrongInputException {
		
		if (refNo == null || !InputSanitizer.isPlainText(refNo) || amount != null && amount < 0.0d)
			throw new WrongInputException();
		
		this.partnerId = partnerId;
		this.creditNo = creditNo;
		this.refNo = refNo;
		this.amount = amount;
	}

	public Credit(JSONObject one) throws JSONException, WrongInputException {
		this(one.getInt("partnerId"), one.getString("creditNo"), one.getString("refNo"), one.getDouble("amount"));
	}

	public void fillUpdateData(JSONObject one) {
		
		approved = one.getBoolean("approved");
		amount = one.getDouble("amount");
		creditNo = one.getString("creditNo");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreditNo() {
		return creditNo;
	}

	public void setCreditNo(String creditNo) {
		this.creditNo = creditNo;
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

	public int getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(int partnerId) {
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

	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
	}
	
	

	
}