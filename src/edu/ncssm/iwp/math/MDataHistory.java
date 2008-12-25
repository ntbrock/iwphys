package edu.ncssm.iwp.math;

import edu.ncssm.iwp.exceptions.UnknownVariableException;
import edu.ncssm.iwp.exceptions.UnknownTickException;

/**
 * This is the new Interface that gets passed into the MEquation
 * object to calculate. DProblem is going to implement this.
 * There is also a concrete implementation in MVariablesImpl.java
 * @author brockman
 *
 */

public interface MDataHistory
{

	/**
	 * Return the value of the current tick. Useful for processing
	 * that doesn't care what time it is.
	 * @return
	 */
	public int getCurrentTick ( );


	
	/**
	 * Look backwards in time to pull out a past value. Used by the graphing routines.
	 * 
	 * @param variableName
	 * @param tick
	 * @return
	 * @throws UnknownVariableException
	 * @throws UnknownTickException
	 */
	public double getAtTick ( String variableName, int tick )
		throws UnknownVariableException, UnknownTickException;

	
	
	/**
	 * Create a key->Double hash of the values of the current tick.
	 * @return
	 * @throws UnknownTickException
	 */	
	public MVariables getCurrentTickVars ( )
		throws UnknownTickException;
	/*
	NO - dont' look back in time. I am now properly ordering the time objects.
	public Hashtable createPreviousTickHash ( )
		throws UnknownTickException;
	 */

	
	/**
	 * This is a core variable, available everywhere. The amount
	 * of elapsed time between ticks. Very important a lot of places,
	 * and this will prbaoby be the only 'hard' fixed variable I 
	 * expose here.
	 * @return
	 */
	
	public double getDeltaTime()
		throws UnknownVariableException, UnknownTickException;
	

	/**
	 * Return the time of the current tick frame. This looks in the
	 * hash like it's suppoesd to for "t";
	 * @return
	 */
	public double getCurrentTime()
		throws UnknownVariableException, UnknownTickException;
	
	
	

	/**
	 * Set a variable and a historical value in to the hash.
	 * @param variableName
	 * @param value
	 */
	public void setAtCurrentTick ( String variableName, double value );
	
	
	/**
	 * Get a variable from the current tick. This used to be the
	 * 'last' tick, but now the problem re-roders itself in dependent
	 * order!
	 * 
	 * @param variableName
	 * @return
	 * @throws UnknownTickException
	 * @throws UnknownVariableException
	 */
	
	public double getAtCurrentTick ( String variableName )
		throws UnknownTickException, UnknownVariableException;

	
	/**
	 * Return all the variables in a specific tick.
	 * @param tick
	 * @return
	 */
	
	public MVariables getVarsAtTick ( int tick );
	
	
}