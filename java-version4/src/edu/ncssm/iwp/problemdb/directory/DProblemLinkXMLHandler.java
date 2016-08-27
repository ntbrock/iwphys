
package edu.ncssm.iwp.problemdb.directory;

import org.xml.sax.*;
import javax.xml.parsers.*;

import edu.ncssm.iwp.objects.directory.*;
import edu.ncssm.iwp.problemdb.*;


public class DProblemLinkXMLHandler extends IWPDefaultXmlHandler
{
	ContentHandler parent;
	SAXParser parser;

	DProblemLink link;

	/*--------------------------------------------------------------------*/

	public void collectObject ( SAXParser parser, ContentHandler parent, DProblemLink link )
	throws SAXException
	{
		this.parent = parent;
		this.parser = parser;
		this.link = link;

		parser.getXMLReader().setContentHandler ( this );
	}

	public void cleanup ( ) { }

	/*--------------------------------------------------------------------*/

	public void startElement ( String namespaceURI, String localName,
			   String qName, Attributes attr )
		throws SAXException
	{
		resetContents();
	}
	
	
	public void endElement(String namespaceURI, String localName,
            String qName)
		throws SAXException
    {
		String contents = getContents();

		if ( qName.equals("filename") ) { 
			link.setFilename ( contents.toString() );
		} else if ( qName.equals("summary") ) { 
			link.setSummary ( contents.toString() );
		} else if ( qName.equals("problemLink") ) {
			/* ok. Im done! */
			cleanup();
			parser.getXMLReader().setContentHandler ( parent );
		}
		
	}

}
