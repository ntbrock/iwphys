package edu.ncssm.iwp.math;

/**
 * This is a calculator that will always return zero. Good to use
 * for initialization of objects.
 * I have overriden Parametric, so that I preserve all of my GUI controls.
 * @author brockman
 *
 */

public class MCalculator_AlwaysZero extends MCalculator_Parametric
{

	public MCalculator_AlwaysZero()
	{
		super("0");
	}

}
