package cz.leveland.robzone.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import cz.leveland.appbase.database.entity.PK;

@Entity
@Table(name = "productnature")
@IdClass(PK.class)
public class ProductNature implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;
	public static final int TYPE_GOODS = 1;
	public static final int TYPE_TRANSPORT = 2;
	public static final int TYPE_PAYMENT = 3;
	

	@Id
	@Column(name = "companyid")
	private Integer companyId;
	@Id
	@Column(name = "id")
	private Integer id;
	
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
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
		
}