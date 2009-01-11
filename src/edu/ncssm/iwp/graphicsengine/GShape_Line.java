// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
//
// For full copyright information, see main source file,
// "iwp.java"


// 2004.09.18: Added the line object

package edu.ncssm.iwp.graphicsengine;

import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.objects.*;
import edu.ncssm.iwp.exceptions.*;

/**
 * Very similar to GShape_Vector. Use Vector if you want
 * arrow heads.
 * 
 * @author brockman
 *
 */

public class GShape_Line extends GShape
{
	private static final long serialVersionUID = 1L;
    int lineThickness = 1;   // Default of 1, means just a simple Swing line drae

    
    public GShape_Line ( DObject_Solid solid )
    {
        super( solid, "1", "1" ); // default is a 1x1 line.
    }

    public GShape_Line ( DObject_Solid solid,
    					   String widthStr, String heightStr )
    {
        super( solid, widthStr, heightStr );
    }

    public String getType() { return GShape.STRING_LINE; }
    public String getParams() { return ""; }
    
    
    
    public void iwpDraw ( IWPDrawer g,
    					  DProblemState state )
    	throws InvalidEquationException, UnknownVariableException, UnknownTickException
    {

        double w = super.getSolidWidth(state);
        double h = super.getSolidHeight(state);
    
        this.iwpDraw(g, state, w, h);
    }
    
    /**
     * This method is invoked by the GShape_Vector subclass. It wants to use an 
     * explicit line width + height reather than the one of the solid. In the vector case,
     * I believe the solid is the attached object.
     * Brockman 2007-Jun-04.
     * 
     * @param g
     * @param state
     * @param w
     * @param h
     * @throws InvalidEquationException
     * @throws UnknownVariableException
     * @throws UnknownTickException
     */
    
    protected void iwpDraw ( IWPDrawer g,
    		DProblemState state, double w, double h )
    throws InvalidEquationException, UnknownVariableException, UnknownTickException
    {        
        double x = super.getSolidX(state);
        double y = super.getSolidY(state);
        
        // We have all the data, now we just need to draw it on the screen.
        g.drawLine ( x, y, x + w, y + h );

        // TODO: Implement Thickness drawing.

        // super call. This shows object trails if selected.
        commonDraw ( g, state);
    }
}

