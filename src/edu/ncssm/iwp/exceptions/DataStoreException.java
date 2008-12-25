package edu.ncssm.iwp.exceptions;


public class DataStoreException extends Exception
{
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
