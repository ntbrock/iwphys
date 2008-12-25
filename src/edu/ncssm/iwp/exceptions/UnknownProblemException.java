package edu.ncssm.iwp.exceptions;


public class UnknownProblemException extends Exception
{
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
