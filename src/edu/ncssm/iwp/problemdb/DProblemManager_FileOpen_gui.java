
package edu.ncssm.iwp.problemdb;

import javax.swing.*;
import java.io.*;

import edu.ncssm.iwp.exceptions.*;
import edu.ncssm.iwp.ui.*;

import edu.ncssm.iwp.util.*;

public class DProblemManager_FileOpen_gui extends GAccessor
{
	DProblemManager manager;


	JFileChooser fileChooser;
	JFrame tmpFrame;
    String fileNameOnly="";

	public DProblemManager_FileOpen_gui ( DProblemManager manager ) 
	{
		this.manager = manager;


	}

	public void selectFile ( )
	{
		IWPOpenFilter iwpFilter = new IWPOpenFilter();

		fileChooser = new JFileChooser(".");
		fileChooser.addChoosableFileFilter(iwpFilter);
		fileChooser.setFileFilter(iwpFilter);		
	
		tmpFrame = new JFrame();
		fileChooser.showOpenDialog(tmpFrame);
	}

	public DProblem getProblem( )
		throws DataStoreException
	{

		File foundFile = fileChooser.getSelectedFile();
		if (foundFile == null) return null;

		System.out.println(foundFile.getPath()+": "+foundFile.getName()+"");
		fileNameOnly=foundFile.getName();
		IWPLog.info(this,"[DProblemManager_FileOpen_gui] filenameonly"+getFileNameOnly());
		return manager.loadFile ( foundFile.getPath() );
	}
    public String getFileNameOnly() {return fileNameOnly;}
}






class IWPOpenFilter extends javax.swing.filechooser.FileFilter {
	public boolean accept(File file) {
		//String mask = new String(".iwp");
		String filename = file.getName().toUpperCase();
		return filename.endsWith(".IWP") | file.isDirectory();
	}
		public String getDescription() {
		return "Interactive Web Physics Problems";
	}
}

