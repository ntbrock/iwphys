package edu.ncssm.iwp.util;

import javax.swing.*;

import java.awt.*;

import edu.ncssm.iwp.util.IWPMagicFile;
import edu.ncssm.iwp.exceptions.CannotLoadIconX;
import edu.ncssm.iwp.exceptions.MagicFileNotFoundX;


// TODO refactor: mod the GShape_Bitmap Designer to use this class

public class MagicImageIconLoader
{
	
	//Temporary Try loading 
    public static ImageIcon loadIcon( String source )
		throws CannotLoadIconX
	{

		try { 
			IWPMagicFile file = new IWPMagicFile(source);
    		MediaTracker m = new MediaTracker(new JPanel());
			Image img = Toolkit.getDefaultToolkit().createImage(file.readBytes());
			
			m.addImage(img,0);
			m.waitForAll();

			return new ImageIcon(img);
		}
    	catch ( MagicFileNotFoundX e ) {
    		IWPLog.x("Magic File Not Found. Can't load icon: " + source, e);
    		throw new CannotLoadIconX(source, e);
    	}
    	catch ( InterruptedException e ) { 
    		IWPLog.x("Interrupted Exception. Can't load icon: " + source, e);
    		throw new CannotLoadIconX(source, e);
    	}
	}
    
	
	
}