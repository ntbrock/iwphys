// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.ui;

import edu.ncssm.iwp.ui.widgets.*;
import edu.ncssm.iwp.plugin.IWPAnimated;
import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.exceptions.*;
import edu.ncssm.iwp.objects.*;


import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.*;

import edu.ncssm.iwp.util.*;



public class GWindow_Animator_Outputs
	extends GAccessor_animator implements IWPAnimated
{
	private static final long serialVersionUID = 1L;
	
	GWindow_Animator parent;
	Collection output_animators = null;

	JPanel timerPanel;
	GOutput_Timer timer;

	boolean initialized = false;
	JPanel panelWhereOutputWidgetsLive = new JPanel();
		
	public GWindow_Animator_Outputs(GWindow_Animator iParent)
	{
		parent = iParent;
		
		timer = new GOutput_Timer("Time", "");
		timerPanel = new JPanel();
		timer.setBorder(new EmptyBorder(5,5,5,5));
		
		// initialize my gui.
		JLabel topLabel = new JLabel ("Outputs", JLabel.CENTER );
		topLabel.setForeground ( Color.WHITE );
		topLabel.setBackground( Color.RED );
		topLabel.setOpaque(true);
		
		JScrollPane scroller =  new JScrollPane(panelWhereOutputWidgetsLive);
		scroller.setBorder(new EmptyBorder(1,1,1,1)); // this makes the Metal line border go away.
		
		setLayout(new BorderLayout());
		add(BorderLayout.NORTH, topLabel );
		add(BorderLayout.CENTER,scroller);
			
		add(BorderLayout.SOUTH, timer );
		// no inputs to begin with.
		
	}


	
	public void zero(DProblem problem, DProblemState state)
		throws UnknownVariableException, UnknownTickException
	{
		Collection outputs = problem.getOutputObjects();

		// inputs bounds checking
		if (outputs == null) {
			IWPLog.error(this,"[zero] outputs == null");
			return;
		}

		
		// Collect the animators for the output objects.
		output_animators = (Collection) new ArrayList( outputs.size() + 10 );
		for ( Iterator i = outputs.iterator(); i.hasNext(); ) { 
			DObject_Output o = (DObject_Output) i.next();
			if (o.isVisible()) {
				output_animators.add(o.getAnimator());
			}
		}

		panelWhereOutputWidgetsLive.removeAll();
		panelWhereOutputWidgetsLive.setBorder(new EmptyBorder(5,5,5,5)); // Add some padding
		panelWhereOutputWidgetsLive.setLayout(new GridLayout(output_animators.size(), 1, 5, 5));		    
		
		for ( Iterator i = output_animators.iterator(); i.hasNext(); ) { 
			DObject_Output_animator ani = (DObject_Output_animator) i.next();
			panelWhereOutputWidgetsLive.add ( ani );
		}	
		
		// Do I need to do the initial tick?
		// yes, because the reset 
		// TODO make this work.
		tick(state);
		
		// Redraw
		invalidate();
		validate();		
		repaint();
		
		initialized = true;
		
	}


	/**
	 * Look at the problem state and update the vairables.
	 */
	public void tick(DProblemState state)
		throws UnknownVariableException, UnknownTickException
	{

		if (output_animators != null) {
			Iterator i = output_animators.iterator();

			while (i.hasNext()) {
				DObject_Output_animator o = (DObject_Output_animator) i.next();
				o.tick( state );
			}
		}

		// now update the time number.
		timer.setValue( state.vars().getCurrentTime() );
	}


}
