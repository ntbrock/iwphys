package edu.ncssm.iwp.exceptions;

public class UnknownTickException extends Exception
{
	private static final long serialVersionUID = 1L;
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
