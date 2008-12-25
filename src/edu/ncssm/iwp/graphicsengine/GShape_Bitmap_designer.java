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

    	
    	Panel inputFrame = new Panel();
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
        String[] fileList = {
                "car_final",
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
                "sun",
                
        
				};

		inputFile = new GInput_Selector("Shape", fileList);
		
		inputFile.addItemListener(this);
		
		
		if(shape.getFile()!="") {
			String temp = shape.getFile();
			inputFile.setSelected(temp.substring(8, temp.length()-4));
		}
		inputFrame.add(inputFile);
		

		add(BorderLayout.CENTER,inputFrame);
		
		
        try { 
        	icon = new JLabel(loadIcon());
        	add (BorderLayout.SOUTH,icon );
        } catch ( CannotLoadIconX x ) { 
        	add ( new JLabel ( "Icon Error", JLabel.CENTER ));
        }
        

		
		
    	
    	
/*    	// Add in file name
    	inputFile = new GInput_Text("File", "/images/a.png" );
    	add(inputFile);*/
    }
    
    
    public void refreshIcon() {
    	this.remove(icon);
    	
    	
        try { 
        	icon = new JLabel(loadIcon());
        	add (BorderLayout.SOUTH,icon );
        } catch ( CannotLoadIconX x ) { 
        	System.out.println("Refresh Error");
        }
        
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
    	
    	in.makeFilename("/images/"+inputFile.getValue()+".png");
    	
    	return in;
    }	
    
    
    //Temporary Try loading Shits and Giggles Stuff
   
    private ImageIcon loadIcon() throws CannotLoadIconX
	{
    	String source = "/images/"+inputFile.getValue()+".png";
	
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
