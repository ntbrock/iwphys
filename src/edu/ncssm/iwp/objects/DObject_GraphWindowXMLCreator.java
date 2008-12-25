package edu.ncssm.iwp.objects;

import edu.ncssm.iwp.plugin.IWPObjectXmlCreator;
import edu.ncssm.iwp.toolkit.xml.*;


public class DObject_GraphWindowXMLCreator implements IWPObjectXmlCreator
{

	DObject_GraphWindow object;
	
	public DObject_GraphWindowXMLCreator ( DObject_GraphWindow object )
	{
		this.object = object;
	}

	public XMLElement getElement ( ) 
	{
		XMLElement elem = new XMLElement ( "GraphWindow" );
			 
		elem.addElement ( new XMLElement ( "xmin", "" + object.getMinX() ));
		elem.addElement ( new XMLElement ( "xmax", "" + object.getMaxX() ));
		elem.addElement ( new XMLElement ( "ymin", "" + object.getMinY() ));
		elem.addElement ( new XMLElement ( "ymax", "" + object.getMaxY() ));
		elem.addElement ( new XMLElement ( "xgrid", "" + object.getGridX() ));
		elem.addElement ( new XMLElement ( "ygrid", "" + object.getGridY() ));


		return elem;
	}


}

