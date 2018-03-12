package cz.leveland.robzone.database.entity.dto;


public class ClaimItemDto implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;
	
	private int qty;
	private String name;
	private String serialNo;
	private int instockId;
	private int reason;
	private int action;
	
	public ClaimItemDto() {
		super();
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getInstockId() {
		return instockId;
	}
	public void setInstockId(int instockId) {
		this.instockId = instockId;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public int getReason() {
		return reason;
	}
	public void setReason(int reason) {
		this.reason = reason;
	}
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}
		
}