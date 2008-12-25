package edu.ncssm.iwp.exceptions;


public class NoPluginObjectX extends Exception
{
    public NoPluginObjectX ( ) { }
    public NoPluginObjectX ( String message )
    {
        super ( message );
    }

    public NoPluginObjectX ( Throwable t )
    {
        super ( t.getMessage() );
    }


    public NoPluginObjectX ( String message, Throwable t )
    {
        super ( message, t );
    }

}
