package cz.leveland.robzone.database.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "delivery")
public class Delivery implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;
	public static final int TYPE_IN = 1;
	public static final int TYPE_OUT = 2;
	

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "companyid")
	private int companyId;

	@Column(name = "type")
	private int type;
	
	@Column(name = "orderid")
	private int orderId;
	
	@Column(name = "shipped")
	private Date shipped;	

	@Column(name = "created")
	private Date created;	
	
	@Column(name = "delivered")
	private Date delivered;	
	
	
	public Delivery() {
		super();
	}

	public Delivery(int companyId, int orderId, int type) {
		this.companyId = companyId;
		this.orderId = orderId;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getShipped() {
		return shipped;
	}

	public void setShipped(Date shipped) {
		this.shipped = shipped;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public Date getDelivered() {
		return delivered;
	}

	public void setDelivered(Date delivered) {
		this.delivered = delivered;
	}
	
	

	
}