/*
  TEMPLATE FILE

  This is a template flie that is a gui harness for DObject testing
  Replace Output w/ the specific object naming type
*/

package edu.ncssm.iwp.objects;

import javax.swing.*;

import edu.ncssm.iwp.util.IWPLog;

import java.awt.*;
import java.awt.event.*;
import edu.ncssm.iwp.problemdb.*;


public class TEST_DObject_Output_designer extends JPanel implements ActionListener
{

	DObject_Output oObject;
	DObject_designer oDesigner;
	JButton oGetButton;

	public TEST_DObject_Output_designer ( )
	{
		oObject = new DObject_Output (  );
		oDesigner = (DObject_designer)oObject.getDesigner ( );

		oGetButton = new JButton ( "DObject_Output_designer.get ( )" );
		oGetButton.addActionListener ( this );
		
		//setLayout ( new BorderLayout ( ) );
		setLayout ( new GridLayout ( 1,1 ) );
		add ( BorderLayout.NORTH, oDesigner );
		//add ( BorderLayout.SOUTH, oGetButton );

	}


	/* INTERFACE: ActionListener */
	public void actionPerformed ( ActionEvent e ) 
	{
		IWPLog.out ( this, "[TEST_DObject_Output_designer] get button pressed" );

		try {
			DObject_Output oGetObject = (DObject_Output) oDesigner.buildObjectFromDesigner ( );
			IWPLog.out ( this, "[TEST_DObject_Output_designer] return: "+ oGetObject );
		} catch ( Exception x ) {
			IWPLog.x (this, "Generic Exception", x );
		}

		
	}

}





