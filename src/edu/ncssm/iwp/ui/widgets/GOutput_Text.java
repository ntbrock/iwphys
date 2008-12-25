// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.ui.widgets;

import javax.swing.*;
import java.awt.*;

public class GOutput_Text extends GOutput
{

	JLabel textValue = new JLabel();

	double time = 0;

	//---- Constructors ----

	public GOutput_Text(String iLabel, String iValue)
	{

		setLabel(iLabel);
		setValue(iValue);

		setLayout(new BorderLayout());

		add("West", textLabel);
		add("Center", textValue);

	}

	//---- Accessors ----

	public String getValue() { return textValue.getText(); }
	public void setValue(String iValue)
	{

		textValue.setText(iValue);
	}



}




