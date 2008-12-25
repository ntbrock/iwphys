package edu.ncssm.iwp.exceptions;


public class IrreversibleLoadTypeException extends Exception
{
	public IrreversibleLoadTypeException ( ) { }
	public IrreversibleLoadTypeException ( String message )
	{
		super ( message );
	}

	public IrreversibleLoadTypeException ( Throwable t )
	{
		super ( t.getMessage() );
	}

}
