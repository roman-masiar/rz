package cz.leveland.robzone.database.entity.dto;

import java.util.Date;

import cz.leveland.robzone.stock.Allocateable;


public class InStockDto implements java.io.Serializable, Allocateable {
 

	private static final long serialVersionUID = 1L;
	private Integer id;
	private int warehouseId;	
	private Integer stockItemId;
	private Date created;
	private int qty;
	private Double price;
	private int userId;
	private int pickerId;
	private String serialNo;
	private String info;
	private Integer claimId;
	private Integer deliveryItemId;	
	private String stockItemName;
	
	/* only to help find free instocks */
	private Integer outstockId;
	private int allocated = 0;



	public InStockDto() {
		super();
	}


	@Override
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



	public Date getCreated() {
		return created;
	}



	public void setCreated(Date created) {
		this.created = created;
	}



	public int getQty() {
		return qty;
	}



	public void setQty(int qty) {
		this.qty = qty;
	}



	public int getUserId() {
		return userId;
	}



	public void setUserId(int userId) {
		this.userId = userId;
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



	public Integer getDeliveryItemId() {
		return deliveryItemId;
	}



	public void setDeliveryItemId(Integer deliveryItemId) {
		this.deliveryItemId = deliveryItemId;
	}



	public String getStockItemName() {
		return stockItemName;
	}



	public void setStockItemName(String stockItemName) {
		this.stockItemName = stockItemName;
	}



	public Double getPrice() {
		return price;
	}



	public void setPrice(Double price) {
		this.price = price;
	}



	public Integer getOutstockId() {
		return outstockId;
	}



	public void setOutstockId(Integer outstockId) {
		this.outstockId = outstockId;
	}



	public int getPickerId() {
		return pickerId;
	}



	public void setPickerId(int pickerId) {
		this.pickerId = pickerId;
	}



	public Integer getClaimId() {
		return claimId;
	}



	public void setClaimId(Integer claimId) {
		this.claimId = claimId;
	}



	@Override
	public int getWhatId() {
		
		return stockItemId;
	}



	@Override
	public boolean isAllocated() {
		
		return allocated > 0;
	}



	@Override
	public void setAllocated(int qty) {
		allocated = qty;
	}
	
	
	
}