package io.oth.xdsgenerator.service;

import io.oth.xdsgenerator.model.kih.*;
import io.oth.xdsgenerator.model.phmr.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class KihToPhmrMappingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KihToPhmrMappingService.class);

    private static final List<String> VITAL_SIGN_CODES = new ArrayList<String>(
            Arrays.asList("NPU03011", "DNK05472","DNK05473", "NPU21692", "NPU08676"));

    private static final String KIH_ENUM_PROFESSIONAL_PRODUCER = "PNT";

    private static final String KIH_ENUM_USE_WORK = "W";

    private static final String PHR_ENUM_PRODUCER_HEALTHCARE_PROFESSIONAL = "HealthcareProfessional";
    private static final String PHR_ENUM_PRODUCER_HEALTHCARE_CITIZEN = "Citizen";
    private static final String PHR_ENUM_PRODUCER_TYPED_CITIZEN = "TypedByCitizen";
    private static final String PHR_ENUM_PRODUCER_TYPED_HCPRO = "TypedByHealthcareProfessional";
    private static final String PHR_ENUM_PRODUCER_AUTOMATIC = "Electronically";

    private static final String PHR_ENUM_USE_WORKPLACE = "WorkPlace";
    private static final String PHR_ENUM_USE_HOME_ADDRESS = "HomeAddress";

    private static final String PHR_TELECOM_MAIL = "mailto";
    private static final String PHR_TELECOM_PHONE = "tel";

    @Value("${sor.code}")
    private String sorCode;
    @Value("${sor.name}")
    private String sorName;

    private Organisation defaultOrganisation = null;

    public KihToPhmrMappingService() {
    }

    public KihToPhmrMappingService(String defaultSorCode, String createdByToSorCodeMapping) {
        // Til test
        this.defaultOrganisation = createOrganisation(defaultSorCode, createdByToSorCodeMapping);
    }

    public void createDefaultOrganisation(String code, String name) {
        defaultOrganisation = createOrganisation(code, name);
    }

    private Organisation createOrganisation(String code, String name) {
        Organisation organisation = new Organisation();
        organisation.setCode(code);
        organisation.setName(name);

        return organisation;
    }

    @PostConstruct
    private void initDefaultOrganisation() {
        Organisation organisation = createOrganisation(sorCode, sorName);
        LOGGER.info("Initialized default organisation: "+organisation);
        defaultOrganisation = organisation;
    }

    public Organisation getDefaultSorCode() {
        return defaultOrganisation;
    }

    /**
     * Maps from @SelfMonitoringCollection to
     * a @PersonalHealthMonitoringReportRequest
     */
    public PersonalHealthMonitoringReportRequest map(SelfMonitoringCollection collection) {
        PersonalHealthMonitoringReportRequest request = new PersonalHealthMonitoringReportRequest();

        List<ClinicalMeasurement> results = new ArrayList<>();
        List<ClinicalMeasurement> vitalSigns = new ArrayList<>();
        List<MedicalEquipment> equipments = new ArrayList<>();

        // Set author, custodian and legal authenticator
        request.setAuthorOrganisation(defaultOrganisation);
        //request.setAuthor(mapAssignedEntityToIdentity(author.getAssignedAuthor()));

        if (collection.getSelfMonitoringSamples() != null) {
            for (SelfMonitoringSamples s : collection.getSelfMonitoringSamples()) {

                if (s.getSelfMonitoringSample().getLaboratoryReports() != null) {
                    for (LaboratoryReport report : s.getSelfMonitoringSample().getLaboratoryReports()) {
                        ClinicalMeasurement measurement = mapReport(report);

                        if (isVitalSign(report.getIupacIdentifier())) {
                            vitalSigns.add(measurement);
                        } else {
                            results.add(measurement);
                        }

                        if (report.getInstrument() != null) {
                            MedicalEquipment equipment = mapEquipment(report.getInstrument());
                            if (shouldAddEquipment(equipments, equipment)) {
                                equipments.add(equipment);
                            }
                        }
                    }
                }
            }
        }

        if (request.getAuthorOrganisation() == null) {
            // Vi kræver en author ... fejl
            throw new IllegalArgumentException(
                    "Createdby skal være sat, da der kræves en AuthorOrganisation i PHMR dokumentet");
        }

        request.setResults(results.toArray(new ClinicalMeasurement[0]));
        request.setVitalSigns(vitalSigns.toArray(new ClinicalMeasurement[0]));
        request.setMedicalEquipment(equipments.toArray(new MedicalEquipment[0]));

        if (collection.getCitizen() != null) {
            LOGGER.debug("Setting patient from " + collection.getCitizen());
            request.setPatient(mapCitizen(collection.getCitizen()));
        } else {
            throw new IllegalArgumentException("Citizen skal være sat");
        }

        if (collection.getLegalAuthenticator() != null) {
            LegalAuthenticator legalAuthenticator = collection.getLegalAuthenticator();
            if (legalAuthenticator.getAssignedEntity() != null) {
                request.setAuthenticator(mapAssignedEntityToIdentity(legalAuthenticator.getAssignedEntity()));
            }
        } else {
            request.setAuthenticatorOrganisation(request.getAuthorOrganisation());
        }

        if (collection.getAuthor() != null) {
            Author author = collection.getAuthor();
            if (author.getAssignedAuthor() != null) {
                request.setAuthor(mapAssignedEntityToIdentity(author.getAssignedAuthor()));
            }
        }

        // if (collection.getCustodian() != null && collection.getCustodian().getAssignedCustodian() != null) {
        //     AssignedCustodian custodian = collection.getCustodian().getAssignedCustodian();

        //     request.setCustodian(mapAssignedCustodianToIdentity(custodian));

        //     if (custodian.getRepresentedCustodianOrganization() != null) {
        //         Organisation organisation = new Organisation();
        //         organisation.setName(custodian.getRepresentedCustodianOrganization().getName());
        //         request.setCustodianOrganisation(organisation);
        //     }
        // } else {
        //     request.setCustodianOrganisation(request.getAuthorOrganisation());
        // }

        // Hard code custion to the same as author.
        request.setCustodianOrganisation(request.getAuthorOrganisation());
        return request;
    }

    // private Organisation mapToOrganisation(String createdBy) {
    //     Organisation retVal = null;

    //     if (createdByToSorCodeMap.containsKey(createdBy)) {
    //         retVal = createdByToSorCodeMap.get(createdBy);
    //     }

    //     if (null == retVal) {

    //         retVal = defaultOrganisation;
    //     }

    //     LOGGER.debug("Returning organisation " + retVal);
    //     return retVal;
    // }

    private boolean shouldAddEquipment(List<MedicalEquipment> equipments, MedicalEquipment equipment) {
        for (MedicalEquipment e : equipments) {
            if (null != null && e.getMedicalDeviceCode().equals(equipment.getMedicalDeviceCode())) {
                return false;
            }
        }
        return true;
    }

    private static final String MEASUREMENT_TRANSFERED_BY_HCPROF = "typedbyhcprof";
    private static final String MEASUREMENT_TRANSFERED_BY_TYPED = "typed";
    private static final String MEASUREMENT_TRANSFERED_BY_AUTOMATIC = "automatic";

    private ClinicalMeasurement mapReport(LaboratoryReport report) {

        if (isBlank(report.getResultText())) {
            throw new IllegalArgumentException("ResultText must be set");
        }

        ClinicalMeasurement measurement = new ClinicalMeasurement();
        measurement.setMeasuredAt(report.getCreatedDateTime());
        measurement.setResultText(report.getResultText());
        measurement.setResultUnitText(report.getResultUnitText());
        measurement.setCode(report.getIupacIdentifier());

        if (!isBlank(report.getAnalysisText())) {
            measurement.setCodeDescription(report.getAnalysisText());
        } else {
            measurement.setCodeDescription(report.getIupacIdentifier());
        }

        // map based on measurement transfered by

        mapTransferedByAndPerformer(report, measurement);

        measurement.setId(report.getUuidIdentifier());

        return measurement;
    }

    /**
     * Handles mapping of {@link LaboratoryReport} MeasurementTransferedBy to set
     * the correct values for PHMR
     */
    protected void mapTransferedByAndPerformer(LaboratoryReport report, ClinicalMeasurement measurement) {
        LOGGER.debug("Measurement Transferred by is " + report.MeasurementTransferredBy);
        if (null != report.MeasurementTransferredBy) {

            switch (report.MeasurementTransferredBy) {
                case MEASUREMENT_TRANSFERED_BY_TYPED:
                    measurement.setContextPerformerType(PHR_ENUM_PRODUCER_HEALTHCARE_CITIZEN);
                    measurement.setContextTransferedMethod(PHR_ENUM_PRODUCER_TYPED_CITIZEN);
                    break;
                case MEASUREMENT_TRANSFERED_BY_HCPROF:
                    measurement.setContextPerformerType(PHR_ENUM_PRODUCER_HEALTHCARE_PROFESSIONAL);
                    measurement.setContextTransferedMethod(PHR_ENUM_PRODUCER_TYPED_HCPRO);
                    break;
                case MEASUREMENT_TRANSFERED_BY_AUTOMATIC:
                    measurement.setContextPerformerType(PHR_ENUM_PRODUCER_HEALTHCARE_CITIZEN);
                    measurement.setContextTransferedMethod(PHR_ENUM_PRODUCER_AUTOMATIC);
                    break;
                default:
                    measurement.setContextPerformerType(PHR_ENUM_PRODUCER_HEALTHCARE_CITIZEN);
                    measurement.setContextTransferedMethod(PHR_ENUM_PRODUCER_AUTOMATIC);
            }
        }

    }

    private MedicalEquipment mapEquipment(Instrument instrument) {
        MedicalEquipment equipment = new MedicalEquipment();
        equipment.setMedicalDeviceCode(instrument.getMedComID());
        equipment.setMedicalDeviceDisplayName(instrument.getProductType());
        equipment.setManufacturerModelName(instrument.getManufacturer() + "/" + instrument.getModel());
        equipment.setSoftwareName(instrument.getSoftwareVersion());
        return equipment;
    }

    private Identity mapAssignedEntityToIdentity(AssignedEntity entity) {
        Identity identity = new Identity();

        identity.setAddress(mapAddressPostal(entity.getAddress(), PHR_ENUM_USE_WORKPLACE));

        setNamesOnIdentity(identity, entity.getAssignedPerson());

        setPhoneAndEmailOnIdentity(identity, entity.getPhoneNumberSubscriber(), entity.getEmailAddress());

        return identity;
    }

    private Identity mapAssignedCustodianToIdentity(AssignedCustodian custodian) {
        Identity identity = new Identity();

        identity.setAddress(
                mapAddressPostal(custodian.getRepresentedCustodianOrganization().getAddress(), PHR_ENUM_USE_WORKPLACE));

        setPhoneAndEmailOnIdentity(identity,
                new PhoneNumberSubscriber[] {
                        custodian.getRepresentedCustodianOrganization().getPhoneNumberSubscriber() },
                new EmailAddress[] {});

        return identity;
    }

    private Identity mapCitizen(Citizen c) {
        Identity identity = new Identity();

        identity.setPatientIdentifier(c.getPersonCivilRegistrationIdentifier());

        setNamesOnIdentity(identity, c.getPersonNameStructure());

        setPhoneAndEmailOnIdentity(identity, new PhoneNumberSubscriber[] { c.getPhoneNumberSubscriber() },
                new EmailAddress[] { c.getEmailAddress() });

        identity.setAddress(mapAddressPostal(c.getAddressPostal(), PHR_ENUM_USE_HOME_ADDRESS));

        return identity;
    }

    private void setNamesOnIdentity(Identity identity, PersonNameStructure names) {
        if (names != null) {
            if (names.getPersonMiddleName() != null) {
                identity.setGivenNames(new String[] { names.getPersonGivenName(), names.getPersonMiddleName() });
            } else {
                identity.setGivenNames(new String[] { names.getPersonGivenName() });
            }

            identity.setFamilyName(names.getPersonSurName());
        }
    }

    private void setPhoneAndEmailOnIdentity(Identity identity, PhoneNumberSubscriber[] phones, EmailAddress[] emails) {
        List<Telecom> telecoms = new ArrayList<>();

        if (emails != null) {
            for (EmailAddress e : emails) {
                if (e != null) {
                    Telecom emailT = new Telecom();
                    emailT.setProtocol(PHR_TELECOM_MAIL);
                    emailT.setTelecomString(e.getEmailAddressIdentifier());
                    emailT.setUse(mapUse(e.getEmailAddressUse()));
                    telecoms.add(emailT);
                }
            }
        }

        if (phones != null) {
            for (PhoneNumberSubscriber p : phones) {
                if (p != null) {
                    Telecom phone = new Telecom();
                    phone.setProtocol(PHR_TELECOM_PHONE);
                    phone.setTelecomString(p.getPhoneNumberIdentifier());
                    phone.setUse(mapUse(p.getPhoneNumberUse()));
                    telecoms.add(phone);
                }
            }
        }

        identity.setTelecoms(telecoms.toArray(new Telecom[0]));
    }

    private String mapUse(String u) {
        if (KIH_ENUM_USE_WORK.equals(u)) {
            return PHR_ENUM_USE_WORKPLACE;
        } else {
            return PHR_ENUM_USE_HOME_ADDRESS;
        }
    }

    protected Address mapAddressPostal(AddressPostal ap, String use) {
        Address a = new Address();
        LOGGER.debug("Use: " + use + " AP: " + ap);
        if (ap != null) {

            List<String> addressLines = new ArrayList<>();

            String addressLine = getStringNotNull(ap.getStreetName()) + " "
                    + getStringNotNull(ap.getStreetBuildingIdentifier()) + " "
                    + getStringNotNull(ap.getFloorIdentifier()) + " " + getStringNotNull(ap.getSuiteIdentifier());

            LOGGER.debug("Using address" + addressLine.strip());

            addressLines.add(addressLine.strip());
            if (null != ap.getDistrictName()) {
                addressLines.add(ap.getDistrictName());

            }
            if (null != ap.getDistrictSubdivisionIdentifier()) {
                addressLines.add(ap.getDistrictSubdivisionIdentifier());
            }

            String[] lines = addressLines.toArray(new String[0]);
            LOGGER.debug("Setting address lines" + lines);
            for (int i = 0; i < lines.length; i++) {
                LOGGER.debug("Line " + i + "> " + lines[i]);
            }
            a.setAddressLines(lines);
            a.setPostalCode(ap.getPostCodeIdentifier());
            a.setCity(ap.getMunicipalityName());

            if (ap.getCountryIdentificationCode() != null) {
                a.setCountry(ap.getCountryIdentificationCode().getValue());
            }

            a.setUse(use);
            return a;
        }
        return null;
    }

    private String getStringNotNull(String s) {
        if (s == null) {
            return "";
        } else {
            return s.strip();
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().length() == 0;
    }

    private boolean isVitalSign(String iupacCode) {
        if (iupacCode == null) {
            return false;
        }
        return VITAL_SIGN_CODES.contains(iupacCode);
    }
}
