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


public class GAboutDialogue extends GFrame implements ActionListener, WindowListener
{

	
	String helpFile = IWPFileLocations.HELP_MAGIC;
	String gplFile = IWPFileLocations.GPL_MAGIC;
	String helpData;
	String gplData;


	JEditorPane textPane = new JEditorPane();
	JButton closeButton = new JButton ("Ok");
	JButton dispButton = new JButton ("License Info");

	boolean license=false;

	void setEvents()
	{
		closeButton.addActionListener(this);
		dispButton.addActionListener(this);

		addWindowListener(this);
	}


	public GAboutDialogue ()
	{

		setTitle("Interactive Web Physics: About");

		try { 
			IWPMagicFile magicFile = new IWPMagicFile(helpFile);
			helpData = new String(magicFile.readBytes());
			
		} catch ( MagicFileNotFoundX e ) { 
			IWPLogPopup.x(this, "Unable to load help data: " + e.getMessage(), e);
			helpData = "ERROR: unable to load help data: " + e.getMessage();
		}

		try { 
			IWPMagicFile magicFile = new IWPMagicFile(gplFile);
			gplData = new String(magicFile.readBytes());
		} catch ( MagicFileNotFoundX e ) {
			IWPLogPopup.x(this, "Unable to load GPL data: " + e.getMessage(), e);
			gplData = "ERROR: unable to load GPL data: " + e.getMessage();
		}

		textPane.setEditable(false);
		textPane.setText( helpData );

		JPanel north = new JPanel();
		north.setLayout(new BorderLayout());

		north.add(new JScrollPane(textPane));

		JPanel south = new JPanel();
		south.setLayout(new GridLayout(1,2));

		south.add(dispButton);
		south.add(closeButton);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(BorderLayout.NORTH, new JLabel("Build Version: " + BuildVersion.VERSION ) );
		getContentPane().add(BorderLayout.CENTER, north);
		getContentPane().add(BorderLayout.SOUTH, south);


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

		} else if (tmp == dispButton) {

			if (license==true) {
				textPane.setText ( helpData );
				dispButton.setText("License Info");
				license = false;
			} else {
				textPane.setText ( gplData );
				dispButton.setText("About IWP");

				license = true;
			}

			textPane.setCaretPosition ( 0 );

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




