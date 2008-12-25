
/*
  DProblemXMLParser
  This is a static class for turning problems <-> xml

  Author: Taylor Brockman
  Date: 6/12/01
*/

package edu.ncssm.iwp.problemdb;

import edu.ncssm.iwp.util.*;
import edu.ncssm.iwp.toolkit.xml.*;
import java.io.*;
import org.xml.sax.*;
//import org.xml.sax.helpers.*;
import edu.ncssm.iwp.exceptions.*;
import javax.xml.parsers.*;


//import common.*;

public class DProblemXMLParser
{

    //This was pre-SAXParser
    /*     When we were using Xerces
    	private static final String DEFAULT_PARSER_NAME =
    		"org.apache.xerces.parsers.SAXParser";
    
    private static final String DEFAULT_PARSER_NAME =
	"javax.xml.parsers.SAXParser";
    */

	public static DProblem load ( String data )
		throws XMLParserException
	{
		return load ( new ByteArrayInputStream ( data.getBytes(), 0, data.getBytes().length ) );
	}

	public static DProblem load ( InputStream reader )
		throws XMLParserException
	{
		
		try {
		   
		    // 2006-Apr-26 brockman here. I am going to try and put
		    // in a new xml sax2 parser. SAXParser uis throwing all sorts
		    // of SAX1 deprecation errors. Using picollo now.    
			
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parserV2 = factory.newSAXParser();
			DProblemXMLHandler xh = new DProblemXMLHandler(parserV2);

			DProblemXMLInputStream is = new DProblemXMLInputStream(reader);
			
			parserV2.parse( is, xh );			
			return xh.getProblem();

		} catch ( ParserConfigurationException e ) {  
			IWPLog.x("[DProblemXMLParser][load] ParserConfigurationException: " + e, e );
			throw new XMLParserException ( e );
		} catch ( SAXException e ) { 
			IWPLog.x("[DProblemXMLParser][load] SAXException: " + e, e );
			throw new XMLParserException ( e );
		} catch ( IOException e ) { 
			IWPLog.x("[DProblemXMLParser][load] IOException: " + e, e );
			throw new XMLParserException ( e );
		}
	}


	public static String save ( DProblem problem )
		throws XMLParserException
	{
		DProblemXMLCreator problemCreator = new DProblemXMLCreator();

		XMLElement problemElement = problemCreator.getElement ( problem );
		XMLDocument problemDoc = new XMLDocument ( problemElement );

 		try { 

			StringWriter sw = new StringWriter();
			// OutputStreamWriter out = new OutputStreamWriter ( sw );
			
			problemDoc.toXML ( sw );

			return sw.getBuffer().toString(); 

 		} catch ( IOException e ) { 
 			IWPLog.x("[DProblemXMLParser][save] IOException: " + e, e );
			throw new XMLParserException ( e );
 		}

	}


}



