package edu.ncssm.iwp.objects.floatingtext;

import java.util.ArrayList;

import javax.xml.parsers.SAXParser;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import edu.ncssm.iwp.plugin.*;
import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.exceptions.InvalidObjectNameX;
import edu.ncssm.iwp.graphicsengine.GColor;
import edu.ncssm.iwp.graphicsengine.GColorXMLHandler;
import edu.ncssm.iwp.math.*;
import edu.ncssm.iwp.util.IWPLogPopup;

public class DObject_FloatingText_XMLHandler extends IWPDefaultXmlHandler implements IWPObjectXmlHandler
{
	MCalculatorXMLHandler xmlCalculatorHandler = new MCalculatorXMLHandler();
	GColorXMLHandler xmlColorHandler = new GColorXMLHandler();
	
	ContentHandler parent;
	SAXParser parser;
	ArrayList objects = new ArrayList();

	DObject_FloatingText object;
	String varName;
	MCalculator varCalculator;
	
	/*--------------------------------------------------------------------*/
	/**
	 * here we need to collect everything we need to know and then hand control back.
	 */

	public void collectObject ( SAXParser parser, IWPDefaultXmlHandler parent,
						       	IWPXmlable object )
		throws SAXException
	{
		this.parent = parent;
		this.parser = parser;
		this.object = (DObject_FloatingText) object;
		parser.getXMLReader().setContentHandler ( this );
	}


	public void startElement(String namespaceURI, String localName, String qName, Attributes attr) throws SAXException {
		
		resetContents();

		

		if ( qName.equals("color") ) { 
            GColor color = new GColor();
            object.setFontColor ( color );
            xmlColorHandler.collectObject ( parser, this, color );
            
		} else if ( qName.equals("calculator") ) {

			varCalculator = null;			
			String type = (String)attr.getValue("type");
			
			if ( type.equals ( "parametric" ) ) { 
				varCalculator = new MCalculator_Parametric();
			} else {
				IWPLogPopup.error(this, "The Floating Text only supports Parametric Calcs");
				return;
			}
			
			xmlCalculatorHandler.collectObject ( parser, this, varCalculator );
		}
	
	}

	public void endElement(String namespaceURI, String localName,
			String qName) throws SAXException
	{
		String contents = getContents();

        if ( qName.equals("name") ) {
          	
        	// If name is invalid, leave the object name set to the default name. 2007-Jun-03 brockman.
            try { 
        		object.setName(contents.toString());
        	} catch ( InvalidObjectNameX x ) {
        		IWPLogPopup.error(this, x.getMessage());
        	}

        } else if (qName.equals("text") ) {
			object.setText(contents.toString());

        } else if (qName.equals("units") ) {
			object.setUnits(contents.toString());
			
        } else if (qName.equals("showValue") ) {
			String bool = contents.toString();
			// TODO, move this true value out to a util class?
			if ( bool.equalsIgnoreCase("true") || bool.equalsIgnoreCase("1") ) {
				object.setShowValue(true);
			} else {
				object.setShowValue(false);
			}
			
        } else if (qName.equals("fontSize") ) {
			object.setFontSize(Integer.parseInt(contents.toString()));

        } else if ( qName.equals("xpath")) {
        	object.setXpath(varCalculator);
        
        } else if ( qName.equals("ypath")) {
        	object.setYpath(varCalculator);

        } else if ( qName.equals("value")) {
        	object.setValue(varCalculator);		

        } else if ( qName.equals("object") ) {
            /* ok. Im done! */
            parser.getXMLReader().setContentHandler ( parent );
        }

    }
		
}
