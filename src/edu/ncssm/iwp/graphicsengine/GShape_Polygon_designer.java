// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"
package edu.ncssm.iwp.graphicsengine;

import edu.ncssm.iwp.math.*;
//import edu.ncssm.iwp.math.designers.MCalculator_Parametric_simpleDesigner;
import edu.ncssm.iwp.util.*;
import edu.ncssm.iwp.exceptions.*;
import edu.ncssm.iwp.ui.widgets.*;

import javax.swing.*;
import java.util.*;
import java.awt.*;

public class GShape_Polygon_designer extends JPanel implements GShape_designer_interface
{
	public static final int INPUT_LENGTH = 22;
	public static final boolean INPUT_MULTILINE = false;
	
	GShape_designer parentContainer;
	
	JPanel panelForPoints;

    Vector xInputs;
    Vector yInputs;
    GShape shape;
    Vector children;
    
    public static final int INIT_PTS=3;

    public GShape_Polygon_designer(GShape_designer parent, GShape in) {
		this.parentContainer = parent;
		shape=in;
		xInputs=new Vector(3,1);
		yInputs=new Vector(3,1);
		children=new Vector(INIT_PTS,1);
	if (in instanceof GShape_Polygon) {
	    xInputs=((GShape_Polygon)in).getXPointCalcs();
	    yInputs=((GShape_Polygon)in).getYPointCalcs();
	} else {
	    xInputs=new Vector(INIT_PTS,1);
	    yInputs=new Vector(INIT_PTS,1);
	    //fill with garbage
	    for(int i=0;i<INIT_PTS;i++) {
		xInputs.add(i,new MCalculator_Parametric("0"));
		yInputs.add(i,new MCalculator_Parametric("0"));	    
	    }
	    //casses covered: handed a GShape_Polygon or anything else.
	}
	//in addition to the normal building GShape_designer,
	//controls need to be displayed for the points.
	init();
	buildGui();
    }
    protected void init() {
	children=new Vector(xInputs.size(),1);
	for(int i=0;i<xInputs.size();i++) {
		// 2008-Dec-25, brockman added wider polygon input fields
	    children.add(i,new GPolygon_Point( ((MCalculator_Parametric)xInputs.elementAt(i)).getSimpleParametricDesigner("x path", INPUT_LENGTH, INPUT_MULTILINE),
					       ((MCalculator_Parametric)yInputs.elementAt(i)).getSimpleParametricDesigner("y path", INPUT_LENGTH, INPUT_MULTILINE ),
					       this,i));
	    add((JPanel)children.elementAt(i));
	}
    }



	/**
	 * called initial time
	 */
	
	public void buildGui() 
	{
		panelForPoints = this;
		rebuildGui();

	}
	
	/**
	 * called subsequent times
	 */
    public void rebuildGui() {

    	panelForPoints.removeAll();
    	panelForPoints.setLayout(new GridLayout(children.size(),1));

    	//setSize(1000,5000);
     	    	
    	for(int i=0;i<children.size();i++) {
    		panelForPoints.add((JPanel)children.elementAt(i));
    	}

    	revalidate();
		parentContainer.rebuildGui();

    	IWPLog.debug(this,"[GShape_Polygon_designer] children size: " +children.size());
    }
    
    public void setShape(GShape in) {shape=in;}
    public GShape get(GShape in)
    	throws InvalidEquationException
    {
    	//IWPLog.info(this,"[GShape_Polygon_designer NOT IMPLEMENTED YET");
    	for(int i=0;i<children.size();i++) {
    		//((GShape_Polygon)in).setXCalc(((GPolygon_Point)children.elementAt(i)).getXCalc(),i);
    		GPolygon_Point tOne=(GPolygon_Point)children.elementAt(i);
    		MCalculator tTwo=tOne.getXCalc();
    		((GShape_Polygon)in).setXPointCalc(tTwo,i);
    		((GShape_Polygon)in).setYPointCalc(((GPolygon_Point)children.elementAt(i)).getYCalc(),i);
    	}
    	return in;
    }
    
    public void removePoint(int index) {
	children.trimToSize();
	if(children.size()<4) {
	    JOptionPane.showMessageDialog(null,"There would be too few points to form a polygon",
					  "Cannot remove Point",JOptionPane.INFORMATION_MESSAGE);
	} else {
	    children.remove(index);
	    rebuildIndicies();
	    /*
	    for(int i=index;i<children.size();i++) {
		((GPolygon_Point)children.elementAt(i)).decrimentIndex();
	    }
	    */
	    rebuildGui();
	}
					  
    }
    public void addPointBefore(int index) {
    	
    	//((MCalculator_Parametric)xInputs.elementAt(i)).getSimpleParametricDesigner("x path", 5)
    	
    	Vector dummy = new Vector(2,1);
    	dummy.add(0,new MCalculator_Parametric("0"));
    	dummy.add(0,new MCalculator_Parametric("0"));
 
    	
    	children.add ( index,
				   new GPolygon_Point( ((MCalculator_Parametric)dummy.elementAt(0)).getSimpleParametricDesigner("x path", INPUT_LENGTH, INPUT_MULTILINE),
						   			  	((MCalculator_Parametric)dummy.elementAt(1)).getSimpleParametricDesigner("y path", INPUT_LENGTH, INPUT_MULTILINE),
					   			   this,index));
    	
    	/*
    	children.add ( index,
    				   new GPolygon_Point( new MCalculator_Parametric_simpleDesigner("x path"),
    						   			   new MCalculator_Parametric_simpleDesigner("y path"),
    					   			   this,index));*/	
    	
	/*
	for(int i=index;i<children.size();i++) {
	    ((GPolygon_Point)children.elementAt(i)).incrimentIndex();
	}
	*/
	rebuildIndicies();
	rebuildGui();
    }
    public void addPointAfter(int index) {
	addPointBefore(index+1);
    }
    
    protected void rebuildIndicies() {
	int lSize=children.size();
	for(int i=0;i<lSize;i++) {
	    ((GPolygon_Point)children.elementAt(i)).setIndex(i);
	}
    }
}
