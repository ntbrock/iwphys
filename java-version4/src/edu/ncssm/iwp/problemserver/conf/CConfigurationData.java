package edu.ncssm.iwp.problemserver.conf;

import java.util.*;
import java.io.*;

import edu.ncssm.iwp.exceptions.*;

// note: this should be converted over to use an XML file when I have the
// chance. brock 06.04.02

public class CConfigurationData
{

	Hashtable oVariableHash = new Hashtable();


	public CConfigurationData ( String isFile )
		throws IOException
	{
		read ( isFile );
	}


	/* read elements from the file + put them into the hash */
	private void read ( String isFile )
		throws IOException
	{

		BufferedReader oReader = new BufferedReader ( new FileReader ( isFile ));
		String sLine;

		while ( (sLine = oReader.readLine()) != null ) {

			/* //IWPLog.debug(this,"ReadLine> "+sLine ); */

			/* if it's a blank, or # commented line, ignore */
			if ( (sLine.length() < 1) ||
				 (sLine.charAt ( 0 ) == '#') ||
				 (sLine.charAt ( 0 ) == '/') ||
				 (sLine.charAt ( 0 ) == ' ') ) {
				
				/* it's a comment line ! */
				
			} else {
				
				/* it's a line with variables */
				StringTokenizer oStringTok = new StringTokenizer ( sLine, " :");
				String sKey, sVal;
				
				/* get the key */
				if ( oStringTok.hasMoreTokens() ) {
					sKey = oStringTok.nextToken();
					
					if ( oStringTok.hasMoreTokens() ) {
						sVal = oStringTok.nextToken();
						
						/* //IWPLog.debug(this,"Added "+sKey+", "+sVal ); */
						oVariableHash.put ( sKey, sVal );
					}
				}
				
			}
			
		}
		
	}




	public String get ( String isKey )
		throws UnknownConfigurationFieldException
	{
		if ( oVariableHash.get ( isKey ) == null ) { 
			throw new UnknownConfigurationFieldException ( isKey );
		} else { 
			return (String) oVariableHash.get ( isKey );
		}
	}
}








