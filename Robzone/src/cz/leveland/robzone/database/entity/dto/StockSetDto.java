package cz.leveland.robzone.database.entity.dto;

import java.io.Serializable;
import java.util.Date;

import cz.leveland.robzone.stock.Allocateable;

public class StockSetDto implements Serializable, Allocateable {

	
	private static final long serialVersionUID = 1L;

	private Integer id;
	private int productId;
	private int stockItemId;
	private Date created;
	private String name;
	private Number cnt;
	private int allocated;
	
	
	
	public StockSetDto() {
		super();
	}

	public StockSetDto(int id, int productId, int stockItemId, Date created, String name, Number cnt) {
		super();
		this.id = id;
		this.productId = productId;
		this.stockItemId = stockItemId;
		this.created = created;
		this.name = name;
		this.cnt = cnt;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getStockItemId() {
		return stockItemId;
	}

	public void setStockItemId(int stockItemId) {
		this.stockItemId = stockItemId;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Number getCnt() {
		return cnt;
	}

	public void setCnt(Number cnt) {
		this.cnt = cnt;
	}

	public int getAllocated() {
		return allocated;
	}

	@Override
	public void setAllocated(int allocated) {
		this.allocated = allocated;
	}
	
	@Override
	public boolean isAllocated() {
		return allocated > 0;
	}

	@Override
	public int getWhatId() {
		
		return productId;
	}
	
	
}
