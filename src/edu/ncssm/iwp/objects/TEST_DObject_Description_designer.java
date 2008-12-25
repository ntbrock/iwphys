/*
  TEMPLATE FILE

  This is a template flie that is a gui harness for DObject testing
  Replace Description w/ the specific object naming type
*/

package edu.ncssm.iwp.objects;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import edu.ncssm.iwp.util.*;


public class TEST_DObject_Description_designer extends JPanel implements ActionListener
{

	DObject_Description oObject;
	DObject_designer oDesigner;
	JButton oGetButton;

	public TEST_DObject_Description_designer ( )
	{

		oObject = new DObject_Description (  );
		oDesigner = (DObject_designer) oObject.getDesigner (  );

		oGetButton = new JButton ( "DObject_Description_designer.get ( )" );
		oGetButton.addActionListener ( this );
		
		setLayout ( new BorderLayout ( ) );
		add ( BorderLayout.CENTER, oDesigner );
		add ( BorderLayout.SOUTH, oGetButton );

	}


	/* INTERFACE: ActionListener */
	public void actionPerformed ( ActionEvent e ) 
	{
		IWPLog.out(this, "[TEST_DObject_Description_designer] get button pressed" );

		try {
			DObject_Description oGetObject = (DObject_Description) oDesigner.buildObjectFromDesigner ( );
			IWPLog.out (this, "[TEST_DObject_Description_designer] return: "+ oGetObject );
		} catch ( Exception x ) {
			IWPLog.x (this, "Generic Exception", x );
		}
		
	
		
	}

}



