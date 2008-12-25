package edu.ncssm.iwp.exceptions;


public class UnknownAuthkeyException extends Exception
{
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
