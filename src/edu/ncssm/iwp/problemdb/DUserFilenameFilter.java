package edu.ncssm.iwp.problemdb;

import java.io.*;

public class DUserFilenameFilter implements FilenameFilter
{

	public boolean accept ( File dir,
							String name )
	{
		if ( name.endsWith (DSingleFileUserDB.USER_FILE_EXTENSION) ) { 
			return true;
		} else { 
			return false;
		}

	}

}
