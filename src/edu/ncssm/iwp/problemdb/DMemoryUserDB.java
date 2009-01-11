package edu.ncssm.iwp.problemdb;

import java.io.*;
import java.util.*;

import edu.ncssm.iwp.exceptions.*;

import edu.ncssm.iwp.util.*;


public class DMemoryUserDB implements DUserDB, Serializable
{

	private static final long serialVersionUID = 1L;
	Hashtable userHash;    /* where all the user info is kept in RAM */
	String storageFile;

	String adminUsername = null;
	String adminPassword = null;

	DMemoryAuthkeyBank authBank;
	
	/*-----------------------------------------------------------------*/

	public DMemoryUserDB ( )
	{
		userHash = new Hashtable ( );
		authBank = new DMemoryAuthkeyBank ( );
	}

	public DMemoryUserDB ( String userFile )
	{
		IWPLog.info(this,"[DMemoryUserDB] storageFile: " + storageFile );
	}

	/*-----------------------------------------------------------------*/

	/* add a user / set user info into the database */
	public void set ( DUser user )
	{
		/* keyed by username */
		userHash.put ( user.getUsername(), user );
	}

	/* get a user out of the database */
	public DUser get ( String username )
		throws UnknownUserException
	{
		DUser user = (DUser) userHash.get ( username );
		if ( user == null ) {
			throw new UnknownUserException ( );
		} else {
			return user;
		}

	}

	public DUser authkeyLookup ( Authkey authkey )
		throws UnknownAuthkeyException, UnknownUserException
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


	public void remove ( String username )
	{
		userHash.remove ( username );
	}


	public DUser createUser ( String username,
							  String password,
							  String email )
		throws DuplicateUserException
	{
		IWPLog.info(this,"[DMemoryUserDB.createUser] username: " + username );

		// see if this user exists already
		try { 
			DUser user = get ( username );
			if ( user == null ) { } // make the warning go away.
			throw new DuplicateUserException ( username );
		} catch ( UnknownUserException e ) { }
		
		DUser user = new DUser ( username, password, email );
		set ( user );

		return user;
	}



	/* get a list of the Users */
	public Collection getList ( )
	{
		ArrayList userList = new ArrayList ( );
		Enumeration oKeys = userHash.keys();

		while ( oKeys.hasMoreElements() ) {
			Object oKey = oKeys.nextElement();
	  
			/* System.out.println("Adding user: "+(String)oKey); */
			userList.add ( userHash.get ( oKey ));
		}

		return userList;
	}

	// nothing to do here.
	public void close ( ) { }


	/*-----------------------------------------------------------------*/

	public Authkey authUser ( String username, String password )
		throws InvalidCredentialsException, UnknownUserException
	{
		/* look up the username */
		/*		System.out.println("[DMemoryUserDB] Authing "+username); */
		DUser user = (DUser) ( userHash.get ( username ) );

		if ( user == null ) {
			throw new UnknownUserException ("user not known: "+ username );
		}

		if ( ! user.getPassword().equals(password) ) {
			throw new InvalidCredentialsException ("Invalid Password");
		}

		// keep a record of authkey -> username mappings.
		return authBank.create ( user.getUsername ( ) );
	}



	public void setAdmin ( String adminUsername,
						   String adminPassword )
	{
		IWPLog.info(this,"[UserDB.setAdmin] adding: " + adminUsername + " : " + adminPassword );

		DUser adminUser = new DUser ( adminUsername,
									  adminPassword,
									  "admin@iwp.sourceforge.net" );
		adminUser.setAdminFlag ( true );

		userHash.put ( adminUsername, adminUser );
	}


}








