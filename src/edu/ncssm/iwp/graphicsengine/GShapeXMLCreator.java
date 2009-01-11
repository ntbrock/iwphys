package edu.ncssm.iwp.graphicsengine;

import edu.ncssm.iwp.math.*;
import edu.ncssm.iwp.toolkit.xml.*;
import edu.ncssm.iwp.util.*;


// WARNING: Magic strings everywhere throughout this file.

public class GShapeXMLCreator extends XMLCreator
{
    MCalculatorXMLCreator widthCreator;
    MCalculatorXMLCreator heightCreator;
    MCalculatorXMLCreator angleCreator;
    GShape_GraphPropertySelectorXMLCreator gpsCreator;

    GShape shape;

    public GShapeXMLCreator ( GShape shape )
    {
        this.shape = shape;

        widthCreator = new MCalculatorXMLCreator ( shape.getWidthCalculator() );
        heightCreator = new MCalculatorXMLCreator ( shape.getHeightCalculator() );
        angleCreator = new MCalculatorXMLCreator( shape.getAngleCalculator() );
        gpsCreator=new GShape_GraphPropertySelectorXMLCreator(shape.getGShape_GraphPropertySelector());
    }


    public XMLElement getElement ()
    {
        XMLElement elem = new XMLElement ( "shape" );

        if ( shape instanceof GShape_Rectangle ) {
            elem.addAttribute ( "type", GShape.XML_RECTANGLE );

        } else if ( shape instanceof GShape_Circle ) {
            elem.addAttribute ( "type", GShape.XML_CIRCLE );

        } else if ( shape instanceof GShape_Polygon ) {
            elem.addAttribute( "type", GShape.XML_POLYGON );

            //XMLElement points = new XMLElement("points");
            //points.addElement((new GShape_PolygonXMLCreator((GShape_Polygon)shape)).getElement());
            elem.addElement((new GShape_PolygonXMLCreator((GShape_Polygon)shape)).getElement());

            // 2004.09.18 iwpmtg: WAR
        } else if ( shape instanceof GShape_Vector ) {
        	elem.addAttribute( "type", GShape.XML_VECTOR );
        
        } else if ( shape instanceof GShape_Line ) {
            elem.addAttribute ( "type", GShape.XML_LINE ); // type="Line"
        	
        } else if (shape instanceof GShape_Bitmap) {
        	elem.addAttribute ( "type", GShape.XML_BITMAP);
        	
            XMLElement file = new XMLElement("file");
            file.addAttribute("image", shape.getFile());
            elem.addElement(file);
 
        } else {

            IWPLog.info(this,"[GShapeXMLCreator] ERROR: Couldn't determine what type this object is!");
            return null;
        }

        
        // 2004.09.18: iwpmtg: Drawrails
        elem.addAttribute ( "drawTrails", shape.isDrawTrails() ? "true" : "false" );
    	
        


        

	// 2006.02.05: sweeneyb -- vectors need to save.
	XMLElement vectors = new XMLElement("vectors");
	GShape_VectorSelector gvs = shape.getGShape_VectorSelector();
	elem.addAttribute("drawVectors", shape.isDrawVectors() ? "true" : "false" );
	vectors.addAttribute("xVel", gvs.xVelSelected()? "true" : "false" );
	vectors.addAttribute("yVel", gvs.yVelSelected()? "true" : "false" );
	vectors.addAttribute("xAccel", gvs.xAccelSelected()? "true" : "false" );
	vectors.addAttribute("yAccel", gvs.yAccelSelected()? "true" : "false" );
	vectors.addAttribute("Vel", gvs.VelSelected()? "true" : "false" );
	vectors.addAttribute("Accel", gvs.AccelSelected()? "true" : "false" );
	elem.addElement ( vectors );

        XMLElement width = new XMLElement("width");
        width.addElement ( widthCreator.getElement () );
        elem.addElement ( width );


        XMLElement height = new XMLElement("height");
        height.addElement ( heightCreator.getElement( ) );
        elem.addElement ( height );
        
        // 2009-Jan-11 brockman, polygons don't save angles.
        if ( ! ( shape instanceof GShape_Polygon ) ) { 
        	XMLElement angle = new XMLElement("angle");
        	angle.addElement ( angleCreator.getElement( ) );
        	elem.addElement ( angle );
        }

        XMLElement graphOptions = new XMLElement("graphOptions");
        graphOptions.addAttribute("graphVisible",""+shape.getIsGraphable());
        graphOptions.addElement(gpsCreator.getElement());
        elem.addElement(graphOptions);

        return elem;
    }


}

