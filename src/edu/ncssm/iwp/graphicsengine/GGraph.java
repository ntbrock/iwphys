// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
//
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.graphicsengine;

import edu.ncssm.iwp.plugin.IWPAnimated;
import edu.ncssm.iwp.plugin.IWPGraphable;
import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.ui.*;
import edu.ncssm.iwp.objects.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;

import edu.ncssm.iwp.util.*;

/**
 * This is the canvas that plots the graph lines.
 * @author brockman
 *
 */

public class GGraph extends JPanel
	implements IWPAnimated
{
	private static final long serialVersionUID = 1L;
	// I'm saving this because I have to update objects in the problem Async - GraphWindow.
	// I don't want the graph window to reset when I hit reset on the problem, just reload,
	// so I cannot store the variables just in the GGraph. GGraph always reads from the problem objct.
	// brockman 2006-Aug-28
	protected DProblemState lastTickState = null;

    DObject_GraphWindow window;
    


    public GGraph ( GWindow_Animator iParent )
    {
        setBackground(Color.WHITE);
    }


    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g)
    {
        int width = this.getSize().width;
        int height = this.getSize().height;

        //draw background
        g.setColor(Color.WHITE);
        g.fillRect(0,0,width,height);

        //grid,, lines, etc

        if ( window != null ) {
            window.setWidth(width);
            window.setHeight(height);
            window.drawMe(g);
        } else {
            IWPLog.error(this,"Window object is NULL!");
        }

        int paintedCount = 0;
       
        Collection objects = lastTickState.getProblem().getObjectsForDrawing();
        IWPGrapher drawingWorker=new IWPGrapher(g,window);

        for ( Iterator i = objects.iterator(); i.hasNext(); ) {
            Object currentObject = (Object) i.next();
            
            if(currentObject instanceof IWPGraphable) {
                
            	Color oldColor = g.getColor();
            	g.setColor( ((IWPGraphable)currentObject).getGColor().getAWTColor() );
            	
            	try { 
            		((IWPGraphable)currentObject).iwpGraph(drawingWorker,lastTickState);
            		paintedCount++;
            	} catch ( Exception e ) {
            		IWPLogPopup.x(this,"Paint Exception", e);
            	}
            	
            	g.setColor(oldColor);
            }
        }

        IWPLog.debug(this, "[paint] tick: " + lastTickState.getCurrentTick() + "  paintedCount: " + paintedCount );
    }

    

    /**
     * Brockman 2006-Apr-29. I'm not sure that just captuting the 
     * state to draw stuff is the best way, but its' the way that
     * it was done before, so I'm going to stick w/ it.
     */

    public void tick ( DProblemState state )
    {
        this.lastTickState = state;
        repaint();
    }

    
    public void zero ( DProblem problem, DProblemState state )
    {
        this.lastTickState = state;
    	this.window = problem.getGraphWindowObject();
    }

    public DObject_AbstractWindow getWindow() {
    	return window;
    }

    
    

    
    
    public void updateGraphWindowObjectAndTick ( double minX, double maxX,
    									  	double minY, double maxY,
    									  	double gridX, double gridY )
    {
    	
    
    	if ( lastTickState != null ) { 
    		
    		DProblem currentProblem = lastTickState.getProblem();
    		currentProblem.getGraphWindowObject().setMinX(minX);
    		currentProblem.getGraphWindowObject().setMaxX(maxX);
    		currentProblem.getGraphWindowObject().setMinY(minY);
    		currentProblem.getGraphWindowObject().setMaxY(maxY);
    		currentProblem.getGraphWindowObject().setGridX(gridX);
    		currentProblem.getGraphWindowObject().setGridY(gridY);

    		// update my new
    		tick(lastTickState);
    	}
    }


}



