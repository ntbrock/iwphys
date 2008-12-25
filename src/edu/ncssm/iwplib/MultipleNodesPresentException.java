/**
 * Standard Exception signature
 *
 * @author brockman
 */

package edu.ncssm.iwplib;

public class MultipleNodesPresentException extends UnknownKeyException
{
	public MultipleNodesPresentException ( ) { } 
	public MultipleNodesPresentException ( String s ) { super ( s ); }
	public MultipleNodesPresentException ( Throwable t ) { super ( t ); }
}

