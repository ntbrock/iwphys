package edu.ncssm.iwp.problemdb.directory;

import javax.xml.parsers.SAXParser;

import edu.ncssm.iwp.objects.directory.*;
import edu.ncssm.iwp.problemdb.*;
import org.xml.sax.*;



public class DCategoryXMLHandler extends IWPDefaultXmlHandler
{
	ContentHandler parent;
	SAXParser parser;
	
	DProblemLinkXMLHandler problemLinkHandler = new DProblemLinkXMLHandler();
	
	DCategory category;
	
	/*-----------------------------------------------------------------*/

	public void collectObject ( SAXParser parser, ContentHandler parent, DCategory category )
	throws SAXException
	{
		this.parent = parent;
		this.parser = parser;
		this.category = category;

		parser.getXMLReader().setContentHandler ( this );
	}

	public void cleanup ( )
	{
	}
	
	/*-----------------------------------------------------------------*/

	public void startElement ( String namespaceURI, 
							   String localName, 
							   String qName, 
							   Attributes attr ) 
		throws SAXException 
	{
		resetContents ();
		
		if ( qName.equals ("problemLink" ) ) {
			DProblemLink link = new DProblemLink();
			problemLinkHandler.collectObject(parser, this ,link);
			this.category.addProblemLink( link );
		}
	}

	

	public void endElement ( String namespaceURI,
							 String localName, 
							 String qName )
		throws SAXException 
	{
		String contents = getContents();
		
		if ( qName.equals ( "name" ) ) {
			category.setName ( contents.toString().trim() );

		} else if ( qName.equals("expandedByDefault") ) { 
			
			String content = contents.toString().trim();
			
			if ( content.equalsIgnoreCase("on") ||
			     content.equalsIgnoreCase("yes") ||
				 content.equalsIgnoreCase("true") ||
				 content.equalsIgnoreCase("1") ) {
				category.setExpandedByDefault(true);
			}
			
		} else if ( qName.equals("category") ) {
			cleanup();
			parser.getXMLReader().setContentHandler ( parent );
		}

		
	}
	
}


