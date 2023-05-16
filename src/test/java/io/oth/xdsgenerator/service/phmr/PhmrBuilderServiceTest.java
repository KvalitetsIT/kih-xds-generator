package io.oth.xdsgenerator.service.phmr;



import io.oth.xdsgenerator.model.phmr.PersonalHealthMonitoringReportRequest;
import io.oth.xdsgenerator.model.phmr.PhmrAndMetadata;
import io.oth.xdsgenerator.service.TestConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestConfiguration.class })
public class PhmrBuilderServiceTest {

	@Autowired
	PhmrBuilderService phmrBuilderService;

	@Test
	public void testDataInputContextAsExpected() {

		// Given
		PersonalHealthMonitoringReportRequest personalHealthMonitoringReport = PersonalHealthMonitoringReportRequestHelper.createPersonalHealthMonitoringReportRequest();

		// When
		PhmrAndMetadata phmrAndMetadata = phmrBuilderService.buildPhmrClinicalDocument(personalHealthMonitoringReport);

		// Then
		assertNotNull(phmrAndMetadata);
		assertNotNull(phmrAndMetadata.getPhmr());
		assertNotNull(phmrAndMetadata.getDocumentMetadata());

		assertNotNull(phmrAndMetadata.getPhmr().getResults());
		assertEquals(1, phmrAndMetadata.getPhmr().getResults().size());
		assertNotNull(phmrAndMetadata.getPhmr().getResults().get(0).getDataInputContext());
		assertNotNull(phmrAndMetadata.getPhmr().getResults().get(0).getDataInputContext().getMeasurementPerformerCode());
		assertEquals("Citizen", phmrAndMetadata.getPhmr().getResults().get(0).getDataInputContext().getMeasurementPerformerCode().toString());
		assertEquals("TypedByCitizen", phmrAndMetadata.getPhmr().getResults().get(0).getDataInputContext().getMeasurementProvisionMethodCode().toString());

		assertEquals("id1234result", phmrAndMetadata.getPhmr().getResults().get(0).getId().getExtension());
		assertEquals("kg", phmrAndMetadata.getPhmr().getResults().get(0).getUnit());
		assertEquals("77", phmrAndMetadata.getPhmr().getResults().get(0).getValue());

		assertNotNull(phmrAndMetadata.getPhmr().getVitalSigns());
		assertEquals(1, phmrAndMetadata.getPhmr().getVitalSigns().size());
		assertNotNull(phmrAndMetadata.getPhmr().getVitalSigns().get(0).getDataInputContext());
		assertNotNull(phmrAndMetadata.getPhmr().getVitalSigns().get(0).getDataInputContext().getMeasurementPerformerCode());
		assertEquals("Citizen", phmrAndMetadata.getPhmr().getVitalSigns().get(0).getDataInputContext().getMeasurementPerformerCode().toString());
		assertEquals("TypedByCitizen", phmrAndMetadata.getPhmr().getVitalSigns().get(0).getDataInputContext().getMeasurementProvisionMethodCode().toString());

		assertEquals("id1234vital", phmrAndMetadata.getPhmr().getVitalSigns().get(0).getId().getExtension());
		assertEquals("kg", phmrAndMetadata.getPhmr().getVitalSigns().get(0).getUnit());
		assertEquals("76", phmrAndMetadata.getPhmr().getVitalSigns().get(0).getValue());

		System.out.println(phmrAndMetadata.getPhmr().getServiceStartTime());
		System.out.println(phmrAndMetadata.getPhmr().getServiceStopTime());
	}

	@Test
	public void testStartAndStopTimeFromResults() {

		// Given
		PersonalHealthMonitoringReportRequest personalHealthMonitoringReport = PersonalHealthMonitoringReportRequestHelper.createPersonalHealthMonitoringReportRequest();
	    Date date1 = Date.from(LocalDateTime.of(2020,02, 15, 17, 30, 15).atZone(ZoneId.systemDefault()).toInstant());
	    Date date2 = Date.from(LocalDateTime.of(2020,02, 16, 17, 30, 15).atZone(ZoneId.systemDefault()).toInstant());


		// When
		PhmrAndMetadata phmrAndMetadata = phmrBuilderService.buildPhmrClinicalDocument(personalHealthMonitoringReport);

		// Then
		assertNotNull(phmrAndMetadata);
		assertNotNull(phmrAndMetadata.getPhmr());
		assertNotNull(phmrAndMetadata.getDocumentMetadata());

		assertNotNull(phmrAndMetadata.getPhmr().getResults());

		assertNotNull(phmrAndMetadata.getPhmr().getServiceStartTime());
		assertNotNull(phmrAndMetadata.getPhmr().getServiceStopTime());
		assertTrue(phmrAndMetadata.getPhmr().getServiceStartTime().equals(date1));
		assertTrue(phmrAndMetadata.getPhmr().getServiceStopTime().equals(date2));

	}

	@Test
	public void testStartAndStopTimeWhenNoResultsIncluded() {

		// Given
		PersonalHealthMonitoringReportRequest personalHealthMonitoringReport = PersonalHealthMonitoringReportRequestHelper.createPersonalHealthMonitoringReportRequest();
		personalHealthMonitoringReport.setResults(null);
	    Date date = Date.from(LocalDateTime.of(2020,02, 15, 17, 30, 15).atZone(ZoneId.systemDefault()).toInstant());

		// When
		PhmrAndMetadata phmrAndMetadata = phmrBuilderService.buildPhmrClinicalDocument(personalHealthMonitoringReport);

		// Then
		assertNotNull(phmrAndMetadata);
		assertNotNull(phmrAndMetadata.getPhmr());
		assertNotNull(phmrAndMetadata.getDocumentMetadata());

		assertNotNull(phmrAndMetadata.getPhmr().getResults());

		assertNotNull(phmrAndMetadata.getPhmr().getServiceStartTime());
		assertNotNull(phmrAndMetadata.getPhmr().getServiceStopTime());
		assertFalse(phmrAndMetadata.getPhmr().getServiceStartTime().equals(date));
		assertFalse(phmrAndMetadata.getPhmr().getServiceStopTime().equals(date));

	}


	@Test
	public void testTextFieldIsHtmlAsExpected() {

		// Given
		PersonalHealthMonitoringReportRequest personalHealthMonitoringReport = PersonalHealthMonitoringReportRequestHelper.createPersonalHealthMonitoringReportRequest();

		String resultTextStart = "<paragraph styleCode=\"Bold\">weightdescriptionResult</paragraph><table width=\"100%\"><tbody><tr><th>Date/Time</th><th>Value</th><th>Measured by</th><th>Transferred by</th><th>Status</th><th>Comment</th></tr>";
		String resultTextLine = "<tr><td>2020-02-15 17:30:15</td><td>77 kg</td><td>Målt af borger</td><td>Indtastet af borger</td><td>Completed</td><td></td></tr>";
		String resultTextEnd = "</tbody></table>";

		String vitalSignsTextStart = "<paragraph styleCode=\"Bold\">weightdescriptionVitalSign</paragraph><table width=\"100%\"><tbody><tr><th>Date/Time</th><th>Value</th><th>Measured by</th><th>Transferred by</th><th>Status</th><th>Comment</th></tr>";
		String vitalSignsTextLine = "<tr><td>2020-02-16 17:30:15</td><td>76 kg</td><td>Målt af borger</td><td>Indtastet af borger</td><td>Completed</td><td></td></tr>";
		String vitalSignsTextEnd = "</tbody></table>";

		String htmlResultsText = resultTextStart.concat(resultTextLine).concat(resultTextEnd);
		String htmlVitalSignsText = vitalSignsTextStart.concat(vitalSignsTextLine).concat(vitalSignsTextEnd);

		// When
		PhmrAndMetadata phmrAndMetadata = phmrBuilderService.buildPhmrClinicalDocument(personalHealthMonitoringReport);

		// Then
		assertNotNull(phmrAndMetadata);
		assertNotNull(phmrAndMetadata.getPhmr());
		assertNotNull(phmrAndMetadata.getDocumentMetadata());

		assertNotNull(phmrAndMetadata.getPhmr().getResults());
		assertEquals(1, phmrAndMetadata.getPhmr().getResults().size());
		assertNotNull(phmrAndMetadata.getPhmr().getResultsText());
		assertNotNull(phmrAndMetadata.getPhmr().getVitalSignsText());

		assertEquals(htmlResultsText, phmrAndMetadata.getPhmr().getResultsText());
		assertEquals(htmlVitalSignsText, phmrAndMetadata.getPhmr().getVitalSignsText());

	}

	@Test
	public void testTextFieldIsHtmlAsExpectedWhenNoResultsAndVitalSignsIncluded() {

		// Given
		PersonalHealthMonitoringReportRequest personalHealthMonitoringReport = PersonalHealthMonitoringReportRequestHelper.createPersonalHealthMonitoringReportRequest();
		personalHealthMonitoringReport.setResults(null);
		personalHealthMonitoringReport.setVitalSigns(null);

		// When
		PhmrAndMetadata phmrAndMetadata = phmrBuilderService.buildPhmrClinicalDocument(personalHealthMonitoringReport);

		// Then
		assertNotNull(phmrAndMetadata);
		assertNotNull(phmrAndMetadata.getPhmr());
		assertNotNull(phmrAndMetadata.getDocumentMetadata());

		assertNotNull(phmrAndMetadata.getPhmr().getResults());
		assertEquals(0, phmrAndMetadata.getPhmr().getResults().size());
		assertNotNull(phmrAndMetadata.getPhmr().getVitalSigns());
		assertEquals(0, phmrAndMetadata.getPhmr().getVitalSigns().size());

		assertEquals("Results", phmrAndMetadata.getPhmr().getResultsText());
		assertEquals("Vital Signs", phmrAndMetadata.getPhmr().getVitalSignsText());

	}


}
