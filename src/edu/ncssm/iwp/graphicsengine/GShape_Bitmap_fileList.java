/**
 * 2008-Dec-25 enhancing the Bitmap selector to read dynamically from the .jar /images/bitmap/ directory
 * brockman
 * 
 * This utility class helps manage the file list of Bitmaps.
 */

package edu.ncssm.iwp.graphicsengine;

public class GShape_Bitmap_fileList
{
	public static final String BITMAP_PATH = "/images/bitmap/";
	
	/**
	 * Look into the filesystem or the jar path and get the list of available bitmaps
	 * 
	 * Used by GShape_Bitmap_Designer
	 */
	
	static public String[] readBitmapFileList()
	{
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

		

		
		return names;
	}
	

	static public String translateFilenameIntoFullPath( String filename )
	{
		return BITMAP_PATH + filename + ".png";
	}

}
