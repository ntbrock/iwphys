/**
 * Standard Exception signature
 *
 * @author brockman
 */

package edu.ncssm.iwplib;

public class MultipleNodesPresentException extends UnknownKeyException
{
	private static final long serialVersionUID = 1L;
	public MultipleNodesPresentException ( ) { } 
	public MultipleNodesPresentException ( String s ) { super ( s ); }
	public MultipleNodesPresentException ( Throwable t ) { super ( t ); }
}

