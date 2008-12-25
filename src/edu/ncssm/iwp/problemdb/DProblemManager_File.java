
/* this is the wrapper for the client that connects to the TCP 
   problemserver */

package edu.ncssm.iwp.problemdb;

import java.io.*;
import java.util.*;

import edu.ncssm.iwp.exceptions.*;

import edu.ncssm.iwp.util.*;


public class DProblemManager_File extends DProblemManager_Base
{

	String directory;

	public DProblemManager_File ( String iDirectory )
	{
		directory = iDirectory;
	}


	public void setDirectory ( String iDirectory )
	{
		directory = iDirectory;
	}



	public DProblem loadProblem ( String isProblemName )
		throws DataStoreException
	{
		/* open the file */
		String fullFile = getFullFile ( directory, isProblemName );

		IWPLog.info(this,"Loading file: " + fullFile );

		/* read the file in */

		try { 

			BufferedReader in = new BufferedReader ( new FileReader ( fullFile ) );
			StringBuffer data = new StringBuffer();
			String line;
			int i = 0;

			while ( ( line = in.readLine() ) != null ) { 
				data.append ( line + "\n" );
				// IWPLog.info(this,"Going through While loop in loadProblem():time:"+i);
				i++;
			}

			DProblem prob = DProblemXMLParser.load ( data.toString() );

			if ( prob != null ) { 
				// set the meta information
				// prob.setUsername ( "Local" ); // stored in file now.
				prob.setFilename ( isProblemName );
				prob.setAccessMode ( DProblemManager.FILE );
			}

			return prob;

		} catch ( IOException e ) { 
			IWPLog.info(this,"[DProblemManager_File][loadProblem] unable to load: " + isProblemName );
			e.printStackTrace();
			throw new DataStoreException ( e );
		} catch ( XMLParserException e ) { 
		    e.printStackTrace();
		    IWPLog.info(this,"[DProblemManager_File] Having a parser issue...");
			throw new DataStoreException ( e );
		}

	}


	public void saveProblem ( String isProblemName, DProblem problem )
		throws DataStoreException
	{

		// IWPLog.info(this,"[DProblemManager_file] Saving to file: " + fullFile );

		// IWPLog.info(this,"Data: " + data );

		try { 

			String fullFile = getFullFile ( directory, isProblemName );
			IWPLog.info(this,"WATCHPOINT: use fullFile: " + fullFile + " or use isProblemName: " + isProblemName );
			
			String data = DProblemXMLParser.save ( problem );

			BufferedWriter out = new BufferedWriter ( new FileWriter ( isProblemName ));
			out.write ( data );
			out.close ( );

		} catch ( IOException e ) {
			IWPLog.info(this,"[DProblemManager_File][saveProblem] unable to save: " + isProblemName );
			throw new DataStoreException ( e );
		} catch ( XMLParserException e ) { 
			throw new DataStoreException ( e ); 
		}

	}


	public Collection getProblemList ( )
		throws DataStoreException
	{
		/* get a list of files out of the directory */

		IWPLog.info(this,"[DProblemManager_File] Returning empty list of files in: " + directory );

		throw new DataStoreException ( "NOT DONE");
	}




	private static String getFullFile ( String dir, String file )
	{
		// is it an absolute path?
		File fileObj = new File(file);
		
		if (fileObj.isAbsolute())
		{
			return file;
		}

		// Determine whether absolute path in windows
		String fullFile = dir.concat ( file );
		return fullFile;
	}


	public static String getFile ( String fullPath )
	{
		// find the last /
	    String separator="/";
	    try {separator=System.getProperty("file.separator");}
	    catch(Exception e) {
		IWPLog.x("[DProblemManager_File] could not get the file separator - using '/'", e);
	    }
		int i = fullPath.lastIndexOf ( separator );
		if ( i >= 0 ) { 
			return fullPath.substring ( i + 1 );
		} else { 
			return fullPath;
		}


	}

}





