package cz.leveland.robzone.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "unit")
@IdClass(PkUnit.class)
public class Unit implements java.io.Serializable {
 
	public static final int PIECE = 1;
	public static final int HOUR = 2;

	private static final long serialVersionUID = 1L;

	@Id	
	@Column(name = "id")
	private Integer id;
	
	@Id
	@Column(name = "countryid")
	private int countryId;
	
	@Column(name = "name")
	private String name;
	
}