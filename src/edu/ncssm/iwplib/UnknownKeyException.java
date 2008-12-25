/**
 * Standard Exception signature
 *
 * @author brockman
 */

package edu.ncssm.iwplib;

public class UnknownKeyException extends BaseException
{
	public UnknownKeyException ( ) { } 
	public UnknownKeyException ( String s ) { super ( s ); }
	public UnknownKeyException ( Throwable t ) { super ( t ); }
}

