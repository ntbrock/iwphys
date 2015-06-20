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

import edu.ncssm.iwp.ui.*;
import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.exceptions.*;

import edu.ncssm.iwp.util.*;
import com.alee.laf.WebLookAndFeel;

public class IWP_Designer
{
	public static GWindow_Designer designer;

	public static String filename = new String();


	public static void main(String args[])
	{
		// 2015-Jun-20 Interactive Web Physics 5 - Fresh Look and Feel
		WebLookAndFeel.install ();

		// brockman goofin around. 08.10.03
        //Make sure we have nice window decorations.
        // JFrame.setDefaultLookAndFeelDecorated(true);
        // JDialog.setDefaultLookAndFeelDecorated(true);


		DProblemManager probManager = new DProblemManager ();
		DProblem problem = null;

		IWPLog.debug("---[IWP_Designer]-----(Loading)-------------------------------");

		try { 
			if ( args.length >= 1 ) {
				problem = probManager.loadFile ( args[0] );
			} else {
				/* load an empty problem */
				problem = probManager.getEmptyProblem ( );
			}
		} catch ( DataStoreException e ) { 
			IWPLog.x("Unable to load file: " + args[0], e );
			//System.exit(1);
		}


		IWPLog.debug("---[IWP_Designer]-----(Constructing)--------------------------");
	
		// passing a null here because I want the Designer to create + mange the new window.
		designer = new GWindow_Designer ( null, GWindow_Designer.MODE_APPLICATION );
		
		designer.designProblem ( problem );
		probManager.registerDesigner ( designer );

		IWPLog.debug("---[IWP_Designer]-----(Running)-------------------------------");
	}

}








