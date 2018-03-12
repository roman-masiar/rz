package cz.leveland.robzone.type;

public class Qty {
	
	private static final int DEC_QTY = 1;
	private static final int[] DIV = {1,10,100,1000,1000,10000,100000,1000000};

	Integer qty = null;
	Integer qtyDec = DEC_QTY;
	
	public Qty(Integer qty) {
		
		this.qty = qty;
		this.qtyDec = 0;
	}

	public Qty(Integer qty, int dec) {
		
		this.qty = qty;
		this.qtyDec = dec;
	}
	
	public Qty(Double val, int qtyDec) {
		this.qtyDec = qtyDec;
		if (val != null)
			qty = (int) Math.round(val * DIV[qtyDec]);
	}

	public Qty(Double val) {
		
		this(val, DEC_QTY);
	}


	public Integer get() {
		return qty;
	}

	public Integer getQty() {
		return qty;
	}
	
	
	public Integer getQtyDec() {
		return qtyDec;
	}
	
	public Double getDouble() {
		if (qty == null)
			return null;
		return (double)qty / (double)qtyDec;
	}
	
	

}
