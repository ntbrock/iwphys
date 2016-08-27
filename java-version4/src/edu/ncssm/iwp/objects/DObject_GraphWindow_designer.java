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
import edu.ncssm.iwp.util.*;


public class DObject_GraphWindow_designer extends DObject_designer
{
	private static final long serialVersionUID = 1L;
    static int TEXT_WIDTH = 6;

    DObject_GraphWindow object;

    public DObject_GraphWindow_designer ( DObject_GraphWindow iObject )
    {
        object = iObject;
        buildGui();
        setVisible(true);
    }


    JTextField inputMinX;
    JTextField inputMaxX;
    JTextField inputGridX;

    JTextField inputMinY;
    JTextField inputMaxY;
    JTextField inputGridY;


    public void buildGui()
    {
        int horizGap = 30;
        int vertGap = 10;

        JPanel input = new JPanel();
        input.setLayout ( new GridLayout( 3, 2, horizGap, vertGap) );

        inputMinX = new JTextField ( "" + object.getMinX(), 4 );
        inputMaxX = new JTextField ( "" + object.getMaxX(), 4 );
        inputMinY = new JTextField ( "" + object.getMinY(), 4 );
        inputMaxY = new JTextField ( "" + object.getMaxY(), 4);
        inputGridX = new JTextField ( "" + object.getGridX(), 4 );
        inputGridY = new JTextField ( "" + object.getGridY(), 4 );

        input.add ( new JLabel ( "Time Max: " ) );
        input.add ( inputMaxX );
        input.add ( new JLabel ( "PVA Max: " ) );
        input.add ( inputMaxY );
        input.add ( new JLabel ( "Time Min: " ) );
        input.add ( inputMinX );
        input.add ( new JLabel ( "PVA Min: " ) );
        input.add ( inputMinY );
        input.add ( new JLabel ( "Time Grid: " ) );
        input.add ( inputGridX );

        input.add ( new JLabel ( "PVA Grid: " ) );
        input.add ( inputGridY );


        input.setBorder(new EmptyBorder(10,10,10,10));

        buildEasyGui ( "Graph Window", Color.BLACK, Color.ORANGE, input );

    }



    //add a bunch of event handlers to change the object when they trigger

    /*--------------------------------------------------------------------*/
    /* ACCESSORS */
    /*--------------------------------------------------------------------*/

    public String getName ( ) { return object.getName (); }

    //object (g/s)
    public DObject_GraphWindow getObject() {
        return object;


    }
    public void setObject(DObject_GraphWindow iObject) { object = iObject; }


    public void write()
    {


        object.setMinX    ( getDoubleValue ( inputMinX ));
        object.setMaxX    ( getDoubleValue ( inputMaxX ));
        object.setGridX   ( getDoubleValue ( inputGridX ));

        object.setMinY    ( getDoubleValue ( inputMinY ));
        object.setMaxY    ( getDoubleValue ( inputMaxY ));
        object.setGridY   ( getDoubleValue ( inputGridY ));
    }


    private double getDoubleValue ( JTextField textField )
    {
        try {
            return ( (Double.valueOf ( textField.getText() )).doubleValue() );
        } catch ( NumberFormatException e ) {
            return 0.0;
        }
    }
    public IWPObject buildObjectFromDesigner() {
        DObject_GraphWindow goingBack = new DObject_GraphWindow();
        goingBack.setMinX(Double.parseDouble(inputMinX.getText()));
        goingBack.setMaxX(Double.parseDouble(inputMaxX.getText()));
        IWPLog.debug ( this, "XMax at write time: "+Double.parseDouble(inputMaxX.getText()));
        goingBack.setGridX(Double.parseDouble(inputGridX.getText()));
        goingBack.setMinY(Double.parseDouble(inputMinY.getText()));
        goingBack.setMaxY(Double.parseDouble(inputMaxY.getText()));
        goingBack.setGridY(Double.parseDouble(inputGridY.getText()));
        return goingBack;
    }
}
