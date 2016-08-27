package edu.ncssm.iwp.problemdb.directory;

import edu.ncssm.iwp.objects.directory.*;
import edu.ncssm.iwp.exceptions.*;
import java.io.*;
import java.util.*;

public class TEST_DDirectoryManager
{
	
	public static void main ( String args[] )
	{
		
		if ( args.length < 1 ) { 
			System.err.println("Usage: TEST_DDirectoryManager [filename]");
			System.exit(1);
		}
		
		try { 
		
			DDirectoryManager manager = new DDirectoryManager();
		
			DDirectory directory = manager.loadFile( args[0] );
			
			System.out.println("[Directory Successfully Read]: " + directory.toString() );

			System.out.println("directory.title: " + directory.getTitle() );
			for ( Iterator i = directory.getCategories().iterator(); i.hasNext(); ) { 
				
				DCategory category = (DCategory) i.next();
				
				System.out.println("directory.category.name: " + category.getName() );
				
				for ( Iterator j = category.getProblemLinks().iterator(); j.hasNext(); ) {
					
					DProblemLink link = (DProblemLink) j.next();
					
					System.out.println("directory.category.problemLinks.filename: " + link.getFilename() );
					System.out.println("directory.category.problemLinks.summary : " + link.getSummary() );
				}
				
			}
			
		} catch ( FileNotFoundException e ) { 
			System.err.println("FileNotFound: " + e );
			e.printStackTrace();
		} catch ( XMLParserException e ) { 
			System.err.println("XMLParserException: " + e );
			e.printStackTrace();
		}

	}

}
