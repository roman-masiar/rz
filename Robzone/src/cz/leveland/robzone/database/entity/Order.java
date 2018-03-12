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
import cz.leveland.robzone.database.entity.interfaces.ProcessableOrder;

@Entity
@Table(name = "custorder")
public class Order extends AbstractPojo implements java.io.Serializable, ProcessableOrder {
 

	private static final long serialVersionUID = 1L;


	public static final int STATUS_NEW = 1;
	public static final int STATUS_PROCESSED = 2;
	public static final int STATUS_PICKED = 3;
	public static final int STATUS_SHIPPED = 4;	
	public static final int STATUS_WAITING = 5;
	public static final int STATUS_MISSING = 6;
	public static final int STATUS_DELIVERED = 7;
	public static final int STATUS_RETURNED = 8;
	public static final int STATUS_PROBLEM = 99;
	
	public static final int STATUS_SUSPENDED = 50;
	public static final int STATUS_CANCELLED = 100;






	
	

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "orderno")
	private String orderNo;
	
	@Column(name = "partnerid")
	private int partnerId;
	
	@Column(name = "payerid")
	private Integer payerId;
	
	@Column(name = "created")
	private Date created;
	
	@Column(name = "status")
	private int status = STATUS_NEW;

	@Column(name = "items")
	private int items;
	
	@Column(name = "currencyid")
	private int currencyId;
	
	@Column(name = "warehouseid")
	private int warehouseId;
	
	@Column(name = "price")
	private Double price;
	
	@Column(name = "vat")
	private Double vat;
	
	@Column(name = "totalprice")
	private Double totalPrice;
	
	@Column(name = "rounding")
	private Double rounding;
	
	@Column(name = "pricetopay")
	private Double priceToPay;
	
	@Column(name = "origpaymenttype")
	private Integer origPaymentType;
	
	@Column(name = "claimid")
	private Integer claimId;

	
	
	@Transient
	private Integer paymentTypeId;

	@Transient
	private Integer transportTypeId;
	
	public Order() {
		super();
	}


	
	public Order(Date created, String orderNo, int warehouseId, int currencyId, int partnerId, Integer payerId) throws WrongInputException {
		
		notNull(created, orderNo);
		if (partnerId<0)
			throw new WrongInputException();
		this.created = created;
		this.orderNo = orderNo;
		this.partnerId = partnerId;
		this.currencyId = currencyId;
		this.warehouseId = warehouseId;
		this.payerId = payerId;
		
	}

	public Order(Date created, String orderNo, int warehouseId, int currencyId, int partnerId, Integer payerId, double price, double vat, double totalPrice) throws WrongInputException {
		this(created, orderNo, warehouseId, currencyId, partnerId, payerId);
		this.price = price;
		this.vat = vat;
		this.totalPrice = totalPrice;
		
	}
	
	public Order(int orderId) {
		this.id = orderId;
		this.created = new Date();
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public int getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(int partnerId) {
		this.partnerId = partnerId;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getVat() {
		return vat;
	}

	public void setVat(Double vat) {
		this.vat = vat;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Double getPriceToPay() {
		return priceToPay;
	}

	public void setPriceToPay(Double priceToPay) {
		this.priceToPay = priceToPay;
	}

	public Integer getPaymentTypeId() {
		return paymentTypeId;
	}



	public void setPaymentTypeId(Integer paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}



	public Integer getTransportTypeId() {
		return transportTypeId;
	}



	public void setTransportTypeId(Integer transportTypeId) {
		this.transportTypeId = transportTypeId;
	}



	public int getItems() {
		return items;
	}



	public void setItems(int items) {
		this.items = items;
	}



	public Double getRounding() {
		return rounding;
	}



	public void setRounding(Double rounding) {
		this.rounding = rounding;
	}



	public List<OrderChange> update(int userId, Integer id, String orderNo) {
		
		List<OrderChange> changes = new ArrayList<OrderChange>();
		if (!this.orderNo.equals(orderNo)) {
			changes.add(new OrderChange(id, 0, userId, "číslo objednávky", this.orderNo, orderNo));
			this.orderNo = orderNo;
		}
		return changes;
	}



	public Integer getOrigPaymentType() {
		return origPaymentType;
	}



	public void setOrigPaymentType(Integer origPaymentType) {
		this.origPaymentType = origPaymentType;
	}



	public int getCurrencyId() {
		return currencyId;
	}



	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}



	public int getWarehouseId() {
		return warehouseId;
	}



	public void setWarehouseId(int warehouseId) {
		this.warehouseId = warehouseId;
	}



	public static Order getRepairInstance(int userId, int customerId, Integer payerId, int claimId) {
		Order order = new Order();
		order.setClaimId(claimId);
		order.setPartnerId(customerId);
		order.setCreated(new Date());
		order.setPayerId(payerId);
		return order;
	}



	public Integer getClaimId() {
		return claimId;
	}

	public void setClaimId(Integer claimId) {
		this.claimId = claimId;
	}



	public Integer getPayerId() {
		return payerId;
	}



	public void setPayerId(Integer payerId) {
		this.payerId = payerId;
	}



}