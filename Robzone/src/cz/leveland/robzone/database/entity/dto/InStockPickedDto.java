package cz.leveland.robzone.database.entity.dto;

import cz.leveland.robzone.exception.OverAllocationException;

public class InStockPickedDto implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;
	private Integer id;
	private int warehouseId;	
	private Integer stockItemId;
	private Number qty;
	private Number pickedQty;
	private boolean hasSerialNo;
	private int allocated;

	public InStockPickedDto() {
		super();
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

	public Number getQty() {
		return qty;
	}

	public void setQty(Number qty) {
		this.qty = qty;
	}

	public Number getPickedQty() {
		return pickedQty;
	}

	public void setPickedQty(Number pickedQty) {
		if (pickedQty == null)
			pickedQty = 0;

		this.pickedQty = pickedQty;
	}

	public boolean isHasSerialNo() {
		return hasSerialNo;
	}

	public void setHasSerialNo(char hasSerialNo) {
		this.hasSerialNo = hasSerialNo == 'T' || hasSerialNo == 't';
	}

	public int getAllocated() {
		return allocated;
	}

	public void setAllocated(int allocated) {
		this.allocated = allocated;
	}

	public void allocate(int allocQty) throws OverAllocationException {
		
		if (this.pickedQty.intValue() + allocQty > qty.intValue())
			throw new OverAllocationException();
		this.allocated += allocQty;
		
	}
	
	public int getAvailable() {
		return this.qty.intValue() - this.pickedQty.intValue() - allocated;
	}
	
	

	
}