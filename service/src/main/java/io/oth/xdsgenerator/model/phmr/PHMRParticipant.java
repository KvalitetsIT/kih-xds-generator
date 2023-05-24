package io.oth.xdsgenerator.model.phmr;

import java.util.Date;

public class PHMRParticipant {
	
	public Date time;
	
	public PHMRAddress address;
	
	public String orgName;
	
	public String[] givenNames;
	
	public PHMRTelecom[] telecoms;

	public String familyName;
	
	public String id;
	
	public String idCode;

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public PHMRAddress getAddress() {
		return address;
	}

	public void setAddress(PHMRAddress address) {
		this.address = address;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
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
	
	public PHMRTelecom[] getTelecoms() {
		return telecoms;
	}

	public void setTelecoms(PHMRTelecom[] telecoms) {
		this.telecoms = telecoms;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdCode() {
		return idCode;
	}

	public void setIdCode(String idCode) {
		this.idCode = idCode;
	}
	
}
