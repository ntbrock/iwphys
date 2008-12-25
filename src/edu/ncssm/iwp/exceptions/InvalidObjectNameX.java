package edu.ncssm.iwp.exceptions;

/**
 * Thrown when object setName is called.
 * @author brockman
 *
 */

public class InvalidObjectNameX extends Exception
{
	public InvalidObjectNameX ( ) { }
	public InvalidObjectNameX ( String message )
	{
		super ( message );
	}

	public InvalidObjectNameX ( Throwable t )
	{
		super ( t.getMessage() );
	}

}
