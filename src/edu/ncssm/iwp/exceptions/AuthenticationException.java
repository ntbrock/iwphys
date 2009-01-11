package edu.ncssm.iwp.exceptions;


public class AuthenticationException extends Exception
{
	private static final long serialVersionUID = 1L;
	
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
