package cz.leveland.robzone.database.entity.dto;

public class ProductDto implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;
	 
	private Integer id;
	private int companyId;
	private int countryId;
	private int vatCategoryId;
	private int productTypeId;
	private int productNatureId;
	private Integer providerId;
	private int productCategoryId;
	private String productCategoryName;
	private String vatName;
	private char setProduct;	
	private char keyProduct;	
	private String code;
	private String name;
	private double price;	
	private String image;
	private Integer stockItemId;
	private boolean hasSerialNo;
	
	private boolean active;

	

	public ProductDto() {
		super();
	}



	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public double getPrice() {
		return price;
	}



	public void setPrice(double price) {
		this.price = price;
	}



	public String getImage() {
		return image;
	}



	public void setImage(String image) {
		this.image = image;
	}



	public Integer getStockItemId() {
		return stockItemId;
	}



	public void setStockItemId(Integer stockItemId) {
		this.stockItemId = stockItemId;
	}



	public boolean isActive() {
		return active;
	}



	public void setActive(char active) {
		this.active = active=='T' || active=='t';
	}




	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}




	public int getCompanyId() {
		return companyId;
	}



	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}



	public int getCountryId() {
		return countryId;
	}



	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}



	public int getVatCategoryId() {
		return vatCategoryId;
	}



	public void setVatCategoryId(int vatCategoryId) {
		this.vatCategoryId = vatCategoryId;
	}



	public int getProductTypeId() {
		return productTypeId;
	}



	public void setProductTypeId(int productTypeId) {
		this.productTypeId = productTypeId;
	}


	public int getProductCategoryId() {
		return productCategoryId;
	}



	public void setProductCategoryId(int productCategoryId) {
		this.productCategoryId = productCategoryId;
	}



	public String getProductCategoryName() {
		return productCategoryName;
	}



	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
	}



	public String getVatName() {
		return vatName;
	}



	public void setVatName(String vatName) {
		this.vatName = vatName;
	}

	public boolean isSetProduct() {
		return setProduct == 'T' || setProduct == 't';
	}
	public void setSetProduct(char setProduct) {
		this.setProduct = setProduct;
	}

	public boolean isKeyProduct() {
		return keyProduct == 'T' || keyProduct == 't';
	}
	public void setKeyProduct(char keyProduct) {
		this.keyProduct = keyProduct;
	}
	


	public int getProductNatureId() {
		return productNatureId;
	}



	public void setProductNatureId(int productNatureId) {
		this.productNatureId = productNatureId;
	}



	public boolean hasSerialNo() {
		return hasSerialNo;
	}



	public void setHasSerialNo(Character hasSerialNo) {
		if (hasSerialNo == null) {
			this.hasSerialNo = false;
			return;
		}
			
		this.hasSerialNo = hasSerialNo=='T' || hasSerialNo=='t';
	}



	public Integer getProviderId() {
		return providerId;
	}



	public void setProviderId(Integer providerId) {
		this.providerId = providerId;
	}



	
	
}