package edu.ncssm.iwp.objects;

import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.plugin.*;
import edu.ncssm.iwp.util.*;

import org.xml.sax.*;
import javax.xml.parsers.*;

public class DObject_DescriptionXMLHandler extends IWPDefaultXmlHandler implements IWPObjectXmlHandler
{

	ContentHandler parent;
	SAXParser parser;

	DObject_Description object;

	/*--------------------------------------------------------------------*/

	public void collectObject ( SAXParser parser,IWPDefaultXmlHandler parent,
				    IWPXmlable object )
		throws SAXException
	{
		this.parent = parent;
		this.parser = parser;
		this.object = (DObject_Description) object;
		parser.getXMLReader().setContentHandler ( this );
	}

	public DObject_Description getObject ( ) { return object; }

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
		
		if ( qName.equals("text") ) {
			String desc = contents.toString();
			object.setText ( new String ( desc ) );

		} else if ( qName.equals("description") ) {
			/* ok. Im done! */
			parser.getXMLReader().setContentHandler ( parent );
		} else { 
			IWPLog.error(this,"[XMLHandler] unknown tag: " + qName );
		}

	}


	/*--------------------------------------------------------------------*/

}
