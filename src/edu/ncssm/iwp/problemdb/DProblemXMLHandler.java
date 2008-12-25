package edu.ncssm.iwp.problemdb;

import java.util.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import edu.ncssm.iwp.util.*;


public class DProblemXMLHandler extends IWPDefaultXmlHandler
{
	SAXParser parser;

	DProblemObjectsXMLHandler objectHandler = new DProblemObjectsXMLHandler();
	DAuthorXMLHandler authorHandler = new DAuthorXMLHandler();

	ArrayList objects = new ArrayList();
	DAuthor author = new DAuthor();


	public DProblemXMLHandler ( SAXParser parser )
	{
		this.parser = parser;
	}

	

	/*-----------------------------------------------------------------*/

	public DProblem getProblem ( )
	{
		DProblem problem = new DProblem ();
		problem.setAuthor ( author );
		problem.addObjects ( (Collection) objects );

		return problem;
	}


	/*-----------------------------------------------------------------*/

	public void startElement ( String namespaceURI, String localName,
			   String qName, Attributes attr )
		throws SAXException 
	{
		resetContents();

		if ( qName.equals ( "objects" ) ) {
			objectHandler.collectObjects ( parser, this, objects );

		} else if ( qName.equals("author" ) ) {
			authorHandler.collectObject ( parser, this, author );

		} else if ( qName.equals("problem") ) {
			

		} else { 
			IWPLog.error(this,"[DProblemXMLHandler] unckd tag: " + qName );
		}
	}


	public void endElement(String namespaceURI, String localName,
			String qName) throws SAXException
	{
	}

}


