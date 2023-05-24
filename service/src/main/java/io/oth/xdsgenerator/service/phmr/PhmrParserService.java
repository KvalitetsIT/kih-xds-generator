package io.oth.xdsgenerator.service.phmr;


import dk.s4.hl7.cda.convert.PHMRXmlCodec;

import dk.s4.hl7.cda.model.*;
import dk.s4.hl7.cda.model.DataInputContext.PerformerType;

import dk.s4.hl7.cda.model.Telecom;
import dk.s4.hl7.cda.model.phmr.Measurement;

import io.oth.xdsgenerator.model.phmr.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//@Service
public class PhmrParserService {

	public static final Logger LOGGER = LoggerFactory.getLogger(PhmrParserService.class);
	
	public PHMRDocument parse(String s) throws ParseException {
		PHMRXmlCodec codec = new PHMRXmlCodec();
		dk.s4.hl7.cda.model.phmr.PHMRDocument doc = null;
		try {
			doc = codec.decode(s);
		} catch (Exception e) {
			LOGGER.error("Parse error", e);
			throw new ParseException(e.getMessage());
		}
		
		PHMRDocument document = new PHMRDocument();
		
		document.setAuthor(getPHMRParticipant(doc.getAuthor()));
		document.setCustodian(getPHMRCustodian(doc.getCustodianIdentity()));
		document.setLegalAuthenticator(getPHMRParticipant(doc.getLegalAuthenticator()));
		
		document.setPatient(getPHMRPatient(doc.getPatient()));

		document.setResults(getMeasurements(doc.getResults()));
		document.setVitalSigns(getMeasurements(doc.getVitalSigns()));
		document.setMedicalEquipment(getMedicalEquipment(doc.getMedicalEquipments()));
		
		document.setResultsText(doc.getResultsText());
		document.setDocumentId(doc.getId().getExtension());
		document.setCreatedBy((doc.getCustodianIdentity() != null  && doc.getCustodianIdentity().getId() != null) ? doc.getCustodianIdentity().toString() : (doc.getAuthor() != null && doc.getAuthor().getId() != null ? doc.getAuthor().toString() : null));
		
		return document;
	}

	private PHMRCustodian getPHMRCustodian(OrganizationIdentity o) {
		if (o != null) {
			PHMRCustodian c = new PHMRCustodian();
			
			c.setName(o.getOrgName());
			c.setTelecoms(getPHMRTelecom(o.getTelecomList()));
			c.setAddress(getPHMRAddress(o.getAddress()));

			if (o.getId() != null) {
				ID id = o.getId();
				c.setId(id.getExtension());
				c.setIdCode(id.getExtension());
			}			
			
			return c;
		} else {
			return null;
		}
	}
	
	private PHMRParticipant getPHMRParticipant(Participant p) {
		if (p != null) {
			PHMRParticipant la = new PHMRParticipant();
			
			la.setTime(p.getTime());
			
			if (p.getOrganizationIdentity() != null) {
				OrganizationIdentity oi = p.getOrganizationIdentity();
				la.setOrgName(oi.getOrgName());
				la.setAddress(getPHMRAddress(oi.getAddress()));				
				if (p.getOrganizationIdentity().getId() != null) {
					ID id = p.getOrganizationIdentity().getId();
					la.setId(id.getExtension());
					la.setIdCode(id.getExtension());
				}				
			}
			
			if (p.getPersonIdentity() != null) {
				PersonIdentity pi = p.getPersonIdentity();
				la.setFamilyName(pi.getFamilyName());
				la.setGivenNames(pi.getGivenNames());
			}		
			
			la.setTelecoms(getPHMRTelecom(p.getTelecomList()));			

			return la;
		} else {
			return null;
		}
	}
	
	
	private PHMRPatient getPHMRPatient(Patient p) {
		PHMRPatient patient = new PHMRPatient();
		
		patient.setCpr(p.getIdValue());
		patient.setGivenNames(p.getGivenNames());
		patient.setFamilyName(p.getFamilyName());
		
		patient.setTelecoms(getPHMRTelecom(p.getTelecomList()));
		patient.setAddress(getPHMRAddress(p.getAddress()));			
		
		return patient;
	}
	
	private PHMRTelecom[] getPHMRTelecom(Telecom[] telecom) {
		if (telecom != null) {
			List<PHMRTelecom> tList = new ArrayList<>();
		
			for (Telecom t : telecom) {
				PHMRTelecom phmrt = new PHMRTelecom();
				phmrt.setProtocol(t.getProtocol());
				phmrt.setValue(t.getValue());
				tList.add(phmrt);
			}
		
			return tList.toArray(new PHMRTelecom[0]);		
		} else {
			return null;
		}
	}
	
	private PHMRAddress getPHMRAddress(AddressData ad) {
		if (ad != null) {
			PHMRAddress address = new PHMRAddress();
			address.setStreet(ad.getStreet());
			address.setPostCode(ad.getPostalCode());
			address.setCity(ad.getCity());
			address.setCountry(ad.getCountry());
			return address;
		} else {
			return null;
		}
	}
	
	private PHMRMeasurement[] getMeasurements(List<Measurement> measurements) {
		List<PHMRMeasurement> mList = new ArrayList<>();
		
		for (Measurement m : measurements) {
			PHMRMeasurement phmrm = new PHMRMeasurement();
			if (m.hasComment()) {
				phmrm.setComment(m.getComment().getText());
			}
			phmrm.setTimeStamp(m.getTimestamp());
			phmrm.setValue(m.getValue());
			phmrm.setUnit(m.getUnit());
						
			if (m.getTranslatedCode() != null && m.getTranslatedCodeSystem() != null && m.getTranslatedCodeSystemName() != null) {
				phmrm.setCode(m.getTranslatedCode());
				phmrm.setCodeSystem(m.getTranslatedCodeSystem());
				phmrm.setCodeSystemName(m.getTranslatedCodeSystemName());
				phmrm.setDisplayName(m.getTranslatedDisplayName());
			} else {
				phmrm.setCode(m.getCode());
				phmrm.setCodeSystem(m.getCodeSystem());
				phmrm.setCodeSystemName(m.getCodeSystemName());
				phmrm.setDisplayName(m.getDisplayName());
			}
			
			if (m.getDataInputContext() != null) {
				DataInputContext context = m.getDataInputContext();
				if (context.getMeasurementPerformerCode() != null) {
					PerformerType pType = context.getMeasurementPerformerCode();
					if (pType != null) {
						phmrm.setContextPerformerType(pType.name());
					}
				}				
			}
			phmrm.setId(m.getId().getExtension());
			
			mList.add(phmrm);
		}
		
		return mList.toArray(new PHMRMeasurement[0]);
	}
	
	private PHMRMedicalEquipment[] getMedicalEquipment(List<dk.s4.hl7.cda.model.phmr.MedicalEquipment> equipments) {
		List<PHMRMedicalEquipment> mList = new ArrayList<>();
			
		for (dk.s4.hl7.cda.model.phmr.MedicalEquipment e : equipments) {
			PHMRMedicalEquipment phmrme = new PHMRMedicalEquipment();
			
			phmrme.setManufacturerModelName(e.getManufacturerModelName());
			phmrme.setMedicalDeviceCode(e.getMedicalDeviceCode());
			phmrme.setMedicalDeviceDisplayName(e.getMedicalDeviceDisplayName());
			phmrme.setSerialNumber(e.getSerialNumber());
			phmrme.setSoftwareName(e.getSoftwareName());
			mList.add(phmrme);
		}
			
		return mList.toArray(new PHMRMedicalEquipment[0]);
	}
}
