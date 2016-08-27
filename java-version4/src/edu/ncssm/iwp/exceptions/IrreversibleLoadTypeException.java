package edu.ncssm.iwp.exceptions;


public class IrreversibleLoadTypeException extends Exception
{
	private static final long serialVersionUID = 1L;
	
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
