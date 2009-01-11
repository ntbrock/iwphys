// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
//
// For full copyright information, see main source file,
// "iwp.java"
//
// 2005-Oct-16 back again. We're redoing the parameters to the applet
// to let it display inline or in a popup window.

package edu.ncssm.iwp.bin;

import javax.swing.JOptionPane;
import javax.swing.JLabel;
import java.awt.BorderLayout;

import edu.ncssm.iwp.ui.*;
import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.exceptions.*;
import edu.ncssm.iwp.util.*;


public class IWP_Applet extends java.applet.Applet
{
	private static final long serialVersionUID = 1L;

    public static GWindow_Animator animator;
    public static GWindow_Designer designer;


    public static final boolean DEBUG = false;

    public static final String PARAMETER_PROBLEM = "problem";
    public static final String PARAMETER_STUDENT = "student"; // legacy arg

    // 2006-Aug-30 brockman. New, specific arguments that specifiy that a
    // problem should be loaded from http or from the packaged problems.
    public static final String PARAMETER_PACKAGED_PROBLEM = "packagedProblem";
    public static final String PARAMETER_HTTP_PROBLEM = "httpProblem";


    // The display mode for the animator. Show it embedded in the applet, or popup a new window?
    public static final String PARAMETER_INLINE_ANIMATOR = "inlineAnimator";
    public static final String PARAMETER_INLINE_DESIGNER = "inlineDesigner";
    public static final String PARAMETER_WINDOW_ANIMATOR = "windowAnimator";
    public static final String PARAMETER_WINDOW_DESIGNER = "windowDesigner";

    public static final String PARAMETER_PROBLEMSERVER_HOST = "ps_host";
    public static final String PARAMETER_PROBLEMSERVER_USERNAME = "ps_username";
    public static final String PARAMETER_PROBLEMSERVER_PASSWORD = "ps_password";

    // default display config.
    boolean inlineDesigner = false;
    boolean inlineAnimator = true;
    boolean windowDesigner = true; // a window designer automatically pops up a new window for the
       // animator. You probably dont want to use windowAnimator and windowDesigner in combination.
    boolean windowAnimator = false;


    public void init()
    {}

    public void start()
    {

        // legacy student parameter.
        // check the 'student' parameter, if set to any value, set showDesigner = false.
        if ( getParameter( PARAMETER_STUDENT ) != null) {
            inlineDesigner = false;
            inlineAnimator = false;
            windowDesigner = false;
            windowAnimator = true;
        }

        // check other parameters for window types.
        if ( getParameter ( PARAMETER_WINDOW_DESIGNER ) != null ) { windowDesigner = true; }
        if ( getParameter ( PARAMETER_WINDOW_ANIMATOR ) != null ) { windowAnimator = true; }
        if ( getParameter ( PARAMETER_INLINE_DESIGNER ) != null ) { inlineDesigner = true; }
        if ( getParameter ( PARAMETER_INLINE_ANIMATOR ) != null ) { inlineAnimator = true; }



        if ( inlineAnimator ) {
            animator = new GWindow_Animator( this );
        }

        if ( windowAnimator ) {
            animator = new GWindow_Animator ( GWindow_Animator.MODE_APPLET );
            //setup the display of the applet
            showAppletText();
        }

        if ( inlineDesigner ) {
            // not supported yet.
            JOptionPane.showMessageDialog(null,
                    "Error: Inline designer not supported yet", "Not Supported Parameter", JOptionPane.ERROR_MESSAGE);
        }

        if ( windowDesigner ) {
            designer =
                new GWindow_Designer ( animator, GWindow_Designer.MODE_APPLET );
            /**
             * This simulates the button pressing of the 'view'.
             */
            designer.toFront();
            designer.playProblemInConnectedAnimator();
            designer.toFront();
        }

        // fill in the parameters for the problem server. This sets them into the
        // UI for the problemserver file browser.
        if ( designer != null ) {
            problemServerConnectInit ( designer );
        }


        DProblem problem = loadParameterProblem();
        if ( animator != null ) { animator.viewProblem ( problem ); }
        if ( designer != null ) { designer.designProblem( problem ); }

    }


    public void stop()
    {
        IWPLog.debug ( this, "stop() called");
        // The re should clean me up.
    }

    private void problemServerConnectInit ( GWindow_Designer designer )
    {
        String host = getParameter ( PARAMETER_PROBLEMSERVER_HOST );
        String username = getParameter ( PARAMETER_PROBLEMSERVER_USERNAME );
        String password = getParameter ( PARAMETER_PROBLEMSERVER_PASSWORD );

        IWPLog.info ( this, "[IWP_DesignApplet.problemServerConnectInit] host: '" + host + "'  username: '" + username + "'" );

        // password is optional
        if ( password == null ) { password = ""; }

        if ( host != null &&
             username != null ) {
            designer.setProblemServerParameters ( host,
                                                  username,
                                                  password );
        }
    }


    private void showAppletText()
    {
        setLayout ( new BorderLayout() );

		try { 
			add ( BorderLayout.CENTER, new JLabel( MagicImageIconLoader.loadIcon ( "/images/IWPApplet.png") ));
		} catch ( CannotLoadIconX x ) { 
	        add ( BorderLayout.NORTH, new JLabel ("Interactive Web Physics") );
	        add ( BorderLayout.SOUTH, new JLabel ("A new window should pop up") );
		}
		
	}


    /**
     * Not ok, must return a blank problem;
     *
     * @return
     */
    private DProblem loadParameterProblem()
    {

        DProblem problemFromParams = null;


        // Is there a problem url passed as a parameter?
        if ( getParameter( PARAMETER_PROBLEM ) != null ) {
            problemFromParams = loadHttpProblem(getParameter(PARAMETER_PROBLEM));
        } else if ( getParameter( PARAMETER_HTTP_PROBLEM ) != null ) {
            problemFromParams = loadHttpProblem(getParameter(PARAMETER_HTTP_PROBLEM));
        } else if ( getParameter( PARAMETER_PACKAGED_PROBLEM ) != null ) {
            problemFromParams = loadPackagedProblem(getParameter(PARAMETER_PACKAGED_PROBLEM));
        }

        if ( problemFromParams == null ) {
            problemFromParams = new DProblem ("default", "default");
        }

        return problemFromParams;
    }


    /**
     * Ok to return null on error.
     * @param url
     * @return
     */

    private DProblem loadHttpProblem (String url)
    {

        try {
            DProblemManager_HTTP probManager = new DProblemManager_HTTP();
            return probManager.loadProblem ( getParameter ( PARAMETER_PROBLEM ) );
        }
        catch ( Exception e ) {
            // any exception will cause a default problem to be loaded.
            JOptionPane.showMessageDialog(null,
            "Error: " + e.getClass().getName() + "\n" + e.getMessage() + "\nUsing blank problem.", "Error in loading Problem", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }



    /**
     * Ok to return null on error.
     * @param url
     * @return
     */

    private DProblem loadPackagedProblem (String magicFilename)
    {

        try {
            DProblemManager_Magic probManager = new DProblemManager_Magic();
            return probManager.loadProblem( magicFilename );
        }
        catch ( Exception e ) {
            // any exception will cause a default problem to be loaded.
            JOptionPane.showMessageDialog(null,
            "Error: " + e.getClass().getName() + "\n" + e.getMessage() + "\nUsing blank problem.", "Error in loading Problem", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }


}




