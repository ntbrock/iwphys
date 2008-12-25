package edu.ncssm.iwp.exceptions;


public class InvalidPathException extends Exception
{
	public InvalidPathException ( ) { }
	public InvalidPathException ( String message )
	{
		super ( message );
	}

	public InvalidPathException ( Throwable t )
	{
		super ( t.getMessage() );
	}

}
