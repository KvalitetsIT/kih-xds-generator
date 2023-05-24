package io.oth.xdsgenerator.service.phmr;

import dk.s4.hl7.cda.model.DataInputContext.PerformerType;
import dk.s4.hl7.cda.model.DataInputContext.ProvisionMethod;
import io.oth.xdsgenerator.model.phmr.ClinicalMeasurement;
import io.oth.xdsgenerator.model.phmr.Identity;
import io.oth.xdsgenerator.model.phmr.Organisation;
import io.oth.xdsgenerator.model.phmr.PersonalHealthMonitoringReportRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class PersonalHealthMonitoringReportRequestHelper {

	public static PersonalHealthMonitoringReportRequest createPersonalHealthMonitoringReportRequest() {

		Identity patient = new Identity();
		patient.setPatientIdentifier("121212ABCD");
		patient.setFamilyName("Andersen");

		Organisation organisation = new Organisation();


		PersonalHealthMonitoringReportRequest personalHealthMonitoringReport = new PersonalHealthMonitoringReportRequest();
		personalHealthMonitoringReport.setPatient(patient);

		ClinicalMeasurement[] clinicalMeasurementsResult = new ClinicalMeasurement[] { createResults("") };
		personalHealthMonitoringReport.setResults(clinicalMeasurementsResult);

		ClinicalMeasurement[] clinicalMeasurementsVitalSign = new ClinicalMeasurement[] { createVitalSigns("") };
		personalHealthMonitoringReport.setVitalSigns(clinicalMeasurementsVitalSign);

		String sorCode = "368061000016003";

		Organisation authorOrganisation = new Organisation();
		authorOrganisation.setCode(sorCode);
		authorOrganisation.setName("Aalborg Universitetshospital");
		personalHealthMonitoringReport.setAuthorOrganisation(authorOrganisation);

		return personalHealthMonitoringReport;
	}

	public static PersonalHealthMonitoringReportRequest createPersonalHealthMonitoringReportRequestTwoSections() {

		Identity patient = new Identity();
		patient.setPatientIdentifier("121212ABCD");
		patient.setFamilyName("Andersen");

		PersonalHealthMonitoringReportRequest personalHealthMonitoringReport = new PersonalHealthMonitoringReportRequest();
		personalHealthMonitoringReport.setPatient(patient);

		ClinicalMeasurement[] clinicalMeasurementsResult = new ClinicalMeasurement[] { createResults("1"), createResults("2") };
		personalHealthMonitoringReport.setResults(clinicalMeasurementsResult);

		ClinicalMeasurement[] clinicalMeasurementsVitalSign = new ClinicalMeasurement[] { createVitalSigns("1"), createVitalSigns("2") };
		personalHealthMonitoringReport.setVitalSigns(clinicalMeasurementsVitalSign);

		String sorCode = "368061000016003";

		Organisation authorOrganisation = new Organisation();
		authorOrganisation.setCode(sorCode);
		authorOrganisation.setName("Aalborg Universitetshospital");
		personalHealthMonitoringReport.setAuthorOrganisation(authorOrganisation);

		return personalHealthMonitoringReport;

	}

	private static ClinicalMeasurement createVitalSigns(String seq) {

	    LocalDateTime localDateTimeVitalSign = LocalDateTime.of(2020,02, 16, 17, 30, 15);
	    Date dateVitalSign = Date.from(localDateTimeVitalSign.atZone(ZoneId.systemDefault()).toInstant());

		ClinicalMeasurement weightVitalSign = new ClinicalMeasurement();
		weightVitalSign.setMeasuredAt(dateVitalSign);
		weightVitalSign.setResultText("76" + seq);
		weightVitalSign.setContextPerformerType(PerformerType.Citizen.name());
		weightVitalSign.setContextPerformerType(PerformerType.Citizen.name());
        weightVitalSign.setContextTransferedMethod(ProvisionMethod.TypedByCitizen.name());

		weightVitalSign.setResultUnitText("kg");
		weightVitalSign.setCodeDescription("weightdescriptionVitalSign" + seq);
		weightVitalSign.setId("id1234vital" + seq);

		return weightVitalSign;

	}

	private static ClinicalMeasurement createResults(String seq) {

		// result
	    LocalDateTime localDateTimeResult = LocalDateTime.of(2020,02, 15, 17, 30, 15);
	    Date dateResult = Date.from(localDateTimeResult.atZone(ZoneId.systemDefault()).toInstant());

		ClinicalMeasurement weightResult = new ClinicalMeasurement();

	    weightResult.setMeasuredAt(dateResult);
		weightResult.setResultText("77" + seq);
		weightResult.setContextPerformerType(PerformerType.Citizen.name());
        weightResult.setContextTransferedMethod(ProvisionMethod.TypedByCitizen.name());

		weightResult.setResultUnitText("kg");
		weightResult.setCodeDescription("weightdescriptionResult" + seq);
		weightResult.setId("id1234result" + seq);

		return weightResult;


	}

}
