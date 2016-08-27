// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.ui.widgets;

import javax.swing.*;


/**
 * This is the top-level input class. It just helps out with field labeling.
 * @author brockman
 *
 */


public class GInput extends JPanel
{
	private static final long serialVersionUID = 1L;
	private JLabel label = null;

	
	//---- Constructors ----
	
	public GInput(String iLabel)
	{
		setLabel(iLabel);
	}


	//---- Accessors ----
	public JLabel getLabel()
	{
		return label;
	}
	
	
	public String getLabelText()
	{
		if ( label == null ) { 
			return null;
		} else { 
			return label.getText();
		}
	}

	public void setLabel(String iValue)
	{
		// The space at the beginning in a hack to prevent the font from being sliced off a bit.
		label = new JLabel(" " + iValue+" = ");
	}

	
	protected boolean isLabelDefined()
	{
		if ( label == null ) { 
			return false;
		} else { 
			return true;
		}
	}


	
	
	public String toString() { return getLabelText(); }
}

