// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
//
// For full copyright information, see main source file,
// "iwp.java"


package edu.ncssm.iwp.graphicsengine;

import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.objects.*;
import edu.ncssm.iwp.exceptions.*;


public class GShape_Rectangle extends GShape
{

	public GShape_Rectangle ( DObject_Solid solid )
    {
        super( solid, "1", "1", "0" ); // default is a 1x1 circle.
    }

    public GShape_Rectangle ( DObject_Solid solid,
    					      String widthStr, String heightStr, String angleStr )
    {
        super( solid, widthStr, heightStr, angleStr );
    }

    public String getType() { return GShape.STRING_RECTANGLE; }
    public String getParams() { return ""; }
    
    public void iwpDraw ( IWPDrawer g,
    					  DProblemState state )
    	throws InvalidEquationException, UnknownVariableException, UnknownTickException
    {

    	double w = super.getSolidWidth(state);
        double h = super.getSolidHeight(state);
        double a = super.getSolidAngle(state);
        
        double x = super.getSolidX(state);
        double y = super.getSolidY(state);
        
        
        // draw it.
        double posX=(x-w/2);
        double posY=(y+h/2);
        g.fillRect(posX,posY,w,h,a);

        // super call. This shows object trails if selected.
        commonDraw ( g, state);
    }
    
}

