package io.oth.xdsgenerator.model.kih;

public class Instrument {
	
	public String Manufacturer;
	
    public String MedComID;
    
    public String Model;
    
    public String ProductType;
    
    public String SoftwareVersion;

	public String getManufacturer() {
		return Manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		Manufacturer = manufacturer;
	}

	public String getMedComID() {
		return MedComID;
	}

	public void setMedComID(String medComID) {
		MedComID = medComID;
	}

	public String getModel() {
		return Model;
	}

	public void setModel(String model) {
		Model = model;
	}

	public String getProductType() {
		return ProductType;
	}

	public void setProductType(String productType) {
		ProductType = productType;
	}

	public String getSoftwareVersion() {
		return SoftwareVersion;
	}

	public void setSoftwareVersion(String softwareVersion) {
		SoftwareVersion = softwareVersion;
	}
    
}
