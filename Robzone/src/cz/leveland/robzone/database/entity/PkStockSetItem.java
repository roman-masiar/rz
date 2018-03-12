package cz.leveland.robzone.database.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import cz.leveland.appbase.database.entity.AbstractCompositeKey;

@Embeddable
public class PkStockSetItem extends AbstractCompositeKey implements Serializable {
 
	private static final long serialVersionUID = 1L;

	@Column(name = "instockid")
	private int inStockId;
	
	@Column(name = "stocksetid")
    private int stockSetId;
 
    public PkStockSetItem() {
    }
 
    public PkStockSetItem(int inStockId, int stockSetId) {
        this.inStockId = inStockId;
        this.stockSetId = stockSetId;
    }
    
 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PkStockSetItem)) return false;
        PkStockSetItem that = (PkStockSetItem) o;
        return Objects.equals(getStockSetId(), that.getStockSetId()) &&
                Objects.equals(getInStockId(), that.getInStockId());
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(getInStockId(), getStockSetId());
    }

	public int getStockSetId() {
		return stockSetId;
	}

	public void setStockSetId(int stockSetId) {
		this.stockSetId = stockSetId;
	}

	public int getInStockId() {
		return inStockId;
	}

	public void setInStockId(int inStockId) {
		this.inStockId = inStockId;
	}
    
    
}