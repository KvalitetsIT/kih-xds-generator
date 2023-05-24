package io.oth.xdsgenerator.model.phmr;

public class PHMRCustodian {

	public String name;
	
	public PHMRTelecom[] telecoms;
	
	public PHMRAddress address;
	
	public String id;
	
	public String idCode;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public PHMRTelecom[] getTelecoms() {
		return telecoms;
	}

	public void setTelecoms(PHMRTelecom[] telecoms) {
		this.telecoms = telecoms;
	}
	
	public PHMRAddress getAddress() {
		return address;
	}

	public void setAddress(PHMRAddress address) {
		this.address = address;
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
