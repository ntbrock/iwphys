
/*
  DDataStore
  This is the controlling database class. It just worries about putting
  things down to disk + pulling them back.
*/

package edu.ncssm.iwp.problemdb;

import java.util.*;

import edu.ncssm.iwp.exceptions.*;

import edu.ncssm.iwp.util.*;

public class DDataStore
{

	String userDirectory;
	DUserDB userDB;

	String problemDirectory;
	DProblemDB problemDB;

	String admin;
	String adminPassword;

/*--------------------------------------------------------------------*/

	public DDataStore ( String userDirectory, String problemDirectory,
						String admin, String adminPassword )
		throws DataStoreException
	{
		this.userDirectory = userDirectory;
		this.problemDirectory = problemDirectory;

		this.admin = admin;
		this.adminPassword = adminPassword;

		readDatabases();
	}

	/* WE NEED A DECONSTRUCTOR !! */
	protected void finalize ( )
		throws DataStoreException
	{
		IWPLog.info(this,"DDataStore Is being garbage collected!\n");
		writeDatabases();
	}

/*--------------------------------------------------------------------*/



	// this method is heavily outdated now that the server uses single-file
	// as needed writes + reads.

	private void readDatabases ( )
		throws DataStoreException
	{
		/* For User/ProblemMeta:
		   try to deserialize the file... if we can't, then print an
		   error message, and construct a new one ! */

		// open the User DB
		// readUserDB();


		/* for this: just construct a new object - it'll do the rest
		   itself ! */
		/* open the Problem DB */

		
		userDB = new DSingleFileUserDB ( userDirectory );
		problemDB = new DProblemDB ( problemDirectory );

	}



	// this method is heavily outdated now that the server uses single-file
	// as needed writes + reads.
	private void writeDatabases ( )
		throws DataStoreException
	{
		// serialize the UserDB 
		// writeUserDB();

		/* the problem DB will be written on the fly - don't worry to 
		   much about it */

		userDB.close ( );
		problemDB.close ( );

	}

/*--------------------------------------------------------------------*/

	/* put the data down to disk! */
	public void sync ()
		throws DataStoreException
	{
		writeDatabases();
	}


	/* get the total list back */
	public Collection getProblemList ( ) 
		throws DataStoreException
	{
		return problemDB.getList();
	}

	
	public Collection getProblemList ( String username )
		throws DataStoreException
	{
		return problemDB.getList ( username );
	}
	

	/* get a problem out ! */
	public DProblem getProblem ( String filename )
		throws DataStoreException, UnknownProblemException
	{
		return problemDB.get ( filename );
	}


	public DProblem getUserProblem ( DUser user,
									 String filename )
		throws DataStoreException, UnknownProblemException
	{
		return problemDB.getUserProblem ( user.getUsername(), filename );
	}


	/**
	 * Delete a problem file, given the user and user file name
	 * @author brockman 07.18.03
	 */
	public void removeRelativeProblem ( DUser user,
										String filename )
		throws DataStoreException, UnknownProblemException
	{
		problemDB.removeRelative ( user, filename );
	}

	public void removeAbsoluteProblem ( DUser user,
										String absFilename )
		throws DataStoreException, UnknownProblemException
	{
		problemDB.removeAbsolute ( user, absFilename );
	}


	/* get a problem out ! */
	public DProblem getAbsoluteProblem ( String filename )
		throws DataStoreException, UnknownProblemException
	{
		return problemDB.getAbsolute ( filename );
	}

	/* put a problem back in */
	public void setProblem ( DProblem problem )
		throws DataStoreException
	{
		/* put it into the problem DB */
		problemDB.set ( problem );
	}

	public void deleteProblem ( String username, String problemName )
		throws DataStoreException
	{
		throw new DataStoreException ( "NOT DONE" );
	}



	/* the user interfaces */
	public Collection getUserList ( )
		throws DataStoreException
	{
		return userDB.getList();
	}

	public DUser getUser ( String userName )
		throws UnknownUserException, DataStoreException
	{
		return userDB.get ( userName );
	}

	/**
	 * This will look up a user object by the assigned authkey. Authentication
	 * Exception will be thrown if the Authkey does not exist or has expired
	 */
	public DUser getUser ( Authkey authkey )
		throws UnknownAuthkeyException, UnknownUserException,
			   DataStoreException
	{
		return userDB.authkeyLookup ( authkey );
	}


	public void setUser ( DUser user )
		throws DataStoreException
	{
		userDB.set ( user );
	}

	public void removeUser ( String userName )
		throws DataStoreException, UnknownUserException
	{ 
		userDB.remove ( userName );
	}


	public DUser createUser ( String username,
							  String password,
							  String email )
		throws DuplicateUserException, DataStoreException
	{
		return userDB.createUser ( username, password, email );
	}


	public Authkey authUser ( String username, String password )
		throws UnknownUserException,
			   InvalidCredentialsException, DataStoreException
	{
		////IWPLog.debug(this,"Admin: "+sAdmin+":"+sAdminPassword);
		Authkey key = userDB.authUser ( username, password );
		
		IWPLog.info(this,"[DDataStore] Authenicated: "+username+":"+password+ "  authkey: " + key );
		return key;
 	}

	/**
	 * This will take in a valid or expired authkey and give you a 
	 * working authkey back 
	 * @author brockman 07.18.03
	 */
	public Authkey refreshAuthkey ( Authkey authkey )
		throws UnknownAuthkeyException, DataStoreException
	{
		return userDB.refreshAuthkey ( authkey );
	}

	//--------------------------------------------------------------------
	// Directory access

	public Collection getProblemDirectory ( String path )
		throws DataStoreException,InvalidPathException
	{
		return problemDB.getDirectory ( path );
	}

	public Collection getUserDirectory ( DUser user )
		throws DataStoreException,InvalidPathException
	{
		return problemDB.getUserDirectory ( user );
	}


	/*--------------------------------------------------------------------*/
	/* Private functions for getting data to/from disk */
	/*
	private void readUserDB () 
		throws DataStoreException
	{

		// with the advent of the SingleFileUserDB, this is no longer a 
		// nessecary step. brock 09.27.02

		try { 
			ObjectInput objIn = new ObjectInputStream ( new FileInputStream( userFile ));
			userDB = (DUserDB) objIn.readObject();
			objIn.close();

		} catch (ClassNotFoundException e) { 
			IWPLog.x(this, "Class Not Found", e );

		} catch  (IOException e ) {
			IWPLog.x(this, "Class Not Found", e );
			IWPLog.info( this, "[DDataStore] Creating a new memory object\n");
			userDB = new DMemoryUserDB ();
		}

		// create the administrator user
		userDB.setAdmin ( admin, adminPassword );
		
 	}



	private void writeUserDB () 
		throws DataStoreException
	{
		// with the advent of the single file userDB, this is no longer a 
		// nessecary step. brock 09.27.02

		try { 

			// remove the administrator user - don't want to save this 
			// guy to disk at all.
			userDB.remove ( admin );

			ObjectOutput objOut = new ObjectOutputStream ( new FileOutputStream( userFile ));
			objOut.writeObject( userDB );
			objOut.close();
		} catch  (IOException e ) {
			System.err.println ( e.getMessage() );
		}

	}

	*/

	/*
	private void readProblemMetaDB () 
	{

		try { 
			ObjectInput objIn = new ObjectInputStream ( new FileInputStream( problemMetaFile ));
			problemMetaDB = (DProblemMetaDB) objIn.readObject();
			objIn.close();

		} catch (ClassNotFoundException e) { 
			System.err.println ( e.getMessage () );
		} catch  (IOException e ) {

			System.err.println ( e.getMessage() );
			System.err.println ( "[DDataStore] Creating a new memory object\n");
			problemMetaDB = new DProblemMetaDB ();
		}
	}



	private void writeProblemMetaDB () 
	{
		try { 
			ObjectOutput objOut = new ObjectOutputStream ( new FileOutputStream( problemMetaFile ));
			objOut.writeObject( problemMetaDB );
			objOut.close();
		} catch  (IOException e ) {
			System.err.println ( e.getMessage() );
		}
	}

	*/


/*--------------------------------------------------------------------*/

}









