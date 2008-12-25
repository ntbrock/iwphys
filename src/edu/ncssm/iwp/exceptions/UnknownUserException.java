package edu.ncssm.iwp.exceptions;


public class UnknownUserException extends Exception
{
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
