// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"


package edu.ncssm.iwp.math;

import edu.ncssm.iwp.exceptions.*;
import edu.ncssm.iwp.math.designers.MCalculator_RK4_subDesigner;
import edu.ncssm.iwp.math.designers.MCalculator_Abstract_subDesigner;


public class MCalculator_RK4 extends MCalculator_Diff
{
	private static final long serialVersionUID = 1L;
	public MCalculator_RK4()
	{
		super ( new MEquation(), new MEquation(), new MEquation() );
	}

	public MCalculator_RK4 ( MEquation initialDisplacementEqn,
			MEquation initialVelocityEqn,
			MEquation accelEquation ) 
	{
		super ( initialDisplacementEqn, initialVelocityEqn, accelEquation );
	}
	

	public String getType()
	{
		return MCalculator.TYPE_STRING_RK4;
	}

	public MCalculator_Abstract_subDesigner getSubDesigner( String label )
	{
		return new MCalculator_RK4_subDesigner(this);
	}

	
	void calculatePointAfterZero ( MDataHistory vars, int atTick )
		throws UnknownVariableException, InvalidEquationException,
			UnknownTickException
	{
		int nTick = atTick;
		double dt = vars.getDeltaTime();

		double t = ((Double)vT.elementAt(nTick - 1)).doubleValue();
		double x = ((Double)vX.elementAt(nTick - 1)).doubleValue();
		double v = ((Double)vV.elementAt(nTick - 1)).doubleValue();

		double kx0, kx1, kx2, kx3;
		double kv0, kv1, kv2, kv3;

		kx0 = dt * (v );
		kv0 = dt * calculateAccel(t, x, v,vars);
		kx1 = dt * (v + kv0 / 2);
		kv1 = dt * calculateAccel(t + dt / 2, x + kx0 / 2, v + kv0 / 2,vars);
		kx2 = dt * (v + kv1 / 2);
		kv2 = dt * calculateAccel(t + dt / 2, x + kx1 / 2, v + kv1 / 2,vars);
		kx3 = dt * (v + kv2);
		kv3 = dt * calculateAccel(t + dt, x + kx2, v + kv2,vars);

		x += ((kx0) + (2 * kx1) + (2 * kx2) + (kx3)) / 6;
		double a = ((kv0) + (2 * kv1) + (2 * kv2) + (kv3)) / 6; 
		v += a;
		t += dt;
		
		super.storePoint(nTick,t,x,v,a);
	}

}
