// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"


package edu.ncssm.iwp.math;

import edu.ncssm.iwp.exceptions.*;
import edu.ncssm.iwp.math.designers.MCalculator_Abstract_subDesigner;
import edu.ncssm.iwp.util.IWPLog;
import edu.ncssm.iwp.util.IWPLogPopup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;


/**
 * All implementations of this class are now required to use storePoint 
 * after each time calculatePointAfterZero is called.
 * 
 * @author brockman
 *
 */

public abstract class MCalculator_Diff extends MCalculator
{
	public static String ACCEL_EQN_VAR_D = MInjectedSymbols.EULER_D; // d and x are set to same value.
	public static String ACCEL_EQN_VAR_X = MInjectedSymbols.EULER_X;
	public static String ACCEL_EQN_VAR_V = MInjectedSymbols.EULER_V;
	
	
	public static int WARNING_VECTOR_SIZE = 100000; // 100k
	public static int ERROR_VECTOR_SIZE = 1000000; // 1M

	// Vectors that hold the previously calculated data. We store them locally here
	// instead of calling MVariables every time to avoid a hash lookup.

	int vectorGrowthIncrement = 100; // When you resize a vector, resze them by this amount for memory efficency.


	protected Vector vT = new Vector();
	protected Vector vX = new Vector();
	protected Vector vV = new Vector();
	protected Vector vA = new Vector();
		
	private double latestX = 0;
	private double latestV = 0;
	private double latestA = 0;

	protected MEquation initDispEqn;
	protected MEquation initVelEqn;
	protected MEquation accelEqn;

	
	protected MCalculator_Diff ( MEquation dispEqn,
								MEquation velEqn,
								MEquation accelEqn )
	{		
		setInitDispEqn(dispEqn);
		setInitVelEqn(velEqn);
		setAccelEqn(accelEqn);
		
		resetVectors();
	}
	

	public void zero ( MDataHistory vars )
		throws UnknownVariableException, InvalidEquationException, UnknownTickException
	{
		resetVectors();
		calculate(vars);
	}


	/**
	 * This is the real implementation of the logic.
	 * The zero calculation is going ot be all the same for the diff methods,
	 * so I'm renaming this and doing the zero calc at the higher level in
	 * common code.
	 * 2006-Aug-30 brockman
	 * @param vars
	 * @param atTick
	 * @throws UnknownVariableException
	 * @throws InvalidEquationException
	 * @throws UnknownTickException
	 */
	
	abstract void calculatePointAfterZero ( MDataHistory vars, int atTick )
		throws UnknownVariableException, InvalidEquationException, UnknownTickException;
	
	
	
	/**
	 * The common calculator for Diff (Euler, RK2, RK4) functions. It's smart enough to 
	 * handle a starting t=0 case and calls the calculate back method.
	 * 
	 * The implementations are required to use storePoint for each tick they calculate.
	 */
	
	public double calculate( MDataHistory vars )
		throws UnknownVariableException, InvalidEquationException, UnknownTickException
	{
	    int curTick = vars.getCurrentTick();
	    if( curTick == 0 ) {

	    	double x0 = initDispEqn.calculate(vars);
	    	double v0 = initVelEqn.calculate(vars);
	    	
	    	// initial acceleration calculation. The same for all Diff calculators.
			storePoint( curTick, vars.getCurrentTime(),
						x0, v0, calculateAccel(vars.getCurrentTime(), x0, v0, vars) );
	    } else { 
	    	// Calculate back travels back in time, and populates this.x,this.v,this.a w/ current values. 
	    	calculateBack( vars, curTick );
	    }

	    latestX = ((Double)vX.elementAt(curTick)).doubleValue(); 
	    latestV = ((Double)vV.elementAt(curTick)).doubleValue();
	    latestA = ((Double)vA.elementAt(curTick)).doubleValue();
	    return latestX;
	}

	
	
	private void calculateBack ( MDataHistory vars, int toTick )
		throws UnknownVariableException, InvalidEquationException, UnknownTickException
	{
		// int curTick = vars.getCurrentTick();

		if ( !exists( toTick )) {
			// roll the ticks back by 1 
			if ( ! exists ( toTick-1 )) {
				calculateBack ( vars, toTick-1 );
			}
			calculatePointAfterZero ( vars, toTick );
		}
	}


	boolean exists ( int ticks )
	{
		try
		{
			// Return whether element exists
			if (vT.elementAt( ticks ) == null)
			{
				return false;
			} else {
				return true;
			}
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			// Element cannot exists if out-of-bounds
			return false;
		}
	}
	
	
	

	/**
	 * Do the acceleration calculation
	 * @param t
	 * @param x
	 * @param v
	 * @param vars
	 * @return
	 * @throws UnknownVariableException
	 * @throws InvalidEquationException
	 * @throws UnknownTickException
	 */
	
	double calculateAccel(double t, double x, double v,MDataHistory vars)
		throws UnknownVariableException, InvalidEquationException, UnknownTickException
	{
				
		// WARNING: These will overwrite each frame's data. I could jsut turn these off and 
		// use Object.xvel, which would be much better.
		vars.setAtCurrentTick( ACCEL_EQN_VAR_D, x);
		vars.setAtCurrentTick( ACCEL_EQN_VAR_X, x);
		vars.setAtCurrentTick( ACCEL_EQN_VAR_V, v);
		
		return accelEqn.calculate ( vars );
	}

	

	/**
	 * My implemenations MUST now use this handy function to save the results of
	 * a a calculation.
	 * 
	 * The implementations are required to use storePoint for each tick they calculate.
	 * 
	 * @param nTick
	 * @param t
	 * @param x
	 * @param v
	 * @param a
	 */
	
	protected void storePoint ( int nTick, double t, double x, double v, double a)
	{		
		growVectors(nTick+1);
		
		try { 
			vT.setElementAt(new Double(t), nTick);
			vX.setElementAt(new Double(x), nTick);
			vV.setElementAt(new Double(v), nTick);
			vA.setElementAt(new Double(a), nTick);
		} catch ( ArrayIndexOutOfBoundsException e ) { 
			IWPLog.error(this, "ArrayIndexOOB: nTick=" + nTick + ": " + e.getMessage() );
		}
	}
		

	private void resetVectors()
	{
		// We cannot save our numbers between runs. What if an input changes?
		vT = new Vector( vectorGrowthIncrement, vectorGrowthIncrement );
		vX = new Vector( vectorGrowthIncrement, vectorGrowthIncrement );
		vV = new Vector( vectorGrowthIncrement, vectorGrowthIncrement );
		vA = new Vector( vectorGrowthIncrement, vectorGrowthIncrement );

		// The garbage collector should handle all the old data.
	}
	
	
	private void growVectors(int inSize)
	{
		// return, already big enough.
		int currentVectorSize = vT.size();
		
		if ( inSize <= currentVectorSize ) { return; }

		int newVectorSize = currentVectorSize;	
		
		// Grow the vectors in increments up to the size.
		while ( newVectorSize < inSize ) { 
			newVectorSize += vectorGrowthIncrement;
			
			if ( newVectorSize > WARNING_VECTOR_SIZE ) { 
				IWPLog.warn(this, "Vector size (" + currentVectorSize + ") growing beyond warning (" + WARNING_VECTOR_SIZE + ")!" );
			}

			if ( newVectorSize > ERROR_VECTOR_SIZE ) { 
				IWPLogPopup.error(this, "Vector size (" + currentVectorSize + ") growing beyond error (" + ERROR_VECTOR_SIZE + ")!" );
			}
		}

		vT.setSize(newVectorSize);
		vX.setSize(newVectorSize);
		vV.setSize(newVectorSize);
		vA.setSize(newVectorSize);
	}
	
	// Part of MCalculator generic. This always returns the latestx,v,a
	public double getDisp() { return this.latestX; }
	public double getVel() { return this.latestV; }
	public double getAccel() { return this.latestA; }

	// The designer uses this to get eqn's in and out.
	public MEquation getInitDispEqn()
	{
		return initDispEqn;
	}
    public void setInitDispEqn( MEquation eqn )
    {
    	initDispEqn = eqn;
    }
	
    public MEquation getInitVelEqn()
	{ 
		return initVelEqn;
	}
    
    public void setInitVelEqn( MEquation eqn )
    {
    	initVelEqn = eqn;
    }
    
    public MEquation getAccelEqn()
	{
		return accelEqn;
	}
    
	public void setAccelEqn( MEquation eqn )
	{
		accelEqn = eqn;
	}
	
	
	
	public abstract MCalculator_Abstract_subDesigner getSubDesigner( String label );


	public Collection getRequiredSymbols()
		throws InvalidEquationException
	{
		return accelEqn.listVariables();
	}


	public Collection getProvidedSymbols() throws InvalidEquationException
	{
		ArrayList out = new ArrayList(5);
		out.add ( MCalculator.SYMBOL_DISP );
		out.add ( MCalculator.SYMBOL_POS );
		out.add ( MCalculator.SYMBOL_VEL );
		out.add ( MCalculator.SYMBOL_ACCEL );

		return out;
	}
	
}
