package edu.ncssm.iwp.exceptions;


public class UnknownUserException extends Exception
{
	private static final long serialVersionUID = 1L;
	public UnknownUserException ( ) { }
	public UnknownUserException ( String message )
	{
		super ( message );
	}

	public UnknownUserException ( Throwable t )
	{
		super ( t.getMessage() );
	}

}
