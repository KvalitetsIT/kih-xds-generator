package io.oth.xdsgenerator.model.phmr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonalHealthMonitoringReportRequest {
    private static final Logger log = LoggerFactory.getLogger(PersonalHealthMonitoringReportRequest.class);

	public Identity patient;

	public Identity author;

	public Identity authenticator;

	public Identity custodian;

	public Organisation authorOrganisation;

	public Organisation custodianOrganisation;

	public Organisation authenticatorOrganisation;

	public ClinicalMeasurement[] results;

	public ClinicalMeasurement[] vitalSigns;

	public MedicalEquipment[] medicalEquipment;

	public Identity getPatient() {
        log.debug("Returning patient " + patient);

		return patient;
	}

	public void setPatient(Identity patient) {
		this.patient = patient;
	}

	public ClinicalMeasurement[] getResults() {
		return results;
	}

	public void setResults(ClinicalMeasurement[] results) {
		this.results = results;
	}

	public ClinicalMeasurement[] getVitalSigns() {
		return vitalSigns;
	}

	public void setVitalSigns(ClinicalMeasurement[] vitalSigns) {
		this.vitalSigns = vitalSigns;
	}

	public MedicalEquipment[] getMedicalEquipment() {
		return medicalEquipment;
	}

	public void setMedicalEquipment(MedicalEquipment[] medicalEquipment) {
		this.medicalEquipment = medicalEquipment;
	}

	public Organisation getAuthorOrganisation() {
		return authorOrganisation;
	}

	public void setAuthorOrganisation(Organisation authorOrganisation) {
		this.authorOrganisation = authorOrganisation;
	}

	public Organisation getCustodianOrganisation() {
		return custodianOrganisation;
	}

	public void setCustodianOrganisation(Organisation custodianOrganisation) {
		this.custodianOrganisation = custodianOrganisation;
	}

	public Organisation getAuthenticatorOrganisation() {
		return authenticatorOrganisation;
	}

	public void setAuthenticatorOrganisation(Organisation authenticatorOrganisation) {
		this.authenticatorOrganisation = authenticatorOrganisation;
	}

	public Identity getAuthor() {
		return author;
	}

	public void setAuthor(Identity author) {
		this.author = author;
	}

	public Identity getAuthenticator() {
		return authenticator;
	}

	public void setAuthenticator(Identity authenticator) {
		this.authenticator = authenticator;
	}

	public Identity getCustodian() {
		return custodian;
	}

	public void setCustodian(Identity custodian) {
		this.custodian = custodian;
	}

	public String toString() {
		return "Measurements: " + this.getResults().length + " Vital signs: " + this.getVitalSigns().length;
	}

}
