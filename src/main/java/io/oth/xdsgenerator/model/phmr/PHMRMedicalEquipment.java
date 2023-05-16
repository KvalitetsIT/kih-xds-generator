package io.oth.xdsgenerator.model.phmr;

public class PHMRMedicalEquipment {
	
	public String manufacturerModelName;
	
	public String medicalDeviceCode;
	
	public String medicalDeviceDisplayName;
	
	public String serialNumber;
	
	public String softwareName;

	public String getManufacturerModelName() {
		return manufacturerModelName;
	}

	public void setManufacturerModelName(String manufacturerModelName) {
		this.manufacturerModelName = manufacturerModelName;
	}

	public String getMedicalDeviceCode() {
		return medicalDeviceCode;
	}

	public void setMedicalDeviceCode(String medicalDeviceCode) {
		this.medicalDeviceCode = medicalDeviceCode;
	}

	public String getMedicalDeviceDisplayName() {
		return medicalDeviceDisplayName;
	}

	public void setMedicalDeviceDisplayName(String medicalDeviceDisplayName) {
		this.medicalDeviceDisplayName = medicalDeviceDisplayName;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getSoftwareName() {
		return softwareName;
	}

	public void setSoftwareName(String softwareName) {
		this.softwareName = softwareName;
	}
	
}
