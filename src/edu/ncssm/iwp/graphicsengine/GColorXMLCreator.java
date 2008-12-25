package edu.ncssm.iwp.graphicsengine;

import edu.ncssm.iwp.toolkit.xml.*;

public class GColorXMLCreator extends XMLCreator
{
	GColor color;
	
	public GColorXMLCreator ( GColor color )
	{
		this.color = color;
	}


	public XMLElement getElement () 
	{
		XMLElement elem = new XMLElement ( "color" );

		elem.addElement ( new XMLElement ( "red", "" + color.getRed() ));
		elem.addElement ( new XMLElement ( "green", "" + color.getGreen() ));
		elem.addElement ( new XMLElement ( "blue", "" + color.getBlue() ));

		return elem;
	}


}

