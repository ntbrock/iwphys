package edu.ncssm.iwp.problemdb;


import edu.ncssm.iwp.exceptions.*;


public class DProblemServerDB
{

	public DUserDB oUserDB;
	public DProblemDB oProblemDB;

	public DProblemServerDB ( String isUserDirectory,
							  String isProblemDirectory )
		throws DataStoreException
	{
		// oUserDB = new DMemoryUserDB ( isUserFile );

		// the new-style user database!
		oUserDB = new DSingleFileUserDB ( isUserDirectory );
		oProblemDB = new DProblemDB ( isProblemDirectory );

	}


}

