
package edu.ncssm.iwp.problemdb;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.ncssm.iwp.exceptions.CannotLoadIconX;
import edu.ncssm.iwp.exceptions.MagicFileNotFoundX;
import edu.ncssm.iwp.exceptions.NoPluginObjectX;
import edu.ncssm.iwp.exceptions.NotIWPDesignedX;
import edu.ncssm.iwp.objects.DObject_Description_designer;
import edu.ncssm.iwp.objects.DObject_Time_designer;
import edu.ncssm.iwp.objects.DObject_Window_designer;
import edu.ncssm.iwp.objects.DObject_designer;
import edu.ncssm.iwp.plugin.IWPDesigned;
import edu.ncssm.iwp.plugin.IWPObject;
import edu.ncssm.iwp.plugin.IWPPluginFactory;
import edu.ncssm.iwp.ui.GAccessor_designer;
import edu.ncssm.iwp.ui.GIconSet;
import edu.ncssm.iwp.ui.GWindow_Designer;
import edu.ncssm.iwp.util.IWPLog;
import edu.ncssm.iwp.util.IWPLogPopup;
import edu.ncssm.iwp.util.IWPMagicFile;


/*--------------------------------------------------------------------------*/
/**
 * @author brockman
 * an internal class to seperate the gui controls from the data controls
 * passive class -- control exported to DProblem_designer
 *
 * IWP3: brockman modified the 'new' obejct to take a class instead of an int.
 *
 */

class DProblem_designer_gui extends JPanel implements ListSelectionListener, ActionListener
{
	public static final String SPLASH_IMAGE_SOURCE = "/images/IWPSplash.jpg";
	
	
    DProblem_designer oDesigner; /* a link back to the controlling class,
                                    to filter up GUI events */

    DefaultListModel oListModel; /* the viewing model for the list */

    JList oDesignerList; /* this is the object designer selector */
    JPanel oListPanel;   /* this is the list panel to the left hand side
                            w/ up/down control buttons + object list/tree */
    JPanel oDesignPanel; /* this is where object designers are told to
                            draw themselves */

    JPanel oButtonPanel; /* the panel to place the buttons that are used to
                            move things up/down in the tree */
    JButton oClone, oView, oUpButton, oDownButton, oNewButton, oDeleteButton;

    JPopupMenu oPopupMenu;  // this pops up when you press 'new'

    // IWP3 - these are populated based on the factory now.
    JMenuItem objectButtons[];
    GIconSet oIconSet;

    // shared unit lists
    DObject_designer objectDesignerInPane = null;
    Vector unitList = new Vector();


    /*-------------------------------------------------------------------------*/
    public DProblem_designer_gui ( DProblem_designer ioDesigner )
    {
        oDesigner = ioDesigner;

        // add init units
        unitList.add ( "m" );
        unitList.add ( "m/s" );
        unitList.add ( "degrees" );
        unitList.add ( "kg" );
        unitList.add ( "m/(s^2)");



        /* make the icon set */
        oIconSet = new GIconSet ( );

        /* LIST SETUP */
        oListModel = new DefaultListModel ( );
        oDesignerList = new JList ( oListModel );
        oDesignerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        oDesignerList.addListSelectionListener ( this );

        oDesignerList.setCellRenderer ( new DProblem_ObjectCellRenderer ( oDesignerList, oIconSet ));

        JScrollPane oDesignerPane =
            new JScrollPane ( oDesignerList,
                              JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                              JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        oDesignerPane.setMinimumSize(new Dimension(1,1));

        /* BUTTON SETUP */
        oNewButton = new JButton ( "New" );
        oNewButton.addActionListener ( this );

        oDeleteButton = new JButton ( "Delete" );
        oDeleteButton.addActionListener ( this );

        oUpButton = new JButton ( "Up" );
        oUpButton.addActionListener ( this );

        oDownButton = new JButton ( "Down" );
        oDownButton.addActionListener ( this );

        oView = new JButton("View");
        oView.addActionListener(this);

        oClone = new JButton("Clone");
        oClone.addActionListener(this);


        // IWP3 - the Icons are now dynamically pulled from the IWPPluginFactory
        // 2007-Feb-01
        oPopupMenu = new JPopupMenu ( "New" );

        String objectNames[] = IWPPluginFactory.getPluggedObjectNames();
        objectButtons = new JMenuItem[objectNames.length];
        for ( int i = 0; i < objectNames.length; i++ ) {
            objectButtons[i] = new JMenuItem(objectNames[i]);
            objectButtons[i].addActionListener(this);
            oPopupMenu.add(objectButtons[i]);
        }



        oButtonPanel = new JPanel ( );
        oButtonPanel.setLayout ( new GridLayout ( 3, 2 ));

        oButtonPanel.add(oClone);
        oButtonPanel.add(oView);
        oButtonPanel.add ( oNewButton );
        oButtonPanel.add ( oDeleteButton );
        oButtonPanel.add ( oUpButton );
        oButtonPanel.add ( oDownButton );

        /* LEFTSIDE LISTPANEL SETUP */
        oListPanel = new JPanel ( );
        oListPanel.setLayout ( new BorderLayout ( ) );
        oListPanel.add ( BorderLayout.CENTER, oDesignerPane);
        oListPanel.add ( BorderLayout.SOUTH, oButtonPanel );

        /* DESIGNER PANEL SETUP */
                
        oDesignPanel = new JPanel ( );  
        oDesignPanel.setLayout(new BorderLayout() );
        
        // 2007-Jun-03 Brockman was goofing around and decided to make the welcome screen prettier.
        
        
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BorderLayout());
        welcomePanel.setBorder(new EmptyBorder(10,10,10,10));

        // Set the splash image or the alt text.
        try { 
        	welcomePanel.add ( BorderLayout.NORTH, new JLabel(loadSplashImageIcon()) );
        } catch ( CannotLoadIconX x ) { 
        	welcomePanel.add ( BorderLayout.NORTH, new JLabel ( "Interactive Web Physics Designer", JLabel.CENTER ));
        }
        
        String middleText = "Try our example problems:   File, Open, Packaged";
        welcomePanel.add ( BorderLayout.CENTER, new JLabel(middleText, JLabel.CENTER ) );
 
        String bottomText = "For complete information, visit:     http://www.iwphys.org/";
        welcomePanel.add ( BorderLayout.SOUTH, new JLabel(bottomText, JLabel.CENTER ) );

        oDesignPanel.add(BorderLayout.CENTER,welcomePanel);

        
        
        /* set up myself */
        setLayout ( new BorderLayout ( ) );
        add ( BorderLayout.WEST, oListPanel );
        add ( BorderLayout.CENTER, new JScrollPane ( oDesignPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED ) );

    }

    /*-------------------------------------------------------------------------*/
    public void addDesigners ( Vector ioDesigners )
    {
        oDesignerList.setListData ( ioDesigners );
        if ( objectDesignerInPane != null ) {
            unitList = objectDesignerInPane.getUnitList();
        }
        for(int i=0;i<ioDesigners.size();i++) {
            ((DObject_designer)ioDesigners.elementAt(i)).setUnitList(unitList);
        }

    }


    public DObject_designer getSelectedDesigner ( )
    {
        return ( DObject_designer ) oDesignerList.getSelectedValue();
    }

    public void selectDesigner ( DObject_designer designer )
    {
        oDesignerList.setSelectedValue ( designer, true );
        // show this guy too?
        editDesigner ( designer );
    }



    /**
     * Call this to edit a different designer
     *
     */

    public void editDesigner ( DObject_designer designer )
    {

        if ( objectDesignerInPane != null ) {
            unitList = objectDesignerInPane.getUnitList();
        }


        if ( designer != null ) {

            designer.setUnitList ( unitList );
            objectDesignerInPane = designer;

            oDesignPanel.removeAll();
            oDesignPanel.add ("Center", designer );


        }

        oDesignPanel.invalidate();
        oDesignPanel.validate();
        oDesignPanel.repaint();

        // do me too. - this gets the scollbar to jump
        invalidate();
        validate();
        repaint();
    }



    /*-------------------------------------------------------------------------*/
    /* INTERFACE: ListSelectionListener */
    /* these will filter up the user action to the data layer via oDesigner */




    public void valueChanged ( ListSelectionEvent evt )
    {
        /* which item is selected? */
        /* display that item's edit widget in the editor pane */

        if ( ! evt.getValueIsAdjusting() ) {

            DObject_designer object_designer =
                (DObject_designer) oDesignerList.getSelectedValue();
            //IWPLog.info(this,"[DProblem_designer] setting units");

            editDesigner ( object_designer );
        }

    }


    /* INTERFACE: ActionListener */
    public void actionPerformed ( ActionEvent evt ) {

        /* determine which item is selected */
        DObject_designer oSelectedDesigner = (DObject_designer) oDesignerList.getSelectedValue();

        /* move the item up or down */



        if ( evt.getSource() instanceof JButton ) {

            if ( oSelectedDesigner != null && evt.getSource() == oUpButton ) {
                oDesigner.upDesigner ( oSelectedDesigner );

            } else if ( oSelectedDesigner != null && evt.getSource() == oDownButton ) {
                oDesigner.downDesigner ( oSelectedDesigner );

            } else if ( oSelectedDesigner != null &&
                        evt.getSource() == oDeleteButton ) {
                oDesigner.deleteDesigner ( oSelectedDesigner );

            } else if ( evt.getSource() == oNewButton ) {

                // make a new JPopupMenu()
                oPopupMenu.show ( (Component)evt.getSource(), oNewButton.getSize().width, 0  );

                //oDesigner.newDesigner ( );
            } else if (evt.getSource() == oView) {
                oDesigner.viewProblem();
            } else if (evt.getSource() == oClone) {
                oDesigner.cloneObject ( oSelectedDesigner );

            } else {
                /* action didn't satisfy any criteria */
            }
        } else if ( evt.getSource() instanceof JMenuItem ) {

            try {

                // is it one of the new object buttons?
                for ( int i = 0; i < objectButtons.length; i++ ) {
                    // IWP3 - brockman 2007-Feb-02
                    if(evt.getSource() == objectButtons[i] ) {
                        String className = IWPPluginFactory.findClassNameByObjectName(objectButtons[i].getText() );
                        oDesigner.addNewObject( IWPPluginFactory.newInstanceOfObject(className) );
                    }
                }
            } catch ( NoPluginObjectX x ) {
                IWPLogPopup.x(this, "No Plugin Object: " + x.getMessage(), x);
            } catch ( NotIWPDesignedX x ) {
                IWPLogPopup.x(this, "IllegalAccessException: " + x.getMessage(), x);
            }
        }

    }
    
    


    /**
     * Image handling routines for loading the splash image on the initial blank canvas.
     * @return
     */
    private ImageIcon loadSplashImageIcon()
    	throws CannotLoadIconX
    {
    	String source = SPLASH_IMAGE_SOURCE;
    	
    	try { 
    		IWPMagicFile file = new IWPMagicFile(source);
        	MediaTracker m = new MediaTracker(new JPanel());
    		Image img = Toolkit.getDefaultToolkit().createImage(file.readBytes());
    		m.addImage(img,0);
    		m.waitForAll();
        		
    		return new ImageIcon(img);
    	}
        catch ( MagicFileNotFoundX e ) {
        	IWPLog.x("Magic File Not Found. Can't load icon: " + source, e);
        	throw new CannotLoadIconX(source, e);
        }
        catch ( InterruptedException e ) { 
        	IWPLog.x("Interrupted Exception. Can't load icon: " + source, e);
        	throw new CannotLoadIconX(source, e);
        }
    }

}




/*--------------------------------------------------------------------------*/
/* THE REAL CLASS */
/*--------------------------------------------------------------------------*/

/**
 * Accessor / Designer code design
 * @author brockman
 * All Designers are going to have a function to 'get' back the same
 * type of object that they were constructed w/. I've found that in
 * OO design, there is no way to effectively transparently update an
 * object easily (mainly casting b/t different types).
 *
 * So,,, the when upper-level control requires output from the designers,
 * they're going to have to explicitilly call 'get'. The only one to
 * really worry about will be DProblem_designer.get ( DProblem ).. the
 * calls will cascade, and out will pop the new problem.... not transparently
 * as before in the brain!
 *
 * (added 06/23/01)
 * Also,, all the designer information can exist in 'gui' form for editing.
 * It only needs to write it's info when DProblem_designer.get is called..
 * the get can cascade --- so that means that I don't need to tell the
 * object_desginer to sync themselves down when they loose focus.
 *
 *
 *
 * DProblem_designer
 * brockman 04/28/01
 *
 * This is the top-level designer class for problems... it instantiates
 * the cascae of designers AND has the toplevel 'get' function
 *
 * (A lot of this code is going to come from GAccessor_designer) !
 */

public class DProblem_designer extends GAccessor_designer
{
    /* vars */
    DProblem oProblem;
    DProblem_designer_gui oGui;
    Vector oObjectDesigners = new Vector ( );

    String sUserName;
    String sFileName;

    GWindow_Designer parent;

    /*----------------------------------------------------------------------*/

    public DProblem_designer ( DProblem ioProblem )
    {
        oProblem = ioProblem;

        /* load the problem into the gui */
        _loadProblem ( );

        /* temp holders -- later should add widgets to change the problem
           name , etc */
        sUserName = ioProblem.getUsername();
        sFileName = ioProblem.getFilename();

        oGui = new DProblem_designer_gui ( this );

        /* add data into the gui */
        oGui.addDesigners ( oObjectDesigners );

        /* display my graphics */
        setLayout ( new BorderLayout ( ) );
        add ( BorderLayout.CENTER, oGui );

    }


    private void _loadProblem ( )
    {
        /* add each object in the problem to the dynamic list */

        Collection objects = oProblem.getObjectsForDrawing ( );
        Iterator i = objects.iterator();

        while ( i.hasNext() ) {

            IWPObject o = (IWPObject) i.next();

            if ( o instanceof IWPDesigned ) {
                DObject_designer tmp=(DObject_designer)((IWPDesigned)o).getDesigner();
                tmp.setParentProblemDesigner(this);
                //oObjectDesigners.add ( o.getDesigner() );
                oObjectDesigners.add(tmp);
            } else {
                IWPLog.warn(this, "The object class: "+ o.getClass().getName() + " does not implement IWPDesigned.");
            }
        }

    }

    public DProblem_designer_gui getOGui() {return this.oGui;}
    public void setParent(GWindow_Designer in) {
    parent=in;
    }

    public DProblem get (  )
    {
        /* make a new DProblem object */
        DProblem oReturn = new DProblem ( sUserName, sFileName );

        /* add the objects from the designers in the list */
        for ( int i=0; i < oObjectDesigners.size(); i++ ) {

            /* call gets on the object designers */
            DObject_designer oObjectDesigner = (DObject_designer) oObjectDesigners.get ( i );

            try {
                oReturn.addObject ( oObjectDesigner.buildObjectFromDesigner ( ) );
            } catch ( Exception x ) {
                handleException(x);
            }
        }

        return oReturn;
    }


    public void handleException ( Exception e )
    {
        IWPLogPopup.x (this, e.getMessage(), e );
    }



    /*------------------------------------------------------------------*/
    /* Accessors */
    /*------------------------------------------------------------------*/

    public void addDesigner ( GAccessor_designer genericDesigner )
    {
        DObject_designer ioDesigner = (DObject_designer) genericDesigner;

        if ( ioDesigner == null ) {
            IWPLog.error(this,"[DProb_des](addDes) ioDes is null");
            return;
        }

        oObjectDesigners.add ( ioDesigner );

        // add this to the list!
        oGui.addDesigners ( oObjectDesigners );
        // edit this guy!
        oGui.selectDesigner ( ioDesigner );
        //set the top-level designer for access

        ioDesigner.setParentProblemDesigner(this);
        ioDesigner.finalize();

    }

    public void deleteDesigner ( DObject_designer ioDesigner )
    {
        if ( ioDesigner == null ) { return; }
        if ( ioDesigner instanceof DObject_Window_designer ||
             ioDesigner instanceof DObject_Time_designer ||
             ioDesigner instanceof DObject_Description_designer ) {
        } else {

        	// 2007-Jun-06 brockman - add delete object confirmation popup
        	int really = JOptionPane.showConfirmDialog(null,
                    "Really delete " + ioDesigner.getName() + " ?\nThere is no Undo.", "Delete Object?",
                    JOptionPane.YES_NO_OPTION);

        	if(really==0) {
        	
        		int d = oObjectDesigners.indexOf ( ioDesigner );
        		oObjectDesigners.remove ( ioDesigner );
        		oGui.addDesigners ( oObjectDesigners );

        		if ( d >= oObjectDesigners.size() ) {
        			d = oObjectDesigners.size() - 1;
        		}

        		DObject_designer prev = (DObject_designer) oObjectDesigners.get ( d );
        		oGui.selectDesigner ( prev );
        	}

        }
    }


    public void downDesigner ( DObject_designer ioDesigner )
    {
        if ( ioDesigner == null ) { return; }
        moveDesigner ( ioDesigner, false );
    }

    public void upDesigner ( DObject_designer ioDesigner )
    {
        if ( ioDesigner == null ) { return; }
        moveDesigner ( ioDesigner, true );
    }

    public void moveDesigner ( DObject_designer ioDesigner, boolean up )
    {
        if ( ioDesigner == null ) { return; }
        // move the designers.

        // find it, move it's index


        int d = oObjectDesigners.indexOf ( ioDesigner );

        // bounds checking
        if ( ( d <= 0 && up ) ||
             ( d >= oObjectDesigners.size() - 1 && ! up ) ) { return; }

        //IWPLog.info(this,"[DProblem_designer][moveDesigner] Old index: " + d );
        // take it out...
        DObject_designer o = (DObject_designer) oObjectDesigners.remove ( d );
        if ( up ) { d--; } else { d++; }

        oObjectDesigners.insertElementAt ( o, d );
        oGui.addDesigners ( oObjectDesigners );
        oGui.selectDesigner ( o );

    }
    /**
     * This is intended to refresh the name of the designer
     * by removing it and adding it back to the designer list.
     * This is inteded to be called by the designer itself,
     * after detecting that its name has changed.
     * Sweeneyb 7/10/03
     *
     * @param ioDesigner designer that will get refreshed
     *
     * @see edu.ncssm.iwp.object.DObject_designer
     * @see edu.ncssm.iwp.object.DObject_Input_designer
    */
    public void refreshDesigner(DObject_designer ioDesigner)
    {

        if ( ioDesigner == null ) { return; }

        // find it's index
        int d = oObjectDesigners.indexOf ( ioDesigner );
        IWPLog.debug(this,"[DProblem_designer] refreshing index"+d);
        //  take it out...
        DObject_designer o = (DObject_designer) oObjectDesigners.remove ( d );
        //deleteDesigner(ioDesigner);
        oObjectDesigners.insertElementAt ( o, d );
        oGui.addDesigners ( oObjectDesigners );
        oGui.selectDesigner ( o );
    }

    public DObject_designer getSelected ( )
    {
        // figure out which one is selected!
        return oGui.getSelectedDesigner ( );
    }


    public void deleteSelected ( )
    {
        DObject_designer s = getSelected ();
        if ( s != null ) {
        	deleteDesigner ( s );       	
        }
    }


    /**
     * Add a new designable object to the designer list. The calling class takes care
     * of the instantiation. (which can be done easily by using IWPPluginFactory).
     *
     * @param objectType
     */
    public void addNewObject ( IWPObject newObject )
        throws NotIWPDesignedX
    {
        if ( newObject instanceof IWPDesigned ) {
            addDesigner ( (DObject_designer) ((IWPDesigned)newObject).getDesigner() );
        } else {
            throw new NotIWPDesignedX("Not instance of IWPDesigned: " + newObject.getClass().getName() );
        }
    }


    /**
     * Does this really still work?
     *
     * @param ioDesigner
     */
    public void cloneObject( DObject_designer ioDesigner )
    {
        if ( ioDesigner == null ) { return; }

        if ( ioDesigner instanceof DObject_Window_designer ||
                ioDesigner instanceof DObject_Time_designer ||
                ioDesigner instanceof DObject_Description_designer ) {
            IWPLogPopup.info("Only one copy of " + ioDesigner.getName() + " allowed in problem");

        } else {

            try {
                IWPDesigned copy = (IWPDesigned) ioDesigner.buildObjectFromDesigner();
                // 2007-may-22 brockman : clone fix. Cloned objects should have a different name.
                copy.setName(copy.getName() + "Clone");
                addDesigner(copy.getDesigner());
            } catch ( Exception x ) {
                handleException(x);
            }
        }

    }


    public void viewProblem() {
        parent.playProblemInConnectedAnimator();
    }

    
    
    public void iwpRepaint()
    {
		System.err.println("[DProblem_designer] iwpRepaint invoked!");
        invalidate();
        validate();
        repaint();
		
    }

}




/*--------------------------------------------------------------------------*/
/*
  internal class for rendering JList cells
*/

class DProblem_ObjectCellRenderer extends DefaultListCellRenderer {

    GIconSet iconSet;

    public DProblem_ObjectCellRenderer ( JList list, GIconSet iIconSet )
    {
        super();
        iconSet = iIconSet;
    }


    public Component getListCellRendererComponent ( JList list,
                                                    Object value,
                                                    int modelIndex,
                                                    boolean isSelected,
                                                    boolean cellHasFocus )
    {
        DObject_designer designer = (DObject_designer) value;
        String text;
        ImageIcon icon;

        // IWPLog.info(this,"[ObjectCellRenderer] getListCellRenderComponent called: "+designer.getName() );

        if ( isSelected ) {
            text = "" + designer.getName() + "";
        } else {
            text = "" + designer.getName() + "";
        }



        DefaultListCellRenderer out = ( DefaultListCellRenderer )
            super.getListCellRendererComponent (list, text,
                                                modelIndex,
                                                isSelected,
                                                cellHasFocus );


        /* set the icon - IWP 3 now loads the items gracefully */
        try {
            IWPObject object = designer.buildObjectFromDesigner();
            icon = iconSet.getObjectIcon ( object );

            if ( icon != null ) {
                out.setIcon ( icon );
            }
            else {
                IWPLog.info(this,"[DProblem_designer] Icon is NULL: "+designer.getName() );
            }
        } catch ( Exception e) {
            IWPLogPopup.x(this, e.getMessage(), e );
        }

        return (Component) out;
    }
}
