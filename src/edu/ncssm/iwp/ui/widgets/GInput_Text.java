// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"


package edu.ncssm.iwp.ui.widgets;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class GInput_Text extends GInput
{
	boolean inputOnNewLine = false;
	int inputLength = FIELD_WIDTH;
	static int FIELD_WIDTH = 20;

	public JTextField inputField;
	private Color origBackground; // the background color before we set any errors.

	
	public GInput_Text(String iLabel, String iValue)
	{
		super(iLabel);
		setValue(iValue);
		buildGui();
	}

	public GInput_Text(String iLabel, String iValue, int inputLength)
	{
		super(iLabel);
		this.inputLength = inputLength;
		
		setValue(iValue);
		buildGui();
	}

	public GInput_Text(String iLabel, String iValue, int inputLength, boolean inputOnNewLine )
	{
		super(iLabel);
		this.inputLength = inputLength;
		this.inputOnNewLine = inputOnNewLine;
		
		setValue(iValue);
		buildGui();
	}
		
	
	
	//------------------
	
	private void buildGui() 
	{
		setLayout(new BorderLayout() );
		
		// 2008-Dec-25 
		if ( this.inputOnNewLine ) { 

			if ( isLabelDefined() ) { add(BorderLayout.NORTH, getLabel()); }
			add(BorderLayout.CENTER, inputField);

		} else { 
			// Classic behavior 
			if ( isLabelDefined() ) { add(BorderLayout.WEST, getLabel()); }
			add(BorderLayout.CENTER, inputField);	
		}

		origBackground = inputField.getBackground();
	}

	
	//---- Accessors ----

	//value (g/s)
	public String getValue() { return inputField.getText(); }
	public String setValue(String iValue)
	{
		if(inputField == null) {
			inputField = new JTextField(iValue, inputLength );
			return "";
		} else {

			String oldValue = inputField.getText();
			inputField.setText(iValue);
			return oldValue;
		}
	}
	public double getDoubleValue() { 

		try {
			return Double.valueOf(getValue()).doubleValue();
		} catch (NumberFormatException e) {
			return 0;

		}
	}

	// IWP 3 - We can show error input now as well. For Equation editing

	public void setError(String message)
	{
		inputField.setBackground(Color.YELLOW);
		inputField.setToolTipText(message);
	}
		
	public void releaseError()
	{
		inputField.setBackground(origBackground);
		inputField.setToolTipText(null);
	}
	
	public void addKeyListener(KeyListener listener )
	{
		inputField.addKeyListener(listener);
	}
	
	public void addActionListener(ActionListener listener )
	{
		inputField.addActionListener(listener);
	}

	public void addDocumentListener(DocumentListener listener )
	{
		inputField.getDocument().addDocumentListener(listener);
	}
	
}




