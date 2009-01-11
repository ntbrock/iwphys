
package edu.ncssm.iwp.ui;

import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.objects.*;
import edu.ncssm.iwp.exceptions.*;
import edu.ncssm.iwp.plugin.*;
import edu.ncssm.iwp.math.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import edu.ncssm.iwp.util.*;

public class GWindow_data extends JFrame
{
	private static final long serialVersionUID = 1L;
	
    DProblem problem;
    DProblemState state;
    JTabbedPane pane;

    public GWindow_data ( DProblem prob,DProblemState dstate )
    {
    	problem=prob;
    	state=dstate;
    	init();
    	this.setTitle("IWP All Data");
    	this.setSize(new Dimension(500,400));
    	this.setVisible(true);
    }
    
    public void init() {
	
    	this.getContentPane().setLayout(new BorderLayout());
	
    	pane=new JTabbedPane();

    	double start=0.0,totalSteps=0.0,timeChange=0.0;

    	try {
    		// VARS: Should these be time.time_start?
    		totalSteps = state.vars().getAtTick( MVariables.MAX_STEPS, 0);
    		timeChange = state.vars().getAtTick( MVariables.DELTA_T, 0);
    		start = state.vars().getAtTick( MVariables.STARTTIME, 0);
    	} catch (UnknownVariableException e) {
    		IWPLog.x ( this, "Unknown Variable", e );
    	} catch (UnknownTickException e) {
    		IWPLog.x ( this, "Unknown Tick", e );
    	}
	
    	
    	Collection objects = problem.getObjectsForDrawing();
    	Iterator iterator = objects.iterator();
    	
    	while(iterator.hasNext()) {
    		
    		IWPObject dcurrent=(IWPObject) iterator.next();
    		if(!(dcurrent instanceof DObject_Solid)) {continue;}
    		DObject_Solid current=(DObject_Solid) dcurrent;    		
    		String objectName = current.getName();

    		JTextArea myData=new JTextArea();	    
    		myData.append("time,xp,xv,xa,yp,yv,ya\n");
	    
    		IWPLog.debug(this,current.getName());
    		IWPLog.debug(this,"time,xp,xv,xa,yp,yv,ya\n");
    		for(int i=0;i<totalSteps;i++) {
                double someTime=start+(i*timeChange); //same way DProblemState calculates it.
                //double someTime=-1;
                //try {someTime=vars.get("real_time");}
                //catch(Exception e) {IWPLog.info(this,"[GWindow_data] no time");}
                try{
                	String temp = someTime + "," + 
                		state.vars().getAtTick( objectName+"."+MVariables.XDISP, i) +","+
                		state.vars().getAtTick( objectName+"."+MVariables.YDISP, i) +","+
                		state.vars().getAtTick( objectName+"."+MVariables.XVEL, i) +","+
                		state.vars().getAtTick( objectName+"."+MVariables.YVEL, i) +","+
                		state.vars().getAtTick( objectName+"."+MVariables.XACCEL, i) +","+
                		state.vars().getAtTick( objectName+"."+MVariables.YACCEL, i) +"\n";
                	myData.append(temp);
                	IWPLog.debug(this,temp);
                } catch( UnknownVariableException x) {
                    IWPLog.x(this, "Unknown Variable for Object: " + objectName + ", Tick = " + i, x);
                } catch( UnknownTickException x) {
                    IWPLog.x(this, "Unknown Variable for Object: " + objectName + ", Tick = " + i, x);
                }
            }
	    
    		pane.add(current.getName(),new JScrollPane(myData,
    				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
    				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
    		
    	}
    	
    	this.getContentPane().add("Center",pane);
    }
}
