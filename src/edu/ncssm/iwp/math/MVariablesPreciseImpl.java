package edu.ncssm.iwp.math;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Iterator;

import edu.ncssm.iwp.exceptions.UnknownVariableException;

/**
 * A slightly smarter implementation of MVariables that wlll try to counteract
 * double inpercision.
 * 
 * @author brockman
 *
 */

public class MVariablesPreciseImpl extends Hashtable implements MVariables
{
	public double get(String name)
		throws UnknownVariableException
	{

		BigDecimal val = (BigDecimal) super.get(name);
		if ( val == null ) { 
			throw new UnknownVariableException(name);
		} else { 
			double in = val.doubleValue();
			double fixed = MEquation.fixPrecision ( in );
			//System.err.println("Precise: " + in + " to " + fixed );
			return fixed;
		}
	}

	public void set(String name, double value)
	{
		try { 
			super.put(name, new BigDecimal(value+""));
		} catch ( NumberFormatException x ) { 
			super.put(name, new BigDecimal(0));
		}
	}


	public Iterator iterator()
	{
		return super.keySet().iterator();
	}
}
