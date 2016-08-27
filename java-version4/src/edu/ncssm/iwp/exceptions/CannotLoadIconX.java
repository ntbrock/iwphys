package edu.ncssm.iwp.exceptions;

public class CannotLoadIconX extends Exception {
	private static final long serialVersionUID = 1L;
	public CannotLoadIconX() {
		super();
	}

	public CannotLoadIconX(String message) {
		super(message);
	}

	public CannotLoadIconX(String message, Throwable cause) {
		super(message, cause);
	}

	public CannotLoadIconX(Throwable cause) {
		super(cause);
	}

}
