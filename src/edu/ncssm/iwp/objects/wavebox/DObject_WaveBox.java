package edu.ncssm.iwp.objects.wavebox;

import edu.ncssm.iwp.exceptions.InvalidEquationException;
import edu.ncssm.iwp.exceptions.UnknownTickException;
import edu.ncssm.iwp.exceptions.UnknownVariableException;
import edu.ncssm.iwp.plugin.*;
import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.ui.GAccessor_designer;

import java.util.*;

import edu.ncssm.iwp.math.*;
import edu.ncssm.iwp.util.IWPLog;
import edu.ncssm.iwp.graphicsengine.IWPDrawer;
import edu.ncssm.iwp.graphicsengine.GColor;
import java.awt.Color;

import edu.ncssm.iwp.problemdb.DProblem_designer;

/**
 * 2007-Jan-19 IWP 3
 *
 * This is the new master object type that handles the WaveBox.
 * The IWP Problems use the new xml <object class="edu.ncssm.iwp.objects.wavebox"
 * loader, so this guy needs to support the IWPXmlWorker interface to be saved +
 * loaded from stream.
 *
 * @author indurkhya
 *
 */

public class DObject_WaveBox 
    implements IWPObject, IWPAnimated, IWPDrawable, IWPCalculated, IWPDesigned, IWPXmlable
{

	public String getIconFilename()
	{
		return "/images/icon_DObject_Unknown.gif";
	}
	
	
    //public static final String DISPLAYMODE_CIRCULAR = "Circular";
    //public static final String DISPLAYMODE_PARALLEL = "Parallel";

    private String name = "New_WaveBox";
    private GColor color = new GColor(0,0,0);
    //private String displayMode;
    private Collection waveGenerators = new ArrayList(10);
    private MCalculator minX = new MCalculator_Parametric("-10");
    private MCalculator maxX = new MCalculator_Parametric("10");
    private MCalculator minY = new MCalculator_Parametric("-10");
    private MCalculator maxY = new MCalculator_Parametric("10");


    public DObject_WaveBox()
    {
    }

    public IWPObjectXmlCreator newXmlObjectCreator()
    {
        return new DObject_WaveBox_XMLCreator(this);
    }

    public IWPObjectXmlHandler newXmlObjectHandler()
    {
        return new DObject_WaveBox_XMLHandler();
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GAccessor_designer getDesigner( )
    {
        return new DObject_WaveBox_designer(this);
    }


    public void tick(DProblemState state )
        throws UnknownVariableException, UnknownTickException, InvalidEquationException
    {

        IWPLog.info(this, "TICK: " + state.getCurrentTick() );

    }

    public void zero(DProblem problem, DProblemState state)
        throws UnknownVariableException, InvalidEquationException, UnknownTickException
    {

        IWPLog.info(this, "ZERO: " + state.getCurrentTick() );
    }



    /**
     * TODO Sagar: This method should return the list of symbols that are set back
     * into MVariables by the tick method.
     *
     */
    
    public void parseEquation(String equation) throws InvalidEquationException
    {
    	// Build up a vector of Objects with their names
    	
    	// Compare 
    }
    
    public Collection getProvidedSymbols() throws InvalidEquationException {
        return null;
    }

    public Collection getRequiredSymbols()
        throws InvalidEquationException
    {
        ArrayList out = new ArrayList(100);

        for ( Iterator i = waveGenerators.iterator(); i.hasNext(); ) {
            WaveGeneratorVO waveGenerator = (WaveGeneratorVO) i.next();
            Collection holding;
            holding = waveGenerator.getFrequency().getRequiredSymbols();
            if ( holding != null ) { out.addAll(holding); }
            holding = waveGenerator.getMaxAmplitude().getRequiredSymbols();
            if ( holding != null ) { out.addAll(holding); }
            holding = waveGenerator.getPeriod().getRequiredSymbols();
            if ( holding != null ) { out.addAll(holding); }
            holding = waveGenerator.getPhaseShift().getRequiredSymbols();
            if ( holding != null ) { out.addAll(holding); }
            holding = waveGenerator.getWavelength().getRequiredSymbols();
            if ( holding != null ) { out.addAll(holding); }
            holding = waveGenerator.getX().getRequiredSymbols();
            if ( holding != null ) { out.addAll(holding); }
            holding = waveGenerator.getY().getRequiredSymbols();
            if ( holding != null ) { out.addAll(holding); }
        }

        return out;
    }

    public void iwpDraw(IWPDrawer drawer, DProblemState state)
    	throws UnknownVariableException, UnknownTickException, InvalidEquationException{
        IWPLog.info(this, "DRAW: " + drawer );


        // so, let's draw the back ground first.


        /*double mX = minX.calculate(state.vars());
        double mY = minY.calculate(state.vars());
        double MX = maxX.calculate(state.vars());
        double MY = maxY.calculate(state.vars());*/

        // WEIRD - must begin drawing at the top left corner.
        //drawer.drawRect(minX.calculate(state.vars()), maxY.calculate(state.vars()), MX - mX, MY - mY);

        drawer.setColor(Color.BLACK);
        
        //drawer.drawOval(-10.0, 10.0, 10.0, 10.0);
        
        // for kicks, draw every generator just as 1x1 circle of color
        for(Iterator iter = waveGenerators.iterator(); iter.hasNext();)
        {
            WaveGeneratorVO w = (WaveGeneratorVO)iter.next();

            
            WaveInstanceVO s = new WaveInstanceVO(w, state);
            
            drawer.setColor(w.getColor().getAWTColor());
            
            if(w.getDoppler().calculate(state.vars()) == 1.0)
            {
            	double NumberOfWaves = w.getNumWaves().calculate(state.vars());
            	double XV = w.getXVelocity().calculate(state.vars());
            	double YV = w.getYVelocity().calculate(state.vars());
            	
            	s.DrawDopplerWave(drawer, (int)(NumberOfWaves), XV, YV, state.problem.getTime().getTime());
            }
            else
            {
            	double Iterations = w.getWaveIterations().calculate(state.vars());
            	double Range = w.getWidth().calculate(state.vars()); // Needs to be added to variables
            	
            	s.DrawSinusoidalWave(
            			drawer, 
            			(int)Iterations, 
            			Range, 
            			w.getX().calculate(state.vars()), 
            			w.getY().calculate(state.vars()), 
            			w.getWidth().calculate(state.vars()), 
            			w.getHeight().calculate(state.vars()), 
            			state.vars().getCurrentTime());
            	
            	//s.DrawSinusoidalWave(drawer, (int)Iterations, Range, mX, MY, MX - mX, MY - mY, tate.vars().getCurrentTime());
            }
        }
        
        //drawer.drawRect(mX, MY, MX - mX, MY - mY);
    }

    //---------------
    // ACCESSORS

    public void addWaveGenerator(WaveGeneratorVO waveGen)
    {
        waveGenerators.add(waveGen);
    }

    public Collection getWaveGenerators() {
        return waveGenerators;
    }

    public void setWaveGenerators(Collection waveGenerators) {
        this.waveGenerators = waveGenerators;
    }


    public MCalculator getMaxX() {
        return maxX;
    }

    public void setMaxX(MCalculator maxX) {
        this.maxX = maxX;
    }

    public MCalculator getMaxY() {
        return maxY;
    }

    public void setMaxY(MCalculator maxY) {
        this.maxY = maxY;
    }

    public MCalculator getMinX() {
        return minX;
    }

    public void setMinX(MCalculator minX) {
        this.minX = minX;
    }

    public MCalculator getMinY() {
        return minY;
    }

    public void setMinY(MCalculator minY) {
        this.minY = minY;
    }

    public GColor getGColor() {
        return color;
    }

    public void setGColor(GColor color) {
        this.color = color;
    }


}
