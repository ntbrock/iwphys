package edu.ncssm.iwp.problemdb.directory;

import edu.ncssm.iwp.objects.directory.DDirectory;
import edu.ncssm.iwp.objects.directory.DCategory;
import edu.ncssm.iwp.problemdb.*;

import org.xml.sax.*;
import javax.xml.parsers.*;



public class DDirectoryXMLHandler extends IWPDefaultXmlHandler
{
	SAXParser parser;	
	DCategoryXMLHandler categoryHandler = new DCategoryXMLHandler();
	DDirectory directory;
	
	/*-----------------------------------------------------------------*/

	/**
	 * This is a top-level parser, so it doesn't use the collectObject notation.
	 */
	public DDirectoryXMLHandler ( SAXParser parser ) {
		this.parser = parser;
	}
	
	public DDirectory getDirectory()
	{
		return this.directory;
	}
	
	
	/*-----------------------------------------------------------------*/

	public void startElement ( String namespaceURI, String localName,
			   String qName, Attributes attr )
		throws SAXException 
	{
		resetContents();

		if ( qName.equals ( "directory" ) ) {
			
			this.directory = new DDirectory();
		
		} else if ( qName.equals( "category" ) ) {
			
			DCategory category = new DCategory();
			categoryHandler.collectObject ( parser, this, category );
			this.directory.addCategory( category );
			
		}
	}


	public void endElement(String namespaceURI, String localName,
			String qName) throws SAXException
	{
		String contents = getContents();
		
		if ( qName.equals ( "title" ) ) {
			directory.setTitle( contents.toString().trim() );
		
		} else if ( qName.equals("directory") ) {
			// done

		}
	
	}

}


