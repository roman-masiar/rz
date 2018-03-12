package cz.leveland.robzone.database.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import cz.leveland.appbase.database.exceptions.WrongInputException;

@Entity
@Table(name = "orderitem")
public class OrderItem implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;



	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "orderid")
	private int orderId;
	
	@Column(name = "productid")
	private Integer productId;
	
	@Column(name = "unitprice")
	private double unitPrice ;
	
	@Column(name = "price")
	private double price ;
	
	@Column(name = "qty")
	private double qty ;
	
	@Column(name = "unit")
	private int unit = Unit.PIECE;
	
	@Column(name = "discount")
	private Double discount;
	
	@Column(name = "discountpercent")
	private Double discountPercent;
	
	@Column(name = "totalprice")
	private Double totalPrice;
	
	@Column(name = "vat")
	private Double vat;
	
	@Column(name = "vatRate")
	private Double vatRate;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "processed")
	private Date processed;
	
	@Column(name = "claimitemid")
	private Integer claimitemId;
	
	@Column(name = "payerid")
	private Integer payerId;
	
	@Transient
	private Integer discountClassId;
	
	public OrderItem() {
		super();
	}

	public OrderItem(int orderId, Integer productId, String description, double qty, Double unitPrice, double discountPercent, double discount) throws WrongInputException {
		
		if (productId == null && (description == null || description.length() == 0))
			throw new WrongInputException();
		this.orderId = orderId;
		this.productId = productId;
		this.qty = qty;
		this.unitPrice = unitPrice;
		this.discountPercent = discountPercent;
		this.discount = discount;
		this.description = description;
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	
	
	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public List<OrderChange> update(int userId, int productId, int qty, double unitPrice, double discountPercent) {
		
		List<OrderChange> changes = new ArrayList<OrderChange>();
		if (this.productId != productId) {
			changes.add(new OrderChange(orderId, id, userId, "produkt", this.productId+"", productId+""));
			this.productId = productId;
		}
		if (this.qty != qty) {
			changes.add(new OrderChange(orderId, id, userId, "množství", this.qty+"", qty+""));
			this.qty = qty;
		}
		if (this.unitPrice != unitPrice) {
			changes.add(new OrderChange(orderId, id, userId, "cena za ks", this.unitPrice+"", unitPrice+""));
			this.unitPrice = unitPrice;
		}
		if (this.discountPercent != discountPercent) {
			changes.add(new OrderChange(orderId, id, userId, "sleva", this.discountPercent+"", discountPercent+""));
			this.discountPercent = discountPercent;
			this.discount = this.unitPrice * discountPercent/100;
		}
		return changes;
	}

	public Date getProcessed() {
		return processed;
	}

	public void setProcessed(Date processed) {
		this.processed = processed;
	}

	public Integer getDiscountClassId() {
		return discountClassId;
	}

	public void setDiscountClassId(Integer discountClassId) {
		this.discountClassId = discountClassId;
	}

	public void calculateDiscount(double discountPercent) {
		
		this.discountPercent = discountPercent;
		this.discount = this.unitPrice * discountPercent/100;
	}

	public Double getDiscountpercent() {
		return discountPercent;
	}

	public void setDiscountpercent(Double discountPercent) {
		this.discountPercent = discountPercent;
	}

	public Double getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(Double discountPercent) {
		this.discountPercent = discountPercent;
	}

	public int getUnit() {
		return unit;
	}

	public void setUnit(int unit) {
		this.unit = unit;
	}

	public Double getVat() {
		return vat;
	}

	public void setVat(Double vat) {
		this.vat = vat;
	}

	public Double getVatRate() {
		return vatRate;
	}

	public void setVatRate(Double vatRate) {
		this.vatRate = vatRate;
	}

	public Integer getClaimitemId() {
		return claimitemId;
	}

	public void setClaimitemId(Integer claimitemId) {
		this.claimitemId = claimitemId;
	}

	public Integer getPayerId() {
		return payerId;
	}

	public void setPayerId(Integer payerId) {
		this.payerId = payerId;
	}

	

}