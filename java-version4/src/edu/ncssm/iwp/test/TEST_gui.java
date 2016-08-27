
package edu.ncssm.iwp.test;

import edu.ncssm.iwp.math.designers.TEST_MCalculator_designer;
import edu.ncssm.iwp.objects.*;
import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.util.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;




/*------------------------------------------------------------------------*/


public class TEST_gui extends JFrame implements WindowListener
{
	private static final long serialVersionUID = 1L;
	JTabbedPane tabbedPane;



	TEST_gui ( )
	{ 
		IWPLog.info(this,"[TEST_gui] Creating Panel framework");

		/* set up this pane */
		setName ( "TEST gui" );
		setTitle ( "TEST gui" );
		getContentPane().setLayout ( new BorderLayout() );
		

		/* create a tabbed pane for each test */
		tabbedPane = new JTabbedPane();
		getContentPane().add ( tabbedPane, BorderLayout.CENTER );


		JPanel oNullPanel = new JPanel ( );
		JLabel oNullLabel = new JLabel ( "DUMMY" );
		oNullPanel.add ( oNullLabel );
		tabbedPane.addTab ("NULL TAB", null, oNullPanel );



		/* Various Widgets */


		tabbedPane.addTab ( "DObject_Output_animator",
							null, new TEST_DObject_Output_animator ( ) );
		tabbedPane.addTab ( "DObject_Input_animator",
							null, new TEST_DObject_Input_animator ( ) );



		tabbedPane.addTab ( "DProblem_designer",
							null, new TEST_DProblem_designer ( ) );

		tabbedPane.addTab ( "DObject_Window_designer",
							null, new TEST_DObject_Window_designer ( ) );
		tabbedPane.addTab ( "DObject_Description_designer",
							null, new TEST_DObject_Description_designer ( ) );
		tabbedPane.addTab ( "DObject_Time_designer",
							null, new TEST_DObject_Time_designer ( ) );
		tabbedPane.addTab ( "DObject_Solid_designer",
							null, new TEST_DObject_Solid_designer ( ) );
		tabbedPane.addTab ( "DObject_Output_designer",
							null, new TEST_DObject_Output_designer ( ) );
		tabbedPane.addTab ( "DObject_Input_designer",
							null, new TEST_DObject_Input_designer ( ) );

		tabbedPane.addTab ( "MCalculator_designer",
							null, new TEST_MCalculator_designer ( ) );




		System.err.println ("[TEST_gui] Packin + showin");

		setSize ( 640, 480 );

		setVisible ( true );
		addWindowListener ( this );
	}


	public static void main ( String ipArgs[] )
	{

		/* just instantiate a new gui, with a pane for 
		   each widget */

		TEST_gui gui = new TEST_gui();
		if ( gui == null ) {  // makes the warning go away.
			IWPLog.error("ERROR: null GUI returned. very weird");
		}

		IWPLog.out ("[TEST_gui] at end");
	}


	/*--------------------------------------------------------------------*/
	/* INTERFACE: WindowListener */

	public void windowOpened ( WindowEvent evt ) { }
	public void windowClosed ( WindowEvent evt ) { }
	public void windowDeiconified ( WindowEvent evt ) { }
	public void windowIconified ( WindowEvent evt ) { }
	public void windowActivated ( WindowEvent evt ) { }
	public void windowDeactivated ( WindowEvent evt ) { }
	public void windowClosing ( WindowEvent evt )
	{
		System.exit(0);
	}


}




