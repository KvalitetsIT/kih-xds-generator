package io.oth.xdsgenerator.model.kih;

import java.util.Date;

public class LegalAuthenticator {

	public String SignatureCode;
	
	public Date Time;
	
	public AssignedEntity AssignedEntity;

	public String getSignatureCode() {
		return SignatureCode;
	}

	public void setSignatureCode(String signatureCode) {
		SignatureCode = signatureCode;
	}

	public Date getTime() {
		return Time;
	}

	public void setTime(Date time) {
		Time = time;
	}

	public AssignedEntity getAssignedEntity() {
		return AssignedEntity;
	}

	public void setAssignedEntity(AssignedEntity assignedEntity) {
		AssignedEntity = assignedEntity;
	}
	
}
