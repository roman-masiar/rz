package cz.leveland.robzone.database.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "outstock")
public class OutStock implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;
	

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "warehouseid")
	private int warehouseId;

	@Column(name = "orderitemid")
	private Integer orderItemId;
	
	@Column(name = "instockid")
	private Integer inStockId;
	
	@Column(name = "stocksetid")
	private Integer stockSetId;
	
	@Column(name = "deliveryitemid")
	private Integer deliveryItemId;
	
	@Column(name = "pickerid")
	private Integer pickerId;
	
	@Column(name = "userid")
	private Integer userId;
	
	@Column(name = "reasonid")
	private Integer reasonId;	
	
	@Column(name = "qty")
	private int qty;

	@Column(name = "created")
	private Date created;

	public OutStock() {
		super();
	}

	public OutStock(int warehouseId, Integer orderItemId, Integer inStockId, Integer stockSetId, Integer deliveryItemId,
			Integer pickerId, Integer userId, Integer reasonId, int qty) {
		super();
		this.warehouseId = warehouseId;
		this.orderItemId = orderItemId;
		this.inStockId = inStockId;
		this.stockSetId = stockSetId;
		this.deliveryItemId = deliveryItemId;
		this.pickerId = pickerId;
		this.userId = userId;
		this.reasonId = reasonId;
		this.qty = qty;
		this.created = new Date();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(int warehouseId) {
		this.warehouseId = warehouseId;
	}

	public Integer getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Integer orderItemId) {
		this.orderItemId = orderItemId;
	}

	public Integer getInStockId() {
		return inStockId;
	}

	public void setInStockId(Integer inStockId) {
		this.inStockId = inStockId;
	}

	public Integer getStockSetId() {
		return stockSetId;
	}

	public void setStockSetId(Integer stockSetId) {
		this.stockSetId = stockSetId;
	}

	public Integer getDeliveryItemId() {
		return deliveryItemId;
	}

	public void setDeliveryItemId(Integer deliveryItemId) {
		this.deliveryItemId = deliveryItemId;
	}

	public Integer getPickerId() {
		return pickerId;
	}

	public void setPickerId(Integer pickerId) {
		this.pickerId = pickerId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	
}