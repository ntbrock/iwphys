
package edu.ncssm.iwp.objects;

import edu.ncssm.iwp.plugin.IWPObjectXmlCreator;
import edu.ncssm.iwp.toolkit.xml.*;



public class DObject_InputXMLCreator implements IWPObjectXmlCreator
{

	DObject_Input object;
	

	DObject_InputXMLCreator ( DObject_Input object )
	{
		this.object = object;
	}


	public XMLElement getElement ( ) 
	{
		XMLElement elem = new XMLElement ( "input" );
		elem.addAttribute ( "calculationOrder", object.getCalculatorOrder()+"" );

		elem.addElement ( new XMLElement ( "name", object.getName() ));
		elem.addElement ( new XMLElement ( "text", object.getText() ));
		elem.addElement ( new XMLElement ( "initialValue", "" + object.getInitialValue() ));
		elem.addElement ( new XMLElement ( "units", object.getUnits() ));

		if ( ! object.isVisible() ) {
			elem.addElement ( new XMLElement ( "hidden" , "1" ) );
		}

		return elem;
	}

	/*-----------------------------------------------------------------*/

}

