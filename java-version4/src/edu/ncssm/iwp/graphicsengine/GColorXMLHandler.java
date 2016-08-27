package edu.ncssm.iwp.graphicsengine;

import org.xml.sax.*;
import javax.xml.parsers.*;
import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.util.*;


public class GColorXMLHandler extends IWPDefaultXmlHandler
{

	ContentHandler parent;
	SAXParser parser;
	GColor color;

	/*--------------------------------------------------------------------*/

	public void collectObject ( SAXParser parser, ContentHandler parent,
							    GColor color )
		throws SAXException
	{

		this.parent = parent;
		this.parser = parser;
		this.color = color;

		parser.getXMLReader().setContentHandler( this );
		
	}

	public void cleanup ( )
	{
		/* create the type */
		

	}

	/*--------------------------------------------------------------------*/

	public void startElement ( String namespaceURI, String localName,
							   String qName, Attributes attr )
		throws SAXException
	{
		resetContents();
	}


	public void endElement(String namespaceURI, String localName,
			String qName) throws SAXException
	{
		String contents = getContents();
		
		if ( qName.equals("red") ) { 
			color.setRed ( Integer.valueOf ( contents.toString().trim() ).intValue() );
		} else if ( qName.equals("green") ) { 
			color.setGreen ( Integer.valueOf ( contents.toString().trim() ).intValue() );
		} else if ( qName.equals("blue") ) { 
			color.setBlue ( Integer.valueOf ( contents.toString().trim() ).intValue() );

		} else if ( qName.equals("color") ) {
			/* ok. Im done! */
			cleanup();
			parser.getXMLReader().setContentHandler ( parent );
		} else { 
			IWPLog.error(this,"[XMLHandler] Unknown tag: " + qName );
		}


	}


	/*--------------------------------------------------------------------*/

}







