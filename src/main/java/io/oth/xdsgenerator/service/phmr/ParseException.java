package io.oth.xdsgenerator.service.phmr;

public class ParseException extends Exception {

	private static final long serialVersionUID = 1L;

	private String message;
	
	public ParseException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
