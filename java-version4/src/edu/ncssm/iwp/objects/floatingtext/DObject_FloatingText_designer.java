// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
//
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.objects.floatingtext;

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


public class DObject_FloatingText_designer extends DObject_designer implements  KeyListener
{
	private static final long serialVersionUID = 1L;
    static int TEXT_WIDTH = 6;

    DObject_FloatingText object;


    public DObject_FloatingText_designer (DObject_FloatingText iobject )
    {
        object = iobject;
        buildGui();
    }

    JTextField inputName;
    JTextField inputText;
    JTextField inputFontSize;
    JCheckBox inputShowValue;
    JTextField inputUnits;
    
    GInput_ColorSelector inputColor;
    MCalculator_designer inputValue;
    MCalculator_designer inputXpath;
    MCalculator_designer inputYpath;


    public void buildGui()
    {

        JPanel input = new JPanel();

        int horizGap = 30;
        int vertGap = 10;

        input.setLayout ( new GridLayout( 5, 2, horizGap, vertGap) );

        inputName = new JTextField ( "" + object.getName(), TEXT_WIDTH );
        inputName.addKeyListener(this);
        
        inputText  = new JTextField ( "" + object.getText(), TEXT_WIDTH );
        inputFontSize  = new JTextField ( "" + object.getFontSize(), TEXT_WIDTH );
        inputShowValue = new JCheckBox ( "", object.getShowValue() );
        inputUnits  = new JTextField ( "" + object.getUnits(), TEXT_WIDTH );       
        
        inputColor = new GInput_ColorSelector("Font Color", object.getGColor().getAWTColor());
        inputValue = object.getValue().getDesigner("Value");
        inputXpath = object.getXpath().getDesigner("X Path");
        inputYpath = object.getYpath().getDesigner("Y Path");
        
        
        input.add ( new JLabel ( "Name: " ) );
        input.add ( inputName );

        input.add ( new JLabel ( "Text: " ) );
        input.add ( inputText );

        input.add ( new JLabel ( "Show Value: " ) );
        input.add ( inputShowValue );

        input.add ( new JLabel ( "Units: " ) );
        input.add ( inputUnits );
        
        input.add ( new JLabel ( "Font Size: " ) );
        input.add ( inputFontSize );
        
        input.setBorder(new EmptyBorder(10,10,10,10));

        JPanel extraInput = new JPanel();
        extraInput.setLayout(new GridLayout(3,1,5,5));
        extraInput.add(inputValue);
        extraInput.add(inputXpath);
        extraInput.add(inputYpath);

        JPanel norther = new JPanel(); norther.setLayout(new BorderLayout());
        norther.add(BorderLayout.NORTH, inputColor);
        norther.add(BorderLayout.CENTER, extraInput);

        JPanel combined = new JPanel();
        combined.setLayout(new BorderLayout());
        combined.add(BorderLayout.NORTH, input);
        combined.add(BorderLayout.CENTER, norther);
               
        buildEasyGui ( "Floating Text", Color.WHITE, new Color(86, 117, 167), combined );
    }


    //ACCESSORS

    public String getName () { return object.getName ( ); }

    //object (g/s)
    public DObject_FloatingText getObject() { return object; }
    public void setObject(DObject_FloatingText iObject) { object = iObject; }


    public IWPObject buildObjectFromDesigner()
    	throws InvalidEquationException, DesignerInputException
    {
    	DObject_FloatingText goingBack = new DObject_FloatingText();
    	
    	String newName = inputName.getText();
    	
    	if ( newName.length() > 0 ) { 
    		try { 
        		goingBack.setName(inputName.getText());
        	} catch ( InvalidObjectNameX x ) {
        		throw new DesignerInputException ("The name: " + inputName.getText() + " is invalid: " + x.getMessage() );
        	}
        }
    	
    	goingBack.setText(inputText.getText());
    	goingBack.setFontSize(Integer.parseInt(inputFontSize.getText()));
    	goingBack.setShowValue(inputShowValue.isSelected());
    	goingBack.setUnits(inputUnits.getText());
    	
    	goingBack.setFontColor( new GColor(inputColor.getColor() ));
    	goingBack.setValue(inputValue.get());
    	goingBack.setXpath(inputXpath.get());
    	goingBack.setYpath(inputYpath.get());
    	
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
