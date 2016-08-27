// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"


package edu.ncssm.iwp.math;

import edu.ncssm.iwp.math.designers.MCalculator_designer;
import edu.ncssm.iwp.math.designers.MCalculator_Abstract_subDesigner;
import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.exceptions.*;
import edu.ncssm.iwp.plugin.*;

// TODO: make this class abstract + a lot of the methods inside

public abstract class MCalculator extends DEntity implements IWPCalculated
{
	/**
	 * These symbols are used by the provides and requires symbols.
	 * 
	 */
	public static final String SYMBOL_DISP  = "disp";
	public static final String SYMBOL_POS   = "pos";
	public static final String SYMBOL_VEL   = "vel";
	public static final String SYMBOL_ACCEL = "accel";
	
	public static final String TYPE_STRING_UNKNOWN = "MCalculator_Unknown";
	public static final String TYPE_STRING_PARAMETRIC = "MCalculator_Parametric";
	public static final String TYPE_STRING_RK2 = "MCalculator_RK2";
	public static final String TYPE_STRING_RK4 = "MCalculator_RK4";
	public static final String TYPE_STRING_EULER = "MCalculator_Euler";

	public MCalculator() {}

	/**
	 * Zero has been replaced with a calculate at zero for simplicity.
	 * 
	 * @param variables
	 * @return
	 * @throws UnknownVariableException
	 * @throws InvalidEquationException
	 * @throws UnknownTickException
	 */
	
	public abstract double calculate ( MDataHistory variables )
		throws UnknownVariableException,
		InvalidEquationException,
		UnknownTickException;

	public abstract double getDisp();
	public abstract double getVel();
	public abstract double getAccel();


	/**
	 * This should be the one called externally.
	 * @param label
	 * @return
	 */
	
	public MCalculator_designer getDesigner(String label)
	{
		return new MCalculator_designer(this, label);
	}
	
	
	/**
	 * This method is only called by the MCalculator_Designer - it pulls the
	 * gui objects to populate into the top-level MCalculator_designer.
	 * @param label
	 * @return
	 */
	
	public abstract MCalculator_Abstract_subDesigner getSubDesigner ( String label ); 

	public abstract String getType ( );

	
	/**
	 * Implemented to zero out cached variables between resets, specifically for MCalculator_diff
	 * @param vars
	 */
	public abstract void zero ( MDataHistory vars )
		throws UnknownVariableException, InvalidEquationException, UnknownTickException;
	
}


