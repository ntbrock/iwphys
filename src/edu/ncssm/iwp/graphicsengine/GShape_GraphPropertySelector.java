// 2008-Dec-25 brockman, touching this file so that the CVS HEAD tag will apply

package edu.ncssm.iwp.graphicsengine;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.JRadioButton;


public class GShape_GraphPropertySelector extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
    public JRadioButton xPos;
    protected JRadioButton xVel;
    protected JRadioButton xAccel;
    protected JRadioButton yPos;
    protected JRadioButton yVel;
    protected JRadioButton yAccel;

    ArrayList propertyChangeListeners = new ArrayList();



    public GShape_GraphPropertySelector() {
    xPos=new JRadioButton("x Pos");
    xVel=new JRadioButton("x Vel");
    xAccel=new JRadioButton("x Accel");
    yPos=new JRadioButton("y Pos");
    yVel=new JRadioButton("y Vel");
    yAccel=new JRadioButton("y Accel");

    this.setLayout(new GridLayout(3,2));
    this.add(xPos);
    this.add(yPos);
    this.add(xVel);
    this.add(yVel);
    this.add(xAccel);
    this.add(yAccel);

    xPos.addActionListener(this);
    yPos.addActionListener(this);
    xVel.addActionListener(this);
    yVel.addActionListener(this);
    xAccel.addActionListener(this);
    yAccel.addActionListener(this);

    }

    public boolean xPosSelected() {return xPos.isSelected();}
    public boolean xVelSelected() {return xVel.isSelected();}
    public boolean xAccelSelected() {return xAccel.isSelected();}
    public boolean yPosSelected() {return yPos.isSelected();}
    public boolean yVelSelected() {return yVel.isSelected();}
    public boolean yAccelSelected() {return yAccel.isSelected();}

    public void setXPosSelected(boolean in) {xPos.setSelected(in);}
    public void setXVelSelected(boolean in) {xVel.setSelected(in);}
    public void setXAccelSelected(boolean in) {xAccel.setSelected(in);}
    public void setYPosSelected(boolean in) {yPos.setSelected(in);}
    public void setYVelSelected(boolean in) {yVel.setSelected(in);}
    public void setYAccelSelected(boolean in) {yAccel.setSelected(in);}

    public GShape_GraphPropertySelector copy() {
    GShape_GraphPropertySelector goingBack=new GShape_GraphPropertySelector();
    goingBack.setXPosSelected(xPosSelected());
    goingBack.setXVelSelected(xVelSelected());
    goingBack.setXAccelSelected(xAccelSelected());
    goingBack.setYPosSelected(yPosSelected());
    goingBack.setYVelSelected(yVelSelected());
    goingBack.setYAccelSelected(yAccelSelected());
    return goingBack;
    }

    public void actionPerformed(ActionEvent e) {

        GShape_GraphPropertySelectorChangeEvent evt = new GShape_GraphPropertySelectorChangeEvent(this);

        for ( Iterator i = this.propertyChangeListeners.iterator(); i.hasNext(); ) {
            GShape_GraphPropertySelectorChangeListener listener =
                (GShape_GraphPropertySelectorChangeListener) i.next();

            listener.graphPropertyChanged(evt);
        }

    }

    public void addPropertyChangeListener ( GShape_GraphPropertySelectorChangeListener listener )
    {
        this.propertyChangeListeners.add(listener);
    }
    public void clearPropertyChangeListeners()
    {
        this.propertyChangeListeners.clear();
    }
}
