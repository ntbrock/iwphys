package edu.ncssm.iwp.problemdb.directory;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

import edu.ncssm.iwp.exceptions.XMLParserException;
import edu.ncssm.iwp.util.IWPLog;
import edu.ncssm.iwp.objects.directory.*;


/**
 * Makes loading the DDirectory objects easy. Saving is not handled yet. 
 * 
 * 2006-Aug-14 brockman
 * 
 * @author brockman
 *
 */

public class DDirectoryXMLParser
{

	public static DDirectory load ( InputStream reader )
		throws XMLParserException
	{
		
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parserV2 = factory.newSAXParser();
			DDirectoryXMLHandler xh = new DDirectoryXMLHandler( parserV2 );
			parserV2.parse(reader, xh );

			return xh.getDirectory();

		} catch ( ParserConfigurationException e ) {  
			IWPLog.x("[DDirectoryXMLParser][load] ParserConfigurationException: " + e, e );
			throw new XMLParserException ( e );
		} catch ( SAXException e ) { 
			IWPLog.x("[DDirectoryXMLParser][load] SAXException: " + e, e );
			throw new XMLParserException ( e );
		} catch ( IOException e ) { 
			IWPLog.x("[DDirectoryXMLParser][load] IOException: " + e, e );
			throw new XMLParserException ( e );
		}
	}

	
}
