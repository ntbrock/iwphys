// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
//
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.objects;

import edu.ncssm.iwp.plugin.*;
import edu.ncssm.iwp.util.*;
import edu.ncssm.iwp.ui.*;


/**
 * It's ok for this guy not to implement IWPAnimated or IWPCalculated - he's not really
 * for either of those purposes - he's just input data, like Description.
 * @author brockman
 *
 */

public class DObject_Window extends DObject_AbstractWindow implements IWPObject, IWPXmlable, IWPDesigned
{
	public boolean drawGridNumbers = true;
	public boolean showAllDataAvailable = false;

	
    public String getIconFilename()
    {
        return "icon_DObject_Window2.gif";
    }


    public static final String OBJECT_NAME = "XY Window";

    public DObject_Window()
    {
        minX = -10;
        minY = -10;
        maxX = 10;
        maxY = 10;
    }

    public DObject_Window( double iMinX, double iMinY,
                           double iMaxX, double iMaxY )
    {
        minX = iMinX;
        minY = iMinY;
        maxX = iMaxX;
        maxY = iMaxY;
    }

    public String getName() { return OBJECT_NAME; }

    // setting the name of a window has no effect.
    public void setName( String newName) { }

    /**
     * Designed
     */

    public DObject_animator getObject_animator() {
        IWPLog.info(this,"[DObject_Window][getObject_animator] "+this.getClass().getName()+" shouldn't have called this.");
        return null;
    }

    public GAccessor_designer getDesigner ( ) {
        return new DObject_Window_designer ( this );
    }


    /**
     * Xmlable
     */


    public IWPObjectXmlCreator newXmlObjectCreator() {
        return new DObject_WindowXMLCreator(this);
    }

    public IWPObjectXmlHandler newXmlObjectHandler() {
        return new DObject_WindowXMLHandler();
    }

	public boolean isDrawGridNumbers() {
		return drawGridNumbers;
	}

	public void setDrawGridNumbers(boolean drawGridNumbers) {
		this.drawGridNumbers = drawGridNumbers;
	}

	public boolean isShowAllDataAvailable() {
		return showAllDataAvailable;
	}

	public void setShowAllDataAvailable(boolean showAllDataAvailable) {
		this.showAllDataAvailable = showAllDataAvailable;
	}


}


