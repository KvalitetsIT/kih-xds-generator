package io.oth.xdsgenerator.model.phmr;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class ClinicalMeasurement {

	public String resultText;

	public String resultUnitText;

	public String code;

	public String codeDescription;

	public String contextPerformerType;
    public String contextTransferMethod;

	public String codeSystemOID;

	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssZ")
	public Date measuredAt;

	public String id;

    public String getContextTransferedMethod() {
        return contextTransferMethod;
    }

    public void setContextTransferedMethod(String transferMethod) {
        this.contextTransferMethod = transferMethod;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getMeasuredAt() {
		return measuredAt;
	}

	public void setMeasuredAt(Date measuredAt) {
		this.measuredAt = measuredAt;
	}

	public String getResultText() {
		return resultText;
	}

	public void setResultText(String resultText) {
		this.resultText = resultText;
	}

	public String getResultUnitText() {
		return resultUnitText;
	}

	public void setResultUnitText(String resultUnitText) {
		this.resultUnitText = resultUnitText;
	}

	public String getCodeDescription() {
		return codeDescription;
	}

	public void setCodeDescription(String codeDescription) {
		this.codeDescription = codeDescription;
	}

	public String getContextPerformerType() {
		return contextPerformerType;
	}

	public void setContextPerformerType(String contextPerformerType) {
		this.contextPerformerType = contextPerformerType;
	}

	public String getCodeSystemOID() {
		return codeSystemOID;
	}

	public void setCodeSystemOID(String codeSystemOID) {
		this.codeSystemOID = codeSystemOID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ClinicalMeasurement [resultText=" + resultText + ", resultUnitText=" + resultUnitText + ", code=" + code
				+ ", codeDescription=" + codeDescription + ", contextPerformerType=" + contextPerformerType
				+ ", codeSystemOID=" + codeSystemOID + ", measuredAt=" + measuredAt + ", id=" + id + "]";
	}

}
