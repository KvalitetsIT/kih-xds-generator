package io.oth.xdsgenerator.model.kih;

public class AssignedEntity {

	public PersonNameStructure AssignedPerson;

	public AddressPostal Address;

	public EmailAddress[] EmailAddress;

	public Identifier[] Id;

	public PhoneNumberSubscriber[] PhoneNumberSubscriber;

	public PersonNameStructure getAssignedPerson() {
		return AssignedPerson;
	}

	public void setAssignedPerson(PersonNameStructure assignedPerson) {
		AssignedPerson = assignedPerson;
	}

	public AddressPostal getAddress() {
		return Address;
	}

	public void setAddress(AddressPostal address) {
		Address = address;
	}

	public EmailAddress[] getEmailAddress() {
		return EmailAddress;
	}

	public void setEmailAddress(EmailAddress[] emailAddress) {
		EmailAddress = emailAddress;
	}

	public Identifier[] getId() {
		return Id;
	}

	public void setId(Identifier[] id) {
		Id = id;
	}

	public PhoneNumberSubscriber[] getPhoneNumberSubscriber() {
		return PhoneNumberSubscriber;
	}

	public void setPhoneNumberSubscriber(PhoneNumberSubscriber[] phoneNumberSubscriber) {
		PhoneNumberSubscriber = phoneNumberSubscriber;
	}
}
