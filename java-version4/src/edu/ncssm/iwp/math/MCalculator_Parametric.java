// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"


package edu.ncssm.iwp.math;

import java.util.ArrayList;
import java.util.Collection;

import edu.ncssm.iwp.math.designers.MCalculator_Parametric_simpleDesigner;
import edu.ncssm.iwp.math.designers.MCalculator_Parametric_subDesigner;
import edu.ncssm.iwp.math.designers.MCalculator_Abstract_subDesigner;
import edu.ncssm.iwp.util.*;
import edu.ncssm.iwp.exceptions.*;


public class MCalculator_Parametric extends MCalculator
{
	private static final long serialVersionUID = 1L;
	// Moving the derivative code out to a higher level.
	private double curDisp;
	private double curVel;
	private double curAccel;	

	private MEquation eqn;
	
	public MCalculator_Parametric ( )
	{
		setEquation(new MEquation());
	}
	
	public MCalculator_Parametric ( String s )
	{
		try {
			MEquation eqn = new MEquation(s);
			setEquation(eqn);
		} catch ( InvalidEquationException x ) {
			IWPLog.x(this, "InvalidEquationException: " + x.getMessage(), x);
		}
	}
	
	
	public MCalculator_Parametric ( MEquation eqn )
	{
		setEquation(eqn);
	}

	
	public void setEquation ( MEquation eqn )
	{
		this.eqn = eqn;
	}

	
	public MEquation getEquation ( )
	{
		return eqn;
	}
	
	
	/**
	 * Actually perform the calculation for this equation.
	 * 
	 */
	
	public double calculate ( MDataHistory vars )
		throws UnknownVariableException, InvalidEquationException, UnknownTickException
	{
		// Data Consistency Check.
		if ( eqn == null) {
			IWPLog.error(this,"[MCalculator_Parametric][calculate] ERROR: null eqn!!");
			throw new InvalidEquationException("Null Equation");
		}

		// derivative code 

		// Find out time elapsed between now + last step.
		double timeDelta = vars.getDeltaTime();
		
		// do the calculation.
		curDisp = eqn.calculate ( vars );

		// I'm not a huge fan of rolling the clock forward and 
		// backwards like this, but I've made it a little better
		// by cloning and not actually messing with anything.	
		//
		// MAJOR Change here: I am calculating the current velocity based
		// on the last two nodes instead of a node behind + ahead.
		// but this should be better because I'm going to actually be 
		// using the varaibles from the time periods.
		
		// NOTE that for performnace, the toHashtable is not doing any memory allocation.
		
		if ( vars.getCurrentTick() == 0 ) {
			// This is the first frame. I dont' have these numbers.
			curVel = 0;
			curAccel = 0;
			
		} else if ( vars.getCurrentTick() == 1 ) { 
			
			// First frame, I've only got accel.
			curAccel = 0;
			
			MVariables backOne = vars.getVarsAtTick(vars.getCurrentTick()-1);
			double backOneDisp = eqn.calculate ( backOne );
			curVel             = (curDisp-backOneDisp)/(timeDelta);
			
		} else {
			
			MVariables backOne = vars.getVarsAtTick(vars.getCurrentTick()-1);
			MVariables backTwo = vars.getVarsAtTick(vars.getCurrentTick()-2);
		
			double backOneDisp = eqn.calculate ( backOne );
			double backTwoDisp = eqn.calculate ( backTwo );

			curVel             = (curDisp-backOneDisp)/(timeDelta);
			double backOneVel  = (backOneDisp-backTwoDisp)/(timeDelta);
			curAccel           = (curVel-backOneVel)/(timeDelta);

		}
					
		return curDisp;
	}

	
	public String getType ( )
	{
		return MCalculator.TYPE_STRING_PARAMETRIC;
	}
	
	
	public void zero ( MDataHistory vars )
		throws UnknownVariableException, InvalidEquationException, UnknownTickException
	{
		calculate(vars);	
	}

	public double getDisp() { return this.curDisp; }
	public double getVel() { return this.curVel; }
	public double getAccel() { return this.curAccel; }
	
	public String getEquationString () { return eqn.getEquationString(); }
	public String toString() { return getEquationString(); }


	public MCalculator_Abstract_subDesigner getSubDesigner ( String label )
	{
		return new MCalculator_Parametric_subDesigner ( this, label );
	}
	
	public MCalculator_Abstract_subDesigner getSubDesigner ( String label, int inputLength )
	{
		return new MCalculator_Parametric_subDesigner ( this, label, inputLength );
 	}

	// 2008-Dec-25 brockman, multiline input.
	public MCalculator_Abstract_subDesigner getSubDesigner ( String label, int inputLength, boolean inputOnNewLine )
	{
		return new MCalculator_Parametric_subDesigner ( this, label, inputLength, inputOnNewLine );
 	}

	
	/**
	 * Simple designer is just a text input field for Parametric equations.
	 * New in IWP3. Used heavily in the polygon point designers.
	 * @author brockman
	 * @param label
	 * @return
	 */
	public MCalculator_Parametric_simpleDesigner getSimpleParametricDesigner ( String label )
	{
		return new MCalculator_Parametric_simpleDesigner ( label, this );
 	}
	
	public MCalculator_Parametric_simpleDesigner getSimpleParametricDesigner ( String label, int inputLength )
	{
		return new MCalculator_Parametric_simpleDesigner ( label, this, inputLength );
 	}
	
	public MCalculator_Parametric_simpleDesigner getSimpleParametricDesigner ( String label, int inputLength, boolean inputOnNewLine )
	{
		return new MCalculator_Parametric_simpleDesigner ( label, this, inputLength, inputOnNewLine );
 	}
	
	


	public Collection getRequiredSymbols()
		throws InvalidEquationException
	{
		return eqn.listVariables();
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



