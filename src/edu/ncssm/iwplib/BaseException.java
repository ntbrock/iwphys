package edu.ncssm.iwplib;


public class BaseException extends Exception
{
	private static final long serialVersionUID = 1L;
	public BaseException ( ) { } 

	public BaseException ( String s ) 
	{
		super ( s );
	}

	public BaseException ( Throwable t )
	{
		super ( t.getClass().getName()+ ": " + t.getMessage() );
	}
}
