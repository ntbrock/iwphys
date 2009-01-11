package edu.ncssm.iwp.problemdb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class TEST_DProblem_designer extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	DProblem oProblem;
	DProblem_designer oDesigner;
	JButton oGetButton;

	public TEST_DProblem_designer ( )
	{

		oProblem = new DProblem ( "TEST", "testuser" );
		oDesigner = oProblem.getDesigner (  );

		oGetButton = new JButton ( "DProblem_designer.get ( )" );
		oGetButton.addActionListener ( this );
		
		setLayout ( new BorderLayout ( ) );
		add ( BorderLayout.CENTER, oDesigner );
		add ( BorderLayout.SOUTH, oGetButton );

	}


	/* INTERFACE: ActionListener */
	public void actionPerformed ( ActionEvent e ) 
	{
		System.err.println ( "[TEST_DProblem_designer] get button pressed" );
		
	}

}


