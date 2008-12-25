
/* this is the wrapper for the client that connects to the TCP 
   problemserver */

package edu.ncssm.iwp.problemdb;

import java.io.*;
import java.net.*;
import java.util.*;

import edu.ncssm.iwp.exceptions.*;

import edu.ncssm.iwp.util.*;


public class DProblemManager_HTTP extends DProblemManager_Base
{
	public DProblemManager_HTTP ( )
	{
	}

	public DProblem loadProblem ( String urlString )
		throws DataStoreException
	{
		try { 
		    
			URL url = new URL ( urlString );
			IWPLog.info(this,"[DProblemManager_HTTP] URL :"+urlString);
			BufferedReader in = new BufferedReader ( new InputStreamReader ( url.openStream ( ) ) );
			StringBuffer data = new StringBuffer();
			String line;
			int i = 0;

			while ( ( line = in.readLine() ) != null ) { 
				data.append ( line + "\n" );
				// System.out.println("Going through While loop in loadProblem():time:"+i);
				i++;
			}


			DProblem prob = DProblemXMLParser.load ( data.toString() );
			if ( prob != null ) { 
				// set the meta information
				prob.setUsername ( "Local" );
				prob.setFilename ( urlString );
				prob.setAccessMode ( DProblemManager.HTTP );
			}

			return prob;

		    

		} catch ( MalformedURLException e ) { 
			throw new DataStoreException ( e );
		} catch ( IOException e ) { 
			IWPLog.info(this,"[DProblemManager_HTTP.loadProblem] unable to load: " + urlString + ": " + e.getMessage() );
			e.printStackTrace();
			throw new DataStoreException ( e );
		} catch ( XMLParserException e ) { 
			throw new DataStoreException ( e );
		}

	}


	public void saveProblem ( String isProblemName, DProblem problem )
		throws DataStoreException
	{
		throw new DataStoreException ( "NOT SUPPORTED" );
	}

	public Collection getProblemList ( )
		throws DataStoreException
	{
		throw new DataStoreException ( "NOT SUPPORTED" );
	}

}



