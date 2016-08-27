package edu.ncssm.iwp.graphicsengine;

import edu.ncssm.iwp.toolkit.xml.*;

public class GShape_GraphPropertySelectorXMLCreator extends XMLCreator {

    GShape_GraphPropertySelector gps;

    public GShape_GraphPropertySelectorXMLCreator(GShape_GraphPropertySelector in) {
	gps=in;
    }

    public XMLElement getElement() {

	XMLElement properties=new XMLElement("initiallyOn");
	properties.addAttribute("xPos",""+gps.xPosSelected());
	properties.addAttribute("xVel",""+gps.xVelSelected());
	properties.addAttribute("xAccel",""+gps.xAccelSelected());
	properties.addAttribute("yPos",""+gps.yPosSelected());
	properties.addAttribute("yVel",""+gps.yVelSelected());
	properties.addAttribute("yAccel",""+gps.yAccelSelected());
	return properties;
    }
}
