package cz.leveland.robzone.database.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cz.leveland.appbase.database.exceptions.WrongInputException;

@Entity
@Table(name = "partner")
public class Partner extends AbstractPojo implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;
	
	public static final byte TYPE_CUSTOMER = 1;
	public static final byte TYPE_SUPPLIER = 2;
	public static final byte TYPE_CREDIT_PROVIDER = 3;



	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "countryid")
	private int countryId;
	
	@Column(name = "type")
	private int type;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "familyname")
	private String familyName;
	
	@Column(name = "regno")
	private String regNo;
	
	@Column(name = "taxno")
	private String taxNo;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "street")
	private String street;
	
	@Column(name = "phone")
	private String phone;
	
	@Column(name = "email")
	private String email;

	@Column(name = "zip")
	private String zip;
	
	@Column(name = "userid")
	private String userId;
	
	public Partner() {
		
	}

	public Partner(int countryId, int type, String name, String familyName, String regNo, String taxNo, String city, String street, String zip, String phone, String email) throws WrongInputException {
		
		notNull(name, city, street, phone, email);
		
		this.countryId = countryId;
		this.type = type;
		this.name = name;
		this.familyName = familyName;
		this.regNo = regNo;
		this.taxNo = taxNo;
		this.city = city;
		this.street = street;
		this.phone = phone;
		this.email = email;
		this.zip = zip;
		
		if (countryId <= 0 || type <= 0 || 
				name==null || name.length() == 0 ||
				city==null || city.length() == 0 ||
				email==null || email.length() == 0 ||
				street==null || street.length() == 0
				)
			throw new WrongInputException();
				
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}

	public String getTaxNo() {
		return taxNo;
	}

	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public List<OrderChange> update(int userId, int orderId, String name, String familyName, String regNo, String taxNo, String email, String phone,
			String city, String street, String zip) {
		
			List<OrderChange> changes = new ArrayList<OrderChange>();
			if (dif(this.name,name)) {
				changes.add(new OrderChange(orderId, null, userId, "jméno", this.name, name));
				this.name = name;
			}			
			if (dif(this.familyName,familyName)) {
				changes.add(new OrderChange(orderId, null, userId, "příjmení", this.familyName, familyName));
				this.familyName = familyName;
			}
			if (dif(this.regNo,regNo)) {
				changes.add(new OrderChange(orderId, null, userId, "IČO", this.regNo, regNo));
				this.regNo = regNo;
			}
			if (dif(this.taxNo,taxNo)) {
				changes.add(new OrderChange(orderId, null, userId, "DIČ", this.taxNo, taxNo));
				this.taxNo = taxNo;
			}
			if (dif(this.email,email)) {
				changes.add(new OrderChange(orderId, null, userId, "email", this.email, email));
				this.email = email;
			}
			if (dif(this.phone,phone)) {
				changes.add(new OrderChange(orderId, null, userId, "telefon", this.phone, phone));
				this.phone = phone;
			}
			if (dif(this.street,street)) {
				changes.add(new OrderChange(orderId, null, userId, "adresa", this.street, street));
				this.street = street;
			}
			if (dif(this.city,city)) {
				changes.add(new OrderChange(orderId, null, userId, "město", this.city, city));
				this.city = city;
			}
			if (dif(this.zip,zip)) {
				changes.add(new OrderChange(orderId, null, userId, "PSČ", this.zip, zip));
				this.zip = zip;
			}

			return changes;
		
		
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	
}