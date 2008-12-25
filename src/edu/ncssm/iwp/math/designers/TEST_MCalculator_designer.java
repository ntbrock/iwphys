package edu.ncssm.iwp.math.designers;

import edu.ncssm.iwp.math.MCalculator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * A gui test for the MCalculator.
 * @author brockman
 *
 */

public class TEST_MCalculator_designer extends JPanel implements ActionListener
{

	MCalculator oCalc;
	MCalculator_designer designer;
	
	JPanel outputPanel;
	JButton outputButton;
	JLabel outputValue;
	
	public static void main(String args[]) {
		
		
		JFrame window = new JFrame("TEST_MCalculator_Designer");
		TEST_MCalculator_designer me = new TEST_MCalculator_designer();
		
		window.setLayout(new BorderLayout());
		window.add(BorderLayout.CENTER, me);
		
		window.pack();
		window.setVisible(true);
		
		// Run the window
		
	}
	
	public TEST_MCalculator_designer ( )
	{
		// Create the button + the label for the 'get' harness
		outputPanel = new JPanel();
		outputValue = new JLabel("Press get to generate MCalculator");
		outputButton = new JButton ( "MCalculator_designer.get()" );
		outputButton.addActionListener ( this );

		outputPanel.setLayout(new BorderLayout());
		outputPanel.add(BorderLayout.NORTH, outputButton);
		outputPanel.add(BorderLayout.CENTER, outputValue);


		// Designer Construction
		designer = new MCalculator_designer(null, "value");
		
		// Toplevel window layout
		
		setLayout ( new BorderLayout ( ) );
		add ( BorderLayout.CENTER, designer );
		add ( BorderLayout.SOUTH, outputPanel );
		

	}


	/* INTERFACE: ActionListener */
	public void actionPerformed ( ActionEvent e ) 
	{
		try { 
			
			/* acquire the NEW object thru the interface */
			MCalculator oNewCalc = designer.get ();
			outputValue.setText("Got a calc. Type: " + oNewCalc.getType());
			
		} catch ( Exception x ) { 
			outputValue.setText("X: "+ x.getMessage() );
		}

	}
}

