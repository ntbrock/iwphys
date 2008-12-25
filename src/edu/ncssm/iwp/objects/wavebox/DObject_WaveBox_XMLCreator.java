package edu.ncssm.iwp.objects.wavebox;

import java.util.Collection;
import java.util.Iterator;

import edu.ncssm.iwp.math.MCalculator;
import edu.ncssm.iwp.math.MCalculatorXMLCreator;
import edu.ncssm.iwp.plugin.IWPObjectXmlCreator;
import edu.ncssm.iwp.toolkit.xml.XMLElement;
import edu.ncssm.iwp.graphicsengine.GColorXMLCreator;

/**
 * Todo - implement
 * @author brockman
 *
 */



public class DObject_WaveBox_XMLCreator implements IWPObjectXmlCreator
{
	
	DObject_WaveBox parent;
	
	public DObject_WaveBox_XMLCreator ( DObject_WaveBox parent )
	{
		this.parent = parent;
	}
	
	public XMLElement getElement()
	{
		XMLElement elem = new XMLElement ( "object" );
		elem.addAttribute("class", parent.getClass().getName());
				 
		// FIll in all the core attributes.

		elem.addElement( new XMLElement ("name", parent.getName()) );

		GColorXMLCreator colorCreator = new GColorXMLCreator(parent.getGColor());
		elem.addElement( colorCreator.getElement() );
		
		//elem.addElement ( new XMLElement ( "displayMode", "" + parent.getDisplayMode() ));

		addCalcElement ( elem, "minX", parent.getMinX() );
		addCalcElement ( elem, "maxX", parent.getMaxX() );
		addCalcElement ( elem, "minY", parent.getMinY() );
		addCalcElement ( elem, "maxY", parent.getMaxY() );
		

		Collection waveGenerators = parent.getWaveGenerators();
		
		for(Iterator iter = waveGenerators.iterator(); iter.hasNext();)
		{
			
			/* Variables list
			 * x
			 * y
			 * width
			 * height
			 * x_velocity
			 * y_velocity
			 * Doppler
			 * waveIterations
			 * numWaves
			 * frequency
			 * wavelength
			 * period
			 * maxAmplitude
			 * phaseShift
			 * leftToRight
			 */

			// Sagar: I have made a sub-element here which should work.
			
			XMLElement waveGen = new XMLElement("waveGenerator");
			
            WaveGeneratorVO w = (WaveGeneratorVO)iter.next();
            
            XMLElement X 				= new XMLElement("X");
            XMLElement Y 				= new XMLElement("Y");
            XMLElement Width 			= new XMLElement("Width");
            XMLElement Height 			= new XMLElement("Height");
            XMLElement X_Velocity 		= new XMLElement("XVelocity");
            XMLElement Y_Velocity 		= new XMLElement("YVelocity");
            XMLElement Doppler 			= new XMLElement("Doppler");
            XMLElement waveIterations 	= new XMLElement("waveIterations");
            XMLElement numWaves 		= new XMLElement("numWaves");
            XMLElement Frequency 		= new XMLElement("Frequency");
            XMLElement Wavelength 		= new XMLElement("Wavelength");
            XMLElement Period 			= new XMLElement("Period");
            XMLElement maxAmplitude 	= new XMLElement("maxAmplitude");
            XMLElement phaseShift	 	= new XMLElement("phaseShift");
            XMLElement leftToRight	 	= new XMLElement("leftToRight");
            
            X.addElement( 				new MCalculatorXMLCreator(w.getX()).getElement());
            Y.addElement( 				new MCalculatorXMLCreator(w.getY()).getElement());
            Width.addElement( 			new MCalculatorXMLCreator(w.getWidth()).getElement());
            Height.addElement(			new MCalculatorXMLCreator(w.getHeight()).getElement());
            X_Velocity.addElement( 		new MCalculatorXMLCreator(w.getXVelocity()).getElement());
            Y_Velocity.addElement( 		new MCalculatorXMLCreator(w.getYVelocity()).getElement());
            Doppler.addElement( 		new MCalculatorXMLCreator(w.getDoppler()).getElement());
            waveIterations.addElement( 	new MCalculatorXMLCreator(w.getWaveIterations()).getElement());
            numWaves.addElement( 		new MCalculatorXMLCreator(w.getNumWaves()).getElement());
            Frequency.addElement( 		new MCalculatorXMLCreator(w.getFrequency()).getElement());
            Wavelength.addElement( 		new MCalculatorXMLCreator(w.getWavelength()).getElement());
            Period.addElement( 			new MCalculatorXMLCreator(w.getPeriod()).getElement());
            maxAmplitude.addElement( 	new MCalculatorXMLCreator(w.getMaxAmplitude()).getElement());
            phaseShift.addElement( 		new MCalculatorXMLCreator(w.getPhaseShift()).getElement());
            leftToRight.addElement( 	new MCalculatorXMLCreator(w.getLeftToRight()).getElement());
            
            /*
             * Before this the surrounding XML stuff for each wavegenerator should be there
             */
            
            waveGen.addElement(X);
            waveGen.addElement(Y);
            waveGen.addElement(Width);
            waveGen.addElement(Height);
            waveGen.addElement(X_Velocity);
            waveGen.addElement(Y_Velocity);
            waveGen.addElement(Doppler);
            waveGen.addElement(waveIterations);
            waveGen.addElement(numWaves);
            waveGen.addElement(Frequency);
            waveGen.addElement(Wavelength);
            waveGen.addElement(Period);
            waveGen.addElement(maxAmplitude);
            waveGen.addElement(phaseShift);
            waveGen.addElement(leftToRight);
         
            elem.addElement(waveGen);
        }
		
		//IWPLog.error(this, "TODO: Fill in the WaveBox XML Creation");
		
		return elem;
	}
	
	
	/**
	 * Utility function to make adding calculator elements to the stream easier.
	 * 
	 * @param elem
	 * @param name
	 * @param calc
	 */
	private void addCalcElement ( XMLElement elem, String name, MCalculator calc )
	{
		XMLElement minXE = new XMLElement( name );
		MCalculatorXMLCreator minXC = new MCalculatorXMLCreator(calc);
		minXE.addElement( minXC.getElement() );
		elem.addElement(minXE);
	}
	
}
