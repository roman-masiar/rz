package cz.leveland.robzone.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "stockitem")
public class StockItem implements java.io.Serializable {
 
	public static final int TYPE_WITH_SERIAL_NO = 1;
	public static final int TYPE_WITHOUT_SERIAL_NO = 2;

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "companyid")
	private int companyId;
	
	@Column(name = "productcategoryid")
	private Integer productCategoryId;
	
	@Column(name = "supplierid")
	private int supplierId;
	
	@Column(name="hasserialno")
	@Type(type="true_false")
	private boolean hasSerialNo;
	
	@Column(name = "code")
	private String code;
	
	@Column(name = "image")
	private String image;

	@Column(name = "name")
	private String name;
	
	@Column(name = "volume")
	private Double volume;
	
	@Column(name = "weight")
	private Double weight;
	
	@Column(name = "packingtype")
	private Integer packingType;
	
	public StockItem() {
		super();
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

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public boolean isHasSerialNo() {
		return hasSerialNo;
	}

	public void setHasSerialNo(boolean hasSerialNo) {
		this.hasSerialNo = hasSerialNo;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Integer getPackingType() {
		return packingType;
	}

	public void setPackingType(Integer packingType) {
		this.packingType = packingType;
	}
	
	
}