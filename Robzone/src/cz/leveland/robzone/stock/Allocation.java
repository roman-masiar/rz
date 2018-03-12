package cz.leveland.robzone.stock;

public class Allocation {
	public int sourceId;
	public int qty;
	public Allocation(int sourceId, int qty) {
		super();
		this.sourceId = sourceId;		
		this.qty = qty;
	}
	public int getSourceId() {
		return sourceId;
	}
	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}
	
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	
	
}