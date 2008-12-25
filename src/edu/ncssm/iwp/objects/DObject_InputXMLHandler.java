
package edu.ncssm.iwp.objects;


import edu.ncssm.iwp.exceptions.InvalidObjectNameX;
import edu.ncssm.iwp.plugin.IWPObjectXmlHandler;
import edu.ncssm.iwp.plugin.IWPXmlable;
import edu.ncssm.iwp.problemdb.*;

import org.xml.sax.*;

import javax.xml.parsers.*;

import edu.ncssm.iwp.util.*;

public class DObject_InputXMLHandler extends IWPDefaultXmlHandler implements IWPObjectXmlHandler
{

	ContentHandler parent;
	SAXParser parser;

	DObject_Input object;

	/*--------------------------------------------------------------------*/

	public void collectObject ( SAXParser parser,IWPDefaultXmlHandler parent,
		    IWPXmlable object )
		throws SAXException
	{
		this.parent = parent;
		this.parser = parser;
		this.object = (DObject_Input) object;
		parser.getXMLReader().setContentHandler ( this );
	}

	public DObject_Input getObject ( ) { return object; }

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

		if ( qName.equals("name") ) {

			try { 
				object.setName(contents.toString());
        	} catch ( InvalidObjectNameX x ) {
        		IWPLogPopup.error(this, x.getMessage());
        		// Leave the object name set to the default name. 2007-Jun-03 brockman.
        	}

		} else if ( qName.equals("text") ) {

			object.setText(contents.toString());

		} else if ( qName.equals("initialValue") ) {

			object.setInitialValue( Double.valueOf ( contents.toString().trim() ).doubleValue() );

		} else if ( qName.equals("units") ) {

			object.setUnits(contents.toString());

		} else if ( qName.equals("hidden")) {

			int vis = Integer.valueOf ( contents.toString().trim() ).intValue();
			if ( vis > 0 ) { object.setVisible ( false ); }

		} else if ( qName.equals("input") ) {
			/* ok. Im done! */
			parser.getXMLReader().setContentHandler ( parent );

		} else { 

			IWPLog.error(this,"[XMLHandler] Unknown tag: " + qName );

		}

	}


	/*--------------------------------------------------------------------*/

}









