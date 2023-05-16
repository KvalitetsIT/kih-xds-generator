package io.oth.xdsgenerator.service.phmr;

import dk.s4.hl7.cda.codes.Loinc;
import dk.s4.hl7.cda.codes.MedCom;
import dk.s4.hl7.cda.codes.NPU;
import dk.s4.hl7.cda.model.*;
import dk.s4.hl7.cda.model.AddressData.Use;
import dk.s4.hl7.cda.model.DataInputContext.PerformerType;
import dk.s4.hl7.cda.model.DataInputContext.ProvisionMethod;
import dk.s4.hl7.cda.model.ID.IDBuilder;
import dk.s4.hl7.cda.model.Participant.ParticipantBuilder;
import dk.s4.hl7.cda.model.phmr.Measurement;
import dk.s4.hl7.cda.model.phmr.PHMRDocument;
import io.oth.xdsgenerator.model.Code;
import io.oth.xdsgenerator.model.DocumentMetadata;
import io.oth.xdsgenerator.model.phmr.*;
import io.oth.xdsgenerator.util.MeasurementHtmlTableGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * Creates a PHMR document from KIH Mapping Structure
 */
//@Service
public class PhmrBuilderService {

    public static final Logger log = LoggerFactory.getLogger(PhmrBuilderService.class);

    private String phmrTitlePrefix;
    private String patientIdRoot = "1.2.208.176.1.2";
    private String patientIdDisplayName = "CPR";

    public PhmrBuilderService(String phmrTitlePrefix) {
        this.phmrTitlePrefix = phmrTitlePrefix;
    }

    public PhmrBuilderService() {
    }

    /**
     * Builds a @class PhmrAndMetadata object storing both the actual document and also metadata for it
     * @param request  The request to be converted
     * @return  the PHMR DOcument and metadata
     */
    public PhmrAndMetadata buildPhmrClinicalDocument(PersonalHealthMonitoringReportRequest request) {
        Date documentCreationTime = new Date();
        DocumentMetadata metadata = new DocumentMetadata();

        // Sets coding
        metadata.setClassCode(new Code("001", "Klinisk rapport", "1.2.208.184.100.9"));
        metadata.setFormatCode(new Code("urn:ad:dk:medcom:phmr:full", "DK PHMR schema", "1.2.208.184.100.10"));
        metadata.setHealthcareFacilityTypeCode(new Code("550621000005101", "hjemmesygepleje", "2.16.840.1.113883.6.96"));
        metadata.setPracticeSettingCode(new Code("408443003", "almen medicin", "2.16.840.1.113883.6.96"));
        metadata.setSubmissionTime(new Date());

        Patient patientIdentity = createFromIdentity(request.getPatient());
        PHMRDocument phmr = new PHMRDocument(makeRandomID());
        phmr.setTitle(phmrTitlePrefix + patientIdentity.getIdValue());

        // 1.1 Populate with time and version info
        phmr.setEffectiveTime(documentCreationTime);

        // 1.2 Populate the document with patient information
        phmr.setPatient(patientIdentity);


        log.debug("Setting author");

        ParticipantBuilder authorBuilder = new ParticipantBuilder();
        authorBuilder.setPersonIdentity(createFromIdentity(request.getAuthor()));
        authorBuilder.setOrganizationIdentity(createFromOrganizationAndIdentity(request.getAuthorOrganisation(), request.getAuthor()));
        authorBuilder.setTime(documentCreationTime);
        if (request.getAuthorOrganisation() != null && request.getAuthorOrganisation().getCode() != null) {
            authorBuilder.setSOR(request.getAuthorOrganisation().getCode());
        }
        phmr.setAuthor(authorBuilder.build());

        log.debug("Set author!");
        phmr.setCustodian(createFromOrganizationAndIdentity(request.getCustodianOrganisation(), request.getCustodian()));
        log.debug("Set custodian");

        log.debug("Setting legal authenticator if not null");
        if (request.getAuthenticatorOrganisation() != null) {
            ParticipantBuilder legalAuthenticatorBuilder = new ParticipantBuilder();
            legalAuthenticatorBuilder.setPersonIdentity(createFromIdentity(request.getAuthenticator()));
            legalAuthenticatorBuilder.setSOR(request.getAuthorOrganisation().getCode());
            legalAuthenticatorBuilder.setOrganizationIdentity(createFromOrganizationAndIdentity(request.getAuthenticatorOrganisation(), request.getAuthenticator()));
            legalAuthenticatorBuilder.setTime(documentCreationTime);
            phmr.setLegalAuthenticator(legalAuthenticatorBuilder.build());
            log.debug("Set legal authenticator!");

        }

        phmr.setConfidentialityCode("N");
        phmr.setLanguageCode("da-DK");

        // 1.4 Define the service period
        Date from = null;
        Date to = null;


        // Add Results
        MeasurementHtmlTableGenerator resultsMeasurementHtmlTableGenerator = new MeasurementHtmlTableGenerator();
        if (request.getResults() != null && request.getResults().length > 0) {

            for (ClinicalMeasurement measurement : request.getResults()) {
                Measurement phmrMeasurement = createFromMeasurement(measurement);
                phmr.addResult(phmrMeasurement);

                if (from == null) {
                    from = measurement.getMeasuredAt();
                } else if (from.after(measurement.getMeasuredAt())) {
                    from = measurement.getMeasuredAt();
                }
                if (to == null) {
                    to = measurement.getMeasuredAt();
                } else if (to.before(measurement.getMeasuredAt())) {
                    to = measurement.getMeasuredAt();
                }
                resultsMeasurementHtmlTableGenerator.addLine(phmrMeasurement);
                log.debug("Measurement Result is : " + measurement.toString());
            }

        }

        // Add VitalSigns
        MeasurementHtmlTableGenerator vitalSignsMeasurementHtmlTableGenerator = new MeasurementHtmlTableGenerator();
        if (request.getVitalSigns() != null) {
            for (ClinicalMeasurement measurement : request.getVitalSigns()) {
                Measurement phmrMeasurement = createFromMeasurement(measurement);
                phmr.addVitalSign(phmrMeasurement);

                if (from == null) {
                    from = measurement.getMeasuredAt();
                } else if (from.after(measurement.getMeasuredAt())) {
                    from = measurement.getMeasuredAt();
                }
                if (to == null) {
                    to = measurement.getMeasuredAt();
                } else if (to.before(measurement.getMeasuredAt())) {
                    to = measurement.getMeasuredAt();
                }
                vitalSignsMeasurementHtmlTableGenerator.addLine(phmrMeasurement);
                log.debug("Measurement VitalSign is : " + measurement.toString());
            }
        }
        if (from == null) {
            from = documentCreationTime;
        }
        if (to == null) {
            to = documentCreationTime;
        }

        //set VitalSignsText and ResultsText
        if (resultsMeasurementHtmlTableGenerator.hasText()) {
            phmr.setResultsText(resultsMeasurementHtmlTableGenerator.getText());
        }
        if (vitalSignsMeasurementHtmlTableGenerator.hasText()) {
            phmr.setVitalSignsText(vitalSignsMeasurementHtmlTableGenerator.getText());
        }

        // Add MedicalEquipment
        if (request.getMedicalEquipment() != null) {
            for (MedicalEquipment equipment : request.getMedicalEquipment()) {
                if (equipment.getManufacturerModelName().length() > 0) {
                    phmr.addMedicalEquipment(createFromMedicalEquipment(equipment));
                }
            }
        }

        phmr.setDocumentationTimeInterval(from, to);

        if (phmr.getResults().size() > 0) {
            log.debug("phmr results item 1 created is : " + phmr.getResults().get(0).toString());
        }
        if (phmr.getVitalSigns().size() > 0) {
            log.debug("phmr vitalSign item 1 created is : " + phmr.getVitalSigns().get(0).toString());
        }

        return new PhmrAndMetadata(phmr, metadata);
    }

    private ID makeRandomID() {
        IDBuilder idBuilder = new ID.IDBuilder();
        idBuilder.setRoot(MedCom.ROOT_OID).setExtension(UUID.randomUUID().toString()).setAuthorityName(MedCom.ROOT_AUTHORITYNAME);
        return idBuilder.build();
    }

    private OrganizationIdentity createFromOrganizationAndIdentity(Organisation o, Identity i) {
        if (o == null) {
            return null;
        }

        OrganizationIdentity.OrganizationBuilder builder = new OrganizationIdentity.OrganizationBuilder();

        if (o.getCode() != null && o.getCode().trim().length() > 0) {
            builder.setSOR(o.getCode());
        }

        builder.setName(o.getName());
        builder.setAddress(createFromAddress((i != null ? i.getAddress() : null), Use.WorkPlace));
        if (i != null && i.getTelecoms() != null) {
            for (io.oth.xdsgenerator.model.phmr.Telecom telecom : i.getTelecoms()) {
                builder.addTelecom(Use.valueOf(telecom.getUse()), telecom.getProtocol(), telecom.getTelecomString());
            }
        }
        return builder.build();
    }

    private Measurement createFromMeasurement(ClinicalMeasurement measurement) {
        Measurement.MeasurementBuilder builder = new Measurement.MeasurementBuilder(measurement.getMeasuredAt(), Measurement.Status.COMPLETED);
        String codeSystemOID = NPU.CODESYSTEM_OID;
        String codeSystemDisplayName = NPU.DISPLAYNAME;
        if (measurement.getCodeSystemOID() != null && measurement.getCodeSystemOID().length() > 0) {
            codeSystemOID = measurement.getCodeSystemOID();
            codeSystemDisplayName = measurement.getCodeSystemOID();
            if (codeSystemOID.equals(NPU.CODESYSTEM_OID)) {
                codeSystemDisplayName = NPU.DISPLAYNAME;
            } else if (codeSystemOID.equals(Loinc.OID)) {
                codeSystemDisplayName = Loinc.DISPLAYNAME;
            }
        }
        log.debug("CodeSystemOID is : " + codeSystemOID + " CodeSystemDisplayName is: " + codeSystemDisplayName);
        builder.setPhysicalQuantity(measurement.getResultText(), measurement.getResultUnitText(), measurement.getCode(), measurement.getCodeDescription(), codeSystemOID, codeSystemDisplayName);
        if (measurement.getContextPerformerType() != null && measurement.getContextPerformerType().length() > 0) {
            // Handle the different cases
            DataInputContext creationContext = null;

            //if ("automatic" == measurement.getContextTransferedMethod()) {
            creationContext=  new DataInputContext(ProvisionMethod.valueOf(measurement.contextTransferMethod),
                                PerformerType.valueOf(measurement.getContextPerformerType()));
                //}

            log.debug("Setting up data input context to {}", creationContext);

            builder.setContext(creationContext);
        }

        if (measurement.getId() != null) {
            ID measurementId = new IDBuilder().setRoot(MedCom.ROOT_OID).setExtension(measurement.getId()).setAuthorityName(MedCom.ROOT_AUTHORITYNAME).build();
            builder.setId(measurementId);
        } else {
            builder.setId(makeRandomID());
        }

        return builder.build();
    }

    private dk.s4.hl7.cda.model.phmr.MedicalEquipment createFromMedicalEquipment(MedicalEquipment equipment) {
        return new dk.s4.hl7.cda.model.phmr.MedicalEquipment.MedicalEquipmentBuilder().
                setMedicalDeviceCode(equipment.getMedicalDeviceCode()).
                setMedicalDeviceDisplayName(equipment.getMedicalDeviceDisplayName()).
                setManufacturerModelName(equipment.getManufacturerModelName()).
                setSoftwareName(equipment.getSoftwareName()).build();
    }

    private Patient createFromIdentity(Identity patient) {

        Patient.PatientBuilder builder = new Patient.PatientBuilder((patient != null ? patient.getFamilyName() : "N/A"));

        if (patient != null && patient.getGivenNames() != null) {
            for (String givenName : patient.getGivenNames()) {
                builder.addGivenName(givenName);
            }
        } else {
            builder.addGivenName("N/A");
        }

        if (patient != null && patient.getTelecoms() != null) {
            for (io.oth.xdsgenerator.model.phmr.Telecom telecom : patient.getTelecoms()) {
                builder.addTelecom(Use.valueOf(telecom.getUse()), telecom.getProtocol(), telecom.getTelecomString());
            }
        }

        if (patient != null) {
            ID patientId = new IDBuilder().setRoot(patientIdRoot).setExtension(patient.getPatientIdentifier()).setAuthorityName(patientIdDisplayName).build();
            builder.setPersonID(patientId);
        } else { //TODO: hvornår giver det mening ikke at have et cpr nummer?
            ID patientId = new IDBuilder().setRoot(patientIdRoot).setExtension("N/A").setAuthorityName(patientIdDisplayName).build();
            builder.setPersonID(patientId);
        }

        builder.setAddress(createFromAddress((patient != null ? patient.getAddress() : null), Use.HomeAddress));
        if (patient != null && patient.getPatientIdentifier() != null) {
            int[] birthTime = calculateBirthTimeFromCPR(patient.getPatientIdentifier());
            if (birthTime != null) {
                builder.setBirthTime(birthTime[0], birthTime[1] - 1, birthTime[2]);
            }
        }

        return builder.build();
    }

    /*
     * This is kind of a hack, we are setting a birthtime from CPR. The PHMRParser fails if cpr has nullflavor (not correct)
     */
    private int[] calculateBirthTimeFromCPR(String cpr) {
        try {
            int[] birthTime = null;
            if (cpr.length() >= 10) {
                int day = Integer.parseInt(cpr.substring(0, 2));
                int month = Integer.parseInt(cpr.substring(2, 4));
                String year = cpr.substring(4, 6);
                String charSeven = cpr.substring(6, 7);
                Integer birthYear = calculateYear(year, charSeven);

                if (birthYear != null) {
                    birthTime = new int[3];
                    birthTime[0] = birthYear;
                    birthTime[1] = month;
                    birthTime[2] = day;
                }
            }

            return birthTime;
        } catch (NumberFormatException nfe) {
            // no valid birthTime could be based
            nfe.printStackTrace();
            return null;
        }
    }

    /*
     * Parsed based on https://da.wikipedia.org/wiki/CPR-nummer
     */
    private Integer calculateYear(String yearArg, String charSevenArg) {
        String century = null;
        int year = Integer.parseInt(yearArg);
        int charSeven = Integer.parseInt(charSevenArg);

        if (charSeven < 4) {
            century = "19";
        } else if (charSeven == 4 || charSeven == 9) {
            if (year < 37) {
                century = "20";
            } else {
                century = "19";
            }
        } else if (charSeven > 5 && charSeven < 9) {
            if (year < 58) {
                century = "20";
            } else {
                century = "18";
            }
        }
        // Return
        if (century == null) {
            return null;
        } else {
            return Integer.parseInt(century + yearArg);
        }

    }

    private AddressData createFromAddress(Address a, Use defaultUse) {
        AddressData.AddressBuilder builder = new AddressData.AddressBuilder();

        log.debug("Input address: " + a);

        if (a != null && a.getAddressLines() != null) {
            for (String addressLine : a.getAddressLines()) {
                builder.addAddressLine(addressLine);
            }
        } else {
            log.debug("Set address to N/A");
            builder.addAddressLine("N/A");
        }
        builder.setCity((a != null && a.getCity() != null ? a.getCity() : "N/A"));
        builder.setPostalCode((a != null && a.getPostalCode() != null ? a.getPostalCode() : "N/A"));
        builder.setCountry((a != null && a.getCountry() != null ? a.getCountry() : "N/A"));
        builder.setUse((a != null && a.getUse() != null ? Use.valueOf(a.getUse()) : defaultUse));
        return builder.build();
    }
}
