/**
 * Standard Exception signature
 *
 * @author brockman
 */

package edu.ncssm.iwplib;

public class InvalidProblemException extends BaseException
{
	public InvalidProblemException ( String s ) { super ( s ); }
	public InvalidProblemException ( Throwable t ) { super ( t ); }
}

