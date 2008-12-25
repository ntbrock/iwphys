package edu.ncssm.iwp.graphicsengine;

import edu.ncssm.iwp.problemdb.IWPDefaultXmlHandler;

import org.xml.sax.*;
import javax.xml.parsers.*;
import java.util.*;

public class GShape_GraphPropertySelectorXMLHandler extends IWPDefaultXmlHandler
{
    
    ContentHandler parent;
    SAXParser parser;

    GShape shape;

    public void collectObject(SAXParser parser, IWPDefaultXmlHandler parent,
			      	GShape shape)
    	throws SAXException
    	
    {
    	this.parser=parser;
    	this.parent=parent;
    	this.shape=shape;

		parser.getXMLReader().setContentHandler(this);
    }

    public void cleanup() {}

    public void startElement ( String namespaceURI, String localName,
			   String qName, Attributes attr )
		throws SAXException
	{
	
    	resetContents();
    	
    	Hashtable attrs = attrListToHash(attr);
    	if(qName.equals("initiallyOn")) {
    		shape.getGShape_GraphPropertySelector().
    		setXPosSelected(mapStringToBool((String)attrs.get("xPos")));
    		shape.getGShape_GraphPropertySelector().
    		setXVelSelected(mapStringToBool((String)attrs.get("xVel")));
    		shape.getGShape_GraphPropertySelector().
    		setXAccelSelected(mapStringToBool((String)attrs.get("xAccel")));
    		shape.getGShape_GraphPropertySelector().
    		setYPosSelected(mapStringToBool((String)attrs.get("yPos")));
    		shape.getGShape_GraphPropertySelector().
			setYVelSelected(mapStringToBool((String)attrs.get("yVel")));
    		shape.getGShape_GraphPropertySelector().
    		setYAccelSelected(mapStringToBool((String)attrs.get("yAccel")));
    	}
    }
    
    public void endElement(String namespaceURI, String localName,
    		String qName)
		throws SAXException
	{
    	if(qName.equals("initiallyOn")) {
    		cleanup();
    		parser.getXMLReader().setContentHandler(parent);
    	}
    }
    

    protected boolean mapStringToBool(String in) {
    	if(in.equals("false")) {return false;}
		return true;
    }
}
