package edu.ncssm.iwp.objects;


import edu.ncssm.iwp.plugin.IWPObjectXmlHandler;
import edu.ncssm.iwp.plugin.IWPXmlable;
import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.util.*;

import java.util.*;
import org.xml.sax.*;


import javax.xml.parsers.*;

public class DObject_GraphWindowXMLHandler extends IWPDefaultXmlHandler implements IWPObjectXmlHandler
{

	ContentHandler parent;
	SAXParser parser;
	ArrayList objects = new ArrayList();

	DObject_GraphWindow object;

	/*--------------------------------------------------------------------*/

	public void collectObject ( SAXParser parser,IWPDefaultXmlHandler parent,
		    IWPXmlable object )
		throws SAXException
	{
		this.parent = parent;
		this.parser = parser;
		this.object = (DObject_GraphWindow) object;
		parser.getXMLReader().setContentHandler ( this );
	}

	public DObject_GraphWindow getObject ( ) { return object; }

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

		if ( qName.equals("xmin") ) {
			object.setMinX ( Double.valueOf ( contents.toString().trim() ).doubleValue() );

		} else if ( qName.equals("xmax") ) { 
			object.setMaxX ( Double.valueOf ( contents.toString().trim() ).doubleValue() );

		} else if ( qName.equals("ymin") ) { 
			object.setMinY ( Double.valueOf ( contents.toString().trim() ).doubleValue() );

		} else if ( qName.equals("ymax") ) { 
			object.setMaxY ( Double.valueOf ( contents.toString().trim() ).doubleValue() );

		} else if ( qName.equals("xgrid") ) { 
			object.setGridX ( Double.valueOf ( contents.toString().trim() ).doubleValue() );

		} else if ( qName.equals("ygrid") ) { 
			object.setGridY ( Double.valueOf ( contents.toString().trim() ).doubleValue() );

		} else if ( qName.equals("GraphWindow") ) {
			/* ok. Im done! */
			parser.getXMLReader().setContentHandler ( parent );
		} else { 
			IWPLog.error(this,"[XMLHandler] Unknown tag: " + qName );
		}

	}


	/*--------------------------------------------------------------------*/

}







