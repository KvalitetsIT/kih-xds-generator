package io.oth.xdsgenerator.model.phmr;

public class MedicalEquipment {
	
	public String medicalDeviceCode;
	
	public String medicalDeviceDisplayName;
	
	public String manufacturerModelName;
	
	public String softwareName;
	
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
	
	public String getManufacturerModelName() {
		return manufacturerModelName;
	}
	
	public void setManufacturerModelName(String manufacturerModelName) {
		this.manufacturerModelName = manufacturerModelName;
	}
	
	public String getSoftwareName() {
		return softwareName;
	}
	
	public void setSoftwareName(String softwareName) {
		this.softwareName = softwareName;
	}

}
