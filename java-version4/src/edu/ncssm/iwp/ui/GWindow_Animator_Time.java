// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.ui;

import edu.ncssm.iwp.plugin.IWPAnimated;
import edu.ncssm.iwp.problemdb.*;

import java.awt.*;
import javax.swing.*;

import java.awt.event.*;

import edu.ncssm.iwp.util.*;



/* should make this look cool one day. Maybe a picture of a stopwatch? */

/**
 * WARNING: This is oddly like the DObject_Time_Animator - remove duplicateion
 * one day.
 * brockman 2006-Apr-29
 * TODO
 */

public class GWindow_Animator_Time extends GAccessor_animator
	implements ActionListener, IWPAnimated
{
	private static final long serialVersionUID = 1L;
	
	GWindow_Animator parent;

    //JPanel infoPanel = new JPanel();
	JPanel buttonPanel = new JPanel();


    //GOutput_Number scalex;
    //GOutput_Number scaley;
    //GOutput_Timer timer;


	JButton buttonStepBackward = new JButton("<<");
	JButton buttonBackward = new JButton("<");
	JButton buttonForward = new JButton(">");
	JButton buttonStepForward = new JButton(">>");
	JButton buttonReset = new JButton("Reset");
	JButton buttonStop = new JButton("Stop");


	public GWindow_Animator_Time ( GWindow_Animator iParent )
	{
		parent = iParent;

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

		//add("North",infoPanel);
		//add("Center",timer);
		add("South",buttonPanel);
	}
    /**
     *Method intended to be called after the animator is constructed.
     *It will hopefully reset so that Euler works.
     */
    public void clickReset() {buttonReset.doClick();}
    public void clickStepForward(){buttonStepForward.doClick();}
    public void clickStepBackward(){buttonStepBackward.doClick();}

    
    
	public void actionPerformed(ActionEvent e)
	{
		JButton source = ((JButton)(e.getSource()));

		if ( parent == null ) {
			IWPLog.info(this,"[GWindow_Animator][actionPerformed] MAJOR ERROR: problem is NULL!");
		} else {

		if (source == buttonStepBackward ) {
			parent.handleTimeStepBackwardButtonPress();
		} else if (source == buttonBackward ) {
			parent.handleTimeBackwardButtonPress();
		} else if (source == buttonStepForward ) {
			parent.handleTimeStepForwardButtonPress();
		} else if (source == buttonForward ) {
			parent.handleTimeForwardButtonPress();
		} else if (source == buttonReset ) {
			parent.handleTimeResetButtonPress();
		} else if (source == buttonStop ) {
			parent.handleTimeStopButtonPress();
		} else {}

		}

	}

	public void zero ( DProblem problem, DProblemState iState )
	{		
	}

	public void tick( DProblemState iState )
	{
		// this could update the clock value.
		// DO I even display the clock value in this view?
	}

}
