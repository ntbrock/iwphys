package edu.ncssm.iwp.problemserver.client;

import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;


import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.exceptions.*;
import edu.ncssm.iwp.ui.GFrame;

import edu.ncssm.iwp.util.*;


/**
 * This is the Swing wrapper for the Applet implementation of the
 * ProblemServer client.
 *
 * DONE: Make directory browsing work. '.' should refresh.
 * DONE: Delete
 * TODO: Make exception handling throw pop-ups / Disconnect on every error
 * TODO: Integrate with IWP Applet
 *
 * BUGBUG: Network access is slow - it's bad tha tht eserver has to
 *         reparse every problem a few times to display a directory.
 * BUGBUG: The Load + Save panes operate independently of each other,
 * so refreshing is cruical.
 *
 * @author brockman 08.08.03
 */

public class SwingXmlRpcClient implements ProblemWriter, ProblemServerClient
{
    // ---- MEMBERS -----------------------------------------------------
    private ConnectionInfo connInfo = new ConnectionInfo ();
    private AppletXmlRpcClient clientHandle = null;
    private boolean isConnected = false;
    private String errorMessage = null;
    private MasterWindow masterWindow = null;
    private ProblemListener problemListener = null;

    private Collection connectionEventListeners = new ArrayList();

    private DProblem callbackProblem;
    private String currentDirectory;

    // ---- CONSTRUCTORS ------------------------------------------------

    /**
     * Default constructor. This will pre-populate the host, pass, user
     * fields with blank values
     *
     * @author brockman 08.08.03
     */
    public SwingXmlRpcClient ( ProblemListener problemListener )
    {
        this.problemListener = problemListener;
        constructGui ( );
    }


    /**
     * Pre-population constructor. The values supplied will be used to
     * pre-populate the host, pass, and user fields
     *
     * @author brockman 08.08.03
     */
    public SwingXmlRpcClient ( String host,
                               String username,
                               String password,
                               ProblemListener problemListener )
    {
        this.problemListener = problemListener;

        connInfo.host = host;
        connInfo.username = username;
        connInfo.password = password;

        openConnection();

        constructGui();
    }

    //--- GUI ---------------------------------------------------------
    /**
     * Make the window visible, showign the load Problem dialogue. If
     * the client is not connected, the setup dialogue will be shown.
     */
    public void showLoadProblemDialogue ( )
    {
        if ( isConnected() ) {
            masterWindow.raiseLoad();
        }
        else {
            masterWindow.raiseSetup ( );
        }
    }


    public void showSaveAsProblemDialogue ( )
    {
        if ( isConnected() ) {
            masterWindow.raiseSave();
        }
        else {
            masterWindow.raiseSetup ( );
        }
    }


    public void runSave (  )
    {
        // NOT DONE
        // nothign graphical to do here

        showSaveAsProblemDialogue ( );

    }

    public void showSetupProblemDialogue ( )
    {
        masterWindow.raiseSetup();
    }



    //--- Client Management -------------------------------------------


    /**
     * Establish a client connection to the server. This will be trigged
     * when a button is pressed in the UI
     *
     * @author brockman 08.08.03
     */

    public void openConnection ( )
    {
        try {

            debug ( "openConnection", "connecting to: " + connInfo.host );
            clientHandle = new AppletXmlRpcClient ( connInfo.host,
                                                    connInfo.username,
                                                    connInfo.password );
            isConnected = true;
            errorMessage = "Connected to Server";

        } catch ( Exception e ) {
            handleException ( e );
        }


        if ( isConnected ) {

            // some form of success here
            notifyListeners ( );
            // start directory is homeDir
            setCurrentDirectory ( getHomeDirectory ( ) );

            // The gui might not have been constructed yet.
            if ( masterWindow != null ) { masterWindow.refreshFileViews(); }
        } else {

            debug ( "openConnection", "Connection Error: " + errorMessage );

        }

    }

    /**
     * The directory that the user will always start in when he
     * connects.
     *
     * Brockman updated 09.25.04 to refelct new-style naming
     */
    private String getHomeDirectory ( )
    {
        return "/Users/" + connInfo.username + "/";
    }


    /**
     * Cleanup + close the connection to the server. This should be done
     * before destroying the client.
     *
     * @author brockman 08.08.03
     */

    public void closeConnection ( )
    {
        if ( isConnected ) {

            clientHandle.close();
            isConnected = false;
            errorMessage = "Disconnected From Server";

            notifyListeners ( );
        }
    }


    /**
     * This satistfies the generic ProblemServer client interface
     * @author brokcman 10.04.03
     */
    public void close ( )
    {
        closeConnection();
    }

    //----- CONNECTION EVENT LISTENERS -----------------------------------

    /**
     * You can hook an event listener to this class. If you add the listener,
     * he will get sent a method call everytime the client cahnges connection
     * state to the server.
     *
     * @author brockman 08.08.03
     */
    public void addConnectionEventListener ( ConnectionEventListener listener )
    {
        connectionEventListeners.add ( listener );
    }

    /**
     * Send a notice of the current client state (this is called after a
     * change only) to all listening handlers.
     *
     * @author brockman 08.08.03
     */
    private void notifyListeners ( )
    {
        for ( Iterator i = connectionEventListeners.iterator();
              i.hasNext(); ) {

            ConnectionEventListener listener = (ConnectionEventListener) i.next();
            if ( isConnected ( ) ) {
                listener.clientConnected ( );
            } else {
                listener.clientDisconnected ( );
            }
        }
    }


    /**
     * Return true if the client is connected validly to the server
     */
    public boolean isConnected ( ) { return isConnected; }

    public String getErrorMessage ( ) { return errorMessage; }

    public ConnectionInfo getConnectionInfo ( ) { return connInfo; }

    public void setConnectionInfo ( ConnectionInfo conn )
    {
        this.connInfo = conn;
    }


    public AppletXmlRpcClient handle ( )
    {
        return clientHandle;
    }


    /**
     * Follow a relative filename link and reset the directory
     * appropriately, doing path cannonization
     *
     * @author brockman 08.09.03
     */

    public void changeDirectory ( String filename )
    {
        String relativeDir = filename.substring ( 1, filename.length() - 1 );

        debug ( "changeDirectory", "relativeDir: '" + relativeDir + "'" );

        if ( relativeDir.startsWith ( "/" ) ) {
            setCurrentDirectory ( relativeDir );

        } else if ( relativeDir.equals ( ".." ) ) {
            setCurrentDirectory ( chopOffOneFolderInPath ( getCurrentDirectory() ) );
        } else if ( relativeDir.equals ( "." ) ) {
            // do nothing
        } else {
            setCurrentDirectory ( beginSlash ( slashEnd ( noSlashEnd ( getCurrentDirectory() ) + "/" + relativeDir ) ) );

        }

        debug ( "changeDirecotry", "new Current Directory: " + getCurrentDirectory() );

    }



    public void setCurrentDirectory ( String newDir )
    {
        // safety catch.x
        if ( newDir.equals("") ) {

            newDir = "/";

        }

        debug ( "setCurrentDirectory", newDir );

        this.currentDirectory = newDir;
    }


    public String getCurrentDirectory ( )
    {
        return currentDirectory;
    }


    //----- GUI -----------------------------------------------------------
    /**
     * Construct the Graphical Swing elements
     */
    
    public static final int WINDOW_WIDTH = 640;
    public static final int WINDOW_HEIGHT = 480;
    
    private void constructGui ( )
    {
        try {

            masterWindow = new MasterWindow ( this );
            addConnectionEventListener ( masterWindow );

            masterWindow.setSize ( WINDOW_WIDTH, WINDOW_HEIGHT );
            masterWindow.setTitle ( "IWP ProblemServer Client" );
            masterWindow.centerOnScreen();

            // this is not set as visible from the beginning. The
            // 'show' calls are what actually make it show up .
            // masterWindow.setVisible ( true );
        }
        catch ( Exception e ) {
            handleException ( e );
        }

    }

    //----- FILE OPS HANDLERS ----------------------------------------------
    // This is the binding layer between the button pushes and the xmlrpc
    // client.
    public void loadFile ( String filename )
    {
        try {
            // read the file from teh server
            DProblem problem = handle().getFile ( fullFilename ( filename ) );
            problemListener.loadProblem ( problem );

            masterWindow.setVisible ( false );
        }
        catch ( Exception e ) {
            handleException ( e );
        }

    }

    public void saveFile ( String filename )
    {
        try {

            problemListener.saveProblem ( this );
            DProblem problem = callbackProblem; // the one to be saved.

            problem.setFilename ( filename );
            problem.setUsername (connInfo.username);
            handle().putFile ( problem );

            masterWindow.setVisible ( false );
        }
        catch ( Exception e ) {
            handleException ( e );
        }
    }

    public void deleteFile ( String filename )
    {
        try {
            handle().deleteFile ( fullFilename ( filename ) );
        }
        catch ( Exception e ) {
            handleException ( e );
        }
    }


    /**
     * Return a fully qualified file name for use in communication
     * to and from the server. It just appends the parameter filename
     * to the current working directory.
     *
     * @author brockman 08.08.03
     */

    private String fullFilename ( String filename )
    {
        // TODO : interpret . and ..

        return getCurrentDirectory() + "/" + filename;
    }



    /**
     * This is a callback trigered by the save event being sent back
     * to the caller of this frame.
     */
    public void writeProblem ( DProblem problem )
    {
        debug ( "writeProblem", "Getting a request to write a problem");
        this.callbackProblem = problem;
    }



    /**
     * Generically handle any problems that could go wrong with a
     * popup dialogue + a message capture
     */

    public void handleException ( Exception ex )
    {
        try {
            throw ex;
        } catch ( MalformedURLException e ) {
            errorMessage = "Malformed Server RPC URL\n" + e.getMessage();
	    makeErrorDialog("Malformed Url","Check your problem server URL");
        } catch ( AuthenticationException e ) {
            errorMessage = "Authentication Exception\n" + e.getMessage();
	    makeErrorDialog("Invalid Username or Password","Please check your username and password, or visit:\nhttp://iwp2.ncssm.edu/pps/ to register.");
        } catch ( ProblemServerRemoteException e ) {
        	errorMessage = "Remote Exception\n" + e.getMessage();
        	makeErrorDialog("Remote Exception",e.getFriendlyMessage());
        } catch ( IOException e ) {
            errorMessage = "IOException\n" + e.getMessage();
            makeErrorDialog("Network Exception","Check your Internet Connection and\nProblem Server URL and try again.");
        } catch ( Exception e ) {
            errorMessage = e.getClass().getName() + e.getMessage();
            makeErrorDialog("General Fault", e.getClass().getName() + "\n" + e.getMessage() );
            e.printStackTrace();
        }

        IWPLog.info(this,"[SwingXmlRpcClient.handleException] ERROR: " + errorMessage );
        ex.printStackTrace();
    }

    public void makeErrorDialog(String title, String message) {
	JOptionPane.showMessageDialog(null,
				      message,
				      title,
				      JOptionPane.ERROR_MESSAGE);
    }


    // ---- File Path Tools --------------------------------------------
    /**
     * Remove all the slashes off the end of a path
     */
    private String noSlashEnd ( String in )
    {
        while ( in.endsWith ("/") ) {
            in = in.substring ( 0, in.length()-1 );
        }

        return in;
    }



    /**
     * Make sure there is always a slash on the end of the string
     */
    private String slashEnd ( String in )
    {
        return noSlashEnd ( in ) + "/";
    }

    /**
     * Make sure the string begins w/ a single slash
     */
    private String beginSlash ( String in )
    {

        // make sure there are never two slashes on the front
        while ( in.startsWith ("/") ) {
            in = in.substring ( 1 );
        }
        in = "/" + in;
        return in;
    }


    /**
     * chop off one folder in path
     */
    private String chopOffOneFolderInPath ( String in )
    {
        String out = "";
        StringTokenizer st = new StringTokenizer ( in, "/" );

        while ( st.hasMoreElements() ) {
            String part = (String) st.nextElement();

            if ( st.hasMoreElements() ) {
                out = out + part + "/";
            }
        }

        return beginSlash ( noSlashEnd ( out ) );
    }


    public static final void debug ( String section, String message )
    {
        IWPLog.debug("[SwingXmlRpcClient." + section + "] " + message );
    }


}



/**
 * This GUI is going to have 3 tabbed window panes. The first is going to e
 * the user configuration tab. There, the user will enter his username,
 * password, and host information. This is represented by the ConnectInfoPanel.
 *
 * The next pane is the load pane. This is where the user will select his
 * directory, choose files, exame their meta information, and load / delte
 * them. This is represented by the ______ Class
 *
 * The last pane, save, is very similar to load. I'm hoping to be able to
 * use some code re-use tricks to save myself time.
 *
 * @author brockman 08.08.03
 */

/**
 * This is the master frame that is launched from the client wrapper itself.
 * It contains the tabbedPane set which, in turn, holds all the other
 * panes of interest.
 *
 * @author brockman 08.08.03
 */

interface ConnectionEventListener
{
    public void clientDisconnected ( );
    public void clientConnected ( );
}


class MasterWindow extends GFrame implements ConnectionEventListener
{
	private static final long serialVersionUID = 1L;
    private JTabbedPane tabbedPane;
    private ConnectInfoPanel connectInfoPanel;
    private FileManagerPanel loadPanel;
    private FileManagerPanel savePanel;

    public MasterWindow ( SwingXmlRpcClient swingClient )
        throws ProblemServerRemoteException, IOException,
               UnknownDirectoryException
    {
        constructGui ( swingClient );
    }

    private void constructGui ( SwingXmlRpcClient swingClient )
        throws ProblemServerRemoteException, IOException,
               UnknownDirectoryException
    {
        getContentPane().setLayout ( new BorderLayout() );

        /* create a tabbed pane for each test */
        tabbedPane = new JTabbedPane();
        getContentPane().add ( tabbedPane, BorderLayout.CENTER );

        // the connection info
        connectInfoPanel = new ConnectInfoPanel ( swingClient );
        tabbedPane.addTab ("Setup", null, connectInfoPanel );

        loadPanel = new FileManagerPanel ( swingClient, FileManagerPanel.TYPE_LOAD,this );
        tabbedPane.addTab ("Load", null, loadPanel );

        savePanel = new FileManagerPanel ( swingClient, FileManagerPanel.TYPE_SAVE,this  );
        tabbedPane.addTab ("Save", null, savePanel );
    }

    public void refreshFileViews() {
    try {
        loadPanel.refreshFileList();
        savePanel.refreshFileList();
    } catch(Exception e) {
        IWPLog.info(this,"[SwingXmlRpcClient encountered an error: ");
        e.printStackTrace();
    }
    }

    public void clientDisconnected ( )
    {
        debug ("clientDisconnected", "Client Disconnected");
        tabbedPane.setEnabledAt ( tabbedPane.indexOfTab ( "Load" ), false );
        tabbedPane.setEnabledAt ( tabbedPane.indexOfTab ( "Save" ), false );
    }

    public void clientConnected ( )
    {
        debug ("clientConnected", "Client Connected");
        tabbedPane.setEnabledAt ( tabbedPane.indexOfTab ( "Load" ), true );
        tabbedPane.setEnabledAt ( tabbedPane.indexOfTab ( "Save" ), true );

    }

    public void raiseSetup ( )
    {
        tabbedPane.setSelectedIndex ( tabbedPane.indexOfTab ( "Setup" ) );
        setVisible ( true );
    }

    public void raiseSave ( )
    {
        tabbedPane.setSelectedIndex ( tabbedPane.indexOfTab ( "Save" ) );
        setVisible ( true );
    }

    /**
     * Make the master window visible and make sure the load pane
     * is selected
     */
    public void raiseLoad ( )
    {
        tabbedPane.setSelectedIndex ( tabbedPane.indexOfTab ( "Load" ) );
        setVisible ( true );
    }

    public static final void debug ( String section, String message )
    {
        IWPLog.debug("[SwingXmlRpcClient." + section + "] " + message );
    }

}


class ConnectInfoPanel extends JPanel
    implements ActionListener
{
	private static final long serialVersionUID = 1L;
    public static final String DEFAULT_MESSAGE_STRING = "";

    public int TEXT_FIELD_COLUMNS = 35;

    public JTextField hostField;
    public JTextField userField;
    public JPasswordField passField;
    public JButton infoButton;
    public JLabel infoLabel;
    public JLabel errorLabel;
    private SwingXmlRpcClient swingClient = null;


    public ConnectInfoPanel ( SwingXmlRpcClient swingClient )
    {
        this.swingClient = swingClient;
        constructGui ( );
    }


    private void constructGui ( )
    {
        setLayout ( new FlowLayout() );

        // setup the text fields
        infoLabel = new JLabel("<html><p align=\"left\"><b>NCSSM IWP Problem Server</b><br>" +
        						"http://iwp2.ncssm.edu/pps/<br>" + 
        						"<br>" +
        						"Visit our website to create a new account or browse problems.<br>" +
        						"<br></html>");

        hostField = new JTextField ( swingClient.getConnectionInfo().host, TEXT_FIELD_COLUMNS );
        userField = new JTextField ( swingClient.getConnectionInfo().username, TEXT_FIELD_COLUMNS );
        passField = new JPasswordField ( swingClient.getConnectionInfo().password, TEXT_FIELD_COLUMNS );
        infoButton = new JButton ("Connect to Problem Server");
        infoButton.addActionListener ( this );

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;

        JPanel infoPanel = new JPanel();

        infoPanel.setLayout(new BorderLayout());
        //JPanel overall = new JPanel();
        //overall.setLayout(new BorderLayout());
        JPanel left=new JPanel();
        JPanel right = new JPanel();
        left.setLayout(new GridLayout(3,1));
        right.setLayout(new GridLayout(3,1));
        left.add(new JLabel("RPC Url:"));
        left.add(new JLabel("Email:"));
        left.add(new JLabel("Password:"));
        infoPanel.add(BorderLayout.WEST,left);
        right.add(hostField);
        right.add(userField);
        right.add(passField);
        infoPanel.add(BorderLayout.CENTER,right);
        infoPanel.add(BorderLayout.SOUTH, infoButton);
        
        errorLabel = new JLabel ( DEFAULT_MESSAGE_STRING );
        

        setLayout(new BorderLayout());
        add ( BorderLayout.NORTH, infoPanel );
        add ( BorderLayout.CENTER, infoLabel );
        add ( BorderLayout.SOUTH, errorLabel );
        setBorder(new EmptyBorder(5,5,5,5));
        
        updateEditablesBasedOnConnectionState ( ) ;
    }


   

    public void actionPerformed ( ActionEvent e )
    {
        Object source = e.getSource();

        if ( source instanceof JButton ) {
            JButton buttonSource = (JButton) source;

            debug("actionPerformed", "button was clicked: " + buttonSource.getText() );

            // update the connection info
            ConnectionInfo connInfo = swingClient.getConnectionInfo();
            connInfo.host = hostField.getText();
            connInfo.username = userField.getText();
            connInfo.password = new String ( passField.getPassword() );
            swingClient.setConnectionInfo ( connInfo );

            if ( buttonSource == infoButton ) {

                if ( ! swingClient.isConnected() ) {
                    swingClient.openConnection ( );
                } else {
                    swingClient.closeConnection ( );
                }
            }
        }

        updateEditablesBasedOnConnectionState ( );
    }


    private void updateEditablesBasedOnConnectionState ( )
    {
        boolean editable = ! swingClient.isConnected();
        hostField.setEditable ( editable );
        userField.setEditable ( editable );
        passField.setEditable ( editable );

        if ( swingClient.isConnected ( ) ) {
            infoButton.setText ( "Disconnect" );
        } else if ( ! swingClient.isConnected ( ) ) {
            infoButton.setText ( "Connect" );
        }

        if ( swingClient.getErrorMessage() != null ) {
            errorLabel.setText ( swingClient.getErrorMessage() );
        }
    }


    public static final void debug ( String section, String message )
    {
        IWPLog.debug ( "[SwingXmlRpcClient." + section + "] " + message );
    }

}

class FileManagerPanel extends JPanel implements ListSelectionListener,
                                                 ActionListener,
                                                 MouseListener
{
	private static final long serialVersionUID = 1L;
    public static final String TYPE_LOAD = "LOAD";
    public static final String TYPE_SAVE = "SAVE";

    private SwingXmlRpcClient client;
    private Vector fileNames;
    private JList list;
    private JSplitPane splitPane;
    private FileInformationPanel infoPanel;
    private JScrollPane listScrollPane;
    private String accessType;
    private DirectoryPanel directoryPanel;
    private FilenameSaveAsPanel saveAsPanel;

    private JButton refreshButton;

    private MasterWindow parent;

    public FileManagerPanel ( SwingXmlRpcClient client, String accessType, MasterWindow parent )
        throws ProblemServerRemoteException, IOException,
               UnknownDirectoryException
    {
        this.client = client;
        this.accessType = accessType;
        this.parent=parent;

        fileNames = new Vector();

        constructGui ( );

        refreshFileList ( );
    }

    /**
     * Build the baseline gui frame
     */
    private void constructGui ( )
    {
        refreshButton = new JButton ("Refresh");
        refreshButton.addActionListener ( this );

        infoPanel = new FileInformationPanel ( this.accessType, this );
        list = new JList(fileNames);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.addMouseListener ( this );


        listScrollPane = new JScrollPane(list);

        directoryPanel = new DirectoryPanel ( );

        saveAsPanel = new FilenameSaveAsPanel ( this );

        infoPanel.setPreferredSize ( new Dimension ( 400, 400 ) );


        // This makes it scroll. not a great idea.
        // JScrollPane infoPanelScrollPane = new JScrollPane ( infoPanel );

        //Create a split pane with the two scroll panes in it.
        splitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT,
                                    new CenterSouthPanel ( listScrollPane,
                                                           refreshButton ),
                                    infoPanel ); // infoPanelScrollPane
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);

        //Provide minimum sizes for the two components in the split pane.
        Dimension minimumSize = new Dimension(100, 50);
        listScrollPane.setMinimumSize(minimumSize);
        infoPanel.setMinimumSize(minimumSize);

        //Provide a preferred size for the split pane.
        splitPane.setPreferredSize(new Dimension(400, 200));

        // need a combo panel that contains at least the directoryPanel
        // for load, and then also potentially the saveAs panel, if
        // mode is save.

        JPanel topTextFieldPanel = new JPanel();
        topTextFieldPanel.setLayout ( new BorderLayout() );
        topTextFieldPanel.add ( "North", directoryPanel );
        if ( accessType.equals ( TYPE_SAVE ) ) {
            topTextFieldPanel.add ( "South", saveAsPanel );
        }

        // add to self.
        setLayout ( new BorderLayout ( ) );
        add ( "North", topTextFieldPanel );
        add ( "Center", splitPane );

    }


    /**
     * Todo: Not allof these have to be constructed / deconstructed
     * every time. Make it more efficient by taking the new's out and
     * running them w/ the constructor
     */

    public void refreshFileList ( )
        throws ProblemServerRemoteException, IOException,
               UnknownDirectoryException
    {
        String dir = client.getCurrentDirectory();
        fileNames.clear();

        if ( client.isConnected ( ) ) {
            debug ( "refreshFileList", "Client is connected.");

            Collection content = client.handle().listContent ( dir );

            // This was the idea I had about adding a 'My Problems'
            // easy link to get to your home folder, ala microsft windows.
            // fileNames.add ( "/public" );
            // fileNames.add ( "~My Problems" );

            fileNames.add ( "[.]" );
            fileNames.add ( "[..]" );

            // convert the collections to a data vector
            for ( Iterator i = content.iterator(); i.hasNext(); ) {
                Hashtable fileInfo = (Hashtable) i.next();
                String type = (String) fileInfo.get ("type");

                if ( type.equals ( "directory" ) ) {
                    // it's a directory
                    fileNames.add ( "[" + fileInfo.get("filename") + "]" );
                } else {
                    // regular file
                    fileNames.add ( fileInfo.get("filename") );
                }
            }

            // directory panel
            directoryPanel.setDirectory ( dir );
        }

        //Create the list of images and put it in a scroll pane.
        list.setListData ( fileNames ); // reset the list



        /*
          // no need to  set a selection by default.
          // set the current problem info into the problem info pane
        if ( ! fileNames.isEmpty() ) {
            String selectedProblem = (String) fileNames.firstElement();
            // this needs to send more info. need to call getInfo sub
            setSelectedProblem ( selectedProblem );
        } else {
            setSelectedProblem ( null );
        }
        */

    }


    public void valueChanged(ListSelectionEvent e)
    {
        if (e.getValueIsAdjusting())
            return;

        JList theList = (JList)e.getSource();
        // IWPLog.info(this,"[SwingXmlRpcClient] theList: " + theList );

        if (theList.isSelectionEmpty()) {
            // IWPLog.info(this,"[Selection Empty]");

            setSelectedProblem ( null );

        } else {

            int index = theList.getSelectedIndex();
            String fileName = (String) fileNames.elementAt ( index );

            try {
                setSelectedProblem ( fileName );
            } catch ( Exception ex ) {
                client.handleException ( ex );
            }
        }
    }

    /**
     * This method will be called whenever the filename changes or
     * needs to be passed down to the file display panel
     */
    private void setSelectedProblem ( String selectedProblem )
    {

        if ( selectedProblem != null ) {

            // select the problem in the list on th eleft
            list.setSelectedValue ( selectedProblem, true );

            // do a lookpu by name in the datatable to find the index
            // int index = theList.getSelectedIndex();
            // String fileName = (String) fileNames.elementAt ( index );


            if ( selectedProblem.startsWith ( "[" ) ) {
                // it is a directory
                infoPanel.hideContents ();
                infoPanel.revalidate();
            } else {
                // it's a file
                infoPanel.setSelectedProblem ( selectedProblem );
                infoPanel.showContents ( );
                infoPanel.revalidate();


                // also set the filename into the 'save as' widget
                saveAsPanel.setFilename ( selectedProblem );
            }
        }

    }


    /**
     * This event is triggered when the user presses one of the
     * Load Delete or Save buttons
     *
     * @author brockman 08.08.03
     */


    public void actionPerformed ( ActionEvent e )
    {
        Object source = e.getSource();

        if ( source instanceof JButton ) {

            String selectedProblem = infoPanel.getSelectedProblem ( );
            JButton buttonSource = (JButton) source;

            debug ("actionPerformed" ,"button was clicked: " + buttonSource.getText() + "   selectedProblem: " + selectedProblem );
            try {

                if ( buttonSource.getText().equals ( "Load" ) ) {
                    client.loadFile ( selectedProblem );
                } else if ( buttonSource.getText().equals ( "Save") ) {

                    // At this point we want to pull text out of
                    // directory path AND the filename save as fields
                    // to determine the final name of the problem filename.

                    String filename = directoryPanel.getDirectory() + "/" + saveAsPanel.getFilename();

                    // and, make the call to the server to send the
                    // file data.
                    client.saveFile ( filename );

                    //refreshFileList ( );
                    parent.refreshFileViews();
                } else if ( buttonSource.getText().equals ( "Delete" ) ) {
                    client.deleteFile ( selectedProblem );
                    refreshFileList ( );
                } else if ( buttonSource.getText().equals ( "Refresh" ) ) {
                    refreshFileList ( );
                }
            }
            catch ( Exception ex ) {
                client.handleException ( ex );
            }

        }

    }


    public void mouseClicked ( MouseEvent e )
    {
        if (e.getClickCount() == 2) {
            int index = list.locationToIndex(e.getPoint());
//          IWPLog.debug(this,"Double clicked on Item: " + index);
            String filename = (String) fileNames.elementAt ( index );

            try {
                if ( filename.startsWith ("[") ) {
                    client.changeDirectory ( filename );
                    refreshFileList ( );
                }

            } catch ( Exception ex ) {
                client.handleException ( ex );
            }

          }
     }

    public void mousePressed ( MouseEvent e ) { }
    public void mouseDragged ( MouseEvent e ) { }
    public void mouseReleased ( MouseEvent e ) { }
    public void mouseEntered ( MouseEvent e ) { }
    public void mouseExited ( MouseEvent e ) { }

    public static final void debug ( String section, String message )
    {
        IWPLog.debug("[SwingXmlRpcClient." + section + "] " + message );
    }

}

/**
 * The panel for displaying the directory choice
 */

class DirectoryPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
    JTextField dirField = null;

    public DirectoryPanel ( )
    {
        dirField = new JTextField ( );
        dirField.setEditable ( false );

        setLayout ( new BorderLayout ( ) );
        add ( "West", new JLabel ("Path: ") );
        add ( "Center", dirField );
        // no reason for Go button for now.
        // add ( "East", new JButton ("GO") );

    }

    public void setDirectory ( String dir )
    {
        dirField.setText ( dir );
    }

    public String getDirectory ( )
    {
        return dirField.getText ( );
    }


}




class FilenameSaveAsPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
    JTextField fileField = null;

    public FilenameSaveAsPanel ( ActionListener listener )
    {
        fileField = new JTextField ( );
        // fileField.setEditable ( true );

        JButton saveButton = new JButton ( "Save" );
        saveButton.addActionListener ( listener );

        setLayout ( new BorderLayout ( ) );
        add ( "West", new JLabel ("Filename: ") );
        add ( "Center", fileField );
        add ( "East", saveButton );
        // no reason for Go button for now.
        // add ( "East", new JButton ("GO") );

    }

    public void setFilename ( String filename )
    {
        fileField.setText ( filename );
    }

    public String getFilename ( )
    {
        return fileField.getText ( );
    }


}





class NorthCenterPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
    public NorthCenterPanel ( JComponent north, JComponent center )
    {
        setLayout ( new BorderLayout ( ) );
        add ( "North", north );
        add ( "Center", center );
    }
}

class CenterSouthPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
    public CenterSouthPanel ( JComponent center, JComponent south )
    {
        setLayout ( new BorderLayout ( ) );
        add ( "Center", center );
        add ( "South", south );
    }
}



class FlowLayoutPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
    public FlowLayoutPanel ( JComponent addObject )
    {
        setLayout ( new FlowLayout ( ) );
        add ( addObject );
    }
}



class FileInformationPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
    public static final int FIELD_WIDTH = 15;

    private JTextField fileNameField;
    private JPanel fieldPanel;
    private FileButtonPanel buttonPanel;

    public FileInformationPanel ( String accessType, ActionListener listener )
    {
        TitledBorder titleBorder = BorderFactory.createTitledBorder("Problem Info");
        titleBorder.setTitleJustification(TitledBorder.LEFT);
        setBorder(titleBorder);
        setLayout(new BorderLayout ( ));

        fieldPanel = new JPanel ( );
        fieldPanel.setLayout ( new FlowLayout() );

        fileNameField = new JTextField ( "", FIELD_WIDTH );
        fileNameField.setEditable ( false );

        fieldPanel.add ( new JLabel ("Filename: " ) );
        fieldPanel.add ( fileNameField );

        buttonPanel = new FileButtonPanel ( accessType, listener );

        add ( "Center", new FlowLayoutPanel (  fieldPanel ) );
        add ( "South", buttonPanel );

        hideContents();
    }

    /**
     * There is nothign valid to display in the File Inforation panel,
     * so we've been told to turn off our internal field display
     * @author brockman 08.08.03
     */
    public void hideContents ( )
    {
        fieldPanel.setVisible ( false );
        buttonPanel.setVisible ( false );

        setMinimumSize ( getSize() ); // keep our size?
    }

    public void showContents ( )
    {
        fieldPanel.setVisible ( true );
        buttonPanel.setVisible ( true );

        setMinimumSize ( getSize() ); // keep our size?
    }

    public void setSelectedProblem ( String fileName )
    {
        debug ( "setSelectedProblem", "fileName: " + fileName );

        fileNameField.setText ( fileName );

    }

    public String getSelectedProblem ( )
    {
        return fileNameField.getText ( );
    }

    public static final void debug ( String section, String message )
    {
        IWPLog.debug("[SwingXmlRpcClient." + section + "] " + message );
    }

}

class FileButtonPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
    public FileButtonPanel ( String accessType,
                             ActionListener listener )
    {
        JButton load = new JButton ( "Load" );
        JButton delete = new JButton ( "Delete" );

        load.addActionListener ( listener );
        delete.addActionListener ( listener );

        if ( accessType.equals ( FileManagerPanel.TYPE_LOAD ) ) {
            add ( load );
            add ( delete );
        } else if ( accessType.equals ( FileManagerPanel.TYPE_SAVE ) ) {
            // the save button has been moved up to the topFieldPanel
            // aka- save as bar
            add ( delete );
        }
    }
}




class ConnectionInfo
{
    // Brockman default host. 09.25.04
    public String host = "http://iwp2.ncssm.edu/pps/problemServer.cgi";
    public String username = null;
    public String password = null;

    public boolean isDefined ( )
    {
        if ( host != null &&
             username != null &&
             password != null ) {
            return true;
        } else {
            return false;
        }
    }
}
