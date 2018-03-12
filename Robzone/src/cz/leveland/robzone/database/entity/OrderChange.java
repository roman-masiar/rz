package cz.leveland.robzone.database.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "orderchange")
public class OrderChange implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "orderid")
	private int orderId;
	
	@Column(name = "orderitemid")
	private Integer orderItemId;
	
	@Column(name = "userid")
	private int userId;
	
	@Column(name = "field")
	private String field;
	
	@Column(name = "origvalue")
	private String origValue;
	
	@Column(name = "newvalue")
	private String newValue;
	
	@Column(name = "created")
	private Date created;
	
	public OrderChange() {
		super();
	}
	
	

	public OrderChange(int orderId, Integer orderItemId, int userId, String field, String origValue, String newValue) {
		super();
		this.orderId = orderId;
		this.orderItemId = orderItemId;
		this.userId = userId;
		this.field = field;
		this.origValue = origValue;
		this.newValue = newValue;
		this.created = new Date();
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Integer orderItemId) {
		this.orderItemId = orderItemId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getOrigValue() {
		return origValue;
	}

	public void setOrigValue(String origValue) {
		this.origValue = origValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}



	public Date getCreated() {
		return created;
	}



	public void setCreated(Date created) {
		this.created = created;
	}
	
	

}