package edu.ncssm.iwp.exceptions;


public class NotIWPDesignedX extends Exception
{
    public NotIWPDesignedX ( ) { }
    public NotIWPDesignedX ( String message )
    {
        super ( message );
    }

    public NotIWPDesignedX ( Throwable t )
    {
        super ( t.getMessage() );
    }


    public NotIWPDesignedX ( String message, Throwable t )
    {
        super ( message, t );
    }

}
