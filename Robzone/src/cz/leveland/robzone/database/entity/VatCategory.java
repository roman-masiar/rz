package cz.leveland.robzone.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import cz.leveland.appbase.database.entity.PK;

@Entity
@Table(name = "vatcategory")
@IdClass(PK.class)
public class VatCategory implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;
	public static final int TYPE_BASIC = 1;
	public static final int TYPE_LOWER = 2;
	public static final int TYPE_LOWER2 = 3;
	
	

	@Id
	@Column(name = "companyid")
	private int companyId;
	@Id
	@Column(name = "id")
	private int id;
	
	@Column(name = "name")
	private String name;
	

	public VatCategory() {
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