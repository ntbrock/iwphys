package edu.ncssm.iwp.exceptions;


public class UnknownVariableException extends Exception
{
	public UnknownVariableException ( ) { }
	public UnknownVariableException ( String message )
	{
		super ( message );
	}

	public UnknownVariableException ( Throwable t )
	{
		super ( t.getMessage() );
	}

}
