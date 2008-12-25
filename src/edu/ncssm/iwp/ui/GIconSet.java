// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.ui;

import edu.ncssm.iwp.plugin.*;

import java.util.*;
import java.awt.*;
import javax.swing.*;

import edu.ncssm.iwp.util.*;
import edu.ncssm.iwp.exceptions.*;


/* This should really be called the ObjectIconSet */

public class GIconSet
{
	public static String ICON_PATH = "/images/";
	public static String DEFAULT_ICON = "icon_DObject_Unknown.gif";
	
	Hashtable iconCache = new Hashtable();
    MediaTracker m = new MediaTracker(new JPanel());

	public GIconSet()
	{
	}

	
	public ImageIcon getObjectIcon ( IWPObject iObject )
	{
		if ( iObject instanceof IWPDesigned ) { 
			return cacheLoad( ((IWPDesigned)iObject).getIconFilename() );
		} else {
			return cacheLoad( DEFAULT_ICON );
		}
	}
	
	
	
	
	/**
	 * Load the image from a resource file, or from the filesystem. 
	 * This has to be generic enough to work in the Jar or in the eclipse
	 * build tree.
	 * 
	 * Should make this even more generic to handle the demo files, etc.
	 * TODO: Replace this with edu.ncssm.iwp.util.IWPMagicFile
	 * 
	 * 2006-Aug-14 brockman
	 * 
	 * @param source
	 * @return
	 * @throws CannotLoadIconX
	 */
	
	
    private Image loadImage(String source)
    	throws CannotLoadIconX
    {
    
    	try { 
    		IWPLog.debug(this, "Loading image: "+source);

    		IWPMagicFile file = new IWPMagicFile(source);
    		
    		Image img = Toolkit.getDefaultToolkit().createImage(file.readBytes());
    		m.addImage(img,0);
    		m.waitForAll();
    		
    		return img;
    	}
    	catch ( MagicFileNotFoundX e ) { 
    		throw new CannotLoadIconX(source, e);
    	}
    	catch ( InterruptedException e ) { 
    		throw new CannotLoadIconX(source, e);
    	}
    }
    

    
	
	private ImageIcon cacheLoad ( String iconPath )
	{
		// Check cache
		if ( iconCache.get(iconPath) != null ) {
			return (ImageIcon) iconCache.get(iconPath);
		}
		
		// Load from disk into cache.
		try { 
			iconCache.put(iconPath, new ImageIcon(loadImage( ICON_PATH + iconPath )) );
		} catch ( CannotLoadIconX x ) { 
			IWPLog.warn( this, "Unable to load Icon: " + iconPath + ": " + x.getMessage() );
			
			try { 
				iconCache.put(iconPath, new ImageIcon(loadImage( ICON_PATH + DEFAULT_ICON )) );
			} catch ( CannotLoadIconX x2 ) {
				IWPLog.error( this, "Unable to load DEFAULT Icon: " + iconPath + ": " + x.getMessage() );
			}
		}
		
		// return new cache entry.
		// Could be null here if I can't load the default icon.
		return (ImageIcon) iconCache.get(iconPath);
	}
	
    
}

