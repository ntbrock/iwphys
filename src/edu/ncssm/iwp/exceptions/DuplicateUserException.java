package edu.ncssm.iwp.exceptions;


public class DuplicateUserException extends Exception
{
	public DuplicateUserException ( ) { }
	public DuplicateUserException ( String message )
	{
		super ( message );
	}

	public DuplicateUserException ( Throwable t )
	{
		super ( t.getMessage() );
	}

}
