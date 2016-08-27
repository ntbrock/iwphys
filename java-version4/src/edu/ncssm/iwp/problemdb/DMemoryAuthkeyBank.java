package edu.ncssm.iwp.problemdb;

import java.util.*;


import edu.ncssm.iwp.exceptions.*;


public class DMemoryAuthkeyBank
{
	private Hashtable keys = new Hashtable ( );


	public DMemoryAuthkeyBank ( ) { }


	public Authkey create ( String username )
	{
		// keep a record of authkey -> ref.key mappings.
		// (ref key is the username, usually)
		Authkey key = new Authkey ( username );
		keys.put ( key.getKey(), key );
	
		return key;
	}

	public void remove ( Authkey key )
	{
		keys.remove ( key.getKey () );
	}


	/**
	 * This method will just look up a stored Authkey and return it
	 * in-tact
	 * @author brockman 07.18.03
	 */

	public Authkey lookup ( Authkey authkey )
		throws UnknownAuthkeyException
	{
		Authkey trueKey = (Authkey) keys.get ( authkey.getKey() );
		if ( trueKey == null ) { 
			throw new UnknownAuthkeyException ( authkey.getKey ( ) );
		}
		
		return trueKey;
	}

	/**
	 * If you feed this emthod an authkey, and it's is expired, it
	 * will issue you a new one. If it's still valid, then it will
	 * return you the same one. Vital Method
	 * @author brockman 07.17.03
	 */
	public Authkey refresh ( Authkey authkey )
		throws UnknownAuthkeyException
	{
		Authkey trueKey = lookup ( authkey );

		// See if the key has expired. If so, then issue a new and
		// and delete the expired one.

		if ( trueKey.isExpired ( ) ) { 
			// remove myself
			remove ( trueKey );

			// create a new one
			trueKey = create ( trueKey.getUsername ( ) );
		} 

		return trueKey;
	}

}
