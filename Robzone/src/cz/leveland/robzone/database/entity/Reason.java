package cz.leveland.robzone.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import cz.leveland.appbase.database.entity.PK;

@Entity
@Table(name = "reason")
@IdClass(PK.class)
public class Reason implements java.io.Serializable {
 
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "companyid")
	private int companyId;
	
	@Id	
	@Column(name = "id")
	private Integer id;
		
	@Column(name = "name")
	private String name;


	public Reason() {
		
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


	public int getCompanyId() {
		return companyId;
	}


	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}



	

	
}