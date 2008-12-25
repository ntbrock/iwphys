package edu.ncssm.iwp.math.designers;

import edu.ncssm.iwp.exceptions.InvalidEquationException;
import edu.ncssm.iwp.math.MCalculator;
import edu.ncssm.iwp.ui.GAccessor_designer;

/**
 * The sub designers now extend this guy instead of extending the top-level generic MCalculator_designer.
 * @author brockman
 *
 */

public abstract class MCalculator_Abstract_subDesigner extends GAccessor_designer
{
	/**
	 * Create a new instance of the Calculator that was laid out in the swing.
	 * @return
	 */
	
	public abstract MCalculator getCalculator ( )
		throws InvalidEquationException;
}
