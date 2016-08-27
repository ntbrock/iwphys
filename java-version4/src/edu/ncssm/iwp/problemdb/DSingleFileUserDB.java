package edu.ncssm.iwp.problemdb;

import java.io.*;
import java.util.*;

import edu.ncssm.iwp.exceptions.*;

import edu.ncssm.iwp.util.*;

public class DSingleFileUserDB implements DUserDB
{
	public static final String USER_FILE_EXTENSION = ".user";

	DMemoryAuthkeyBank authBank;
	String baseDirectory;

	public DSingleFileUserDB ( String baseDirectory )
		throws DataStoreException
	{
		authBank = new DMemoryAuthkeyBank ( );


		if ( baseDirectory == null ) { 
			throw new DataStoreException ( "baseDirectory was null");
		} else if ( baseDirectory.equals ("") ) {
			throw new DataStoreException ( "baseDirectory was not defined");
		}

		this.baseDirectory = baseDirectory;
	}


	// these are used to determine local filenames

	private String determineStorageFilename ( DUser user )
	{
		return determineStorageFilename ( user.getUsername ( ) );
	}

	private String determineStorageFilename ( String username )
	{
		return baseDirectory + File.separator + username + USER_FILE_EXTENSION;
	}


	//-----------------------------------------------------------------
	// add a user / set user info into the database 
	public void set ( DUser user )
		throws DataStoreException
	{
		// write this down to disk!
		String filename = determineStorageFilename ( user );

		try { 

			// write this data to disk.
			String userData = DUserXMLParser.save ( user );

			BufferedWriter out = new BufferedWriter ( new FileWriter ( filename ));
			out.write ( userData );
			out.close ( );

		} catch ( IOException e ) {
			IWPLog.info(this,"[DProblemManager_File][saveProblem] unable to save: " + filename );
			throw new DataStoreException ( e );
		} catch ( XMLParserException e ) {
			throw new DataStoreException ( e );
		}

	}


	/* get a user out of the database */
	public DUser get ( String username )
		throws UnknownUserException, DataStoreException
	{
		String filename = determineStorageFilename ( username );

		try { 
			
			File inputFile = new File ( filename );

			// throw an UnknownUserException if that file dosen't exist
			if ( ! inputFile.exists() ) { 
				throw new UnknownUserException("File: " + filename + " dosen't exist" );
			}

			BufferedReader in = new BufferedReader ( new FileReader ( inputFile ) );
			// BufferedReader in = new BufferedReader ( new FileReader ( filename ) );
			StringBuffer data = new StringBuffer();
			String line;
			int i = 0;

			while ( ( line = in.readLine() ) != null ) { 
				data.append ( line + "\n" );
				// System.out.println("Going through While loop in loadProblem():time:"+i);
				i++;
			}

			DUser user = DUserXMLParser.load ( data.toString ( ) );

			return user;

		} catch ( IOException e ) { 
			IWPLog.info(this,"[DProblemManager_File][loadProblem] unable to load: " + filename );
			e.printStackTrace();
			throw new DataStoreException ( e );
		} catch ( XMLParserException e ) {
			throw new DataStoreException ( e );
		}

	}

	public void remove ( String username )
		throws DataStoreException, UnknownUserException
	{
		// erase the file for the user
		String filename = determineStorageFilename ( username );
		File inputFile = new File ( filename );

		// throw an UnknownUserException if that file dosen't exist
		if ( ! inputFile.exists() ) { 
			throw new UnknownUserException("File: " + filename + " dosen't exist" );
		}
		
		inputFile.delete ( );
	}


	public DUser createUser ( String username,
							  String password,
							  String email )
		throws DuplicateUserException, DataStoreException
	{

		// see if this user already exists
		try {
			DUser oldUser = get ( username );
			if ( oldUser != null ) { throw new DuplicateUserException ( username ); }
		} catch ( UnknownUserException e ) { 
			// this is ok
		}


		DUser newUser = new DUser ( username, password, email );
		set ( newUser );

		return newUser;
	}


	// load all of the users from disk.
	public Collection getList ( )
		throws DataStoreException
	{
		String currentUsername = null;

		try { 

			// need to do a directory listing here.
			Collection out = new ArrayList ( );
			File baseDir = new File ( baseDirectory );
			
			String[] dirs = baseDir.list ( new DUserFilenameFilter ( ) );
			
			for ( int i = 0; i < dirs.length; i++) { 

				currentUsername = dirs[i].substring ( 0, dirs[i].lastIndexOf( USER_FILE_EXTENSION ) );
				out.add ( get ( currentUsername ) );
			}
			
			return out;

		} catch ( UnknownUserException e ) { 
			throw new DataStoreException ("The finder worked, but the loader crapped out: " + currentUsername );
		}

	}


	public void close ( )
		throws DataStoreException
	{
		// nothing that needs to get done.

	}
	
	//-----------------------------------------------------------------

	public DUser authkeyLookup ( Authkey authkey )
		throws UnknownAuthkeyException, UnknownUserException,
			   DataStoreException
	{
		Authkey key = null;

		key = authBank.lookup ( authkey );
		
		return get ( key.getUsername( ) );
	}

	public Authkey refreshAuthkey ( Authkey authkey )
		throws UnknownAuthkeyException, DataStoreException
	{
		return authBank.refresh ( authkey );
	}


	public Authkey authUser ( String username, String password )
		throws InvalidCredentialsException, UnknownUserException,
			   DataStoreException
	{
		// try to get the user
		DUser user = null;

		user = get ( username );
		if ( ! user.passwordMatches ( password ) ) { 
			throw new InvalidCredentialsException ("Invalid Password");
		}
		
		// have to create an authkey here.
		return authBank.create ( user.getUsername ( ) );
	}

	public void setAdmin ( String adminUsername,
						   String adminPassword )
		throws DataStoreException
	{
		throw new DataStoreException ( "NOT DONE" );
	}

}
