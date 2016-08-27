// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.objects;

/*
import edu.ncssm.iwp.ui.widgets.*;
import edu.ncssm.iwp.ui.*;
import edu.ncssm.iwp.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
*/


/**
 * This class now gets attached to the animator to pass back the button
 * click events to control the time flow of the entire program.
 * 
 * NOT USED ANY MORE? Functionality in GWindow_ANimator_Time?
 * 
 * I actually like this location a bit more because it fits into the
 * inital naming convention.
 * 
 * @author brockman
 *
 */

public class DObject_Time_animator extends DObject_animator
{
	private static final long serialVersionUID = 1L;
	/*
	GWindow_Animator animator;
	DObject_Time object;


	JPanel infoPanel = new JPanel();
	JPanel buttonPanel = new JPanel();


	GOutput_Timer timer;


	JButton buttonStepBackward = new JButton("<<");
	JButton buttonBackward = new JButton("<");
	JButton buttonForward = new JButton(">");
	JButton buttonStepForward = new JButton(">>");
	JButton buttonReset = new JButton("Reset");
	JButton buttonStop = new JButton("Stop");



	public DObject_Time_animator ( DObject_Time iObject )
	{	
		object = iObject;
		
		//pull in the list of objects+ make a new GAccessor_ObjectInput for each
		timer = new GOutput_Timer("Time", "");

		infoPanel.setLayout(new GridLayout(1,1));


		buttonStepBackward.addActionListener(this);
		buttonBackward.addActionListener(this);
		buttonForward.addActionListener(this);
		buttonStepForward.addActionListener(this);
		buttonReset.addActionListener(this);
		buttonStop.addActionListener(this);

		buttonPanel.setLayout(new GridLayout(3,2));
		buttonPanel.add(buttonBackward);
		buttonPanel.add(buttonForward);
		buttonPanel.add(buttonStepBackward);
		buttonPanel.add(buttonStepForward);
		buttonPanel.add(buttonStop);
		buttonPanel.add(buttonReset);

		//slider later

		setLayout(new BorderLayout());

		add("North",infoPanel);
		add("Center",timer);
		add("South",buttonPanel);
	}

	

	//this class talks directly to and from the brain, getting and reading inputs


	public void think()
	{
		timer.setValue(object.getTime());
		// should probably cascae a refresh to all my elements 
	}
*/

}




