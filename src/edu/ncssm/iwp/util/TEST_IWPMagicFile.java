package edu.ncssm.iwp.util;

import edu.ncssm.iwp.exceptions.*;

public class TEST_IWPMagicFile
{

	public static void main ( String args[] )
	{
		
		
		if ( args.length < 1 ) { 
			System.err.println("Usage: TEST_IWPMagicFile [fileOrResource]");
			System.exit(1);
		}
		
		
		try {
		
			IWPMagicFile file = new IWPMagicFile ( args[0] );
			
			String contents = new String(file.readBytes());
			
			System.out.println(contents);
			
		} catch ( MagicFileNotFoundX e ) { 
			IWPLog.x("TEST_IWPMagicFile", e);
		}
		
		
	}
	
}
