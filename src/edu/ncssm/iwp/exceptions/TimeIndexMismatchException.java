package edu.ncssm.iwp.exceptions;

public class TimeIndexMismatchException extends  Exception
{
	public TimeIndexMismatchException ( ) { }
	public TimeIndexMismatchException ( String message )
	{
		super ( message );
	}

	public TimeIndexMismatchException ( Throwable t )
	{
		super ( t.getMessage() );
	}

}
