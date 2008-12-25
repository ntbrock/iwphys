// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"


package edu.ncssm.iwp.ui.widgets;

import javax.swing.*;
import java.awt.event.ItemListener;
import java.awt.BorderLayout;

public class GInput_Selector extends GInput
{

	JComboBox comboBox;

	public GInput_Selector(String iLabel, String[] iValues)
	{
		super(iLabel);
		
		comboBox = new JComboBox(iValues);
	
		setLayout(new BorderLayout());
		
		if ( isLabelDefined() ) { add(BorderLayout.WEST, getLabel() ); }
		add(BorderLayout.CENTER, comboBox);

	}

	public void setSelected(String iValue)
	{
		comboBox.setSelectedItem(iValue);
	}

	public String getSelected()
	{
		//IWPLog.debug(this,"[GInput_Selector] selected Item: "+comboBox.getSelectedItem());
	    for(int i=0;i<comboBox.getItemCount();i++) {
	    	//IWPLog.debug(this,"[GInput_Selector] item at "+i+": "+comboBox.getItemAt(i));
	    }
		return (String)(comboBox.getSelectedItem());

	}

	public String getValue() { return getSelected(); }
    public void addItemListener(ItemListener i) {comboBox.addItemListener(i);}
}
