
/*
  DUserXMLParser
  This is a static class for turning users <-> xml

  Author: Taylor Brockman
  Date: 09/24/02
*/

package edu.ncssm.iwp.problemdb;

import edu.ncssm.iwp.toolkit.xml.*;

import java.io.*;

import org.xml.sax.*;

import edu.ncssm.iwp.exceptions.*;

import edu.ncssm.iwp.util.*;


public class DUserXMLParser
{

	private static final String DEFAULT_PARSER_NAME =
		"org.apache.xerces.parsers.SAXParser";

	public static DUser load ( String data )
		throws XMLParserException
	{

		/* parse this baby! */


		try { 

			XMLReader xr = (XMLReader)Class.forName(DEFAULT_PARSER_NAME).newInstance();

			//XMLReader xr = XMLReaderFactory.createXMLReader();
		
			DUserXMLHandler xh = new DUserXMLHandler ( xr );
			xr.setContentHandler ( xh );


			StringReader sr = new StringReader ( data );
			// InputStreamReader in = new InputStreamReader ( sr );

			xr.parse ( new InputSource ( sr ));

			return xh.getUser();

		} catch ( ClassNotFoundException e ) {
			IWPLog.x("[DUserXMLParser][load] ClassNotFoundException: " + e,e );
			throw new XMLParserException ( e );

		} catch ( InstantiationException e ) {
			IWPLog.x("[DUserXMLParser][load] InstantationException: " + e, e);
			throw new XMLParserException ( e );

		} catch ( IllegalAccessException e ) {
			IWPLog.x("[DUserXMLParser][load] IllegalAccessException: " + e, e );
			throw new XMLParserException ( e );

		} catch ( SAXException e ) { 
			IWPLog.x("[DUserXMLParser][load] SAXException: " + e, e );
			throw new XMLParserException ( e );

		} catch ( IOException e ) { 
			IWPLog.x("[DUserXMLParser][load] IOException: " + e, e );
			throw new XMLParserException ( e );
		}
	}


	public static String save ( DUser user )
		throws XMLParserException
	{
		DUserXMLCreator userCreator = new DUserXMLCreator( user );

		XMLElement userElement = userCreator.getElement ( );
		XMLDocument userDoc = new XMLDocument ( userElement );

 		try { 

			StringWriter sw = new StringWriter();
			// OutputStreamWriter out = new OutputStreamWriter ( sw );
			
			userDoc.toXML ( sw );

			return sw.getBuffer().toString(); 

 		} catch ( IOException e ) { 
 			IWPLog.x("[DUserXMLParser][save] IOException: " + e,e );
			throw new XMLParserException ( e );
 		}

	}

}



