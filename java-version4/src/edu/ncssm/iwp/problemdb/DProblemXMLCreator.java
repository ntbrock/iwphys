package edu.ncssm.iwp.problemdb;

import edu.ncssm.iwp.plugin.*;
import edu.ncssm.iwp.toolkit.xml.*;

import java.util.*;

public class DProblemXMLCreator
{
	DAuthorXMLCreator authorCreator;


	/*-----------------------------------------------------------------*/

	public XMLElement getElement ( DProblem problem ) 
	{
		XMLElement elem = new XMLElement ("problem");

		// add the author
		authorCreator = new DAuthorXMLCreator ( problem.getAuthor() );
		elem.addElement ( authorCreator.getElement () );
		

		// add all thje objects
		XMLElement objects = new XMLElement ( "objects" );
		elem.addElement ( objects );

		Collection objs = problem.getObjectsForDrawing();
		Iterator i = objs.iterator();

		while ( i.hasNext() ) {
			Object o = (Object) i.next();
			
			if ( o instanceof IWPXmlable ) { 
				objects.addElement ( ((IWPXmlable)o).newXmlObjectCreator().getElement () );
			}
		}

		return elem;
	}

	/*-----------------------------------------------------------------*/

}


