// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
//
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.graphicsengine;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JPanel;

import edu.ncssm.iwp.objects.DObject_Window;
import edu.ncssm.iwp.plugin.IWPAnimated;
import edu.ncssm.iwp.plugin.IWPDrawable;
import edu.ncssm.iwp.problemdb.DProblem;
import edu.ncssm.iwp.problemdb.DProblemState;
import edu.ncssm.iwp.ui.GWindow_Animator;
import edu.ncssm.iwp.util.IWPLog;

/**
 * GRender is the component that handles all of the drawing of the 
 * animation state onto the canvas.
 * 
 * in IWP3, this guy got more pixels by default.
 * 
 * @author brockman
 *
 */

public class GRender extends JPanel
	implements IWPAnimated
{
	private static final long serialVersionUID = 1L;
	// IWP3 has this set bigger.
	public static final int WIDTH = 475;
	public static final int HEIGHT = 475;
	
    DObject_Window window;
    DProblemState lastCompletedStateCopy;
    GWindow_Animator parent;

    public GRender ( GWindow_Animator parent )
    {
        this.parent = parent;
        setBackground(Color.WHITE );
    }


    public void update(Graphics g)
    {
        paint(g);
    }

    public void paint(Graphics g)
    {
    	// Don't
    	
    	if ( lastCompletedStateCopy == null ) { 
    		// WARNING, the animation hasn't started yet (there's been no thinks).
    		// should the masterReset, think(0)?
    		IWPLog.info(this,"[GRender] Drawing nothing, state = null");
    		return;
    	}

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
            IWPLog.info(this,"[GRender.paint] WARNING: Window object is NULL!");
        }


        //need to pull out all the object data and do the processing
        //here ourseves!
        //draw the crosshatches + grid on the og.
        Collection objects = parent.getProblem().getObjectsForDrawing();
        Iterator i = objects.iterator();

        IWPDrawer worker = new IWPDrawer(g,window);

        while ( i.hasNext() ) {

            Object currentObject = (Object) i.next();
            if(currentObject instanceof IWPDrawable) {
            	
            	GColor iwpgColor = ((IWPDrawable)currentObject).getGColor();
            	Color oldColor = g.getColor();
            	
            	if ( iwpgColor != null ) {
            		g.setColor( iwpgColor.getAWTColor() );
            	}
            	
                try { 
                	((IWPDrawable)currentObject).iwpDraw(worker,lastCompletedStateCopy);
                } catch ( Exception e ) { 
                	IWPLog.x(e.getMessage(), e);
                	// This can cause Swing Threadlocking. 2007-Jun-03 brockman
                	//parent.visualException(e);
                }
                
                // reset old color back
                g.setColor(oldColor);
            }
        }
        //finalize
    }


    public void zero ( DProblem problem, DProblemState state )
    { 
    	this.window = problem.getWindowObject();
    	tick ( state );
    }

    /**
     * Brockman 2006-Apr-29. I am going to be cloning the state so
     * that the Animator doesn't draw in the middle of a state tick.
     * 
     * This is the weird 'jumping' problems that have been reported in 
     * previous versions. 
     * 
     * IN the master tick thread, I tick all of the solids, and then I tick
     * the render panels. If the users redraws mid-calculate, he's going ot 
     * see last tick's picture.
     */
    public void tick( DProblemState state )
    {
    	// 2006-Oct-01. Trying to get window to change on the fly.
    	this.window = state.getProblem().getWindowObject();
    	
    	// 2007-Jun-03 brockman. Trying to prevent data corruption in multithread env.
    	while ( state.isTickInProgress() ) {
    		Thread.yield();
    	}

    	this.lastCompletedStateCopy = state.copyOfButPreservePointers();
    }

}



