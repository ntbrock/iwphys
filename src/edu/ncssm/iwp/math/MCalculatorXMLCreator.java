package edu.ncssm.iwp.math;


import edu.ncssm.iwp.toolkit.xml.*;

import edu.ncssm.iwp.util.*;


public class MCalculatorXMLCreator extends XMLCreator
{
	MCalculator calc;

	public MCalculatorXMLCreator ( MCalculator calc )
	{
		this.calc = calc;
	}


	public XMLElement getElement ( ) 
	{
		XMLElement elem = new XMLElement ( "calculator" );

		if ( calc instanceof MCalculator_Parametric ) {
			elem.addAttribute ( "type", "parametric" );
			elem.addElement ( new XMLElement ( "value", ((MCalculator_Parametric)calc).getEquationString() ));

		} else if ( calc instanceof MCalculator_Diff ) {
			
			elem.addElement (new XMLElement("displacement",((MCalculator_Diff)calc).getInitDispEqn().getEquationString()));
			elem.addElement (new XMLElement("velocity",((MCalculator_Diff)calc).getInitVelEqn().getEquationString()));				
			elem.addElement (new XMLElement("acceleration",((MCalculator_Diff)calc).getAccelEqn().getEquationString()));
			
			if ( calc instanceof MCalculator_Diff ) {
				elem.addAttribute ( "type", "euler" );
			} else if ( calc instanceof MCalculator_RK2 ) {
				elem.addAttribute ( "type", "RK2" );
			} else if ( calc instanceof MCalculator_RK4 ) {
				elem.addAttribute ( "type", "RK4" );
			}

		} else {
			IWPLog.info(this,"[MCalculatorXMLCreator] ERROR: Our Calculator detection fell thou!!!");
			return null;
		} 
		
		return elem;
	}

	/*-----------------------------------------------------------------*/
	
}

