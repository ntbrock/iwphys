// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"


package edu.ncssm.iwp.ui;


import edu.ncssm.iwp.problemdb.*;


import javax.swing.*;

public class GAccessor extends JPanel
{

	public GAccessor() {}

	public void think( DProblemState iState ) {}


	public GAccessor_designer getDesigner ( )
	{
		return new GAccessor_designer();
	}

	public GAccessor_animator getAnimator ( )
	{
		return new GAccessor_animator();
	}

	public void write()
	{}
}











