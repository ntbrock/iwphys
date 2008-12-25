package edu.ncssm.iwp.exceptions;

public class UnknownTickException extends Exception {

	public UnknownTickException() {
		super();
	}

	public UnknownTickException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public UnknownTickException(String message) {
		super(message);
	
	}

	public UnknownTickException(Throwable cause) {
		super(cause);
	
	}

}
