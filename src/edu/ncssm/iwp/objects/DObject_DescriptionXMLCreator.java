package edu.ncssm.iwp.objects;

import edu.ncssm.iwp.plugin.IWPObjectXmlCreator;
import edu.ncssm.iwp.toolkit.xml.*;

public class DObject_DescriptionXMLCreator implements IWPObjectXmlCreator
{
	DObject_Description object;

	public DObject_DescriptionXMLCreator ( DObject_Description object )
	{
		this.object = object;
	}


	public XMLElement getElement ( ) 
	{
		XMLElement elem = new XMLElement ( "description" );
			 
		elem.addElement ( new XMLElement ( "text", object.getText() ));

		return elem;
	}

	/*-----------------------------------------------------------------*/

}

