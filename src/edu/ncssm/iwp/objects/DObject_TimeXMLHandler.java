package edu.ncssm.iwp.objects;

import edu.ncssm.iwp.plugin.IWPObjectXmlHandler;
import edu.ncssm.iwp.plugin.IWPXmlable;
import edu.ncssm.iwp.problemdb.*;

import org.xml.sax.*;

import javax.xml.parsers.*;

import edu.ncssm.iwp.util.*;

public class DObject_TimeXMLHandler extends IWPDefaultXmlHandler implements IWPObjectXmlHandler
{

	ContentHandler parent;
	SAXParser parser;

	DObject_Time object;

	/*--------------------------------------------------------------------*/

	public void collectObject ( SAXParser parser,IWPDefaultXmlHandler parent,
		    IWPXmlable object )
	throws SAXException
	{
		this.parent = parent;
		this.parser = parser;
		this.object = (DObject_Time) object;
		parser.getXMLReader().setContentHandler ( this );
	}

	public DObject_Time getObject ( ) { return object; }

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

		if ( qName.equals("start") ) {
			object.setStartTime ( Double.valueOf ( contents.toString().trim() ).doubleValue() );

		} else if ( qName.equals("stop") ) { 
			object.setStopTime ( Double.valueOf ( contents.toString().trim() ).doubleValue() );

		} else if ( qName.equals("change") ) { 
			object.setChange ( Double.valueOf ( contents.toString().trim() ).doubleValue() );

		} else if ( qName.equals("fps") ) { 
			object.setFps ( Double.valueOf ( contents.toString().trim() ).doubleValue() );

		} else if ( qName.equals("fps") ) { 
			object.setFps ( Double.valueOf ( contents.toString().trim() ).doubleValue() );
		 
		} else if (qName.equals("usePreciseCalculations") ) {
			String bool = contents.toString();
			// TODO, move this true value out to a util class?
			if ( bool.equalsIgnoreCase("true") || bool.equalsIgnoreCase("1") ) {
				object.setUsePreciseCalculations(true);
			} else {
				object.setUsePreciseCalculations(false);
			}
			
		} else if ( qName.equals("time") ) {
			/* ok. Im done! */
			parser.getXMLReader().setContentHandler ( parent );
		} else { 
			IWPLog.error(this,"[XMLHandler] Unknown tag: " + qName );
		}


	}

	/*--------------------------------------------------------------------*/

}







