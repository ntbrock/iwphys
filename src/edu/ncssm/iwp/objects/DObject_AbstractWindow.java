// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
//
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.objects;

import edu.ncssm.iwp.problemdb.*;
import java.awt.*;
import java.text.DecimalFormat;

import edu.ncssm.iwp.util.*;
import edu.ncssm.iwp.plugin.*;

/**
 * I believe this base class was created to make a
 * GraphWindow (new) and leave the regular DObject_WIndow (old)
 *
 * I like OO design.
 *
 * @author brockman
 *
 */
public abstract class DObject_AbstractWindow implements IWPObject
{
    //VARIABLES

    int width;
    int height;

    double minX =0;
    double maxX =10;
    double minY =-10;
    double maxY =10;

    int gridType = 1;

    double gridX = 2;
    double gridY = 2;

    String UnitX = "m";
    String UnitY = "m";

    boolean showGridNumbers = true;



    //Constructors

    // just for object completeness - inherits an abstract class.
    public void zero ( DProblem problem, DProblemState state ) { showGridNumbers = problem.getWindowObject().isDrawGridNumbers();}
    public void tick ( DProblemState state ) { }


    //ACCESSORS

    public void setMinX(double iX) { minX = iX; }
    public double getMinX() { return minX; }

    public void setMinY(double iY) { minY = iY; }
    public double getMinY() { return minY; }
    public double getMinYReverse() { return -maxY; }

    public void setMaxX(double iX) {
        maxX = iX;
        IWPLog.debug ( this, "setMaxX: "+iX+":"+maxX);

    }
    public double getMaxX() {
        IWPLog.debug ( this, "(getMaxX) maxX: "+maxX);
        return maxX;
    }

    public void setMaxY(double iY) {maxY = iY; }
    public double getMaxY() { return maxY; }
    public double getMaxYReverse() { return -minY; }

    public void setGridX(double iX) { gridX = iX; }
    public double getGridX() { return gridX; }

    public void setGridY(double iY) { gridY = iY; }
    public double getGridY() { return gridY; }

    public void setUnitX(String uX) { UnitX = uX; }
    public String getUnitX() { return UnitX; }

    public void setUnitY(String uY) { UnitY = uY; }
    public String getUnitY() { return UnitY; }

    public void setGridType(int iType) { gridType = iType; }
    public int getGridType() { return gridType; }



    public void setWidth(int iW) { width = iW;
    //IWPLog.debug(this,"window: setting width="+width);
    }
    public int getWidth() { return width; }

    public void setHeight(int iH) { height = iH;
    //IWPLog.debug(this,"window: setting height="+height);
    }
    public int getHeight() { return height; }

    public int getDrawX(double iX)
    {
        int offset = 0;

        //scale it.
        double range = maxX - minX;
        double point = iX - minX;

        //percentage of width
        return (int)( (point / range) * (double)width) + offset;
    }



    public int getDrawY(double iY)
    {
        int offset = 0;

        //scale it.

        double range = getMaxYReverse() - getMinYReverse();
        double point = (iY) - getMinYReverse();
        return (int)( (point / range) * (double)height) + offset;
    }

    public void drawMe(Graphics g)
    {
        g.setColor(new Color(255,0,0));

        //line color?


        //10:46 march 18. Switching all maxy, miny. That
        //might correct problems.


        //Add in a tiny section to write the legend to the screen at the top
        Font curFont = g.getFont();
        int fontSize = curFont.getSize(); // for formatting
        Graphics2D g2d = (Graphics2D)g;

        Color darkGrey = new Color(160,160,160);
        Color lightGrey = new Color(230,230,230);



        // 2008-Sep-15 brockman. In here changing teh reverse order of the grid numbers for IWP 4.0

        //Draw Lines First
        g.setColor(lightGrey);
        for (double x = 0; x <= maxX; x +=gridX) {
            g.drawLine( getDrawX(x), getDrawY(getMinYReverse()-gridY), getDrawX(x), getDrawY(getMaxYReverse()+gridY) );
        }
        for (double x = 0; x >= minX; x -=gridX) {
            g.drawLine( getDrawX(x), getDrawY(getMinYReverse()-gridY), getDrawX(x), getDrawY(getMaxYReverse()+gridY) );
        }

        for (double y = 0; y <= getMaxYReverse(); y +=gridY) {
            g.drawLine( getDrawX(minX-gridX), getDrawY(y), getDrawX(maxX+gridX), getDrawY(y) );
        }
        for (double y = 0; y >= getMinYReverse(); y -=gridY) {
            g.drawLine( getDrawX(minX-gridX), getDrawY(y), getDrawX(maxX+gridX), getDrawY(y) );
        }


        //Draw Numbers

        int multx = (int)(maxX/gridX)/5; //calculate number of numbers
        int multy = (int)(getMaxYReverse()/gridY)/4;

        if(multx==0) {multx=1;}
        if(multy==0) {multy=1;}

        g.setColor(darkGrey);
        if(showGridNumbers){
            for (double x = 0; x <= maxX; x +=multx*gridX) {
                g2d.drawString(formatGridNum(x),getDrawX(x)+3,fontSize+1);
            }
            for (double x = 0; x >= minX; x -=multx*gridX) {
                g2d.drawString(formatGridNum(x),getDrawX(x)+3,fontSize+1);
            }

            // BUGFIX: Needed to flip the y numbers around.
            for (double y = 0; y <= getMaxYReverse(); y +=multy*gridY) {
                g2d.drawString(formatGridNum(-1*y),3,getDrawY(y)-1);
            }
            for (double y = 0; y >= getMinYReverse(); y -=multy*gridY) {
                g2d.drawString(formatGridNum(-1*y)+"",3,getDrawY(y)-1);
            }
        }



        g.setColor(darkGrey);

        g.drawLine( getDrawX(0), getDrawY(getMinYReverse()), getDrawX(0), getDrawY(getMaxYReverse()) );
        g.drawLine( getDrawX(minX), getDrawY(0), getDrawX(maxX), getDrawY(0) );




 /*
        //prepare what we want to write
        //NumberFormat format = NumberFormat.getNumberInstance();
        String xRange = "X:("+Double.toString(minX)+","+Double.toString(maxX)+")";
        String yRange = "Y:("+Double.toString(minY)+","+Double.toString(maxY)+")";
 */
        //Draw
        g.setColor(new Color(100,100,100));


    }

    public String formatGridNum(double iTime)
    {

        DecimalFormat format=new DecimalFormat();
        format.setMaximumFractionDigits(14);

        if(iTime==0) {
            return "0.0";
        }

        if(iTime>-1 && iTime<1) {
            format.applyPattern("0.0#E0");
        }
        else if(iTime>-99 && iTime<99) {
            format.applyPattern("#0.0#");
        }
        else {format.applyPattern("0.0#E0");}


        return format.format(iTime)+"";

    }




}

