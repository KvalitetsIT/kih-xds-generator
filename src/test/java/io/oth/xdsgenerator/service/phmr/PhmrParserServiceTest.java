package io.oth.xdsgenerator.service.phmr;


import org.springframework.beans.factory.annotation.Autowired;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


public class PhmrParserServiceTest {
//
//	@Autowired
//	PhmrParserService phmrParserService;
//
//	@Test
//	public void testAlexandraOT3Ex1() throws ParseException, IOException {
//
//		String content = new String(Files.readAllBytes(Paths.get("src/test/resources/PHMR_Alexandra_OT3_ex1.xml")),StandardCharsets.UTF_8);
//		PHMRDocument doc = phmrParserService.parse(content);
//
////		assertEquals("021ba3bd-6935-45ca-bdcd-ed77f8b2ee2a", doc.getDocumentId());
////		assertEquals(1, doc.getMedicalEquipment().length);
////		assertEquals(0, doc.getResults().length);
////		assertEquals(3, doc.getVitalSigns().length);
/////		assertEquals("2512489996", doc.getPatient().getCpr());
//	}
//
//
//	@Test
//	public void testParseKOLExample1() throws ParseException, IOException {
//
//		String content = new String(Files.readAllBytes(Paths.get("src/test/resources/PHMR_KOL_Example_1_MaTIS.xml")),StandardCharsets.UTF_8);
//		PHMRDocument doc = phmrParserService.parse(content);
//
//		assertEquals("021ba3bd-6935-45ca-bdcd-ed77f8b2ee2a", doc.getDocumentId());
//		assertEquals(1, doc.getMedicalEquipment().length);
//		assertEquals(0, doc.getResults().length);
//		assertEquals(3, doc.getVitalSigns().length);
//		assertEquals("a7ef9d75-866e-4dc3-b0bd-10126ea7b323", doc.getVitalSigns()[0].getId());
//		assertEquals("d6d62fef-4fa2-4ac4-a497-462ecbfc42bb", doc.getVitalSigns()[1].getId());
//		assertEquals("6a8bcf3c-1f56-43a7-bc9e-9c652158ec7c", doc.getVitalSigns()[2].getId());
//		assertEquals("2512489996", doc.getPatient().getCpr());
//
//	}
//
//	@Test
//	public void testParseKOLExample2() throws ParseException, IOException {
//
//		String content = new String(Files.readAllBytes(Paths.get("src/test/resources/PHMR_KOL_Example_2_MaTIS.xml")),StandardCharsets.UTF_8);
//		PHMRDocument doc = phmrParserService.parse(content);
//
//		assertEquals("f6a33353-10b0-4d1b-b4ab-208cc2a72e4e", doc.getDocumentId());
//		assertEquals(2, doc.getMedicalEquipment().length);
//		assertEquals(0, doc.getResults().length);
//		assertEquals(4, doc.getVitalSigns().length);
//		assertEquals("2002402834", doc.getPatient().getCpr());
//	}
//
//	@Test
//	public void testParseKOLExample3() throws ParseException, IOException {
//
//		String content = new String(Files.readAllBytes(Paths.get("src/test/resources/PHMR_KOL_Example_3_MaTIS.xml")),StandardCharsets.UTF_8);
//		PHMRDocument doc = phmrParserService.parse(content);
//
//		assertEquals("1b45db4e-81d0-4ebc-a557-d247c73b62b1", doc.getDocumentId());
//		assertEquals(2, doc.getMedicalEquipment().length);
//		assertEquals(0, doc.getResults().length);
//		assertEquals(4, doc.getVitalSigns().length);
//		assertEquals("2002562939", doc.getPatient().getCpr());
//	}
//
//	@Test
//	public void testParseKOLExample4() throws ParseException, IOException {
//
//		String content = new String(Files.readAllBytes(Paths.get("src/test/resources/PHMR_KOL_Example_4_MaTIS.xml")),StandardCharsets.UTF_8);
//		PHMRDocument doc = phmrParserService.parse(content);
//
//		assertEquals("55e0a52a-1a20-4c36-8a02-4b49c07a6964", doc.getDocumentId());
//		assertEquals(2, doc.getMedicalEquipment().length);
//		assertEquals(0, doc.getResults().length);
//		assertEquals(4, doc.getVitalSigns().length);
//		assertEquals("2002562939", doc.getPatient().getCpr());
//	}
//
//	@Test
//	public void testParseInputFromKIH() throws ParseException, IOException {
//
//		// Given
//		String content = new String(Files.readAllBytes(Paths.get("src/test/resources/PHMR_Input_from_KIH.xml")),StandardCharsets.UTF_8);
//
//		// When
//		PHMRDocument doc = phmrParserService.parse(content);
//
//		// Then
//		assertEquals("cb2b2700-e1a2-48f0-b7fc-5252bb738e14", doc.getDocumentId());
//		assertEquals("0000000125", doc.getPatient().getCpr());
//	}
//
//	public void testCanParseNancyExample() throws ParseException, IOException {
//
//		String content = new String(Files.readAllBytes(Paths.get("src/test/resources/PHMR_Nancy.xml")),StandardCharsets.UTF_8);
//		PHMRDocument doc = phmrParserService.parse(content);
//	}
//
//	@Test
//	public void testParseKIHTestJuli2020() throws ParseException, IOException {
//
////		Output from kihDataToCdaService
////		SelfMonitoringCollection: SelfMonitoringCollection
////		[Citizen=Citizen
////		[PersonCivilRegistrationIdentifier=2512484916, AddressPostal=null, EmailAddress=null, phoneNumberSubscriber=null,PersonNameStructure=null],
////		Author=null, LegalAuthenticator=null, Custodian=null,
////		SelfMonitoringSamples=[SelfMonitoringSamples [SelfMonitoringSample=SelfMonitoringSample [LaboratoryReports=[LaboratoryReport
////		[AnalysisText=Pt—Legeme;                                 masse = ? kg,
////		HealthCareProfessionalComment=null, Instrument=null, UuidIdentifier=0d81c915-cd07-42c3-a97c-ca8bec4e7e49, createdDateTime=Fri Jul 10 13:02:32 GMT 2020,
////		ResultText=84.9, ResultUnitText=kg, ResultTypeOfInterval=unspecified, NationalSampleIdentifier=9999999999,
////		IupacIdentifier=NPU03804, ProducerOfLabResult [Identifier=Patient målt, IdentifierCode=POT], MeasuringCircumstances=null]],
////		CreatedByText=OTH Test]]]]
//
//
//		String content = new String(Files.readAllBytes(Paths.get("src/test/resources/PHMR_KIH_Test_Juli_2020.xml")),StandardCharsets.UTF_8);
//		PHMRDocument doc = phmrParserService.parse(content);
//
//		assertEquals(1, doc.getResults().length);
//
//		//Below is 'prepared' using placeholders for future test on all fields
//
////		Citizen - PersonCivilRegistrationIdentifier
//		assertEquals("2512484916", doc.getPatient().getCpr());
//
////		Author
////		LegalAuthenticator
////		Custodian
//
////		SelfMonitoringSample - LaboratoryReport - AnalysisText
//		assertEquals("Pt—Legeme;                                 masse = ? kg", doc.getResults()[0].getDisplayName());
//
////		SelfMonitoringSample - LaboratoryReport - HealthCareProfessionalComment
////		SelfMonitoringSample - LaboratoryReport - Instrument
//
////		SelfMonitoringSample - LaboratoryReport - UuidIdentifier
//		assertEquals("99b43369-537a-4cfc-8ac6-d862ad69c047", doc.getResults()[0].getId());
//
////		SelfMonitoringSample - LaboratoryReport - createdDateTime; ("Fri Jul 10 15:02:32 CEST 2020")
////		assertEquals("10 Jul 2020 13:02:32 GMT", doc.getResults()[0].getTimeStamp().toGMTString());
//
////		SelfMonitoringSample - LaboratoryReport - ResultText;
//		assertEquals("84.9", doc.getResults()[0].getValue());
//
////		SelfMonitoringSample - LaboratoryReport - ResultUnitText;
//		assertEquals("kg", doc.getResults()[0].getUnit());
//
////		SelfMonitoringSample - LaboratoryReport - ResultTypeOfInterval;
////		assertEquals("unspecified", doc.getResults()[0].?);
//
////		SelfMonitoringSample - LaboratoryReport - NationalSampleIdentifier;
////		assertEquals("9999999999", doc.getResults()[0].?);
//
////		SelfMonitoringSample - LaboratoryReport - IupacIdentifier;
//		assertEquals("NPU03804", doc.getResults()[0].getCode());
//
////		SelfMonitoringSample - LaboratoryReport - ProducerOfLabResult - Identifier;
////		assertEquals("Patient m\\u00e5lt", doc.getResults()[0].?);
//
////		SelfMonitoringSample - LaboratoryReport - ProducerOfLabResult - IdentifierCode;
////		assertEquals("POT", doc.getResults()[0].?);
//
////		SelfMonitoringSample - LaboratoryReport - MeasuringCircumstances
//
//		//TODO hvad er dette, ser ikke ud til at blive brugt til noget i koden?
////        "MeasurementTransferredBy": "automatic",
////        "MeasurementLocation": "home",
////        "MeasurementScheduled": "scheduled"
//
////		SelfMonitoringSample - CreatedByText
//		assertEquals("Org: 6071000016008  / OTH Test Adr: Address: N/A N/A Street: [N/A,] N/A (Use: WorkPlace) Telecom: [] ", doc.getCreatedBy()); //orignal value is "OTH Test"
//
//	}
//
//	

}
