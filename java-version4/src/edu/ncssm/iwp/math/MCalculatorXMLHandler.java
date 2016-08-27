package edu.ncssm.iwp.math;


import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.util.*;
import edu.ncssm.iwp.exceptions.*;

import java.util.*;
import org.xml.sax.*;

import javax.xml.parsers.*;

public class MCalculatorXMLHandler extends  IWPDefaultXmlHandler
{

	ContentHandler parent;
	SAXParser parser;
	ArrayList objects = new ArrayList();

	MCalculator calculator;

	/*--------------------------------------------------------------------*/

	public void collectObject ( SAXParser parser, ContentHandler parent,
						    MCalculator calculator )
		throws SAXException
	{

		this.parent = parent;
		this.parser = parser;
		this.calculator = calculator;

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
			String qName) throws SAXException
	{
		String contents = getContents();
		
		try { 
		
			if ( qName.equals("value") ) {

			if ( calculator instanceof MCalculator_Parametric ) {
			    //IWPLog.info(this,"[MCalculatorXMLHandler:element=value] setting equation to: '" + contents.toString() + "'");
				((MCalculator_Parametric)calculator).setEquation ( new MEquation(contents.toString()) );
			} else { 
				IWPLog.error(this, "Improper set of 'value' for a non Parametric Calculator");
			}

			} else if(qName.equals("displacement")) {
				if(calculator instanceof MCalculator_Diff) {
					((MCalculator_Diff)calculator).setInitDispEqn(new MEquation(contents.toString()) );
				} else {
					IWPLog.error(this, "Improper set of 'displacement' for a non Diff/Euler Calculator");
				}
		    
			} else if(qName.equals("velocity")) {
				if(calculator instanceof MCalculator_Diff) {
		    	
					((MCalculator_Diff)calculator).setInitVelEqn(new MEquation(contents.toString()) );
				} else {
					IWPLog.error(this, "Improper set of 'velocity' for a non Diff/Euler Calculator");
				}
		    
			} else if(qName.equals("acceleration")) {
				if(calculator instanceof MCalculator_Diff) {
					((MCalculator_Diff)calculator).setAccelEqn(new MEquation(contents.toString()) );
				} else {
					IWPLog.error(this, "Improper set of 'acceleration' for a non Diff/Euler Calculator");
				}
		
			} else if ( qName.equals("calculator") ) {
				/* ok. Im done! */
				cleanup();
				parser.getXMLReader().setContentHandler ( parent );
			} else { 	
				IWPLog.warn(this,"unknown tag: " + qName );
			}

		} catch ( InvalidEquationException x ) {
			IWPLog.x(this, "InvalidEquation: " + x.getMessage(), x);
			throw new SAXException(x);
		}
	}
}







