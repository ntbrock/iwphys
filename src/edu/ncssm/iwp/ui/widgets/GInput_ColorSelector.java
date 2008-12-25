// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.ui.widgets;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

import java.awt.event.*;


public class GInput_ColorSelector extends GInput implements ActionListener
{
	JButton colorButton = new JButton("Change");
	Color color = new Color(0,0,255);

	public void actionPerformed(ActionEvent e) 
	{
		//do it here - can determine what type of action/params
		//by looking at e. Look at class ActionEvent in book
		//to learn how to use e. 
			JFrame frame = new JFrame();
//			frame.setSize(300,300);
//			frame.setVisible(true);

			color = JColorChooser.showDialog(frame,"Color",color);

			colorButton.setBackground(color);
	}


	
	public GInput_ColorSelector(String iLabel, Color iColor) 
	{
		super (iLabel);
		setColor(iColor);
		colorButton.setBackground(color);

		colorButton.addActionListener(this);

		setLayout(new BorderLayout());

		TitledBorder titleBorder = BorderFactory.createTitledBorder( iLabel );
		titleBorder.setTitleJustification(TitledBorder.LEFT);
		setBorder(titleBorder);

		add(BorderLayout.CENTER, colorButton);
	}

	//---- Accessors ----
	
	public Color getColor() { return color; }
	public Color setColor(Color Value) 
	{
		if(color == null) {
			color=Value;
			colorButton.setBackground(color);
			return null;
		} else {

			Color oldValue = color;
			color = Value;
			colorButton.setBackground(color);
			return oldValue;
		}

	}


}
