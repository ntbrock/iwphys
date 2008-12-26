package edu.ncssm.iwp.math;

import java.util.Iterator;

import edu.ncssm.iwp.exceptions.UnknownVariableException;

/**
 * This is basically a hashtable interface. It has no historical values.
 * @author brockman
 *
 */

public interface MVariables
{
	// These are very global varaibles that are at the top of the hash.
	// DObject_Solid.
	public static String MAX_STEPS = "max_steps__";
	public static String DELTA_T = "delta_t";
	public static String T = "t";
	
	// These now depend on the symbols provided by the Calculator
	public static String XDISP = "x" + MCalculator.SYMBOL_DISP;
	public static String XPOS = "x" + MCalculator.SYMBOL_POS;
	public static String XVEL = "x" + MCalculator.SYMBOL_VEL;
	public static String XACCEL = "x" + MCalculator.SYMBOL_ACCEL;
	
	public static String YDISP = "y" + MCalculator.SYMBOL_DISP;
	public static String YPOS = "y" + MCalculator.SYMBOL_POS;
	public static String YVEL = "y" + MCalculator.SYMBOL_VEL;
	public static String YACCEL = "y" + MCalculator.SYMBOL_ACCEL;
	
	public static String WIDTH = "width";
	public static String HEIGHT = "height";
	public static String ANGLE = "angle";

	// DObject_Time
	public static String CURTIME = "curTime";
	public static String STARTTIME = "startTime";
	public static String ENDTIME = "endTime";
	public static String STOPTIME = "stopTime";
	public static String DELTATIME = "deltaTime";
	
	// DObject_Output
	
	public static String VALUE = "value";
	
	
	/**
	 * Get the double value associated with a variable name.
	 * @param name
	 * @return
	 */
	public double get ( String name )
		throws UnknownVariableException;
	
	/**
	 * Set the value in.
	 * @param name
	 * @param value
	 */
	public void set ( String name, double value );
	
	
	
	/**
	 * Return an iterator of all varaibles.
	 * @return
	 */
	
	public Iterator iterator();
	
}
