// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.objects;

import java.util.Collection;
import java.util.ArrayList;
import edu.ncssm.iwp.math.*;
import edu.ncssm.iwp.objects.DObjectUtility;
import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.exceptions.*;
import edu.ncssm.iwp.plugin.*;
import edu.ncssm.iwp.ui.*;
import edu.ncssm.iwp.problemdb.DProblem_designer;



public class DObject_Output
	implements IWPObject, IWPAnimated, IWPCalculated, IWPXmlable, IWPDesigned
{

	public String getIconFilename()
	{
		return "icon_DObject_Output.gif";
	}
	
	
	
	//  ---- VARIABLES ---- 

	public String name = "New_Output";
	public String text = "";
	boolean visible = true;
	double value = 0.0;
	
	public MCalculator calc = new MCalculator_AlwaysZero();
	private String equation = "";

    String units;

	//  ---- CONSTRUCTORS ---- 


	public DObject_Output()
	{
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name)
		throws InvalidObjectNameX
	{
		DObjectUtility.ensureName(name);
		this.name = name;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public void setHidden(boolean hidden) {
		this.visible = !hidden;
	}

	// IWP Animated
	
	public void zero( DProblem problem, DProblemState state )
		throws UnknownVariableException, InvalidEquationException, UnknownTickException
	{
		tick (state);
	}

	public void tick ( DProblemState state )
		throws UnknownVariableException, InvalidEquationException, UnknownTickException
	{		
		if (calc != null) {
			
			try {
				value = calc.calculate( state.vars() );
			} catch ( UnknownVariableException x ) { 
				throw new UnknownVariableException( x.getMessage() + " (in " + this.getName() + " Output)");
			}
			
			state.vars().setAtCurrentTick( getName() + "."+MVariables.VALUE,value);
		    state.vars().setAtCurrentTick( getName(),value);
		}
	}


	public Collection getRequiredSymbols() throws InvalidEquationException
	{
		return this.calc.getRequiredSymbols();
	}

	
	
	public Collection getProvidedSymbols() throws InvalidEquationException
	{
		ArrayList out = new ArrayList(5);
		
		out.add( getName() + ".value" );
		out.add( getName() );
		
		return out;
	}


	public void setEquation ( String equation )
	{
		this.equation = equation;
		calc = new MCalculator_Parametric( this.equation );
	}

    public String getUnits() {return units;}
    public void setUnits(String units) {this.units=units;}
	
	public void setCalculator ( MCalculator calc )
	{
		this.calc = calc;
	}

	public MCalculator getCalculator ( ) { return calc; }



	/*----------------------------------------------------------------------*/
	/* DESIGNERS */
	/*----------------------------------------------------------------------*/

	public GAccessor_designer getDesigner ( ) {
		return new DObject_Output_designer ( this );
	}

	public DObject_animator getAnimator ( ) { 
		return new DObject_Output_animator ( this );
	}


	/*----------------------------------------------------------------------*/

	public IWPObjectXmlCreator newXmlObjectCreator()
	{
		return new DObject_OutputXMLCreator(this);
	}

	public IWPObjectXmlHandler newXmlObjectHandler()
	{
		return new DObject_OutputXMLHandler();
	}
}




