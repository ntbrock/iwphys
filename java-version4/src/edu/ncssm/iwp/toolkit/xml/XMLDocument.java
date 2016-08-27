package edu.ncssm.iwp.toolkit.xml;

import java.io.Writer;
import java.io.IOException;

public class XMLDocument 
{
	XMLElement root;


	public XMLDocument ( XMLElement root )
	{
		this.root = root;
	}


	public void toXML ( Writer out )
		throws IOException
	{
		out.write ( "<?xml version=\"1.0\"?>\n\n" );

		root.toXML ( out, 0 );
	}
}

