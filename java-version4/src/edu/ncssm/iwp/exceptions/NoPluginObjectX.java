package edu.ncssm.iwp.exceptions;


public class NoPluginObjectX extends Exception
{
	private static final long serialVersionUID = 1L;
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
