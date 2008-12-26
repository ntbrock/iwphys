package edu.ncssm.iwp.plugin.test;

import edu.ncssm.iwp.plugin.*;
import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.exceptions.*;
import edu.ncssm.iwp.util.*;
import edu.ncssm.iwp.ui.*;
import edu.ncssm.iwp.graphicsengine.*;
import edu.ncssm.iwp.objects.*;
import edu.ncssm.iwp.toolkit.xml.XMLElement;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


/**
 * A Swing test for every aspect of an object:
 * 	Calculated
 *  Animated
 *  Designed
 *  Drawable
 *  Graphable
 *  Xmlable
 *  
 * @author brockman
 *
 */

public class TEST_IWPObject extends GFrame implements WindowListener, ChangeListener, ActionListener
{

	public static void main ( String args[] )
	{
		if ( args.length == 0 ) { 
			
			// Run the select dialog.
			FactorySelectorFrame selectFrame = new FactorySelectorFrame();
			selectFrame.setVisible(true);
			
		} else {
			TEST_IWPObject tester = new TEST_IWPObject(args[0]);
			tester.setVisible(true);
		}
		
	}
	
	//-------------------------------------------------------------------------
	// All the different things that objects can do.
	String className = null;
	IWPObject object = null;
	IWPDesigned designed = null;
	IWPAnimated animated = null;
	IWPCalculated calculated = null;
	IWPXmlable xmlable = null;
	IWPGraphable graphable = null;
	IWPDrawable drawable = null;

	int timeMin = 0;
	int timeMax = 100;
	double timeFromSlider = 0.0;

	JSlider timeSlider;
	JTextField timeShow;
	JMenuBar menuBar;

	
	
	public TEST_IWPObject ( String className )
	{
		this.className = className;
		
		try { 
	
			object = IWPPluginFactory.newInstanceOfObject(className);
			
			loadObject ( object );

			buildGui();
			
		} catch ( NoPluginObjectX x ) { 
			IWPLog.x("NoPLuginObject. Class: " + className, x);
			System.exit(1);
		}
		
	}

	private void loadObject ( IWPObject object )
	{
		// spawn something up for each interface.
		if ( object instanceof IWPDesigned ) { designed = (IWPDesigned) object; }
		if ( object instanceof IWPAnimated ) { animated = (IWPAnimated) object; }
		if ( object instanceof IWPCalculated ) { calculated = (IWPCalculated) object; }
		if ( object instanceof IWPXmlable ) { xmlable = (IWPXmlable) object; }
		if ( object instanceof IWPGraphable ) { graphable = (IWPGraphable) object; }
		if ( object instanceof IWPDrawable ) { drawable = (IWPDrawable) object; }
	}
	
	
	
	private void buildGui()
	{
		JTabbedPane tabbedPane = new JTabbedPane();
		
		tabbedPane.addTab( "IWPObject", constructObjectPanel() );
		tabbedPane.addTab( "IWPAnimated", constructAnimatedPanel() );
		tabbedPane.addTab( "IWPDesigned", constructDesignedPanel() );
		tabbedPane.addTab( "IWPCalculated", constructCalculatedPanel() );
		tabbedPane.addTab( "IWPGraphable", constructGraphablePanel() );
		tabbedPane.addTab( "IWPDrawable", constructDrawablePanel() );
		tabbedPane.addTab( "IWPXmlable", constructXmlablePanel() );
	
		
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, tabbedPane);
		add(BorderLayout.SOUTH, constructTimeSliderPanel() );
		
		
		this.setTitle("TEST_IWPObject: " + className);
		this.setSize(new Dimension(640,600));
		this.centerOnScreen();
	}
	

	
	private JPanel constructObjectPanel()
	{
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		if ( p == null ) { 
			p.add(BorderLayout.NORTH, new JLabel("IWPObject: " + className + " does not implement!"));
		} else {	
			p.add(BorderLayout.NORTH, new JLabel("IWPObject: Core Information"));
			
			JPanel dataGrid = new JPanel();
			dataGrid.setLayout(new GridLayout(3,2,5,5) );
			
			dataGrid.add(new JLabel("Name:" ));
			dataGrid.add(new JLabel( object.getName() ));
			
			dataGrid.add(new JLabel("Class:" ));
			dataGrid.add(new JLabel( object.getClass().getName() ));
					
			dataGrid.add(new JLabel(".toString():" ));
			dataGrid.add(new JLabel( object.toString() ));
			
			p.add( new NortherPanel ( dataGrid ) );
		}

		return p;
	}
	

	private JPanel constructAnimatedPanel()
	{
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		
		if ( animated == null ) { 
			p.add(BorderLayout.NORTH, new JLabel("IWPAnimated: " + className + " does not implement!"));
		} else {	
			p.add(BorderLayout.NORTH, new JLabel("IWPAnimated: Tick and Zero"));
			p.add(BorderLayout.CENTER, new JButton("TODO: Add a tick and zero and a time slider"));
		}
		return p;
	}
	
	

	
	
	JButton designerGetButton = new JButton("Get Object from Designer");
	GAccessor_designer designer = null;
	
	
	private JPanel constructDesignedPanel()
	{
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		
		if ( animated == null ) { 
			p.add(BorderLayout.NORTH, new JLabel("IWPDesigned: " + className + " does not implement!"));
		} else {	
			
			GIconSet icons = new GIconSet();
			
			designer = designed.getDesigner( );
			
			p.add(BorderLayout.NORTH, new JLabel(icons.getObjectIcon(object)));
			p.add(BorderLayout.CENTER, new JScrollPane( designer ) );
			p.add(BorderLayout.SOUTH, designerGetButton );
			designerGetButton.addActionListener(this);
		}
		return p;
	}

	
	
	
	
	private JPanel constructCalculatedPanel()
	{
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		
		if ( animated == null ) { 
			p.add(BorderLayout.NORTH, new JLabel("IWPCalculated: " + className + " does not implement!"));
		} else {	
			p.add(BorderLayout.NORTH, new JLabel("IWPCalculated: Symbolic Depends and Provides"));
			p.add(BorderLayout.CENTER, new JButton("TODO: Add a list of provides and requires symbols"));
		}
		return p;
	}
	
	
	JButton leftXmlCreateButton = new JButton("create");
	JButton leftXmlHandleButton = new JButton("handle");
	JButton leftXmlCopyButton = new JButton("copy <<");
	JTextArea leftXmlText = new JTextArea(5,5);
	
	JButton rightXmlCreateButton = new JButton("create");
	JButton rightXmlHandleButton = new JButton("handle");
	JButton rightXmlCopyButton = new JButton(">> copy");
	JTextArea rightXmlText = new JTextArea(5,5);
	
	
	private JPanel constructXmlablePanel()
	{
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		
		if ( animated == null ) { 
			p.add(BorderLayout.NORTH, new JLabel("IWPXmlable: " + className + " does not implement!"));

			
		} else {	
			p.add(BorderLayout.NORTH, new JLabel("IWPXmlable: Save and Read from Xml"));

			leftXmlCreateButton.addActionListener(this);
			leftXmlHandleButton.addActionListener(this);
			leftXmlCopyButton.addActionListener(this);
			rightXmlCreateButton.addActionListener(this);
			rightXmlHandleButton.addActionListener(this);
			rightXmlCopyButton.addActionListener(this);
			
			
			JPanel leftPanel = new JPanel();
			leftPanel.setLayout(new BorderLayout());
			leftPanel.add(BorderLayout.CENTER, new JScrollPane(leftXmlText));
			
			JPanel leftButtonPanel = new JPanel();
			leftButtonPanel.add(leftXmlCreateButton);
			leftButtonPanel.add(leftXmlHandleButton);
			leftButtonPanel.add(leftXmlCopyButton);
			leftPanel.add(BorderLayout.NORTH, leftButtonPanel);
				
		
			JPanel rightPanel = new JPanel();
			rightPanel.setLayout(new BorderLayout());
			rightPanel.add(BorderLayout.CENTER, new JScrollPane(rightXmlText));

			JPanel rightButtonPanel = new JPanel();
			rightButtonPanel.add(rightXmlCopyButton);
			rightButtonPanel.add(rightXmlHandleButton);
			rightButtonPanel.add(rightXmlCreateButton);
			rightPanel.add(BorderLayout.NORTH, rightButtonPanel);
			
			
			JSplitPane splitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel );
			splitPane.setOneTouchExpandable(true);
			splitPane.setDividerLocation(312); //midway

			p.add(BorderLayout.CENTER,splitPane);
			
			/*
//			Provide minimum sizes for the two components in the split pane
			Dimension minimumSize = new Dimension(100, 50);
			listScrollPane.setMinimumSize(minimumSize);
			pictureScrollPane.setMinimumSize(minimumSize);
			*/
			
			
		}
		return p;
	}
	
	
	
	private JPanel constructGraphablePanel()
	{
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		
		if ( animated == null ) { 
			p.add(BorderLayout.NORTH, new JLabel("IWPGraphable: " + className + " does not implement!"));
		} else {	
			p.add(BorderLayout.NORTH, new JLabel("IWPGraphable: Historical Data Plot"));
			p.add(BorderLayout.CENTER, new JButton("TODO: Add a graph window."));
		}
		return p;
	}
	
	
	// Drawable
	
	class AnonymousRenderPanel extends JPanel
	{
		DObject_Time time = new DObject_Time();
		DObject_Window window = new DObject_Window();
		DProblemState problemState = new DProblemState( new DProblem() );
		
		TEST_IWPObject parent;
		
		public AnonymousRenderPanel(TEST_IWPObject parent)
		{
			this.parent = parent;
		}
		
		public void paint ( Graphics g ){
			
			try {
				time.setTime(parent.timeFromSlider);
				time.setStartTime(parent.timeFromSlider);
				time.tick(problemState);
				animated.tick(problemState);

				//IWPLog.info(this, "Current Time: " + time.getTime() +  "  Time from slider: " + timeFromSlider );
				
				g.setColor(Color.WHITE);
				g.fillRect(0,0, this.getWidth(), this.getHeight());
				
				window.setWidth(this.getWidth());
				window.setHeight(this.getHeight());
				window.drawMe(g);
				
				
				IWPDrawer worker = new IWPDrawer(g,window);
				Color origColor = g.getColor();
				g.setColor(drawable.getGColor().getAWTColor());
				
				// The top-level handles the changing of the color. 
				parent.drawable.iwpDraw(worker, problemState);
				
				g.setColor(origColor);
								
			} catch ( UnknownTickException e ) { 
				IWPLogPopup.x(this, e.getMessage(), e);
			} catch ( UnknownVariableException e ) { 
				IWPLogPopup.x(this, e.getMessage(), e);
			} catch ( InvalidEquationException e ) { 
				IWPLogPopup.x(this, e.getMessage(), e);
			}
		}
		
	}

	
	AnonymousRenderPanel renderPanel = new AnonymousRenderPanel(this);
		
	private JPanel constructDrawablePanel()
	{
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		
		if ( animated == null ) { 
			p.add(BorderLayout.NORTH, new JLabel("IWPDrawable: " + className + " does not implement!"));
		} else {	
			p.add(BorderLayout.NORTH, new JLabel("IWPDrawable: Render State onto a Canvas"));
			p.add(BorderLayout.CENTER, renderPanel);
			
			renderPanel.invalidate();
		}
		
		return p;
	}

	
	private JPanel constructTimeSliderPanel()
	{
	
		JPanel p = new JPanel();

		timeSlider = new JSlider(JSlider.HORIZONTAL, timeMin, timeMax, timeMin );
		timeShow = new JTextField("0.0", 5);

		timeSlider.addChangeListener(this);

		p.add ( new JLabel("Time = ") );
		p.add ( timeSlider );
		p.add ( timeShow );
		p.add ( new JLabel ( "Sec") );
		
		return p;
	}
	

	//-----------------------------------------------------------
	
	public void windowActivated(WindowEvent e) {}
	public void windowClosed(WindowEvent e) { System.exit(0); }
	public void windowClosing(WindowEvent e) { }
	public void windowDeactivated(WindowEvent e) { }
	public void windowDeiconified(WindowEvent e) { }
	public void windowIconified(WindowEvent e) { }
	public void windowOpened(WindowEvent e) { }




	public void stateChanged(ChangeEvent e)
	{
		if ( e.getSource() == timeSlider ) { 
			timeFromSlider = timeSlider.getValue() / 10.0;
			timeShow.setText(timeFromSlider+"");
			
			renderPanel.invalidate();
			renderPanel.repaint();
		}
		
	}
	
	
	public void actionPerformed(ActionEvent e)
	{
		if ( e.getSource() == leftXmlCreateButton || e.getSource() == rightXmlCreateButton ) {
			
			IWPObjectXmlCreator creator = xmlable.newXmlObjectCreator();
			XMLElement xml = creator.getElement();
			StringWriter writer = new StringWriter();
			String text = "";
			
			try { 
				xml.toXML(writer);
				text = writer.getBuffer().toString();
			} catch ( IOException x ) { 
				IWPLogPopup.x(this, "IOException in STringBuffer", x);
			}

			if ( e.getSource() == leftXmlCreateButton ) {
				leftXmlText.setText(text);
			} else if ( e.getSource() == rightXmlCreateButton ) {
				rightXmlText.setText(text);
			}		
		}
	
		
		if ( e.getSource() == leftXmlHandleButton || e.getSource() == rightXmlHandleButton ) {
			
			String text = "";

			if ( e.getSource() == leftXmlHandleButton ) {
				text = leftXmlText.getText();
			} else if ( e.getSource() == rightXmlHandleButton ) {
				text = rightXmlText.getText();
			}
			
			// need to create a fake handler here.
			IWPObjectXmlHandler handler = xmlable.newXmlObjectHandler();
			if ( handler == null ) { }
			// make a small sax parser?
			try { 
				InternalObjectXmlParser.load(xmlable, text );
			} catch ( XMLParserException x ) {
				IWPLogPopup.x(this, "XMLPArserException", x);
			}
		}
	
		
		
		
		if ( e.getSource() == leftXmlCopyButton ) {
			leftXmlText.setText(rightXmlText.getText());
		} else if ( e.getSource() == rightXmlCopyButton ) {
			rightXmlText.setText(leftXmlText.getText());
		}
	
		
		if ( e.getSource() == designerGetButton ) {
			try { 
			
				loadObject ( ((DObject_designer)designer).buildObjectFromDesigner() );
				
			} catch ( DesignerInputException x ) { 
				IWPLogPopup.x(this, "DesignerInputException: " + x.getMessage(), x);
			} catch ( InvalidEquationException x ) { 
				IWPLogPopup.x(this, "InvalidEquationException: " + x.getMessage(), x);
			}
			
		}
		
		
	}
	
	
}



/**
 * A wrapped for adding to a BorderLayout.NORTH middle panel.
 * @author brockman
 *
 */
class NortherPanel extends JPanel
{
	public NortherPanel ( JPanel subPanel )
	{			
		setLayout(new BorderLayout() );
		setBorder(new EmptyBorder(5,5,5,5));
		add(BorderLayout.NORTH, subPanel );
	}
}







class InteralXmlHandler extends IWPDefaultXmlHandler
{
	IWPXmlable xmlable;
	IWPObjectXmlHandler handler;
	SAXParser parser;
	
	public InteralXmlHandler( SAXParser parser, IWPObjectXmlHandler handler, IWPXmlable xmlable )
	{
		this.parser = parser;
		this.handler = handler;
		this.xmlable = xmlable;
	}
	
	
	public void startElement ( String namespaceURI, String localName,
			   String qName, Attributes attr )
		throws SAXException
	{
		
		handler.collectObject ( parser, this, xmlable );
	}
    
    
    public void endElement(String namespaceURI, String localName,
     String qName)
	throws SAXException
	{
    	
	}
    
	
}



class InternalObjectXmlParser
{

	public static IWPXmlable load ( IWPXmlable xmlable, String xmlString )
		throws XMLParserException
	{
		
		try {
		   
		    // 2006-Apr-26 brockman here. I am going to try and put
		    // in a new xml sax2 parser. SAXParser uis throwing all sorts
		    // of SAX1 deprecation errors. Using picollo now.    
			
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parserV2 = factory.newSAXParser();

			
			InteralXmlHandler xh = new InteralXmlHandler( parserV2, xmlable.newXmlObjectHandler(), xmlable );

			ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlString.getBytes());
			parserV2.parse(inputStream, xh );
			
			return xmlable;
			
		} catch ( ParserConfigurationException e ) {  
			IWPLogPopup.x("[DProblemXMLParser][load] ParserConfigurationException: " + e, e );
			throw new XMLParserException ( e );
		} catch ( SAXException e ) { 
			IWPLogPopup.x("[DProblemXMLParser][load] SAXException: " + e, e );
			throw new XMLParserException ( e );
		} catch ( IOException e ) { 
			IWPLogPopup.x("[DProblemXMLParser][load] IOException: " + e, e );
			throw new XMLParserException ( e );
		}
	}

	

}

/**
 * Handles the object selector
 * @author brockman
 *
 */

class FactorySelectorFrame extends GFrame implements ActionListener
{
	JButton objectButtons[];
	
	public FactorySelectorFrame(  )
	{
		this.setTitle("Select Object");
		
		String names[] = IWPPluginFactory.getPluggedObjectNames();
		objectButtons = new JButton[names.length];
		
		setLayout( new GridLayout ( names.length+1, 1, 5, 5 ) );
		
		this.add(new JLabel("IWPPluginFactory"));
		
		for ( int i = 0; i < names.length; i++ ) { 
			objectButtons[i] = new JButton(names[i]);
			add(objectButtons[i]);
			objectButtons[i].addActionListener(this);
		}
		
		this.pack();
		this.centerOnScreen();
	}
	
	public void actionPerformed(ActionEvent e )
	{
		
		for ( int i = 0; i < objectButtons.length; i++ ) {
			
			if ( objectButtons[i] == e.getSource() ) {

				JButton clickedButton = (JButton) objectButtons[i];
				
				try {
				
					TEST_IWPObject tester = new TEST_IWPObject( IWPPluginFactory.findClassNameByObjectName(clickedButton.getText()) );
					setVisible(false);
					tester.setVisible(true);
				} catch ( NoPluginObjectX x ) {
					IWPLogPopup.x(this, "NoPluginObject: " + clickedButton.getText(), x);
				}
				

			}
		}
		
	}
	
}