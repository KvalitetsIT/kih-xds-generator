package io.oth.xdsgenerator.model.kih;

public class RepresentedCustodianOrganization {
	
	public AddressPostal Address;
	
	public String Name;
	
	public PhoneNumberSubscriber PhoneNumberSubscriber;
	
	public Identifier[] Id;

	public AddressPostal getAddress() {
		return Address;
	}

	public void setAddress(AddressPostal address) {
		Address = address;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public PhoneNumberSubscriber getPhoneNumberSubscriber() {
		return PhoneNumberSubscriber;
	}

	public void setPhoneNumberSubscriber(PhoneNumberSubscriber phoneNumberSubscriber) {
		PhoneNumberSubscriber = phoneNumberSubscriber;
	}

	public Identifier[] getId() {
		return Id;
	}

	public void setId(Identifier[] id) {
		Id = id;
	}
}
