// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.ui.widgets;

import javax.swing.*;
import java.awt.*;

public class GOutput_Number extends GOutput
{
	private static final long serialVersionUID = 1L;
    JLabel textValue = new JLabel();
    
    double num = 0;
    String units="";

	//---- Constructors ----

    public GOutput_Number(String iLabel, double iValue)
    {
    	setLabel(iLabel);
    	num = iValue;
    	setValue(iValue+"");
	
    	setLayout(new BorderLayout());
	
    	add( BorderLayout.WEST, textLabel);
    	add( BorderLayout.EAST, textValue);
    	//setPreferredSize(new Dimension(100,20));
    }
    
    public GOutput_Number(String iLabel, double iValue, String units)
    {
    	setLabel(iLabel);
    	num = iValue;
    	//while(units.length()<DObject.MIN_UNIT_LENGTH) {
    	//	units+=" ";
    	//}
    	if(units!="") {
    		this.units=" "+units;
    	}
    	setValue(iValue+"");
	
    	setLayout(new BorderLayout());
	
    	add(BorderLayout.WEST, textLabel);
    	add(BorderLayout.EAST, textValue);
    }

    //---- Accessors ----

    String getValue() { return textValue.getText(); }
    void setValue(String iValue)
    {
    	textValue.setForeground(Color.red);
    	textValue.setText(iValue+units);
    }
    
    
    public void setValue(double iNumber)
    {
    	// reduce to 4 decimal
    	// num = ( (double)( (int)(iNumber*10000) ) ) / 10000;
    	num=iNumber;
    	setValue(GNumber_formatting.format(iNumber));
    }

}


