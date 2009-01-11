package edu.ncssm.iwp.exceptions;


public class UnknownConfigurationFieldException extends Exception
{
	private static final long serialVersionUID = 1L;
	public UnknownConfigurationFieldException ( ) { }
	public UnknownConfigurationFieldException ( String message )
	{
		super ( message );
	}

	public UnknownConfigurationFieldException ( Throwable t )
	{
		super ( t.getMessage() );
	}

}
