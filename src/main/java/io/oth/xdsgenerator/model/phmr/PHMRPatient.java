package io.oth.xdsgenerator.model.phmr;

public class PHMRPatient {

	public String cpr;
	
	public String[] givenNames;
	
	public String familyName;
	
	public PHMRTelecom[] telecoms;

	public PHMRAddress address;	
	
	public PHMRTelecom[] getTelecoms() {
		return telecoms;
	}

	public void setTelecoms(PHMRTelecom[] telecoms) {
		this.telecoms = telecoms;
	}

	public String[] getGivenNames() {
		return givenNames;
	}

	public void setGivenNames(String[] givenNames) {
		this.givenNames = givenNames;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}	
	
	public String getCpr() {
		return cpr;
	}
	
	public void setCpr(String cpr) {		
		this.cpr = cpr;
	}

	public PHMRAddress getAddress() {
		return address;
	}

	public void setAddress(PHMRAddress address) {
		this.address = address;
	}
	

}
