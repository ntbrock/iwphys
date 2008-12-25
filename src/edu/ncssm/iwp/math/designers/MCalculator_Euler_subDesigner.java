/*
  MCalculator_Euler_designer
  The Designer Class for the Euler Equations

  Date: 02/17/01
  Author: Brockman
*/

package edu.ncssm.iwp.math.designers;

import edu.ncssm.iwp.math.MCalculator;
import edu.ncssm.iwp.math.MCalculator_Euler;
import edu.ncssm.iwp.util.*;
import edu.ncssm.iwp.exceptions.InvalidEquationException;

import java.awt.*;



public class MCalculator_Euler_subDesigner extends MCalculator_Abstract_subDesigner
{
	MCalculator_Euler object;
	MEquation_Editor oInitDisp;
	MEquation_Editor oInitVel;
	MEquation_Editor oAccelEqn;

	public MCalculator_Euler_subDesigner ( MCalculator_Euler iObject )
	{
		setLayout ( new GridLayout ( 3, 1 ));

		object = iObject;

		oInitDisp = new MEquation_Editor ( "Init. Disp.", object.getInitDispEqn() );
		oInitVel = new MEquation_Editor ( "Init. Vel", object.getInitVelEqn() );
		oAccelEqn = new MEquation_Editor ( "Accel", object.getAccelEqn() );

		add ( oInitDisp );
		add ( oInitVel );
		add ( oAccelEqn );

	}


	public MCalculator getCalculator ( ) {
		
		try { 
			return new MCalculator_Euler ( oInitDisp.getEquation(),
										oInitVel.getEquation() ,
										oAccelEqn.getEquation() );
		} catch ( InvalidEquationException e ) { 
			IWPLogPopup.x(this, "Euler Number format exception", e );
			return new MCalculator_Euler();
		} catch ( NumberFormatException e ) { 
			IWPLogPopup.x(this, "Euler Number format exception", e );
			return new MCalculator_Euler();
		}

	}

    public MCalculator get() {return getCalculator();}

	public void write ( )
	{
		IWPLog.info(this,"[MCalculator_Euler_designer] write ( ) not finished" );
	}


}
