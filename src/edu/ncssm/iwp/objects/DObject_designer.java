package edu.ncssm.iwp.objects;

import edu.ncssm.iwp.ui.*;
import edu.ncssm.iwp.plugin.*;
import edu.ncssm.iwp.exceptions.DesignerInputException;
import edu.ncssm.iwp.exceptions.InvalidEquationException;
import edu.ncssm.iwp.problemdb.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;


/**
 * The basic designer. Serves as an interface and helps out with the buildEasyGui
 * 
 * @author brockman
 *
 */

public abstract class DObject_designer extends GAccessor_designer
{
    public static int WIDTH = 460;
    public static int STRUT = 5;
    public static int CELLGAP = 3;

    public Vector unitList;

    //link for name updates -- see designers for details. NOte, this is used by the keyEvent in the designer subclasses.
    protected DProblem_designer parentProblemDesigner;

    public DObject_designer () {}

    public void setParentProblemDesigner(DProblem_designer d){
    parentProblemDesigner=d;
    }
    public DProblem_designer getParentProblemDesigner() {return parentProblemDesigner;}

    public void buildGui() {}


    public abstract IWPObject buildObjectFromDesigner ( ) throws DesignerInputException, InvalidEquationException;

    public Vector getUnitList ( )
    {
        return unitList;
    }

    public void setUnitList ( Vector unitList )
    {
        this.unitList = unitList;
    }
    /**
     * This is an optional method to finish up an init'ing that needs to be done.
     */
    public void finalize() {}



    /**
     * IWP3
     * @author brockman
     * Sub implemetnations can call this to easily get the formatting + coloring
     * associated with the new IWP3 stylish designers
     */

    protected void buildEasyGui( String headerText, Color headerForeground, Color headerBackground,
                                 JComponent meat )
    {
        JLabel typeLabel = new JLabel ( headerText );
        typeLabel.setOpaque(true);
        typeLabel.setForeground ( headerForeground );
        typeLabel.setBackground ( headerBackground );
        typeLabel.setBorder(new EmptyBorder(5,5,5,5));


        JPanel inputNorth = new JPanel();
        inputNorth.setLayout(new BorderLayout());
        inputNorth.add(BorderLayout.NORTH, meat );

        setLayout(new BorderLayout());
        add(BorderLayout.NORTH, typeLabel);
        add(BorderLayout.CENTER, inputNorth );

        // override the width to the size of the designer.
        Dimension preferredSize = getPreferredSize();
        setPreferredSize ( new Dimension ( WIDTH, preferredSize.height ));
    }

}



