// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.ui;

import edu.ncssm.iwp.plugin.IWPAnimated;
import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.objects.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import java.util.*;

import edu.ncssm.iwp.util.*;



public class GWindow_Animator_Inputs extends GAccessor_animator
	implements IWPAnimated
{
	private static final long serialVersionUID = 1L;
	
	GWindow_Animator parent;
	Collection input_animators = null;
	boolean initialized = false;
    
	JPanel panelWhereInputWidgetsLive = new JPanel();
	
	// When the user hits reset, I don't want to reset the input variables. 
	// To do that, they can reload the problem.
	DProblem lastLoadedProblem = null;
	
	
	public GWindow_Animator_Inputs ( GWindow_Animator iParent )
	{
		parent = iParent;

		// initialize my gui.

		JLabel topLabel = new JLabel ("Inputs", JLabel.CENTER );
		topLabel.setForeground ( Color.BLACK );
		topLabel.setBackground( Color.GREEN );
		topLabel.setOpaque(true);
		
		JScrollPane scroller = new JScrollPane(panelWhereInputWidgetsLive);;
		scroller.setBorder(new EmptyBorder(1,1,1,1)); // this makes the Metal line border go away.
		
		setLayout(new BorderLayout());
		add(BorderLayout.NORTH, topLabel );
		add(BorderLayout.CENTER, scroller );
		
		// no inputs to begin with.
	}

	
	// zero resets all of the widgets and rereads the problem.
	public void zero ( DProblem problem, DProblemState state )
	{

		if ( lastLoadedProblem != null && lastLoadedProblem == problem ) {
			// The problem hasn't changed. Don't change my inputs - leave
			// user defined.
			return;
		}
		lastLoadedProblem = problem;

		
		// Check Inputs
		Collection inputs = problem.getInputObjects();
		if ( inputs == null ) { 
			IWPLog.error(this,"[zero] inputs == null");
			return;
		}
		
		
		// Collect the animators for the input objects.
		input_animators = (Collection) new ArrayList( inputs.size() + 10 );
		for ( Iterator i = inputs.iterator(); i.hasNext(); ) { 
			DObject_Input o = (DObject_Input) i.next();
			if (o.isVisible()) {
				input_animators.add(o.getAnimator());
			}
		}

		
		// Clean out the UI and re-popuplate
		
		panelWhereInputWidgetsLive.removeAll();
		panelWhereInputWidgetsLive.setBorder(new EmptyBorder(5,5,5,5)); // Add some padding
		panelWhereInputWidgetsLive.setLayout(new GridLayout(input_animators.size(), 1));		    
		
		
		for ( Iterator i = input_animators.iterator(); i.hasNext(); ) { 

			panelWhereInputWidgetsLive.add ( (DObject_Input_animator) i.next() );
		}

		panelWhereInputWidgetsLive.invalidate();
		
		
		// 2006-Aug-30 brockman.
		// This is important, or else the user input values will not be set into MVariabesl
		// when the user hits reset. Was causing problems w/ the Euler initial values.
		tick(state);
		
		// Redraw
		invalidate();
		validate();		
		repaint();
		
		initialized = true;
	}

	
    public void tick (DProblemState iState)
    {
    	// The inputs do not need to do any thinking or scaling.
    	readUserValuesIntoStateVars( iState );
    }

    
	/** 
	 * Take the values in the user input boxes, and set them as the values
	 * of the input object in the DProblem
	 *
	 * 2005-oct-16 how does this get called? Now that eclipse is on the scene,
	 * I can put a breakpoint here.
	 *
	 * @author brockman
	 */
	public void readUserValuesIntoStateVars ( DProblemState iState ) 
	{
		// null check
		if ( input_animators == null ) { return; }
		for ( Iterator i = input_animators.iterator(); i.hasNext(); ) { 
			DObject_Input_animator ani = (DObject_Input_animator) i.next();
			// tell ani to read his text value, and set it as his objects
			// value
			ani.setUserDefinedValueIntoObject();
			
			// Now also set it as a variable in the problem state.
			// Doesn't this get set elsewhere?!
			
			
			
		}		
	}

}

