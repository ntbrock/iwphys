// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.math;

import edu.ncssm.iwp.exceptions.*;
import edu.ncssm.iwp.math.designers.MCalculator_RK2_subDesigner;
import edu.ncssm.iwp.math.designers.MCalculator_Abstract_subDesigner;

/**
 * RungaKutta 2
 * @author brockman
 *
 */


public class MCalculator_RK2 extends MCalculator_Diff
{
	private static final long serialVersionUID = 1L;

	public MCalculator_RK2 ()
	{
		super ( new MEquation(),
				new MEquation(),
				new MEquation() );
	}
	
	public MCalculator_RK2( MEquation initialDisplacementEqn,
			MEquation initialVelocityEqn,
			MEquation accelEquation ) 
	{
		super ( initialDisplacementEqn, initialVelocityEqn, accelEquation );
	}

	public String getType()
	{
		return MCalculator.TYPE_STRING_RK2;
	}

	public MCalculator_Abstract_subDesigner getSubDesigner( String label )
	{
		return new MCalculator_RK2_subDesigner(this);
	}

	
	//--------------------------------------------------------------

	void calculatePointAfterZero ( MDataHistory vars, int atTick )
		throws UnknownVariableException, InvalidEquationException, UnknownTickException
	{
		int nTick = atTick;
		double dt = vars.getDeltaTime();

		double t = ((Double)vT.elementAt(nTick - 1)).doubleValue();
		double x = ((Double)vX.elementAt(nTick - 1)).doubleValue();
		double v = ((Double)vV.elementAt(nTick - 1)).doubleValue();
		double a = ((Double)vA.elementAt(nTick - 1)).doubleValue();

		double kx0, kx1;
		double kv0, kv1;

		kx0 = dt * (v);
		kv0 = dt * calculateAccel(t, x, v,vars);
		kx1 = dt * (v + kv0);
		//sweeneyb mod/add      
		//kv1 = dt * getAcc(t + dt, x + kx0, v + kv0,vars);
		a=calculateAccel(t + dt, x + kx0, v + kv0,vars);
		kv1= dt * a;

		x += (kx0 + kx1) / 2;
		v += (kv0 + kv1) / 2;
		t += dt;
		
		super.storePoint(nTick,t,x,v,a);
	}
	
}
