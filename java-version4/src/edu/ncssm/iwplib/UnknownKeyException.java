/**
 * Standard Exception signature
 *
 * @author brockman
 */

package edu.ncssm.iwplib;

public class UnknownKeyException extends BaseException
{
	private static final long serialVersionUID = 1L;
	public UnknownKeyException ( ) { } 
	public UnknownKeyException ( String s ) { super ( s ); }
	public UnknownKeyException ( Throwable t ) { super ( t ); }
}

