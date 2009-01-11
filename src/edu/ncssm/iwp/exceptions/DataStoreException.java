package edu.ncssm.iwp.exceptions;


public class DataStoreException extends Exception
{
	private static final long serialVersionUID = 1L;
	public DataStoreException ( ) { }
	public DataStoreException ( String message )
	{
		super ( message );
	}

	public DataStoreException ( Throwable t )
	{
		super ( t );
	}

}
