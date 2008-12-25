package edu.ncssm.iwp.math;

/**
 * Factory patttern so I can easily swap out the variables implementation underneath.
 * @author brockman
 *
 */

public class MVariablesFactory
{
	public static boolean usePrecise = false;
	
	public static void setPrecise ( boolean precise ) 
	{
		usePrecise = precise;
	}
	
	public static MVariables newInstance()
	{	
		if ( ! usePrecise ) { 
		
			return new MVariablesHashImpl();
			
		} else { 
			
			// Method that tries to prevent most doubles from doing the .00000000000000004 thing
			// This one really doesn't work any better
			return new MVariablesPreciseImpl();
		}
		
		
	}

}
