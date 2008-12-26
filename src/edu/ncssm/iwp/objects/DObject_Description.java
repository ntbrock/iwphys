// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.objects;

import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.plugin.*;
import edu.ncssm.iwp.ui.GAccessor_designer;
import edu.ncssm.iwp.problemdb.DProblem_designer;

/**
 * It's ok for this guy not to implement IWPAnimated or IWPCalculated - he's not really
 * for either of those purposes - he's just input data, like Description.
 * @author brockman
 *
 */

public class DObject_Description implements IWPObject, IWPXmlable, IWPDesigned
{

	String text;
	String name;
	
	public String getIconFilename()
	{
		return "icon_DObject_Description.gif";
	}
	
	
	//Constructors
	public DObject_Description()
	{
		setName("Description");
		setText(new String());
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}





	public void command(String param)
	{
		addLine(param);
	}

        
	public void addLine(String iLine) { text += iLine + "\n"; }



	public DObject_animator getObject_animator ( ) { 
		return new DObject_Description_animator ( this );
	}


	// inherits an abstract class
	public void zero ( DProblem problem, DProblemState state ) { }
	public void tick ( DProblemState state ) { }


	/*----------------------------------------------------------------------*/
	/* DESIGNERS */
	/*----------------------------------------------------------------------*/

	public GAccessor_designer getDesigner ( ) {
		return new DObject_Description_designer ( this );
	}


	/*----------------------------------------------------------------------*/
	
	

	public IWPObjectXmlCreator newXmlObjectCreator()
	{
		return new DObject_DescriptionXMLCreator(this);
	}

	public IWPObjectXmlHandler newXmlObjectHandler()
	{
		return new DObject_DescriptionXMLHandler();
	}
}


