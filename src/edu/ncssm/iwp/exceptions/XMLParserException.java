package edu.ncssm.iwp.exceptions;


public class XMLParserException extends Exception
{
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
