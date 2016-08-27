package edu.ncssm.iwp.graphicsengine;

import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.util.*;
import edu.ncssm.iwp.math.*;
import java.util.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
 
public class GShape_Polygon_PointXMLHandler extends IWPDefaultXmlHandler
{

	ContentHandler parent;
	SAXParser parser;
	GShape_Polygon shape;

	MCalculatorXMLHandler xmlCalculator = new MCalculatorXMLHandler();
	GShape_GraphPropertySelectorXMLHandler xmlProps =
		new GShape_GraphPropertySelectorXMLHandler();
	
	boolean inWidth = false;
	boolean inHeight = false;
    int index=0;

	/*--------------------------------------------------------------------*/

	public void collectObject ( SAXParser parser,ContentHandler parent,
				    			GShape_Polygon shape,String inIndex )
		throws SAXException
	{

		this.parent = parent;
		this.parser = parser;
		this.shape = shape;
		try{
		    index=Integer.parseInt(inIndex);
		} catch (Exception e) {
		    IWPLog.info(this,"[GShape_Polygon_PointXMLHandler] Error reading point index");
		}

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
		Hashtable attrs = attrListToHash(attr);
		//IWPLog.info(this,"[GShapeXMLHandler] name: " + name );

		/* have to do some attr type switches here */

		if ( qName.equals("xpath") ) {
			inWidth = true;
			inHeight = false;
			
		} else if ( qName.equals("ypath") ) {
			inWidth = false;
			inHeight = true;

		}
		else if ( qName.equals("calculator") ) {

			MCalculator calculator;
			
			String type = (String)attrs.get("type");
			//IWPLog.info(this,"Making a new calculator of type: " + type );

			if ( type.equals ( "parametric" ) ) { 
				calculator = new MCalculator_Parametric();
 			} else if (type.equals("euler")) {
			    calculator = new MCalculator_Euler();
 			} else if (type.equals("RK2")) {
			    calculator = new MCalculator_RK2();
			} else { 
				IWPLog.info(this,"Unknown calculator type: " + type );
				return;
			}

			/* are we in the x or the y ? */
			if ( inWidth ) { 
				shape.setXPointCalc( calculator,index );
				inWidth = false; inHeight = false;
			} else if ( inHeight ) { 
				shape.setYPointCalc ( calculator,index );
				inWidth = false; inHeight = false;
			} else { 
				IWPLog.info(this,"[GShape_Polygon_PointXMLHandler] Don't know where to put this eqn!");
			}

			xmlCalculator.collectObject ( parser, this, calculator );

		}

	}



	public void endElement(String namespaceURI, String localName,
			String qName) throws SAXException
	{

		if ( qName.equals("point") ) {
			/* ok. Im done! */
			cleanup();
			parser.getXMLReader().setContentHandler ( parent );
		
		} else if ( qName.equals("width") ) { 
			// ok
		} else if ( qName.equals("height") ) { 
			// ok
		} else if ( qName.equals("xpath") ) { 
			// ok
		} else if ( qName.equals("ypath") ) { 
			// ok
		} else { 
			IWPLog.warn(this,"[XMLHandler] unknown tag: " + qName );
		}


	}



	/*--------------------------------------------------------------------*/

}







