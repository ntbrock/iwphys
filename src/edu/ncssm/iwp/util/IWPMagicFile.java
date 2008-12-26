package edu.ncssm.iwp.util;

import java.io.*;

import edu.ncssm.iwp.exceptions.*;


/**
 * Magic file is a file that can either live in the filesystem under Eclipse,
 * or in the .jar file. This is used for loading directories, packaged problems,
 * and icons.
 * 
 * 2006-Aug-15 brockman
 * 
 * @author brockman
 *
 */

public class IWPMagicFile
{
	public static final String MAGIC_BASE_FILESYSTEM_PATH = "./etc";
	public static final String MAGIC_SEPARATOR = "/";
	
	public String resourceOrFilename;
	
	public IWPMagicFile ( String filename )
	{
		this.resourceOrFilename = filename;
	}
	
	
	public byte[] readBytes ()
		throws MagicFileNotFoundX
	{
		
		try { 
			IWPLog.debug(this, "Loading resourceOrFilename: " + resourceOrFilename );


			// Try to pull it in as a resource.
			InputStream is = getClass().getResourceAsStream(resourceOrFilename);
			BufferedInputStream bis = null;
			
			if ( is != null ) { 
				IWPLog.debug(this, "Resource Reading: " + resourceOrFilename );
				// Load the image as a .jar resource.
				bis = new BufferedInputStream(is);
				
	    	} else { 
	    		// Load the image as a file from the filesystem.
	    		// This is probably Eclipse.
	    				
	    		// UNIX Specific.
	    		String filename = MAGIC_BASE_FILESYSTEM_PATH + resourceOrFilename;
	    		
	    		// Always make sure we have a correct filepath.
	    		if ( ! resourceOrFilename.startsWith( MAGIC_SEPARATOR ) ) {
	    			filename = MAGIC_BASE_FILESYSTEM_PATH + MAGIC_SEPARATOR + resourceOrFilename;
	    		}
	    		
	    		
	    		IWPLog.debug(this, "Filesystem Reading: " + filename );
	    		
	    		FileInputStream fis = new FileInputStream(filename);
	    		bis = new BufferedInputStream(fis);
	    	}
	
			// but this way is neat.
			if ( bis != null ) { 
				byte[] readBytes = new byte[bis.available()];
				int byteRead = bis.read(readBytes,0,readBytes.length);
				if ( byteRead == 0 ) { 
					throw new MagicFileNotFoundX(resourceOrFilename + ": Read 0 bytes");
				}

				return readBytes;
			
			} else { 

				throw new MagicFileNotFoundX(resourceOrFilename+": Not found in resource or filesystem.");
				// ERROR: unable to find it in resource or filesystem.
			}
		}
			
	    catch ( IOException e ) { 
	    	throw new MagicFileNotFoundX(resourceOrFilename, e);
	    }
	    
	}
}
