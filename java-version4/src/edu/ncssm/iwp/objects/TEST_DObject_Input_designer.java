/*
  TEMPLATE FILE

  This is a template flie that is a gui harness for DObject testing
  Replace Input w/ the specific object naming type
*/


package edu.ncssm.iwp.objects;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import edu.ncssm.iwp.util.IWPLog;


public class TEST_DObject_Input_designer extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	DObject_Input oObject;
	DObject_designer oDesigner;
	JButton oGetButton;

	public TEST_DObject_Input_designer ( )
	{
		oObject = new DObject_Input (  );
		oDesigner = (DObject_designer) oObject.getDesigner ( );

		oGetButton = new JButton ( "DObject_Input_designer.get ( )" );
		oGetButton.addActionListener ( this );
		
		setLayout ( new BorderLayout ( ) );
		add ( BorderLayout.NORTH, oDesigner );
		add ( BorderLayout.SOUTH, oGetButton );

	}


	/* INTERFACE: ActionListener */
	public void actionPerformed ( ActionEvent e ) 
	{
		IWPLog.out( this, "get button pressed" );

		try {
			DObject_Input oGetObject = (DObject_Input) oDesigner.buildObjectFromDesigner ( );
			IWPLog.out ( this, "return: "+ oGetObject );
		} catch ( Exception x ) {
			IWPLog.x (this, "Generic Exception", x );
		}
		
	}

}



