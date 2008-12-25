package edu.ncssm.iwp.problemdb;

import java.io.File;
import edu.ncssm.iwp.ui.*;
import edu.ncssm.iwp.exceptions.*;
import edu.ncssm.iwp.util.*;


public class DProblemManager
{
	static final int FILE = 1;
	static final int PROBLEM_SERVER = 2;
	static final int HTTP = 3;
	static final int MAGIC = 4; // From inside eclipse or .jar

	DProblemManager_File fileManager = null;
	DProblemManager_HTTP httpManager = null;
	DProblemManager_Designer designerManager = null;

	/*-------------------------------------------------------------------*/

	public DProblemManager ()
	{
		// This is system-independent
		fileManager = new DProblemManager_File ( "." + File.separator);
		httpManager = new DProblemManager_HTTP ( );
		IWPLog.debug(this,"[DProblemManager] Manager created.");
	}

	/*-------------------------------------------------------------------*/

	public void registerDesigner ( GWindow_Designer iDesigner )
	{
		designerManager = new DProblemManager_Designer ( iDesigner );
	}


	/*-------------------------------------------------------------------*/

	public boolean isFileAvailable ( ) 
	{
		if ( fileManager == null ) { return false; }
		else { return true; }
	}

	public boolean isDesignerAvailable ( ) 
	{
		if ( designerManager == null ) { return false; }
		else { return true; }
	}


	/*-------------------------------------------------------------------*/

	public DProblem loadFile ( String iFile )
		throws DataStoreException
	{
		return fileManager.loadProblem(iFile);
	}


	public void saveFile ( String filename, DProblem problem )
		throws DataStoreException
	{
		// should check for the error condition here

		fileManager.saveProblem ( filename, problem );
	}


	public DProblem loadUrl ( String url )
		throws DataStoreException
	{
       	IWPLog.debug(this,"[DProblemManager] Trying to read in "+url);
       	return httpManager.loadProblem ( url );
	}

	/*-------------------------------------------------------------------*/

	public void save ( DProblem problem ) 
		throws IrreversibleLoadTypeException, DataStoreException
	{
		switch ( problem.getAccessMode() ) {
		case PROBLEM_SERVER:
			IWPLogPopup.error(this, "Unable to save problems of type 'ProblemServer' automatically");
			break;

		case HTTP:
			throw new IrreversibleLoadTypeException();

		case FILE:
		default:
			saveFile ( problem.getFilename(), problem );
			break;
		}

	}

	/*-------------------------------------------------------------------*/

	public DProblem getEmptyProblem ()
	{
		return new DProblem ( );
	}

	/*-------------------------------------------------------------------*/

}

