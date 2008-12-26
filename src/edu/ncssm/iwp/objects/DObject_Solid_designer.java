// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
//
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.objects;

import edu.ncssm.iwp.ui.widgets.*;
import edu.ncssm.iwp.plugin.*;
import edu.ncssm.iwp.math.designers.MCalculator_designer;
import edu.ncssm.iwp.graphicsengine.*;
import edu.ncssm.iwp.exceptions.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;

import edu.ncssm.iwp.util.*;
import edu.ncssm.iwp.problemdb.DProblem_designer;

public class DObject_Solid_designer extends DObject_designer implements  KeyListener
{

    DObject_Solid object;


    JTextField inputName;
    GShape_designer inputShape;
    GInput_ColorSelector inputColor;
    MCalculator_designer inputCalcX;
    MCalculator_designer inputCalcY;

    public DObject_Solid_designer ( DObject_Solid iobject )
    {
        object = iobject;

        /* set up the members */

        inputShape = object.getShape().getShape_designer(this);
        inputCalcX = object.getCalcX().getDesigner ( "X Path" );
        inputCalcY = object.getCalcY().getDesigner ( "Y Path" );

        /* construct the user interface */
        buildGui();
        setVisible(true);
    }

    public void buildGui()
    {
        inputColor = new GInput_ColorSelector("Color", object.getGColor().getAWTColor());

        Box box = new Box(BoxLayout.Y_AXIS);

        JPanel input = new JPanel();
        input.setLayout ( new GridLayout ( 1, 2 ));

        inputName = new JTextField( object.getName() );
        inputName.addKeyListener(this);
        input.add ( new JLabel ( "Name: " ));
        input.add ( inputName );


        box.add ( input );

        // Moved the calculators up to the otp.
        box.add(Box.createVerticalStrut( STRUT ));
        box.add(inputCalcX);

        box.add(Box.createVerticalStrut( STRUT ));
        box.add(inputCalcY);

        box.add(Box.createVerticalStrut( STRUT ));
        box.add(inputColor);

        box.add(Box.createVerticalStrut( STRUT ));
        box.add(inputShape);

        // setBackground ( Color.black );

        box.setBorder(new EmptyBorder(10,10,10,10));

        buildEasyGui ( "Solid", Color.WHITE, Color.BLUE, box );
    }


    public Dimension getMinimumSize ( )
    {
        Dimension size = getPreferredSize();
        size.width = WIDTH;
        return size;
    }

    public Dimension getMaximumSize ( )
    {
        return getMinimumSize();
    }



    //ACCESSORS

    //object (g/s)
    public DObject_Solid getObject() { return object; }
    public void setObject(DObject_Solid iObject) { object = iObject; }


    /* THIS SHOULD REALLY DRAW OUT OF THE INPUT BOX */
    public String getName () { return object.getName ( ); }

    public IWPObject buildObjectFromDesigner()
        throws DesignerInputException, InvalidEquationException
    {
        DObject_Solid goingBack = new DObject_Solid();

        // 2007-Jun-03 brockman: don't let people specify invalid object names.
        if ( inputName.getText().length() > 0 ) {
        	try { 
        		goingBack.setName(inputName.getText());
        	} catch ( InvalidObjectNameX x ) {
        		throw new DesignerInputException ("The name: " + inputName.getText() + " is invalid: " + x.getMessage() );
        	}
        }
        
        goingBack.setShape(inputShape.get());
        goingBack.setGColor(new GColor(inputColor.getColor()));
        goingBack.setCalculatorX(inputCalcX.get());
        goingBack.setCalculatorY(inputCalcY.get());
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


    public void doValidation() {

    validate();
    }

    public void iwpRepaint()
    {
		super.iwpRepaint();
    }

}


