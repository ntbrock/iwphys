// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
//
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.objects;

import edu.ncssm.iwp.plugin.*;
import edu.ncssm.iwp.util.*;
import edu.ncssm.iwp.ui.*;
import edu.ncssm.iwp.problemdb.DProblem_designer;


public class DObject_GraphWindow extends DObject_AbstractWindow
 implements IWPObject, IWPXmlable, IWPDesigned
{
	
	public String getIconFilename()
	{
		return "icon_DObject_Window.gif";
	}
	
	
	
    //VARIABLES
	String name;
	
	
	
    //Constructors
	

    public DObject_GraphWindow()
    {
        setName("Graph Window");

        minX = 0;
        minY = -10;
        maxX = 10;
        maxY = 10;
    }

    public DObject_GraphWindow( double iMinX, double iMinY,
                           double iMaxX, double iMaxY )
    {
    	setName("Graph Window");

        minX = iMinX;
        minY = iMinY;
        maxX = iMaxX;
        maxY = iMaxY;
    }


    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
    
    /*----------------------------------------------------------------------*/
    /* DESIGNERS */
    /*----------------------------------------------------------------------*/


	public DObject_animator getObject_animator() {
        IWPLog.info(this,"[DObject_GraphWindow][getObject_animator] "+this.getClass().getName()+" shouldn't have called this.");
        return null;
    }

    public GAccessor_designer getDesigner ( ) {
        return new DObject_GraphWindow_designer ( this );
    }

    

	public IWPObjectXmlCreator newXmlObjectCreator()
	{
		return new DObject_GraphWindowXMLCreator(this);
	}

	public IWPObjectXmlHandler newXmlObjectHandler()
	{
		return new DObject_GraphWindowXMLHandler();
	}
}


