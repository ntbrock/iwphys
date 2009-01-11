/**
 * Standard Exception signature
 *
 * @author brockman
 */

package edu.ncssm.iwplib;

public class InvalidProblemException extends BaseException
{
	private static final long serialVersionUID = 1L;
	
	public InvalidProblemException ( String s ) { super ( s ); }
	public InvalidProblemException ( Throwable t ) { super ( t ); }
}

