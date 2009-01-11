/**
 * 2008-Dec-25 enhancing the Bitmap selector to read dynamically from the .jar /images/bitmap/ directory
 * brockman
 */

package edu.ncssm.iwp.graphicsengine;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import edu.ncssm.iwp.ui.widgets.*;
import edu.ncssm.iwp.util.IWPLog;
import edu.ncssm.iwp.util.IWPMagicFile;
import edu.ncssm.iwp.exceptions.CannotLoadIconX;
import edu.ncssm.iwp.exceptions.MagicFileNotFoundX;
import edu.ncssm.iwp.math.MCalculator_Parametric;

public class GShape_Bitmap_designer
	extends JPanel implements GShape_designer_interface, ItemListener
{
	private static final long serialVersionUID = 1L;

    GShape shape;
    GInput_Text inputWidth;
    GInput_Text inputHeight;
    GInput_Text inputAngle;
    GInput_Selector inputFile;
    
    JLabel icon;
    

    public GShape_Bitmap_designer(GShape shape)
    {
    	this.shape = shape;
    	
    	setLayout(new BorderLayout());

    	
    	JPanel inputFrame = new JPanel();
    	inputFrame.setLayout(new GridLayout(4,1));
    	
    	
    	// 2007-Jan-25 Brockman Bugfix#1551666 - The height and width now show into Designer.
    	inputWidth = new GInput_Text("Width",
    									((MCalculator_Parametric)shape.getWidthCalculator()).getEquationString() );
    	inputFrame.add(inputWidth);
    	
    	inputHeight = new GInput_Text("Height",
    								  ((MCalculator_Parametric)shape.getHeightCalculator()).getEquationString() );
    	inputFrame.add(inputHeight);

    	inputAngle = new GInput_Text("Theta",
				  ((MCalculator_Parametric)shape.getAngleCalculator()).getEquationString() );
    	inputFrame.add(inputAngle);    	
    	

    	//Image Dropdown Selector - Cory    	
		// 2008-Dec-25 brockman, added the dynamic filename reader 
        String[] fileList = GShape_Bitmap_fileList.readBitmapFileList();

		inputFile = new GInput_Selector("Shape", fileList);
		
		inputFile.addItemListener(this);
		
		
		if(shape.getFile()!="") {

			// The file needs to be broken down into the part after the last / and minus the .png
			
			String partialFile = GShape_Bitmap_fileList.stripPathAndExtension(shape.getFile());

			// System.err.println("[GShape_Bitmap_Designer] looking for file: '" + partialFile + "'");

			inputFile.setSelected(partialFile);
		}
		
		inputFrame.add(inputFile);
		

		add(BorderLayout.CENTER,inputFrame);
		
		
        try { 
        	icon = new JLabel(loadIcon());
        	add (BorderLayout.SOUTH,icon );
        } catch ( CannotLoadIconX x ) { 
        	add ( new JLabel ( "Icon Error: Check etc/images/list.txt", JLabel.CENTER ));
        }
           	
    }
    
    
    public void refreshIcon() {

		if ( icon != null ) { 
    		this.remove(icon);
    	}
		
		
        try { 
        	icon = new JLabel(loadIcon());
        	add (BorderLayout.SOUTH,icon );

        } catch ( CannotLoadIconX x ) { 
        	System.err.println("[GShape_Bitmap_Designer] Refresh Error: " + x);
			x.printStackTrace();
        }

		// 2008-Dec-25 brockman, trying to get the icon change selection to update
        this.revalidate();
        this.repaint();
          			
    }
    
    

    
    public void setShape(GShape in)
    {
    	shape=in;
    }

    
    public GShape get(GShape in)
    {
    	in.makeWidthCalculator( inputWidth.getValue() );
    	in.makeHeightCalculator( inputHeight.getValue() );
    	in.makeAngleCalculator( inputAngle.getValue());
    	
    	in.makeFilename( GShape_Bitmap_fileList.translateFilenameIntoFullPath(inputFile.getValue() ) );

    	return in;
    }	
    
    
    //Temporary Try loading 
    private ImageIcon loadIcon() throws CannotLoadIconX
	{
    	String source = GShape_Bitmap_fileList.translateFilenameIntoFullPath(inputFile.getValue());
	
		try { 
			IWPMagicFile file = new IWPMagicFile(source);
    		MediaTracker m = new MediaTracker(new JPanel());
			Image img = Toolkit.getDefaultToolkit().createImage(file.readBytes());
			
			img = img.getScaledInstance(100, 100, Image.SCALE_DEFAULT);
			
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
    
    public void itemStateChanged(ItemEvent e) {
    	
    	refreshIcon();
    	
    }

}
