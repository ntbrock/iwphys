package edu.ncssm.iwp.graphicsengine;

import javax.swing.*;
import java.awt.*;
import edu.ncssm.iwp.ui.widgets.*;
import edu.ncssm.iwp.math.MCalculator_Parametric;

public class GShape_default_designer
	extends JPanel implements GShape_designer_interface
{
    
    GShape shape;
    GInput_Text inputWidth;
    GInput_Text inputHeight;
    GInput_Text inputAngle;

    public GShape_default_designer(GShape shape)
    {
    	this.shape = shape;
    	
    	setLayout(new GridLayout(3,1));
    	
    	// 2007-Jan-25 Brockman Bugfix#1551666 - The height and width now show into Designer.
    	inputWidth = new GInput_Text("Width",
    									((MCalculator_Parametric)shape.getWidthCalculator()).getEquationString() );
    	add(inputWidth);
    	
    	inputHeight = new GInput_Text("Height",
    								  ((MCalculator_Parametric)shape.getHeightCalculator()).getEquationString() );
    	add(inputHeight);

    	
    	inputAngle = new GInput_Text("Theta",
				  ((MCalculator_Parametric)shape.getAngleCalculator()).getEquationString() );
    	add(inputAngle);
    }

    
    public void setShape(GShape in)
    {
    	shape=in;
    }

    
    public GShape get(GShape in)
    {
    	in.makeWidthCalculator( inputWidth.getValue() );
    	in.makeHeightCalculator( inputHeight.getValue() );
    	in.makeAngleCalculator(inputAngle.getValue());
    	return in;
    }

}
