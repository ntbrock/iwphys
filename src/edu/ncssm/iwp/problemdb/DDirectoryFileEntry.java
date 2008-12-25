package edu.ncssm.iwp.problemdb;


public class DDirectoryFileEntry extends DDirectoryEntry
{
	String username;

	public DDirectoryFileEntry ( String path, String filename,
								 String username )
	{
		super ( path, filename, FILE );
		this.username = username;

	}


	public String getUsername ( ) { return username; }
	public void setUsername ( String username )
	{
		this.username = username;
	}

}
