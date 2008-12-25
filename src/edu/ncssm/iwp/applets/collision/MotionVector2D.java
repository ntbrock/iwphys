/**
 * This class maintains the calculations nessecary for doing 2D motion
 * involving velocity, mass, and inertia.
 *
 * @author brockman
 */

package edu.ncssm.iwp.applets.collision;

import edu.ncssm.iwp.util.IWPLog;

public class MotionVector2D
{
    public MotionVector x;
    public MotionVector y;

    public MotionVector2D ( MotionVector x,
                            MotionVector y )
    {
        this.x = x;
        this.y = y;
    }


    public MotionPoint2D getIntialDisplacement ( )
    {

        return new MotionPoint2D ( x.getInitialDisplacement(),
                                   y.getInitialDisplacement() );
    }


    /**
     * Calculate the destination point based on the velocities of the two
     * vectors, including all other inertia / mass calculates.
     */
    public MotionPoint2D calculate ( int step, MotionVector2D velocityDelta )
    {
        // TODO: actually define this
        float xDisp = 0;
        float yDisp = 0;

        xDisp = x.calculateWithVelocityAdjustment ( step,
                                                    velocityDelta.x.getVelocity( step ) );

        yDisp = y.calculateWithVelocityAdjustment ( step,
                                                    velocityDelta.y.getVelocity( step ) );

        return new MotionPoint2D ( xDisp, yDisp );
    }


    public MotionPoint2D calculate ( int step )
    {
        float xDisp = 0;
        float yDisp = 0;

        xDisp = x.calculate ( step );
        yDisp = y.calculate ( step );

        IWPLog.debug(this, "[MotionVector2D.calculate] xDisp = " + xDisp + "   yDisp = " + yDisp );

        return new MotionPoint2D ( xDisp, yDisp );
    }
}
