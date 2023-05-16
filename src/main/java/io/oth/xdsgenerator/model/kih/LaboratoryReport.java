package io.oth.xdsgenerator.model.kih;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @class LaboratoryReport represents the LaboratoryReport object in KIH
 */
public class LaboratoryReport {

	/**
	 * Analysis text
	 */
	public String AnalysisText;

    public String HealthCareProfessionalComment;

    public Instrument Instrument;

    public String UuidIdentifier;

    //@JsonFormat(pattern="EEE MMM dd HH:mm:ss z yyyy")
    public Date CreatedDateTime;

    public String ResultText;

    public String ResultUnitText;

    public String ResultTypeOfInterval;

    public String NationalSampleIdentifier;

    public String IupacIdentifier;

    public Identifier ProducerOfLabResult;

    public String MeasuringCircumstances;
    public String MeasurementTransferredBy;
    public String MeasurementLocation;
    public String MeasurementScheduled;

	public String getAnalysisText() {
		return AnalysisText;
	}

	public void setAnalysisText(String analysisText) {
		AnalysisText = analysisText;
	}

	public String getHealthCareProfessionalComment() {
		return HealthCareProfessionalComment;
	}

	public void setHealthCareProfessionalComment(String healthCareProfessionalComment) {
		HealthCareProfessionalComment = healthCareProfessionalComment;
	}

	public Instrument getInstrument() {
		return Instrument;
	}

	public void setInstrument(Instrument instrument) {
		Instrument = instrument;
	}

	public String getUuidIdentifier() {
		return UuidIdentifier;
	}

	public void setUuidIdentifier(String uuidIdentifier) {
		UuidIdentifier = uuidIdentifier;
	}

	public Date getCreatedDateTime() {
		return CreatedDateTime;
	}

	public void setCreatedDateTime(Date createdDateTime) {
		this.CreatedDateTime = createdDateTime;
	}

	public String getResultText() {
		return ResultText;
	}

	public void setResultText(String resultText) {
		ResultText = resultText;
	}

	public String getResultUnitText() {
		return ResultUnitText;
	}

	public void setResultUnitText(String resultUnitText) {
		ResultUnitText = resultUnitText;
	}

	public String getResultTypeOfInterval() {
		return ResultTypeOfInterval;
	}

	public void setResultTypeOfInterval(String resultTypeOfInterval) {
		ResultTypeOfInterval = resultTypeOfInterval;
	}

	public String getNationalSampleIdentifier() {
		return NationalSampleIdentifier;
	}

	public void setNationalSampleIdentifier(String nationalSampleIdentifier) {
		NationalSampleIdentifier = nationalSampleIdentifier;
	}

	public String getIupacIdentifier() {
		return IupacIdentifier;
	}

	public void setIupacIdentifier(String iupacIdentifier) {
		IupacIdentifier = iupacIdentifier;
	}

	public Identifier getProducerOfLabResult() {
		return ProducerOfLabResult;
	}

	public void setProducerOfLabResult(Identifier producerOfLabResult) {
		ProducerOfLabResult = producerOfLabResult;
	}

	public String getMeasuringCircumstances() {
		return MeasuringCircumstances;
	}

	public void setMeasuringCircumstances(String measuringCircumstances) {
		MeasuringCircumstances = measuringCircumstances;
	}

	@Override
	public String toString() {
		return "LaboratoryReport [AnalysisText=" + AnalysisText + ", HealthCareProfessionalComment="
				+ HealthCareProfessionalComment + ", Instrument=" + Instrument + ", UuidIdentifier=" + UuidIdentifier
				+ ", createdDateTime=" + CreatedDateTime + ", ResultText=" + ResultText + ", ResultUnitText="
				+ ResultUnitText + ", ResultTypeOfInterval=" + ResultTypeOfInterval + ", NationalSampleIdentifier="
				+ NationalSampleIdentifier + ", IupacIdentifier=" + IupacIdentifier + ", ProducerOfLabResult ["
				+ "Identifier=" + ProducerOfLabResult.getIdentifier() + ", IdentifierCode=" + ProducerOfLabResult.getIdentifierCode()
				+ "]"
				+ ", MeasuringCircumstances=" + MeasuringCircumstances + "]";
	}

}
