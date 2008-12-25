package edu.ncssm.iwp.toolkit.xml;

public class XMLParser 
{

	XMLNode topNode;

	public XMLParser ( String data )
	{
		topNode = new XMLNode ( "DOCUMENT", data, 0 );
	}

}

