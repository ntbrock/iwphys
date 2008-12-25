package edu.ncssm.iwp.problemdb;

import java.io.*;
import java.util.*;

public class Authkey implements Serializable
{
	public static final int AUTHKEY_LIFETIME = 3600;
	public static final int AUTHKEY_LENGTH = 16;

	private String authkey = null;
	private Date issueDate = null;
	private String username = null;

	public Authkey ( String username ) 
	{
		authkey = generateRandom ( );
		issueDate = new Date ( );
		this.username = username;
	}

	public Authkey ( boolean construct, String authkey )
	{
		this.authkey = authkey;
		issueDate = new Date ( );
		this.username = "anonymous";
	}

	public static String generateRandom ( )
	{
		StringBuffer out = new StringBuffer("");

		for ( int i = 0; i < AUTHKEY_LENGTH; i++ ) { 
			// ascii 48 -> 90
			out.append ( (char) ( (int)( Math.random ( ) * 42 ) + 48 ) ); 
		}
		
		return out.toString ( );
	}
	


	public String getUsername ( ) 
	{
		return username;
	}

	public String getKey ( ) 
	{
		return authkey;
	}

	public int getAge ( )
	{
		return (int) ( (new Date ().getTime()) - issueDate.getTime() );
	}

	public String toString ( ) 
	{
		return getKey ( );
	}


	public boolean isExpired ( ) 
	{
		Date now = new Date ( );
		long cutoff = issueDate.getTime() + AUTHKEY_LIFETIME;

		if ( now.getTime() < cutoff ) { 
			return false;
		} else { 
			return true;
		}
	}
}
