package edu.ncssm.iwp.exceptions;


public class UnknownDirectoryException extends Exception
{
	private static final long serialVersionUID = 1L;
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
