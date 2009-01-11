// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.ui.widgets;

import javax.swing.*;
import java.awt.*;

public class GOutput extends JPanel
{
	private static final long serialVersionUID = 1L;
	JLabel textLabel = new JLabel();

	//---- Constructors ----

	public GOutput() {}


	//NOTE: is this necessary?
	public GOutput(String iLabel) 
	{
		setLabel(iLabel);

		setLayout(new BorderLayout());

		add("West", textLabel);
	}

	//---- Accessors ----

	//text (g/s)
	String getLabel() { return textLabel.getText(); }
	String setLabel(String iValue)
	{
		if(textLabel == null) {
			textLabel = new JLabel(iValue+": ");
			return "";
		} else {

			String oldValue = textLabel.getText();
			textLabel.setText(iValue+": ");
			return oldValue;
		}
	}


}

