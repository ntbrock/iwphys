/**
 * 
 * MCalculator_Parametric_designer 
 * The Designer Class for the Parametric Equations 
 * @author brockman
 * @date 02/17/01
 * @date 2007-Feb-01
 * 
 * Updated: This is now used by MCalculator_Parameteric_simpleDesigner as well
 * as the generic MCalculator_designer
 * 
 * 
 */

package edu.ncssm.iwp.math.designers;

import edu.ncssm.iwp.math.MCalculator;
import edu.ncssm.iwp.math.MCalculator_Parametric;
import edu.ncssm.iwp.util.*;
import edu.ncssm.iwp.exceptions.*;


public class MCalculator_Parametric_subDesigner
	extends MCalculator_Abstract_subDesigner 
{

	MCalculator_Parametric object;
	MEquation_Editor editor;

	public MCalculator_Parametric_subDesigner ( MCalculator_Parametric iObject, String label )
	{
		editor = new MEquation_Editor( label, iObject.getEquation() );
		add(editor);
	}

	public MCalculator_Parametric_subDesigner ( MCalculator_Parametric iObject, String label, int inputLength )
	{
		editor = new MEquation_Editor( label, iObject.getEquation(), inputLength );
		add(editor);
	}

	// 2008-Dec-25, Request to make the polygon input fields bigger
	public MCalculator_Parametric_subDesigner ( MCalculator_Parametric iObject, String label, int inputLength, boolean inputOnNewLine )
	{
		editor = new MEquation_Editor( label, iObject.getEquation(), inputLength, inputOnNewLine );
		add(editor);
	}

	public MCalculator getCalculator ( )
		throws InvalidEquationException
	{
		return new MCalculator_Parametric ( editor.getEquation() );
	}

	public void write ( )
	{
		IWPLog.error(this,"[MCalculator_Parametric_designer] write ( ) not finished" );
	}

	
}


