// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"


package edu.ncssm.iwp.ui.widgets;

import java.awt.*;


public class GInput_Card extends GInput
{
	private static final long serialVersionUID = 1L;
	public GInput_Card(String iLabel) {

		super(iLabel);
		setLayout(new BorderLayout());
	}

	public String toString() { return getLabelText(); }
}


