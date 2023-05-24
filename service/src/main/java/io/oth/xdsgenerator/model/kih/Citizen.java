package io.oth.xdsgenerator.model.kih;

public class Citizen {

    public String PersonCivilRegistrationIdentifier;

    public AddressPostal AddressPostal;

	public EmailAddress EmailAddress;

	public PhoneNumberSubscriber PhoneNumberSubscriber;

	public PersonNameStructure PersonNameStructure;

	public String getPersonCivilRegistrationIdentifier() {
		return PersonCivilRegistrationIdentifier;
	}

	public void setPersonCivilRegistrationIdentifier(String personCivilRegistrationIdentifier) {
		PersonCivilRegistrationIdentifier = personCivilRegistrationIdentifier;
	}

	public AddressPostal getAddressPostal() {
		return AddressPostal;
	}

	public void setAddressPostal(AddressPostal addressPostal) {
		AddressPostal = addressPostal;
	}

	public EmailAddress getEmailAddress() {
		return EmailAddress;
	}

	public void setEmailAddress(EmailAddress emailAddress) {
		EmailAddress = emailAddress;
	}

	public PhoneNumberSubscriber getPhoneNumberSubscriber() {
		return PhoneNumberSubscriber;
	}

	public void setPhoneNumberSubscriber(PhoneNumberSubscriber phoneNumberSubscriber) {
		this.PhoneNumberSubscriber = phoneNumberSubscriber;
	}

	public PersonNameStructure getPersonNameStructure() {
		return PersonNameStructure;
	}

	public void setPersonNameStructure(PersonNameStructure personNameStructure) {
		PersonNameStructure = personNameStructure;
	}

	@Override
	public String toString() {
		return "Citizen [PersonCivilRegistrationIdentifier=" + PersonCivilRegistrationIdentifier + ", AddressPostal="
				+ AddressPostal + ", EmailAddress=" + EmailAddress + ", phoneNumberSubscriber=" + PhoneNumberSubscriber
				+ ", PersonNameStructure=" + PersonNameStructure + "]";
	}


}
