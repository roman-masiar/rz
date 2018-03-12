package cz.leveland.robzone.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cz.leveland.appbase.database.exceptions.WrongInputException;
import cz.leveland.robzone.database.entity.dto.ForInvoiceDto;

@Entity
@Table(name = "invoiceitem")
public class InvoiceItem extends AbstractPojo implements java.io.Serializable {
 
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "invoiceid")
	private int invoiceId;		
	
	@Column(name = "companyid")
	private int companyId;		
	
	@Column(name = "vatrate")
	private double vatRate;		
	
	/* out */
	@Column(name = "orderitemid")
	private Integer orderItemId;
	
	@Column(name = "outstockid")
	private Integer outStockId;
	
	@Column(name = "deliveryitemid")
	private Integer deliveryItemId;
		
	@Column(name = "description")
	private String description;
	
	/* quantity */
	@Column(name = "qty")
	private Integer qty;
	
	/* money */
	@Column(name = "unitprice")
	private double unitPrice;
	
	@Column(name = "price")
	private double price;
	
	@Column(name = "discount")
	private double discount;
	
	@Column(name = "totalprice")
	private double totalPrice;
	
	@Column(name = "vat")
	private double vat;
	
	public InvoiceItem() {
		super();
	}

	public InvoiceItem(ForInvoiceDto item,  int invoicingSource, int invoicingPrice) throws WrongInputException {
	
		if (item.getProductId() == null) {
			if (invoicingSource == Invoice.INVOICING_SOURCE_DELIVERY || item.getDescription() == null)
				throw new WrongInputException();
			description = str(item.getDescription());
		} else if (item.getDescription() != null)
			description = item.getName() + " ( " + str(item.getDescription()) + " )";
		else
			description = item.getName();
		
		qty = item.getOrdQty();
		
		switch(invoicingPrice) {
		case Invoice.INVOICING_PRICE_ORDER:
			/* price is taken from order item */
			unitPrice = item.getUnitPrice();
			vatRate = item.getOrderVatRate();			
			break;
		case Invoice.INVOICING_PRICE_PRODUCT:
			/* price is taken from product */
			unitPrice = item.getProductUnitPrice(); 
			vatRate = item.getVatRate();
			break;
		default:
			throw new WrongInputException();
		}
		
		discount = item.getDiscount();
		price = unitPrice - discount;
		totalPrice = price * qty;
		vat = totalPrice * vatRate / 100;
		deliveryItemId = item.getDeliveryItemId();
		//if (deliveryItemId == null)
		orderItemId = item.getOrderItemId();
	}
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public Integer getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Integer orderItemId) {
		this.orderItemId = orderItemId;
	}

	public Integer getOutStockId() {
		return outStockId;
	}

	public void setOutStockId(Integer outStockId) {
		this.outStockId = outStockId;
	}

	public Integer getDeliveryItemId() {
		return deliveryItemId;
	}

	public void setDeliveryItemId(Integer deliveryItemId) {
		this.deliveryItemId = deliveryItemId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public double getVat() {
		return vat;
	}

	public void setVat(double vat) {
		this.vat = vat;
	}

	public double getVatRate() {
		return vatRate;
	}

	public void setVatRate(double vatRate) {
		this.vatRate = vatRate;
	}



	
}