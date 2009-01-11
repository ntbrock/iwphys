package edu.ncssm.iwp.exceptions;


public class InvalidEquationException extends Exception
{
	private static final long serialVersionUID = 1L;
	
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
