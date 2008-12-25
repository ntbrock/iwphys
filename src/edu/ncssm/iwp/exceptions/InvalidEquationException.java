package edu.ncssm.iwp.exceptions;


public class InvalidEquationException extends Exception
{
	public InvalidEquationException ( ) { }
	public InvalidEquationException ( String message )
	{
		super ( message );
	}

	public InvalidEquationException ( Throwable t )
	{
		super ( t.getMessage() );
	}

}
