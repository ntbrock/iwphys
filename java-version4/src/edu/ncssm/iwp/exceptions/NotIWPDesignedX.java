package edu.ncssm.iwp.exceptions;


public class NotIWPDesignedX extends Exception
{
	private static final long serialVersionUID = 1L;
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
