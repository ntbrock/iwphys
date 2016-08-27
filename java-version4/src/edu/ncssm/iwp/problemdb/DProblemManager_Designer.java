
/*
  DProblemManager_Designer
  The designer interface conduit for problems
*/

package edu.ncssm.iwp.problemdb;

import java.util.*;
import edu.ncssm.iwp.ui.*;

import edu.ncssm.iwp.util.*;


public class DProblemManager_Designer extends DProblemManager_Base
{
	GWindow_Designer designer;

	/*---------------------------------------------------------------------*/

	public DProblemManager_Designer ( GWindow_Designer iDesigner )
	{
		designer = iDesigner;
	}


	/*---------------------------------------------------------------------*/

	public DProblem loadProblem ( String problemName )
	{
		return designer.getProblem();
	}

	public void saveProblem ( String problemName, DProblem iProblem )
	{
		/* hand the current problem off to the designer to edit */
		designer.designProblem ( iProblem );
	}


	/*---------------------------------------------------------------------*/

	public Collection getProblemList ( )
	{
		IWPLog.info(this,"[DProblemManager_Designer.getProblemList] Does this make sense?");
		return new ArrayList ( );
	}


}






