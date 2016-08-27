package edu.ncssm.iwplib;

import java.io.*;
import java.net.*;
import java.util.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import edu.ncssm.iwp.util.*;


public class XMLProblemParser
{
	private static final String DEFAULT_PARSER_NAME =
		"org.apache.xerces.parsers.SAXParser";

	public static XMLProblem loadProblem ( String urlString )
		throws XMLParserException, MalformedURLException, IOException
	{

		// open a url connection
		URL url = new URL ( urlString );
		InputStream urlStream = url.openStream ( );

		Reader reader = new InputStreamReader ( urlStream );

		// WARNING: this is a cut + paste from edu.ncssm.iwp.problemdb.DProblemXMLParser - brock 03.10.03

		/* parse this baby! */
		try { 

			XMLReader xr = (XMLReader)Class.forName(DEFAULT_PARSER_NAME).newInstance();

			//XMLReader xr = XMLReaderFactory.createXMLReader();
		
			GenericHandler xh = new GenericHandler ( xr, urlString );
			xr.setContentHandler ( xh );

			// StringReader sr = new StringReader ( data );
			// InputStreamReader in = new InputStreamReader ( sr );

			IWPLog.info("[XMLProblemParser][load] about to parse!");
			xr.parse ( new InputSource ( reader ));

			IWPLog.info("[XMLProblemParser][load] spitting out null");

			return xh.getProblem();

		} catch ( ClassNotFoundException e ) {
			IWPLog.x("[XMLProblemParser][load] ClassNotFoundException", e );
			throw new XMLParserException ( e );

		} catch ( InstantiationException e ) {
			IWPLog.x("[XMLProblemParser][load] InstantationException", e );
			throw new XMLParserException ( e );
		} catch ( IllegalAccessException e ) {
			IWPLog.x("[XMLProblemParser][load] IllegalAccessException", e );
			throw new XMLParserException ( e );
		} catch ( SAXException e ) { 
			IWPLog.x("[XMLProblemParser][load] SAXException", e );
			throw new XMLParserException ( e );
		} catch ( IOException e ) { 
			IWPLog.x("[XMLProblemParser][load] IOException", e );
			throw new XMLParserException ( e );
		}
	}

}



class GenericHandler extends DefaultHandler
{
	XMLReader parser;
	private CharArrayWriter contents = new CharArrayWriter();

	String problemURL = null;
	XMLProblemNode topNode = new XMLProblemNode ( );

	Stack nodeStack = new Stack ( );

	public GenericHandler ( XMLReader parser, String url )
	{
		this.parser = parser;
		this.problemURL = url;
	}

	public XMLProblem getProblem ( )
	{
		return new XMLProblem ( topNode, problemURL );
	}

	/*-----------------------------------------------------------------*/

	public void startDocument( ) throws SAXException { }
	public void endDocument( ) throws SAXException { }

	/*-----------------------------------------------------------------*/

	public void startElement ( String namespaceURI, 
							   String localName, 
							   String qName, 
							   Attributes attr ) 
		throws SAXException 
	{
		contents.reset ();

		if ( localName.equals ("problem") ) {
			// skip the top node
			return;
		}

		IWPLog.info(this,"[XMLProblemParser.startElement] (" + nodeStack.size() + ")  element: " + localName );

		XMLProblemNode node = new XMLProblemNode ( localName, attrsToMap ( attr ) );

		nodeStack.push ( node );
	}

	
	private Map attrsToMap ( Attributes attr )
	{
		Map out = new HashMap ( );

		int length = attr.getLength ( );
		for ( int i = 0; i < length; i++ ) { 
			String key = attr.getLocalName ( i );
			String value = attr.getValue ( i );
			out.put ( key, value );
		}
		return out;
	}


	public void endElement ( String namespaceURI,
							 String localName, 
							 String qName )
		throws SAXException 
	{
		if ( localName.equals ("problem") ) { 
			// skip the top node.
			return;
		}

		XMLProblemNode endingNode = (XMLProblemNode) nodeStack.pop ( );

		try { 
			XMLProblemNode parentNode = (XMLProblemNode) nodeStack.peek ( );
			parentNode.addSubNode ( endingNode );
		} catch ( EmptyStackException e ) { 
			// it's a 1st-level node
			topNode.addSubNode ( endingNode );
		}

	}

	public void characters ( char[] ch, int start, int length )
		throws SAXException 
	{
		contents.write ( ch, start, length );
	}

	

}
