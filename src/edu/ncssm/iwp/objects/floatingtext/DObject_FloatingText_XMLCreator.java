package edu.ncssm.iwp.objects.floatingtext;

import edu.ncssm.iwp.graphicsengine.*;
import edu.ncssm.iwp.math.*;
import edu.ncssm.iwp.plugin.IWPObjectXmlCreator;
import edu.ncssm.iwp.toolkit.xml.XMLElement;

/**
 * Example XMLCreator for new plugin objects.
 * @author brockman
 *
 */

public class DObject_FloatingText_XMLCreator implements IWPObjectXmlCreator
{
	
	DObject_FloatingText parent;
	
	public DObject_FloatingText_XMLCreator ( DObject_FloatingText parent )
	{
		this.parent = parent;
	}
	
	public XMLElement getElement()
	{
		XMLElement elem = new XMLElement ( "object" );
		elem.addAttribute("class", parent.getClass().getName());
		
		elem.addElement ( new XMLElement ( "name", parent.getName() ));
		elem.addElement ( new XMLElement ( "text", parent.getText() ));
		elem.addElement ( new XMLElement ( "units", parent.getUnits() ));

		XMLElement value = new XMLElement("value");
		value.addElement( new MCalculatorXMLCreator(parent.getValue()).getElement() );
		elem.addElement(value);
		
		elem.addElement ( new XMLElement ( "fontSize", "" + parent.getFontSize() ));
		elem.addElement ( new XMLElement ( "showValue", parent.getShowValue() ? "true" : "false" ));
		
		elem.addElement ( (new GColorXMLCreator(parent.getFontColor())).getElement() );
		
		
		XMLElement ypath = new XMLElement("ypath");
		ypath.addElement( new MCalculatorXMLCreator(parent.getYpath()).getElement() );
		elem.addElement(ypath);
		
		XMLElement xpath = new XMLElement("xpath");
		xpath.addElement( new MCalculatorXMLCreator(parent.getXpath()).getElement() );
		elem.addElement(xpath);
		
		return elem;
	}
	
}
