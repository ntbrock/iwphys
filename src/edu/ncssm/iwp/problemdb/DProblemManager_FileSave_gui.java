
package edu.ncssm.iwp.problemdb;

import edu.ncssm.iwp.ui.*;

import javax.swing.*;
import java.io.*;

import edu.ncssm.iwp.util.*;



public class DProblemManager_FileSave_gui extends GAccessor
{
	DProblemManager manager;


	JFileChooser fileChooser;
	JFrame tmpFrame;
    String fileNameOnly="";


	public DProblemManager_FileSave_gui ( DProblemManager manager ) 
	{
		this.manager = manager;


	}
    /*
	public int  selectFile ( )
	{
		IWPSaveFilter iwpFilter = new IWPSaveFilter();

		fileChooser = new JFileChooser(".");
		fileChooser.addChoosableFileFilter(iwpFilter);
		fileChooser.setFileFilter(iwpFilter);		
	
		tmpFrame = new JFrame();
		int dialogResult=fileChooser.showSaveDialog(tmpFrame);
		//return fileChooser.showSaveDialog(tmpFrame);
		if(dialogResult!=JFileChooser.APPROVE_OPTION) {
		    return dialogResult;
		} else {
		    File foundFile = fileChooser.getSelectedFile();
		    if(foundFile.exists()) {
			JOptionPane opt= new JOptionPane();
			int yesNo=opt.showConfirmDialog(null, 
							"Are you sure you want to overwrite this file?",
							"Overwrite existing file?",
							JOptionPane.YES_NO_OPTION);
			
			if(yesNo==JOptionPane.YES_OPTION) {
			    return JFileChooser.APPROVE_OPTION;
			} else if(yesNo==JOptionPane.NO_OPTION) {
			    return selectFile(fileChooser.getCurrentDirectory());
			} else {return JFileChooser.CANCEL_OPTION;}
		    }
		    return JFileChooser.APPROVE_OPTION;
		}
	}
    */
    public int selectFile () {
	return selectFile(new File("."));
    }

	public int  selectFile (File directory )
	{
		IWPSaveFilter iwpFilter = new IWPSaveFilter();

		fileChooser = new JFileChooser(directory);
		fileChooser.addChoosableFileFilter(iwpFilter);
		fileChooser.setFileFilter(iwpFilter);		
	
		tmpFrame = new JFrame();
		int dialogResult=fileChooser.showSaveDialog(tmpFrame);
		//return fileChooser.showSaveDialog(tmpFrame);
		if(dialogResult!=JFileChooser.APPROVE_OPTION) {
		    return dialogResult;
		} else {
		    File foundFile = fileChooser.getSelectedFile();
		    if(foundFile.exists()) {
			
			int yesNo = JOptionPane.showConfirmDialog(null, 
							"Are you sure you want to overwrite this file?",
							"Overwrite existing file?",
							JOptionPane.YES_NO_OPTION);
			
			if(yesNo==JOptionPane.YES_OPTION) {
			    return JFileChooser.APPROVE_OPTION;
			} else if(yesNo==JOptionPane.NO_OPTION) {
			    return selectFile(fileChooser.getCurrentDirectory());
			} else {return JFileChooser.CANCEL_OPTION;}
		    }
		    return JFileChooser.APPROVE_OPTION;
		}
	}


	public String getFilename( )
	{

		File foundFile = fileChooser.getSelectedFile();
		if (foundFile == null) return null;

		System.out.println(foundFile.getPath()+": "+foundFile.getName()+"");
		if(fileChooser.getFileFilter() instanceof IWPSaveFilter) {
		    IWPLog.info(this,"IWPSaveFilter on");
		    if(foundFile.getName().lastIndexOf(".iwp")!=foundFile.getName().length()-4) {
			IWPLog.info(this,"Not a .iwp");
			fileNameOnly=foundFile.getName()+".iwp";
			return foundFile.getPath()+".iwp";
		    }
		    
		}
		fileNameOnly=foundFile.getName();
		return foundFile.getPath();
	}
    public String getFileNameOnly() {return fileNameOnly;}

}







class IWPSaveFilter extends javax.swing.filechooser.FileFilter {
	public boolean accept(File file) {
		// String mask = new String(".iwp");
		String filename = file.getName().toUpperCase();
		return filename.endsWith(".IWP") | file.isDirectory();
	}
		public String getDescription() {
		return "Interactive Web Physics Problems";
	}
}


