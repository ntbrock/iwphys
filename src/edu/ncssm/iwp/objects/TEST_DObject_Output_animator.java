/*
  TEMPLATE FILE

  This is a template flie that is a gui harness for DObject testing
  Replace Output w/ the specific object naming type
*/


package edu.ncssm.iwp.objects;


import javax.swing.*;
import java.awt.event.*;


public class TEST_DObject_Output_animator
	extends JPanel implements ActionListener
{

	DObject_Output oObject;
	DObject_animator oAnimator;
	JButton oGetButton;

	public TEST_DObject_Output_animator ( )
	{

		oObject = new DObject_Output (  );
		oAnimator = oObject.getAnimator (  );

		oGetButton = new JButton ( "DObject_Output_animator.get ( )" );
		oGetButton.addActionListener ( this );
		
		//setLayout ( new BorderLayout ( ) );
		add ( oAnimator );
		//add ( BorderLayout.SOUTH, oGetButton );

	}


	/* INTERFACE: ActionListener */
	public void actionPerformed ( ActionEvent e ) 
	{
		System.err.println ( "[TEST_DObject_Output_animator] get button pressed" );

		// DObject_Output oGetObject = (DObject_Output) oAnimator.get ( );
		// System.err.println ( "[TEST_DObject_Output_animator] return: "+ oGetObject );
	
		
	}

}





