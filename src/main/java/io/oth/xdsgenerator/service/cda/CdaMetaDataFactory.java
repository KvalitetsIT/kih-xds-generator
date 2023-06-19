package io.oth.xdsgenerator.service.cda;

import dk.s4.hl7.cda.convert.CDAMetadataXmlCodec;
import dk.s4.hl7.cda.model.CodedValue;
import dk.s4.hl7.cda.model.cdametadata.CDAMetadata;
import io.oth.xdsgenerator.model.Code;
import io.oth.xdsgenerator.model.DocumentMetadata;
import io.oth.xdsgenerator.model.cda.Person;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntryType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

@Component
public class CdaMetaDataFactory {

    private static Logger log = LoggerFactory.getLogger(CdaMetaDataFactory.class);

    public DocumentMetadata createFromCdaRegistrationRequest(DocumentMetadata cdaMetadata, String document) {

        log.info("Starting cda metadata map");
        DocumentMetadata cdaDocumentMetadata = new DocumentMetadata();

        if (cdaMetadata != null) {

            log.info("Transfering metadata - " + cdaMetadata.getHealthcareFacilityTypeCode().getCode() + " "
                    + cdaMetadata.getHealthcareFacilityTypeCode().getName());
            cdaDocumentMetadata.setClassCode(cdaMetadata.getClassCode());
            cdaDocumentMetadata.setFormatCode(cdaMetadata.getFormatCode());
            cdaDocumentMetadata.setHealthcareFacilityTypeCode(cdaMetadata.getHealthcareFacilityTypeCode());
            cdaDocumentMetadata.setPracticeSettingCode(cdaMetadata.getPracticeSettingCode());
            cdaDocumentMetadata.setSubmissionTime(cdaMetadata.getSubmissionTime());
            cdaDocumentMetadata.setMimeType("text/xml");

            cdaDocumentMetadata.setAvailabilityStatus(AvailabilityStatus.APPROVED);
            cdaDocumentMetadata.setObjectType(DocumentEntryType.STABLE);

            log.debug("ClassCode is: " + cdaDocumentMetadata.getClassCode());
            log.debug("FormatCode is" + cdaDocumentMetadata.getFormatCode());
            log.debug("HealthcareFacilityTypeCode is: " + cdaDocumentMetadata.getHealthcareFacilityTypeCode());
            log.debug("SubmissionTime is: " + cdaDocumentMetadata.getSubmissionTime());
            log.debug("PracticeSettingCode is: " + cdaDocumentMetadata.getPracticeSettingCode());
            log.debug("MimeType is: " + cdaDocumentMetadata.getMimeType());

            log.debug("Availabilitystatus is: " + cdaDocumentMetadata.getAvailabilityStatus());
            log.debug("Objecttype is: " + cdaDocumentMetadata.getObjectType());

        }

        getMetadataFromDocument(cdaDocumentMetadata, document);
        log.debug("After getMetadataFromDocument()");

        log.debug("ClassCode is: {}", cdaDocumentMetadata.getClassCode());
        log.debug("FormatCode is {}", cdaDocumentMetadata.getFormatCode());
        log.debug("HealthcareFacilityTypeCode is: {}", cdaDocumentMetadata.getHealthcareFacilityTypeCode());
        log.debug("SubmissionTime is: {}", cdaDocumentMetadata.getSubmissionTime());
        log.debug("PracticeSettingCode is: {}", cdaDocumentMetadata.getPracticeSettingCode());
        log.debug("MimeType is: {}", cdaDocumentMetadata.getMimeType());

        log.debug("Availabilitystatus is: {}", cdaDocumentMetadata.getAvailabilityStatus());
        log.debug("Objecttype is: {}", cdaDocumentMetadata.getObjectType());
        log.info("Unique ID: {}", cdaDocumentMetadata.getUniqueId());
        log.debug("Returning from  cda metadata map");
        return cdaDocumentMetadata;
    }

    private void getMetadataFromDocument(DocumentMetadata cdaDocumentMetadata, String document) {
        log.debug("Starting metadata mpa from document");

        CDAMetadataXmlCodec codec = new CDAMetadataXmlCodec();
        CDAMetadata cdaMetadataDecoded = codec.decode(document);

        // author.authorInstitution - organization
        if (cdaMetadataDecoded.getAuthorInstitution() != null
                && cdaMetadataDecoded.getAuthorInstitution().getCode() != null
                && cdaMetadataDecoded.getAuthorInstitution().getCodeSystem() != null) {
            Code autherOrganizationCode = createCode(cdaMetadataDecoded.getAuthorInstitution().getCode(),
                    cdaMetadataDecoded.getAuthorInstitution().getCodeSystem(),
                    cdaMetadataDecoded.getAuthorInstitution().getDisplayName());
            cdaDocumentMetadata.setOrganisation(autherOrganizationCode);
            log.debug(cdaDocumentMetadata.getOrganisation().toString());
        }

        // auther.authorperson
        if (cdaMetadataDecoded.getAuthorperson() != null) {
            Person authorPerson = new Person();
            if (cdaMetadataDecoded.getAuthorperson().getFamilyName() != null) {
                authorPerson.setFamilyName(cdaMetadataDecoded.getAuthorperson().getFamilyName());
                log.debug(authorPerson.getFamilyName());
            }
            if (cdaMetadataDecoded.getAuthorperson().getGivenNames() != null
                    && cdaMetadataDecoded.getAuthorperson().getGivenNames().length > 0) {
                authorPerson.setGivenName(cdaMetadataDecoded.getAuthorperson().getGivenNames()[0]);
                log.debug(authorPerson.getGivenName());
                if (cdaMetadataDecoded.getAuthorperson().getGivenNames().length > 1) {
                    authorPerson.setSecondAndFurtherGivenNames(cdaMetadataDecoded.getAuthorperson().getGivenNames()[1]);
                    for (int i = 2; i < cdaMetadataDecoded.getAuthorperson().getGivenNames().length; i++) {
                        authorPerson.setSecondAndFurtherGivenNames(authorPerson.getSecondAndFurtherGivenNames() + "&"
                                + cdaMetadataDecoded.getAuthorperson().getGivenNames()[i]);
                        log.debug(authorPerson.getSecondAndFurtherGivenNames());
                    }
                }
            }
            cdaDocumentMetadata.setAuthorPerson(authorPerson);
        }

        // confidentialityCode
        if (cdaMetadataDecoded.getConfidentialityCodeCodedValue() != null
                && cdaMetadataDecoded.getConfidentialityCodeCodedValue().getCode() != null
                && cdaMetadataDecoded.getConfidentialityCodeCodedValue().getCodeSystem() != null) {
            Code confidentialityCode = createCode(cdaMetadataDecoded.getConfidentialityCodeCodedValue().getCode(),
                    cdaMetadataDecoded.getConfidentialityCodeCodedValue().getCodeSystem(),
                    cdaMetadataDecoded.getConfidentialityCodeCodedValue().getDisplayName());
            cdaDocumentMetadata.setConfidentialityCode(confidentialityCode);
            log.debug(cdaDocumentMetadata.getConfidentialityCode().toString());

        }

        // contentTypeCode - not used

        // creationTime
        if (cdaMetadataDecoded.getCreationTime() != null) {
            cdaDocumentMetadata.setReportTime(cdaMetadataDecoded.getCreationTime());
            log.debug(cdaDocumentMetadata.getReportTime().toString());
        }

        // eventCodedList
        for (CodedValue event : cdaMetadataDecoded.getEventCodeList()) {
            if (cdaDocumentMetadata.getEventCodes() == null) {
                cdaDocumentMetadata.setEventCodes(new LinkedList<Code>());
            }
            Code eventCode = createCode(event.getCode(), event.getCodeSystem(), event.getDisplayName());
            cdaDocumentMetadata.getEventCodes().add(eventCode);
            log.debug(eventCode.toString());
        }

        // LanguageCode
        cdaDocumentMetadata.setLanguageCode(cdaMetadataDecoded.getLanguageCode());
        log.debug(cdaDocumentMetadata.getLanguageCode());

        // legalAuthenticator
        if (cdaMetadataDecoded.getLegalAuthenticator() != null
                && cdaMetadataDecoded.getLegalAuthenticator().getPersonIdentity() != null) {
            Person legalAuthenticator = new Person();
            if (cdaMetadataDecoded.getLegalAuthenticator().getPersonIdentity().getFamilyName() != null) {
                legalAuthenticator
                        .setFamilyName(cdaMetadataDecoded.getLegalAuthenticator().getPersonIdentity().getFamilyName());
                log.debug(legalAuthenticator.getFamilyName());
            }
            if (cdaMetadataDecoded.getLegalAuthenticator().getPersonIdentity().getGivenNames() != null
                    && cdaMetadataDecoded.getLegalAuthenticator().getPersonIdentity().getGivenNames().length > 0) {
                legalAuthenticator.setGivenName(
                        cdaMetadataDecoded.getLegalAuthenticator().getPersonIdentity().getGivenNames()[0]);
                log.debug(legalAuthenticator.getGivenName());
                if (cdaMetadataDecoded.getLegalAuthenticator().getPersonIdentity().getGivenNames().length > 1) {
                    legalAuthenticator.setSecondAndFurtherGivenNames(
                            cdaMetadataDecoded.getLegalAuthenticator().getPersonIdentity().getGivenNames()[1]);
                    for (int i = 2; i < cdaMetadataDecoded.getLegalAuthenticator().getPersonIdentity()
                            .getGivenNames().length; i++) {
                        legalAuthenticator.setSecondAndFurtherGivenNames(legalAuthenticator
                                .getSecondAndFurtherGivenNames() + "&"
                                + cdaMetadataDecoded.getLegalAuthenticator().getPersonIdentity().getGivenNames()[i]);
                        log.debug(legalAuthenticator.getSecondAndFurtherGivenNames());
                    }
                }
            }
            cdaDocumentMetadata.setLegalAuthenticator(legalAuthenticator);
        }

        // patientId
        if (cdaMetadataDecoded.getPatientId() != null && cdaMetadataDecoded.getPatientId().getCode() != null
                && cdaMetadataDecoded.getPatientId().getCodeSystem() != null) {
            Code patientIdCode = createCode(cdaMetadataDecoded.getPatientId().getCode(),
                    cdaMetadataDecoded.getPatientId().getCodeSystem(), "");
            cdaDocumentMetadata.setPatientId(patientIdCode);
            log.debug(cdaDocumentMetadata.getPatientId().toString());
        }

        // serviceStartTime
        if (cdaMetadataDecoded.getServiceStartTime() != null) {
            cdaDocumentMetadata.setServiceStartTime(cdaMetadataDecoded.getServiceStartTime());
            log.debug(cdaDocumentMetadata.getServiceStartTime().toString());
        }

        // serviceStopTime
        if (cdaMetadataDecoded.getServiceStopTime() != null) {
            cdaDocumentMetadata.setServiceStopTime(cdaMetadataDecoded.getServiceStopTime());
            log.debug(cdaDocumentMetadata.getServiceStopTime().toString());
        }

        // sourcePatientId
        if (cdaMetadataDecoded.getSourcePatientId() != null && cdaMetadataDecoded.getSourcePatientId().getCode() != null
                && cdaMetadataDecoded.getSourcePatientId().getCodeSystem() != null) {
            Code sourcePatientIdCode = createCode(cdaMetadataDecoded.getSourcePatientId().getCode(),
                    cdaMetadataDecoded.getSourcePatientId().getCodeSystem(), "");
            cdaDocumentMetadata.setSourcePatientId(sourcePatientIdCode);
            log.debug(cdaDocumentMetadata.getSourcePatientId().toString());
        }

        // sourcePatientInfo
        if (cdaMetadataDecoded.getPatient() != null) {
            Person sourcePatientInfoPerson = new Person();
            if (cdaMetadataDecoded.getPatient().getFamilyName() != null) {
                sourcePatientInfoPerson.setFamilyName(cdaMetadataDecoded.getPatient().getFamilyName());
                log.debug(sourcePatientInfoPerson.getFamilyName());
            }
            if (cdaMetadataDecoded.getPatient().getGivenNames() != null
                    && cdaMetadataDecoded.getPatient().getGivenNames().length > 0) {
                sourcePatientInfoPerson.setGivenName(cdaMetadataDecoded.getPatient().getGivenNames()[0]);
                log.debug(sourcePatientInfoPerson.getGivenName());
                if (cdaMetadataDecoded.getPatient().getGivenNames().length > 1) {
                    sourcePatientInfoPerson
                            .setSecondAndFurtherGivenNames(cdaMetadataDecoded.getPatient().getGivenNames()[1]);
                    for (int i = 2; i < cdaMetadataDecoded.getPatient().getGivenNames().length; i++) {
                        sourcePatientInfoPerson
                                .setSecondAndFurtherGivenNames(sourcePatientInfoPerson.getSecondAndFurtherGivenNames()
                                        + "&" + cdaMetadataDecoded.getPatient().getGivenNames()[i]);
                        log.debug(sourcePatientInfoPerson.getSecondAndFurtherGivenNames());
                    }
                }
            }
            cdaDocumentMetadata.setSourcePatientInfoPerson(sourcePatientInfoPerson);
            if (cdaMetadataDecoded.getPatient().getBirthTime() != null) {
                cdaDocumentMetadata.setSourcePatientInfoDateOfBirth(cdaMetadataDecoded.getPatient().getBirthTime());
                log.debug(cdaDocumentMetadata.getSourcePatientInfoDateOfBirth().toString());
            }
            if (cdaMetadataDecoded.getPatient().getGender() != null) {
                cdaDocumentMetadata
                        .setSourcePatientInfoGender(cdaMetadataDecoded.getPatient().getGender().name().substring(0, 1));
                log.debug(cdaDocumentMetadata.getSourcePatientInfoGender());
            }
        }

        // title
        if (cdaMetadataDecoded.getTitle() != null) {
            cdaDocumentMetadata.setTitle(cdaMetadataDecoded.getTitle());
            log.debug(cdaDocumentMetadata.getTitle());
        }

        // typeCode
        if ((cdaMetadataDecoded.getCodeCodedValue() != null)
                && (cdaMetadataDecoded.getCodeCodedValue().getCode() != null)
                && (cdaMetadataDecoded.getCodeCodedValue().getCodeSystem() != null)
                && (cdaMetadataDecoded.getCodeCodedValue().getDisplayName() != null)) {
            Code typeCode = createCode(cdaMetadataDecoded.getCodeCodedValue().getCode(),
                    cdaMetadataDecoded.getCodeCodedValue().getCodeSystem(),
                    cdaMetadataDecoded.getCodeCodedValue().getDisplayName());
            cdaDocumentMetadata.setTypeCode(typeCode);
            log.debug(cdaDocumentMetadata.getTypeCode().toString());
        }

        // uniqeId
        if (cdaMetadataDecoded.getId() != null && cdaMetadataDecoded.getId().getExtension() != null
                && cdaMetadataDecoded.getId().getRoot() != null) {
            cdaDocumentMetadata.setUniqueId(
                    cdaMetadataDecoded.getId().getExtension() + "^" + cdaMetadataDecoded.getId().getRoot());

        }
        log.debug("Returning metadata mpa from document");
    }

    private Code createCode(String code, String codeSystem, String displayName) {
        Code returnCode = new Code();
        returnCode.setCode(code);
        returnCode.setCodingScheme(codeSystem);
        returnCode.setName(displayName);
        return returnCode;
    }

}
