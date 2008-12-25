package edu.ncssm.iwp.applets.collision;

import edu.ncssm.iwp.util.IWPLog;
import edu.ncssm.iwplib.*;
import edu.ncssm.iwp.exceptions.*;


public class MotionVector
{
    CalculatorEuler displacementCalc;

    float initialVelocity;
    float initialDisplacement;

    public MotionVector ( float initialDisplacement,
                          float initialVelocity,
                          String accelEquation )
    	throws InvalidEquationException
    {
        this.initialDisplacement = initialDisplacement;
        this.initialVelocity = initialVelocity;

        displacementCalc = CalculatorFactory.constructEuler ( initialDisplacement,
                                                              initialVelocity,
                                                              accelEquation );
    }


    public float getVelocity ( int step )
    {
        return initialVelocity;
    }

    public float getInitialDisplacement ( )
    {
        return initialDisplacement;
    }


    public float getDisplacement ( int step )
    {
        // NOT DONE!

        return initialDisplacement;
    }


    public float calculateWithVelocityAdjustment ( int step,
                                                   float velocityAdjustment )
    {
        // this is for some sort of collision.
        adjustVelocity ( step, velocityAdjustment );

        // must also calculate after the velocity ajustment
        return calculate ( step );

    }


    public void adjustVelocity ( int step, float velocityAdjustment )
    {
        // TODO: need to add this functionality to CalculatorEuler
    }

    public float calculate ( int step )
    {
        float retValue = initialDisplacement + ((float)step) * initialVelocity; // + accel!
        IWPLog.debug(this, "[MotionVector.calculate] step: " + step + "  Di: " +initialDisplacement + " Vi: " + initialVelocity + "  ret: " + retValue  );

        return retValue;
    }
}
