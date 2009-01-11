package edu.ncssm.iwp.math.designers;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

import edu.ncssm.iwp.math.MEquation;
import edu.ncssm.iwp.ui.widgets.GInput_Text;
import edu.ncssm.iwp.exceptions.*;


/**
 * A new generic Equation editor that is sensitive to syntax checking
 * and missing variables.
 * 
 * @author brockman
 * @date 2007-Feb-01
 *
 */

public class MEquation_Editor extends JComponent implements DocumentListener
{
	private static final long serialVersionUID = 1L;
	String label;
	MEquation equation;
	GInput_Text inputText;
	int inputLength = -1;
	boolean inputOnNewLine = false; // 2008-Dec-25, ability to have doubleLine Inputs.


	public MEquation_Editor ( String label )
	{
		this.label = label;
		this.equation = new MEquation();
		buildGui();
	}
	
	public MEquation_Editor ( String label, MEquation equation )
	{
		this.label = label;
		this.equation = equation;
		buildGui();
	}
	
	public MEquation_Editor ( String label, MEquation equation, int inputLength )
	{
		this.label = label;
		this.equation = equation;
		this.inputLength = inputLength;
		buildGui();
	}

	// 2008-Dec-25 
	public MEquation_Editor ( String label, MEquation equation, int inputLength, boolean inputOnNewLine )
	{
		this.label = label;
		this.equation = equation;
		this.inputLength = inputLength;
		this.inputOnNewLine = inputOnNewLine;
		buildGui();
	}
	
	
	private void buildGui()
	{	
		if ( inputOnNewLine ) { 
			inputText = new GInput_Text(label, equation.getEquationString(), inputLength, inputOnNewLine);
		} else if ( inputLength > 0 ) { 
			inputText = new GInput_Text(label, equation.getEquationString(), inputLength );
		} else {
			inputText = new GInput_Text(label, equation.getEquationString() );
		}
		
		inputText.addDocumentListener(this);
		
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, inputText);
		
	}
		
	
	
	public MEquation getEquation()
		throws InvalidEquationException
	{
		MEquation out = new MEquation(inputText.getValue() );
		out.checkValidity();
		return out;
	}

	
	// Interface: Document
	// When the user types, we want to validate the equation
	
	private void checkEquation()
	{
		try { 
			this.getEquation().checkValidity();
			inputText.releaseError();
			
		} catch ( InvalidEquationException x ) { 
			//IWPLog.x(this, "Invalid: " + x.getMessage(), x); // to chatty.
			inputText.setError("Invalid: " + x.getMessage());	
		}
	}
	
	public void changedUpdate(DocumentEvent e) { checkEquation(); }
	public void insertUpdate(DocumentEvent e) { checkEquation(); }
	public void removeUpdate(DocumentEvent e) { checkEquation(); }
	
	
	
}
