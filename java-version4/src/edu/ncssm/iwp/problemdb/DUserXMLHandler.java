package edu.ncssm.iwp.problemdb;

import org.xml.sax.*;
import edu.ncssm.iwp.util.*;


public class DUserXMLHandler extends IWPDefaultXmlHandler
{
	XMLReader parser;
	
	DAuthorXMLHandler authorHandler = new DAuthorXMLHandler();

	DUser user = new DUser ( );

	public DUserXMLHandler ( XMLReader parser )
	{
		this.parser = parser;
	}


	/*-----------------------------------------------------------------*/

	public DUser getUser ( )
	{
		return user;
	}

	/*-----------------------------------------------------------------*/

	public void startDocument( ) throws SAXException
	{
		// System.out.println( "SAX Event: START DOCUMENT" );
	}
	
	public void endDocument( ) throws SAXException
	{
		// System.out.println( "SAX Event: END DOCUMENT" );
	}

	/*-----------------------------------------------------------------*/

	public void startElement ( String namespaceURI, 
							   String localName, 
							   String qName, 
							   Attributes attr ) 
		throws SAXException 
	{
		resetContents ();
	}


	public void endElement ( String namespaceURI,
							 String localName, 
							 String qName )
		throws SAXException 
	{
		String contents = getContents();
		
		if ( localName.equals ( "username" ) ) {
			user.setUsername ( contents.toString().trim() );
		} else if ( localName.equals ( "password" ) ) {
			user.setPassword ( contents.toString().trim() );
		} else if ( localName.equals ( "email" ) ) {
			user.setEmail ( contents.toString().trim() );

		} else if ( localName.equals ( "realName" ) ) {
			user.setRealName ( contents.toString().trim() );

		} else if ( localName.equals ( "adminFlag" ) ) {

			if ( contents.toString().trim().equals("true") ) { 
				user.setAdminFlag ( true );
			} else {
				user.setAdminFlag ( false );
			}


		} else if ( localName.equals ("user") ) {
			// we're closing out here.

		} else { 
			IWPLog.info(this,"[DUserXMLHandler] XML ERROR: unckd tag: " + localName );
		}

	}

}


