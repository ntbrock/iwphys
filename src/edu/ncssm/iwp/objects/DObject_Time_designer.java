// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
//
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.objects;

import edu.ncssm.iwp.plugin.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DObject_Time_designer extends DObject_designer
{
	private static final long serialVersionUID = 1L;
    static int TEXT_WIDTH = 6;

    DObject_Time object;


    public DObject_Time_designer (DObject_Time iobject )
    {
        object = iobject;
        buildGui();
    }

    JTextField inputStart;
    JTextField inputStop;
    JTextField inputStep;
    JTextField inputFps;
    JCheckBox usePreciseCalculations;
    
    public void buildGui()
    {

        JPanel input = new JPanel();

        int horizGap = 30;
        int vertGap = 10;

		boolean displayUsePrecise = false;

        input.setLayout ( new GridLayout( displayUsePrecise ? 5 : 4, 2, horizGap, vertGap) );

        inputStart = new JTextField ( "" + object.getStartTime(), TEXT_WIDTH );
        inputStop  = new JTextField ( "" + object.getStopTime(), TEXT_WIDTH );
        inputStep  = new JTextField ( "" + object.getChange(), TEXT_WIDTH );
        inputFps   = new JTextField ( "" + object.getFps(), TEXT_WIDTH );
        usePreciseCalculations = new JCheckBox ( "", object.getUsePreciseCalculations() );
        
        input.add ( new JLabel ( "Start Time: " ) );
        input.add ( inputStart );

        input.add ( new JLabel ( "Stop Time: " ) );
        input.add ( inputStop );

        input.add ( new JLabel ( "Step Time: " ) );
        input.add ( inputStep );

        input.add ( new JLabel ( "Frames Per Second: " ) );
        input.add ( inputFps );

		if ( displayUsePrecise ) { 	
        	JLabel altText = new JLabel ( "Use Alternate Rounding Calculator: " );
        	altText.setToolTipText("Use the alternate rounding calculator when you see numbers close to zero off by a very very small amount. Turning this on, however, limits the use of numbers < 1E-9");
        	input.add ( altText );
        	input.add ( usePreciseCalculations );
    	}
        
		input.setBorder(new EmptyBorder(10,10,10,10));

        buildEasyGui ( "Time", Color.WHITE, Color.BLACK, input );
    }


    //ACCESSORS

    public String getName () { return object.getName ( ); }

    //object (g/s)
    public DObject_Time getObject() { return object; }
    public void setObject(DObject_Time iObject) { object = iObject; }


    private double getDoubleValue ( JTextField textField )
    {
        try {
            return ( (Double.valueOf ( textField.getText() )).doubleValue() );
        } catch ( NumberFormatException e ) {
            return 0.0;
        }
    }

    public IWPObject buildObjectFromDesigner() {
        DObject_Time goingBack = new DObject_Time();
        goingBack.setStartTime(getDoubleValue(inputStart));
        goingBack.setStopTime(getDoubleValue(inputStop));
        goingBack.setChange(getDoubleValue(inputStep));
        goingBack.setFps(getDoubleValue(inputFps));
        goingBack.setUsePreciseCalculations( usePreciseCalculations.isSelected() );
        return goingBack;
    }
}
