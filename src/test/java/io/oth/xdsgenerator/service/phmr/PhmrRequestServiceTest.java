package io.oth.xdsgenerator.service.phmr;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class PhmrRequestServiceTest {

/*
	@Autowired
	PhmrRequestService phmrRequestService;
	
	@Test
	public void testWeightResult() throws FileNotFoundException {
		// Given
		PersonalHealthMonitoringReportRequest personalHealthMonitoringReport = PersonalHealthMonitoringReportRequestHelper.createPersonalHealthMonitoringReportRequest();
		String sorCode = "368061000016003";
		
		// When
		CdaDocumentResponse cdaDocumentResponse = phmrRequestService.createPhmr(personalHealthMonitoringReport);
		
		// Then
		assertNotNull(cdaDocumentResponse);
		assertNotNull(cdaDocumentResponse.getDocument());
		
		String base64Decoded = new String(Base64.decodeBase64(cdaDocumentResponse.getDocument()));
		
		assertTrue(base64Decoded.contains("</ClinicalDocument>"));
		assertTrue(base64Decoded.contains(sorCode));
		
	}
	
	@Test
	public void testTextResultExist() throws FileNotFoundException {
		// Given
		PersonalHealthMonitoringReportRequest personalHealthMonitoringReport = PersonalHealthMonitoringReportRequestHelper.createPersonalHealthMonitoringReportRequestTwoSections();

		String textStr = "<text>";
		String textEnd = "</text>";
		
		String resultTextStart1 = "<paragraph styleCode=\"Bold\">weightdescriptionResult1</paragraph><table width=\"100%\"><tbody><tr><th>Date/Time</th><th>Value</th><th>Measured by</th><th>Transferred by</th><th>Status</th><th>Comment</th></tr>"; 
		String resultTextStart2 = "<paragraph styleCode=\"Bold\">weightdescriptionResult2</paragraph><table width=\"100%\"><tbody><tr><th>Date/Time</th><th>Value</th><th>Measured by</th><th>Transferred by</th><th>Status</th><th>Comment</th></tr>";
		String resultTextLine1 = "<tr><td>2020-02-15 17:30:15</td><td>771 kg</td><td>Målt af borger</td><td>Indtastet af borger</td><td>Completed</td><td></td></tr>";
		String resultTextLine2 = "<tr><td>2020-02-15 17:30:15</td><td>772 kg</td><td>Målt af borger</td><td>Indtastet af borger</td><td>Completed</td><td></td></tr>";
		String resultTextEnd = "</tbody></table>";
		
		String vitalSignsTextStart1 = "<paragraph styleCode=\"Bold\">weightdescriptionVitalSign1</paragraph><table width=\"100%\"><tbody><tr><th>Date/Time</th><th>Value</th><th>Measured by</th><th>Transferred by</th><th>Status</th><th>Comment</th></tr>"; 
		String vitalSignsTextStart2 = "<paragraph styleCode=\"Bold\">weightdescriptionVitalSign2</paragraph><table width=\"100%\"><tbody><tr><th>Date/Time</th><th>Value</th><th>Measured by</th><th>Transferred by</th><th>Status</th><th>Comment</th></tr>";
		String vitalSignsTextLine1 = "<tr><td>2020-02-16 17:30:15</td><td>761 kg</td><td>Målt af borger</td><td>Indtastet af borger</td><td>Completed</td><td></td></tr>";
		String vitalSignsTextLine2 = "<tr><td>2020-02-16 17:30:15</td><td>762 kg</td><td>Målt af borger</td><td>Indtastet af borger</td><td>Completed</td><td></td></tr>";
		String vitalSignsTextEnd = "</tbody></table>";

		String htmlResultsText1 = resultTextStart1.concat(resultTextLine1).concat(resultTextEnd);
		String htmlResultsText2 = resultTextStart2.concat(resultTextLine2).concat(resultTextEnd);
		htmlResultsText1 = textStr.concat(htmlResultsText1).concat(htmlResultsText2).concat(textEnd); //two tables

		String htmlVitalSignsText1 = vitalSignsTextStart1.concat(vitalSignsTextLine1).concat(vitalSignsTextEnd);
		String htmlVitalSignsText2 = vitalSignsTextStart2.concat(vitalSignsTextLine2).concat(vitalSignsTextEnd);
		htmlVitalSignsText1 = textStr.concat(htmlVitalSignsText1).concat(htmlVitalSignsText2).concat(textEnd); //two tables 

		// When
		CdaDocumentResponse cdaDocumentResponse = phmrRequestService.createPhmr(personalHealthMonitoringReport);
		
		// Then
		assertNotNull(cdaDocumentResponse);
		assertNotNull(cdaDocumentResponse.getDocument());
		
		String base64Decoded = new String(Base64.decodeBase64(cdaDocumentResponse.getDocument()));
		System.out.println(base64Decoded);
		System.out.println(htmlResultsText1);
		System.out.println(htmlVitalSignsText1);
		assertTrue(base64Decoded.contains("</ClinicalDocument>"));
		assertTrue(base64Decoded.contains(htmlResultsText1));
		assertTrue(base64Decoded.contains(htmlVitalSignsText1));
		
	}
*/

}
