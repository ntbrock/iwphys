
package edu.ncssm.iwp.problemdb;

import org.xml.sax.*;

import edu.ncssm.iwp.util.IWPLog;

import javax.xml.parsers.*;

public class DAuthorXMLHandler extends IWPDefaultXmlHandler
{

	ContentHandler parent;
	SAXParser parser;

	DAuthor author;

	/*--------------------------------------------------------------------*/

	public void collectObject ( SAXParser parser, ContentHandler parent,
							    DAuthor author )
	throws SAXException
	{

		this.parent = parent;
		this.parser = parser;
		this.author = author;

		parser.getXMLReader().setContentHandler ( this );
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
            String qName)
		throws SAXException
    {
		String contents = getContents();

		if ( qName.equals("name") ) { 
			author.setName ( contents.toString() );
		} else if ( qName.equals("username") ) { 
			author.setUsername ( contents.toString() );
		} else if ( qName.equals("email") ) { 
			author.setEmail ( contents.toString() );
		} else if ( qName.equals("organization") ) { 
			author.setOrganization ( contents.toString() );

		} else if ( qName.equals("author") ) {
			/* ok. Im done! */
			cleanup();
			parser.getXMLReader().setContentHandler ( parent );
		} else { 
			IWPLog.error(this,"[XMLHandler] Unknown tag: " + qName );
		}


	}

	/*--------------------------------------------------------------------*/

}







