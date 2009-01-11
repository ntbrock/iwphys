package edu.ncssm.iwp.exceptions;

/**
 * Thrown when object setName is called.
 * @author brockman
 *
 */

public class InvalidObjectNameX extends Exception
{
	private static final long serialVersionUID = 1L;
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
