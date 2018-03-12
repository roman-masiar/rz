package cz.leveland.robzone.database.entity.dto;
public class ProductSetItemDto implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer productId;
	private Integer productSetId;
	private Integer stockItemId;
	private int qty;
	private boolean invoiced = true;
	private String name;
	private String code;
	private boolean hasSerialNo;

	


	public ProductSetItemDto() {
		super();
	}




	public Integer getId() {
		return id;
	}




	public void setId(Integer id) {
		this.id = id;
	}




	public Integer getProductId() {
		return productId;
	}




	public void setProductId(Integer productId) {
		this.productId = productId;
	}




	public Integer getProductSetId() {
		return productSetId;
	}




	public void setProductSetId(Integer productSetId) {
		this.productSetId = productSetId;
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




	public boolean isInvoiced() {
		return invoiced;
	}




	public void setInvoiced(boolean invoiced) {
		this.invoiced = invoiced;
	}




	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	public String getCode() {
		return code;
	}




	public void setCode(String code) {
		this.code = code;
	}




	public boolean isHasSerialNo() {
		return hasSerialNo;
	}




	public void setHasSerialNo(char hasSerialNo) {
		this.hasSerialNo = hasSerialNo == 'T' || hasSerialNo == 't';
	}




	public void setHasSerialNo(boolean hasSerialNo) {
		this.hasSerialNo = hasSerialNo;
	}
	
	

	
	
}