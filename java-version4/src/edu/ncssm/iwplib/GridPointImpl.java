package edu.ncssm.iwplib;

public class GridPointImpl implements GridPoint
{
    double x;
    double y;

    public GridPointImpl ( double x, double y )
    {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }

}
