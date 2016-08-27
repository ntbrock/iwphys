
package edu.ncssm.iwp.problemdb;

import java.util.Hashtable;

public class DProblemMetaDB extends DEntity
{
	private static final long serialVersionUID = 1L;
	//	String sProblemFile;
	Hashtable oProblemHash;    /* where all the user info is kept in RAM */

/*-----------------------------------------------------------------*/

	public DProblemMetaDB ( ) {
		oProblemHash = new Hashtable();
	}


/*-----------------------------------------------------------------*/

	/* add a user / set user info into the database */
	public void set ( DProblemMeta ioUser )
	{
		/* keyed by username */
		oProblemHash.put ( ioUser.sUserName, ioUser );
	}

	/* get a user out of the database */
	public DProblemMeta get ( String isUserName )
	{
		return (DProblemMeta) oProblemHash.get ( isUserName );
	}

	/* get a list of the Users */
	public DProblemMeta[] getList ( )
	{
		DProblemMeta[] oArray = new DProblemMeta[0];
		return oArray;
	}

/*-----------------------------------------------------------------*/

}







