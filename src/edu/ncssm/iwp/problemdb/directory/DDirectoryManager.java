package edu.ncssm.iwp.problemdb.directory;

import edu.ncssm.iwp.exceptions.*;
import edu.ncssm.iwp.objects.directory.*;

import java.io.*;
import edu.ncssm.iwp.util.*;

/**
 * Handles the loading and saving of Directories. These are a simple concept:
 * collection of problems organized into categories w/ summaries and links to 
 * prodblem files. Initially created for the PackagedProblems.
 * 
 * 2006-Aug-14 brockman
 * 
 * @author brockman
 *
 */

public class DDirectoryManager
{

	public DDirectory loadFile ( String filename )
		throws FileNotFoundException, XMLParserException
	{
		FileInputStream fsi = new FileInputStream(filename);
		return DDirectoryXMLParser.load ( fsi );
	}
	
	/**
	 * Load a file from the filesystem or jar, whether the system
	 * is linux or windows.
	 * 
	 * @param magicName
	 * @return
	 * @throws MagicFileNotFoundX
	 * @throws XMLParserException
	 */
	
	public DDirectory loadMagic ( String magicName )
		throws MagicFileNotFoundX, XMLParserException
	{
		IWPMagicFile magic = new IWPMagicFile ( magicName );
		
		ByteArrayInputStream bsi = new ByteArrayInputStream(magic.readBytes());
		
		return DDirectoryXMLParser.load ( bsi );
	}
	
}
