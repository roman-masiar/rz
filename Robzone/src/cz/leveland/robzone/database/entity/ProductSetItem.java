package cz.leveland.robzone.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "productsetitem")
public class ProductSetItem implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "productid")
	private int productId;
		
	@Column(name = "stockitemid")
	private int stockItemId;
	
	@Column(name = "qty")
	private int qty;
	
	
	public ProductSetItem() {
		super();
	}

	public ProductSetItem(Integer productId, Integer stockItemId, Integer qty) {
		this.productId = productId;
		this.stockItemId = stockItemId;
		this.qty = qty;
	}

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

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}
	
	

}