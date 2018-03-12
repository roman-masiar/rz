package cz.leveland.robzone.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import cz.leveland.appbase.database.entity.PK;

@Entity
@Table(name = "producttype")
@IdClass(PK.class)
public class ProductType implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;
	public static final int TYPE_GOODS = 1;
	public static final int TYPE_TRANSPORT_PICKUP = 2;
	public static final int TYPE_TRANSPORT_DELIVERY = 3;
	public static final int TYPE_PAYMENT_COD = 4;
	public static final int TYPE_PAYMENT_CREDIT = 5;
	public static final int TYPE_PAYMENT_WIRE = 6;
	public static final int TYPE_PAYMENT_ONLINE = 7;
	public static final int TYPE_PAYMENT_PARTS = 10;
	public static final int TYPE_PAYMENT_LABOR = 11;
	

	@Id
	@Column(name = "companyid")
	private Integer companyId;
	@Id
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "productnatureid")
	private int productNatureId;
	
	@Column(name = "name")
	private String name;

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

	
		
}