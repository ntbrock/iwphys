// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.objects;


import edu.ncssm.iwp.util.*;
import edu.ncssm.iwp.ui.widgets.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class DObject_Input_animator extends DObject_animator implements KeyListener
{
	DObject_Input object;
	Color origInputBackground = null;
	

	public DObject_Input_animator ( DObject_Input iobject )
	{
		object = iobject;

		buildGui();
		setVisible(true);
	}

	GInput_Text inputName;
	JLabel inputText;
	JTextField inputValue;
	JLabel inputUnits;
	GInput_Boolean inputHidden;

	public void buildGui()
	{
		setLayout(new GridLayout(2,1));
		
		inputText = new JLabel ( object.getText());
		add(inputText);


		Box inputPanel = new Box ( BoxLayout.X_AXIS );
		inputValue = new JTextField ( object.getValue()+"", 8 );
		inputValue.setHorizontalAlignment(JTextField.RIGHT);
		inputPanel.add(inputValue);
		origInputBackground = inputValue.getBackground();
		
		IWPLog.debug(this,"[DObject_Input_animator] object.getValue(): "+object.getValue());

		inputUnits = new JLabel ( " " + object.getUnits() );
		inputPanel.add(inputUnits);

		add ( inputPanel );


		// inputHidden = new GInput_Boolean("Hidden",!(object.isVisible()));
		// add(inputHidden);

		// size appropriately
		/*
		int width = 100;
		int height = 100;

		IWPLog.debug(this,"[DObject_Input_animator] setting width: " + width + ", height: " + height );

		setMinimumSize ( new Dimension ( width, height ) );
		setMaximumSize ( new Dimension ( width, height ) );
		*/
		// setPreferredSize ( new Dimension ( ) );

		// 2006-Aug-30 brockman
		// We have a chicken and egg problem here with inputs getting pushed into MVars.
		// They come in via the 'tick', howver, the MasterTick does all of the object ticks, and
		// then does all of the Animation ticks. So, this creates a situation where a 
		// ui value could not be read when the reset button is pressed.
		inputValue.addKeyListener(this);
		
	}

	
	public void setUserDefinedValueIntoObject()
	{
		double value;
		String text = "OUT OF SCOPE";

		try { 
			text = inputValue.getText();

			if ( text.length() <= 0 ) { 
				IWPLog.info(this,"[DObject_Input_animator.setUserDefinedValue] Length <= 0");
				setError("Empty Value! Please Enter a Number");
				object.setUserValue(0);
			} else {
				value = Double.parseDouble ( text );
				object.setUserValue ( value );
				noError();
			}
			
		} catch ( NumberFormatException e ) { 
			IWPLog.error(this,"[DObject_Input_animator.setUserDefinedValue] NumberFormatException. text: '" + text + "': " + e.getMessage());
			setError("Inputs must be Numeric!");
			object.setUserValue(0);
			
		} catch ( NullPointerException e ) { 
			IWPLog.error(this,"[DObject_Input_animator.setUserDefinedValue] NullPointerException: " + e.getMessage() );
			setError("Null Value! Please Enter a Number");
			object.setUserValue(0);
		}
		

	}


	// IWP 3, make the background yellow + set the tool tip if there's a problem.
	public void setError(String message)
	{
		inputValue.setBackground(Color.YELLOW);
		inputValue.setToolTipText(message);		
	}
	
	public void noError()
	{
		inputValue.setBackground(origInputBackground);
		inputValue.setToolTipText(null);
	}
	
	
	
	//ACCESSORS

	//object (g/s)
	public DObject_Input getObject() { return object; }
	public void setObject(DObject_Input iObject) { object = iObject; }



	/*
	 * 2006-Aug-30 brockman
	 * We have a chicken and egg problem here with inputs getting pushed into MVars.
	 * They come in via the 'tick', howver, the MasterTick does all of the object ticks, and
	 * then does all of the Animation ticks. So, this creates a situation where a 
	 * ui value could not be read when the reset button is pressed.
	 *
	 */
	// I'm not going to mess around with platform incompatibility here.
	public void keyPressed(KeyEvent e)
	{
		setUserDefinedValueIntoObject();
	}
	public void keyReleased(KeyEvent e)
	{
		setUserDefinedValueIntoObject();
	}
	public void keyTyped(KeyEvent e)
	{
		setUserDefinedValueIntoObject();
	}	
		
}




















