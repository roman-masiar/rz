package cz.leveland.robzone.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "stocksetitem")
public class StockSetItem implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;		

	@Column(name = "stocksetid")
	private int stockSetId;
	
	@Column(name = "instockid")
	private Integer inStockId;		

	@Column(name = "stockitemid")
	private Integer stockItemId;		
	
	@Column(name = "qty")
	private int qty;
	
	
	public StockSetItem() {
		super();
	}

	

	public StockSetItem(int stockSetId, Integer inStockId, Integer stockItemId, int qty) {
		super();
		this.stockSetId = stockSetId;
		this.inStockId = inStockId;
		this.stockItemId = stockItemId;
		this.qty = qty;
	}



	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public int getStockSetId() {
		return stockSetId;
	}


	public void setStockSetId(int stockSetId) {
		this.stockSetId = stockSetId;
	}


	public Integer getInStockId() {
		return inStockId;
	}


	public void setInStockId(Integer inStockId) {
		this.inStockId = inStockId;
	}


	public Integer getStockItemId() {
		return stockItemId;
	}


	public void setStockItemId(Integer stockItemId) {
		this.stockItemId = stockItemId;
	}


	public int getQty() {
		return qty;
	}


	public void setQty(int qty) {
		this.qty = qty;
	}
	
	
}