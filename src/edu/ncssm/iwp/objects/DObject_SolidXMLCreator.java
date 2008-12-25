
package edu.ncssm.iwp.objects;

import edu.ncssm.iwp.math.*;
import edu.ncssm.iwp.plugin.IWPObjectXmlCreator;
import edu.ncssm.iwp.graphicsengine.*;
import edu.ncssm.iwp.toolkit.xml.*;


public class DObject_SolidXMLCreator implements IWPObjectXmlCreator
{
	DObject_Solid object;

	GShapeXMLCreator shapeCreator;
	GColorXMLCreator colorCreator;
	MCalculatorXMLCreator xCalcCreator;
	MCalculatorXMLCreator yCalcCreator;


	public DObject_SolidXMLCreator ( DObject_Solid object )
	{
		this.object = object;
	}


	public XMLElement getElement ( ) 
	{
		shapeCreator = new GShapeXMLCreator ( object.getShape() );
		colorCreator = new GColorXMLCreator ( object.getGColor() );
		xCalcCreator = new MCalculatorXMLCreator ( object.getCalcX() );
		yCalcCreator = new MCalculatorXMLCreator ( object.getCalcY() );


		XMLElement elem = new XMLElement ( "solid" );

		// name 
		elem.addElement ( new XMLElement ( "name", object.getName() ));

		// shape
		elem.addElement ( shapeCreator.getElement () );

		// color
		elem.addElement ( colorCreator.getElement () );

		// xpath 
		XMLElement xpath = new XMLElement ("xpath");
		xpath.addElement ( xCalcCreator.getElement ( ) );
		elem.addElement ( xpath );

		// ypath
		XMLElement ypath = new XMLElement ("ypath");
		ypath.addElement ( yCalcCreator.getElement() );
		elem.addElement ( ypath );





		return elem;
	}

	/*-----------------------------------------------------------------*/

}

