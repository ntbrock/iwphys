/**
 * 2008-Dec-25 enhancing the Bitmap selector to read dynamically from the .jar /images/bitmap/ directory
 * brockman
 * 
 * This utility class helps manage the file list of Bitmaps.
 */

package edu.ncssm.iwp.graphicsengine;

import edu.ncssm.iwp.util.IWPMagicFile;
import edu.ncssm.iwp.exceptions.MagicFileNotFoundX;


public class GShape_Bitmap_fileList
{
	public static final String BITMAP_PATH = "/images/bitmap/";
	public static final String BITMAP_LIST = "/images/bitmap/list.txt";
	
	/**
	 * Look into the filesystem or the jar path and get the list of available bitmaps
	 * 
	 * Used by GShape_Bitmap_Designer
	 */
	
	static public String[] readBitmapFileList()
	{
		//System.err.println("[GShape_Bitmap_fileList]  readBitmapFileList invoked");
		
		try {
			IWPMagicFile magicFile = new IWPMagicFile(BITMAP_LIST);
		
			String fileContents = new String ( magicFile.readBytes() );
			String names[] = fileContents.split("\\n");
			
			/*			
				String names[] = { "car_final",
	                "ncssmlogo",
	                "pullhand",
	                "pushhand",
	                "weight",
	                "wagon",
	                "rope",
	                "disc",
	                "speaker",
	                "earth",
	                "mars",
	                "moon",
	                "sun" };

*/
			return names;		
		
		} catch ( MagicFileNotFoundX x ) { 
			
			System.err.println("[GShape_Bitmap_fileList] Error: Unable to read file: " + BITMAP_LIST);
			String names[] = { "Error: unable to read: " + BITMAP_LIST };
			return names;
		}
		
		
	}
	

	public static String stripPathAndExtension(String filename)
	{
		String out = filename;
		
		int idx = -1;
		if ( (idx = out.lastIndexOf("/")) >= 0) { 
			out = out.substring(idx+1);
		}
		
		if ( (idx = out.lastIndexOf("\\")) >= 0) { 
			out = out.substring(idx+1);
		}
		
		if ( (idx = out.lastIndexOf(".")) >= 0) { 
			out = out.substring(0, idx);
		}
		
		return out;
	}


	static public String translateFilenameIntoFullPath( String filename )
	{
		// It's important to keep the png off of the filenames due to the xml loader.
		return BITMAP_PATH + filename + ".png";
//		return BITMAP_PATH + filename;

	}

}
