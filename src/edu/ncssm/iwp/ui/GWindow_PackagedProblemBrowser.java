package edu.ncssm.iwp.ui;

import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

import edu.ncssm.iwp.util.*;
import edu.ncssm.iwp.exceptions.*;
import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.problemdb.directory.*;
import edu.ncssm.iwp.objects.directory.*;



/**
 * This is a JFrame window that lets the user browse a tree of packaged
 * problems and choose one to load into the Animator or the Designer.
 * 
 * 2006-Aug-14 brockman
 * 
 * @author brockman
 *
 */


public class GWindow_PackagedProblemBrowser
	extends GFrame implements ActionListener, TreeSelectionListener, MouseListener
{
	private static final long serialVersionUID = 1L;
	
	public static final int DEFAULT_WIDTH = 640;
	public static final int DEFAULT_HEIGHT = 500;
	
	public static final int TREE_HEIGHT = 340;


	ProblemListener problemListener;
	DProblemManager_Magic managerMagic; // loads the DProblems from inside here.
	
	JButton cancelButton = new JButton("Cancel");
	JButton openButton = new JButton("Open");
	
	JTree problemTree = new JTree();
	
	DProblemLink selectedProblemLink = null;
	
	
	
	
	public GWindow_PackagedProblemBrowser ( ProblemListener problemListener )
	{
		this.problemListener = problemListener;
		_constructTree();
		_constructGui();
		this.centerOnScreen();
		
		// also, index the xml file that contains all the paths, etc.
		cancelButton.addActionListener(this);
		openButton.addActionListener(this);
		
		problemTree.addMouseListener(this);
		
		managerMagic = new DProblemManager_Magic();
	}

	
	public void openDialog()
	{
		// don't reset the tree - I like it better to save state.
		// IWPLog.info(this, "I should be resetting the tree on an openDialog call.");

		// show
		setVisible(true);		
	}
	

	
	
	private void _constructGui ()
	{
		setTitle("IWP: Packaged Problem Browser" );
		setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);

		
		JPanel actionButtonPanel = new JPanel();
		actionButtonPanel.add( cancelButton );
		actionButtonPanel.add( openButton );
		
		JPanel controlAndDescriptionPanel = new JPanel();
		controlAndDescriptionPanel.setLayout(new BorderLayout());
		controlAndDescriptionPanel.add(BorderLayout.SOUTH, actionButtonPanel);

		// Size the widgets.		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(BorderLayout.CENTER, new JScrollPane(problemTree) );
		getContentPane().add(BorderLayout.SOUTH, controlAndDescriptionPanel );

		// must call openDialog to get the display.
	}
	
	
	public void actionPerformed(ActionEvent e)
	{
		try { 
		
			if (e.getSource() == openButton ) {
			
				if ( selectedProblemLink != null ) { 
				
					IWPLog.debug(this, "Loading: " + selectedProblemLink.getFilename() );
					DProblem problem = managerMagic.loadProblem( selectedProblemLink.getFilename() );
			
					problemListener.loadProblem( problem );
					setVisible(false);
				
				}
			
			
			} else if (e.getSource() == cancelButton ) {
			
				setVisible(false);
			
			}
			
		} catch ( Exception x ) {
			
			IWPLog.x(this, "actionPerformed", x );
		}
	}

	
	
	

	private void _constructTree ()
	{
		
		// I want to parse the directory.xml structure here
		// directory.category.problemLink.
		
		DefaultMutableTreeNode treeTopNode = new DefaultMutableTreeNode( "EMPTY TREE" );
		
		
		try { 
		
			DDirectoryManager dmanager = new DDirectoryManager();
			
			DDirectory directory = dmanager.loadMagic( IWPFileLocations.PACKAGED_DIRECTORY_MAGIC );
			
			// Now, translate the DDirectory into a jtree.
		
			treeTopNode = new DefaultMutableTreeNode( directory.getTitle() );
		
			for ( Iterator i = directory.getCategories().iterator(); i.hasNext(); ) {
			
				DCategory cat = (DCategory) i.next();
			
				DefaultMutableTreeNode catNode = new DefaultMutableTreeNode( cat );
			
				for ( Iterator j = cat.getProblemLinks().iterator(); j.hasNext(); ) { 
					DProblemLink link = (DProblemLink) j.next();
					catNode.add( new DefaultMutableTreeNode ( link ) );
				}
		
				treeTopNode.add( catNode );
			}
			
		} catch ( MagicFileNotFoundX e ) {
			IWPLogPopup.x(this, "Directory.xml could not be found in jar", e);
		} catch ( XMLParserException e ) {
			IWPLogPopup.x(this, "Directory.xml has a parse exception", e);
		}
				

		problemTree = new JTree(treeTopNode);
		problemTree.getSelectionModel().setSelectionMode (TreeSelectionModel.SINGLE_TREE_SELECTION);
		problemTree.setRootVisible(false);
		problemTree.setShowsRootHandles(true);
		
		
		// Expand all the pre-set nodes. work back to front because we dont' want
		// our row numbers shifting.
		// I have to pull rows because there doesn't seem to be a getAllPaths.
		// brockman 2006-Aug-30
		for ( int i = problemTree.getRowCount()-1; i >= 0; i-- ) { 

			TreePath treePath = problemTree.getPathForRow(i);
			Object[] objectPath = treePath.getPath();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) objectPath[objectPath.length-1];			
			
			DCategory cat = (DCategory) node.getUserObject();
			if ( cat.isExpandedByDefault() ) { problemTree.expandPath(treePath); }
		}
		
		
		//Listen for when the selection changes.
	    problemTree.addTreeSelectionListener(this);
	}
	
	
	
	public void valueChanged(TreeSelectionEvent e) {
	    DefaultMutableTreeNode node = (DefaultMutableTreeNode) problemTree.getLastSelectedPathComponent();

	    if (node == null) return;

	    Object nodeInfo = node.getUserObject();
	    if ( node.isLeaf() &&
	         nodeInfo != null && nodeInfo instanceof DProblemLink ) {
	    	
	    	selectedProblemLink = (DProblemLink) nodeInfo;	    	
	    }
	    
	}

	public void mouseClicked(MouseEvent e) {
		
		if ( e.getClickCount() == 2 ) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) problemTree.getLastSelectedPathComponent();
			if ( node != null ) { 
			
				Object nodeInfo = node.getUserObject();
			
				if ( node.isLeaf() &&
						nodeInfo != null && nodeInfo instanceof DProblemLink ) {
					
					// how do I handle doubleckick
			
					try { 
				
						IWPLog.debug(this, "DoubleClick Loading: " + selectedProblemLink.getFilename() );
						DProblem problem = managerMagic.loadProblem( selectedProblemLink.getFilename() );
		
						problemListener.loadProblem( problem );
					
					} catch ( DataStoreException x ) {
						IWPLogPopup.x(this, x.getMessage(), x );
					}	

					setVisible(false);
				}
				
			}	
		}
		
	}

	public void mouseEntered(MouseEvent e) { }

	public void mouseExited(MouseEvent e) {	}

	public void mousePressed(MouseEvent e) { }

	public void mouseReleased(MouseEvent e) { }
		
	
}

