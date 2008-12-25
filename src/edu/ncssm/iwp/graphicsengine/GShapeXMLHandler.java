package edu.ncssm.iwp.graphicsengine;

import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.util.*;

import edu.ncssm.iwp.math.*;

import java.util.*;

import org.xml.sax.*;

import javax.xml.parsers.*;

public class GShapeXMLHandler extends IWPDefaultXmlHandler
{
    ContentHandler parent;
    SAXParser parser;

    GShape shape;

    MCalculatorXMLHandler xmlCalculator = new MCalculatorXMLHandler();
    
    GShape_GraphPropertySelectorXMLHandler xmlProps =
    	new GShape_GraphPropertySelectorXMLHandler();

    boolean inWidth = false;
    boolean inHeight = false;
    boolean inAngle = false;
    
    boolean setAngle = false;

    /*--------------------------------------------------------------------*/

    public void collectObject ( SAXParser parser, ContentHandler parent,
                                GShape shape )
    	throws SAXException
    {

        this.parent = parent;
        this.parser = parser;
        this.shape = shape;

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

        if ( qName.equals("width") ) {
            inWidth = true;
            inHeight = false;
            inAngle = false;

        } else if ( qName.equals("height") ) {
            inWidth = false;
            inHeight = true;
            inAngle = false;

        } else if (qName.equals("angle") ) {
        	inWidth = false;
        	inHeight = false;
        	inAngle = true;
        	
        	setAngle = true;
        } else if (qName.equals("file") ) {
        	
        	shape.setFile((String)attrs.get("image"));
        
        }else if ( qName.equals("calculator") ) {

            MCalculator_Parametric calculator;
        	
            String type = (String)attrs.get("type");
            //IWPLog.info(this,"Making a new calculator of type: " + type );

            if ( type.equals ( "parametric" ) ) {
                calculator = new MCalculator_Parametric();
            } else {
                throw new SAXException ("[GShapeXMLHandler] Unknown calculator type for Shape: " + type );
            }

            /* are we in the x or the y ? */
            if ( inWidth ) {
            	shape.setWidthCalculator ( calculator );
                inWidth = false; inHeight = false; inAngle = false;
            } else if ( inHeight ) {
            	shape.setHeightCalculator ( calculator );
                inWidth = false; inHeight = false; inAngle = false;
            } else if ( inAngle ) {
            	shape.setAngleCalculator ( calculator );
            	inWidth = false; inHeight = false; inAngle = false;
            }
            else {
                IWPLog.info(this,"[GShapeXMLHandler] Don't know where to put this eqn!");
            }

            xmlCalculator.collectObject ( parser, this, calculator );


        } else if (qName.equals("vectors")) {
        
        	GShape_VectorSelector gvs = new GShape_VectorSelector();
        	gvs.setxVelSelected(attrs.get("xVel").equals("true")?true:false);
        	gvs.setyVelSelected(attrs.get("yVel").equals("true")?true:false);
        	gvs.setxAccelSelected(attrs.get("xAccel").equals("true")?true:false);
        	gvs.setyAccelSelected(attrs.get("yAccel").equals("true")?true:false);
        	gvs.setVelSelected(attrs.get("Vel").equals("true")?true:false);
        	gvs.setAccelSelected(attrs.get("Accel").equals("true")?true:false);
        	shape.setGShape_VectorSelector(gvs);
        	//	gsvSelector = gvs;
	
        } else if ( qName.equals("graphOptions") ) {
            String isGraphable = (String) attrs.get("graphVisible");
            if ( isGraphable.equals("false") ) {
            	shape.setIsGraphable(false);
            }
            
            xmlProps.collectObject(parser,this,shape);

        } else if(qName.equals("points") && (shape instanceof GShape_Polygon)) {
            GShape_PolygonXMLHandler polyHandler=new GShape_PolygonXMLHandler();
            polyHandler.collectObject ( parser, this, shape );
            
        }
        

    }



	public void endElement(String namespaceURI, String localName,
			String qName) throws SAXException
	{


        if ( qName.equals("shape") ) {
            //ok. Im done!
        	
        	//Backwards compatibility -- Cory, for files w/out rotation (v3)
        	if(!setAngle) {
        		MCalculator_Parametric calculator = new MCalculator_Parametric();;
        		shape.setAngleCalculator ( calculator );
        	}
        	
        	
            cleanup();
            parser.getXMLReader().setContentHandler ( parent );
        } else if ( qName.equals("height") || qName.equals("width") || qName.equals("angle")) {
            inWidth = false;
            inHeight = false;
            inAngle = false;
        } else if ( qName.equals("graphOptions") || qName.equals("vectors") || 	qName.equals("file") ) { 
        	// no error.
           
        } else {
        	throw new SAXException("[GshapeXMLHandler] unknown tag: " + qName );
        }


    }


    /*--------------------------------------------------------------------*/

}







