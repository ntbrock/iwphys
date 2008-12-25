/*
  MCalculator_RK2
  The Designer Class for the RK2 Equations

  Date: 04/14/01
*/

package edu.ncssm.iwp.math.designers;

import edu.ncssm.iwp.math.MCalculator;
import edu.ncssm.iwp.math.MCalculator_RK2;
import edu.ncssm.iwp.exceptions.*;
import edu.ncssm.iwp.util.IWPLogPopup;

import java.awt.*;

public class MCalculator_RK2_subDesigner extends MCalculator_Abstract_subDesigner 
{
	MCalculator_RK2 object;
	MEquation_Editor initDisp;
	MEquation_Editor initVel;
	MEquation_Editor accelEqn;

	public MCalculator_RK2_subDesigner ( MCalculator_RK2 iObject )
	{
		setLayout ( new GridLayout ( 3, 1 ));

		object = iObject;

		initDisp = new MEquation_Editor ( "Init Disp", object.getInitDispEqn() );
		initVel = new MEquation_Editor ( "Init Vel", object.getInitVelEqn() );
		accelEqn = new MEquation_Editor ( "Accel", object.getAccelEqn() );

		add ( initDisp );
		add ( initVel );
		add ( accelEqn );

	}


	public MCalculator getCalculator ( )
	{
		try { 

			return new MCalculator_RK2 ( initDisp.getEquation(),
										initVel.getEquation(),
										accelEqn.getEquation() );
		
		} catch ( InvalidEquationException e ) { 
			IWPLogPopup.x(this, "RK2 Number Invalid Equation", e );
			return new MCalculator_RK2();
	
		} catch ( NumberFormatException e ) { 
			IWPLogPopup.x(this, "RK2 Number format exception", e );
			return new MCalculator_RK2();
		}
		
	}

}
