// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.ui;

import javax.swing.*;
import java.awt.*;


public class GWindow_Generic extends GFrame
{
	private static final long serialVersionUID = 1L;

	public GWindow_Generic(String iTitle, Component iComp)
	{

		setTitle(iTitle);

		JPanel data = new JPanel();
		data.setLayout(new BorderLayout());
		data.add("North", iComp);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", new JScrollPane(data));

		pack();

		setVisible(true);

	}


}

