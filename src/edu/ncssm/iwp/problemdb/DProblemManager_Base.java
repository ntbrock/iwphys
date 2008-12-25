package edu.ncssm.iwp.problemdb;

import java.util.*;

import edu.ncssm.iwp.exceptions.*;

public abstract class DProblemManager_Base
{

	public DProblemManager_Base () {}
	
	public abstract DProblem loadProblem ( String isProblemName )
		throws DataStoreException;

	public abstract void saveProblem ( String isProblemName,
									   DProblem ioProblem )
		throws DataStoreException;

	public abstract Collection getProblemList ( )
		throws DataStoreException;
}

