package io.oth.xdsgenerator.model.phmr;

public class Identity {

	public String patientIdentifier;
	public String familyName;
	public String givenNames[];
	public Telecom telecoms[];
	public Address address;
	
	public String getPatientIdentifier() {
		return patientIdentifier;
	}
	
	public void setPatientIdentifier(String patientIdentifier) {
		this.patientIdentifier = patientIdentifier;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String[] getGivenNames() {
		return givenNames;
	}

	public void setGivenNames(String[] givenNames) {
		this.givenNames = givenNames;
	}
	
	public Telecom[] getTelecoms() {
		return telecoms;
	}

	public void setTelecoms(Telecom[] telecoms) {
		this.telecoms = telecoms;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}
