package cz.leveland.robzone.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "vatrate")
@IdClass(PkVatRate.class)
public class VatRate implements java.io.Serializable {
 
	private static final long serialVersionUID = 1L;

	@Id	
	@Column(name = "companyid")
	private int companyId;
	@Id
	@Column(name = "vategoryid")
	private int vatCategoryId;
	@Id
	@Column(name = "countryid")
	private int countryId;
	
	@Column(name = "rate")
	private double rate;
	
	public VatRate() {
		super();
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public int getVatCategoryId() {
		return vatCategoryId;
	}

	public void setVatCategoryId(int vatCategoryId) {
		this.vatCategoryId = vatCategoryId;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}
	
	
		
}