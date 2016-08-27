// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
//
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.objects;

import edu.ncssm.iwp.plugin.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DObject_Window_designer extends DObject_designer implements KeyListener
{
	private static final long serialVersionUID = 1L;
    static int TEXT_WIDTH = 6;

    DObject_Window object;

    public DObject_Window_designer ( DObject_Window iObject)
    {
        object = iObject;
        buildGui();
        setVisible(true);
    }


    JTextField inputMinX;
    JTextField inputMaxX;
    JTextField inputGridX;
    JTextField inputUnitX;

    JTextField inputMinY;
    JTextField inputMaxY;
    JTextField inputGridY;
    JTextField inputUnitY;

    JCheckBox showAllDataAvailable;
    JCheckBox showGridNumbers;
    
    String UnitX;
    String UnitY;

    public void buildGui()
    {

        JPanel input = new JPanel();

        int horizGap = 30;
        int vertGap = 10;

        input.setLayout ( new GridLayout( 5, 4, horizGap, vertGap ) );


        inputMaxX = new JTextField ( "" + object.getMaxX(), TEXT_WIDTH ); inputMaxX.addKeyListener(this);
        inputMaxY = new JTextField ( "" + object.getMaxY(), TEXT_WIDTH ); inputMaxY.addKeyListener(this);
        inputMinX = new JTextField ( "" + object.getMinX(), TEXT_WIDTH ); inputMinX.addKeyListener(this);

        inputMinY = new JTextField ( "" + object.getMinY(), TEXT_WIDTH ); inputMinY.addKeyListener(this);
        inputGridX = new JTextField ( "" + object.getGridX(), TEXT_WIDTH ); inputGridX.addKeyListener(this);

        inputGridY = new JTextField ( "" + object.getGridY(), TEXT_WIDTH ); inputGridY.addKeyListener(this);

        inputUnitX = new JTextField ( "" + object.getUnitX(), TEXT_WIDTH ); inputUnitX.addKeyListener(this);
        inputUnitY = new JTextField ( "" + object.getUnitY(), TEXT_WIDTH ); inputUnitY.addKeyListener(this);

        
        showAllDataAvailable = new JCheckBox ( "", object.isShowAllDataAvailable());
        showGridNumbers = new JCheckBox ( "", object.isDrawGridNumbers() );


        input.add ( new JLabel ( "X Max: " ) );
        input.add ( inputMaxX );
        input.add ( new JLabel ( " Y Max: " ) );
        input.add ( inputMaxY );

        input.add ( new JLabel ( "X Min: " ) );
        input.add ( inputMinX );

        input.add ( new JLabel ( " Y Min: " ) );
        input.add ( inputMinY );

        input.add ( new JLabel ( "X Grid: " ) );
        input.add ( inputGridX );


        input.add ( new JLabel ( " Y Grid: " ) );
        input.add ( inputGridY );

        input.add ( new JLabel ( "X Units: " ) );
        input.add ( inputUnitX );


        input.add ( new JLabel ( " Y Units: " ) );
        input.add ( inputUnitY );
        

        // Seperate panel blow for checkboxes.
        JPanel panelForCheckboxes = new JPanel();
        panelForCheckboxes.setLayout(new GridLayout(2,2)); 
        
        JLabel showAllDataAvailableText = new JLabel ( "Show all available data" );
        //altText.setToolTipText("Use the alternate rounding calculator when you see numbers close to zero off by a very very small amount. Turning this on, however, limits the use of numbers < 1E-9");
        panelForCheckboxes.add ( showAllDataAvailableText );
        panelForCheckboxes.add ( showAllDataAvailable );
        
        JLabel showGridNumbersText = new JLabel ( "Show grid numbers" );
        //altText.setToolTipText("Use the alternate rounding calculator when you see numbers close to zero off by a very very small amount. Turning this on, however, limits the use of numbers < 1E-9");
        panelForCheckboxes.add ( showGridNumbersText );
        panelForCheckboxes.add ( showGridNumbers );
        

        // make a 3rd panel to wrap it all togather
        
        JPanel masterPanel = new JPanel();
        masterPanel.setLayout(new BorderLayout());
        masterPanel.add(BorderLayout.NORTH,input);
        masterPanel.add(BorderLayout.CENTER, panelForCheckboxes);
        masterPanel.setBorder(new EmptyBorder(10,10,10,10));

        buildEasyGui ( "XY Window", Color.BLACK, Color.ORANGE, masterPanel );
    }


    // on keypress, check the values
    

	public void keyPressed(KeyEvent e) {}

	public void keyReleased(KeyEvent e) {  sanityCheckValues();}

	public void keyTyped(KeyEvent e) { sanityCheckValues(); }
	
	private void sanityCheckValues()
	{
		
		// make sure that min is > max
		if ( getDoubleValue(inputMaxX) <= getDoubleValue(inputMinX) ) {
			inputMaxX.setBackground(Color.YELLOW); inputMaxX.setToolTipText("MaxX must be greater than MinX");
			inputMinX.setBackground(Color.YELLOW); inputMinX.setToolTipText("MaxX must be greater than MinX");		
		} else {
			inputMaxX.setBackground(Color.WHITE); inputMaxX.setToolTipText(null);
			inputMinX.setBackground(Color.WHITE); inputMinX.setToolTipText(null);	
		}
		
		
		if ( getDoubleValue(inputMaxY) <= getDoubleValue(inputMinY) ) {
			inputMaxY.setBackground(Color.YELLOW); inputMaxY.setToolTipText("MaxY must be greater than MinY");
			inputMinY.setBackground(Color.YELLOW); inputMinY.setToolTipText("MaxY must be greater than MinY");		
		} else {
			inputMaxY.setBackground(Color.WHITE); inputMaxY.setToolTipText(null);
			inputMinY.setBackground(Color.WHITE); inputMinY.setToolTipText(null);	
		}

	}
	
    

    //add a bunch of event handlers to change the object when they trigger

    /*--------------------------------------------------------------------*/
    /* ACCESSORS */
    /*--------------------------------------------------------------------*/

    public String getName ( ) { return object.getName (); }

    //object (g/s)
    public DObject_Window getObject() {
        return object;


    }
    public void setObject(DObject_Window iObject) { object = iObject; }
    
    

    private double getDoubleValue ( JTextField textField )
    {
        try {
            return ( (Double.valueOf ( textField.getText() )).doubleValue() );
        } catch ( NumberFormatException e ) {
            return 0.0;
        }
    }
    
    
    public IWPObject buildObjectFromDesigner()
    {
        DObject_Window goingBack = new DObject_Window();
        goingBack.setMinX(Double.parseDouble(inputMinX.getText()));
        goingBack.setMaxX(Double.parseDouble(inputMaxX.getText()));

        goingBack.setGridX(Double.parseDouble(inputGridX.getText()));
        goingBack.setGridY(Double.parseDouble(inputGridY.getText()));

        goingBack.setMinY(Double.parseDouble(inputMinY.getText()));
        goingBack.setMaxY(Double.parseDouble(inputMaxY.getText()));

        goingBack.setUnitX(inputUnitX.getText());
        goingBack.setUnitY(inputUnitY.getText());
        
        goingBack.setDrawGridNumbers(showGridNumbers.isSelected());
        goingBack.setShowAllDataAvailable(showAllDataAvailable.isSelected());

        return goingBack;
    }


    
}
