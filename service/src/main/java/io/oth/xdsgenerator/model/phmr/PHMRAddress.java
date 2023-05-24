package io.oth.xdsgenerator.model.phmr;

public class PHMRAddress {
	
	public String street[];
	
	public String postCode;
	
	public String city;
	
	public String country;

	public String[] getStreet() {
		return street;
	}

	public void setStreet(String[] street) {
		this.street = street;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}	
	
	
}
