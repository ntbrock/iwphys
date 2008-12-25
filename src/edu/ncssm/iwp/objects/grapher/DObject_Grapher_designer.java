// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
//
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.objects.grapher;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import edu.ncssm.iwp.math.designers.MCalculator_designer;
import edu.ncssm.iwp.objects.*;
import edu.ncssm.iwp.plugin.*;
import edu.ncssm.iwp.ui.widgets.GInput_ColorSelector;
import edu.ncssm.iwp.util.IWPLogPopup;
import edu.ncssm.iwp.graphicsengine.GColor;
import edu.ncssm.iwp.exceptions.*;


public class DObject_Grapher_designer extends DObject_designer implements  KeyListener
{
    static int TEXT_WIDTH = 6;

    DObject_Grapher object;


    public DObject_Grapher_designer (DObject_Grapher iobject )
    {
        object = iobject;
        buildGui();
    }

    JTextField inputName;
    JTextField inputEquation;
    
    JCheckBox inputShowBounding;
    JCheckBox inputTransformCoords;
    
    JTextField inputBoxX;
    JTextField inputBoxY;
    JTextField inputBoxH;
    JTextField inputBoxW;
    
    JTextField inputRes;
    JTextField inputStroke;
    
    
    GInput_ColorSelector inputColor;
    
    public void buildGui()
    {

        JPanel input = new JPanel();

        int horizGap = 30;
        int vertGap = 10;

        input.setLayout ( new GridLayout( 10, 2, horizGap, vertGap) );

        inputName = new JTextField ( "" + object.getName(), TEXT_WIDTH );
        inputName.addKeyListener(this);
        
        inputEquation  	= new JTextField ( "" + object.getEquation(), TEXT_WIDTH );
        inputBoxX		= new JTextField ( "" + object.getBoxX(), TEXT_WIDTH );
        inputBoxY		= new JTextField ( "" + object.getBoxY(), TEXT_WIDTH );
        inputBoxH		= new JTextField ( "" + object.getBoxH(), TEXT_WIDTH );
        inputBoxW		= new JTextField ( "" + object.getBoxW(), TEXT_WIDTH );
        
        inputRes		= new JTextField ( "" + object.getRes(), TEXT_WIDTH );
        inputStroke		= new JTextField ( "" + object.getStroke(), TEXT_WIDTH);
        
        inputShowBounding	 = new JCheckBox ( "", object.getShowBounding() );
        inputTransformCoords = new JCheckBox ( "", object.getTransformCoords() );
        
        inputColor = new GInput_ColorSelector("Graph Color", object.getGColor().getAWTColor());
        
        input.add ( new JLabel ( "Name: " ) );
        input.add ( inputName );

        input.add ( new JLabel ( "Equation f(x)=: " ) );
        input.add ( inputEquation );

        input.add ( new JLabel ( "Graph X Center: " ) );
        input.add ( inputBoxX );

        input.add ( new JLabel ( "Graph Y Center: " ) );
        input.add ( inputBoxY );
        
        input.add ( new JLabel ( "Graph Height: " ) );
        input.add ( inputBoxH );
        
        input.add ( new JLabel ( "Graph Width: " ) );
        input.add ( inputBoxW );
        
        input.add ( new JLabel ( "Graph Calculation Iterations: " ) );
        input.add ( inputRes );
        
        input.add ( new JLabel ( "Line Stroke Size: " ) );
        input.add ( inputStroke );
        

        //Two Option Boxes, Blocked under the label
        input.add	(new JLabel( "Show Bounding Box?"));        
        input.add	(new JLabel("Transform Coordinates?"));
        input.add (inputShowBounding);
        input.add (inputTransformCoords);
        
        JPanel info = new JPanel(); info.setLayout(new GridLayout(1,1));
        	info.add(new JLabel(
        			"<html><center><b>Grapher Object Beta 4.23.2008 -- Plugin by Cory Li </b></center><br>" +
        			"Graphs a function within user specified bounding box (height, width center).  " +
        			"Equations should be functions of x and y, where x is the x position and y is time.  " +
        			"Also, note that none of the inputboxes currently support internal IWP variables and functions, and they follow their own function syntax." +
        			"</html>"));
        	info.setBorder(new EmptyBorder(10,10,10,10));
        	
        input.setBorder(new EmptyBorder(10,10,10,10));

        //JPanel norther = new JPanel(); norther.setLayout(new BorderLayout());
        //norther.add(BorderLayout.NORTH, inputColor);
        //norther.add(BorderLayout.CENTER, extraInput);

        JPanel combined = new JPanel();
        combined.setLayout(new BorderLayout());
        combined.add(BorderLayout.NORTH, info);
        combined.add(BorderLayout.CENTER, input);
        combined.add(BorderLayout.SOUTH, inputColor);
        //combined.add(BorderLayout.CENTER, norther);
               
        buildEasyGui ( "Grapher - Function Graph", Color.WHITE, new Color(183, 42, 125), combined );
    }


    //ACCESSORS

    public String getName () { return object.getName ( ); }

    //object (g/s)
    public DObject_Grapher getObject() { return object; }
    public void setObject(DObject_Grapher iObject) { object = iObject; }


    public IWPObject buildObjectFromDesigner()
    	throws InvalidEquationException, DesignerInputException
    {
    	DObject_Grapher goingBack = new DObject_Grapher();
    	
    	String newName = inputName.getText();
    	
    	if ( newName.length() > 0 ) { 
    		try { 
        		goingBack.setName(inputName.getText());
        	} catch ( InvalidObjectNameX x ) {
        		throw new DesignerInputException ("The name: " + inputName.getText() + " is invalid: " + x.getMessage() );
        	}
        }
    	
    	//goingBack.setText(inputText.getText());
    	goingBack.setEquation(inputEquation.getText());
    	goingBack.setBoxX	(Double.valueOf(inputBoxX.getText()));
    	goingBack.setBoxY	(Double.valueOf(inputBoxY.getText()));
    	goingBack.setBoxW	(Double.valueOf(inputBoxW.getText()));
    	goingBack.setBoxH	(Double.valueOf(inputBoxH.getText()));
    	goingBack.setRes 	(Integer.valueOf(inputRes.getText()));
    	goingBack.setStroke	(Integer.valueOf(inputStroke.getText()));
    	
    	goingBack.setFontColor( new GColor(inputColor.getColor() ));
    	
    	goingBack.setShowBounding(inputShowBounding.isSelected());
    	goingBack.setTransformCoords(inputTransformCoords.isSelected());
    	
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
