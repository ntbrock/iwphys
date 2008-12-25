// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.ui.widgets;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import edu.ncssm.iwp.math.*;
import edu.ncssm.iwp.math.designers.MCalculator_Parametric_simpleDesigner;
import edu.ncssm.iwp.graphicsengine.*;
import edu.ncssm.iwp.exceptions.*;


import edu.ncssm.iwp.util.*;

public class GPolygon_Point extends JPanel implements ActionListener {
    
    //protected MCalculator xPos;
    //protected MCalculator yPos;
    protected MCalculator_Parametric_simpleDesigner xDes;
    protected MCalculator_Parametric_simpleDesigner yDes;
    protected GShape_Polygon_designer parent;

    protected JButton insertAbove,delete,insertBelow;

    protected int index=0;

    public GPolygon_Point(GShape_Polygon_designer inParent, int inIndex) {
	this( new MCalculator_Parametric_simpleDesigner("x path:"),
			new MCalculator_Parametric_simpleDesigner("y path:"),
	      inParent,inIndex);
    }
    public GPolygon_Point(MCalculator_Parametric_simpleDesigner inx,
    					  MCalculator_Parametric_simpleDesigner iny,
			  GShape_Polygon_designer inParent, int inIndex) {
	xDes=inx;
	yDes=iny;
	parent=inParent;
	index=inIndex;
	insertAbove= new JButton("insert above");
	insertAbove.addActionListener(this);
	delete=new JButton("delete");
	delete.addActionListener(this);
	insertBelow=new JButton("insert below");
	insertBelow.addActionListener(this);
	buildGUI();
    }
    public void buildGUI() {

	TitledBorder titleBorder = BorderFactory.createTitledBorder("Point "+index);
	titleBorder.setTitleJustification(TitledBorder.LEFT);
	setBorder(titleBorder);

	JPanel east = new JPanel();
	east.setLayout(new GridLayout(3,1));
	east.add(insertAbove);
	east.add(delete);
	east.add(insertBelow);
	
	JPanel center = new JPanel();
	center.setLayout(new BorderLayout());
	
	
	center.add(BorderLayout.NORTH,xDes);
	center.add(BorderLayout.CENTER,yDes);
	
	setLayout(new BorderLayout());
	add(BorderLayout.WEST,east);
	add(BorderLayout.CENTER,center);
	
	center.repaint();
	
	//center.setPreferredSize(new Dimension(375,140));
    }
    public void resetTitleBorder() {
	TitledBorder titleBorder = BorderFactory.createTitledBorder("Point "+index);
	titleBorder.setTitleJustification(TitledBorder.LEFT);
	setBorder(titleBorder);
    }

    public MCalculator getXCalc()
    	throws InvalidEquationException
    {
    	return xDes.get();
    }
    public MCalculator getYCalc()
	throws InvalidEquationException
    {
    	return yDes.get();
    }
    public void incrimentIndex() {index++;resetTitleBorder();}
    public void decrimentIndex() {index--;resetTitleBorder();}
    public void setIndex(int in) {index=in;resetTitleBorder();}
    public void actionPerformed(ActionEvent e) {
	//IWPLog.debug(this,""+e.getActionCommand());
	String tmp=e.getActionCommand();
	if(tmp.equals("insert above")) {
	    parent.addPointBefore(index);
	} else if (tmp.equals("insert below")) {
	    parent.addPointAfter(index); 
	} else if(tmp.equals("delete")) {
	    parent.removePoint(index);
	} else {
	    IWPLog.info(this,"[GPolygon_Point] actionPerformed fell through...");
	}
    }
}
