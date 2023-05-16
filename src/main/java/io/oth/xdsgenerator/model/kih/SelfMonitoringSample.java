package io.oth.xdsgenerator.model.kih;

import java.util.Arrays;

public class SelfMonitoringSample {
	
	public LaboratoryReport LaboratoryReports[];
	
	public String CreatedByText;

	public LaboratoryReport[] getLaboratoryReports() {
		return LaboratoryReports;
	}

	public void setLaboratoryReports(LaboratoryReport[] laboratoryReports) {
		LaboratoryReports = laboratoryReports;
	}

	public String getCreatedByText() {
		return CreatedByText;
	}

	public void setCreatedByText(String createdByText) {
		CreatedByText = createdByText;
	}

	@Override
	public String toString() {
		return "SelfMonitoringSample [LaboratoryReports=" + Arrays.toString(LaboratoryReports) + ", CreatedByText="
				+ CreatedByText + "]";
	}

	

}
