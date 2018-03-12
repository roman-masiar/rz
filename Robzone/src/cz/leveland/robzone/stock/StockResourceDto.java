package cz.leveland.robzone.stock;

import java.io.Serializable;

import cz.leveland.robzone.exception.OverAllocationException;

public class StockResourceDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	int type;
	int id;
	int stockItemId;
	Number qty = 0;
	Number pickedQty = 0;
	int allocated = 0;
	
	public StockResourceDto() {
		super();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStockItemId() {
		return stockItemId;
	}

	public void setStockItemId(int stockItemId) {
		this.stockItemId = stockItemId;
	}

	public int getQty() {
		return qty.intValue();
	}

	public void setQty(Number qty) {
		this.qty = qty == null ? 0 : qty;
	}

	public int getPickedQty() {
		return pickedQty.intValue();
	}

	public void setPickedQty(Number pickedQty) {
		
		this.pickedQty = pickedQty == null ? 0 : pickedQty;
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
		if (this.qty == null || this.pickedQty == null)
			return 0;
		return this.qty.intValue() - this.pickedQty.intValue() - allocated;
	}

	
	
	
}
