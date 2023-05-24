package io.oth.xdsgenerator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.oth.xdsgenerator.model.kih.*;
import io.oth.xdsgenerator.model.phmr.Address;
import io.oth.xdsgenerator.model.phmr.ClinicalMeasurement;
import io.oth.xdsgenerator.model.phmr.PersonalHealthMonitoringReportRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class KihToPhmrMappingServiceTest {
    private final Logger log = LoggerFactory.getLogger(KihToPhmrMappingServiceTest.class);
    private final String testFile = "2020-09-25T17:13:56.571445045+02:00.json";

    private String testContent = null;
    private final ObjectMapper mapper = new ObjectMapper();
    private SelfMonitoringCollectionList input = null;
    String defaultCode = "325421000016001";
    String defaultName = "Medcom";

    @BeforeEach
    void setUp() {
        try {
            testContent = new String(getClass().getClassLoader().getResourceAsStream(testFile).readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            input = mapper.readValue(testContent, SelfMonitoringCollectionList.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // subject = new KihToPhmrMappingService(defaultName + "=" + defaultCode,
        //         "TeleCare Nord=6071000016008;OpenTele Region Hovedstaden=6111000016004;OpenTele Region Midt=6081000016005");
        subject = new KihToPhmrMappingService(defaultCode,defaultName);
    }

    private SelfMonitoringCollectionList readTestData(String file) throws IOException, JsonProcessingException {
        String content = new String(getClass().getClassLoader().getResourceAsStream(file).readAllBytes());
        log.info("Read " + content);
        SelfMonitoringCollectionList retVal = mapper.readValue(content, SelfMonitoringCollectionList.class);
        return retVal;
    }

    @Test
    void map() {
        assertTrue(false);
        log.info("Starting with " + input);

        // KihToPhmrMappingService service = new KihToPhmrMappingService(defaultName + "=" + defaultCode,
        //         "TeleCare Nord=6071000016008;OpenTele TeleCard Nord=6071000016008;Region Nord CTG=6071000016008;OpenTele Region Hovedstaden=6111000016004;OpenTele Region Midt=6081000016005;Helbredsprofilen=434171000016000");
        KihToPhmrMappingService service = new KihToPhmrMappingService();
        service.createDefaultOrganisation(defaultCode, defaultName);

        for (SelfMonitoringCollection col : input.getSelfMonitoringCollection()) {
            log.warn("Using " + col);
            log.warn("Created by " + col.SelfMonitoringSamples[0].SelfMonitoringSample.CreatedByText);
            PersonalHealthMonitoringReportRequest output = service.map(col);
            log.warn("Processed: " + output);

            assertEquals(defaultName, output.authenticatorOrganisation.name);
            assertEquals(defaultCode, output.authenticatorOrganisation.code);
        }

    }

    KihToPhmrMappingService subject;

    @Test
    public void testThatMissingCitizenCannotBeMapped() {
        // Given
        SelfMonitoringCollection collection = createCompleteRequest("", null);
        collection.setCitizen(null);

        // When
        IllegalArgumentException thrown = null;
        try {
            subject.map(collection);
        } catch (IllegalArgumentException e) {
            thrown = e;
        }

        // Then
        assertNotNull(thrown);
        assertTrue(thrown.getMessage().contains("Citizen skal være sat"));
    }

    @Test
    public void testThatAuthorWithUnknownCreatedWillBeMappedToDefaultSor() {
        // Given
        SelfMonitoringCollection collection = createCompleteRequest("Unknown", null);

        // When
        PersonalHealthMonitoringReportRequest request = subject.map(collection);

        log.info("Author is "+request.getAuthorOrganisation());
        log.info("Custodian is "+request.getCustodian());
        log.info("LegalAuthenticator is "+request.getAuthenticator());
        // Then
        assertTrue(request.getAuthorOrganisation().getName().equals(defaultName));
        assertTrue(request.getAuthorOrganisation().getCode().equals(defaultCode));
        assertTrue(request.getCustodianOrganisation().getName().equals(defaultName));
        assertTrue(request.getCustodianOrganisation().getCode().equals(defaultCode));
        assertTrue(request.getAuthenticatorOrganisation().getName().equals(defaultName));
        assertTrue(request.getAuthenticatorOrganisation().getCode().equals(defaultCode));
    }

    @Test
    public void testThatAuthorWithKnownCreatedWillBeMapped() {
        // Given
        SelfMonitoringCollection collection = createCompleteRequest("TeleCare Nord", null);
        collection.getAuthor().getAssignedAuthor().setId(null);

        // When
        PersonalHealthMonitoringReportRequest request = subject.map(collection);

        // Then
        assertTrue(request.getAuthorOrganisation().getName().equals(defaultName));
        assertTrue(request.getAuthorOrganisation().getCode().equals(defaultCode));
    }

    @Test
    public void testThatVitalSignWillBeMapped() {
        // Given
        SelfMonitoringCollection collection = createCompleteRequest("TeleCare Nord", "NPU03011");
        collection.getAuthor().getAssignedAuthor().setId(null);

        // When
        PersonalHealthMonitoringReportRequest request = subject.map(collection);

        // Then
        assertTrue(request.getResults().length == 0);
        assertTrue(request.getVitalSigns().length == 1);
        assertTrue(request.getVitalSigns()[0].getCode() == "NPU03011");
        assertTrue(request.getVitalSigns()[0].getId().equals("id123"));

    }

    @Test
    public void testThatResultWillBeMapped() {
        // Given
        SelfMonitoringCollection collection = createCompleteRequest("TeleCare Nord", "NPUxxxxx");
        collection.getAuthor().getAssignedAuthor().setId(null);

        // When
        PersonalHealthMonitoringReportRequest request = subject.map(collection);

        // Then
        assertTrue(request.getResults().length == 1);
        assertTrue(request.getResults()[0].getCode() == "NPUxxxxx");
        assertTrue(request.getResults()[0].getId().equals("id123"));
        assertTrue(request.getVitalSigns().length == 0);

    }

    @Test
    public void testThatAuthorWithUnknownCreatedWillBeMappedByUsingDefault() {
        // Given
        SelfMonitoringCollection collection = createCompleteRequest("xxxxxx", null);
        collection.getAuthor().getAssignedAuthor().setId(null);

        // When
        PersonalHealthMonitoringReportRequest request = subject.map(collection);

        // Then
        assertTrue(request.getAuthorOrganisation().getName().equals(subject.getDefaultSorCode().getName()));
        assertTrue(request.getAuthorOrganisation().getCode().equals(subject.getDefaultSorCode().getCode()));
    }

    public void testThatAuthorWithUnknownCreatedWillBeReturnErrorIfDefaultIsNotSet() {
        // Given
        // subject.setDefaultSor(null);
        SelfMonitoringCollection collection = createCompleteRequest("xxxxxx", null);
        collection.getAuthor().getAssignedAuthor().setId(null);

        // When
        PersonalHealthMonitoringReportRequest request = null;
        Exception thrown = null;
        try {
            request = subject.map(collection);
        } catch (IllegalArgumentException e) {
            thrown = e;
        }

        // Then
        assertNotNull(thrown);
        assertTrue(thrown.getMessage()
                .contains("Createdby skal være sat, da der kræves en AuthorOrganisation i PHMR dokumentet"));
    }

    @Test
    public void mapCollectionWithAuthorWithNoContactDetailsMustBePossible() {
        // Given
        SelfMonitoringCollection collection = createCompleteRequest("", null);
        collection.getAuthor().getAssignedAuthor().setEmailAddress(null);
        collection.getAuthor().getAssignedAuthor().setPhoneNumberSubscriber(null);

        // When
        PersonalHealthMonitoringReportRequest request = subject.map(collection);

        // Then
        assertNotNull(request);
    }

    @Test
    public void testMapAddresspostal() {
        // Given
        log.info("Testing address mapping");
        SelfMonitoringCollectionList input = null;

        try {
            input = readTestData("address-test.json");
        } catch (Exception e) {
            fail(e);
        }
        log.info("Received " + input);

        log.info("Citizen " + input.getSelfMonitoringCollection()[0].getCitizen().PersonCivilRegistrationIdentifier);
        log.info("Address " + input.getSelfMonitoringCollection()[0].getCitizen().AddressPostal);
        String use = "HomeAddress";
        AddressPostal ap = new AddressPostal();
        ap.StreetName = "AdresseM33 33";

        KihToPhmrMappingService service = new KihToPhmrMappingService();
        Address a = service.mapAddressPostal(input.SelfMonitoringCollection[0].Citizen.AddressPostal, use);

        assertNotNull(a);
        assertEquals(ap.StreetName, a.getAddressLines()[0]);

    }

    @Test
    public void testMapTransferedByAndPerformer() {
        ClinicalMeasurement measurement = new ClinicalMeasurement();
        LaboratoryReport report = new LaboratoryReport();
        KihToPhmrMappingService service = new KihToPhmrMappingService();

        // Patient, manual measurement
        report.MeasurementTransferredBy = "typed";
        service.mapTransferedByAndPerformer(report, measurement);

        assertEquals("TypedByCitizen", measurement.getContextTransferedMethod());
        assertEquals("Citizen", measurement.getContextPerformerType());

        // Clinician, manual measurement
        report.MeasurementTransferredBy = "typedbyhcprof";
        service.mapTransferedByAndPerformer(report, measurement);

        assertEquals("TypedByHealthcareProfessional", measurement.getContextTransferedMethod());
        assertEquals("HealthcareProfessional", measurement.getContextPerformerType());

        // Automatic measurement
        report.MeasurementTransferredBy = "automatic";
        service.mapTransferedByAndPerformer(report, measurement);

        assertEquals("Electronically", measurement.getContextTransferedMethod());
        assertEquals("Citizen", measurement.getContextPerformerType());

        // Default
        report.MeasurementTransferredBy = "";
        service.mapTransferedByAndPerformer(report, measurement);

        assertEquals("Electronically", measurement.getContextTransferedMethod());
        assertEquals("Citizen", measurement.getContextPerformerType());
    }

    private SelfMonitoringCollection createCompleteRequest(String createdBy, String iupacIdentifier) {
        SelfMonitoringCollection collection = new SelfMonitoringCollection();
        Citizen citizen = new Citizen();
        citizen.setPersonCivilRegistrationIdentifier("2512484916");
        collection.setCitizen(citizen);
        Author author = new Author();
        Organization representedOrganization = new Organization();
        representedOrganization.setName("TestOrg");
        AssignedEntity assignedAuthor = new AssignedEntity();
        Identifier sorId = new Identifier();
        sorId.setIdentifier("12345");
        sorId.setIdentifierCode("SOR");
        assignedAuthor.setId(new Identifier[] { sorId });
        PersonNameStructure assignedPerson = new PersonNameStructure();
        assignedPerson.setPersonGivenName("Hans");
        assignedPerson.setPersonSurName("Andersen");
        assignedAuthor.setAssignedPerson(assignedPerson);
        author.setAssignedAuthor(assignedAuthor);
        author.setRepresentedOrganization(representedOrganization);
        collection.setAuthor(author);

        LaboratoryReport[] laboratoryReports = new LaboratoryReport[1];
        laboratoryReports[0] = new LaboratoryReport();
        laboratoryReports[0].setUuidIdentifier("id123");
        laboratoryReports[0].setResultText("ResultText");
        laboratoryReports[0].setProducerOfLabResult(new Identifier());
        laboratoryReports[0].setIupacIdentifier(iupacIdentifier);
        laboratoryReports[0].setCreatedDateTime(new Date());

        SelfMonitoringSamples samples = new SelfMonitoringSamples();
        SelfMonitoringSample sample = new SelfMonitoringSample();

        sample.setCreatedByText(createdBy);
        sample.setLaboratoryReports(laboratoryReports);
        samples.setSelfMonitoringSample(sample);
        SelfMonitoringSamples samplesArray[] = new SelfMonitoringSamples[1];
        samplesArray[0] = samples;
        collection.setSelfMonitoringSamples(samplesArray);

        return collection;
    }
}
