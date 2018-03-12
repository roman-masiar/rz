package cz.leveland.robzone.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "country")
public class Country implements java.io.Serializable, IdHolder {
 

	private static final long serialVersionUID = 1L;
	

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "companyid")
	private Integer companyId;
	
	@Column(name = "name")
	private String name;

	@Column(name = "currencyid")
	private int currencyId;
	
	@Column(name = "code")
	private String code;
	
	@Type(type="true_false")
	@Column(name = "defaultone")
	private boolean defaultOne;

	
	
	public Country() {
		super();
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getCompanyId() {
		return companyId;
	}


	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getCurrencyId() {
		return currencyId;
	}


	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public boolean isDefaultOne() {
		return defaultOne;
	}


	public void setDefaultOne(boolean defaultOne) {
		this.defaultOne = defaultOne;
	}

	
	
}