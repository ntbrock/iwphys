package edu.ncssm.iwp.exceptions;


public class UnknownVariableException extends Exception
{
	private static final long serialVersionUID = 1L;
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
