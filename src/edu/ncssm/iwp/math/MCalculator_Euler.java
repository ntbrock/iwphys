// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"


package edu.ncssm.iwp.math;


import edu.ncssm.iwp.math.designers.MCalculator_Euler_subDesigner;
import edu.ncssm.iwp.math.designers.MCalculator_Abstract_subDesigner;
import edu.ncssm.iwp.util.*;
import edu.ncssm.iwp.exceptions.*;

import edu.ncssm.iwplib.*;



public class MCalculator_Euler extends MCalculator_Diff
	implements CalculatorEuler
{
	
	public MCalculator_Euler ()
	{
		super ( new MEquation(),
				new MEquation(),
				new MEquation() );
		// Have to make the null constructor repond.
	}
	
	public MCalculator_Euler ( MEquation initialDisplacementEqn,
			MEquation initialVelocityEqn,
			MEquation accelEquation ) 
	{
		super ( initialDisplacementEqn, initialVelocityEqn, accelEquation );
	}


	public String getType()
	{
		return MCalculator.TYPE_STRING_EULER;
	}
	
	void calculatePointAfterZero( MDataHistory vars, int atTick )
		throws UnknownVariableException, UnknownTickException, InvalidEquationException
	{

		IWPLog.debug(this, "calculatePoint at tick = " + atTick );
		
		int nTick = atTick;

		double dt = vars.getAtTick( MVariables.DELTA_T, nTick);
		double t = vars.getCurrentTime();
		
		if ( atTick == 0 ) { 		
			IWPLog.error(this, "CalculatePointAfterZero called with a tick == 0! PROGRAMMING ERROR");
		} else {
										
			// TODO: I could pull these from the MVariables to make it more standard, 
			// 	but since as of 2006-Aug-30 MVariables uses a hash implementation, it might
			// really slow things down.
			double lastT = ((Double)vT.elementAt(nTick - 1)).doubleValue(); 
			double lastX = ((Double)vX.elementAt(nTick - 1)).doubleValue();
			double lastV = ((Double)vV.elementAt(nTick - 1)).doubleValue();

			double thisX;
			double thisV;
			double thisA = calculateAccel(t, lastX, lastV, vars);
			double thisT = lastT + dt;
			
			if ( (float)thisT != (float)t ) { 
				IWPLog.error(this, "ERROR vars.t("+t+") != lastT("+lastT+")+deltaT("+dt+")");
			}
			
			// Euler's first step workaround.
			if (nTick == 1) {
				thisV = lastV + (.5 * thisA * dt);
				// or is it this?
				//thisV = lastV + (.5 * thisA ) * dt;
			} else {
				thisV = lastV +  thisA * dt;
			}
			
			thisX = lastX + thisV * dt;
			
			storePoint ( nTick, thisT, thisX, thisV, thisA );
		}
		
	}


	public MCalculator_Abstract_subDesigner getSubDesigner( String label )
	{
		return new MCalculator_Euler_subDesigner(this);
	}

}
