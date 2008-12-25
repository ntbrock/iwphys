package edu.ncssm.iwp.math.designers;

import edu.ncssm.iwp.math.*;
import edu.ncssm.iwp.util.IWPLog;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class TEST_MCalculator_Parametric_simpleDesigner extends JPanel implements ActionListener
{

	MCalculator oCalc;
	MCalculator_Parametric_simpleDesigner designer;

	JPanel outputPanel;
	JButton outputButton;
	JLabel outputValue;
	
	public static void main(String args[]) {
		
		
		JFrame window = new JFrame("TEST_MCalculator_Parametric_simpleDesigner");
		TEST_MCalculator_Parametric_simpleDesigner me = new TEST_MCalculator_Parametric_simpleDesigner();
		
		window.setLayout(new BorderLayout());
		window.add(BorderLayout.CENTER, me);
		
		window.pack();
		window.setVisible(true);
		
		// Run the window
		
	}
	
	public TEST_MCalculator_Parametric_simpleDesigner ( )
	{
		// Create the button + the label for the 'get' harness
		outputPanel = new JPanel();
		outputValue = new JLabel("Press get to calculate");
		outputButton = new JButton ( "MCalculator_designer.get ( )" );
		outputButton.addActionListener ( this );

		outputPanel.setLayout(new BorderLayout());
		outputPanel.add(BorderLayout.NORTH, outputButton);
		outputPanel.add(BorderLayout.CENTER, outputValue);


		// Designer Construction
		designer = new MCalculator_Parametric_simpleDesigner("y_disp");
		
		
		// Toplevel window layout
		
		setLayout ( new BorderLayout ( ) );
		add ( BorderLayout.CENTER, designer );
		add ( BorderLayout.SOUTH, outputPanel );
		

	}


	/* INTERFACE: ActionListener */
	public void actionPerformed ( ActionEvent e ) 

	{
		IWPLog.info ( "[CalculatorButtonListener] getCalculator Button Pressed" );

		try {
		
			/* acquire the NEW object thru the interface */
			MCalculator_Parametric oNewCalc = (MCalculator_Parametric) designer.get ();

			outputValue.setText( oNewCalc.getEquation().getEquationString() );
			
		} catch ( Exception x ) { 
			outputValue.setText( x.getClass().getName() + ": " + x.getMessage() );
		}

	}
}

