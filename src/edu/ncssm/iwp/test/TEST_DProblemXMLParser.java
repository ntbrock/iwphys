
package edu.ncssm.iwp.test;

import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.exceptions.*;

import edu.ncssm.iwp.util.*;

public class TEST_DProblemXMLParser
{

	public static void main ( String args[] )
	{
		if ( args.length == 0 ) { 
			IWPLog.out("Usage: TEST.TEST_DProblemXMLParser [problem file]");
			return;
		}

		try { 

			IWPLog.out("------------------------------------------------");
			IWPLog.out(" xml -> Problem");
			IWPLog.out("------------------------------------------------");
			
			DProblemManager pm = new DProblemManager ();
			
			DProblem prob = pm.loadFile ( args[0] );
			
			if ( prob == null ) { 
				IWPLog.error("Error: unable to load: " + args[0] );
				return;
			}
			
			IWPLog.out("------------------------------------------------");
			IWPLog.out(" Problem -> xml");
			IWPLog.out("------------------------------------------------");
			
			
			
			String xmlData;
			
			xmlData = DProblemXMLParser.save ( prob );
			System.out.println("XML Stream:");
			System.out.println(xmlData);
			
			/*
			  DProblem newProb = DProblemXMLParser.load ( xmlData );
			  System.out.println("Reloaded Problem:");
			  System.out.println( newProb );
			*/
		} catch ( DataStoreException e ) { 
			IWPLog.x("DataStoreException: " + e.getMessage ( ), e );
			e.printStackTrace ( );
		} catch ( XMLParserException e ) { 
			IWPLog.x("XMLParserException: " + e.getMessage ( ), e );
			e.printStackTrace ( );
		}

	}

		
}

