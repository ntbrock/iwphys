// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
//
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.objects.wavebox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import edu.ncssm.iwp.graphicsengine.GColor;
import edu.ncssm.iwp.math.MCalculator_Parametric;
import edu.ncssm.iwp.math.designers.MCalculator_designer;
import edu.ncssm.iwp.objects.DObject_designer;
import edu.ncssm.iwp.plugin.IWPObject;
import edu.ncssm.iwp.ui.widgets.GInput_ColorSelector;


public class DObject_WaveBox_designer extends DObject_designer
{
	private static final long serialVersionUID = 1L;
    static int TEXT_WIDTH = 6;

    DObject_WaveBox object;


    public DObject_WaveBox_designer (DObject_WaveBox iobject )
    {
        object = iobject;
        buildGui();
        
        
    }

    JTextField inputMinX;
    JTextField inputMaxX;
    JTextField inputMinY;
    JTextField inputMaxY;

	JTextField inputX;
	JTextField inputY;
	JTextField inputWidth;
	JTextField inputHeight;
	JTextField inputX_velocity;
	JTextField inputY_velocity;
	JCheckBox  inputDoppler;
	JTextField inputIterations;
	JTextField inputNumWaves;
	JTextField inputFrequency;
	JTextField inputWavelength;
	JTextField inputPeriod;
	JTextField inputMaxAmplitude;
	JTextField inputPhaseShift;
	JCheckBox  inputLeftToRight;
	
	GInput_ColorSelector inputColor;
    
	MCalculator_designer mCalcX;
	MCalculator_designer mCalcY;
	MCalculator_designer mCalcWidth;
	MCalculator_designer mCalcHeight;
	MCalculator_designer mCalcX_velocity;
	MCalculator_designer mCalcY_velocity;
	MCalculator_designer mCalcDoppler;
	MCalculator_designer mCalcIterations;
	MCalculator_designer mCalcNumWaves;
	MCalculator_designer mCalcFrequency;
	MCalculator_designer mCalcWavelength;
	MCalculator_designer mCalcPeriod;
	MCalculator_designer mCalcMaxAmplitude;
	MCalculator_designer mCalcPhaseShift;
	MCalculator_designer mCalcLeftToRight;
    
    public void buildGui()
    {
        JPanel input = new JPanel();

        int horizGap = 30;
        int vertGap = 10;

        input.setLayout ( new GridLayout( 5, 2, horizGap, vertGap) );
        
        
        
        
        //Cory's Super Hacked Up Version that will no way in hell work.
        
        //Iterator itr = object.getWaveGenerators().iterator();
        
        //WaveGeneratorVO = itr.next();
        
        
        
        
        inputMinX = new JTextField ( "-10.0", TEXT_WIDTH );
        inputMaxX  = new JTextField ( "10.0", TEXT_WIDTH );
        inputMinY  = new JTextField ( "-10.0", TEXT_WIDTH );
        inputMaxY   = new JTextField ( "10.0", TEXT_WIDTH );
        inputX = new JTextField ( "0.0", TEXT_WIDTH );
    	inputY = new JTextField ( "0.0", TEXT_WIDTH );
    	inputWidth = new JTextField ( "10.0", TEXT_WIDTH );
    	inputHeight = new JTextField ( "10.0", TEXT_WIDTH );
    	inputX_velocity = new JTextField ( "0.0", TEXT_WIDTH );
    	inputY_velocity = new JTextField ( "0.0", TEXT_WIDTH );
    	inputDoppler = new JCheckBox ( "", true );
    	inputIterations = new JTextField ( "10", TEXT_WIDTH );
    	inputNumWaves = new JTextField ( "10", TEXT_WIDTH );
    	inputFrequency = new JTextField ( "2.0", TEXT_WIDTH );
    	inputWavelength = new JTextField ( "2.0", TEXT_WIDTH );
    	inputPeriod = new JTextField ( "0.5", TEXT_WIDTH );
    	inputMaxAmplitude = new JTextField ( "5.0", TEXT_WIDTH );
    	inputPhaseShift = new JTextField ( "0.0", TEXT_WIDTH );
    	inputLeftToRight = new JCheckBox ( "", true );
    	
    	inputColor = new GInput_ColorSelector("Font Color", object.getGColor().getAWTColor());

        input.add ( new JLabel ( "MinX: " ) );
        input.add ( inputMinX );

        input.add ( new JLabel ( "MaxX: " ) );
        input.add ( inputMaxX );

        input.add ( new JLabel ( "MinY: " ) );
        input.add ( inputMinY );

        input.add ( new JLabel ( "MaxY: " ) );
        input.add ( inputMaxY );
        
        input.setBorder(new EmptyBorder(10,10,10,10));
        
        JPanel extraInput = new JPanel();
        extraInput.setLayout ( new GridLayout( 20, 2, 5, vertGap) );
       // extraInput.setLayout(new GridLayout(3,1,5,5));
   
        extraInput.add(inputColor);
        
        extraInput.add ( new JLabel ( "Doppler(1 for yes): " ) );
        extraInput.add ( inputDoppler );
        
        extraInput.add ( new JLabel ( "X: " ) );
        extraInput.add ( inputX );
        
        extraInput.add ( new JLabel ( "Y: " ) );
        extraInput.add ( inputY );
        
        extraInput.add ( new JLabel ( "Width: " ) );
        extraInput.add ( inputWidth );
        
        extraInput.add ( new JLabel ( "Height: " ) );
        extraInput.add ( inputHeight );
        
        extraInput.add ( new JLabel ( "Vx: " ) );
        extraInput.add ( inputX_velocity );
        
        extraInput.add ( new JLabel ( "Vy: " ) );
        extraInput.add ( inputY_velocity );
        
        extraInput.add ( new JLabel ( "Iterations (Sinusoidal only): " ) );
        extraInput.add ( inputIterations );
        
        extraInput.add ( new JLabel ( "Number of Waves (Doppler Only): " ) );
        extraInput.add ( inputNumWaves );
        
        extraInput.add ( new JLabel ( "Frequency: " ) );
        extraInput.add ( inputFrequency );
        
        extraInput.add ( new JLabel ( "Wavelength: " ) );
        extraInput.add ( inputWavelength );
        
        //extraInput.add ( new JLabel ( "Period: " ) );
        //extraInput.add ( inputPeriod );
        
        extraInput.add ( new JLabel ( "Amplitude: " ) );
        extraInput.add ( inputMaxAmplitude );
        
        extraInput.add ( new JLabel ( "Phase Shift: " ) );
        extraInput.add ( inputPhaseShift );
        
        extraInput.add ( new JLabel ( "Left To Right (1 = yes): " ) );
        extraInput.add ( inputLeftToRight );
        
        JPanel norther = new JPanel(); norther.setLayout(new BorderLayout());
        norther.add(BorderLayout.NORTH, inputColor);
        norther.add(BorderLayout.CENTER, extraInput);
        
        JPanel combined = new JPanel();
        combined.setLayout(new BorderLayout());
        combined.add(BorderLayout.NORTH, input);
        combined.add(BorderLayout.SOUTH, norther);

        buildEasyGui ( "WaveBox", Color.BLACK, Color.WHITE, combined);
    }


    //ACCESSORS

    public String getName () { return object.getName ( ); }

    //object (g/s)
    public DObject_WaveBox getObject() { return object; }
    public void setObject(DObject_WaveBox iObject) { object = iObject; }


    public IWPObject buildObjectFromDesigner() {
        DObject_WaveBox goingBack = new DObject_WaveBox();
        /*
        goingBack.setStartWaveBox(Double.parseDouble(inputStart.getText()));
        goingBack.setStopWaveBox(Double.parseDouble(inputStop.getText()));
        goingBack.setChange(Double.parseDouble(inputStep.getText()));
        goingBack.setFps(Double.parseDouble(inputFps.getText()));
        */
        
        WaveGeneratorVO p = new WaveGeneratorVO();
        
    	/*
		JTextField inputX;
		JTextField inputY;
		JTextField inputWidth;
		JTextField inputHeight;
		JTextField inputX_velocity;
		JTextField inputY_velocity;
		JTextField inputDoppler;
		JTextField inputIterations;
		JTextField inputNumWaves;
		JTextField inputFrequency;
		JTextField inputWavelength;
		JTextField inputPeriod;
		JTextField inputMaxAmplitude;
		JTextField inputPhaseShift;
		JTextField inputLeftToRight;
		*/
    	
        p.setColor(new GColor(inputColor.getColor() ));
        p.setX(new MCalculator_Parametric(				inputX.getText()));
        p.setY(new MCalculator_Parametric(				inputY.getText()));
        p.setWidth(new MCalculator_Parametric(			inputWidth.getText()));
        p.setHeight(new MCalculator_Parametric(			inputHeight.getText()));
        p.setXVelocity(new MCalculator_Parametric(		inputX_velocity.getText()));
        p.setYVelocity(new MCalculator_Parametric(		inputY_velocity.getText()));
        
        if(inputDoppler.isSelected())
        	p.setDoppler(new MCalculator_Parametric("1"));
        else
        	p.setDoppler(new MCalculator_Parametric("0"));
        
        p.setWaveIterations(new MCalculator_Parametric(	inputIterations.getText()));
        p.setNumWaves(new MCalculator_Parametric(		inputNumWaves.getText()));
        p.setFrequency(new MCalculator_Parametric(		inputFrequency.getText()));
        p.setWavelength(new MCalculator_Parametric(		inputWavelength.getText()));
        p.setPeriod(new MCalculator_Parametric(			"5")); // For Debugging Purposes
        p.setMaxAmplitude(new MCalculator_Parametric(	inputMaxAmplitude.getText()));
        
        p.setPhaseShift(new MCalculator_Parametric(		inputPhaseShift.getText()));
        
        if(inputLeftToRight.isSelected())
        	p.setLeftToRight(new MCalculator_Parametric("1"));
        else
        	p.setLeftToRight(new MCalculator_Parametric("0"));
        
        goingBack.addWaveGenerator(p);

        return goingBack;
    }
}
