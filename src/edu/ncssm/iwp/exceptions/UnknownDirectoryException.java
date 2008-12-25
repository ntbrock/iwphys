package edu.ncssm.iwp.exceptions;


public class UnknownDirectoryException extends Exception
{
	public UnknownDirectoryException ( ) { }
	public UnknownDirectoryException ( String message )
	{
		super ( message );
	}

	public UnknownDirectoryException ( Throwable t )
	{
		super ( t.getMessage() );
	}

}
