package io.oth.xdsgenerator.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.oth.xdsgenerator.model.cda.Person;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntryType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DocumentMetadata {

    public Code classCode;
    public Code formatCode;
    public Code healthcareFacilityTypeCode;
    public Code practiceSettingCode;
    public AvailabilityStatus availabilityStatus;
    public DocumentEntryType objectType;
    public Code patientId;
    public Date reportTime;
    public Code organisation;
    public Person authorPerson;
    public Code typeCode;
    public List<Code> eventCodes;
    public String languageCode;
    public String title;
    public Code contentTypeCode;
    public Person legalAuthenticator;
    public Date serviceStartTime;
    public Date serviceStopTime;
    public Code sourcePatientId;
    public Person sourcePatientInfoPerson;
    public String sourcePatientInfoGender;
    public Date sourcePatientInfoDateOfBirth;
    public String uniqueId;
    public Date submissionTime;
    private String mimeType;
    private Code confidentialityCode;

    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public Date getSubmissionTime() {
        return submissionTime;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mime) {
        this.mimeType = mime;
    }

    public DocumentEntryType getObjectType() {
        return objectType;
    }

    public void setObjectType(DocumentEntryType objectType) {
        this.objectType = objectType;
    }

    public Code getPatientId() {
        return patientId;
    }

    public void setPatientId(Code patientId) {
        this.patientId = patientId;
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public String getReportTimeStringUTC() {
        if (this.reportTime != null) {
            return dateToStringTimeZone(this.reportTime, "UTC");
        }
        return null;
    }

    public Code getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Code organisation) {
        this.organisation = organisation;
    }

    public List<Code> getEventCodes() {
        return eventCodes;
    }

    public void setEventCodes(List<Code> eventCodes) {
        this.eventCodes = eventCodes;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Code getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(Code typeCode) {
        this.typeCode = typeCode;
    }

    public Code getContentTypeCode() {
        return contentTypeCode;
    }

    public void setContentTypeCode(Code contentTypeCode) {
        this.contentTypeCode = contentTypeCode;
    }

    public Code getConfidentialityCode() {
        return confidentialityCode;
    }

    public void setConfidentialityCode(Code confidentialityCode) {
        this.confidentialityCode = confidentialityCode;
    }

    public Person getAuthorPerson() {
        return authorPerson;
    }

    public void setAuthorPerson(Person authorPerson) {
        this.authorPerson = authorPerson;
    }

    public Person getLegalAuthenticator() {
        return legalAuthenticator;
    }

    public void setLegalAuthenticator(Person legalAuthenticator) {
        this.legalAuthenticator = legalAuthenticator;
    }

    public Date getServiceStartTime() {
        return serviceStartTime;
    }

    public void setServiceStartTime(Date serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }

    public String getServiceStartTimeStringUTC() {
        if (this.serviceStartTime != null) {
            return dateToStringTimeZone(this.serviceStartTime, "UTC");
        }
        return null;

    }

    public Date getServiceStopTime() {
        return serviceStopTime;
    }

    public void setServiceStopTime(Date serviceStopTime) {
        this.serviceStopTime = serviceStopTime;
    }

    public String getServiceStopTimeStringUTC() {
        if (this.serviceStopTime != null) {
            return dateToStringTimeZone(this.serviceStopTime, "UTC");
        }
        return null;
    }

    public Code getSourcePatientId() {
        return sourcePatientId;
    }

    public void setSourcePatientId(Code sourcePatientId) {
        this.sourcePatientId = sourcePatientId;
    }

    public Person getSourcePatientInfoPerson() {
        return sourcePatientInfoPerson;
    }

    public void setSourcePatientInfoPerson(Person sourcePatientInfoPerson) {
        this.sourcePatientInfoPerson = sourcePatientInfoPerson;
    }

    public String getSourcePatientInfoGender() {
        return sourcePatientInfoGender;
    }

    public void setSourcePatientInfoGender(String sourcePatientInfoGender) {
        this.sourcePatientInfoGender = sourcePatientInfoGender;
    }

    public Date getSourcePatientInfoDateOfBirth() {
        return sourcePatientInfoDateOfBirth;
    }

    public void setSourcePatientInfoDateOfBirth(Date sourcePatientInfoDateOfBirth) {
        this.sourcePatientInfoDateOfBirth = sourcePatientInfoDateOfBirth;
    }

    public String getSourcePatientInfoDateOfBirthString() {
        return dateToStringTimeZone(this.sourcePatientInfoDateOfBirth, null).substring(0, 8);
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    private String dateToStringTimeZone(Date date, String timeZone) {
        DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        if (timeZone != null) {
            formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
        }
        return formatter.format(date);
    }

    public Code getClassCode() {
        return classCode;
    }

    public void setClassCode(Code classCode) {
        this.classCode = classCode;
    }

    public Code getFormatCode() {
        return formatCode;
    }

    public void setFormatCode(Code formatCode) {
        this.formatCode = formatCode;
    }

    public Code getHealthcareFacilityTypeCode() {
        return healthcareFacilityTypeCode;
    }

    public void setHealthcareFacilityTypeCode(Code healthcareFacilityTypeCode) {
        this.healthcareFacilityTypeCode = healthcareFacilityTypeCode;
    }

    public Code getPracticeSettingCode() {
        return practiceSettingCode;
    }

    public void setPracticeSettingCode(Code practiceSettingCode) {
        this.practiceSettingCode = practiceSettingCode;
    }

    public void setSubmissionTime(Date submissionTime) {
        this.submissionTime = submissionTime;
    }
}





