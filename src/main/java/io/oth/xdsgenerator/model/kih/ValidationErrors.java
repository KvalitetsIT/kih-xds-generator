package io.oth.xdsgenerator.model.kih;

import java.util.List;

public class ValidationErrors {

	private String message;
	
	private List<ValidationError> errors;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<ValidationError> getErrors() {
		return errors;
	}

	public void setErrors(List<ValidationError> errors) {
		this.errors = errors;
	}
}