
package edu.ncssm.iwp.problemdb;


public class DProblemMeta extends DEntity
{
	private static final long serialVersionUID = 1L;
	public String sProblemName;
	public String sUserName;

/*-------------------------------------------------------------------*/

	public DProblemMeta ( String isProblemName, String isUserName )
	{
		sProblemName = isProblemName;
		sUserName = isUserName;
	}

/*-------------------------------------------------------------------*/


}
