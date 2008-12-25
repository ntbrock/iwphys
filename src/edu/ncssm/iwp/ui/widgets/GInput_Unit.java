// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.ui.widgets;

import javax.swing.*;
import java.awt.*;

public class GInput_Unit extends GInput_Text
{

	JTextField inputUnit = new JTextField();

	//---- Constructors ----

	public GInput_Unit(String iLabel, String iValue, String iUnit)
	{
		super(iLabel, iValue);
		setUnit(iUnit);

		setLayout(new BorderLayout());


		if ( isLabelDefined() ) { add(BorderLayout.WEST, getLabel() ); }
		add( BorderLayout.CENTER, inputField);
		add( BorderLayout.EAST,inputUnit);
	}

	//---- Accessors ----

	public String getUnits() { return getUnit(); }
	public String getUnit() { return inputUnit.getText(); }

	public void setUnits(String iValue) { setUnit(iValue); }

	public void setUnit(String iValue)
	{
		inputUnit.setText(iValue);
	}


}




