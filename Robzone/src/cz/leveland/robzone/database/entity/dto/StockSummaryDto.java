package cz.leveland.robzone.database.entity.dto;

import java.io.Serializable;

public class StockSummaryDto implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	
	int id;
	String name;
	Number inQty=0;
	Number outQty=0;
	Number sum=0;
	
	public StockSummaryDto() {
		super();
	}
	
	
	
	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSum() {
		return sum.intValue();
	}
	public void setSum(Number sum) {
		this.sum = sum;
	}
	public int getInQty() {
		return inQty.intValue();
	}
	public void setInQty(Number inQty) {
		this.inQty = inQty;
	}
	public int getOutQty() {
		return outQty.intValue();
	}
	public void setOutQty(Number outQty) {
		this.outQty = outQty;
	}
	
	

}
