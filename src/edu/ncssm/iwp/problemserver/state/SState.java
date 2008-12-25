
/*
  SState
  The State of the nation

  This object is what's passed on down the line to the sub-objects.
  It is basically just a collection of pointers to runtime resources.
  It prevents having to pass down many individual pointers

*/

package edu.ncssm.iwp.problemserver.state;

import edu.ncssm.iwp.problemserver.conf.*;
import edu.ncssm.iwp.problemdb.*;


public class SState
{
	private static SState globalState = null;

	private CConfigurationData configurationData = null;
	private DDataStore dataStore = null;

	public SState ( ) { }

	/*-------------------------------------------------------------------*/	

	public CConfigurationData getConfigurationData ( )
	{
		return configurationData;
	}

	public void setConfigurationData ( CConfigurationData configurationData )
	{
		this.configurationData = configurationData;
	}


	public DDataStore getDataStore ( ) 
	{
		return dataStore;
	}

	public void setDataStore ( DDataStore dataStore )
	{
		this.dataStore = dataStore;
	}

	/*-------------------------------------------------------------------*/

	public static SState getInstance ( )
	{
		if ( globalState == null ) { globalState = new SState ( ); }
		return globalState;
	}

}

