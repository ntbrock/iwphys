package edu.ncssm.iwp.ui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import edu.ncssm.iwp.plugin.IWPAnimated;
import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.objects.DObject_Time;
import edu.ncssm.iwp.util.IWPLog;

public abstract class GWindow_Animator_setTimePropertiesAbstract extends JPanel
    implements ActionListener, IWPAnimated
{

    GWindow_Animator parent = null;

    JTextField inputStart;
    JTextField inputStop;
    JTextField inputStep;
    JTextField inputFps;
    JButton ok;
    JButton cancel;
    JButton apply;
    DObject_Time object;

    JPanel holderPanel;

    public GWindow_Animator_setTimePropertiesAbstract ( GWindow_Animator parent )
    {
        this.parent = parent;

        holderPanel = new JPanel();
        holderPanel.setLayout(new GridLayout(3,2,5,5));
        
        inputStart = new JTextField ( "", 8 );
        inputStop  = new JTextField ( "", 8 );
        inputStep  = new JTextField ( "", 8 );
        inputFps   = new JTextField ( "", 8 );
        
        finishInit();
    }

    public void tick ( DProblemState problemState ) {}

    public void zero ( DProblem problem, DProblemState problemState )
    {
        object = problem.getTimeObject();
        inputStart.setText(object.getStartTime()+"");
        inputStop.setText(object.getStopTime()+"");
        inputStep.setText(object.getChange()+"");
        inputFps.setText(object.getFps()+"");
    }
    

    protected abstract void finishInit();

    public void actionPerformed(ActionEvent e) {
    	
    	IWPLog.debug(this,"[DObject_GraphWindo_Modifier]"+e.getActionCommand());
    
    	
    	if(e.getActionCommand().equals("Cancel")) {
    		setVisible(false);
    		IWPLog.info("[DObject_GraphWindo_Modifier] cancelled.");
    
    	} else if(e.getActionCommand().equals("OK") || e.getActionCommand().equals("Apply")) {
        
    		try{
    			IWPLog.debug(this,"[DObject_GraphWindo_Modifier] Okay'ing.");

    			object.setStartTime   ( Double.parseDouble(inputStart.getText()) );
    			object.setStopTime    ( Double.parseDouble(inputStop.getText()) );
    			object.setChange      ( Double.parseDouble(inputStep.getText()) );
    			object.setFps         ( Double.parseDouble(inputFps.getText()) );
    		} catch (Exception a) {
    			JOptionPane.showMessageDialog(null,"Setting time paramaters failed");
    		}
    
    	}
    }	
}
    