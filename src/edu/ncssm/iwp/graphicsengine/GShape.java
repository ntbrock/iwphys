// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
//
// For full copyright information, see main source file,
// "iwp.java"
//
// 2008-Dec-25 brockman, touching this file so that the CVS HEAD tag will apply

package edu.ncssm.iwp.graphicsengine;

import edu.ncssm.iwp.exceptions.InvalidEquationException;
import edu.ncssm.iwp.exceptions.UnknownTickException;
import edu.ncssm.iwp.exceptions.UnknownVariableException;
import edu.ncssm.iwp.math.MCalculator;
import edu.ncssm.iwp.math.MCalculator_Parametric;
import edu.ncssm.iwp.math.MDataHistory;
import edu.ncssm.iwp.math.MVariables;
import edu.ncssm.iwp.objects.DObject_Solid;
import edu.ncssm.iwp.objects.DObject_Solid_designer;
import edu.ncssm.iwp.problemdb.DEntity;
import edu.ncssm.iwp.problemdb.DProblem;
import edu.ncssm.iwp.problemdb.DProblemState;

/**
 * Shapes tick and zero now too. The IWP draw should for now,
 * call calculate, and calc may re-used a cached variablne.
 *
 * brock 2006-Apr-29
 *
 * @author brockman
 *
 */

public abstract class GShape extends DEntity
{
    //2004.09.18: refactor to string library.
    public static final String STRING_RECTANGLE = "Rectangle";
    public static final String STRING_CIRCLE = "Circle";
    public static final String STRING_POLYGON = "Polygon";
    public static final String STRING_LINE = "Line";
    public static final String STRING_VECTOR = "Vector";
    public static final String STRING_BITMAP = "Bitmap";

    public static final String XML_RECTANGLE = "rectangle";
    public static final String XML_CIRCLE = "circle";
    public static final String XML_POLYGON = "polygon";
    public static final String XML_LINE = "line";
    public static final String XML_VECTOR = "vector";
    public static final String XML_BITMAP = "Bitmap";

    boolean isGraphable = true;
    boolean isDrawTrails = false;
    boolean isDrawVectors = false;

    protected GShape_GraphPropertySelector propSelector;
    protected GShape_VectorSelector vectorSelector;
    private GShape_AttachedVectorsRenderHelper attachedVectorsRenderHelper = new GShape_AttachedVectorsRenderHelper();

    protected MCalculator width;
    protected MCalculator height;
    protected MCalculator angle;

    protected String widthString;
    protected String heightString;
    protected String angleString;
    protected DObject_Solid solid; // I'm not a huge fan of attaching this here.

    public GShape( DObject_Solid solidIn, String widthIn, String heightIn )
    {
        propSelector = new GShape_GraphPropertySelector();
        vectorSelector = new GShape_VectorSelector();

        solid = solidIn;

        makeWidthCalculator( widthIn );
        makeHeightCalculator( heightIn );
    }


    /**Overload GShape for testing purposes
     * 10-31-07 Cory
     */
    public GShape( DObject_Solid solidIn, String widthIn, String heightIn, String angleIn )
    {
        propSelector = new GShape_GraphPropertySelector();
        vectorSelector = new GShape_VectorSelector();

        solid = solidIn;

        makeWidthCalculator( widthIn );
        makeHeightCalculator( heightIn );
        makeAngleCalculator(angleIn);
    }


    /**
     * Pass on the zero signal to my calculator children.
     * @param problem
     * @param state
     * @throws UnknownVariableException
     * @throws InvalidEquationException
     * @throws UnknownTickException
     */

    public void zero ( DProblem problem, DProblemState state )
        throws UnknownVariableException, InvalidEquationException, UnknownTickException
    {
        getWidthCalculator().zero(state.vars());
        getHeightCalculator().zero(state.vars());
        getAngleCalculator().zero(state.vars());
    }

    // This is similar to an object, but not exactly the same.
    public void tick ( DProblemState state )
        throws UnknownVariableException, InvalidEquationException, UnknownTickException
    {
        // I need to store width back to the variables here.

        double width = getWidthCalculator().calculate( state.vars() );
        state.vars().setAtCurrentTick( solid.getName() + "." + MVariables.WIDTH, width );

        double height = getHeightCalculator().calculate( state.vars() );
        state.vars().setAtCurrentTick( solid.getName() + "." + MVariables.HEIGHT, height );

        double angle;  //Polygon Hack
        if(this.getType()!="Polygon") {
            angle = getAngleCalculator().calculate(state.vars());
            state.vars().setAtCurrentTick(solid.getName() + "." + MVariables.ANGLE, angle);
        }
    }


    /**
     * Useful routine for graphing.
     *
     * @param state
     * @return
     * @throws UnknownTickException
     * @throws UnknownVariableException
     */
    protected double getSolidX ( DProblemState state )
        throws UnknownTickException, UnknownVariableException
    {
        return state.vars().getAtCurrentTick( solid.getName() + "." + MVariables.XDISP );
    }

    protected double getSolidY ( DProblemState state )
        throws UnknownTickException, UnknownVariableException
    {
        return state.vars().getAtCurrentTick( solid.getName() + "." + MVariables.YDISP );
    }

    protected double getSolidWidth ( DProblemState state )
        throws UnknownTickException, UnknownVariableException
    {
        return state.vars().getAtCurrentTick( solid.getName() + "." + MVariables.WIDTH );
    }

    protected double getSolidHeight ( DProblemState state )
        throws UnknownTickException, UnknownVariableException
    {
        return state.vars().getAtCurrentTick( solid.getName() + "." + MVariables.HEIGHT );
    }

    protected double getSolidAngle ( DProblemState state )
        throws UnknownTickException, UnknownVariableException
    {
        return state.vars().getAtCurrentTick( solid.getName() + "." + MVariables.ANGLE );
    }


    public abstract String getType();
    public abstract String getParams();

    protected void makeFilename(String fname) {}; // Dummy Stub for loading files


    /*
     * Width and height are commonly re-used attirbutes.
     *
     * 10-31 Cory -- Now adding in angle
     */

    public MCalculator getAngleCalculator ( ) { return angle; }
    public MCalculator getWidthCalculator ( ) { return width; }
    public MCalculator getHeightCalculator ( ) { return height; }

    public DObject_Solid getSolid ( ) { return solid; }

    public void makeWidthCalculator ( String eqn )
    {
        this.widthString = eqn;
        this.width = new MCalculator_Parametric( eqn );
    }

    public void makeHeightCalculator ( String eqn )
    {
        this.heightString = eqn;
        this.height = new MCalculator_Parametric( eqn );
    }

    public void makeAngleCalculator ( String eqn )
    {
        this.angleString = eqn;
        this.angle = new MCalculator_Parametric( eqn );
    }

    /**
     * As far as I know, this is only used by GShapeXMLHandler
     * @param calc
     */
    public void setWidthCalculator ( MCalculator_Parametric calc )
    {
        this.widthString = calc.getEquationString();
        this.width = calc;
    }


    /**
     * As far as I know, this is only used by GShapeXMLHandler
     * @param calc
     */

    public void setHeightCalculator ( MCalculator_Parametric calc )
    {
        this.heightString = calc.getEquationString();
        this.height = calc;
    }


    public void setAngleCalculator ( MCalculator_Parametric calc )
    {
        this.angleString = calc.getEquationString();
        this.angle = calc;
    }



    public double calcWidth( DProblemState state )
        throws UnknownVariableException, InvalidEquationException, UnknownTickException
    {
        return getWidthCalculator().calculate ( state.vars() );
    }

    public double calcHeight ( DProblemState state )
        throws UnknownVariableException, InvalidEquationException, UnknownTickException
    {
        return getHeightCalculator().calculate ( state.vars() );
    }

    public double calcAngle ( DProblemState state )
        throws UnknownVariableException, InvalidEquationException, UnknownTickException
    {
        return getAngleCalculator().calculate ( state.vars() );
    }


    public GShape_GraphPropertySelector getGShape_GraphPropertySelector()
    {
        return propSelector;
    }

    public void setGShape_GraphPropertySelector(GShape_GraphPropertySelector in)
    {
        propSelector=in;
    }

    public void setIsGraphable(boolean in) {isGraphable=in;}
    public boolean getIsGraphable() {return isGraphable;}

    // 2004.09.18: iwpmtg
    public void setIsDrawTrails(boolean in) { isDrawTrails=in; }
    public boolean getIsDrawTrails() { return isDrawTrails; }
    public boolean isDrawTrails() { return isDrawTrails; }

    public GShape_VectorSelector getGShape_VectorSelector() {return vectorSelector;}
    public void setGShape_VectorSelector(GShape_VectorSelector in) {
    vectorSelector=in;
    }
    public void setIsDrawVectors(boolean in) { isDrawVectors = in; }
    public boolean getIsDrawVectors() { return isDrawVectors; }
    public boolean isDrawVectors() { return isDrawVectors; }

    // all shape designers must be created as a child of solid designers.
    public GShape_designer getShape_designer(DObject_Solid_designer in) {
        return new GShape_designer(in, this );
    }

    public GShape_GraphPropertySelector getGraphOptionPanel() {return propSelector;}

    /**
     * Override this method in your implementations to draw.
     * @param drawer
     * @param state
     * @throws UnknownVariableException
     */
    public abstract void iwpDraw ( IWPDrawer drawer, DProblemState state )
        throws InvalidEquationException, UnknownVariableException, UnknownTickException;



    /**
     * 2006-Aug-29 brockman
     *
     * The graphing code now uses the master variable hash to look up
     * data.
     *
     * I really need to create a more generic graphing rule system, like
     * just graph a variable name over time. I would then map the legacy
     * shape graphing into the new graphing rule system.
     *
     * I could call it Outputgraphing or something.
     *
     * @param drawer
     * @param state
     * @throws UnknownVariableException
     */


    public void iwpGraph( IWPGrapher drawer,
                          DProblemState state )
        throws UnknownVariableException, UnknownTickException
    {
        MDataHistory vars = state.vars();

        // read from the problem for code prettyness below.
        int currentTick = 0;
        double timeStart = 0.0, timeChange = 0.0;

        currentTick = state.vars().getCurrentTick();
        timeChange = state.getProblem().getTimeObject().getChange();
        timeStart = state.getProblem().getTimeObject().getStartTime();


        // go over each variable that needs to be graphed.

        // DISPLACEMENT
        if ( propSelector.xPosSelected() ) {
            drawer.drawGraphForVariable(vars, solid.getName() + "." + MVariables.XDISP,
                                        currentTick, timeStart, timeChange );
        }

        if ( propSelector.yPosSelected() ) {
            drawer.drawGraphForVariable(vars, solid.getName() + "." + MVariables.YDISP,
                                        currentTick, timeStart, timeChange );
        }

        // VELOCITY
        if ( propSelector.xVelSelected() ) {
            drawer.drawGraphForVariable(vars, solid.getName() + "." + MVariables.XVEL,
                                        currentTick, timeStart, timeChange );
        }

        if ( propSelector.yVelSelected() ) {
            drawer.drawGraphForVariable(vars, solid.getName() + "." + MVariables.YVEL,
                                        currentTick, timeStart, timeChange );
        }

        // ACCELERATION
        if ( propSelector.xAccelSelected() ) {
            drawer.drawGraphForVariable(vars, solid.getName() + "." + MVariables.XACCEL,
                                        currentTick, timeStart, timeChange );
        }

        if ( propSelector.yAccelSelected() ) {
            drawer.drawGraphForVariable(vars, solid.getName() + "." + MVariables.YACCEL,
                                        currentTick, timeStart, timeChange );
        }
    }



    /*----------------------------------------------------------------------------------*/

    /**
     * The superclass handles the generic drawing of vecotrs and
     * graphs.
     */

    protected void commonDraw( IWPDrawer g, DProblemState state )
        throws InvalidEquationException, UnknownVariableException, UnknownTickException
    {

        // This should be based on a conditional property of the GShape.
        // also show objectrails
        if ( isDrawTrails() ) {
            iwpDrawTrails ( g, state );
        }

        if ( isDrawVectors() )
        {
            attachedVectorsRenderHelper.draw(this, g, state);
        }
    }



    /**
     * Draw lines that connect all the historical data points for an object.
     * 2004.09.18: iwpmtg: Object Trails
     *
     * 2006-Aug-25: Brockman, Fixed to work with the new MVariables Hash.
     */

    protected void iwpDrawTrails (IWPDrawer g, DProblemState state)
        throws UnknownVariableException, UnknownTickException
    {
        // draw a line that connects all of the displacement points.

        String xName = solid.getName() + "." + MVariables.XDISP;
        String yName = solid.getName() + "." + MVariables.YDISP;

        MDataHistory vars = state.vars();

        boolean firstPoint = true;
        double lastx = 0;
        double lasty = 0;

        for ( int i = 0; i <= state.getCurrentTick(); i++ ) {

            double x = vars.getAtTick(xName, i);
            double y = vars.getAtTick(yName, i);

            // connect the dots - draw a line between last point + this point.
            if ( ! firstPoint ) {
                g.drawLine ( x, y, lastx, lasty );
            }

            // OLD NOTES, but could still make sense.
            // we should check the time to see if we want to abourt the draw -
            // doing this would case the trails to rewind along with the
            // animation. As coded right now, they will always remain.

            lastx = x;
            lasty = y;
            firstPoint = false;

        }


    }


    // - Accessors

    public GShape_VectorSelector getVectorSelector() {
        return vectorSelector;
    }

    public void setVectorSelector(GShape_VectorSelector vectorSelector) {
        this.vectorSelector = vectorSelector;
    }

    //Dummy Definitions
    public String getFile() {return "";}
    public void setFile(String a) {}

}
