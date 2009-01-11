
/*
  DProblemState
  This defines which way time is flowing + how quickly.

  Author: Taylor Brockman
  Date: 6/16/01
*/


package edu.ncssm.iwp.problemdb;
import edu.ncssm.iwp.exceptions.UnknownTickException;
import edu.ncssm.iwp.exceptions.UnknownVariableException;
import edu.ncssm.iwp.math.MDataHistory;
import edu.ncssm.iwp.math.MDataHistoryArrayListImpl;
import edu.ncssm.iwp.util.IWPLog;

/**
 * As of 2006-Apr-29, The DProblemState implements the MVariables object,
 * so I can use it to pull any variable there is in the problem.
 * 
 * @author brockman
 *
 */

public class DProblemState extends DEntity
{
	private static final long serialVersionUID = 1L;
	public final static int FORWARD = 1;
	public final static int STOP = 0;
	public final static int BACKWARD = -1;

	// Reference back to the problem we're operating on.
	public DProblem problem;
	
	// Memory array of all vars + historical values. big.
	// TODO when this is initall contrustructed all the init values
	// should be set at tick zero.
	MDataHistoryArrayListImpl vars;
	
	// Tick direction
	int tickDirection;

	// ticks 
	int tick;
	int stopTick;
	
	// Mark this if it's a clone or copy of.
	boolean copyOfPreservedPointers = false;

	// 2007-Jun-03 brockman. Keeping synchronized track of if I'm in masterTick or not. 
	// trying to fix the high FPS thread contest issues.
	private boolean tickInProgress = false;

	/*--------------------------------------------------------------------*/

	/**
	 * This is the proper way to create a zero'ed problem state.
	 * GWindow_Animator_Ticker calls this method to when a problem is
	 * loaded or reset.
	 * 
	 * brockman 2006-Apr-28
	 */
	
	public DProblemState ( DProblem iProblem )
	{
		vars = new MDataHistoryArrayListImpl();
		problem = iProblem;

		tickDirection = STOP;
		tick = 0;
		stopTick = problem.getTime().calculateStopTick();
	}

	
	/**
	 * This is used by the copyOfButPreservePointers method, which is used to
	 * patch up the rendering layer where it was drawing a picture mid-tick.
	 * 2006-Aug-30 brockman V3.0
	 *  
	 * @param problem
	 * @param vars
	 * @param tickDirection
	 * @param tick
	 */
	
	private DProblemState ( DProblem problem, MDataHistoryArrayListImpl vars,
							int tickDirection, int tick, int stopTick )
	{
		this.problem = problem;
		this.vars = vars;
		this.tickDirection = tickDirection;
		this.tick = tick;
		this.stopTick = stopTick;
		this.copyOfPreservedPointers = true;
	}
	
	
	
	/**
	 * Return the pointer to the current problem. this is for
	 * convenience.
	 * @return
	 */
	
	public DProblem getProblem () {
		return problem;
	}

	
	/**
	 * This is called with every clock tick that goes by. For now,
	 * This is just a pure counter of the number of steps that have
	 * been taken in a certain direction and it is not aware of the
	 * boundaries. This may change with my current refactoring.
	 */
	public synchronized boolean tickMotion ( )
	{

		// what direction am i going? */
		if ( tickDirection == DProblemState.FORWARD ) {
			tickSet(tick+1);
						
			// Are we at the end of time?
			// if ( time > problem.getTimeObject().getStopTime()) {
			// timeDirection = DProblemState.STOP;
			// }
		
			return true;
			
		} else if ( tickDirection == DProblemState.BACKWARD ) {
			tickSet(tick-1);
			return true;
		} else { 
			// no motion.
			return false;
		}
		
	}

	/** 
	 * I wrapped this up becuse I need to adjust it in the variables
	 * as well.
	 * @param newTick
	 */
	private synchronized void tickSet( int newTick )
	{
		// limit at the stop tick
		if ( newTick > stopTick ) { 
			tickDirection = DProblemState.STOP;
			tick = stopTick;
		
		} else if ( newTick <= 0 ) {
			tickDirection = DProblemState.STOP;
			tick = 0;
		
		} else {
			tick = newTick;
		}
		
		vars.setCurrentTick(tick);
	}
	


	/*--------------------------------------------------------------------*/
	/* Time Controls */

	public int getCurrentTick() { return tick; }
	
	public void tickForward() { tickDirection = DProblemState.FORWARD; }
	public void tickBackward() { tickDirection = DProblemState.BACKWARD; }
	public void tickStop() { tickDirection = DProblemState.STOP; }
    public int getTickDirection() {return tickDirection;}

    /**
     * Stop the tick motion, and move one forward.
     *
     */
    
	public void tickStepForward()
	{ 
		tickStop();
		tickSet(tick+1);
	}

	/**
	 * Stop the tick motion and move one backwards.
	 *
	 */
	public void tickStepBackward()
	{
		tickStop();
		tickSet(tick-1);
		
	}

	

	/**
	 * This is now very simple, just return a pointer.
	 * 
	 */

	public synchronized MDataHistory vars()
	{
		return vars;
	}
	
	/**
	 * Pass through utility function.
	 * @return
	 */
	
	public synchronized double getCurrentTime()
	{
		try { 
			return vars.getCurrentTime();
		} catch ( UnknownTickException x ) { 
			IWPLog.x(this, "getCurrentTime: Unknown Tick. Should never happen. Please Report bug.", x);
			return 0;
		} catch ( UnknownVariableException x ) { 
			IWPLog.x(this, "getCurrentTime: Unknown Variable. Should never happen. Please Report bug.", x);
			return 0;
		}
	}


    /*--------------------------------------------------------------------*/
	/**
	 * This used to live in DPRoblem, but now is separated to try and 
	 * split the state out of the problem.
	 * 
	 * Returns true if anything changed and you need to send the 
	 * signal along to the Objects + the UI
	 */
    public boolean handleTickEvent()
        throws Exception
    {
    	return tickMotion();
    }

    
    
    /**
     * Return a copy of this file that preserves the tick, but 
     * keeps pointers to the problem, variables, etc.
     * @return
     */
    
    public synchronized DProblemState copyOfButPreservePointers()
    {
    	return new DProblemState ( problem, vars, tickDirection, tick, stopTick );
    }

	public synchronized boolean isTickInProgress() {
		return tickInProgress;
	}

	public synchronized void setTickInProgress(boolean tickInProgress) {
		this.tickInProgress = tickInProgress;
	}

}





