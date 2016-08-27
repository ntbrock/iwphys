package edu.ncssm.iwp.math;

import edu.ncssm.iwp.exceptions.UnknownVariableException;

/**
 * Functions that are usable in MEquations
 * @author brockman
 *
 */

public class MFunctions
{

	
    private static final String FUNC_SIGN = "sign";
	private static final String FUNC_EXP = "exp";
	private static final String FUNC_TO_RADIANS = "toRadians";
	private static final String FUNC_TO_DEGREES = "toDegrees";
	private static final String FUNC_STEP = "step";
	private static final String FUNC_COT = "cot";
	private static final String FUNC_SEC = "sec";
	private static final String FUNC_CSC = "csc";
	private static final String FUNC_TAN = "tan";
	private static final String FUNC_SQRT = "sqrt";
	private static final String FUNC_SIN = "sin";
	private static final String FUNC_COS = "cos";
	private static final String FUNC_LN = "ln";
	private static final String FUNC_ATAN = "atan";
	private static final String FUNC_ASIN = "asin";
	private static final String FUNC_ACOS = "acos";
	private static final String FUNC_ABS = "abs";
	private static final String FUNC_MODTWO = "modtwo";
	private static final String FUNC_RANDOM = "random";
	private static final String FUNC_RAND = "rand";
	private static final String FUNC_SIGNUM = "signum";
	

	public static String[] list = {
        FUNC_ABS,FUNC_ACOS,FUNC_ASIN,FUNC_ATAN,FUNC_LN,
        FUNC_COS,FUNC_SIN,FUNC_SQRT,FUNC_TAN,FUNC_CSC,FUNC_SEC,FUNC_COT,
        FUNC_STEP,FUNC_TO_RADIANS,FUNC_TO_DEGREES,FUNC_EXP,FUNC_SIGN,FUNC_MODTWO,
        FUNC_RANDOM, FUNC_RAND, FUNC_SIGNUM
    };

	
	
	public static boolean isFunctionSymbol ( String symbol )
	{
		for ( int i = 0 ; i < list.length; i++ ) { 
			if ( list[i].equalsIgnoreCase(symbol) ) { return true; }
		}
		return false;
	}
	
	
	public static double calc ( String function, double value )
		throws UnknownVariableException
	{

        if ( function.equalsIgnoreCase(FUNC_ABS) ) {
            return Math.abs(value);

        } else if (function.equalsIgnoreCase(FUNC_ACOS) ) {
            return Math.acos(value);

        }  else if (function.equalsIgnoreCase(FUNC_ASIN) ) {
            return Math.asin(value);

        }  else if (function.equalsIgnoreCase(FUNC_ATAN) ) {
            return Math.atan(value);
        }  else if (function.equalsIgnoreCase(FUNC_LN) ) {
            return Math.log(value);
        }  else if (function.equalsIgnoreCase(FUNC_COS) ) {
            return Math.cos(value);
        }  else if (function.equalsIgnoreCase(FUNC_SIN) ) {
            return Math.sin(value);
        }  else if (function.equalsIgnoreCase(FUNC_SQRT) ) {
            return Math.sqrt(value);
        }  else if (function.equalsIgnoreCase(FUNC_TAN) ) {
            return Math.tan(value);

        }  else if (function.equalsIgnoreCase(FUNC_CSC) ) {
            return (Math.sin(value)/Math.cos(value));
        }  else if (function.equalsIgnoreCase(FUNC_SEC) ) {
            return (Math.cos(value)/Math.sin(value));
        }  else if (function.equalsIgnoreCase(FUNC_COT) ) {
            return (1/Math.tan(value));
        } else if (function.equalsIgnoreCase(FUNC_STEP) ) {
            //this is the really similar to step;
            if (value > 0) {return 1; }
            else { return 0; }
        } else if (function.equalsIgnoreCase(FUNC_TO_DEGREES) ) {
            return (360)/(2*Math.PI)*value;
        } else if (function.equalsIgnoreCase(FUNC_TO_RADIANS) ) {
            return (2*Math.PI)/(360)*value;
        } else if (function.equalsIgnoreCase(FUNC_EXP) ) {
            return Math.exp(value);
        } else if (function.equalsIgnoreCase(FUNC_SIGN)){
            return (value<0) ? -1 :  1;
        } else if (function.equalsIgnoreCase(FUNC_MODTWO)){
            return value % 2;
        } else if (function.equalsIgnoreCase(FUNC_RANDOM)){
            return value * Math.random();
        } else if (function.equalsIgnoreCase(FUNC_RAND)){
        	return value * Math.random();
        } else if (function.equalsIgnoreCase(FUNC_SIGNUM)){
            if ( value < 0 ) { return -1; }
            else if ( value > 0 ) { return 1; } 
            else { return 0; }
            // java 1.4.2 doesn't have signum support.
            // return Math.signum(value);
        } else {
        	throw new UnknownVariableException("Unknown Function: " + function );
        }

	}
	
}
