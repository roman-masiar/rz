package cz.leveland.robzone.database.entity.dto;

public class ProductTypeDto implements java.io.Serializable {
 
	private static final long serialVersionUID = 1L;
	
	private Integer companyId;
	private Integer id;
	private int productNatureId;
	private String name;
	private String code;
	private int productTypeId;
	private String productName;

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getProductNatureId() {
		return productNatureId;
	}

	public void setProductNatureId(int productNatureId) {
		this.productNatureId = productNatureId;
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

	public int getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(int productTypeId) {
		this.productTypeId = productTypeId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	
		
}