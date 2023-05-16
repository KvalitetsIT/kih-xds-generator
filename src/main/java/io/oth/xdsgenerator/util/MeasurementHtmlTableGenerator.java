package io.oth.xdsgenerator.util;

import dk.s4.hl7.cda.codes.MedCom;
import dk.s4.hl7.cda.model.DataInputContext.PerformerType;
import dk.s4.hl7.cda.model.DataInputContext.ProvisionMethod;
import dk.s4.hl7.cda.model.phmr.Measurement;
import dk.s4.hl7.cda.model.phmr.Measurement.Status;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.Format;

public class MeasurementHtmlTableGenerator {
	
	private String listOfTables;

	private static String resultTextStart = "<paragraph styleCode=\"Bold\">¤description¤</paragraph><table width=\"100%\"><tbody><tr><th>Date/Time</th><th>Value</th><th>Measured by</th><th>Transferred by</th><th>Status</th><th>Comment</th></tr>"; 
	private static String resultTextLine = "<tr><td>¤date¤</td><td>¤meas¤</td><td>¤performer¤</td><td>¤provision¤</td><td>¤status¤</td><td></td></tr>";
	private static String resultTextEnd = "</tbody></table>";
	
	protected static final Format dateTimeformatter = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

	public MeasurementHtmlTableGenerator() {
		listOfTables = new String();

	}
	
	public void addLine(Measurement phmrMeasurement) {
		
		String tableStart = resultTextStart.replace("¤description¤", getDescription(phmrMeasurement));
		
		String tableLine = resultTextLine;
		tableLine = tableLine.replace("¤date¤", getDate(phmrMeasurement));
		tableLine = tableLine.replace("¤meas¤", getMeas(phmrMeasurement));
		tableLine = tableLine.replace("¤performer¤", getPerformer(phmrMeasurement));
		tableLine = tableLine.replace("¤provision¤", getProvision(phmrMeasurement));
		tableLine = tableLine.replace("¤status¤", getStatus(phmrMeasurement));
		//comment is not mapped
		
		listOfTables = listOfTables.concat(tableStart).concat(tableLine).concat(resultTextEnd);
		
	}

	public String getText() {
		return listOfTables;
	}

	public boolean hasText() {
		if (listOfTables != null && !listOfTables.isEmpty()) {
			return true;
		}
		return false;

	}
	
	private String getDate(Measurement phmrMeasurement) {
		if (phmrMeasurement.getTimestamp() != null ) {
			return dateTimeformatter.format(phmrMeasurement.getTimestamp());	

		} else {
			return "";
		}
	}
	
	private String getMeas(Measurement phmrMeasurement) {
		if (phmrMeasurement.getValue() != null || phmrMeasurement.getUnit() != null ) {
			return (phmrMeasurement.getValue() != null ? phmrMeasurement.getValue().concat(" ") : "").concat(phmrMeasurement.getUnit() != null ? phmrMeasurement.getUnit() : "");
		} else {
			return "";
		}
	}
	
	private String getPerformer(Measurement phmrMeasurement) {
		if (phmrMeasurement.getDataInputContext() != null && phmrMeasurement.getDataInputContext().getMeasurementPerformerCode() != null) {
			return getPerformerMethodDisplayName(phmrMeasurement.getDataInputContext().getMeasurementPerformerCode());
		} else {
			return "";
		}
	}
	
	private String getProvision(Measurement phmrMeasurement) {
		if (phmrMeasurement.getDataInputContext() != null && phmrMeasurement.getDataInputContext().getMeasurementProvisionMethodCode() != null) {
			return getProvisionMethodDisplayName(phmrMeasurement.getDataInputContext().getMeasurementProvisionMethodCode());
		} else {
			return "";
		}
	}
	
	private String getStatus(Measurement phmrMeasurement) {
		if (phmrMeasurement.getStatus() != null) {
			return getStatusDisplayName(phmrMeasurement.getStatus());
		} else {
			return "";
		}
	}
	
	private String getDescription(Measurement phmrMeasurement) {
		if (phmrMeasurement.getDisplayName() != null) {
			return phmrMeasurement.getDisplayName();
		} else {
			return "";
		}
	}
	
	private static String getPerformerMethodDisplayName(PerformerType performerType) {
		switch (performerType) {
		case Citizen:
		    return MedCom.PERFORMED_BY_CITIZEN_DISPLAYNAME;
		case HealthcareProfessional:
		    return MedCom.PERFORMED_BY_HEALTHCAREPROFESSIONAL_DISPLAYNAME;
		case CareGiver:
		    return MedCom.PERFORMED_BY_CAREGIVER_DISPLAYNAME;
		default:
		    throw new IllegalStateException("Invalid Context performer code" + performerType.toString());
		}
	}
	private static String getProvisionMethodDisplayName(ProvisionMethod provisionMethod) {
		switch (provisionMethod) {
		case Electronically:
			return MedCom.TRANSFERRED_ELECTRONICALLY_DISPLAYNAME;
		case TypedByCitizen:
		    return MedCom.TYPED_BY_CITIZEN_DISPLAYNAME;
		case TypedByCitizenRelative:
		    return MedCom.TYPED_BY_CITIZEN_RELATIVE_DISPLAYNAME;
		case TypedByHealthcareProfessional:
		    return MedCom.TYPED_BY_HEALTHCAREPROFESSIONAL_DISPLAYNAME;
		case TypedByCareGiver:
		    return MedCom.TYPED_BY_CAREGIVER_DISPLAYNAME;
		default:
		    throw new IllegalStateException("Invalid Context provision method code" + provisionMethod.toString());
		}
	}
	private static String getStatusDisplayName(Status status) {
	    
		switch (status) {
		case COMPLETED:
			return "Completed";
		case NULLIFIED:
		    return "Nullified";
		default:
		    throw new IllegalStateException("Invalid status ode" + status.toString());
		}
	}

}
