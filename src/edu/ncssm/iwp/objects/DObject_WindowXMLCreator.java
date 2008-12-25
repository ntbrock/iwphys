package edu.ncssm.iwp.objects;

import edu.ncssm.iwp.toolkit.xml.*;
import edu.ncssm.iwp.plugin.IWPObjectXmlCreator;

public class DObject_WindowXMLCreator implements IWPObjectXmlCreator
{

	DObject_Window object;
	
	public DObject_WindowXMLCreator ( DObject_Window object )
	{
		this.object = object;
	}

	public XMLElement getElement ( ) 
	{
		XMLElement elem = new XMLElement ( "window" );
			 
		elem.addElement ( new XMLElement ( "xmin", "" + object.getMinX() ));
		elem.addElement ( new XMLElement ( "xmax", "" + object.getMaxX() ));
		elem.addElement ( new XMLElement ( "ymin", "" + object.getMinY() ));
		elem.addElement ( new XMLElement ( "ymax", "" + object.getMaxY() ));
		elem.addElement ( new XMLElement ( "xgrid", "" + object.getGridX() ));
		elem.addElement ( new XMLElement ( "ygrid", "" + object.getGridY() ));
		elem.addElement ( new XMLElement ( "xunit", "" + object.getUnitX() ));
		elem.addElement ( new XMLElement ( "yunit", "" + object.getUnitY() ));
		
		elem.addElement ( new XMLElement ( "showAllDataAvailable", "" + object.isShowAllDataAvailable() ));
		elem.addElement ( new XMLElement ( "drawGridNumbers", "" + object.isDrawGridNumbers() ));

		return elem;
	}


}

