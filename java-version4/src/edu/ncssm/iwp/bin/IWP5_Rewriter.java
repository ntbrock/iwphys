// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version 2
// of the License, or (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
// 
// For more information about IWP, contact Dr. Loren Winters at the 
// North Carolina School of Science and Mathematics at:
// 
// winters@academic.ncssm.edu

package edu.ncssm.iwp.bin;

//import iwp.*;

import edu.ncssm.iwp.exceptions.DataStoreException;
import edu.ncssm.iwp.problemdb.DProblem;
import edu.ncssm.iwp.problemdb.DProblemManager;
import edu.ncssm.iwp.ui.GWindow_Animator;
import edu.ncssm.iwp.ui.GWindow_Designer;
import edu.ncssm.iwp.util.IWPLog;

public class IWP5_Rewriter
{
	public static GWindow_Animator animator;
	public static GWindow_Designer designer;

	public static String filename = new String();


	public static void main(String args[])
	{

		DProblemManager probManager = new DProblemManager ( );
		DProblem problem = null;
		String filename = null;

		IWPLog.debug("---[IWP5_Rewriter]-----(Loading)-------------------------------");

		try { 
			if ( args.length < 1 ) {
				System.err.println("Usage: IWP5_Rewriter <animation.iwp>");
				System.exit(-1);

			} else {

				filename = args[0];
				problem = probManager.loadFile ( args[0] );




				probManager.saveFile(filename, problem);
			}


		} catch ( DataStoreException e ) { 
			IWPLog.x( "Unable to load file: " + args[0], e );
			//System.exit(1);
		}

		IWPLog.debug("---[IWP5_Rewriter]-----(Exiting)-------------------------------");
	}

}







