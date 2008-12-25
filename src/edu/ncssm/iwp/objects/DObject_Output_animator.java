// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"


package edu.ncssm.iwp.objects;

import edu.ncssm.iwp.problemdb.DProblemState;
import edu.ncssm.iwp.ui.widgets.*;
import java.awt.*;


public class DObject_Output_animator extends DObject_animator
{
	public DObject_Output object;
	public DObject_Output_animator (DObject_Output iObject)
	{
		object = iObject;
		buildGui();
		
	}

	GOutput_Number outputWidget;

	public void buildGui()
	{
		outputWidget = new GOutput_Number(" "+object.getText(), object.getValue(),object.getUnits());

		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, outputWidget);
	}


	public void tick ( DProblemState state )
	{
		// 2006-Aug-23 brockman should resume work here.
		// Question: this should probably dsplay the value from the state?
		// Answer: NO, it's quicker to just look it up in the parent reference rather than
		// having to go all the way back to the variable hash.
		outputWidget.setValue(object.getValue());
	}

}

