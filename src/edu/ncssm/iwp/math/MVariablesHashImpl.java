package edu.ncssm.iwp.math;

import java.util.Hashtable;
import java.util.Iterator;

import edu.ncssm.iwp.exceptions.UnknownVariableException;

/**
 * A basic implementation of the MVariabels interface that uses a hash behind it.
 * I don't like this because it does a lot of new Double().
 * @author brockman
 *
 */

public class MVariablesHashImpl extends Hashtable implements MVariables
{

	public double get(String name)
		throws UnknownVariableException
	{

		Double val = (Double) super.get(name);
		if ( val == null ) { 
			throw new UnknownVariableException(name);
		} else { 
			return val.doubleValue();
		}
	}

	public void set(String name, double value)
	{
		super.put(name, new Double(value));
	}

	public Iterator iterator()
	{
		return super.keySet().iterator();
	}
}
