package cz.leveland.robzone.database.entity.dto;


public class InStockProductDto implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;
	private int instockId;	
	private Integer stockItemId;
	private Integer productId;
	private Integer countryId;
	
	public InStockProductDto() {
		super();
	}
	public int getInstockId() {
		return instockId;
	}
	public void setInstockId(int instockId) {
		this.instockId = instockId;
	}
	public Integer getStockItemId() {
		return stockItemId;
	}
	public void setStockItemId(Integer stockItemId) {
		this.stockItemId = stockItemId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getCountryId() {
		return countryId;
	}
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
	
	
	
}