package edu.ncssm.iwp.exceptions;


public class UnknownAuthkeyException extends Exception
{
	private static final long serialVersionUID = 1L;
	public UnknownAuthkeyException ( ) { }
	public UnknownAuthkeyException ( String message )
	{
		super ( message );
	}

	public UnknownAuthkeyException ( Throwable t )
	{
		super ( t.getMessage() );
	}

}
