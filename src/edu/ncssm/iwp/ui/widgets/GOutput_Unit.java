// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.ui.widgets;

import javax.swing.*;
import java.awt.*;

public class GOutput_Unit extends GOutput_Text
{
	private static final long serialVersionUID = 1L;
	JLabel textUnit = new JLabel();

	//---- Constructors ----

	public GOutput_Unit(String iLabel, String iValue, String iUnit)
	{
		super ( iLabel, iValue );
		setUnit(iUnit);

		setLayout(new BorderLayout());

		add("West", textLabel);
		add("Center", textValue);
		add("East", textUnit);
	}

	//---- Accessors ----

	String getUnit() { return textValue.getText(); }
	String setUnit(String iValue)
	{
		if(textUnit == null) {
			textUnit = new JLabel(iValue+": ");
			return "";
		} else {

			String oldValue = textUnit.getText();
			textUnit.setText(iValue+": ");
			return oldValue;
		}
	}

}




