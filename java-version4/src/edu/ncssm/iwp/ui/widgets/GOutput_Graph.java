// Graphing object. Takes an DObject_Output as input, and will create a JPanel
// containing a graph of y vs. x
//
// should also be able to have a spreadsheet view

// I think some other developer started this file,
// and didn't quite finish. brock 03/08/02


package edu.ncssm.iwp.ui.widgets;

import edu.ncssm.iwp.objects.*;
import edu.ncssm.iwp.math.*;

import java.awt.*;

public class GOutput_Graph extends GOutput
{
	private static final long serialVersionUID = 1L;
	DObject_Output graphObject;
	double range;
	double domain; //how long a time period you want to use
	double currtime;
	double resolution; //how many "slices" you want to cut each second into

	int width, height; //current dimensions of the window

	double maxX, maxY, minX, minY;

	private Graphics g; //graphics object. Lets me not pass it

	public GOutput_Graph (DObject_Output iObject) {
		graphObject = iObject;
		range = 20;
		domain = 20;
		resolution = 10;
		setupGraph();
	}

	public GOutput_Graph (DObject_Output iObject, double iresolution,
						  double idomain, double irange) {
		graphObject = iObject;
		range = irange;
		domain = idomain;
		resolution = iresolution;
		setupGraph();
	}

	public void setupGraph() {
		maxX = domain/2;
		minX = -1 * maxX;
		maxY = range/2;
		minY = -1 * maxY;
		width = this.getSize().width;
		height = this.getSize().height;
	}


	public double getX (double iX) {
		return (int) (((iX - minX)/domain * (double)width));
	}

	public double getY (double iY) {
		return (int) (((minY + iY)/range * (double) height));
	}

	public void drawPoint (double iX, double iY) { //fakes drawing a point
		g.drawRect ( (int)getX(iX), (int)getY(iY), 1, 1);
	}

	public void drawLine (double iX1, double iY1, double iX2, double iY2) {
		int tx1, tx2, ty1, ty2;
		tx1 = (int) getX (iX1);
		tx2 = (int) getX (iX2);
		ty1 = (int) getY (iY1);
		ty2 = (int) getY (iY2);
		g.drawLine(tx1, ty1, tx2, ty2);
	}

	public void drawAxes () {
		drawLine (0, minY, 0, maxY);
		drawLine (minX, 0, maxX, 0);
	}

  	public void paint (Graphics ig) {
  		//int width = this.getSize().width;
  		//int height = this.getSize().height;
  		g = ig;
  		drawAxes();
  		for (double distime = currtime; ((distime < (currtime - range)) || distime < 0); distime -= 1/resolution) {
  			
  			// 2006-Apr-29 Brockman
  			// TODO This should really use the history that's alrady recorded in
  			// the MVariablesImpl in the DProblemState.
  			//we construct a thought at an old time to pass to the calculator

			// get teh displacement
			MCalculator yCalc = graphObject.getCalculator ();
			double value = yCalc.getDisp();

  			if (value < maxY && value > minY) {
  				//eventually let people choose whether to draw points or lines
  				drawPoint(distime, value);
  			}
  		}
	}
	
}

		
		









