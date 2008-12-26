package edu.ncssm.iwp.math.designers;

import edu.ncssm.iwp.math.MCalculator_Parametric;
import edu.ncssm.iwp.ui.GAccessor_designer;
import edu.ncssm.iwp.exceptions.*;


/**
 * This is a standalone simple designer that is an alternative to the MCalculator_designer.
 * 
 * This is useful for the case where you just want to put in a constant or something and
 * don't want the full selectability of the Euler, RK, etc.
 * 
 * It's really just a lightweight wrapper that exposes the Parametric subdesigner.
 * 
 * @author brockman
 *
 */

public class MCalculator_Parametric_simpleDesigner extends GAccessor_designer
{
	String label;
	MCalculator_Parametric_subDesigner subDesign;


	
	public MCalculator_Parametric_simpleDesigner ( String label )
	{
		subDesign = (MCalculator_Parametric_subDesigner)(new MCalculator_Parametric()).getSubDesigner(label);
		buildGui();
	}
	
	public MCalculator_Parametric_simpleDesigner ( String label, MCalculator_Parametric calc )
	{
		this.subDesign = (MCalculator_Parametric_subDesigner)calc.getSubDesigner(label);		
		buildGui();
	}
	

	public MCalculator_Parametric_simpleDesigner ( String label, MCalculator_Parametric calc, int inputLength )
	{
		this.subDesign = (MCalculator_Parametric_subDesigner)calc.getSubDesigner(label, inputLength);		
		buildGui();
	}
	
	// 2008-Dec-25 brockman
	public MCalculator_Parametric_simpleDesigner ( String label, MCalculator_Parametric calc, int inputLength, boolean inputOnNewLine )
	{
		this.subDesign = (MCalculator_Parametric_subDesigner)calc.getSubDesigner(label, inputLength, inputOnNewLine );
		buildGui();
	}

	
	
	private void buildGui()
	{
		this.add(subDesign);
	}
	
	
	// TODO, this is just an equation ediutor.
	
	public MCalculator_Parametric get()
		throws InvalidEquationException
	{
		return (MCalculator_Parametric)subDesign.getCalculator();
	}

	
}
