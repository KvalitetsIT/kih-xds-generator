package io.oth.xdsgenerator.model.phmr;

public class PHMRDocument {

	public String documentId;
	
	public String resultsText;
	
	public String createdBy;
	
	public PHMRPatient patient;
	
	public PHMRParticipant author;
	
	public PHMRCustodian custodian;
	
	public PHMRParticipant legalAuthenticator;
	
	public PHMRMeasurement[] results;	

	public PHMRMeasurement[] vitalSigns;

	public PHMRMedicalEquipment[] medicalEquipment;

	public String getResultsText() {
		return resultsText;
	}

	public void setResultsText(String resultsText) {
		this.resultsText = resultsText;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public PHMRPatient getPatient() {
		return patient;
	}

	public void setPatient(PHMRPatient patient) {
		this.patient = patient;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public PHMRMeasurement[] getResults() {
		return results;
	}

	public void setResults(PHMRMeasurement[] results) {
		this.results = results;
	}

	public PHMRMeasurement[] getVitalSigns() {
		return vitalSigns;
	}

	public void setVitalSigns(PHMRMeasurement[] vitalSigns) {
		this.vitalSigns = vitalSigns;
	}	
	
	public PHMRMedicalEquipment[] getMedicalEquipment() {
		return medicalEquipment;
	}

	public void setMedicalEquipment(PHMRMedicalEquipment[] medicalEquipment) {
		this.medicalEquipment = medicalEquipment;
	}
	
	public PHMRParticipant getLegalAuthenticator() {
		return legalAuthenticator;
	}

	public void setLegalAuthenticator(PHMRParticipant legalAuthenticator) {
		this.legalAuthenticator = legalAuthenticator;
	}

	public PHMRCustodian getCustodian() {
		return custodian;
	}

	public void setCustodian(PHMRCustodian custodian) {
		this.custodian = custodian;
	}

	public PHMRParticipant getAuthor() {
		return author;
	}

	public void setAuthor(PHMRParticipant author) {
		this.author = author;
	}	
	
}
