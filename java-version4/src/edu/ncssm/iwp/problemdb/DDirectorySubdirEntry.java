package edu.ncssm.iwp.problemdb;


public class DDirectorySubdirEntry extends DDirectoryEntry
{
	String name;

	public DDirectorySubdirEntry ( String path, String filename )
	{
		super ( path, filename, DIRECTORY );
	}

}
