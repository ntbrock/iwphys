package edu.ncssm.iwp.objects;

import edu.ncssm.iwp.exceptions.InvalidObjectNameX;
import edu.ncssm.iwp.math.*;
import edu.ncssm.iwp.plugin.IWPObjectXmlHandler;
import edu.ncssm.iwp.plugin.IWPXmlable;
import edu.ncssm.iwp.problemdb.*;

import java.util.*;
import org.xml.sax.*;

import javax.xml.parsers.*;

import edu.ncssm.iwp.util.*;


public class DObject_OutputXMLHandler extends IWPDefaultXmlHandler implements IWPObjectXmlHandler
{

	ContentHandler parent;
	SAXParser parser;

	DObject_Output object;
	MCalculator calculator;

	MCalculatorXMLHandler xmlCalculator = new MCalculatorXMLHandler();

	/*--------------------------------------------------------------------*/

	public void collectObject ( SAXParser parser,IWPDefaultXmlHandler parent,
		    IWPXmlable object )
		throws SAXException
	{
		this.parent = parent;
		this.parser = parser;
		this.object = (DObject_Output) object;

		parser.getXMLReader().setContentHandler ( this );
	}


	void cleanup () 
	{
		//IWPLog.info(this,"[DObject_OutputXMLHandler][cleanup]");
	}

	/*--------------------------------------------------------------------*/

	public void startElement ( String namespaceURI, String localName,
			   String qName, Attributes attr )
		throws SAXException
	{
		resetContents();
		Hashtable attrs = attrListToHash(attr);
		
		if ( qName.equals("calculator") ) {
		    // IWPLog.info(this,"[DObject_OutputXMLHandler] Found a calculator");
			
			String type = (String)attrs.get("type");
			//IWPLog.info(this,"Making a new calculator of type: " + type );

			if ( type.equals ( "parametric" ) ) { 
				calculator = new MCalculator_Parametric();
				// IWPLog.info(this,"[DObject_OutputXMLHandler]Parametric calc set");

			} else if(type.equals("euler")) {
			    calculator=new MCalculator_Euler();
			    // IWPLog.info(this,"[DObject_OutputXMLHandler] Euler calc set");
			} else { 
				IWPLog.error(this,"[DObject_OutputXMLHandler] ERROR: Unknown calculator type: " + type );
				return;
			}

			object.setCalculator ( calculator );
			xmlCalculator.collectObject ( parser, this, calculator );

			
			// IWPLog.info(this,"[DObject_OutputXMLHandler] equation in calculator: "+ calculator.toString());

		}
		
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

		} else if ( qName.equals("units") ) {
			object.setUnits(contents.toString());

		} else if ( qName.equals("equation") ) {
			object.setEquation ( contents.toString() );
			
		} else if ( qName.equals("hidden")) {

			int vis = Integer.valueOf ( contents.toString().trim() ).intValue();
			if ( vis > 0 ) { object.setHidden ( true ); }

		} else if ( qName.equals("output") ) {
			/* ok. Im done! */
			cleanup();
			parser.getXMLReader().setContentHandler ( parent );
		} else { 
			IWPLog.error(this,"[XMLHandler] Unknown tag: " + qName );
		}

	}



	/*--------------------------------------------------------------------*/

}
