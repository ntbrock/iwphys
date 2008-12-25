//Interactive Web Physics (IWP)
//Copyright (C) 1999 Nathaniel T. Brockman
//
//For full copyright information, see main source file,
//"iwp.java"


//2004.09.18: Added the line object

package edu.ncssm.iwp.graphicsengine;

import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.objects.*;
import edu.ncssm.iwp.exceptions.*;
import edu.ncssm.iwp.math.*;

import java.util.Vector;


public class GShape_Polygon extends GShape
{
	/*
	 * These vectors hold the point calcuators for the polygon.
	 */
	Vector xPointCalcs;
    Vector yPointCalcs;

    
	/**
	 * Polygon is the first Shape where height + width breaks
	 * down.
	 * @param solid
	 */
	public GShape_Polygon ( DObject_Solid solid )
	{
		super( solid, "1", "1" ); // default is a 1x1 poly
		constructDefaultPoints();
	}

	public GShape_Polygon ( DObject_Solid solid,
			String widthStr, String heightStr )
	{	
		super( solid, widthStr, heightStr );
		constructDefaultPoints();
	}
	

	public String getType() { return GShape.STRING_POLYGON; }
	public String getParams() { return ""; }
 

	/**
	 * Polygon specific, add a new polygon point.
	 * @param g
	 * @param lastTickState
	 * @throws InvalidEquationException
	 * @throws UnknownVariableException
	 */
	
	private void constructDefaultPoints ( )
	{
		xPointCalcs = new Vector(3);
        yPointCalcs = new Vector(3);

        // really simple 1x1 triangle.
        xPointCalcs.add(new MCalculator_Parametric( "0" ));
        yPointCalcs.add(new MCalculator_Parametric( "0" ));
        
        xPointCalcs.add(new MCalculator_Parametric( "1" ));
        yPointCalcs.add(new MCalculator_Parametric( "0" ));
        
        xPointCalcs.add(new MCalculator_Parametric( "0" ));
        yPointCalcs.add(new MCalculator_Parametric( "1" ));
	}
	
	
    public void setXPointCalc(MCalculator in,int index)
    {
    	if(index<xPointCalcs.size()) {
            xPointCalcs.remove(index);
            xPointCalcs.add(index,in);
        } else {
            xPointCalcs.add(index,in);
        }
    }
    
    public void setYPointCalc (MCalculator in, int index)
    {
        if(index<yPointCalcs.size()) {
            yPointCalcs.remove(index);
            yPointCalcs.add(index,in);
        } else {
        	yPointCalcs.add(index,in);
        }
    }


    public Vector getXPointCalcs() {return xPointCalcs;}
    public Vector getYPointCalcs() {return yPointCalcs;}

    
    /**
     * The draw routine.
     * @param g
     * @param state
     * @throws InvalidEquationException
     * @throws UnknownVariableException
     */
	
	
	public void iwpDraw ( IWPDrawer g,
						  DProblemState state )
 		throws InvalidEquationException, UnknownVariableException, UnknownTickException
     {
        double x = super.getSolidX(state);
        double y = super.getSolidY(state);
        
        
		// TODO scale the point values by the width / height?
        // double w = super.getSolidWidth(state);
        // double h = super.getSolidHeight(state);
        
        
		//this isn't the REAL impl. of Polygon, but this should be more precise
	    int numberOfPoints=xPointCalcs.size();
	    
	    double[] xPointsValues=new double[numberOfPoints];
	    double[] yPointsValues=new double[numberOfPoints];
	    
	    for( int i=0; i<numberOfPoints; i++) {
	    	xPointsValues[i]=x+((MCalculator)xPointCalcs.elementAt(i)).calculate(state.vars());
	        yPointsValues[i]=y+((MCalculator)yPointCalcs.elementAt(i)).calculate(state.vars());
	    }

	    g.fillPolygon(xPointsValues,yPointsValues,numberOfPoints);

	    commonDraw ( g, state);
     }
	
	
	//Special zer for polygons
    public void zero ( DProblem problem, DProblemState state )
	throws UnknownVariableException, InvalidEquationException, UnknownTickException
	{
    	getWidthCalculator().zero(state.vars());
    	getHeightCalculator().zero(state.vars());
    	//getAngleCalculator().zero(state.vars());
	}	

}



