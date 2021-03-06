/*
  Window designer files
*/

package edu.ncssm.iwp.objects;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.ncssm.iwp.util.IWPLog;

public class TEST_DObject_Window_designer extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	DObject_Window oObject;
	DObject_designer oDesigner;
	JButton oGetButton;

	public TEST_DObject_Window_designer ( )
	{
		oObject = new DObject_Window ( );
		oDesigner = (DObject_designer)oObject.getDesigner ( );

		oGetButton = new JButton ( "DObject_Window_designer.get ( )" );
		oGetButton.addActionListener ( this );
		
		//setLayout ( new BorderLayout ( ) );
		//add ( BorderLayout.CENTER, oDesigner );
		//add ( BorderLayout.SOUTH, oGetButton );

		setLayout ( new GridLayout  (1, 1));
		add ( new JScrollPane ( oDesigner ) );

	}


	/* INTERFACE: ActionListener */
	public void actionPerformed ( ActionEvent e ) 
	{
		IWPLog.out ( "[TEST_DObject_Window_designer] get button pressed!" );

		try { 
			DObject_Window oGetObject = (DObject_Window) oDesigner.buildObjectFromDesigner ( );
			IWPLog.out ( "[TEST_DObject_Window_designer] return: "+ oGetObject );
		} catch ( Exception x ) {
			IWPLog.x (this, "Generic Exception", x );
		}

	}

}



