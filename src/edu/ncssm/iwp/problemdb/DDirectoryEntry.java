package edu.ncssm.iwp.problemdb;

public class DDirectoryEntry
{
	public static final int FILE = 1;
	public static final int DIRECTORY = 2;

	String filename = null;
	String path = null;
	int type = 0;

	public DDirectoryEntry ( String path, String filename, int type )
	{
		this.path = path;
		this.filename = filename;
		this.type = type;
	}


	public String getFilename ( ) { return filename; }
	public void setFilename ( String filename )
	{
		this.filename = filename;
	}

	public String getPath ( ) { return path; }
	public void setPath ( String path )
	{
		this.path = path;
	}

	public int getType ( ) { return type; }
	public void setType ( int type )
	{
		this.type = type;
	}

	public String getFullFilename ( )
	{
		// bad magic /
		return getPath() + "/" + getFilename();
	}

	public boolean getIsFile ( ) 
	{
		return this.type == FILE ? true : false;
	}

	public boolean getIsDirectory ( ) 
	{
		return this.type == DIRECTORY ? true : false;
	}


}
