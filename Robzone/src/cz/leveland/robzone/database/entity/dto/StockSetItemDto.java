package cz.leveland.robzone.database.entity.dto;

import java.io.Serializable;

public class StockSetItemDto implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private int inStockId;			
	private int stockSetId;
	private int qty;
	
	public StockSetItemDto(int inStockId, int stockSetId, int qty) {
		super();
		this.inStockId = inStockId;
		this.stockSetId = stockSetId;
		this.qty = qty;
	}
	public int getInStockId() {
		return inStockId;
	}
	public void setInStockId(int inStockId) {
		this.inStockId = inStockId;
	}
	public int getStockSetId() {
		return stockSetId;
	}
	public void setStockSetId(int stockSetId) {
		this.stockSetId = stockSetId;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	
	
}
