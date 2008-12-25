package edu.ncssm.iwp.exceptions;


public class UnknownConfigurationFieldException extends Exception
{
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
