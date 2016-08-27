package edu.ncssm.iwp.exceptions;

public class TimeIndexMismatchException extends  Exception
{
	private static final long serialVersionUID = 1L;
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
