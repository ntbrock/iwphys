// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.ui.widgets;

import javax.swing.*;
import java.awt.*;


public class GInput_Boolean extends GInput
{

	JCheckBox stateButton = new JCheckBox();

	//---- Constructors ----

	public GInput_Boolean(String iLabel, boolean iState)
	{
		super(iLabel);
		setState(iState);

		setLayout(new BorderLayout());

		add(BorderLayout.WEST, getLabel() );
		add(BorderLayout.CENTER, stateButton);
	}


	//---- Accessors ----

	public boolean getState() { return stateButton.isSelected(); }
	public boolean setState(boolean iState)
	{
		if (stateButton == null) {
			stateButton = new JCheckBox("",iState);
			return iState;
		} else {
			boolean tmpState = stateButton.isSelected();
			stateButton.setSelected(iState);
			return tmpState;
		}
	}	


}


