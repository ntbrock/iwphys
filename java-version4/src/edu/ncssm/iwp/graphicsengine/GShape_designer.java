// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
//
// For full copyright information, see main source file,
// "iwp.java"


package edu.ncssm.iwp.graphicsengine;

import edu.ncssm.iwp.ui.*;
import edu.ncssm.iwp.util.*;
import edu.ncssm.iwp.ui.widgets.*;
import edu.ncssm.iwp.objects.*;
import edu.ncssm.iwp.exceptions.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;


/**
 * GShape designer is the Widget used in the Designer to manage the properties of a
 * Shape. It contains two sub-ui implementations: GShape_default_designer and
 * GShape_Polygon_designer.
 * 
 * @author brockman
 *
 */

public class GShape_designer extends GAccessor implements ItemListener
{
	private static final long serialVersionUID = 1L;
   	GShape shape;

   	// I don't have a ref to the parent object here, but instead
   	// the parent DObject_Solid designer.
   	DObject_Solid_designer solidDesigner;
   	
   	GShape_default_designer default_designer;
    GShape_Polygon_designer polygon_designer;
    GShape_Bitmap_designer bitmap_designer;

    JPanel dependantSection;

    // UI elements.
    JLabel label;
    GInput_Selector inputType;
    GShape_GraphPropertySelector gps;
    GShape_VectorSelector vs;
    // these now live in the sub designer.
    //GInput_Text inputWidth;
    //GInput_Text inputHeight;

    JRadioButton isGraphable;
    JRadioButton isDrawTrails;      //2004.09.18: iwpmtg: Draw Trails
    JRadioButton isDrawVectors;
    
    

    public GShape_designer (DObject_Solid_designer solidDesigner,
    						GShape iShape)
    {
    	this.solidDesigner = solidDesigner;
    	
        label = new JLabel("Shape");

        shape = iShape;

        dependantSection=new JPanel();
        /* set up designer "cards" */
        default_designer=new GShape_default_designer(shape);
        polygon_designer=new GShape_Polygon_designer(this, shape);
        bitmap_designer = new GShape_Bitmap_designer(shape);

        buildGui();
        //inputType.setSelected(shape.getType());
    }

    
    public void buildGui()
    {
        JPanel leftPanel=new JPanel();
        TitledBorder titleBorder = BorderFactory.createTitledBorder("Shape");
        titleBorder.setTitleJustification(TitledBorder.LEFT);
        setBorder(titleBorder);

        //leftPanel.setLayout(new GridLayout(3,1));
        leftPanel.setLayout(new GridLayout(1,1));


        // 2004.09.18: iwpmtg: adding a 'line' object
        String[] shapeTypes = { GShape.STRING_RECTANGLE,
                                GShape.STRING_CIRCLE,
                                GShape.STRING_POLYGON,
                                GShape.STRING_LINE,
                                GShape.STRING_VECTOR ,
                                GShape.STRING_BITMAP
        						};

        inputType = new GInput_Selector("Type", shapeTypes);
        inputType.setSelected(shape.getType());
        leftPanel.add(inputType);
        inputType.addItemListener(this);

        // Old Squares-only shape input?
        //inputWidth = new GInput_Text("Width", shape.getWidthString()+"");
        //leftPanel.add(inputWidth);

        //inputHeight = new GInput_Text("Height", shape.getHeightString()+
        //"");
        //leftPanel.add(inputHeight);

        setLayout(new BorderLayout());
        add(BorderLayout.NORTH,leftPanel);
        buildDependantSection();
        add(BorderLayout.CENTER,dependantSection);


        gps=shape.getGShape_GraphPropertySelector();
        isGraphable=new JRadioButton("Graphable?");
        //isGraphable.setSelected(shape.getIsGraphable());

        JPanel graphable=new JPanel();
        graphable.setLayout(new BorderLayout());
        graphable.add( BorderLayout.NORTH, isGraphable);
        graphable.add( BorderLayout.SOUTH, gps);
        
        //2004.09.18: iwpmtg: Draw Trails
        isDrawTrails = new JRadioButton ("Draw Object Trails?" );
        isDrawTrails.setSelected ( shape.getIsDrawTrails() );
        
        vs = shape.getGShape_VectorSelector();
        isDrawVectors = new JRadioButton ("Draw Vectors?" );
        isDrawVectors.setSelected ( shape.getIsDrawVectors() );
        
        // subpanel for vector selection radio buttons
        JPanel drawVectors = new JPanel();
        drawVectors.setLayout(new BorderLayout());
        drawVectors.add( BorderLayout.NORTH, isDrawVectors );
        drawVectors.add( BorderLayout.SOUTH, vs );

        // needed a new subpanel;
        JPanel attributeSubPanel = new JPanel();
        attributeSubPanel.setLayout(new BorderLayout());

        attributeSubPanel.add ( BorderLayout.NORTH, isDrawTrails );
        attributeSubPanel.add ( BorderLayout.CENTER, drawVectors );
        attributeSubPanel.add ( BorderLayout.SOUTH, graphable );

		// 2008-Dec-05, need to keep everythig pushed to the top.
		JPanel pushToTopPanel = new JPanel();
		pushToTopPanel.setLayout(new BorderLayout());
		pushToTopPanel.add(BorderLayout.NORTH, attributeSubPanel);


        add(BorderLayout.EAST, pushToTopPanel );
        validate();

    }


	// 2008-Dec-25 brockman, trying to get the main scrollbar of teh designer to 
	// recognize the change in size
	public void rebuildGui()
	{
		revalidate();
		solidDesigner.revalidate();
	}

    //ACCESSORS

    //shape (g/s)
    public GShape get()
    	throws DesignerInputException, InvalidEquationException
    {
    	String selected = inputType.getSelected();
        GShape out = null;
        
        // Which one of these is active?
        if(selected.equals(GShape.STRING_RECTANGLE)) {

        	out = new GShape_Rectangle ( solidDesigner.getObject() );
        	default_designer.get(out);
        	
        } else if (selected.equals(GShape.STRING_CIRCLE)) {
            
        	out = new GShape_Circle( solidDesigner.getObject() );
        	default_designer.get(out);
        
        } else if (selected.equals(GShape.STRING_POLYGON)) {
            
        	// ALERT: this is not the default, it's the polgyon.
        	out = new GShape_Polygon ( solidDesigner.getObject() );
        	polygon_designer.get(out);
        	
        } else if ( selected.equals ( GShape_Polygon.STRING_LINE ) ) {

        	out = new GShape_Line ( solidDesigner.getObject() );
        	default_designer.get(out);
        	
        } else if (selected.equals (GShape_Polygon.STRING_VECTOR)) {

        	out = new GShape_Vector ( solidDesigner.getObject() ); 
   			default_designer.get(out);
   			
        } else if (selected.equals (GShape_Polygon.STRING_BITMAP)) {

        	out = new GShape_Bitmap ( solidDesigner.getObject() ); 
   			bitmap_designer.get(out);
   			
        } else {

        	IWPLog.error ( "Unknown Shape Type: " + selected + ". Can't build object" );
        	throw new DesignerInputException("Unknown Shape Type: " + selected + ". Can't build object");

        }
        
        // Now, populate all the common stuff.
        
        out.setGShape_GraphPropertySelector(gps.copy());
        out.setGShape_VectorSelector(vs.copy());

        //2004.09.18: iwpmtg: Draw Trails
        out.setIsGraphable(isGraphable.isSelected());
        out.setIsDrawTrails (isDrawTrails.isSelected());
        out.setIsDrawVectors (isDrawVectors.isSelected());

        return out;
    }



    
    
    public void itemStateChanged(ItemEvent e) {
        
    	if(e.getStateChange()==ItemEvent.SELECTED) {
        
    		try { 
    			shape=get();
    		} catch ( DesignerInputException x ) { 
    			IWPLogPopup.x(this, "ItemStateChanged, DesignerInputX: " + x.getMessage(), x );
    		} catch ( InvalidEquationException x ) { 
    			IWPLogPopup.x(this, "ItemStateChanged, InvalidEquationException: " + x.getMessage(), x );
    		}
            
            //buildGui();
            buildDependantSection();
        }

		iwpRepaint();
    }


    // Construct a sub-gui for the selected design? No real idea.
    protected void buildDependantSection()
    {
        String selected=inputType.getSelected();
        IWPLog.debug( this, "[GShape_designer] buildDependantSection()");
        
        dependantSection.removeAll();
        if(selected.equals(GShape.STRING_POLYGON)) {
            dependantSection.add(polygon_designer);
        } 
        else if(selected.equals(GShape.STRING_BITMAP)) {
            dependantSection.add(bitmap_designer);
        } 
        else {
            dependantSection.add(default_designer);
        }
        if(solidDesigner!=null) {
            solidDesigner.doValidation();
        } else {
            //setSize(getPreferredSize());
            validate();
            repaint();
        }
        
        iwpRepaint();
    }


	protected void iwpRepaint()
	{
		revalidate();
		solidDesigner.iwpRepaint();
	}


}


