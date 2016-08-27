package edu.ncssm.iwp.applets.collision;

import edu.ncssm.iwplib.*;

public class MotionPoint2D implements GridPoint
{
    public double x;
    public double y;

    public MotionPoint2D ( double x, double y )
    {
        this.x = x;
        this.y = y;
    }

    public static MotionPoint2D addPoints ( MotionPoint2D a,
                                            MotionPoint2D b )
    {
        return new MotionPoint2D ( a.x + b.x,
                                   a.y + b.y );
    }

    public double getX ( ) { return x; }
    public double getY ( ) { return y; }

    public String toString ( )
    {
        return "MotionPoint2D(x="+x + ", y=" + y + ")";
    }

}

