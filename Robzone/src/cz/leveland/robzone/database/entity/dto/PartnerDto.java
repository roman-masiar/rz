package cz.leveland.robzone.database.entity.dto;


public class PartnerDto implements java.io.Serializable {
 

	private static final long serialVersionUID = 1L;
	
	public static final byte TYPE_CUSTOMER = 1;
	public static final byte TYPE_SUPPLIER = 2;
	public static final byte TYPE_CREDIT_PROVIDER = 3;


	private Integer id;
	private byte type;
	private String name;
	private String familyName;
	private String regNo;
	private String taxNo;
	private int countryId;
	private String city;
	private String street;
	private String phone;
	private String email;
	private String countryName;
	
	

	public PartnerDto() {
		
	}



	public PartnerDto(String name, String familyName, String email, String phone, int countryId, String city, String street, String regNo) {
		
		this.name = name;
		this.familyName = familyName;
		this.email = email;
		this.phone = phone;
		this.countryId = countryId;
		this.city = city;
		this.street = street;
		this.regNo = regNo;
				
	}



	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
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



	public int getCountryId() {
		return countryId;
	}



	public void setCountryId(int countryId) {
		this.countryId = countryId;
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



	public byte getType() {
		return type;
	}



	public void setType(byte type) {
		this.type = type;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
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



	public boolean validate() {
		
		return 
				name != null && name.length() > 1 &&
				city != null && city.length() > 1 &&
				phone != null && phone.length() > 1 &&
				email != null && email.length() > 1;
	}



	public String getCountryName() {
		return countryName;
	}



	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	

	
}