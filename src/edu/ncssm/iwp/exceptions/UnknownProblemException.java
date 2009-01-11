package edu.ncssm.iwp.exceptions;


public class UnknownProblemException extends Exception
{
	private static final long serialVersionUID = 1L;
	public UnknownProblemException ( ) { }
	public UnknownProblemException ( String message )
	{
		super ( message );
	}

	public UnknownProblemException ( Throwable t )
	{
		super ( t.getMessage() );
	}

}
