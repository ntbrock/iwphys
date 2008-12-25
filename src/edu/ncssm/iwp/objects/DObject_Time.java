// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
//
// For full copyright information, see main source file,
// "iwp.java"


package edu.ncssm.iwp.objects;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import edu.ncssm.iwp.exceptions.InvalidEquationException;
import edu.ncssm.iwp.math.MVariables;
import edu.ncssm.iwp.plugin.IWPAnimated;
import edu.ncssm.iwp.plugin.IWPCalculated;
import edu.ncssm.iwp.plugin.IWPDesigned;
import edu.ncssm.iwp.plugin.IWPObject;
import edu.ncssm.iwp.plugin.IWPObjectXmlCreator;
import edu.ncssm.iwp.plugin.IWPObjectXmlHandler;
import edu.ncssm.iwp.plugin.IWPXmlable;
import edu.ncssm.iwp.problemdb.DProblem;
import edu.ncssm.iwp.problemdb.DProblemState;
import edu.ncssm.iwp.ui.GAccessor_designer;


/**
 * Time direction flow is no longer kept in here. It has been properly
 * split out in the DProblemState. brockman 2006-Apr-25
 *
 * This is the Time T object used by the other parts of the problem.
 * Calculations on this guy actually sets the T value into the MVariables space.
 *
 * Very important class.
 *
 * 2007-may-22 Brockman: Added a MAX_FPS = 20 that is enforced my setFps/getFps
 *
 * 2007-Jun-06 brockman converted all doubles to BigDecimals to eliminate the off by .00000000000000001 issue
 * see: http://www.velocityreviews.com/forums/t125033-double-add-problem.html
 *
 * @author brockman
 *
 */

public class DObject_Time implements IWPXmlable, IWPObject, IWPCalculated, IWPAnimated, IWPDesigned
{


    public String getIconFilename()
    {
        return "icon_DObject_Time.gif";
    }

    
    public static final String OBJECT_NAME = "Time";

    public static final BigDecimal MAX_FPS = new BigDecimal(25);
    
    
    //VARIABLES
    private BigDecimal curTime = new BigDecimal("0.0");
    private BigDecimal deltaTime = new BigDecimal(".1");

    private BigDecimal startTime = new BigDecimal("0");
    private BigDecimal stopTime = new BigDecimal("10");

    private BigDecimal fps = new BigDecimal(10);

    private boolean usePreciseCalculations = false;

    
    //CONSTRUCTORS

    public DObject_Time()
    {
        curTime = startTime;
    }

    public DObject_Time( double iTime )
    {
        setTime(iTime);
    }

    public String getName() { return OBJECT_NAME; }

    // Don't allow changing the time name
    public void setName(String newName) { }


    public void setFps(double iFps)
    {
        fps = new BigDecimal(iFps);
        // 2007-May-22 brockman - impose a maximum fps.
        if ( fps.compareTo(MAX_FPS) >= 0 ) { fps = MAX_FPS; }
    }
    public double getFps()
    {
        // 2007-May-22 brockman - impose a maximum fps.
        if ( fps.compareTo(MAX_FPS) >= 0 ) { fps = MAX_FPS; }        
        return fps.doubleValue();
    }
    
    public double getTime() { return curTime.doubleValue(); }
    public void setTime(double iTime)
    {
    	curTime = new BigDecimal(""+iTime);
    }

    public double getChange() { return deltaTime.doubleValue(); }
    public void setChange(double iChange)
    {
    	deltaTime = new BigDecimal(""+iChange);
    }

    public double getStopTime() { return stopTime.doubleValue(); }
    public void setStopTime(double iStop)
    { 
    	stopTime = new BigDecimal(""+iStop);
    }

    public double getStartTime() { return startTime.doubleValue(); }
    public void setStartTime(double iStart)
    {
    	startTime = new BigDecimal(""+iStart);
    }

    //-------------------------------------------------------------
    /**
     * The total number of ticks in the time.
     * @return
     */

    public int calculateStopTick()
    {
    	// BigDecimal arithmatic mode.
    	//System.err.println("CalculateStopTick: startTime: " + startTime + "   deltaTime: " + deltaTime );
        return (( stopTime.subtract(startTime) ).divide(deltaTime, BigDecimal.ROUND_UP) ).intValue();

    }

    //-------------------------------------------------------------

    public void zero ( DProblem problem, DProblemState state )
    {
        tick(state);
        state.vars().setAtCurrentTick( MVariables.MAX_STEPS, calculateStopTick()+ 1);
    }



    /**
     * THIS IS IMPORTANT, the current time is calculated here.
     */

    public void tick ( DProblemState state )
    {        
        // 2007-Jun-06 brockman
        // we never want our time to have the classic java off by .00000000000000001 error. I see this
        // occassionally.

        BigDecimal currentTick = new BigDecimal(state.vars().getCurrentTick());        
        curTime = startTime.add( currentTick.multiply( this.deltaTime ) );

        double curTimeDouble = curTime.doubleValue();
        double deltaTimeDouble = deltaTime.doubleValue();
        
        //System.err.println("CurTimeDouble: " + curTimeDouble +    " curTime: " + curTime + "  startTime: " + startTime + "  deltaTime: " + deltaTime);
        
        // My state should be fully accessible
        state.vars().setAtCurrentTick( getName() + "."+MVariables.CURTIME, curTimeDouble  );
        state.vars().setAtCurrentTick( getName() + "."+MVariables.DELTATIME, deltaTimeDouble );
        state.vars().setAtCurrentTick( getName() + "."+MVariables.STARTTIME, startTime.doubleValue() );
        state.vars().setAtCurrentTick( getName() + "."+MVariables.ENDTIME,   stopTime.doubleValue() );
        
        // set these at the top for convenience and v2 compliance. Node that I am dropping support for delta_x in V3.
        state.vars().setAtCurrentTick( MVariables.T,       curTimeDouble );
        state.vars().setAtCurrentTick( MVariables.DELTA_T, deltaTimeDouble );
    }



    /**
     * The list of all the time variables available to you in the problem.
     */

    public Collection getProvidedSymbols() throws InvalidEquationException
    {
        ArrayList out = new ArrayList(10);

        out.add(getName() + "." + MVariables.CURTIME );
        out.add(getName() + "." + MVariables.STARTTIME );
        out.add(getName() + "." + MVariables.ENDTIME );
        out.add(getName() + "." + MVariables.DELTATIME );
        out.add( MVariables.T );
        out.add( MVariables.DELTA_T );

        return out;
    }

    /**
     * Calculate time first. That's what a return of null means here.
     */

    public Collection getRequiredSymbols() { return null; }


    /*----------------------------------------------------------------------*/
    /* DESIGNERS */
    /*----------------------------------------------------------------------*/

    public GAccessor_designer getDesigner ( ) {
        return new DObject_Time_designer ( this );
    }

    /*----------------------------------------------------------------------*/


    public IWPObjectXmlCreator newXmlObjectCreator()
    {
        return new DObject_TimeXMLCreator(this);
    }

    public IWPObjectXmlHandler newXmlObjectHandler()
    {
        return new DObject_TimeXMLHandler();
    }
    
    

	public boolean getUsePreciseCalculations() {
		return usePreciseCalculations;
	}

	public void setUsePreciseCalculations(boolean usePreciseCalculations) {
		this.usePreciseCalculations = usePreciseCalculations;
	}
    
}

