package edu.ncssm.iwp.util;

public class IWPFileLocations
{

	// The / here is relative to the top of the jar, as these can be 
	// resources as well. The MagicFile code takes care of sensing
	// if this is a windows or linux eclipse environment and switches
	// the slashes accordingly.
	
	// Used by GAboutDialogue
	public static final String HELP_MAGIC = "/doc/help.txt";
	public static final String GPL_MAGIC = "/doc/GPL.txt";

	// Used by GFuncRefernece
	public static final String FUNC_REFERENCE = "/doc/funcReference.txt";

	// GWindow_PackagedProblemBrowser to read the packaged problems
	public static final String PACKAGED_DIRECTORY_MAGIC = "/packagedProblems/directory.xml";
	
}
