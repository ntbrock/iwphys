package edu.ncssm.iwp.graphicsengine;

import java.awt.Color;

import edu.ncssm.iwp.exceptions.InvalidEquationException;
import edu.ncssm.iwp.exceptions.UnknownTickException;
import edu.ncssm.iwp.exceptions.UnknownVariableException;
import edu.ncssm.iwp.math.MVariables;
import edu.ncssm.iwp.objects.DObject_Solid;
import edu.ncssm.iwp.problemdb.DProblemState;

/**
 * Handles the rendering of attached vectors. Used to live in GShape, but I moved out to here
 * for simplicity and separation
 * 
 * 2007-Jun-06 brockman
 * 
 * @author brockman
 *
 */

public class GShape_AttachedVectorsRenderHelper
{

	public static final Color velocityVectorColor = Color.blue;
	public static final Color accelerationVectorColor = Color.red;
	
	
	
	protected void draw( GShape shape, IWPDrawer g, DProblemState state )
   		throws InvalidEquationException, UnknownVariableException, UnknownTickException
   	{
		GShape_VectorSelector vectorSelector = shape.getVectorSelector();
		DObject_Solid solid = shape.getSolid();
		
		
		if(vectorSelector.xVelSelected()) {
			double vectorWidth = 0, vectorHeight = 0;

			vectorWidth = state.vars().getAtCurrentTick(solid.getName() + "." + MVariables.XVEL );
    	
			GShape_Vector velVector = new GShape_Vector(solid,String.valueOf(vectorWidth), String.valueOf(vectorHeight) );
			velVector.setHeadColorOverride(velocityVectorColor);
			velVector.iwpDraw(g, state );
		}
		
		
		if(vectorSelector.yVelSelected()) {
			double vectorWidth = 0, vectorHeight = 0;
    		
			vectorHeight = state.vars().getAtCurrentTick(solid.getName() + "." + MVariables.YVEL );
    		
			GShape_Vector velVector = new GShape_Vector(solid,String.valueOf(vectorWidth), String.valueOf(vectorHeight));
			velVector.setHeadColorOverride(velocityVectorColor);
			velVector.iwpDraw(g, state);
		}
		
		
		if(vectorSelector.VelSelected()) {
			double vectorWidth = 0, vectorHeight = 0;
			vectorWidth = state.vars().getAtCurrentTick(solid.getName() + "." + MVariables.XVEL );
			vectorHeight = state.vars().getAtCurrentTick(solid.getName() + "." + MVariables.YVEL );
    	
			GShape_Vector velVector = new GShape_Vector(solid,String.valueOf(vectorWidth), String.valueOf(vectorHeight));
			velVector.setHeadColorOverride(velocityVectorColor);
			velVector.iwpDraw(g, state);
		}
		
		
		if(vectorSelector.xAccelSelected()) {
    		double vectorWidth = 0, vectorHeight = 0;
    		vectorWidth = state.vars().getAtCurrentTick(solid.getName() + "." + MVariables.XACCEL );
    	
    		GShape_Vector velVector = new GShape_Vector(solid,String.valueOf(vectorWidth), String.valueOf(vectorHeight));
    		velVector.setHeadColorOverride(accelerationVectorColor);
    		velVector.iwpDraw(g, state);
		}
		
		if(vectorSelector.yAccelSelected()) {
			double vectorWidth = 0, vectorHeight = 0;
			vectorHeight = state.vars().getAtCurrentTick(solid.getName() + "." + MVariables.YACCEL );
    	
			GShape_Vector velVector = new GShape_Vector(solid,String.valueOf(vectorWidth), String.valueOf(vectorHeight));
			velVector.setHeadColorOverride(accelerationVectorColor);
			velVector.iwpDraw(g, state);
		}
		
		if(vectorSelector.AccelSelected()) {
			double vectorWidth = 0, vectorHeight = 0;
			vectorWidth = state.vars().getAtCurrentTick(solid.getName() + "." + MVariables.XACCEL );
			vectorHeight = state.vars().getAtCurrentTick(solid.getName() + "." + MVariables.YACCEL );
    	
			GShape_Vector velVector = new GShape_Vector(solid,String.valueOf(vectorWidth), String.valueOf(vectorHeight));
			velVector.setHeadColorOverride(accelerationVectorColor);
			velVector.iwpDraw(g, state);
		}	
   	}
	
	
}

