package edu.ncssm.iwp.problemdb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import edu.ncssm.iwp.ui.*;
import edu.ncssm.iwp.exceptions.*;

import edu.ncssm.iwp.util.*;


public class DProblemManager_HttpOpen_gui extends JFrame
	implements ActionListener
{
	private static final long serialVersionUID = 1L;

	DProblemManager manager;
	GWindow_Designer designer;

	JLabel label = new JLabel ( "Url: ");
	JTextField text = new JTextField ( "", 15 );
	JButton ok = new JButton ( "Ok" );


	public DProblemManager_HttpOpen_gui ( DProblemManager manager,
										  GWindow_Designer designer )
	{
		this.manager = manager;
		this.designer = designer;

		buildGui();
	}

	
	private void buildGui ( ) 
	{
		JPanel add = new JPanel();
		add.add ( label );
		add.add ( text );
		add.add ( ok );


		label.setPreferredSize ( new Dimension ( label.getPreferredSize().width, text.getPreferredSize().height ) );


		ok.addActionListener ( this );

		getContentPane().add ( add );
		// getContentPane().setSize ( add.getPreferredSize() );

		setTitle ( "Open Url...");
		setSize ( 300, 60 );

	}

	public void actionPerformed ( ActionEvent e ) 
	{
		if ( e.getSource() == ok ) { 

			setProblem ( );
			setVisible ( false );
		}
	}

	public void display ( )
	{
		text.setText("");
		setVisible ( true );
	}



	private void setProblem ( )
	{
		String url = text.getText();

		IWPLog.info(this,"[DProblemMnager_HttpOpen_gui] url> " + url );


		try { 
			designer.designProblem ( manager.loadUrl ( url ) );
		} catch ( DataStoreException e ) { 
			// make a new alert window?
			IWPLog.info(this,"[DProblemManager_HttpOpen_gui] ALERT: unable to load: " + url + ": " + e.getMessage() );
		}

	}


}



