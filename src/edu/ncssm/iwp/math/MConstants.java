package edu.ncssm.iwp.math;

import edu.ncssm.iwp.exceptions.*;

/**
 * Constants that are usable by the MEquation
 * @author brockman
 *
 */

public class MConstants
{
	public static final String PI = "pi";
	public static final double PI_VALUE = Math.PI;
	
	public static final String E = "e";
	public static final double E_VALUE = Math.E;

		
	public static double get ( String name )
		throws UnknownVariableException
	{
		// Also support PI.value
		if ( name.endsWith(".value") ) {
			name = name.substring(0, name.indexOf(".value"));
		}

		if ( name.equalsIgnoreCase(PI) ) {
			return PI_VALUE;
		} else if ( name.equalsIgnoreCase(E) ) {
			return E_VALUE;
		}
		
		throw new UnknownVariableException("Unknown Constant: " + name);
	}
	
	
	/**
	 * Return true if this symbol is a constant 
	 * @param name
	 * @return
	 */
	
	public static boolean isConstantSymbol ( String name )
	{
	
		try { 
			get(name);
			return true;
		} catch (UnknownVariableException x ){
			return false;
		}
	}
	
}
