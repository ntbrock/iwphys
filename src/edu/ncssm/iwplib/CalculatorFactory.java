/**
 * This is a lightweight API binding layer to tie the new iwplib interfaces
 * back to the real IWP code
 * 
 * @author brockman
 */

package edu.ncssm.iwplib;

import edu.ncssm.iwp.math.*;
import edu.ncssm.iwp.exceptions.*;


public class CalculatorFactory
{
	
	public static CalculatorEuler constructEuler ( double initialDisplacement,
												   double initialVelocity,
												   String accelEquation )
		throws InvalidEquationException
	{
		return new MCalculator_Euler ( new MEquation(initialDisplacement+""),
				new MEquation(initialVelocity+""),
						new MEquation(accelEquation ) ) ;
	}
	
}
