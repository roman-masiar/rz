package cz.leveland.robzone.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

import cz.leveland.appbase.database.exceptions.WrongInputException;
import cz.leveland.appbase.util.InputSanitizer;

@Entity
@Table(name = "product")
public class Product extends AbstractPojo implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "companyid")
	private int companyId;
	
	@Column(name = "countryid")
	private int countryId;
	
	@Column(name = "vatcategoryid")
	private int vatCategoryId;
	
	@Column(name = "producttypeid")
	private int productTypeId;
		
	@Column(name = "discountclassid")
	private Integer discountClassId;
	
	@Column(name = "productcategoryid")
	private int productCategoryId;
	
	@Type(type="true_false")
	@Column(name = "setproduct")
	private boolean setProduct;
	
	@Type(type="true_false")
	@Column(name = "keyproduct")
	private boolean keyProduct;
	
	
	/* commercial attributes */
	
	@Column(name = "code")
	private String code;

	@Column(name = "name")
	private String name;
	
	@Column(name = "price")
	private double price;	

	@Column(name = "image")
	private String image;
	
	/* link to stock item - can be null for set product */
	@Column(name = "stockitemid")
	private Integer stockItemId;
	
	@Column(name = "providerid")
	private Integer providerId;
	
	@Type(type="true_false")
	@Column(name = "active")
	private boolean active = true;

	public Product() {
		super();
	}

	public Product(int companyId, Integer countryId, Integer vatCategoryId, Integer productTypeId, Integer productCategoryId, 
			boolean setProduct, String code, String name, double price, Integer stockItemId) throws WrongInputException {

		notNull(countryId, companyId, code, name, vatCategoryId,  productTypeId,  productCategoryId);
		if (setProduct && code == null || !setProduct && stockItemId == null || price< 0.0d 
				|| !InputSanitizer.isPlainText(name) || !InputSanitizer.isPlainText(code))
			throw new WrongInputException();
			

		this.companyId = companyId;
		this.countryId = countryId;
		this.vatCategoryId = vatCategoryId;
		this.productTypeId = productTypeId;
		this.productCategoryId = productCategoryId;
		this.setProduct = setProduct;		
		this.code = code;
		this.name = name;
		this.price = price;
		this.stockItemId = stockItemId;
		this.active = true;
	}

	public Integer getDiscountClassId() {
		return discountClassId;
	}

	public void setDiscountClassId(Integer discountClassId) {
		this.discountClassId = discountClassId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public void setPrice(Number price) {
		if (price == null)
			this.price = 0.0;
		else
			this.price = price.doubleValue();
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

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isSetProduct() {
		return setProduct;
	}

	public void setSetProduct(boolean setProduct) {
		this.setProduct = setProduct;
	}

	public void validate() throws WrongInputException {
		
		notNull(code, name, vatCategoryId,  productTypeId,  productCategoryId);
		if (setProduct && code == null || !setProduct && stockItemId == null || price< 0.0d 
				|| !InputSanitizer.isPlainText(name) || !InputSanitizer.isPlainText(code))
			throw new WrongInputException();
	}

	public int getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(int productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Integer getProviderId() {
		return providerId;
	}

	public void setProviderId(Integer providerId) {
		this.providerId = providerId;
	}

	public boolean isKeyProduct() {
		return keyProduct;
	}

	public void setKeyProduct(boolean keyProduct) {
		this.keyProduct = keyProduct;
	}
	
	
	
}
