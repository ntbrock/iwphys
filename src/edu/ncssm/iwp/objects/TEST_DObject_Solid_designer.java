/*
  TEMPLATE FILE

  This is a template flie that is a gui harness for DObject testing
  Replace Solid w/ the specific object naming type
*/

package edu.ncssm.iwp.objects;

import javax.swing.*;

import edu.ncssm.iwp.util.IWPLog;

import java.awt.*;
import java.awt.event.*;
import edu.ncssm.iwp.problemdb.*;


public class TEST_DObject_Solid_designer extends JPanel implements ActionListener
{

	DObject_Solid oObject;
	DObject_designer oDesigner;
	JButton oGetButton;

	public TEST_DObject_Solid_designer ( )
	{
		oObject = new DObject_Solid (  );
		oDesigner = (DObject_designer) oObject.getDesigner ( );

		oGetButton = new JButton ( "DObject_Solid_designer.get ( )" );
		oGetButton.addActionListener ( this );

		
		setLayout ( new GridLayout  (1, 1));
		add ( new JScrollPane ( oDesigner ) );

	}


	/* INTERFACE: ActionListener */
	public void actionPerformed ( ActionEvent e ) 
	{
		IWPLog.out( "[TEST_DObject_Solid_designer] get button pressed" );

		try { 
			DObject_Solid oGetObject = (DObject_Solid) oDesigner.buildObjectFromDesigner ( );
			IWPLog.out ( "[TEST_DObject_Solid_designer] return: "+ oGetObject );
		} catch ( Exception x ) {
			IWPLog.x (this, "Generic Exception", x );
		}

	
		
	}

}





