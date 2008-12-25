package edu.ncssm.iwp.objects;

import java.util.Hashtable;

import javax.xml.parsers.SAXParser;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import edu.ncssm.iwp.exceptions.InvalidObjectNameX;
import edu.ncssm.iwp.graphicsengine.GColor;
import edu.ncssm.iwp.graphicsengine.GColorXMLHandler;
import edu.ncssm.iwp.graphicsengine.GShape;
import edu.ncssm.iwp.graphicsengine.GShapeXMLHandler;
import edu.ncssm.iwp.graphicsengine.GShape_Circle;
import edu.ncssm.iwp.graphicsengine.GShape_Line;
import edu.ncssm.iwp.graphicsengine.GShape_Polygon;
import edu.ncssm.iwp.graphicsengine.GShape_Rectangle;
import edu.ncssm.iwp.graphicsengine.GShape_Vector;
import edu.ncssm.iwp.graphicsengine.GShape_Bitmap;
import edu.ncssm.iwp.graphicsengine.GShape_VectorSelector;
import edu.ncssm.iwp.math.MCalculator;
import edu.ncssm.iwp.math.MCalculatorXMLHandler;
import edu.ncssm.iwp.math.MCalculator_Euler;
import edu.ncssm.iwp.math.MCalculator_Parametric;
import edu.ncssm.iwp.math.MCalculator_RK2;
import edu.ncssm.iwp.math.MCalculator_RK4;
import edu.ncssm.iwp.plugin.IWPObjectXmlHandler;
import edu.ncssm.iwp.plugin.IWPXmlable;
import edu.ncssm.iwp.problemdb.IWPDefaultXmlHandler;
import edu.ncssm.iwp.util.IWPLog;
import edu.ncssm.iwp.util.IWPLogPopup;


public class DObject_SolidXMLHandler extends IWPDefaultXmlHandler implements IWPObjectXmlHandler
{

    ContentHandler parent;
    SAXParser parser;

    DObject_Solid object;
    GShape shape;
    MCalculator xpath;
    MCalculator ypath;

    GShapeXMLHandler xmlShape = new GShapeXMLHandler();
    MCalculatorXMLHandler xmlCalculator = new MCalculatorXMLHandler();
    GColorXMLHandler xmlColor = new GColorXMLHandler();

    GShape_VectorSelector gsvSelector = new GShape_VectorSelector();

    boolean inX = false;
    boolean inY = false;

    /*--------------------------------------------------------------------*/

    public void collectObject ( SAXParser parser,IWPDefaultXmlHandler parent,
		    IWPXmlable object )
    throws SAXException	
    {
        this.parent = parent;
        this.parser = parser;
        this.object = (DObject_Solid) object;
        parser.getXMLReader().setContentHandler ( this );
    }

    public DObject_Solid getObject ( ) { return object; }

    public void cleanup () {}

    /*--------------------------------------------------------------------*/

    public void startElement ( String namespaceURI, String localName,
			   String qName, Attributes attr )
        throws SAXException
    {
        resetContents();
        Hashtable attrs = attrListToHash(attr);

        if ( qName.equals("shape") ) {

            String type = (String)attrs.get("type");
            if ( type.equals( GShape.XML_RECTANGLE ) ) {
                shape = new GShape_Rectangle( object );
            } else if ( type.equals( GShape.XML_CIRCLE )) {
                shape = new GShape_Circle( object );
            } else if (type.equals( GShape.XML_POLYGON )) {
                shape=new GShape_Polygon( object );

                // 2004.09.18 iwpmtg: Adding Line Shape
            } else if (type.equals( GShape.XML_LINE )) {
                shape=new GShape_Line( object );

            } else if (type.equals( GShape.XML_VECTOR )) {
            	shape = new GShape_Vector( object );
            
            }  else if (type.equals( GShape.XML_BITMAP )) {  // 12/11/2007 - Cory, adding Bitmap shape
            	shape = new GShape_Bitmap( object );
            
            } else {
            	IWPLog.error(this,"[DObject_SolidXMLHandler] unknown type: " + type );
                return;
            }

            // 2004.09.18: iwpmtg
            try {
                String drawTrails = (String)attrs.get("drawTrails");
                if ( drawTrails.equalsIgnoreCase ( "true" ) ) {
                    shape.setIsDrawTrails ( true );
                }
            } catch ( NullPointerException e ) {
                // the file is old, and this attribute doesn't exist.
            }

            
            
	    try{
		String drawVectors = (String)attrs.get("file");
		shape.setIsDrawVectors(drawVectors.equals("true")?true:false);
		
	    } catch (NullPointerException e ) {
	    }

            object.setShape ( shape );

            /* pass onto the shape parser */
            xmlShape.collectObject ( parser, this, shape );

        } else if ( qName.equals("color") ) {

            GColor color = new GColor();
            object.setGColor ( color );
            xmlColor.collectObject ( parser, this, color );

        } else if ( qName.equals("xpath")) {
            inX = true;
            inY = false;

        } else if ( qName.equals("ypath")) {
            inY = true;
            inX = false;
        } else if ( qName.equals("file")) {
        	
        	shape.setFile((String)attrs.get("image"));
        
        } else if (qName.equals("vectors")) {
	    GShape_VectorSelector gvs = new GShape_VectorSelector();
	    gvs.setxVelSelected(attrs.get("xVel").equals("true")?true:false);
	    gvs.setyVelSelected(attrs.get("yVel").equals("true")?true:false);
	    gvs.setxAccelSelected(attrs.get("xAccel").equals("true")?true:false);
	    gvs.setyAccelSelected(attrs.get("yAccel").equals("true")?true:false);
	    gvs.setVelSelected(attrs.get("Vel").equals("true")?true:false);
	    gvs.setAccelSelected(attrs.get("Accel").equals("true")?true:false);
	    //shape.setGShape_VectorSelector(gvs);
	    gsvSelector = gvs;
	}


        else if ( qName.equals("calculator") ) {

            MCalculator calculator;

            String type = (String)attrs.get("type");
            //IWPLog.info(this,"Making a new calculator of type: " + type );

            if ( type.equalsIgnoreCase ( "parametric" ) ) {
                calculator = new MCalculator_Parametric();
            } else if (type.equalsIgnoreCase("euler")) {
                calculator = new MCalculator_Euler();
            } else if (type.equalsIgnoreCase("RK2")) {
                calculator = new MCalculator_RK2();
            } else if (type.equalsIgnoreCase("RK4")) {
                calculator = new MCalculator_RK4();
                IWPLog.info(this,"[DObj_SolidXMLHandler] built an RK4");
            } else {
                IWPLog.info(this,"Unknown calculator type: " + type );
                return;
            }

            /* are we in the x or the y ? */
            if ( inX ) {
                //IWPLog.info(this,"Setting it as the X Calc!");
                object.setCalc ( "x", calculator );
                inX = false; inY = false;
            } else if ( inY ) {
                //IWPLog.info(this,"Setting it as the X Calc!");
                object.setCalc ( "y", calculator );
                inX = false; inY = false;
            } else {
                IWPLog.info(this,"[DObject_SolidXMLHandler] Don't know where to put this eqn!");
            }

            xmlCalculator.collectObject ( parser, this, calculator );
            //IWPLog.info(this,"[DObject_SolidXMLHandler] calculator's value :"+
            //calculator.toString());

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
        	
        } else if ( qName.equals("solid") ) {
	    //shape.setGShape_VectorSelector(gsvSelector);
            /* ok. Im done! */
            cleanup();
            parser.getXMLReader().setContentHandler ( parent );
        }

    }


    /*--------------------------------------------------------------------*/

}
