package io.oth.xdsgenerator.service.cda;

import io.oth.xdsgenerator.model.DocumentMetadata;
import io.oth.xdsgenerator.service.TestConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import io.oth.xdsgenerator.model.Code;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestConfiguration.class })
public class CDAParserTest {

	private CdaMetaDataFactory cdaMetaDataFactory;
	
	@BeforeEach
	public void setupData() throws IOException {
		cdaMetaDataFactory = new CdaMetaDataFactory();
	}
	
	@Test
	public void testCDA_QRD_KOL_Example_1_Matis() throws IOException, ParseException {
		String content = new String(Files.readAllBytes(Paths.get("src/test/resources/QRD_KOL_Example_1_Matis.xml")),StandardCharsets.UTF_8);
		DocumentMetadata metadata = cdaMetaDataFactory.createFromCdaRegistrationRequest(createMetadata(), content);

		assertNotNull(metadata);
		
		// Organisation
		assertNotNull(metadata.getOrganisation());
		
		// Author Person 2.2.1.2
		assertNotNull(metadata.getAuthorPerson());
		assertNotNull(metadata.getAuthorPerson().getFamilyName());
		assertNotNull(metadata.getAuthorPerson().getGivenName());
		assertNotNull(metadata.getAuthorPerson().getSecondAndFurtherGivenNames());
		assertEquals("Berggren", metadata.getAuthorPerson().getFamilyName());
		assertEquals("Nancy", metadata.getAuthorPerson().getGivenName());
		assertEquals("Ann", metadata.getAuthorPerson().getSecondAndFurtherGivenNames());
		
		// effectiveTime
		assertNotNull(metadata.getReportTimeStringUTC());
		assertEquals("20160609124510", metadata.getReportTimeStringUTC()); 
		
		// confidentialityCode
		assertNotNull(metadata.getConfidentialityCode());
		assertNotNull(metadata.getConfidentialityCode().getCodingScheme());
		assertNotNull(metadata.getConfidentialityCode().getCode());
		assertNull(metadata.getConfidentialityCode().getName());
		assertEquals("2.16.840.1.113883.5.25", metadata.getConfidentialityCode().getCodingScheme());
		assertEquals("N", metadata.getConfidentialityCode().getCode());		
		
		// eventcodes
		assertNull(metadata.getEventCodes());
		
		// languagecode
		assertNotNull(metadata.getLanguageCode());
		assertEquals("da-DK", metadata.getLanguageCode());
		
		// legal authenticator 2.2.16
		assertNull(metadata.getLegalAuthenticator());
		
		// service start Time 2.2.24
		assertNull(metadata.getServiceStartTimeStringUTC());
		
		// service stop Time 2.2.25
		assertNull(metadata.getServiceStopTimeStringUTC());
		
		// Source Patient Id 2.2.28
		assertNotNull(metadata.getPatientId());
		assertEquals("2512489996", metadata.getPatientId().getCode());
		assertEquals("1.2.208.176.1.2", metadata.getPatientId().getCodingScheme());
		assertEquals("", metadata.getPatientId().getName());
		
		// Source Patient Info 2.2.29
		assertNotNull(metadata.getSourcePatientInfoDateOfBirth());
	    assertEquals(new GregorianCalendar(1948, 11, 25, 00, 00, 00).getTime(), metadata.getSourcePatientInfoDateOfBirth());
		assertNotNull(metadata.getSourcePatientInfoGender());
		assertEquals("F", metadata.getSourcePatientInfoGender());
		assertNotNull(metadata.getSourcePatientInfoPerson());
		assertNotNull(metadata.getSourcePatientInfoPerson().getFamilyName());
		assertEquals("Berggren", metadata.getSourcePatientInfoPerson().getFamilyName());
		assertNotNull(metadata.getSourcePatientInfoPerson().getGivenName());
		assertEquals("Nancy", metadata.getSourcePatientInfoPerson().getGivenName());
		assertNotNull(metadata.getSourcePatientInfoPerson().getSecondAndFurtherGivenNames());
		assertEquals("Ann", metadata.getSourcePatientInfoPerson().getSecondAndFurtherGivenNames());
		
		// Title 2.2.31
		assertEquals("KOL spørgeskema", metadata.getTitle());
		
		// Typecode 2.2.32
		assertNotNull(metadata.getTypeCode());
		assertEquals("74465-6", metadata.getTypeCode().getCode());
		assertEquals("2.16.840.1.113883.6.1", metadata.getTypeCode().getCodingScheme());
		assertEquals("Questionnaire Response Document", metadata.getTypeCode().getName());
		
		// uniqueid 2.2.33
		assertNotNull(metadata.getUniqueId());
		assertEquals("edb802b0-2e36-11e6-bdf4-0800200c9a66^1.2.208.184", metadata.getUniqueId());
	}

	
	@Test
	public void testCDA_PHMR_KOL_Parser() throws IOException, ParseException {
		String content = new String(Files.readAllBytes(Paths.get("src/test/resources/PHMR_KOL_Example_1_MaTIS.xml")),StandardCharsets.UTF_8);
		DocumentMetadata metadata = cdaMetaDataFactory.createFromCdaRegistrationRequest(createMetadata(), content);
		
		assertNotNull(metadata);
		
		// Organisation
		assertNotNull(metadata.getOrganisation());
		assertNotNull(metadata.getOrganisation().getCodingScheme());
		assertNotNull(metadata.getOrganisation().getCode());
		assertNotNull(metadata.getOrganisation().getName());
		assertEquals("1.2.208.176.1.1", metadata.getOrganisation().getCodingScheme());
		assertEquals("368061000016003", metadata.getOrganisation().getCode());		
		assertEquals("Aalborg Universitetshospital", metadata.getOrganisation().getName());
		
		// Author Person 2.2.1.2
		assertNotNull(metadata.getAuthorPerson());
		assertNotNull(metadata.getAuthorPerson().getFamilyName());
		assertNotNull(metadata.getAuthorPerson().getGivenName());
		assertNull(metadata.getAuthorPerson().getSecondAndFurtherGivenNames());
		assertEquals("Andersen", metadata.getAuthorPerson().getFamilyName());
		assertEquals("Anders", metadata.getAuthorPerson().getGivenName());
		
		// effectiveTime
		assertNotNull(metadata.getReportTimeStringUTC());
		assertEquals("20160609125010", metadata.getReportTimeStringUTC());		
		
		// confidentialityCode
		assertNotNull(metadata.getConfidentialityCode());
		assertNotNull(metadata.getConfidentialityCode().getCodingScheme());
		assertNotNull(metadata.getConfidentialityCode().getCode());
		assertNull(metadata.getConfidentialityCode().getName());
		assertEquals("2.16.840.1.113883.5.25", metadata.getConfidentialityCode().getCodingScheme());
		assertEquals("N", metadata.getConfidentialityCode().getCode());		
		
		// eventcodes
		assertNull(metadata.getEventCodes());
		
		// languagecode
		assertNotNull(metadata.getLanguageCode());
		assertEquals("da-DK", metadata.getLanguageCode());
		
		// legal authenticator 2.2.16
		assertNotNull(metadata.getLegalAuthenticator());
		assertNotNull(metadata.getLegalAuthenticator().getFamilyName());
		assertNotNull(metadata.getLegalAuthenticator().getGivenName());
		assertNull(metadata.getLegalAuthenticator().getSecondAndFurtherGivenNames());
		assertEquals("Olsen", metadata.getLegalAuthenticator().getFamilyName());
		assertEquals("Lars", metadata.getLegalAuthenticator().getGivenName());

		// service start Time 2.2.24
		assertNotNull(metadata.getServiceStartTimeStringUTC());
		assertEquals("20160609101010", metadata.getServiceStartTimeStringUTC());
		
		// service stop Time 2.2.25
		assertNotNull(metadata.getServiceStopTimeStringUTC());
		assertEquals("20160609103010", metadata.getServiceStopTimeStringUTC());
		
		// Source Patient Id 2.2.28
		assertNotNull(metadata.getPatientId());
		assertEquals("2512489996", metadata.getPatientId().getCode());
		assertEquals("1.2.208.176.1.2", metadata.getPatientId().getCodingScheme());
		assertEquals("", metadata.getPatientId().getName());
		
		// Source Patient Info 2.2.29
		assertNotNull(metadata.getSourcePatientInfoDateOfBirth());
	    assertEquals(new GregorianCalendar(1948, 11, 25, 00, 00, 00).getTime(), metadata.getSourcePatientInfoDateOfBirth());
		assertNotNull(metadata.getSourcePatientInfoGender());
		assertEquals("F", metadata.getSourcePatientInfoGender());
		assertNotNull(metadata.getSourcePatientInfoPerson());
		assertNotNull(metadata.getSourcePatientInfoPerson().getFamilyName());
		assertEquals("Berggren", metadata.getSourcePatientInfoPerson().getFamilyName());
		assertNotNull(metadata.getSourcePatientInfoPerson().getGivenName());
		assertEquals("Nancy", metadata.getSourcePatientInfoPerson().getGivenName());
		assertNotNull(metadata.getSourcePatientInfoPerson().getSecondAndFurtherGivenNames());
		assertEquals("Ann", metadata.getSourcePatientInfoPerson().getSecondAndFurtherGivenNames());

		// Title 2.2.31
		assertEquals("Hjemmemonitorering for 2512489996", metadata.getTitle());
		
		// Typecode 2.2.32
		assertNotNull(metadata.getTypeCode());
		assertEquals("53576-5", metadata.getTypeCode().getCode());
		assertEquals("2.16.840.1.113883.6.1", metadata.getTypeCode().getCodingScheme());
		assertEquals("Personal Health Monitoring Report", metadata.getTypeCode().getName());
		
		// uniqueid 2.2.33
		assertNotNull(metadata.getUniqueId());
		assertEquals("021ba3bd-6935-45ca-bdcd-ed77f8b2ee2a^1.2.208.184", metadata.getUniqueId());
	}
	
	@Test
	public void testCDA_PHMR_KOL_Parser_Tidszone_test() throws IOException, ParseException {
		String content = new String(Files.readAllBytes(Paths.get("src/test/resources/PHMR_KOL_Example_1_MaTIS_anden_tidszone.xml")),StandardCharsets.UTF_8);
		DocumentMetadata metadata = cdaMetaDataFactory.createFromCdaRegistrationRequest(createMetadata(), content);
		
		assertNotNull(metadata);
		
		// effectiveTime
		assertNotNull(metadata.getReportTimeStringUTC());
		assertEquals("20160609105010", metadata.getReportTimeStringUTC());
		
		// service start Time 2.2.24
		assertNotNull(metadata.getServiceStartTimeStringUTC());
		assertEquals("20160609081010", metadata.getServiceStartTimeStringUTC());
		
		// service stop Time 2.2.25
		assertNotNull(metadata.getServiceStopTimeStringUTC());
		assertEquals("20160609083010", metadata.getServiceStopTimeStringUTC());
		
	}
	
	@Test
	public void testCDA_Ex4_COPD_MODIFIED_Parser() throws IOException, ParseException {
		// Given
		String content = new String(Files.readAllBytes(Paths.get("src/test/resources/Ex4-COPD-MODIFIED.xml")),StandardCharsets.UTF_8);

		// When
		DocumentMetadata metadata = cdaMetaDataFactory.createFromCdaRegistrationRequest(createMetadata(), content);
		
		// Then
		assertNotNull(metadata);
		
		// Author Institution 2.2.1.1
		assertNotNull(metadata.getOrganisation());
		assertNotNull(metadata.getOrganisation().getCodingScheme());
		assertNotNull(metadata.getOrganisation().getCode());
		assertNotNull(metadata.getOrganisation().getName());
		assertEquals("1.2.208.176.1.1", metadata.getOrganisation().getCodingScheme());
		assertEquals("77668685", metadata.getOrganisation().getCode());		
		assertEquals("Odense Universitetshospital, Odense", metadata.getOrganisation().getName());
		
		// Author Person 2.2.1.2
		assertNotNull(metadata.getAuthorPerson());
		assertNotNull(metadata.getAuthorPerson().getFamilyName());
		assertNotNull(metadata.getAuthorPerson().getGivenName());
		assertNull(metadata.getAuthorPerson().getSecondAndFurtherGivenNames());
		assertEquals("Madsen", metadata.getAuthorPerson().getFamilyName());
		assertEquals("M", metadata.getAuthorPerson().getGivenName());

		// confidentialityCode 2.2.5
		assertNotNull(metadata.getConfidentialityCode());
		assertNotNull(metadata.getConfidentialityCode().getCodingScheme());
		assertNotNull(metadata.getConfidentialityCode().getCode());
		assertNull(metadata.getConfidentialityCode().getName());
		assertEquals("2.16.840.1.113883.5.25", metadata.getConfidentialityCode().getCodingScheme());
		assertEquals("N", metadata.getConfidentialityCode().getCode());		

		// Creation Time 2.2.7
		assertNotNull(metadata.getReportTimeStringUTC());
		assertEquals("20130217081500", metadata.getReportTimeStringUTC());
		
		// Event Code List 2.2.9
		assertNotNull(metadata.getEventCodes());
		assertEquals(2, metadata.getEventCodes().size());
		Map<String, Code> eventCodeMap = new HashMap<>();
		for (Code eventCode : metadata.getEventCodes()) {
			eventCodeMap.put((eventCode.getCode() != null ? eventCode.getCode() : "X"), eventCode);
		}
		Code codeNPU03011 = eventCodeMap.get("NPU03011");
		assertNotNull(codeNPU03011);
		assertEquals("O2 sat.;Hb(aB)", codeNPU03011.getName());
		assertEquals("1.2.208.176.2.1", codeNPU03011.getCodingScheme());
		Code codeMCS88016 = eventCodeMap.get("MCS88016");
		assertNotNull(codeMCS88016);
		assertEquals("FVC", codeMCS88016.getName());
		assertEquals("1.2.208.184.100.8", codeMCS88016.getCodingScheme());
		
		// Language Code 2.2.15
		assertNotNull(metadata.getLanguageCode());
		assertEquals("da-DK", metadata.getLanguageCode());
		
		// legal authenticator 2.2.16
		assertNotNull(metadata.getLegalAuthenticator());
		assertNotNull(metadata.getLegalAuthenticator().getFamilyName());
		assertNotNull(metadata.getLegalAuthenticator().getGivenName());
		assertNull(metadata.getLegalAuthenticator().getSecondAndFurtherGivenNames());
		assertEquals("Olsen", metadata.getLegalAuthenticator().getFamilyName());
		assertEquals("Lars", metadata.getLegalAuthenticator().getGivenName());

		// service start Time 2.2.24
		assertNotNull(metadata.getServiceStartTimeStringUTC());
		assertEquals("20130211081100", metadata.getServiceStartTimeStringUTC());
		
		// service stop Time 2.2.25
		assertNotNull(metadata.getServiceStopTimeStringUTC());
		assertEquals("20130216101400", metadata.getServiceStopTimeStringUTC());
		
		
		// Source Patient Id 2.2.28
		assertNotNull(metadata.getPatientId());
		assertEquals("2606481234", metadata.getPatientId().getCode());
		assertEquals("1.2.208.176.1.2", metadata.getPatientId().getCodingScheme());
		assertEquals("", metadata.getPatientId().getName());
		
		// Source Patient Info 2.2.29
		assertNotNull(metadata.getSourcePatientInfoDateOfBirth());
	    assertEquals(new GregorianCalendar(1948, 05, 26, 00, 00, 00).getTime(), metadata.getSourcePatientInfoDateOfBirth());
		assertNotNull(metadata.getSourcePatientInfoGender());
		assertEquals("M", metadata.getSourcePatientInfoGender());
		assertNotNull(metadata.getSourcePatientInfoPerson());
		assertNotNull(metadata.getSourcePatientInfoPerson().getFamilyName());
		assertEquals("Berggren", metadata.getSourcePatientInfoPerson().getFamilyName());
		assertNotNull(metadata.getSourcePatientInfoPerson().getGivenName());
		assertEquals("Janus", metadata.getSourcePatientInfoPerson().getGivenName());
		assertNull(metadata.getSourcePatientInfoPerson().getSecondAndFurtherGivenNames());
	
		// Title 2.2.31
		assertEquals("Hjemmemonitorering for 2606481234", metadata.getTitle());
		
		// Typecode 2.2.32
		assertNotNull(metadata.getTypeCode());
		assertEquals("53576-5", metadata.getTypeCode().getCode());
		assertEquals("2.16.840.1.113883.6.1", metadata.getTypeCode().getCodingScheme());
		assertEquals("Personal Health Monitoring Report", metadata.getTypeCode().getName());
		
		// uniqueid 2.2.33
		assertNotNull(metadata.getUniqueId());
		assertEquals("437f8b60-9563-11e3-a5e2-0800200c9a66^1.2.208.184", metadata.getUniqueId());
	}
	
	private DocumentMetadata createMetadata(){
		DocumentMetadata documentMetadata = new DocumentMetadata();
		documentMetadata.setClassCode(new Code());
		documentMetadata.setFormatCode(new Code());
		documentMetadata.setHealthcareFacilityTypeCode(new Code());
		documentMetadata.setMimeType("");
		documentMetadata.setSubmissionTime(new Date());
		return documentMetadata;
		
	}
}
