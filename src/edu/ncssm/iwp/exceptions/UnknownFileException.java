package edu.ncssm.iwp.exceptions;


public class UnknownFileException extends Exception
{
	private static final long serialVersionUID = 1L;
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
