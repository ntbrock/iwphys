// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.graphicsengine;
import edu.ncssm.iwp.problemdb.*;
import java.awt.Color;


public class GColor extends DEntity
{

	int red = 0;
	int green = 0;
	int blue = 0;

	/*----------------------------------------------------------------*/

	public GColor() {}

	public GColor( int red, int green, int blue )
	{
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public GColor( Color color )
	{
		this.red = color.getRed();
		this.green = color.getGreen();
		this.blue = color.getBlue();
	}


	/*----------------------------------------------------------------*/

	public int getRed () { return red; }
	public void setRed ( int red ) { 
		this.red = red;
	}

	public int getGreen () { return green; }
	public void setGreen ( int green ) { 
		this.green = green;
	}

	public int getBlue () { return blue; }
	public void setBlue ( int blue ) { 
		this.blue = blue;
	}



	public Color getAWTColor ( ) 
	{
		return new Color ( red, green, blue );
	}


	/*----------------------------------------------------------------*/

}


