// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.ui.widgets;


import javax.swing.*;
import java.awt.*;

public class GInput_Bag extends JPanel
{

	JPanel data = new JPanel();
	JPanel topData = new JPanel();

	public GInput_Bag()
	{
		data.setLayout(new FlowLayout());
		topData.setLayout(new BorderLayout());
		topData.add(data, BorderLayout.NORTH);

		data.add(new Label("Inputs"));
		JScrollPane scroll = new JScrollPane (topData);

		add(scroll);
	}


	public void addInput(GInput iWidget)
	{
		//will the scrollpane refresh correctly?!
		data.add(iWidget);
	}

}

