package edu.ncssm.iwp.problemdb;

import java.io.*;
import java.util.*;

import edu.ncssm.iwp.exceptions.*;

import edu.ncssm.iwp.util.*;


/**
 * This is what the problemserver uses to store it files 
 * @author brockman
 */

public class DProblemDB extends DEntity
{
	private static final long serialVersionUID = 1L;
	public static final String PROBLEM_FILE_EXTENSION = ".iwp";

	String problemDirectory;

	//----------------------------------------------------------------------

	public DProblemDB ( String problemDirectory )
		throws DataStoreException
	{
		this.problemDirectory = problemDirectory;
	}


	//-----------------------------------------------------------------------

	public void removeAbsolute ( DUser user, String absFile )
		throws DataStoreException, UnknownProblemException
	{
		// TODO: Make sure the user has real permissions!

		File file = new File ( absolute ( absFile ) );
		file.delete ( );
		// done.
	}

	public void removeRelative ( DUser user,
								 String userFile )
		throws DataStoreException, UnknownProblemException
	{
		removeAbsolute ( user,
						 createUserFilename ( user.getUsername(),
											  userFile ) );
	}


	/* write out a problem file */
	public void set ( DProblem problem )
		throws DataStoreException
	{
		String filename = absolute ( createUserFilename ( problem.getUsername(),
														  problem.getFilename() ) );

		try { 

			IWPLog.info(this,"[DProblemDB.set] saving to: " + filename );

			// make sure the user directory exists
			File userDir = new File ( absolute ( createUserDirectory ( problem.getUsername() ) ) );
			userDir.mkdirs();
			IWPLog.info(this,"[DProblemDB] userDir.mkdir() : " + userDir );


			// write this data to disk.
			String problemData = DProblemXMLParser.save ( problem );

			BufferedWriter out = new BufferedWriter ( new FileWriter ( filename ));
			out.write ( problemData );
			out.close ( );

		} catch ( IOException e ) {
			IWPLog.info(this,"[DProblemDB][set] unable to save: " + filename );
			throw new DataStoreException ( e );
		} catch ( XMLParserException e ) {
			throw new DataStoreException ( e );
		}
	}




	public DProblem getUserProblem ( String username, 
									 String problemName )
		throws DataStoreException, UnknownProblemException
	{
		return get ( createUserFilename ( username, problemName ) );
	}


	/* get a problem out of the filesystem */
	public DProblem get ( String relativeFilename )
		throws DataStoreException, UnknownProblemException
	{
		String absoluteFilename = absolute ( relativeFilename );
		return getAbsolute ( absoluteFilename );
	}

	/* get a problem out of the filesystem */
	public DProblem getAbsolute ( String absoluteFilename )
		throws DataStoreException, UnknownProblemException
	{
		IWPLog.info(this,"[DProblemDB.getAbsolute] Reading: " + absoluteFilename );

		try { 
			
			File inputFile = new File ( absoluteFilename );

			// throw an UnknownUserException if that file dosen't exist
			if ( ! inputFile.exists() ) { 
				throw new UnknownProblemException("File: " + absoluteFilename + " dosen't exist" );
			}

			BufferedReader in = new BufferedReader ( new FileReader ( inputFile ) );
			// BufferedReader in = new BufferedReader ( new FileReader ( filename ) );
			StringBuffer data = new StringBuffer();
			String line;
			int i = 0;

			while ( ( line = in.readLine() ) != null ) { 
				data.append ( line + "\n" );
				i++;
			}

			DProblem problem = DProblemXMLParser.load ( data.toString ( ) );
			// do this too
			problem.setFilename ( inputFile.getName() );
			return problem;

		} catch ( IOException e ) { 
			IWPLog.info(this,"[DProblemDB][set] unable to load: " + absoluteFilename );
			e.printStackTrace();
			throw new DataStoreException ( e );
		} catch ( XMLParserException e ) {
			throw new DataStoreException ( e );
		}

	}



	public void close ( )
		throws DataStoreException
	{

	}


	public Collection getList ( ) 
		throws DataStoreException
	{
		throw new DataStoreException ("NOT DONE");
	}

	// get the list of files for a user
	public Collection getList ( String username ) 
		throws DataStoreException
	{
		throw new DataStoreException ("NOT DONE");
	}

	/**
	 * Get the list of all file items in the user's directory
	 */

	public Collection getUserDirectory ( DUser user ) 
		throws DataStoreException, InvalidPathException
	{
		return getDirectory ( createUserDirectory ( user.getUsername() ) );
	}

	/**
	 * Get the list of all the file items in a specified directory
	 */

	public Collection getDirectory ( String path ) 
		throws DataStoreException, InvalidPathException
	{
		Collection out = new ArrayList ( );

		String fullpath = problemDirectory + File.separator + path;
		File listPath = new File ( fullpath );

		IWPLog.info(this,"[DProblemDB.getDirectory] Fullpath: " + fullpath );

		// make sure this is valid
		if ( ! listPath.exists ( ) ) {
			throw new InvalidPathException("Invalid Directory: " + fullpath );
		}


		String filenames[] = listPath.list ( );
		for ( int i = 0; i < filenames.length; i++ ) { 

			// digest each filename
			String dirName = filenames[i];
			String relFilename = path + File.separator + dirName;
			String absFilename = absolute ( relFilename );

			File file = new File ( absFilename );
			IWPLog.info(this,"[DProblemDB.getDirectory("+path+")] filename: " + absFilename + "  isFile: " + file.isFile( ) );

			if ( file.isFile ( ) && 
				 absFilename.endsWith ( PROBLEM_FILE_EXTENSION ) ) { 

				DProblem problem = null;

				try { 
					// load the problem?
					problem = get ( relFilename );

				} catch ( UnknownProblemException e ) { 
					throw new DataStoreException ( e );
				}

				DDirectoryEntry ent = new DDirectoryFileEntry ( path,
																dirName,
																problem.getUsername ( ) );
				
				out.add ( ent );
	
			} else if ( file.isDirectory ( ) ) { 
				
				DDirectoryEntry ent = new DDirectorySubdirEntry ( path,
																  dirName);
				out.add ( ent );

			}
		}

		// get the list of files that match this pattern

		// also, get the list of subdirectories



		return out;


	}




	/*-----------------------------------------------------------------*/

	private String createUserDirectory ( String userName )
	{
		return userName;
	}

	/* create the file naming scheme */
	private String createUserFilename ( String userName, String problemName )
		throws DataStoreException
	{
		return new String ( createUserDirectory ( userName ) +
							"/"+ problemName );
	}

	private String absolute ( String relative )
	{
		// TODO: take out the double seperators

		return problemDirectory + File.separator + relative;
	}


}



















