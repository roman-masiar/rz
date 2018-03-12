package cz.leveland.robzone.database.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cz.leveland.robzone.database.entity.dto.ForInvoiceDto;

@Entity
@Table(name = "invoice")
public class Invoice implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;
	public static final int TYPE_SUPPLIER = 1;
	public static final int TYPE_CUSTOMER = 2;

	public static final int SUBTYPE_INVOICE = 1;
	public static final int SUBTYPE_CREDITNOTE = 2;
	
	public static final int INVOICING_PRICE_ORDER = 1;
	public static final int INVOICING_PRICE_PRODUCT = 2;
	
	public static final int INVOICING_SOURCE_ORDER = 1;
	public static final int INVOICING_SOURCE_ALLOCATION = 2;
	public static final int INVOICING_SOURCE_DELIVERY = 3;
	

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "invoiceno")
	private String invoiceNo;
	
	@Column(name = "companyid")
	private int companyId;
	
	@Column(name = "type")
	private int type;
	
	@Column(name = "subtype")
	private int subType;
	

	@Column(name = "orderid")
	private int orderId;

	/* reference to credit note origin */
	@Column(name = "invoiceid")
	private Integer invoiceId;
	
	@Column(name = "totalprice")
	private Double totalPrice;

	@Column(name = "vat")
	private Double vat;

	@Column(name = "rounding")
	private Double rounding = 0.0d;
	
	@Column(name = "partnerid")
	private Integer partnerId;	
	
	@Column(name = "currencyid")
	private int currencyId;
	
	/* dates */
	@Column(name = "created")
	private Date created;
	
	@Column(name = "taxdate")
	private Date taxDate;
	
	@Column(name = "duedate")
	private Date dueDate;

	public Invoice() {
		super();
	}

	public Invoice(int companyId, int type, int subType, String invoiceNo, int partnerId, int orderId, Date createDate, Date taxDate,
			int currencyId) {
		this.type = type;
		this.subType = subType;
		this.invoiceNo = invoiceNo;
		this.companyId = companyId;
		this.partnerId = partnerId;
		this.created = createDate;
		this.taxDate = taxDate;
		this.dueDate = taxDate;
		this.currencyId = currencyId;
		this.orderId = orderId;
		this.rounding = 0.0;
		this.vat = 0.0;
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}
	
	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Double getVat() {
		return vat;
	}

	public void setVat(Double vat) {
		this.vat = vat;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public int getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}

	public int getSubType() {
		return subType;
	}

	public void setSubType(int subType) {
		this.subType = subType;
	}

	public Double getRounding() {
		return rounding;
	}

	public void setRounding(Double rounding) {
		this.rounding = rounding;
	}

	public Date getTaxDate() {
		return taxDate;
	}

	public void setTaxDate(Date taxDate) {
		this.taxDate = taxDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public Integer getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public void calculatePriceAndVat(List<ForInvoiceDto> oneOrderItems) {
		double price = 0.0, vat = 0.0;
		for (ForInvoiceDto item:oneOrderItems) {
			price += item.getTotalPrice();
			vat += item.getTotalPrice() * item.getOrderVatRate() / 100;
		}
		this.totalPrice = price;
		this.vat = vat;
	}

	

}