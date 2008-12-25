package edu.ncssm.iwp.math;


/**
 * Injected symbols include the symbols x/v/d that are injected into the
 * Euler calculator.
 * They're not really constants, not really user definable variables, so I'm 
 * putting them into their own distinct class.
 * 
 * @author brockman
 *
 */

public class MInjectedSymbols
{
//	 2007-June-06 brockman. Euler methods can reference their own v and d.
	public static String EULER_D = "d";
	public static String EULER_X = "x";
	public static String EULER_V = "v";
	
	
	/**
	 * Return true if this variable is one that always appears in problems.
	 * Like t, or v/x/d in Euler's equations.
	 * @param name
	 * @return
	 */
	public static boolean isPresentVariableSymbol ( String name )
	{
		
		if ( name.equalsIgnoreCase(EULER_D) || 
			 name.equalsIgnoreCase(EULER_X) ||
			 name.equalsIgnoreCase(EULER_V) ) { 
			return true;
		} else {
			return false;
		}

	}

}
