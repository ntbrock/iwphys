package edu.ncssm.iwp.exceptions;


public class UnknownFileException extends Exception
{
	public UnknownFileException ( ) { }
	public UnknownFileException ( String message )
	{
		super ( message );
	}

	public UnknownFileException ( Throwable t )
	{
		super ( t.getMessage() );
	}

}
