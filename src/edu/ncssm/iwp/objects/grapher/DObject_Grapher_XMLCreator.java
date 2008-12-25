package edu.ncssm.iwp.objects.grapher;

import edu.ncssm.iwp.graphicsengine.*;
import edu.ncssm.iwp.math.*;
import edu.ncssm.iwp.plugin.IWPObjectXmlCreator;
import edu.ncssm.iwp.toolkit.xml.XMLElement;

/**
 * Example XMLCreator for new plugin objects.
 * @author brockman
 *
 */

public class DObject_Grapher_XMLCreator implements IWPObjectXmlCreator
{
	
	DObject_Grapher parent;
	
	public DObject_Grapher_XMLCreator ( DObject_Grapher parent )
	{
		this.parent = parent;
	}
	
	public XMLElement getElement()
	{
		XMLElement elem = new XMLElement ( "object" );
		elem.addAttribute("class", parent.getClass().getName());
		
		elem.addElement ( new XMLElement ( "name", parent.getName() ));
		elem.addElement ( new XMLElement ( "equation"	, parent.getEquation()	));
		elem.addElement ( new XMLElement ( "boxX"	, ""+ parent.getBoxX()	));
		elem.addElement ( new XMLElement ( "boxY"	, ""+ parent.getBoxY()	));
		elem.addElement ( new XMLElement ( "boxW"	, ""+ parent.getBoxW()	));
		elem.addElement ( new XMLElement ( "boxH"	, ""+ parent.getBoxH()	));
		elem.addElement ( new XMLElement ( "res" 	, ""+ parent.getRes()	));
		elem.addElement	( new XMLElement ( "stroke" , ""+ parent.getStroke()));
		
		
		elem.addElement ( new XMLElement ( "showBounding", 		parent.getShowBounding() ? "true" : "false" ));
		elem.addElement ( new XMLElement ( "transformCoords", 	parent.getTransformCoords() ? "true" : "false" ));
		
		elem.addElement ( (new GColorXMLCreator(parent.getFontColor())).getElement() );
		
		

		return elem;
	}
	
}
