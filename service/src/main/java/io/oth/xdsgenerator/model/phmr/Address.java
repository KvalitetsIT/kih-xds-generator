package io.oth.xdsgenerator.model.phmr;

public class Address {

	public String[] addressLines;
	public String city;
	public String postalCode;
	public String country;
	public String use;

	public String[] getAddressLines() {
		return addressLines;
	}
	public void setAddressLines(String[] addressLines) {
		this.addressLines = addressLines;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getUse() {
		return use;
	}
	public void setUse(String use) {
		this.use = use;
	}

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (String a : getAddressLines()) {
            if (null != a ) {

            builder.append(a + ",");
            }
        }


        return "Address " + builder.toString()+" " + getPostalCode() +" "+ getCity()+" U: " + getUse();
    }

}
