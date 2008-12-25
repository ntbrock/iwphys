package edu.ncssm.iwp.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import edu.ncssm.iwp.graphicsengine.GGraph;
import edu.ncssm.iwp.graphicsengine.GShape_GraphPropertySelector;
import edu.ncssm.iwp.graphicsengine.GShape_GraphPropertySelectorChangeEvent;
import edu.ncssm.iwp.graphicsengine.GShape_GraphPropertySelectorChangeListener;
import edu.ncssm.iwp.objects.DObject_Solid;
import edu.ncssm.iwp.plugin.IWPAnimated;
import edu.ncssm.iwp.plugin.IWPGraphable;
import edu.ncssm.iwp.plugin.IWPObject;
import edu.ncssm.iwp.problemdb.DProblem;
import edu.ncssm.iwp.problemdb.DProblemState;
import edu.ncssm.iwp.util.IWPLog;
import edu.ncssm.iwp.util.IWPLogPopup;


/**
 * 2006-Aug-29 brockman was here. Visiting. Trying to get graphing turned back
 * onl after my eclipse fun.
 * 
 * The graph is created in the GWindow_Animator on gui construction and becomes
 * an animation listener. It just doesn't get show until the user click showGraph.
 * 
 * 
 * 2007-Oct-20: This is the spot where the user turns on / off graphs. There are 
 * a few other spots that are misleading. beware! we found this by searching for:
 * Pick an object in the code text.
 * 
 * 
 * @author brockman
 *
 */

public class GWindow_Animator_Graph extends JFrame
    implements IWPAnimated,ItemListener,ActionListener, GShape_GraphPropertySelectorChangeListener
{

    protected GWindow_Animator parent;
    protected Collection objects;

    protected JPanel holder;
    protected GGraph graph;
    
    protected GWindow_Animator_setGraphProperties graphProperties;
    
    
    protected JPanel selectionPanel; // This used to change, now it stays static and holds objectSelect + currentSelectionPanelContainer.
    protected JComboBox objectSelect; // The list of graphable objects combobox
    protected JLabel dummyCurrentPanel = new JLabel("Pick an Object");
    protected JPanel currentSelectionPanelContainer; // Holds the current JPanel Config
    
    protected JButton setProps;

    protected Hashtable namesToObjects = new Hashtable(); // clear this every time.
    
    public GWindow_Animator_Graph(GWindow_Animator in)
    {
    	parent=in;
    	setTitle("IWP Animator: Graph");
    	
    	_constructGui();
    	
    }
    
    
    private void _constructGui()
    {
    	graph = new GGraph(parent);   	
    	graphProperties = new GWindow_Animator_setGraphProperties( this );

    	// build the internal widgets. like the side window for object selection.
    	objectSelect=new JComboBox();
    	objectSelect.addItemListener(this);
    	
    	currentSelectionPanelContainer = new JPanel();
    	currentSelectionPanelContainer.add( dummyCurrentPanel );
    	
    	selectionPanel = new JPanel();
    	selectionPanel.setLayout(new BorderLayout());
    	selectionPanel.add(BorderLayout.NORTH,objectSelect);
    	selectionPanel.add(BorderLayout.CENTER, currentSelectionPanelContainer);
    	
    	
    	// build the main window first.
    	JSplitPane main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true, graph,selectionPanel);
    	main.setOneTouchExpandable(true);
    	graph.setPreferredSize(new Dimension(400, 400));

    	JSplitPane holder =
    		new JSplitPane ( JSplitPane.VERTICAL_SPLIT, true, main, graphProperties );    	
    	holder.setOneTouchExpandable(true);
	
    	getContentPane().setLayout(new BorderLayout());
    	getContentPane().add(BorderLayout.CENTER,holder);

    	setSize(600,600);
    	setResizable(true);
    	setDefaultCloseOperation(HIDE_ON_CLOSE);

    	invalidate(); validate();
    	// Not showing here - the upper layer calls setVisible(true)
    }
    
    
    public void zero ( DProblem problem, DProblemState state )
    {
    	
    	// 2006-Aug-28. This is a necessary call - it sets up the window inside the graph.
    	graphProperties.zero(problem,state);
    	graph.zero(problem, state);
    	// graphWindow.zero(problem, state);  is this implied by graph.zero?
    	
    	
    	// Popupate the combo box and the hash word map. 
    	// pull the objects in,.
    	// There is a hash attribute of this class that helps organize the panels and 
    	// the variables.

    	// Remove all the options in the object select.
    	
    	
    	objectSelect.removeAllItems(); //<<-- this may not be working.
    	
    	namesToObjects.clear();

    	for ( Iterator i = problem.getObjectsForDrawing().iterator(); i.hasNext(); ) { 

    		Object current = ((Object)i.next());

    		if(current instanceof IWPGraphable &&
    		   current instanceof IWPObject ) { //great instance of interface usability
    			
    			String currentName = ((IWPObject)current).getName();
    			if ( current instanceof DObject_Solid ) { 
    				
    				if(((DObject_Solid)current).getShape().getIsGraphable()) {
    					objectSelect.addItem(currentName);
    					// add name -> object mapping.
    					namesToObjects.put(currentName,current);
    				}	
    			}
    		}
    	}

    	boolean setPanel = false;
    	
    	// I think the first one might get displayed automatically.
    	String selectedName = (String) objectSelect.getSelectedItem();
    	if ( selectedName != null ) {
    		makeObjectTheCurrentSelectionPanel ( selectedName );
    		setPanel = true;
    	}
    	
    	if ( ! setPanel ) { 
        	currentSelectionPanelContainer.removeAll();
        	currentSelectionPanelContainer.add( dummyCurrentPanel );        	
        	IWPLog.debug(this, "Had to set the dummyCurrentPanel because of a loading sequence problem. Not found: " + selectedName );
    	}

    }
    
    public void tick ( DProblemState state )
    {
    	graph.tick (state);
    }
	
    
    /**
     * This callback is fired every time there is a change event that happens
     * in the selection box above. WARNING: this can also happen the first
     * time that it's loaded + set
     */

    public void itemStateChanged(ItemEvent e)
    {
    	// What object is currently selected?
    	String objectName = (String) objectSelect.getSelectedItem();
    	if ( objectName == null ) { 
    		//IWPLogPopup.warn(this, "No Object Name Selected");
    		return;
    	}
    
    	makeObjectTheCurrentSelectionPanel ( objectName );
    }
    
    
    /**
     * I generalized teh code used in the zero and the handle event here
     * @param objectName
     */
    
    private void makeObjectTheCurrentSelectionPanel ( String objectName )
    {
    	
    	IWPGraphable objectToGraph = (IWPGraphable) namesToObjects.get(objectName);
    	if ( objectToGraph == null ) { 
    		// IWPLog.warn(this, "Object Name(" + objectName + ") not connected to a valid Object");
    		return;
    	}	
    	
    	
    	
    	GShape_GraphPropertySelector currentOptionPanel = objectToGraph.getGraphOptionPanel();
    	if ( currentOptionPanel == null ) { 
    		IWPLogPopup.warn(this, "Graphable object had no configuration panel to show.");
    		return;
    	}
    	
    	// Note of warning: we don't want to double-add the listener. It appears that
    	// the same option panels are coming out of the getGraphOptionPanel call every time
    	// 
    	currentOptionPanel.clearPropertyChangeListeners();
    	currentOptionPanel.addPropertyChangeListener(this);
    	
    	// remove the old one and show this one.
    	currentSelectionPanelContainer.removeAll();
    	currentSelectionPanelContainer.add(BorderLayout.CENTER, currentOptionPanel);
    	selectionPanel.invalidate(); selectionPanel.validate(); //this line needs to be last.
    }
    
    
    public GGraph getGGraph() { return graph; }
    
    
    
    public void actionPerformed(ActionEvent e) {
        //JFrame test=new JFrame("Testing...");
	//        graphWindowModifier=new GWindow_Animator_Graph_setProperties ( graph.getGraphWindow() );
    }
    
    public GWindow_Animator getAnimator() {
    	return parent;
    }


	public void graphPropertyChanged(GShape_GraphPropertySelectorChangeEvent evt) {
		graph.repaint();
	}
    
    
    
}


