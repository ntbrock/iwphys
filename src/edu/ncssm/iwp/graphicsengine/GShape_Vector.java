package edu.ncssm.iwp.graphicsengine;

import edu.ncssm.iwp.exceptions.*;
import edu.ncssm.iwp.graphicsengine.GShape_Line;
import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.objects.*;

import java.awt.*;
import java.lang.Math;

public class GShape_Vector extends GShape_Line
{
   private Color vectorHeadColorOverrde;

	int lineThickness = 1;   // Default of 1, means just a simple Swing line drae
    
    public GShape_Vector ( DObject_Solid solid )
    {
        super( solid, "1", "1" ); // default is a 1x1 line.
    }

    public GShape_Vector ( DObject_Solid solid,
    					   String widthStr, String heightStr )
    {
        super( solid, widthStr, heightStr );
    }

    public String getType() { return GShape.STRING_VECTOR; }
    public String getParams() { return ""; }
    
   
    /**
     * Optionally set this color to override the color of the vector head.
     * @param override
     */
    public void setHeadColorOverride( Color override ) { 
    	this.vectorHeadColorOverrde = override;
    }
    
    
    public void iwpDraw ( IWPDrawer g,
    					  DProblemState state )
    	throws InvalidEquationException, UnknownVariableException, UnknownTickException
    {
    	
    	//2007-Jun-04 brockman IWP3 - In here, getting this to work without the datahistory concept.
    	double w = this.calcWidth(state); 
        double h = this.calcHeight(state);
        
        double x = super.getSolidX(state);
        double y = super.getSolidY(state);
        

        // since we're drawing this in a different color, save the old color.
        
        Color previousColor = g.getColor();
        
        if ( vectorHeadColorOverrde != null ) { 
        	g.setColor(vectorHeadColorOverrde);//needed for component vectors to appear in preset colors
        }
       
        double gridX = state.getProblem().getWindowObject().getGridX();
        double gridY = state.getProblem().getWindowObject().getGridY();
        double gridLength = Math.sqrt((double)(gridX*gridX + gridY*gridY));

        double length = Math.sqrt((double)(w*w+h*h));
        double headLength;

        if (length > gridLength)
        {
            headLength = gridLength;
            /*w = gridX;
            h = gridY;*/
        }
        else
        {
            headLength = length;
        }
        if (length != 0)
        {
            double headWidth = headLength*(w/length*0.866+h/(2*length))/5;
            double headHeight = headLength*(h/length*0.866-w/(2*length))/5;

            g.drawLine(x+w, y+h, x+w-headWidth, y+h-headHeight);

            headWidth = headLength*(h/length*0.866+w/(2*length))/5;
            headHeight = headLength*(w/length*0.866-h/(2*length))/5;

            g.drawLine(x+w, y+h, x+w-headHeight, y+h-headWidth);
        }
        g.setColor(previousColor);

        
        // Draw the line too.
        super.iwpDraw(g, state, w, h);
        
        // super call. This shows object trails if selected.
        commonDraw ( g, state);
    }
    
    
}
