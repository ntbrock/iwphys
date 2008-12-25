// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
//
// For full copyright information, see main source file,
// "iwp.java"


package edu.ncssm.iwp.objects;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.util.*;
import edu.ncssm.iwp.plugin.*;
import edu.ncssm.iwp.util.*;
import edu.ncssm.iwp.exceptions.DesignerInputException;
import edu.ncssm.iwp.exceptions.InvalidObjectNameX;
import edu.ncssm.iwp.math.*;

public class DObject_Output_designer
    extends DObject_designer implements KeyListener
{
    DObject_Output object;

    public DObject_Output_designer ( DObject_Output iobject )
    {
        object = iobject;
        buildGui();
        setVisible(true);
    }

    JTextField inputName;
    JTextField inputText;
    JTextField inputValue;
    JTextField inputUnits;
    JComboBox comboUnits;
    JCheckBox inputHidden;

    JTextField inputEquation;

    public void buildGui()
    {

        JPanel input = new JPanel();
        input.setLayout ( new GridLayout ( 5, 2, CELLGAP, CELLGAP ));


        inputName = new JTextField ( object.getName(), 16 );  //16 is correct
        inputName.addKeyListener(this);
        inputText = new JTextField ( object.getText(), 16 );  //16 is correct
        inputUnits=new JTextField(object.getUnits(), 5);
        //comboUnits = new JComboBox();
        //inputEquation = new JTextField ( object.getEquation(), 16 );

        // This code is a little smarter now, doesn't call a deprecatd function
        // brockman 2006-Apr-29. I'm trying to avoid things like storing data
        // in two places. I'm now just poiunting directly to the string equation
        // inside the parametric calculator.
        try {
            MCalculator_Parametric paramCalc =
                (MCalculator_Parametric) object.getCalculator();
            inputEquation = new JTextField (""+paramCalc.getEquationString(),16);
        } catch ( ClassCastException x ) {
            inputEquation = new JTextField ("DEATH:Calc not Parametric",16);
            IWPLog.x(this, "Calc not Parametric", x);
        }

        inputHidden = new JCheckBox ( "", ! object.isVisible() );

        input.add ( new JLabel ( "Name: " ));
        input.add ( inputName );

        input.add ( new JLabel ( "Text: " ));
        input.add ( inputText );

        input.add ( new JLabel ( "Equation: " ));
        input.add ( inputEquation );


        //comboUnits.setPreferredSize ( new Dimension ( 100, 20 ));
        //comboUnits.setEditable( true );

        input.add ( new JLabel ( "Units: " ));
        //input.add ( comboUnits );
        inputUnits.setPreferredSize(new Dimension(100,20));
        input.add(inputUnits);

        input.add ( new JLabel ( "Hidden: " ));
        input.add ( inputHidden );


        input.setBorder(new EmptyBorder(10,10,10,10));
        buildEasyGui ( "Output", Color.WHITE, Color.RED, input );

    }



    /* This sets the values of the widgets to the objects */
    /* this actually goes right on top of ti's object */
    public void write()
    {
        /*
        object.setName(inputName.getValue());
        object.setText(inputName.getValue());
        object.setEquation(inputEquation.getValue());
        object.setHidden(inputHidden.getState());
        */
    }


    /*---------------------------------------------------------------*/
    /* Accessors */
    /*---------------------------------------------------------------*/

    public String getName ( ) { return object.getName ( ); }


    /*---------------------------------------------------------------*/

    //old calls
    public Vector getUnitList() {
    Vector goBack = new Vector(1,1);
    goBack.add(inputUnits.getText());
    return goBack;
    }
    public void setUnitList(Vector in) {}

    public IWPObject buildObjectFromDesigner()
    	throws DesignerInputException
    {
        IWPLog.debug(this,"[DObject_output_designer](get)...");
        DObject_Output goingBack = new DObject_Output();

        // 2007-Jun-03 brockman: don't let people specify invalid object names.
        try { 
        	if ( inputName.getText().length() > 0 ) {
        		goingBack.setName(inputName.getText());
        	}
        	goingBack.setText(inputText.getText());
        	goingBack.setEquation(inputEquation.getText());
        	goingBack.setUnits(inputUnits.getText());
        	goingBack.setHidden(inputHidden.isSelected());
        
        } catch ( InvalidObjectNameX x ) {
    		throw new DesignerInputException ("The name: " + inputName.getText() + " is invalid: " + x.getMessage() );
        } catch(NullPointerException a) {
        	throw new DesignerInputException("Something was null. Bad");
        }
        return goingBack;
    }
    public void keyTyped(KeyEvent e) { }
    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e)
    {
    	/*The only thing resistered is the name text field
      	 so just update the left panel of the designer
    	 */

    	// 2007-Jun-03 brockman: don't let people specify invalid object names.
    	String newName = inputName.getText();
    	if ( newName.length() > 0 ) { 
    		try { 
    			object.setName(newName);
    		} catch ( InvalidObjectNameX x ) {
    			IWPLogPopup.error(this, x.getMessage() );
    			inputName.setText(object.getName());
    		}
    	}
    	
    	if(parentProblemDesigner!=null) {
    		parentProblemDesigner.refreshDesigner(this);
    	}
    
    	inputName.grabFocus();
    }
}
