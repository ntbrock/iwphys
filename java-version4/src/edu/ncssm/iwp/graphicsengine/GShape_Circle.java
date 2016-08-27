// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
//
// For full copyright information, see main source file,
// "iwp.java"


package edu.ncssm.iwp.graphicsengine;

import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.objects.*;
import edu.ncssm.iwp.exceptions.*;

public class GShape_Circle extends GShape
{
	private static final long serialVersionUID = 1L;
    public GShape_Circle ( DObject_Solid solid )
    {
        super( solid, "1", "1" ); // default is a 1x1 circle.
    }

    public GShape_Circle ( DObject_Solid solid,
    					   String widthStr, String heightStr )
    {
        super( solid, widthStr, heightStr );
    }

    public String getType() { return GShape.STRING_CIRCLE; }
    public String getParams() { return ""; }
    
    public void iwpDraw ( IWPDrawer g,
    					  DProblemState state )
    	throws InvalidEquationException, UnknownVariableException, UnknownTickException
    {

        double w = super.getSolidWidth(state);
        double h = super.getSolidHeight(state);
        double x = super.getSolidX(state);
        double y = super.getSolidY(state);
        
        //g.fillOval((x-w/2),(y-h/2),(x+w/2)-(x-w/2),(y+h/2)-(y-h/2));
        g.fillOval((x-w/2),(y+h/2),w,h);

        // super call. This shows object trails if selected.
        commonDraw ( g, state);
    }
}
