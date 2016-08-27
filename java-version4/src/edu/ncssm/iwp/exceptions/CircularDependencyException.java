package edu.ncssm.iwp.exceptions;


public class CircularDependencyException extends Exception
{
	private static final long serialVersionUID = 1L;
    public CircularDependencyException ( ) { }
    public CircularDependencyException ( String message )
    {
        super ( message );
    }

    public CircularDependencyException ( Throwable t )
    {
        super ( t.getMessage() );
    }

}
