package cz.leveland.robzone.database.entity;

import java.util.LinkedHashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cz.leveland.robzone.database.entity.dto.Bundle;

@Entity
@Table(name = "claimitem")
public class ClaimItem implements java.io.Serializable {
 	private static final long serialVersionUID = 1L;
 	
 	public static final int REASON_RETURN_14 = 1;
 	public static final int REASON_RETURN_7 = 2;
 	public static final int REASON_WARRANTY = 3;
 	public static final int REASON_PAID = 4;

 	public static final int ACTION_RETURN = 1;
 	public static final int ACTION_REPAIR = 2;
 	public static final int ACTION_EXCHANGE = 3;
 	
 		@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "claimid")
	private Integer claimId;
	
	@Column(name = "qty")
	private int qty;
		
	@Column(name = "reason")
	private int reason;

	@Column(name = "action")
	private int action;
	
	@Column(name = "instockId")
	private int instockId;
	
	public ClaimItem() {
		super();
	}

	public ClaimItem(Integer claimId, int reason, int action, int instockId, int qty) {
		
		this.claimId = claimId;
		this.reason = reason;
		this.action = action;
		this.instockId = instockId;
		this.qty = qty;
	}

	public ClaimItem(int claimId, LinkedHashMap<String,Object> one) {
		
		this(claimId, (int)one.get("reason"), (int)one.get("action"), (int)one.get("instockId"), (int)one.get("qty"));
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getClaimId() {
		return claimId;
	}

	public void setClaimId(Integer claimId) {
		this.claimId = claimId;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public int getInstockId() {
		return instockId;
	}

	public void setInstockId(int instockId) {
		this.instockId = instockId;
	}

	
	
	public boolean isWarrantyReason() {
		return this.reason == REASON_WARRANTY;
	}

	public boolean isExchangeAction() {
		return this.action == ACTION_EXCHANGE;
	}
	
	public boolean needsOrder() {
		return this.action == ACTION_REPAIR || this.action == ACTION_EXCHANGE;		
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

	public static int getActionRepair() {
		return ACTION_REPAIR;
	}
	
	
	
}