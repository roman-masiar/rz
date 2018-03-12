package cz.leveland.robzone.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import cz.leveland.appbase.database.entity.PK;

@Entity
@Table(name = "productcategory")
@IdClass(PK.class)
public class ProductCategory implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;
	public static final int TYPE_PRODUCT = 1;
	public static final int TYPE_ACCESSORIES = 2;
	
	
	

	@Id
	@Column(name = "companyid")
	private int companyId;
	@Id
	@Column(name = "id")
	private int id;
	
	@Column(name = "name")
	private String name;
	

	public ProductCategory() {
		super();
	}


	public int getCompanyId() {
		return companyId;
	}


	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


		
}