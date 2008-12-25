// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
//
// For full copyright information, see main source file,
// "iwp.java"
//
// 2008-Dec-25 brockman, touching this file so that the CVS HEAD tag will apply


package edu.ncssm.iwp.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import edu.ncssm.iwp.exceptions.InvalidEquationException;
import edu.ncssm.iwp.exceptions.InvalidObjectNameX;
import edu.ncssm.iwp.exceptions.UnknownTickException;
import edu.ncssm.iwp.exceptions.UnknownVariableException;
import edu.ncssm.iwp.graphicsengine.GColor;
import edu.ncssm.iwp.graphicsengine.GShape;
import edu.ncssm.iwp.graphicsengine.GShape_GraphPropertySelector;
import edu.ncssm.iwp.graphicsengine.GShape_Rectangle;
import edu.ncssm.iwp.graphicsengine.IWPDrawer;
import edu.ncssm.iwp.graphicsengine.IWPGrapher;
import edu.ncssm.iwp.math.MCalculator;
import edu.ncssm.iwp.math.MCalculator_Parametric;
import edu.ncssm.iwp.plugin.IWPAnimated;
import edu.ncssm.iwp.plugin.IWPCalculated;
import edu.ncssm.iwp.plugin.IWPDesigned;
import edu.ncssm.iwp.plugin.IWPDrawable;
import edu.ncssm.iwp.plugin.IWPGraphable;
import edu.ncssm.iwp.plugin.IWPObject;
import edu.ncssm.iwp.plugin.IWPObjectXmlCreator;
import edu.ncssm.iwp.plugin.IWPObjectXmlHandler;
import edu.ncssm.iwp.plugin.IWPXmlable;
import edu.ncssm.iwp.problemdb.DProblem;
import edu.ncssm.iwp.problemdb.DProblemState;
import edu.ncssm.iwp.ui.GAccessor_designer;


public class DObject_Solid
    implements IWPObject, IWPDrawable, IWPGraphable, IWPAnimated, IWPCalculated, IWPXmlable, IWPDesigned
{


    public String getIconFilename()
    {
        return "icon_DObject_Solid.gif";
    }

    public String name = "New_Solid";
    public GShape shape = new GShape_Rectangle( this );

    public GColor color = new GColor(102,204,255); // UNC Blue

    MCalculator x = new MCalculator_Parametric ();
    MCalculator y = new MCalculator_Parametric ();


    public MCalculator getCalcX() { return x; }
    public MCalculator getCalcY() { return y; }
    public void setCalculatorX (MCalculator newX) {x=newX;}
    public void setCalculatorY (MCalculator newY) {y=newY;}



    public DObject_Solid()
    {
    }


    public String getName() {
        return name;
    }

    public void setName(String name)
        throws InvalidObjectNameX
    {
        DObjectUtility.ensureName(name);
        this.name = name;
    }

    public GColor getColor() {
        return color;
    }
    public void setColor(GColor color) {
        this.color = color;
    }
    public GShape getShape() {
        return shape;
    }
    public void setShape(GShape shape) {
        this.shape = shape;
    }


    // IWP Animated

    public void zero ( DProblem problem, DProblemState state )
        throws UnknownVariableException, InvalidEquationException, UnknownTickException
    {
        // CIRCULAR BOOKMARK - trying this to just get some values set into the problem on initial state.

        setVariablesFromCalculators(state);

        // This is necessary to reset Euler data.
        x.zero(state.vars());
        y.zero(state.vars());

        // passing this down for good luck.
        shape.zero(problem, state);

        // calculate + set the t=0 state.
        tick(state);
    }




    public void tick ( DProblemState state )
        throws UnknownVariableException, UnknownTickException, InvalidEquationException
    {
        // 2007-Jun-04 brockman. Solving the self-dependency calculation by setting last frame's values
        // in as current. Don't worry, they are overwritten again after the calculations. This just make's
        // last frames data available, so you can do stuff like  P.yaccel = P.yvel + 5
        // Note that this is another way that you can use an old feature: in diff accel equations, the
        // variables d , v, x are present.
        setVariablesFromCalculators(state);

        // More detailed unknown var reporting
        try {
            x.calculate( state.vars() );
        } catch ( UnknownVariableException x ) {
            throw new UnknownVariableException(x.getMessage() + " (in " + this.getName() +" X)");
        }

        // V2 Compatability - there was a weirdness in V2, where the X calculated results would be instantly
        // available in the proiblem state and visible to the Y component. We need to replicate this for V3,
        // even those it doesn't make 100% sense.
        // 2007-Jun-06 brockman
        setVariablesFromCalculators(state);

        try {
            y.calculate( state.vars() );
        } catch ( UnknownVariableException x ) {
            throw new UnknownVariableException(x.getMessage() + " (in " + this.getName() +" Y)");
        }


        setVariablesFromCalculators(state);

        // Note: this sets the object.width, object.height vars.
        shape.tick(state);

    }

    /**
     *
     * This sets all the calculator properties into the state variables.
     * 2007-Jun-04 brockman. Trying to resolve the solid self-dependency problems.
     */

    private void setVariablesFromCalculators(DProblemState state)
    {
        // Note, I put these into temp variables so I can easily see the values in a debugger.
        double xdisp =  x.getDisp();
        double xvel =   x.getVel();
        double xaccel = x.getAccel();

        state.vars().setAtCurrentTick( getName() + ".x"+MCalculator.SYMBOL_DISP, xdisp);
        state.vars().setAtCurrentTick( getName() + ".x"+MCalculator.SYMBOL_POS,  xdisp);
        state.vars().setAtCurrentTick( getName() + ".x"+MCalculator.SYMBOL_VEL,  xvel);
        state.vars().setAtCurrentTick( getName() + ".x"+MCalculator.SYMBOL_ACCEL,xaccel);

        double ydisp =  y.getDisp();
        double yvel =   y.getVel();
        double yaccel = y.getAccel();

        state.vars().setAtCurrentTick( getName() + ".y"+MCalculator.SYMBOL_DISP, ydisp);
        state.vars().setAtCurrentTick( getName() + ".y"+MCalculator.SYMBOL_POS,  ydisp);
        state.vars().setAtCurrentTick( getName() + ".y"+MCalculator.SYMBOL_VEL,  yvel);
        state.vars().setAtCurrentTick( getName() + ".y"+MCalculator.SYMBOL_ACCEL,yaccel);
    }



    /**
     * Returns the list of symbols that this object provides and that are
     * usable by other parts of the problem.
     */

    public Collection getProvidedSymbols() throws InvalidEquationException
    {
        ArrayList out = new ArrayList(15);

        Collection fromCalc = null;

        // x - Here is where I get the sub-symbols from the calculator and I
        // attach them to the object
        fromCalc = x.getProvidedSymbols();
        if ( fromCalc != null ) {
            for ( Iterator i = fromCalc.iterator(); i.hasNext(); ) {
                String variableName = (String) i.next();
                out.add(getName()+".x" + variableName);
            }
        }

        // y - Here is where I get the sub-symbols from the calculator and I
        // attach them to the object
        fromCalc = y.getProvidedSymbols();
        if ( fromCalc != null ) {
            for ( Iterator i = fromCalc.iterator(); i.hasNext(); ) {
                String variableName = (String) i.next();
                out.add(getName()+".y" + variableName);
            }
        }

        return out;
    }


    // IWPAnimatable - The top-level needs to know the order in which to order calculation
    public Collection getRequiredSymbols()
        throws InvalidEquationException
    {

        ArrayList out = new ArrayList(100);

        Collection xSymbols = x.getRequiredSymbols();
        Collection ySymbols = y.getRequiredSymbols();
        Collection shapeWSymbols = shape.getWidthCalculator().getRequiredSymbols();
        Collection shapeHSymbols = shape.getHeightCalculator().getRequiredSymbols();
        Collection shapeASymbols = null; //Hack for Polygons
        if(shape.getType()!="Polygon") {
            shapeASymbols = shape.getAngleCalculator().getRequiredSymbols();}

        if ( xSymbols != null ) { out.addAll(xSymbols); }
        if ( ySymbols != null  ) { out.addAll(ySymbols); }
        if ( shapeWSymbols != null  ) { out.addAll(shapeWSymbols); }
        if ( shapeHSymbols != null  ) { out.addAll(shapeHSymbols); }
        if ( shapeASymbols != null  ) { out.addAll(shapeASymbols); }

        return out;
    }



    //---------------------
    // These are used by the XMLHandlers.
    public void setCalc(String iVar, MCalculator iCalc)
    {
        if (iVar.equalsIgnoreCase("x")) {
            x = iCalc;
        } else if (iVar.equalsIgnoreCase("y")) {
            y = iCalc;
        }
    }

    public GColor getGColor ( ) { return color; }
    public void setGColor ( GColor color )
    {
        this.color = color;
    }

    //---------------------
    //IWPDrawable interface
    public void iwpDraw ( IWPDrawer drawer,DProblemState state)
        throws UnknownVariableException, InvalidEquationException, UnknownTickException
    {
        shape.iwpDraw(drawer,state);
    }

    //IWPGraphable interface
    public void iwpGraph(IWPGrapher drawer,DProblemState lState)
        throws UnknownVariableException, UnknownTickException
    {
        shape.iwpGraph(drawer,lState ); // xData and Y Data are now carried in the state.
    }

    public GShape_GraphPropertySelector getGraphOptionPanel() {
        return shape.getGraphOptionPanel();
    }


    /*----------------------------------------------------------------------*/
    /* DESIGNERS */
    /*----------------------------------------------------------------------*/

    public GAccessor_designer getDesigner ( ) {
        return new DObject_Solid_designer ( this );
    }

    /*----------------------------------------------------------------------*/



    public IWPObjectXmlCreator newXmlObjectCreator()
    {
        return new DObject_SolidXMLCreator(this);
    }

    public IWPObjectXmlHandler newXmlObjectHandler()
    {
        return new DObject_SolidXMLHandler();
    }

}





