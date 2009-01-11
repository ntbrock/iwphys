package edu.ncssm.iwp.graphicsengine;

import javax.swing.*;
import java.awt.*;

public class GShape_VectorSelector extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	protected JRadioButton xVel;
    protected JRadioButton xAccel;
    protected JRadioButton yVel;
    protected JRadioButton yAccel;
    protected JRadioButton Vel;
    protected JRadioButton Accel;

    public GShape_VectorSelector() {
	xVel=new JRadioButton("x Vel");
	xAccel=new JRadioButton("x Accel");
	yVel=new JRadioButton("y Vel");
	yAccel=new JRadioButton("y Accel");
	Vel=new JRadioButton("Vel");
	Accel=new JRadioButton("Accel");

	this.setLayout(new GridLayout(3,2));
	this.add(xVel);
	this.add(xAccel);
	this.add(yVel);
	this.add(yAccel);
	this.add(Vel);
	this.add(Accel);
	
    }
    
    public boolean xVelSelected() {return xVel.isSelected();}
    public boolean xAccelSelected() {return xAccel.isSelected();}
    public boolean yVelSelected() {return yVel.isSelected();}
    public boolean yAccelSelected() {return yAccel.isSelected();}
    public boolean VelSelected() {return Vel.isSelected();}
    public boolean AccelSelected() {return Accel.isSelected();}

	public void setxVelSelected(boolean in) {xVel.setSelected(in);}
    public void setxAccelSelected(boolean in) {xAccel.setSelected(in);}
    public void setyVelSelected(boolean in) {yVel.setSelected(in);}
    public void setyAccelSelected(boolean in) {yAccel.setSelected(in);}
    public void setVelSelected(boolean in) {Vel.setSelected(in);}
    public void setAccelSelected(boolean in) {Accel.setSelected(in);}

    public GShape_VectorSelector copy() {
	GShape_VectorSelector goingBack=new GShape_VectorSelector();
	goingBack.setxVelSelected(xVelSelected());
	goingBack.setxAccelSelected(xAccelSelected());
	goingBack.setyVelSelected(yVelSelected());
	goingBack.setyAccelSelected(yAccelSelected());
	goingBack.setVelSelected(VelSelected());
	goingBack.setAccelSelected(AccelSelected());
	return goingBack;
    }
}
