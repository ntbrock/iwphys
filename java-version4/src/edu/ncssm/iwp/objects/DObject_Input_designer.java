// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
//
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.objects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import edu.ncssm.iwp.exceptions.DesignerInputException;
import edu.ncssm.iwp.exceptions.InvalidObjectNameX;
import edu.ncssm.iwp.plugin.IWPObject;
import edu.ncssm.iwp.ui.widgets.GInput_Text;
import edu.ncssm.iwp.util.IWPLog;
import edu.ncssm.iwp.util.IWPLogPopup;


public class DObject_Input_designer extends DObject_designer
    implements DocumentListener, KeyListener
{
	private static final long serialVersionUID = 1L;
    public DObject_Input object;

    JTextField inputName;
    JTextField inputText;
    JTextField inputValue;
    JTextField inputUnits;
    //JComboBox comboUnits;
    JCheckBox inputHidden;

    public DObject_Input_designer ( DObject_Input iobject )
    {
        object = iobject;
        buildGui();
    }

    GInput_Text inputWidget;


    public void removeUpdate(DocumentEvent e)
    {
        object.setInitialValue(inputWidget.getDoubleValue());
    }
    public void insertUpdate(DocumentEvent e)
    {
        object.setInitialValue(inputWidget.getDoubleValue());
    }
    public void changedUpdate(DocumentEvent e) {}


    public void buildGui()
    {

        JPanel input = new JPanel();
        input.setLayout ( new GridLayout ( 5, 2, CELLGAP, CELLGAP ));


        inputName = new JTextField ( object.getName(), 16 );  //16 is correct
        inputName.addKeyListener(this);
        inputText = new JTextField ( object.getText(), 16 );  //16 is correct
        inputValue = new JTextField ( object.getInitialValue() + "", 8 );
        //comboUnits = new JComboBox();
        inputUnits = new JTextField(object.getUnits());
        inputHidden = new JCheckBox ( "", ! object.isVisible() );

        input.add ( new JLabel ( "Name: " ));
        input.add ( inputName );

        input.add ( new JLabel ( "Text: " ));
        input.add ( inputText );

        input.add ( new JLabel ( "Init Value: " ));
        input.add ( inputValue );

        //comboUnits.setPreferredSize ( new Dimension ( 100, 20 ));
        //comboUnits.setEditable( true );
        inputUnits.setPreferredSize(new Dimension(100,20));

        input.add ( new JLabel ( "Units: " ));
        //input.add ( comboUnits );
        input.add(inputUnits);

        input.add ( new JLabel ( "Hidden: " ));
        input.add ( inputHidden );


        input.setBorder(new EmptyBorder(10,10,10,10));
        buildEasyGui ( "Input", Color.BLACK, Color.GREEN, input );
    }


    public void think() {}


    /*---------------------------------------------------------------*/
    /* Accessors */
    /*---------------------------------------------------------------*/

    public String getName ( ) { return object.getName ( ); }


    /*---------------------------------------------------------------*/

  

    public IWPObject buildObjectFromDesigner()
    	throws DesignerInputException
    {
        DObject_Input goingBack = new DObject_Input();
        IWPLog.debug(this,""+inputName.getText());

        // 2007-Jun-03 brockman: don't let people specify invalid object names.
    	if ( inputName.getText().length() > 0 ) { 
    		try { 
    			goingBack.setName(inputName.getText());
    		} catch ( InvalidObjectNameX x ) {
    			throw new DesignerInputException ("The name: " + inputName.getText() + " is invalid: " + x.getMessage() );
    		}
    	}
		
        IWPLog.debug(this,inputText.getText());
        goingBack.setText(inputText.getText());
        IWPLog.debug(this,""+Double.parseDouble(inputValue.getText()));
        goingBack.setInitialValue(Double.parseDouble(inputValue.getText()));
        
        goingBack.setUnits(inputUnits.getText());
        IWPLog.debug(this,""+inputHidden.isSelected());
        goingBack.setHidden(inputHidden.isSelected());
        return goingBack;
    }
    
    
    

    public void keyTyped(KeyEvent e) { }
    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e)
    {

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


    
    
    
    //to satisfy old calls.
    /**
     * @deprecated
     */
    public void setUnitList (Vector in) {}
    
    /**
     * @deprecated
     */
    public Vector getUnitList() {
    Vector goBack=new Vector(1,1);
    goBack.add(inputUnits.getText());
    return goBack;
    }
    
    
}






