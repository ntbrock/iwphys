package edu.ncssm.iwp.graphicsengine;

import edu.ncssm.iwp.exceptions.UnknownTickException;
import edu.ncssm.iwp.exceptions.UnknownVariableException;
import edu.ncssm.iwp.math.MDataHistory;
import edu.ncssm.iwp.objects.DObject_GraphWindow;

import java.awt.Graphics;

/* This class will provide the mapping between
 * IWP's coordinates and Graphic's coordinates.
 */

/* This is only going to have basic functions for now (testing).
 * As I need more fuctionality, I'll add it.
 */

public class IWPGrapher {
    protected Graphics g;
    protected DObject_GraphWindow window;
    protected double max=0,min=0;

    public IWPGrapher(Graphics inG, DObject_GraphWindow inW) {
	g=inG;
	window=inW;
    }

    //x is time. y is value.
    public void drawLine(double x, double y, double xx, double yy) {
	checkLimits(y);
	checkLimits(yy);
	//window.setMaxY(max);
	//window.setMinY(min);
	g.drawLine(window.getDrawX( x ),window.getDrawY( -y ),
		   window.getDrawX(xx), window.getDrawY(-yy));
	
    }
    protected void checkLimits(double in) {
	if(in>max) {max=in;}
	if(in<min) {min=in;}
    }

    public Graphics getGraphics() {return g;}
    public DObject_GraphWindow getDOject_GraphWindow() {return window;}
    
    
    

    /**
     * 2006-Aug-29 brockman
     * 
     * New generic variable grapher. This is called from the GShape Grapher. 
     * 
     * @param variableName
     * @throws UnknownVariableException
     */

	
	public void drawGraphForVariable ( MDataHistory vars, String variableName,
									   int currentTick, double timeStart, double timeChange )
		throws UnknownVariableException, UnknownTickException
	{
		
        double lastT = 0;
		double lastX = 0;  		
		
		for ( int i = 0; i < currentTick; i++ ) {
			
			double x = vars.getAtTick(variableName, i);  			
			double t = (timeStart+(i*timeChange));
			
			// Can only graph beyond step 1.
			if ( i > 1 ) { 
				drawLine(lastT,lastX,t,x);
			}
			
			lastT = t;
			lastX = x;
		}
		
	}
    
}
