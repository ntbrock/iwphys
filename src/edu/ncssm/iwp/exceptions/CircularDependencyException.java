package edu.ncssm.iwp.exceptions;


public class CircularDependencyException extends Exception
{
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
