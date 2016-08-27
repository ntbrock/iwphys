// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.math;

import java.math.BigDecimal;
import java.util.Collection;

import edu.ncssm.iwp.exceptions.InvalidEquationException;
import edu.ncssm.iwp.exceptions.UnknownTickException;
import edu.ncssm.iwp.exceptions.UnknownVariableException;
import edu.ncssm.iwp.util.IWPLog;

/**
 * MEquation. This heart + soul of the math engine.
 * 
 * Calls a recursive subclass to pick apart the equation
 * 
 * @author brockman
 *
 */

public class MEquation
{
	
	String equationString;
	MEquation_Parser parser;
	MConstants constants;
	

	public MEquation ()
	{
		equationString = "0";
		try { 
			parser = parseEquation();
		} catch ( InvalidEquationException e ) {
			IWPLog.x(this, "CRITICAL: Got an invalidEquationX on '0': " + e.getMessage(), e );
			// can't happen.
		}
	}
	
	public MEquation( String iEqn )
		throws InvalidEquationException
	{
		equationString = iEqn;
		parser = parseEquation();
	}
	
	
	
	private MEquation_Parser parseEquation()
		throws InvalidEquationException
	{
		/*
		int divider=0;
		for(Enumeration e= consts.keys();e.hasMoreElements();) {
		    String key=(String)e.nextElement();
		    //IWPLog.info(this,"[MEquation] key: "+key);
		    divider=equationString.indexOf(key);
		    while(divider!=-1) {
		    	equationString=equationString.substring(0,divider)+consts.get(key)+equationString.substring(divider+key.length(),equationString.length());
		    	divider=equationString.indexOf("PI.value",divider);
		    }
		}
		*/
		//create eqation.
		return new MEquation_Parser(equationString);
		
	}


	/**
	 * This is the magic. Is it really efficient to do all this
	 * hashtable construction every time we do a calculation?
	 * 
	 * @param vars
	 * @return
	 * @throws UnknownVariableException
	 * @throws InvalidEquationException
	 */
	
	public double calculate ( MDataHistory vars )
		throws UnknownVariableException, InvalidEquationException,
		UnknownTickException
	{
		return calculate ( vars.getCurrentTickVars() );
	}

	/**
	 * Alternate method of calculate that uses a hash instead of mvariables.
	 * @param hash
	 * @return
	 * @throws UnknownVariableException
	 * @throws InvalidEquationException
	 */
	
	public double calculate ( MVariables vars ) 
	 	throws UnknownVariableException, InvalidEquationException
 	{
		if ( parser == null ) { throw new InvalidEquationException ( "Null Parser = null"); }
		double answer = parser.calculate( vars );

		return answer;
 	}
	
	/**
	 * IWP3 brockman 2007-Jan-29. Part of Calculation ordering.
	 * Return a collection of Strings that are the variables in this equation.
	 * @return
	 */
	
	public Collection listVariables()
		throws InvalidEquationException
	{
		// NOTE: Constants + Functions are trimmed below.
		return parser.listRequiredVariables();
	}
	
	
	public String getEquationString()
	{
		return equationString;
	}
	
	
	public void checkValidity()
		throws InvalidEquationException
	{
		parseEquation();
	}

	
	/**
	 * Return true if the symbol is reserved and shouldn't be used in a user-defined object name.
	 * I am doing this now because there was a buggy problem that used e as a variable name, which
	 * is a constant now.
	 * 
	 * 2007-June-03 brockman
	 */
	
	public static boolean isSymbolReservedFunctionOrConstant ( String symbol )
	{
		// Note: I'm not checking the InjectedSymbols here on purpose. Too many existing problems have 
		// inputs named 'd' 'v'
		if ( MConstants.isConstantSymbol(symbol) ) { return true; }
		if ( MFunctions.isFunctionSymbol(symbol) ) { return true; }
		return false;
	}	
	
	
	
	/**
	 * Brockman's attmpt to get rid of the .00000000000002 double problem.
	 * @param n
	 * @return
	 */
	
	private static double expansionFactor = 1000000000;
	private static int expansionDigits = 9;
	
	public static double fixPrecision ( double n )
	{
		if ( Math.abs(n) < 10 ) {
			BigDecimal bd = new BigDecimal((int) (n * expansionFactor));
			return bd.movePointLeft(expansionDigits).doubleValue();
		} else {
			return n;
		}

	}
	
}
