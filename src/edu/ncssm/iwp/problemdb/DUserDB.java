
package edu.ncssm.iwp.problemdb;

import java.util.*;

import edu.ncssm.iwp.exceptions.*;


public interface DUserDB
{

	/*-----------------------------------------------------------------*/

	/* add a user / set user info into the database */
	public void set ( DUser user )
		throws DataStoreException;

	/* get a user out of the database */
	public DUser get ( String username )
		throws UnknownUserException, DataStoreException;

	public DUser authkeyLookup ( Authkey authkey )
		throws UnknownAuthkeyException, UnknownUserException, DataStoreException;

	public Authkey refreshAuthkey ( Authkey authkey )
		throws UnknownAuthkeyException, DataStoreException;

	public void remove ( String username )
		throws DataStoreException, UnknownUserException;

	public DUser createUser ( String username,
							  String password,
							  String email )
		throws DuplicateUserException, DataStoreException;

	/* get a list of the Users */
	public Collection getList ( )
		throws DataStoreException;

	public void close ( )
		throws DataStoreException;

	/*-----------------------------------------------------------------*/

	public Authkey authUser ( String username, String password )
		throws UnknownUserException,
			   InvalidCredentialsException, DataStoreException;

	public void setAdmin ( String adminUsername,
						   String adminPassword )
		throws DataStoreException;

}

