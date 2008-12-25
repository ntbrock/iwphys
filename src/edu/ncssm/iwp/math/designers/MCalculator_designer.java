package edu.ncssm.iwp.math.designers;

import edu.ncssm.iwp.ui.*;
import edu.ncssm.iwp.util.*;
import edu.ncssm.iwp.exceptions.*;
import edu.ncssm.iwp.math.MCalculator;
import edu.ncssm.iwp.math.MCalculator_Euler;
import edu.ncssm.iwp.math.MCalculator_Parametric;
import edu.ncssm.iwp.math.MCalculator_RK2;
import edu.ncssm.iwp.math.MCalculator_RK4;
import edu.ncssm.iwp.objects.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;


/**
 * MCalculator_designer
 * @author brockman 
 * Date: 02/17/01 (created before, dated now)
 * Update: 2007-Feb-01. Split out the MCalculator_designer to MCalculator_subDesigner for subtypes.
 * 
 * 
 * This is the Calculator selection manager + container .
 * The contaner object will create a Calculator object of each type
 * (along w/ it's corresponding designer widgets), and display each of these
 * when they're selected in the combobox.
 * 
 * Ways to instantiate:
 * 
 * new MCalculator_designer ( Label, MCalculator ):
 * 	where Mcalculator can be null.
 * 
 * Figures out what type the object passed in is, and creates new, blank
 * calculators for the rest of the types. sets the default selection to
 * the type passed in.
 *  
 * When getCalculator is called, this widge see's which type is selected, and
 * then returns the calculator object associated w/ it.
 * 
 * The widgets need to be created at insantiation time, so that data is not
 * erased when you select a different type of calculator.
 * 
 * This is model is actually quite funky, b/c of it's gui object polling nature.
 * The higher level code will be unable to reference the actual instantiated
 * object, but must call the dynamic routines if the design window is open. I
 * think that can be handled in the code, and is, IMHO, the cleanest, most
 * dynamic way to do it.
 * 
 *  It just required supporting code up above, you can't just depend on your
 *  MCalculator object being transparently updated.
 */



public final class MCalculator_designer extends GAccessor_designer implements AlertListener
{
	public static final String DISPLAY_PARAMETRIC = "Parametric";
	public static final String DISPLAY_EULER = "Euler";
	public static final String DISPLAY_RK2 = "RK2";
	public static final String DISPLAY_RK4 = "RK4";
	
	
	Hashtable oDesignerWidgets = new Hashtable ();
	Hashtable oCalculatorObjects = new Hashtable ();
	Hashtable oTypeBindings = new Hashtable ();

	JPanel selectorPanel;
	JPanel dataPanel;
	JPanel alertPanel;
	JLabel alertLabel;
	JComboBox calcType;
	String label;
		
	/* not really needed, b/c it's in (oCalculatorObjects) hash. but may as well */
	MCalculator object;

	public MCalculator_designer ( MCalculator iObject, String label )
	{
		this.label = label;

		// save local pointer
		object = iObject;
		// pull in the list of objects+ make a new GAccessor_ObjectInput for each
		buildData ( object );
		// construct the per-object widgets + containment gui
		buildGui( label );

		init();
	}


	public void reset() { init(); }


	
	private void buildData ( MCalculator iObject )
	{

		String sCalcName = null;

		/* set up the data hash */
		if ( iObject != null &&
			 ! iObject.getType().equals ( MCalculator.TYPE_STRING_UNKNOWN ) ) { 
			sCalcName = iObject.getType();
			oCalculatorObjects.put ( sCalcName, iObject );
		}

		if ( iObject == null ||
				! iObject.getType().equals ( MCalculator.TYPE_STRING_PARAMETRIC ) ) { 
			oCalculatorObjects.put ( MCalculator.TYPE_STRING_PARAMETRIC,  new MCalculator_Parametric( ) );
		}

		if ( iObject == null ||
				! iObject.getType().equals ( MCalculator.TYPE_STRING_EULER ) ) {
			oCalculatorObjects.put ( MCalculator.TYPE_STRING_EULER, new MCalculator_Euler ( ) );
		}

		/* - try RK2  */
		
		if ( iObject == null ||
				! iObject.getType().equals ( MCalculator.TYPE_STRING_RK2 ) ) {
			oCalculatorObjects.put ( MCalculator.TYPE_STRING_RK2, 
									new MCalculator_RK2 (  ) );
		}
		if ( iObject == null ||
				! iObject.getType().equals ( MCalculator.TYPE_STRING_RK4 ) ) {
			oCalculatorObjects.put ( MCalculator.TYPE_STRING_RK4, 
									new MCalculator_RK4 (  ) );
		}
		

		/* establish the combo-box string-> type bindings */
		oTypeBindings.put ( DISPLAY_PARAMETRIC, MCalculator.TYPE_STRING_PARAMETRIC );
		oTypeBindings.put ( DISPLAY_EULER, MCalculator.TYPE_STRING_EULER );
		oTypeBindings.put ( DISPLAY_RK2, MCalculator.TYPE_STRING_RK2 );
		oTypeBindings.put ( DISPLAY_RK4, MCalculator.TYPE_STRING_RK4 );
	}


	private void buildGui ( String label )
	{
		TitledBorder titleBorder = BorderFactory.createTitledBorder( "Calculator: " + label );
		titleBorder.setTitleJustification(TitledBorder.LEFT);
		setBorder(titleBorder);


		/* iterate thru the data hash, instantiating each 
		   designer widget. Keep Record of the tallest one
		   and set that to the preferred size of the datapanel. */
		Dimension dataPanelSize = new Dimension();
		Iterator i = oCalculatorObjects.keySet().iterator();

		while ( i.hasNext() ) {
			String sName = (String) i.next();
			
			MCalculator oCalc = (MCalculator) oCalculatorObjects.get ( sName );
			MCalculator_Abstract_subDesigner subDesigner = oCalc.getSubDesigner( label );
			
			if (subDesigner.getPreferredSize().height > dataPanelSize.height ) { 
				dataPanelSize.height = subDesigner.getPreferredSize().height;
			}
			if (subDesigner.getPreferredSize().width > dataPanelSize.width ) { 
				dataPanelSize.width = subDesigner.getPreferredSize().width;
			}
			oDesignerWidgets.put ( sName, subDesigner );
		}

		/*----- data panel selection --------*/
		dataPanel = new JPanel();		
		dataPanel.setLayout ( new BorderLayout());
		dataPanel.setPreferredSize(dataPanelSize);
		

		/* ----- the selector ---------*/
		calcType = new JComboBox();
		calcType.addItem (DISPLAY_PARAMETRIC);
		calcType.addItem (DISPLAY_EULER);
		calcType.addItem (DISPLAY_RK2);
		calcType.addItem (DISPLAY_RK4);

		calcType.addActionListener ( new MCalculator_designer_comboListener ( this,
																			  calcType ));
									
		selectorPanel = new JPanel();
		selectorPanel.setLayout ( new BorderLayout() );
		selectorPanel.add ( calcType, BorderLayout.NORTH );

		if(object instanceof MCalculator_Euler) {
		    IWPLog.info(this,"[MCalculator_designer] setting Euler as selected. calcType index 1: " + calcType.getItemAt(1) );
		    calcType.setSelectedIndex(1);
		}
		if(object instanceof MCalculator_RK2) {
		    calcType.setSelectedIndex(2);
		}
		if(object instanceof MCalculator_RK4) {
		    calcType.setSelectedIndex(3);
		}



		// 2007-Feb-02 brockman. New: An alert panel that displays errors + required symbols.
		alertPanel = new JPanel();
		alertLabel = new JLabel(" "); // must put some string in here, or else it zero heights.
		alertPanel.add(alertLabel);
		
		/* --- the main setup -------*/
		setLayout ( new BorderLayout() );
		add ( selectorPanel, BorderLayout.WEST );
		add ( dataPanel, BorderLayout.CENTER );
		add ( alertPanel, BorderLayout.SOUTH );
		

		// Set the size - now global for all types because I'm making this designer
		// more complicated.
		
		// what height do I need? The height we already have.
		Dimension size = new Dimension ( DObject_designer.WIDTH, getPreferredSize().height );
		setMaximumSize ( size );
		setMinimumSize ( size );
		setPreferredSize ( size );

		updateGui();

		/*
 		invalidate();
 		validate();              
 		repaint();
		*/
	}


	public void updateGui ( )
	{

		try { 

			String sSelected = (String) calcType.getSelectedItem();

			if ( sSelected == null ) { 
				IWPLog.info(this,"[MCalculator_designer] NULL Selected type from combo box!\n");
				return;
			}
	
			String sType = (String) oTypeBindings.get ( sSelected );
			if ( sType == null ) { 
				IWPLog.error(this,"[MCalculator_designer] NULL Binding for combox selection: '"+sSelected );
				return;
			}

			dataPanel.setVisible(false);	
			dataPanel.removeAll();
			/* dataPanel.setLayout ( new GridLayout ( 1, 1 )); */

			MCalculator_Abstract_subDesigner oAdd =
				(MCalculator_Abstract_subDesigner) oDesignerWidgets.get ( sType );
			if ( oAdd == null ) { 
				IWPLog.error(this,"[MCalculator_designer] NULL Widget Binding for Calculator type: "+ sType );
				return;

			} else { 
				dataPanel.add ( BorderLayout.NORTH, oAdd );
			}

			dataPanel.setVisible(true);
			dataPanel.repaint();
		
			repaint();
		} catch ( NullPointerException e ) { 
			IWPLog.x(this,"[MCalculator_designer](updateGUI) ERROR: NULL! ", e );
		}
	}


	/*--------------------------------------------------------------------*/
	/* give back a real copy of the object created in the GUI */
 	public MCalculator get ( )
 		throws InvalidEquationException
	{		
		try { 

			String sSelected = (String) calcType.getSelectedItem();

			if ( sSelected == null ) { 
				IWPLog.error(this,"[MCalculator_designer] NULL Selected type from combo box!\n");
				return null;
			}
			
			String sType = (String) oTypeBindings.get ( sSelected );
			
			if ( sType == null ) { 
				IWPLog.error(this,"[MCalculator_designer] NULL Binding for combox selection: '"+sSelected );
				return null;
			}

		    MCalculator goingBack=((MCalculator_Abstract_subDesigner)oDesignerWidgets.get(sType)).getCalculator();


			IWPLog.debug(this,"MCalculator_designer] sSelected: "+sSelected);
			IWPLog.debug(this,"MCalculator_designer] sType: "+sType);
			
			/* lookup the calculator + give it back */
			MCalculator goingOut = (MCalculator) (oCalculatorObjects.get(sType));
			
			if(goingBack==null) { IWPLog.error(this,"goingBack is null");}
			if(goingOut==null) { IWPLog.debug(this,"goingOut is null");}
			

			/*
			if(goingOut.getTypeString().equals("MCalculator_Parametric")) {
			    IWPLog.info(this,"MCalculator_designer] eq for Parametric: "+
					       ((MCalculator_Parametric)goingOut).getEquationString());
			} else {
			    IWPLog.info(this,"[MCalculator_designer] not Parametric calc");
			}
			*/
			//IWPLog.info(this,"[MCalculator_designer] calc type: "+goingOut.getTypeString());
			//return goingOut;
			return goingBack;
			//return (MCalculator) (oCalculatorObjects.get ( sType ));

		} catch (NullPointerException e) { 
			IWPLog.info(this,"[MCalculator_designer](get) ERROR: NULL! " + e );

			return null;
		}
	}


	public MCalculator getCalculator ( )
	{
		IWPLog.warn(this, "[MCalculator_designer] Warning: getCalculator called on MCalculator_designer class");
		return null;
	}

	public void init() { }
	
	
	/**
	 * Set the alert display
	 * @param message
	 * @param optionalBackgroundColor
	 */
	public void displayAlert(int level, String message)
	{
		
		if ( level == AlertListener.ERROR ) {
			alertLabel.setBackground(Color.ORANGE);
		} else if ( level == AlertListener.WARN ) { 
			alertLabel.setBackground(Color.YELLOW);
		} else {
			alertLabel.setBackground(Color.GRAY);
		}
		
		alertLabel.setText(message);
	}

	/**
	 * Set the alert display to nothing. Called on init.
	 *
	 */
	public void resetAlert()
	{
		displayAlert( AlertListener.INFO, " ");
	}

	
}



class MCalculator_designer_comboListener implements ActionListener
{
	MCalculator_designer oDesigner;
	JComboBox oComboBox;

	public MCalculator_designer_comboListener ( MCalculator_designer iDesigner,
												JComboBox iComboBox )
	{
		/* snag some incomings */
		oDesigner = iDesigner;
		oComboBox = iComboBox;
	}

	public void actionPerformed ( ActionEvent e ) { oDesigner.updateGui(); }
}

