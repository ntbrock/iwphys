// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.math;

import edu.ncssm.iwp.exceptions.*;
import edu.ncssm.iwp.util.*;
import java.util.Hashtable;
import java.util.Vector; 

/**
 * This is an implementation of the new variables Interface.
 * It is a hashtable of vectors, storing the values of each of
 * the variables at each time tick.
 * 
 * @author brockman
 *
 */

public class MDataHistoryArrayListImpl implements MDataHistory
{

	// This is used ast the two arguemnts to Vectors to
	// grow our arrays in chunks.
	public static int DEFAULT_CAPACITY = 1000;
	
	
	// PastFrames contains hashes the of the values used in the past.
	// This drives the getAtTick and is used by the graph functions.
	Vector history = new Vector(DEFAULT_CAPACITY);

	// Our current scratch space 
	MVariables currentVars;
	
	// Maybe the most important variable in IWP?
	// THis is THE definition of what frame of an animation 
	// youare viewing.
	int currentTick = -1;
	
	
	/**
	 * This is the constructor that you should use when 
	 * creating a fresh, empty variable set.
	 *
	 */
	
	public MDataHistoryArrayListImpl ()
	{
		this.setCurrentTick(0);
	}
	
	
	/**
	 * Set data for a variable at a specific tick.
	 * @param variableName
	 * @param tick
	 * @param value
	 */
	public void setAtTick ( String variableName, int tick, double value )
	{
		ensureCapacity(tick);
		MVariables t = (MVariables) history.get(tick);
		if ( t == null ) { 
			t = MVariablesFactory.newInstance();
		}
		
		t.set(variableName, value);
		
		// set it back for good measure.
		history.setElementAt(t, tick);
		
	}

	

	/**
	 * Get data for a variable at a specific tick.
	 * @param variableName
	 * @param tick
	 * @param value
	 */
	
	public double getAtTick ( String variableName, int tick )
		throws UnknownVariableException, UnknownTickException
	{
		ensureCapacity(tick);
		
		MVariables t = (MVariables) history.get(tick);
		if ( t == null ) { 
			throw new UnknownTickException ( "NO Hashtable at tick: " + tick );
		}
		
		return t.get(variableName);
	}
    
	
	
	public MVariables getVarsAtTick ( int tick )
	{
		if ( tick < 0 ) { 
			IWPLog.error(this, "ALERT: Tried to ask for a tick <= 0. ASked for: " + tick );
			// Important note: I must return the first frame here, as the MCalculator PArametric
			// tries to go backwards. I am going to go and make it smarter to not look back on the
			// first two frames
			return getVarsAtTick(0); 
		}
		return (MVariables) history.get( tick );
	}
	
	

	/**
	 * This creates a full set for the last step. We don't
	 * want to get ahead of ourselves, so I am now basing
	 * all calculations on the previous tick state.
	 * 
	 * @param variableName
	 * @param tick
	 * @param value
	 */
	
	public MVariables getCurrentTickVars()
		throws UnknownTickException
	{
		return currentVars;
	}
	
	
	/**
	 * Set this tick's data. Convenience function.
	 */
	public void setAtCurrentTick ( String variableName, double value )
	{
		currentVars.set(variableName, value);
	}
	

	/**
	 * 
	 */
	public double getAtCurrentTick ( String variableName )
		throws UnknownTickException, UnknownVariableException
	{
		return currentVars.get(variableName);
	}
		
	
	/**
	 * What tick are we on?
	 */
	public int getCurrentTick()
	{
		return currentTick;
	}

	
	public void setCurrentTick(int currentTick)
	{
		ensureCapacity(currentTick);
		
		// file away the current vars, only if tick >= 0
		if ( this.currentTick >= 0 ) { history.setElementAt(currentVars,this.currentTick); }
		
		
		// ok , let's go.
		this.currentTick = currentTick;
		
		try { 
			currentVars = (MVariables) history.get(currentTick);
		} catch ( IndexOutOfBoundsException x ) { 
			currentVars = null;
		}
		if ( currentVars == null ) { 
			currentVars = MVariablesFactory.newInstance();
			history.setElementAt(currentVars,currentTick); // also set the pointer back.
		}
	}
	
	
	/**
	 * A simple little trick to pull out the 'delta_t' a commonly used var.
	 */
	
	public double getDeltaTime()
		throws UnknownVariableException, UnknownTickException
	{
		return this.getAtTick( MVariables.DELTA_T, this.getCurrentTick());
	}
	
	
	/**
	 * A simple little trick to pull out 't' a commonly used var.
	 */
	
	public double getCurrentTime()
		throws UnknownVariableException, UnknownTickException
	{
		return this.getAtTick( MVariables.T, this.getCurrentTick());
	}
		
	

	/**
	 * Used my MCalculator_Parametric to calculate instantanous
	 * velocity.
	 */
	
	public Hashtable cloneStepTimeBackward()
	{
		return (Hashtable) history.get(currentTick-1);
	}
	

	
	private void ensureCapacity(int toTick)
	{
		history.ensureCapacity(toTick); // regular ensure capacity doesn't scale members.
		if( history.size() <= toTick ) {
			for ( int i = history.size(); i <= toTick; i++ ) { 
				history.add( MVariablesFactory.newInstance() );
			}
		}
	}
	
}

