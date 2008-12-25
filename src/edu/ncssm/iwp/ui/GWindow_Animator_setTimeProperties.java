package edu.ncssm.iwp.ui;

import javax.swing.*;

import edu.ncssm.iwp.plugin.IWPAnimated;

import java.awt.*;

public class GWindow_Animator_setTimeProperties
    extends GWindow_Animator_setTimePropertiesAbstract
    implements IWPAnimated
{

    public GWindow_Animator_setTimeProperties(GWindow_Animator parent)
    {
    	super(parent);
    }
							
     
    protected void finishInit() 
    {
    	
    	holderPanel.add ( new JLabel ( " Start Time: " ) );
    	holderPanel.add ( inputStart );

    	holderPanel.add ( new JLabel ( " Stop Time: " ) );
    	holderPanel.add ( inputStop );

    	holderPanel.add ( new JLabel ( " Step Time: " ) );
    	holderPanel.add ( inputStep );		
	
    	//input2.add ( new JLabel ( "Frames Per Second: " ) );
    	//input2.add ( inputFps );
		
    	JPanel buttonPanel = new JPanel();
    	JButton apply = new JButton("Apply");
    	buttonPanel.add(apply);
	
    	apply.addActionListener(this);
    	
    	setLayout(new BorderLayout());
    	add(BorderLayout.NORTH,holderPanel);
    	add(BorderLayout.CENTER,buttonPanel);
	
    	
    }
}
