package edu.ncssm.iwp.objects;

import edu.ncssm.iwp.plugin.IWPObjectXmlHandler;
import edu.ncssm.iwp.plugin.IWPXmlable;
import edu.ncssm.iwp.problemdb.*;

import java.util.*;
import org.xml.sax.*;
import javax.xml.parsers.*;

import edu.ncssm.iwp.util.*;

public class DObject_WindowXMLHandler extends IWPDefaultXmlHandler implements IWPObjectXmlHandler
{

	ContentHandler parent;
	SAXParser parser;
	ArrayList objects = new ArrayList();

	DObject_Window object;

	/*--------------------------------------------------------------------*/

	public void collectObject ( SAXParser parser,IWPDefaultXmlHandler parent,
		    IWPXmlable object )
	throws SAXException
	{
		this.parent = parent;
		this.parser = parser;
		this.object = (DObject_Window) object;
		parser.getXMLReader().setContentHandler ( this );
	}

	public DObject_Window getObject ( ) { return object; }

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
			// IWPLog.info(this,"[DObject_WindowXMLHandler]xMax @ read: "+Double.valueOf ( contents.toString().trim() ).doubleValue());

		} else if ( qName.equals("ymin") ) { 
			object.setMinY ( Double.valueOf ( contents.toString().trim() ).doubleValue() );

		} else if ( qName.equals("ymax") ) { 
			object.setMaxY ( Double.valueOf ( contents.toString().trim() ).doubleValue() );

		} else if ( qName.equals("xgrid") ) { 
			object.setGridX ( Double.valueOf ( contents.toString().trim() ).doubleValue() );

		} else if ( qName.equals("ygrid") ) { 
			object.setGridY ( Double.valueOf ( contents.toString().trim() ).doubleValue() );
		
		} else if ( qName.equals("yunit") ) { 
			object.setUnitY ( contents.toString().trim() );

		} else if ( qName.equals("xunit") ) { 
			object.setUnitX ( contents.toString().trim() );
		
		} else if ( qName.equals("showAllDataAvailable")) {
			object.setShowAllDataAvailable (contents.toString().trim().equalsIgnoreCase("true"));
	
		} else if ( qName.equals("drawGridNumbers")) {
			object.setDrawGridNumbers (contents.toString().trim().equalsIgnoreCase("true"));	
			
		} else if ( qName.equals("window") ) {
			/* ok. Im done! */
			parser.getXMLReader().setContentHandler ( parent );
		} else { 
			IWPLog.error(this,"[XMLHandler] Unknown tag: " + qName );
		}

	}


	/*--------------------------------------------------------------------*/

}







