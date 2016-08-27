package edu.ncssm.iwp.problemserver.client;

import java.util.*;
import java.io.*;

import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.exceptions.*;

import edu.ncssm.iwp.util.*;


public class ConsoleXmlRpcClient
{
	public static void main ( String args[] )
	{
		if ( args.length < 3 ) {
			IWPLog.out("Usage: AppletXmlRpcClient [host] [username] [password]");
			System.exit(-1);
		}


		try {
			AppletXmlRpcClient conn = new AppletXmlRpcClient ( args[0],
															   args[1],
															   args[2] );
		
			BufferedReader in = new BufferedReader ( new InputStreamReader ( System.in ) );
			String line = null;

			System.out.print("> "); System.out.flush();

			while ( ( line = in.readLine () ) != null ) {

				if ( line.length() == 0 ||
					 line.startsWith("#") ) { continue; }

				try {
					CommandSet command = new CommandSet ( line );
					if ( command.equals ("listFiles") ) {
						Collection files = conn.listFiles ( command.arg(0) );
						for ( Iterator i = files.iterator(); i.hasNext(); ) {
							System.out.println("" + i.next() );
						}
						
					} else if ( command.equals ("listDirectories") ) {
						conn.listDirectories ( command.arg(0) );
						
					} else if ( command.equals ("putFile") ) {
						// read the file in. create a problem out of it.
						
						DProblem problem = readProblemFile ( command.arg(0) );

						IWPLog.info("[ConsoleXmlRpcClient] uploading problem.filename: " + problem.getFilename () );
						conn.putFile ( problem );
					} else if ( command.equals ("getFile") ) {
						DProblem problem = conn.getFile ( command.arg(0) );
						System.out.println("Got file: " + problem );
						
					} else if ( command.equals ( "quit") ) {
						System.out.println("");
						System.exit(0);

					} else {
						System.out.println("Unknown command: " + command.getCommand() );
					}
					
				}			
				catch ( Exception e ) {
					IWPLog.x("Exception: " + e.getMessage(),e );
					e.printStackTrace();
				}

				System.out.print("> "); System.out.flush();
			}
		}
		catch ( Exception e ) {
			IWPLog.x("Exception: " + e.getMessage(),e );
			e.printStackTrace();
		}

		System.out.println("");
		System.exit(0);
		
	}

	
	/**
	 * used by main
	 */
	private static DProblem readProblemFile ( String filename )
		throws DataStoreException
	{
		DProblemManager_File fileMan = new DProblemManager_File ( "." ); //cwd
		DProblem problem = fileMan.loadProblem ( filename );

		return problem;
	}
}



class CommandSet
{
	public String command;
	public Vector parts;

	public CommandSet ( String line )
	{
		parts = new Vector();

		StringTokenizer st = new StringTokenizer ( line );
		while ( st.hasMoreTokens ( ) ) {
			parts.add ( st.nextToken() );
		}
			
		command = (String) parts.remove ( 0 );
	}

	public String arg ( int index )
		throws NoArgException
	{
		if ( parts.size() <= index ||
			 parts.elementAt (index) == null ) {
			throw new NoArgException ();
		} else {
			return (String) parts.elementAt(index);
		}
	}

	public boolean equals ( String match )
	{
		if ( command.equalsIgnoreCase ( match ) ) {
			return true;
		} else {
			return false;
		}
	}

	public String getCommand ( ) { return command; }
	   
}


class NoArgException extends Exception
{
	private static final long serialVersionUID = 1L;
	public String getMessage ( )
	{
		return "Missing Arg";
	}
}
