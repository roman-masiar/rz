package cz.leveland.robzone.database.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import cz.leveland.appbase.database.entity.AbstractCompositeKey;

@Embeddable
public class PkUnit extends AbstractCompositeKey implements Serializable {
 
   
	private static final long serialVersionUID = 1L;

	@Column(name = "id")
	private int id;
	
	@Column(name = "countryid")
    private int countryId;
 
    public PkUnit() {
    }
 
    public PkUnit(int id, int countryId) {
        this.id = id;
        this.countryId = countryId;
    }
 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PkUnit)) return false;
        PkUnit that = (PkUnit) o;
        return Objects.equals(getCountryId(), that.getCountryId()) &&
                Objects.equals(getId(), that.getId());
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(getId(),getCountryId());
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

    
}