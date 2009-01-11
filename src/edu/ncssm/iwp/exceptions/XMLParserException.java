package edu.ncssm.iwp.exceptions;


public class XMLParserException extends Exception
{
	private static final long serialVersionUID = 1L;
	public XMLParserException ( ) { }
	public XMLParserException ( String message )
	{
		super ( message );
	}

	public XMLParserException ( Throwable t )
	{
		super ( t.getMessage() );
	}

}
