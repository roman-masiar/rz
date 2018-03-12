package cz.leveland.robzone.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "discountitem")
public class DiscountItem implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;
	

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "companyid")
	private Integer companyId;
	
	@Column(name = "discountclassid")
	private Integer discountClassId;
	
	@Column(name = "qty")
	private int qty;
	
	@Column(name = "discount")
	private double discount;

	public DiscountItem() {
		super();
	}

	public DiscountItem(Integer companyId, int qty, double discount) {
		super();
		this.companyId = companyId;
		this.qty = qty;
		this.discount = discount;
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

	public Integer getDiscountClassId() {
		return discountClassId;
	}

	public void setDiscountClassId(Integer discountClassId) {
		this.discountClassId = discountClassId;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}
	
	

		
}