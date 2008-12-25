/*
  MCalculator_RK4
  The Designer Class for the RK4 Equations

  Date: 04/14/01
*/

package edu.ncssm.iwp.math.designers;

import edu.ncssm.iwp.math.MCalculator;
import edu.ncssm.iwp.math.MCalculator_RK4;
import edu.ncssm.iwp.exceptions.*;
import edu.ncssm.iwp.util.IWPLogPopup;
import java.awt.*;


public class MCalculator_RK4_subDesigner extends MCalculator_Abstract_subDesigner
{
	MCalculator_RK4 object;
	MEquation_Editor oInitDisp;
	MEquation_Editor oInitVel;
	MEquation_Editor oAccelEqn;

	public MCalculator_RK4_subDesigner ( MCalculator_RK4 iObject )
	{
		setLayout ( new GridLayout ( 3, 1 ));

		object = iObject;
		
		// I may want to display a grid here to make these line up better.
		
		oInitDisp = new MEquation_Editor ( "Init. Disp.", object.getInitDispEqn() );
		oInitVel = new MEquation_Editor ( "Init. Vel", object.getInitVelEqn() );
		oAccelEqn = new MEquation_Editor ( "Accel", object.getAccelEqn() );
		
		add ( oInitDisp );
		add ( oInitVel );
		add ( oAccelEqn );

	}


	public MCalculator getCalculator ( )
	{
		try { 
			return new MCalculator_RK4 ( oInitDisp.getEquation(),
						oInitVel.getEquation(),
						oAccelEqn.getEquation()  );
		} catch ( InvalidEquationException e ) { 
			IWPLogPopup.x(this, "RK4 Invalid Equation exception", e );
			return new MCalculator_RK4();
		} catch ( NumberFormatException e ) { 
			IWPLogPopup.x(this, "RK4 Number format exception", e );
			return new MCalculator_RK4();
		}
	}
	
}
