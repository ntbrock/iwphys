package edu.ncssm.iwp.graphicsengine;

import edu.ncssm.iwp.toolkit.xml.*;

import edu.ncssm.iwp.math.*;
import java.util.Vector;

public class GShape_PolygonXMLCreator extends XMLCreator {

    GShape_Polygon shape;

    public GShape_PolygonXMLCreator(GShape_Polygon in) {
	shape=in;
    }

    public XMLElement getElement() {
	Vector xPts=shape.getXPointCalcs();
	Vector yPts=shape.getYPointCalcs();

	/*
	  XMLElement points = new XMLElement("points");
	  points.addElement((new GShape_PolygonXMLCreator((GShape_Polygon)shape)).getElement());
	  elem.addElement(points);
	*/
	XMLElement elem=new XMLElement("points");

	for(int i=0;i<xPts.size();i++) {
	    XMLElement point=new XMLElement("point");
	    point.addAttribute("index",""+i);
	    
	    XMLElement xpath = new XMLElement ("xpath");
	    xpath.addElement ( (new MCalculatorXMLCreator((MCalculator)xPts.elementAt(i))).getElement ( ) );
	    point.addElement ( xpath );

	    XMLElement ypath = new XMLElement ("ypath");
	    ypath.addElement ( (new MCalculatorXMLCreator((MCalculator)yPts.elementAt(i))).getElement ( ) );
	    point.addElement ( ypath );

	    elem.addElement(point);
	}
	return elem;
    }
}
