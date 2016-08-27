package edu.ncssm.iwp.problemdb;


import java.util.*;
import edu.ncssm.iwp.exceptions.*;
import edu.ncssm.iwp.util.*;

/**
 * Uses edu.ncssm.iwp.util.IWPMagic file to load a problem either from
 * a resource or inside of the ./etc directory in eclipse's filesystem.
 * 
 * 2006-Aug-15 brockman. Part of the PackagedProblems feature.
 * 
 * @author brockman
 *
 */


public class DProblemManager_Magic extends DProblemManager_Base
{
	public DProblemManager_Magic ( )
	{
	}

	public DProblem loadProblem ( String magicFilename )
		throws DataStoreException
	{
		try { 
		    
			IWPLog.debug(this,"LoadProblem: " + magicFilename );
			IWPMagicFile magicFile = new IWPMagicFile ( magicFilename );
			
			String fileContents = new String ( magicFile.readBytes() );
			
			DProblem prob = DProblemXMLParser.load ( fileContents );
			
			if ( prob != null ) { 
				// set the meta information
				prob.setFilename ( magicFilename );
				prob.setAccessMode ( DProblemManager.MAGIC );
			}

			return prob;

		} catch ( MagicFileNotFoundX e ) { 
			IWPLog.x(this,"MagicFileNotFound: " + magicFilename, e );
			throw new DataStoreException (e);
		} catch ( XMLParserException e ) { 
			
			IWPLog.x(this,"XMLParserException: " + magicFilename, e );
			throw new DataStoreException (e);
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



