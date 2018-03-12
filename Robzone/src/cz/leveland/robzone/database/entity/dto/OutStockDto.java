package cz.leveland.robzone.database.entity.dto;

import java.util.Date;

public class OutStockDto implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;
	private int id;
	private int warehouseId;
	private Integer stockItemId;
	private Date created;
	private Integer orderId;
	private Integer orderWarehouseId;
	private Integer orderItemId;
	private int qty;
	private Integer userId;
	private Integer pickerId;
	private Byte reasonId;
	private Integer inStockId;
	private int productId;
	private boolean setProduct;
	private int transportId;
	private int providerId;
	private String transportName;
	private String stockItemName;
	private String orderNo;
	private String serialNo;
	private String info;
	private String customerName;
	private int items;
	private boolean packed;
	private boolean hasSerialNo=true;
	private Number packageCount;


	public OutStockDto() {
		super();
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getWarehouseId() {
		return warehouseId;
	}


	public void setWarehouseId(int warehouseId) {
		this.warehouseId = warehouseId;
	}


	public Integer getStockItemId() {
		return stockItemId;
	}


	public void setStockItemId(Integer stockItemId) {
		this.stockItemId = stockItemId;
	}


	public Date getCreated() {
		return created;
	}


	public void setCreated(Date created) {
		this.created = created;
	}


	public Integer getOrderItemId() {
		return orderItemId;
	}


	public void setOrderItemId(Integer orderItemId) {
		this.orderItemId = orderItemId;
	}


	public int getQty() {
		return qty;
	}


	public void setQty(int qty) {
		this.qty = qty;
	}


	public Integer getUserId() {
		return userId;
	}


	public void setUserId(Integer userId) {
		this.userId = userId;
	}


	public Byte getReasonId() {
		return reasonId;
	}


	public void setReasonId(Byte reasonId) {
		this.reasonId = reasonId;
	}


	public Integer getInStockId() {
		return inStockId;
	}


	public void setInStockId(Integer inStockId) {
		this.inStockId = inStockId;
	}


	public String getStockItemName() {
		return stockItemName;
	}


	public void setStockItemName(String stockItemName) {
		this.stockItemName = stockItemName;
	}


	public String getSerialNo() {
		return serialNo;
	}


	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}


	public String getInfo() {
		return info;
	}


	public void setInfo(String info) {
		this.info = info;
	}


	public String getOrderNo() {
		return orderNo;
	}


	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}


	public Integer getPickerId() {
		return pickerId;
	}


	public void setPickerId(Integer pickerId) {
		this.pickerId = pickerId;
	}


	public String getCustomerName() {
		return customerName;
	}


	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}


	public int getItems() {
		return items;
	}


	public void setItems(int items) {
		this.items = items;
	}


	public int getProductId() {
		return productId;
	}


	public void setProductId(int productId) {
		this.productId = productId;
	}


	public boolean isPacked() {
		return packed;
	}


	public void setPacked(char packed) {
		this.packed = packed == 'T' || packed == 't' ;
	}


	public boolean isHasSerialNo() {
		return hasSerialNo;
	}


	public void setHasSerialNo(Character hasSerialNo) {
		if (hasSerialNo == null) {
			this.hasSerialNo = false;
			return;
		}
			
		this.hasSerialNo = hasSerialNo=='T' || hasSerialNo=='t';
	}


	public int getTransportId() {
		return transportId;
	}


	public void setTransportId(int tansportId) {
		this.transportId = tansportId;
	}


	public String getTransportName() {
		return transportName;
	}


	public void setTransportName(String transportName) {
		this.transportName = transportName;
	}


	public int getPackageCount() {
		if (packageCount == null)
				return 0;
		return packageCount.intValue();
	}


	public void setPackageCount(Number packageCount) {
		this.packageCount = packageCount;
	}


	public int getProviderId() {
		return providerId;
	}


	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}


	public boolean isSetProduct() {
		return setProduct;
	}


	public void setSetProduct(char setProduct) {
		this.setProduct = setProduct == 'T' || setProduct == 't';
	}


	public Integer getOrderId() {
		return orderId;
	}


	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}


	public Integer getOrderWarehouseId() {
		return orderWarehouseId;
	}


	public void setOrderWarehouseId(Integer orderWarehouseId) {
		this.orderWarehouseId = orderWarehouseId;
	}
}