package cz.leveland.robzone.database.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import cz.leveland.appbase.database.exceptions.WrongInputException;

@Entity
@Table(name = "instock")
public class InStock implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;
	

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "warehouseid")
	private int warehouseId;

	/* link to stock item */
	@Column(name = "stockitemid")
	private Integer stockItemId;
	
	@Column(name = "deliveryitemid")
	private Integer deliveryItemId;
	
	@Column(name = "claimitemid")
	private Integer claimItemId;
	
	@Column(name = "pickerid")
	private int pickerId;
	
	@Column(name = "qty")
	private int qty;

	@Column(name = "price")
	private Double price;
	
	@Column(name = "serialno")
	private String serialNo;
	
	@Column(name = "info")
	private String info;
	
	@Column(name = "created")
	private Date created;
	
	public InStock() {
		super();
	}

	public InStock(int warehouseId, int pickerId, int stockItemId, int qty, String serialNo, String info) throws WrongInputException {
		
		if (stockItemId <= 0 || qty <= 0)
			throw new WrongInputException();
		
		this.warehouseId = warehouseId;
		this.pickerId = pickerId;
		this.stockItemId = stockItemId;
		this.qty = qty;		
		this.serialNo = serialNo;
		this.info = info;
		this.created = new Date();
	}

	public InStock(Integer id, Integer stockItemId, int warehouseId) {
		this.id = id;
		this.stockItemId = stockItemId;
		this.warehouseId = warehouseId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(int warehouseId) {
		this.warehouseId = warehouseId;
	}

	public Integer getStockItemId() {
		return stockItemId;
	}

	public void setStockItemId(Integer stockItemId) {
		this.stockItemId = stockItemId;
	}

	public Integer getDeliveryItemId() {
		return deliveryItemId;
	}

	public void setDeliveryItemId(Integer deliveryItemId) {
		this.deliveryItemId = deliveryItemId;
	}

	public int getPickerId() {
		return pickerId;
	}

	public void setPickerId(int pickerId) {
		this.pickerId = pickerId;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Integer getClaimItemId() {
		return claimItemId;
	}

	public void setClaimItemId(Integer claimItemId) {
		this.claimItemId = claimItemId;
	}
	
	
	
	
}