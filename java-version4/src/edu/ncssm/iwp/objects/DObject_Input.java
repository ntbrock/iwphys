// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.objects;

import java.util.ArrayList;
import java.util.Collection;

import edu.ncssm.iwp.exceptions.InvalidEquationException;
import edu.ncssm.iwp.exceptions.InvalidObjectNameX;
import edu.ncssm.iwp.plugin.*;
import edu.ncssm.iwp.problemdb.DProblem;
import edu.ncssm.iwp.problemdb.DProblemState;
import edu.ncssm.iwp.ui.GAccessor_designer;


/**
 * Core Object: Collects user input
 * @author brockman
 *
 */

public class DObject_Input
	implements IWPObject, IWPAnimated, IWPCalculated, IWPXmlable, IWPDesigned, IWPCalculationOrder
{
	// IWP 4.5 - Each solid has a calculation order
	int calculationOrder = -1;
	public int getCalculatorOrder() { return calculationOrder; }
	public void setCalculatorOrder(int order) { this.calculationOrder = order; }


	public String getIconFilename()
	{
		return "icon_DObject_Input.gif";
	}
	
	
	//  ---- VARIABLES ---- 
	String name = "New_Input";
	String text;
	boolean visible = true;
	double initialValue = 0.0;
	double value = 0.0;
	double userValue = 0.0;
	boolean userValueDefined = false;
    String units="";

	//  ---- CONSTRUCTORS ---- 

	public DObject_Input()
	{
	}
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setHidden(boolean hidden) {
		this.visible = ! hidden;
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

	public void setValue(double value) {
		this.value = value;
	}

	public DObject_animator getObject_animator ( ) { 
		return new DObject_Input_animator ( this );
	}

	public void zero ( DProblem problem, DProblemState state )
	{
		tick(state);
	}

	/**
	 * I do want to record the value w/ every tick, in case it changes.
	 */
	public void tick ( DProblemState state ) {
		
		state.vars().setAtCurrentTick( getName() + ".value", getValue() );
	    state.vars().setAtCurrentTick( getName(), getValue() );

	}


	public Collection getRequiredSymbols() throws InvalidEquationException
	{
		return null;
	}

	public Collection getProvidedSymbols() throws InvalidEquationException
	{
		ArrayList out = new ArrayList(5);
		out.add ( getName() + ".value");
		out.add ( getName());
		return out;
	}



	// this is defined by the user
	public void setUserValue ( double userValue )
	{
		this.userValueDefined = true;
		this.userValue = userValue;
	}


	public void setInitialValue ( double initialValue )
	{
		this.initialValue = initialValue;
	}

	public double getInitialValue ( ) 
	{
		return initialValue;
	}

	//I need to override DObject's getValue method
    //because i have possibly 2 values.
    public double getValue()
    {
    	return (userValueDefined ? userValue : initialValue);
    }

    public String getUnits() {return this.units;}
    public void setUnits(String units) {this.units=units;}




	/*----------------------------------------------------------------------*/
	/* DESIGNERS */
	/*----------------------------------------------------------------------*/

	public GAccessor_designer getDesigner ( ) {
		return new DObject_Input_designer ( this );
	}

	public DObject_animator getAnimator ( ) { 
		return new DObject_Input_animator ( this );
	}

	/*----------------------------------------------------------------------*/

	public IWPObjectXmlCreator newXmlObjectCreator()
	{
		return new DObject_InputXMLCreator(this);
	}

	public IWPObjectXmlHandler newXmlObjectHandler()
	{
		return new DObject_InputXMLHandler();
	}
}
















