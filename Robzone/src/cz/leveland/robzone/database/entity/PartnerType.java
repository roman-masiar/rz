package cz.leveland.robzone.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "partnertype")
public class PartnerType implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;
	
	public static final int TYPE_CUSTOMER = 1;
	public static final int TYPE_SUPPLIER = 2;
	public static final int TYPE_CREDIT_PROVIDER = 3;



	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name")
	private int name;
	
	

	public PartnerType() {
		super();
	}



	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public int getName() {
		return name;
	}



	public void setName(int name) {
		this.name = name;
	}


	
}