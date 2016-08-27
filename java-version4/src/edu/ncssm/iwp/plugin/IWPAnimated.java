package edu.ncssm.iwp.plugin;

import edu.ncssm.iwp.exceptions.InvalidEquationException;
import edu.ncssm.iwp.exceptions.UnknownTickException;
import edu.ncssm.iwp.exceptions.UnknownVariableException;
import edu.ncssm.iwp.problemdb.*;


/**
 * Brockman, sick of the confusing init() and resets() in the old
 * iwp code, created this interface. Any animated panel or widet
 * should implement it to receive signals from the top-end.
 * 
 * This interface is implemented by both DObject abstract base class
 * and by the GWindow_Animator listeners. It's double used, but tick
 * and zero seem to be a good way to do everything across the system.
 * 
 * Note that they have slightly different meanings if you're using
 * them in a DObject calculation context -vs- a GWindow render ctx.
 * 
 * @author brockman
 */

public interface IWPAnimated
{
	/**
	 * Re-read the problem to refresh your contents, and set all of
	 * your inital values into the state at tick=0.
	 * Brockman re-did all of this on 2006-Apr-29
	 * @author brockman
	 * 
	 * @throws UnknownVariableException NEW: If the variable is missing from the 
	 * 	CURRENT Tick. I am now properly re-ordering the calculation pipeline.
	 * 
	 */
	public void zero ( DProblem problem, DProblemState state )
		throws UnknownVariableException, InvalidEquationException, UnknownTickException;

	
	/**
	 * The top-level GWindow_Animator will call this on all of its
	 * widgets. Nodes are responsible for passing the message down.
	 * 
	 * This is called with each tick of the animation. It asks the widget
	 * to update it's display to the latest values.
	 * 
	 * When the user hit's 'reset' on the clock, it sends a think(0), instead
	 * of a reset. This way, the inputs can know if they're loading or not.
	 * 
	 * @author brockman
	 *
	 * @param state
	 *
	 * @throws UnknownVariableException NEW: If the variable is missing from the 
	 * 	CURRENT Tick. I am now properly re-ordering the calculation pipeline.
	 *
	 */

	public void tick ( DProblemState state )
		throws UnknownVariableException, UnknownTickException, InvalidEquationException;

}
