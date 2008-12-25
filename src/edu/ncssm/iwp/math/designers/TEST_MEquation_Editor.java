package edu.ncssm.iwp.math.designers;

import edu.ncssm.iwp.math.*;
import edu.ncssm.iwp.util.*;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class TEST_MEquation_Editor extends JPanel implements ActionListener
{
	MEquation_Editor editor;
	
	JPanel actionPanel;
	JButton calcButton;
	JLabel calcLabel;
	JButton varButton;
	JLabel varLabel;
	JButton valButton;
	JLabel valLabel;

	
	
	public static void main(String args[])
	{
		JFrame window = new JFrame("TEST_MEquation_Editor");
		TEST_MEquation_Editor me = new TEST_MEquation_Editor();
		
		window.setLayout(new BorderLayout());
		window.add(BorderLayout.CENTER, me);
		
		window.pack();
		window.setVisible(true);
		
		// Run the window
		
	}
	
	public TEST_MEquation_Editor ( )
	{
		// Create the button + the label for the 'get' harness
		actionPanel = new JPanel();
		
		calcLabel = new JLabel(" ");
		calcButton = new JButton ( "calculate()" );
		calcButton.addActionListener ( this );

		varLabel = new JLabel(" ");
		varButton = new JButton ( "listVariables()" );
		varButton.addActionListener ( this );

		valLabel = new JLabel(" ");
		valButton = new JButton ( "checkValidity()" );
		valButton.addActionListener ( this );

		
		actionPanel.setLayout(new GridLayout(6,1));
		actionPanel.add(calcButton);
		actionPanel.add(calcLabel);
		actionPanel.add(varButton);
		actionPanel.add(varLabel);
		actionPanel.add(valButton);
		actionPanel.add(valLabel);

		

		// Designer Construction
		editor = new MEquation_Editor("value");
		
		
		// Toplevel window layout
		
		setLayout ( new BorderLayout ( ) );
		add ( BorderLayout.CENTER, editor );
		add ( BorderLayout.SOUTH, actionPanel );
		

	}


	/* INTERFACE: ActionListener */
	public void actionPerformed ( ActionEvent e ) 
	{
		if ( e.getSource() == calcButton ) { 
	
			try { 
				calcLabel.setText(editor.getEquation().calculate(new MDataHistoryArrayListImpl())+"");
			} catch ( Exception x ) {
				IWPLogPopup.x(this, x.getMessage(), x);
			}
			
		} else if ( e.getSource() == varButton ) { 

			try { 
				Collection vars = editor.getEquation().listVariables();
				StringBuffer varString = new StringBuffer( vars.size() + " [");
				for ( Iterator i = vars.iterator(); i.hasNext(); ) {
					varString.append( i.next() );
					if ( i.hasNext() ) { varString.append(", "); }
				}
				varString.append("]");
				
				varLabel.setText(varString.toString());
				
			} catch ( Exception x ) {
				varLabel.setText(x.getMessage());
			}
			
		} else if ( e.getSource() == valButton ) { 
			
			try { 
				
				editor.getEquation().checkValidity();
				valLabel.setText("Equation is valid");
			} catch ( Exception x ) {
				IWPLogPopup.x(this, x.getMessage(), x);
			}
			
		}

	}
}

