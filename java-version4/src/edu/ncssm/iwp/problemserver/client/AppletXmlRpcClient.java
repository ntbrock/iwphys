package edu.ncssm.iwp.problemserver.client;

import java.util.*;
import java.io.*;
import java.net.*;

import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.exceptions.*;


import org.apache.xmlrpc.*;
// import org.apache.xmlrpc.applet.*;
import com.oreilly.servlet.*;

import edu.ncssm.iwp.util.*;

public class AppletXmlRpcClient implements AppletXmlRpcConstants
{
	
	
	
	// public static final boolean USE_LIGHT_IMPL = true;
	

	String username;
	String password;
	String authkey;
	DUser user;
	// Application Mode
	XmlRpcClient client = null;
	// Applet Mode
	// SimpleXmlRpcClient client = null;

	public AppletXmlRpcClient ( String xmlRpcUrl,
								String username,
								String password )
		throws MalformedURLException, AuthenticationException, ProblemServerRemoteException, IOException
	{
		this.username = username;
		this.password = password;

		// regular call - has problems in applet
		client = new XmlRpcClient ( xmlRpcUrl );

		// driver-set call.
		/*
		try { 
			XmlRpc.setDriver ( "org.BORK" );
			client = new XmlRpcClient ( xmlRpcUrl );
		} catch ( ClassNotFoundException e ) { 
			IWPLog.info(this,"[AppletXmlRpcClient] ERROR: SAX Driver not found: " + e.getMessage() );
			throw new IOException ( "ClassNotFound: " + e.getMessage() );
		}
		*/

		this.user = authenticate ( username, password );
	}

	/**
	 * Get a collection of strings back that are names of files on the 
	 * remote system
	 */
	public Collection listFiles ( String directory )
		throws ProblemServerRemoteException, IOException, UnknownDirectoryException
	{
		try { 
			Hashtable request = new Hashtable ( );
			request.put (ATTR_AUTHKEY, this.authkey );
			request.put (ATTR_DIRECTORY, directory );
			
			IWPLog.info(this,"[AppletXmlRpcClient.listFiles] using authkey: " + this.authkey );
			
			Hashtable result = (Hashtable) client.execute ( ACTION_PREFIX + "." + ACTION_LIST_FILES,
															vectorize ( request ) );
			IWPLog.info(this,"[listFiles] resultHash: " + result );
			
			digestNewAuthkey ( result );
			
			Vector files = (Vector) result.get ("files");
			// vectors implement collection
			return files;

		} catch ( XmlRpcException e ) {
			throw new ProblemServerRemoteException ( e ,"There was a problem getting a list of files");
		}
	}

	/**
	 * Get a collection of sub-directory names from teh remote system,
	 * given a directory name
	 */
	public Collection listDirectories ( String directory )
		throws ProblemServerRemoteException, IOException, UnknownDirectoryException
	{
		try { 

			Hashtable request = new Hashtable ( );
			request.put ( ATTR_AUTHKEY, this.authkey );
			request.put ( ATTR_DIRECTORY, directory );
			
			Hashtable result =
				(Hashtable) client.execute ( ACTION_PREFIX + "." + ACTION_LIST_DIRECTORIES,
											 vectorize ( request ) );
			
			digestNewAuthkey ( result );
			
			Vector dirs = (Vector) result.get ( ATTR_DIRECTORIES );
			// vectors implement collection
			return dirs;

		} catch ( XmlRpcException e ) {
			throw new ProblemServerRemoteException ( e ,"There was a problem listing directories");
		}

	}



	/**
	 * Get a collection of higher-order data objects that represent the
	 * items on the remote filesystem, given a directory name
	 */
	public Collection listContent ( String directory )
		throws ProblemServerRemoteException, IOException,
			   UnknownDirectoryException
	{
		try { 

			Hashtable request = new Hashtable ( );
			request.put ( ATTR_AUTHKEY, this.authkey );
			request.put ( ATTR_DIRECTORY, directory );
			
			Hashtable result =
				(Hashtable) client.execute ( ACTION_PREFIX + "." + ACTION_LIST_CONTENT,
											 vectorize ( request ) );
			
			digestNewAuthkey ( result );
			
			Vector dirs = (Vector) result.get ( ATTR_DIRECTORIES );
			// vectors implement collection
			return dirs;

		} catch ( XmlRpcException e ) {
			throw new ProblemServerRemoteException ( e ,"There was a problem listing contents");
		}

	}





	/**
	 * Store the full contents of a problem file to the server
	 */
	public void putFile ( DProblem problem )
		throws ProblemServerRemoteException, IOException, XMLParserException
	{
		try {

			Hashtable request = new Hashtable ( );
			request.put ( ATTR_AUTHKEY, this.authkey );
		
			// MIME-encode the content of the problem.
			String stringContents = DProblemXMLParser.save ( problem );
			String base64Contents = Base64Encoder.encode ( stringContents );

			request.put ( "data", base64Contents );
			request.put ( "filename", problem.getFilename() );
		
			Hashtable result =
				(Hashtable) client.execute ( ACTION_PREFIX + "." + ACTION_PUT_FILE,
											 vectorize ( request ) );
			
			digestNewAuthkey ( result );

			// if there is no exception here, we're good.
			
		} catch ( XmlRpcException e ) {
			throw new ProblemServerRemoteException ( e ,"Problem saving file... Perhaps you don't have permissions here");
		}
	}



	/**
	 * Retrieve the full contents of a problem file from the 
	 * server.
	 */

	public DProblem getFile ( String filename )
		throws ProblemServerRemoteException, IOException, UnknownFileException, XMLParserException
	{
		try { 
			Hashtable request = new Hashtable ( );
			request.put ( ATTR_AUTHKEY, this.authkey );
			request.put ( ATTR_FILENAME, filename );
			
			Hashtable result =
				(Hashtable) client.execute ( ACTION_PREFIX + "." + ACTION_GET_FILE,
											 vectorize ( request ) );
			
			digestNewAuthkey ( result );
			
			// TODO: Read in the file data, de-MIME it.
			String base64Contents = (String) result.get ( ATTR_DATA );
			// this prints a lot of data. brockman 03.19.04
			// IWPLog.info(this,"[AppletXmlRpcClient.getFile] data = '" + base64Contents + "'" );
				
			if ( base64Contents == null ) { 
				throw new ProblemServerRemoteException ("data was null");
			}

			String stringContents = Base64Decoder.decode ( base64Contents );
		
			DProblem problem = DProblemXMLParser.load ( stringContents );
			problem.setFilename ( filename );

			return problem;
			
		} catch ( XmlRpcException e ) {
			throw new ProblemServerRemoteException ( e ,"There was a problem loading the file... try again.");
		}
		
	}


	/**
	 * Delete a file from the remote server
	 * @author brockman 08.08.03
	 */

	public void deleteFile ( String filename )
		throws ProblemServerRemoteException, IOException, UnknownFileException, XMLParserException
	{
		try { 
			Hashtable request = new Hashtable ( );
			request.put ( ATTR_AUTHKEY, this.authkey );
			request.put ( ATTR_FILENAME, filename );
			
			Hashtable result =
				(Hashtable) client.execute ( ACTION_PREFIX + "." + ACTION_DELETE_FILE,
											 vectorize ( request ) );
			
			digestNewAuthkey ( result );
			
			// ok
			return;

		} catch ( XmlRpcException e ) {
			throw new ProblemServerRemoteException ( e ,"Could not delete the file... Do you have permissions here?");
		}
	
	}

	/**
	 * Register a new authentication token by supplying username and
	 * password credentials.
	 */
	public DUser authenticate ( String username, String password )
		throws AuthenticationException, ProblemServerRemoteException, IOException
	{
		try {
		   
			Hashtable request = new Hashtable ( );
			request.put ( "username", username );
			request.put ( "password", password );

			Hashtable result = (Hashtable) client.execute ( ACTION_PREFIX + "." + ACTION_AUTHENTICATE, vectorize ( request ) );
			IWPLog.info(this,"[authenticate] resultHash: " + result );

			digestNewAuthkey ( result );
			
			// give back a user record.
			DUser user = new DUser();
			user.setUsername ( username );
			return user;

		} catch ( XmlRpcException e ) {
		    throw new AuthenticationException ( e );
		}
	}

	/**
	 * This shuld be called with every returned method. The server has
	 * the ability to give out new authkeys with each response. We need
	 * to take note of those.
	 */

	private void digestNewAuthkey ( Hashtable result )
	{
		if ( result.get(ATTR_AUTHKEY) != null ) {
			this.authkey = (String) result.get(ATTR_AUTHKEY);
		}
	}


	/**
	 * Toolkit method
	 */
	/*
	  // we use xmlrpc error messages now instead of encoded returncodes.
	  // brockman 03.19.04
	private int returnCode ( Hashtable result )
	{
		try {
			return ((Integer) result.get ( "return_code" ) ).intValue();
		} catch ( NullPointerException e ) {
			return -2;
		}
	}
	*/
	private Vector vectorize ( Hashtable hash )
	{
		Vector out = new Vector ( 1 );
		out.add ( hash );
		return out;
	}

	public void close ( )
	{
		// those is no XmlRpc close call?
		// client.close();
	}

}


