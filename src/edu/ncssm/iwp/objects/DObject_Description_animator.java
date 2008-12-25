// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.objects;

import java.awt.*;


public class DObject_Description_animator extends DObject_animator
{
	DObject_Description object;

	public DObject_Description_animator ( DObject_Description ipoObject )
	{
		object = ipoObject;

		setLayout(new BorderLayout());
		add("Center", new Label ( "[THIS SHOULD BE THE WORD WRAPPING CODE]" ));
		setVisible(true);
	}

}



