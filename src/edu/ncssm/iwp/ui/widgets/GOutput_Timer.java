// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.ui.widgets;

import javax.swing.*;
import java.awt.*;
import java.text.*;

public class GOutput_Timer extends GOutput
{
	private static final long serialVersionUID = 1L;
	JLabel textValue = new JLabel();

	double time = 0;

	//---- Constructors ----
    
        public GOutput_Timer(String iLabel, String iValue)
	{
		setLabel(iLabel);
		setValue(iValue);


		setLayout(new BorderLayout());

		add("West", textLabel);
		add("East", textValue);

	}
    public GOutput_Timer(String iLabel,double iValue) {
	this(iLabel,iValue+"");
    }

	//---- Accessors ----

	String getValue() { return textValue.getText(); }
	void setValue(String iValue)
	{
		textValue.setText(iValue);
	}


	public void setValue(double iTime)
	{

	    DecimalFormat format=new DecimalFormat();
	    format.setMaximumFractionDigits(14);

	    if(iTime<.001 && iTime!=0.0) {
		format.applyPattern("0.0###E0");
		setValue(format.format(iTime)+" s");
	    }else {
		format.applyPattern("0.0###");
		setValue(format.format(iTime)+" s");
	    }

	      /*sweeneyb
	      I felt the need to re-write this call because
	      there was a trade-off between precision and 
	      the size of the number that would be held.
	      With 7 decimal places of precision, only 214 
	      or so seconds could be displayed.
	    */
	    // Brockman 2007-Feb-15 - We could use NumberFOrmat here.
	}

}


