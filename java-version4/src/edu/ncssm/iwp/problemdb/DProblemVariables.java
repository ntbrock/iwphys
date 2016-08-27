
/*
  DProblem
  The object that reprensents problems.
  This guy is VERY important.

  Author: Taylor Brockman
  Date: 6/10/00
*/

package edu.ncssm.iwp.problemdb;

import edu.ncssm.iwp.plugin.*;
import java.util.*;
import edu.ncssm.iwp.util.*;


public class DProblemVariables extends DEntity
{
	private static final long serialVersionUID = 1L;
	Hashtable vars;

/*--------------------------------------------------------------------*/

	public DProblemVariables ( )
	{
		vars = new Hashtable();
	}

	public DProblemVariables ( Hashtable h )
	{
		vars = h;
	}

/*--------------------------------------------------------------------*/

	public double getVariable ( String iVar )
	{

		/* this function needs to scan the list of available 
		   variables and return the double value */

		return 0.0;

	}



	public Hashtable getHash ( ) 
	{
		//IWPLog.info(this,"[DProblemVariables] getVariableHash not done");
		return vars;
	}


	public void updateVariables ( IWPObject iObject ) {

		/* scan the object for variables, and update them */
		/* particularily useful for Inputs */

		IWPLog.error(this,"[DProblemVariables] updateVariables not done");

	}

}


