// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.ui;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Provides a routune for centering a window.
 * @author brockman
 *
 */

public class GFrame extends JFrame
{

	public GFrame() {}

	public void centerOnScreen()
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();        
		setLocation( (screenSize.width - this.getWidth()) / 2,
				     (screenSize.height - this.getHeight() ) / 2 );
	}

}

