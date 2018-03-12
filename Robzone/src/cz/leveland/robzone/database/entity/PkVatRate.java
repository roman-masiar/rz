package cz.leveland.robzone.database.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import cz.leveland.appbase.database.entity.AbstractCompositeKey;

@Embeddable
public class PkVatRate extends AbstractCompositeKey implements Serializable {
 
   
	private static final long serialVersionUID = 1L;

	@Column(name = "companyid")
    private int companyId;
 
    @Column(name = "vatcategoryid")
    private int vatCategoryId;
 
    @Column(name = "countryid")
    private int countryId;
    
    public PkVatRate() {
    }
 
    public PkVatRate(int companyId, int vatCategoryId, int countryId) {
        this.companyId = companyId;
        this.vatCategoryId = vatCategoryId;
        this.countryId = countryId;        
    }
 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PkVatRate)) return false;
        PkVatRate that = (PkVatRate) o;
        return Objects.equals(getCompanyId(), that.getCompanyId()) &&
                Objects.equals(getVatCategoryId(), that.getVatCategoryId()) &&
                Objects.equals(getCountryId(), that.getCountryId());
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(getCompanyId(), getVatCategoryId(), getCountryId());
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

	
}