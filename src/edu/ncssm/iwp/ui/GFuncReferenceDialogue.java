package edu.ncssm.iwp.ui;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

import edu.ncssm.iwp.util.*;
import edu.ncssm.iwp.exceptions.*;
import edu.ncssm.iwp.util.buildversion.*;

/**
 * I came through here and made this use the new MagicFile so that it
 * can load the resource from the eclipse filesystem or the .jar
 * 
 * 2006-Aug-18 brockman
 * @author brockman
 *
 */


public class GFuncReferenceDialogue extends GFrame implements ActionListener, WindowListener
{
	String helpFile = IWPFileLocations.FUNC_REFERENCE;
	String helpData;

	JEditorPane textPane = new JEditorPane();
	JButton closeButton = new JButton ("Ok");

	boolean license=false;

	void setEvents()
	{
		closeButton.addActionListener(this);
		addWindowListener(this);
	}


	public GFuncReferenceDialogue ()
	{
		setTitle("Interactive Web Physics: Function Reference");

		try { 
			IWPMagicFile magicFile = new IWPMagicFile(helpFile);
			helpData = new String(magicFile.readBytes());
			
		} catch ( MagicFileNotFoundX e ) { 
			IWPLogPopup.x(this, "Unable to load help data: " + e.getMessage(), e);
			helpData = "ERROR: unable to load help data: " + e.getMessage();
		}

		textPane.setEditable(false);
		textPane.setText( helpData );

		JPanel north = new JPanel();
		north.setLayout(new BorderLayout());

		JScrollPane scrollPane =new JScrollPane(textPane); 
		north.add(scrollPane);

		JPanel south = new JPanel();
		south.setLayout(new GridLayout(1,1));

		south.add(closeButton);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(BorderLayout.NORTH, new JLabel("Build Version: " + BuildVersion.VERSION ) );
		getContentPane().add(BorderLayout.CENTER, north);
		getContentPane().add(BorderLayout.SOUTH, south);


		
		// move the scroll bar int he text panel to the top
		
		class ScrollToTop implements Runnable {
			JScrollPane scrollPane;

			public ScrollToTop(JScrollPane scrollPane) {
				this.scrollPane = scrollPane;
			}

			public void run() {
				scrollPane.getVerticalScrollBar().getModel().setValue(0);
			}
		}

		scrollPane.getVerticalScrollBar().getModel().setValue(0);// move
		// focus to
		// top.
		// Queue call to reposition scrollbar
		SwingUtilities.invokeLater(new ScrollToTop(scrollPane));
				
		
		setEvents();
		pack();
		setSize(540,480);
		this.centerOnScreen();
		setVisible(false);

	}



	//-------- buttons

	public void actionPerformed(ActionEvent e) {

		JButton tmp = (JButton)(e.getSource());
		
		if (tmp == closeButton) {
			setVisible(false);
		}
	}


//--------- window

	public void windowClosing(WindowEvent e) {
		setVisible(false);
	}

	public void windowClosed(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}

	
}




