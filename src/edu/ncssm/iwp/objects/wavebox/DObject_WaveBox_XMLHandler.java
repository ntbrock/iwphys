package edu.ncssm.iwp.objects.wavebox;

import java.util.ArrayList;

import javax.xml.parsers.SAXParser;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import edu.ncssm.iwp.plugin.*;
import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.graphicsengine.GColor;
import edu.ncssm.iwp.graphicsengine.GColorXMLHandler;
import edu.ncssm.iwp.math.*;
import edu.ncssm.iwp.util.IWPLog;

public class DObject_WaveBox_XMLHandler extends IWPDefaultXmlHandler implements IWPObjectXmlHandler
{

	MCalculatorXMLHandler xmlCalculatorHandler = new MCalculatorXMLHandler();
	GColorXMLHandler xmlColorHandler = new GColorXMLHandler();
	
	ContentHandler parent;
	SAXParser parser;
	ArrayList objects = new ArrayList();

	DObject_WaveBox object;
	WaveGeneratorVO currentWavegen;
	String varName;
	MCalculator varCalculator;
	
	String debugVar;
	
	
	
	/*--------------------------------------------------------------------*/
	/**
	 * here we need to collect everything we need to know and then hand control back.
	 */

	public void collectObject ( SAXParser parser, IWPDefaultXmlHandler parent,
						       	IWPXmlable object )
		throws SAXException
	{
		this.parent = parent;
		this.parser = parser;
		this.object = (DObject_WaveBox) object;
		parser.getXMLReader().setContentHandler ( this );
	}
	
	public DObject_WaveBox_XMLHandler() { //Initialization
		currentWavegen = new WaveGeneratorVO();		
	}



	public void startElement(String namespaceURI, String localName, String qName, Attributes attr) throws SAXException {
		
		resetContents();

		if ( qName.equals("waveGenerator") ) {
    	
			// make a new temp gen
			//currentWavegen = new WaveGeneratorVO();
		} else if ( qName.equals("var") ) {
			varName = attr.getValue("name");
	    
		} else if ( qName.equals("color") ) { 
            GColor color = new GColor();
            object.setGColor ( color );
            xmlColorHandler.collectObject ( parser, this, color );
            
		} else if ( qName.equals("calculator") ) {

			varCalculator = null;			
			String type = (String)attr.getValue("type");
			
			if ( type.equals ( "parametric" ) ) { 
				varCalculator = new MCalculator_Parametric();
			} else {
				IWPLog.error(this, "The Wavebox only supports Parametric Calcs");
				return;
			}

			xmlCalculatorHandler.collectObject ( parser, this, varCalculator );
		}
	
	}

	public void endElement(String namespaceURI, String localName,
			String qName) throws SAXException
	{
		String contents = getContents();

        if ( qName.equals("name") ) {

            object.setName(contents.toString());
        } else if ( qName.equals("waveGenerator") ) {
        	
        	// end the generator.
        	object.addWaveGenerator(currentWavegen);
    
        } else if ( qName.equals("object") ) {
            /* ok. Im done! */
            parser.getXMLReader().setContentHandler ( parent );
        } /*else if ( qName.equals("var") ) {     //Sagar's original version that _doesn't work_
        	setVar(varName, varCalculator);
        } */
        
        else {
        	setVar(qName, varCalculator);
        }
        

    }

	private void setVar ( String name, MCalculator calc )
	{
		
		
		debugVar = name;
		
		if ( name.equals("minX") ) { 
			object.setMinX(calc);
		} else if ( name.equals("maxX") ) {
			object.setMaxX(calc);
		} else if ( name.equals("minY") ) {
			object.setMinY(calc);
		} else if ( name.equals("maxY") ) {
			object.setMaxY(calc);
		
		// otherwise, it's in the currentWavegen
			
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
		
		} else if ( name.equals("X") ) { 
			currentWavegen.setX(calc);
		} else if ( name.equals("Y") ) {
			currentWavegen.setY(calc);
		} else if ( name.equals("Width") ) {
			currentWavegen.setWidth(calc);
		} else if ( name.equals("Height") ) {
			currentWavegen.setHeight(calc);
		} else if ( name.equals("X_Velocity")) {
			currentWavegen.setXVelocity(calc);
		} else if ( name.equals("Y_Velocity")) {
			currentWavegen.setYVelocity(calc);
		} else if ( name.equals("Doppler")) {
			currentWavegen.setDoppler(calc);
		} else if ( name.equals("waveIterations") ) {
			currentWavegen.setWaveIterations(calc);
		} else if ( name.equals("numWaves") ) {
			currentWavegen.setNumWaves(calc);
		} else if ( name.equals("Frequency") ) {
			currentWavegen.setFrequency(calc);
		} else if ( name.equals("Wavelength") ) {
			currentWavegen.setWavelength(calc);
		} else if ( name.equals("Period") ) {
			currentWavegen.setPeriod(calc);
		} else if ( name.equals("maxAmplitude") ) {
			currentWavegen.setMaxAmplitude(calc);
		} else if ( name.equals("phaseShift") ) {
			currentWavegen.setPhaseShift(calc);
		} else if ( name.equals("leftToRight") ) {
			currentWavegen.setLeftToRight(calc);
		} else {
			IWPLog.error(this, "Uknown variable set: " + name );
		}
	}
		
}
