// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
//
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.ui;

import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.exceptions.*;
import edu.ncssm.iwp.plugin.*;
import edu.ncssm.iwp.problemserver.client.*;
import edu.ncssm.iwp.util.buildversion.*;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import java.io.*;

import edu.ncssm.iwp.util.*;



/**
 * The main Swing frame component that houses the entirety of the
 * Designer aspect of IWP
 *
 * @author brockman 08.09.03
 */

public class GWindow_Designer extends GFrame
    implements ActionListener, WindowListener, ProblemListener
{
	private static final long serialVersionUID = 1L;
    public static int MODE_APPLICATION = 1;
    public static int MODE_APPLET = 2;

//    public static int WINDOW_WIDTH = 640;
	public static int WINDOW_WIDTH = 760; // 2008-Dec-25 widened the designer
    public static int WINDOW_HEIGHT = 640;


    DProblem_designer problemDesigner;
    DProblemManager manager;
    DProblem workingProblem;

    
    JList list;

    GAboutDialogue about = null; //moved to lazy loading first time it's needed.
    GFuncReferenceDialogue funcReference = null; //moved to lazy loading first time it's needed.

    JMenuBar menuBar = new JMenuBar();
    JMenuItem fileItem_new = new JMenuItem("New");
    JMenuItem fileItem_view = new JMenuItem("View");

    JMenu fileItem_save = new JMenu ("Save");
    JMenuItem fileItem_save_local = new JMenuItem("Local File");
    JMenuItem fileItem_save_ps = new JMenuItem("ProblemServer");

    
    JMenu fileItem_saveas = new JMenu ("Save As...");
    JMenuItem fileItem_saveas_local = new JMenuItem("Local File");
    JMenuItem fileItem_saveas_ps = new JMenuItem("ProblemServer");


    JMenu fileItem_open = new JMenu ("Open");
    JMenuItem fileItem_open_local = new JMenuItem("Local File");
    JMenuItem fileItem_open_ps = new JMenuItem("ProblemServer");
    JMenuItem fileItem_open_http = new JMenuItem("HTTP Url");
    JMenuItem fileItem_open_packaged = new JMenuItem("Packaged");

    JMenuItem fileItem_setAuthor = new JMenuItem("Set Author");

    JMenuItem fileItem_exit = new JMenuItem("Exit");

    JMenuItem[] objectButtons; // This is iniitalized in the constructor.

    JMenuItem objItem_delete = new JMenuItem("Delete");

    JMenuItem helpItem_about = new JMenuItem("About IWP");
    JMenuItem helpItem_funcReference = new JMenuItem("Function Reference");

    JPanel controlPanel;
    JPanel dataPanel;
    JPanel buttonPanel;

    JButton moveItemUp = new JButton("Move Up");
    JButton moveItemDown = new JButton("Move Down");


    // The widget needs some context as to wether it's running inside
    // of an applet, or an application.
    int mode = MODE_APPLICATION;

    // ProblemServer client integration. brock 08.09.03
    SwingXmlRpcClient psClient = null;

    // Visual pop up to allow the user to browse the packaged / companion files.
    GWindow_PackagedProblemBrowser packagedProblemBrowser = null;

    //----------------------------------------------------------------------


    GWindow_Animator animatorWindow = null; // a hook to the animator window brock 2005-oct-16

    /**
     * This is the real, final constructor for the designer. It takes in an
     * animator window to display it's 'view' action into.
     */
    public GWindow_Designer ( GWindow_Animator animatorWindow, int runMode )
    {
        this.mode = runMode;
        this.animatorWindow = animatorWindow;

        construct (true);
    }


    /*
     *This is a copy of other construct -- I know -- HORRID DESIGN
     */
    protected void construct ( boolean visible )
    {
        getContentPane().setLayout ( new BorderLayout() );

        addWindowListener(this);
        setTitle("IWP Designer (Version " + BuildVersion.VERSION + ")" );
        createMenu();
        setJMenuBar(menuBar);
        pack();

        setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        this.centerOnScreen();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        constructXmlRpc();

        manager = new DProblemManager();

        // setup a dummy problem @ end.
        designProblem ( new DProblem() );

        if(visible) {
            setVisible(true);
        } else {
            setVisible(false);
        }
    }


    private void constructXmlRpc ( )
    {
        // TODO: read in the username, password, and host from params.

        if ( psClient == null ) {
            // note: this may have already been initialized by the applet
            // invoker + parameter parser. brockman 2004.09.25
            psClient = new SwingXmlRpcClient ( this );
        } else {
            // leave the old one in existance
        }

        manager = new DProblemManager();
        // WARNING: This is called once already at a layer up, in 'construct';
    }



    public void createMenu ( )
    {
        // ---------------------------------------------- FILE MENU -------
        // --- setup the gui layout here ---------------------

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        fileMenu.add(fileItem_new);
        fileMenu.add(fileItem_view);
        fileMenu.add(fileItem_setAuthor);
        fileItem_open.add ( fileItem_open_local );
        //fileItem_open.add ( fileItem_open_ps );
        //fileItem_open.add ( fileItem_open_http );   // off for now
        fileItem_open.add ( fileItem_open_packaged );
        fileMenu.add(fileItem_open);
                
        fileItem_save.add ( fileItem_save_local );
        //fileItem_save.add ( fileItem_save_ps );
        fileMenu.add(fileItem_save);

        fileItem_saveas.add ( fileItem_saveas_local );
        //fileItem_saveas.add ( fileItem_saveas_ps );
        fileMenu.add(fileItem_saveas);
        fileMenu.add(fileItem_exit );

        // --- Bind the control keys here ----------------------
        
        // Ctrl + S Save, Ctrl + O Open
        // Thank you onjava: http://www.onjava.com/pub/a/onjava/excerpt/swing_14/index3.html
        fileItem_save_local.setAccelerator(KeyStroke.getKeyStroke('S',
        		Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), false));

        fileItem_open_local.setAccelerator(KeyStroke.getKeyStroke('O',
        		Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), false));

        fileItem_open_packaged.setAccelerator(KeyStroke.getKeyStroke('P',
        		Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), false));

        fileItem_view.setAccelerator(KeyStroke.getKeyStroke('A',
        		Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), false));
       
        // --- bind the action listeners here --------------------

        fileItem_new.addActionListener(this);
        fileItem_view.addActionListener(this);
        fileItem_setAuthor.addActionListener(this);
        fileItem_save_local.addActionListener(this);
        fileItem_save_ps.addActionListener(this);
        fileItem_saveas_local.addActionListener(this);
        fileItem_saveas_ps.addActionListener(this);

        fileItem_open_local.addActionListener(this);
        fileItem_open_ps.addActionListener(this);
        fileItem_open_http.addActionListener(this);
        fileItem_open_packaged.addActionListener(this);

        fileItem_exit.addActionListener(this);


        // ---------------------------------------------- OBJECT MENU -------

        JMenu objMenu = new JMenu("Objects");
        menuBar.add(objMenu);

        JMenu objItem_new = new JMenu("New");

        
        // IWP3 - The list of objects is now pluggable.
        String objectNames[] = IWPPluginFactory.getPluggedObjectNames();
        objectButtons = new JMenuItem[objectNames.length];
        for ( int i = 0; i < objectNames.length; i++ ) {
            objectButtons[i] = new JMenuItem(objectNames[i]);
            objectButtons[i].addActionListener(this);
            objItem_new.add(objectButtons[i]);
        }
        // jItem_new.add(objItem_new_reference);

        objItem_delete.addActionListener(this);


        objMenu.add(objItem_new);
        objMenu.add(objItem_delete);

        // -------------------------------------------------- HELP MENU -----

        JMenu helpMenu = new JMenu("Help");

        helpMenu.add(helpItem_about);
        helpMenu.add(helpItem_funcReference);
        helpItem_about.setEnabled(true);
        helpItem_funcReference.setEnabled(true);

        menuBar.add( Box.createHorizontalGlue() );
        menuBar.add(helpMenu);


        helpItem_about.addActionListener(this);
        helpItem_funcReference.addActionListener(this);


        // turn on the menu items based on applet / desktop mode
        if ( mode == MODE_APPLET ) {

            fileItem_save_local.setEnabled ( true );
            fileItem_saveas_local.setEnabled ( true );
            fileItem_open_local.setEnabled ( true );

        }

        invalidate();
    }


    public Hashtable getInputs() { return new Hashtable(); }

    /**
     * Return a handle to the problemserver cleint. this allows the
     * controlling program to set variables associated with it.
     * like: username, host, etc
     */
    public SwingXmlRpcClient getProblemServerClient ( )
    {
        return psClient;
    }

    /**
     * Create a new connection to a problemserver, based on the
     * parameterse that the controlling logic passes in. Useful
     * for IWP_AppletDesigner initialization.
     * @author brockman 10.04.03
     */
    public void setProblemServerParameters ( String host,
                         String username,
                         String password )
    {
        if ( psClient != null ) { psClient.close(); }

        psClient = new SwingXmlRpcClient ( host,
                                           username, password, this  );
    }




    //---------------------------------------------------------------------
    // ActionEvent handler

    public void actionPerformed(ActionEvent e)
    {

        if ((e.getSource()) instanceof JMenuItem){
            JMenuItem source = (JMenuItem)(e.getSource());

            try {
                // Pull the list of objects here. IWP3.

                // is it one of the new object buttons?
                for ( int i = 0; i < objectButtons.length; i++ ) {
                    // IWP3 - brockman 2007-Feb-02
                    if(source == objectButtons[i] ) {
                    	String className = IWPPluginFactory.findClassNameByObjectName(objectButtons[i].getText() );
                        problemDesigner.addNewObject( IWPPluginFactory.newInstanceOfObject(className) );
                    }
                }

            } catch ( NoPluginObjectX x ) {
                IWPLogPopup.x(this, "No Plugin Object: " + x.getMessage(), x);
            } catch ( NotIWPDesignedX x ) {
                IWPLogPopup.x(this, "No Designer for Object: " + x.getMessage(), x);
            }

            if (source == objItem_delete) {
               
            	// The confirmation dialog is handled internally to the designer inside this method
            	problemDesigner.deleteSelected();
            	
            //============ FILE OPENING ===============
            } else if (source == fileItem_open_http ) {

                IWPLog.debug(this,"opening http");
                DProblemManager_HttpOpen_gui g =
                    new DProblemManager_HttpOpen_gui ( manager, this );
                g.display();
                IWPLog.debug(this,"loaded from http");

            } else if (source == fileItem_open_ps ) {

                IWPLog.debug(this,"opening problemserver");
                psClient.showLoadProblemDialogue ( );

            } else if (source == fileItem_open_packaged ) {
                IWPLog.debug(this,"opening packaged dialog");

                // lazy loading.
                if ( packagedProblemBrowser == null ) { packagedProblemBrowser = new GWindow_PackagedProblemBrowser(this); }

                packagedProblemBrowser.openDialog();

            } else if (source == fileItem_open_local ) {

                try {
                    IWPLog.debug(this,"[GWindow_Designer] opening local");
                    
                    // I think this blocks the UI thread
                    DProblemManager_FileOpen_gui g = new DProblemManager_FileOpen_gui ( manager );
                    g.selectFile();
                    
                    // 2009-Jan-11 some sort of visual indicator that the problem is loading.
                    indicateProblemLoadingBegin(g.getSelectedFilename());
                    
                    DProblem prob = g.getProblem();
                    // brockman 2005-oct-16
                    designProblem ( prob );

                } catch ( DataStoreException dse ) {
                    IWPLogPopup.x(this,"ERROR: Unable to load file: " + dse.getMessage ( ), dse );
                }

            } else if ( source == fileItem_save_ps ) {

                // Do I have a current filename?
                psClient.runSave ( );

            //============ FILE SAVING ===============

            } else if (source == fileItem_save_local ) {

                if ( getProblem().hasValidFilename() ) {

                    try {
                        manager.save ( getProblem() );
                    } catch ( IrreversibleLoadTypeException ile ) {
                        handleSaveAs();
                    } catch ( DataStoreException dse ) {
                       IWPLogPopup.x(this,"ERROR: Unable to store file: " + dse.getMessage ( ), dse );
                    }
                } else {
                    // I don't have a filename yet. I have to save as. Save as sets the filename in.
                    handleSaveAs();
                }

            } else if (source == fileItem_saveas_ps ) {
                psClient.showSaveAsProblemDialogue ( );

            } else if (source == fileItem_saveas_local ) {
                handleSaveAs();

            } else if (source == fileItem_exit) {
                int really = JOptionPane.showConfirmDialog(null,
                                       "Really Exit IWP?","Exit IWP?",
                                       JOptionPane.YES_NO_OPTION);
                if(really==0) {exit ( );}

            } else if (source == fileItem_new) {

                int yesNo=JOptionPane.showConfirmDialog(null,
                        "Create new file? (Discarding unsaved changes)",
                        "New File?",
                        JOptionPane.YES_NO_OPTION);

                if(yesNo==JOptionPane.YES_OPTION) {
                    designProblem ( new DProblem ( ) );
                }

            } else if (source == fileItem_view) {

                playProblemInConnectedAnimator();

            } else if (source == helpItem_about) {
                if ( about == null ) { about = new GAboutDialogue( ); }
                about.setVisible(true);

            } else if (source == helpItem_funcReference) {
                if ( funcReference == null ) { funcReference = new GFuncReferenceDialogue( ); }
                funcReference.setVisible(true);

                
            } else if (source == fileItem_setAuthor) {
                /*
                JOptionPane pane = new JOptionPane("Set username to:",JOptionPane.OK_CANCEL_OPTION,
                                   JOptionPane.QUESTION_MESSAGE);
                JDialog dialog = pane.createDialog(null,"Set Author");
                dialog.show();
                IWPLog.info(this,"[GWindow_Designer] user: "+pane.getValue());
                String myInternalUsername = JOptionPane.showInputDialog(null,"Set username to:",
                                            workingProblem.getUsername());
                IWPLog.info(this,"[GWindow_Designer] user: "+myInternalUsername);
                workingProblem.setUsername(myInternalUsername);
                */
                IWPLog.debug(this,"set author");
                String myInternalUsername=JOptionPane.showInputDialog(null,"Set Author as:",workingProblem.getUsername());
                workingProblem.setUsername(myInternalUsername);
            }

        }

        getContentPane().invalidate();
        getContentPane().validate();
        getContentPane().repaint();

    }


    private void handleSaveAs()
    {
        DProblemManager_FileSave_gui g =
            new DProblemManager_FileSave_gui ( manager );

        if(g.selectFile()!=JFileChooser.APPROVE_OPTION){return;}

        String filename = g.getFilename();
        if(filename.equals("")) {return;}
        if(!g.getFileNameOnly().equals("")){
            setTitle("IWP Designer: "+g.getFileNameOnly());
        }
        try {
            // put the filename back to the problem.
            getProblem().setFilename(filename);
            manager.saveFile ( filename, getProblem() );
        } catch ( DataStoreException dse ) {
            // TOOD: this hsould po pup a window or something
            IWPLogPopup.x(this,"There was a problem saving your file: " + dse.getMessage ( ), dse );
        }

    }



    //----------------------------------------------------------------------
    /**
     * Reset the design instance, and set the problem data to-be-editied.
     * @author brockman 08.09.03
     */

    public void designProblem ( DProblem problem )
    {
        if ( problem == null ) { return; }
        
    	this.workingProblem = problem;
        // need to re-init the designer over this guy
        problemDesigner = workingProblem.getDesigner();
        problemDesigner.setParent(this);

        if ( workingProblem.hasValidFilename() ) {
            setTitle("IWP Designer: "+ workingProblem.getFilename() );
        } else {
            setTitle("IWP Designer (New Problem)");
        }

        getContentPane().removeAll ();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(BorderLayout.CENTER,problemDesigner); //shouldn't that resize?

        // is this really necessary? brockman 2006-Aug-15 yes.
        getContentPane().invalidate();
        getContentPane().validate();
    }


    /**
     * Analyze the graphical representation of the problem data the user
     * has been editing inside of the designer, and create a DProblem obj
     * out of the state
     *
     * @author brockman 08.09.03
     */

    public DProblem getProblem ( )
    {
        // we require problemDesigner to exist.
        DProblem returnProblem = problemDesigner.get();

        // copy some other stuff out of workingProblem (filename, etc)
        returnProblem.setFilename(workingProblem.getFilename());
        returnProblem.setUsername(workingProblem.getUsername());
        returnProblem.setAccessMode(workingProblem.getAccessMode());
        return returnProblem;
    }


    /**
     * Refactored this out to an obvious location. This is what sends the signal
     * to the animator to play a new problem,.
     *
     */
    public void playProblemInConnectedAnimator ()
    {
        // brockman 2005-oct-16. I'm redoing the relationship betweenthe animator +
        // the designer.
        // TODO: Should I clone the problem here?

        if ( animatorWindow == null ) {
            animatorWindow = new GWindow_Animator ( GWindow_Animator.MODE_DESIGNER_VIEW );
        }
        animatorWindow.setVisible(true);
        animatorWindow.viewProblem( getProblem() );
    }

    //--------------------------------------------------------------------
    // These are callbacks that are triggered by ProblemServer Client events

    // public void loadProblem ( DProblem problem )
    // loadProblem is not called designProblem
    public void loadProblem ( DProblem problem )
    {
        designProblem ( problem );
        
        // Title is reset.
    }

    public void saveProblem ( ProblemWriter output )
    {
        IWPLog.info(this,"Saving current Working file");
        output.writeProblem ( getProblem() );
    }

    public  void indicateProblemLoadingBegin(String filename) {
        setTitle("IWP Designer: Please Wait, Loading: " + filename );
    }
    
    

    //--------------------------------------------------------------------

    public void windowClosing(WindowEvent e)
    {

        int really = JOptionPane.showConfirmDialog(null,
                "Really Exit IWP?","Exit IWP?",
                JOptionPane.YES_NO_OPTION);

        if ( really == 0 ) {
            exit ( );
        } else {
            //
        }


        /*
          // sweeney added this for some reason.
          Sweeney's reply: We were using System.exit, which won't
          work for applets. So i did some testing code and got stuff
          to "die" anyway i coule (ie. hide it).
          //Oh - because it doesn't work.

        //pop up save dialog.
        try{
        IWPLog.info(this,"[GWindow_Designer] variable isApplet: "+isApplet);
        if(!isApplet) {
            IWPLog.info(this,"[GWindow_Designer][windowClosing] Exiting by System.exit(0)");
            System.exit(0);  {for grep'ers, this is in a comment}
        } else {
            IWPLog.info(this,"[GWindow_Designer][windowClosing] killing stuff");
            parent.stop();
            this.setVisible(false);
            workingProblem=null;
            manager=null;
        }
        } catch(java.security.AccessControlException oops) {
        oops.printStackTrace();
        IWPLog.info(this,"[GWindow_Designer][windowClosing] killing stuff");
        this.setVisible(false);
        workingProblem=null;
        manager=null;
        }
        */
    }

    public void windowClosed(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowOpened(WindowEvent e) {}


    /*---------------------------------------------------------------------*/

    /**
     * This CANNOT use System.exit() for applets!!!
     * That will kill the parent process (ie, the browswer)
     * It is valid for app mode, though.
     */
    private void exit ( )
    {
        if(animatorWindow!=null) {
            animatorWindow.applicationExit();
        }
        if ( mode == MODE_APPLICATION ) {
            System.exit(0);
        }
    }



}




class MyFilter extends javax.swing.filechooser.FileFilter {
    public boolean accept(File file) {

        String filename = file.getName().toUpperCase();
        return filename.endsWith(".IWP") ||
            filename.endsWith(".iwp") ||
            filename.endsWith(".XML") || // show xml files too.
            filename.endsWith(".xml") ||
            file.isDirectory();
    }
        public String getDescription() {
        return "Interactive Web Physics Problems";
    }
}
