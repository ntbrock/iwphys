package edu.ncssm.iwp.exceptions;


public class AuthenticationException extends Exception
{
	public AuthenticationException ( ) { }
	public AuthenticationException ( String message )
	{
		super ( message );
	}

	public AuthenticationException ( Throwable t )
	{
		super ( t.getMessage() );
	}

}
