package edu.ncssm.iwp.problemserver.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.exceptions.*;

import edu.ncssm.iwp.util.*;


public class TEST_SwingXmlRpcClient extends JFrame
	implements ProblemListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	//----- TEST ----------------------------------------------------------
	/**
	 * A Test frame
	 * @author brockman 08.08.03
	 */

	private JButton setup;
	private JButton load;
	private JButton save;
	private JButton saveAs;
	private SwingXmlRpcClient client;

	public TEST_SwingXmlRpcClient ( String host, String user, String pass )
	{
		client = new SwingXmlRpcClient ( host, user, pass, this );
		constructGui ( );
	}


	private void constructGui ( )
	{
		// setup the menu drop-down simulation gui
		setTitle ( "TEST_SwingXmlRpcClient");
		setSize ( new Dimension ( 100, 130 ) );
		
		getContentPane().setLayout ( new GridLayout ( 4, 1 ) );
		
		setup = new JButton ( "Setup" );
		load = new JButton ( "Load" );
		save = new JButton ( "Save" );
		saveAs = new JButton ( "Save As" );
		
		setup.addActionListener ( this );
		load.addActionListener ( this );
		save.addActionListener ( this );
		saveAs.addActionListener ( this );
		
		getContentPane().add ( setup );
		getContentPane().add ( load );
		getContentPane().add ( save );
		getContentPane().add ( saveAs );
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			
	}

	public void actionPerformed ( ActionEvent e )
	{
		JButton button = (JButton) e.getSource();

		if ( button == setup ) {
			client.showSetupProblemDialogue ( );
		} else if ( button == load ) {
			client.showLoadProblemDialogue ( );
		} else if ( button == saveAs ) {
			client.showSaveAsProblemDialogue ( );
		} else if ( button == save ) {
			client.runSave ( );
		}
	}


	public static void main ( String args[] )
	{
		if ( args.length < 3 ) {
			IWPLog.out("Usage: SwingXmlRpcClient [host] [username] [password]");
			System.exit(-1);
		}


        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

		TEST_SwingXmlRpcClient self = new TEST_SwingXmlRpcClient ( args[0],
																   args[1],
																   args[2] );
		self.setVisible ( true );
	}


	//------ Problem Listener Inteface -------------------------------
	public void loadProblem ( DProblem problem )
	{
		IWPLog.info(this,"[TEST_SwingXmlRpcClient] LoadProblem: " + problem );
	}

	public void saveProblem ( ProblemWriter output )
	{
		String filenameToSave = "swing.iwp";

		try {
			IWPLog.info(this,"[TEST_SwingXmlRpcClient] I'm being told to save my current problem");
			output.writeProblem ( readProblemFile ( filenameToSave ) );
		} catch ( DataStoreException e ) {
			IWPLog.info(this,"[TEST_SwingXmlRpcClient] Unable to load problem: " + filenameToSave );
		}
	}

		
	/**
	 * used by main
	 */
	private static DProblem readProblemFile ( String filename )
		throws DataStoreException
	{
		DProblemManager_File fileMan = new DProblemManager_File ( "." ); //cwd
		DProblem problem = fileMan.loadProblem ( filename );

		return problem;
	}



}
