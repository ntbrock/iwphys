package edu.ncssm.iwp.exceptions;


public class InvalidCredentialsException extends Exception
{
	public InvalidCredentialsException ( ) { }
	public InvalidCredentialsException ( String message )
	{
		super ( message );
	}

	public InvalidCredentialsException ( Throwable t )
	{
		super ( t.getMessage() );
	}

}
