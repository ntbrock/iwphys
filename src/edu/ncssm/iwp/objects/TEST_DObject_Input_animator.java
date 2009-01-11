/*
  TEMPLATE FILE

  This is a template flie that is a gui harness for DObject testing
  Replace Input w/ the specific object naming type
*/


package edu.ncssm.iwp.objects;

import javax.swing.*;
import java.awt.event.*;


public class TEST_DObject_Input_animator
	extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	DObject_Input oObject;
	DObject_animator oAnimator;
	JButton oGetButton;

	public TEST_DObject_Input_animator ( )
	{

		oObject = new DObject_Input (  );

		oObject.setText ("Test Input");
		oObject.setUnits ("m");
		oObject.setValue ( 56 );

		oAnimator = oObject.getAnimator (  );


		oGetButton = new JButton ( "DObject_Input_animator.get ( )" );
		oGetButton.addActionListener ( this );
		
		//setLayout ( new BorderLayout ( ) );
		add ( oAnimator );
		//add ( BorderLayout.SOUTH, oGetButton );

	}


	/* INTERFACE: ActionListener */
	public void actionPerformed ( ActionEvent e ) 
	{
		System.err.println ( "[TEST_DObject_Input_animator] get button pressed" );

		// DObject_Input oGetObject = (DObject_Input) oAnimator.get ( );
		// System.err.println ( "[TEST_DObject_Input_animator] return: "+ oGetObject );
	
		
	}

}





