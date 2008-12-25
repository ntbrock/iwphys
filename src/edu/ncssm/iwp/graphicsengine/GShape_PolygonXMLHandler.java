package edu.ncssm.iwp.graphicsengine;


import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.util.IWPLog;
import java.util.*;
import org.xml.sax.*;
import javax.xml.parsers.*;


 
public class GShape_PolygonXMLHandler extends IWPDefaultXmlHandler
{

	ContentHandler parent;
	SAXParser parser;

	GShape_Polygon shape;

    GShape_Polygon_PointXMLHandler gppHandler=
    	new GShape_Polygon_PointXMLHandler();


	public void collectObject ( SAXParser parser,
								ContentHandler parent,
							    GShape shape )
		throws SAXException
	{

		this.parent = parent;
		this.parser = parser;
		this.shape = (GShape_Polygon)shape;

		parser.getXMLReader().setContentHandler( this );
	}

	public void cleanup ( )
	{
		/* create the type */
		

	}

	public void startElement ( String namespaceURI, String localName,
			   				   String qName, Attributes attr )
		throws SAXException
	{
		resetContents();
		Hashtable attrs = attrListToHash(attr);
		//IWPLog.info(this,"[GShapeXMLHandler] name: " + name );

		/* have to do some attr type switches here */

		if(qName.equals("point")) {
		    gppHandler.collectObject(parser,this,shape,(String)attrs.get("index"));
		}
	}

	public void endElement(String namespaceURI, String localName,
			String qName) throws SAXException
	{

		if ( qName.equals("points") ) {
			/* ok. Im done! */
			cleanup();
			parser.getXMLReader().setContentHandler ( parent );
		} else { 
			IWPLog.error(this,"[XMLHandler] Unknown tag: " + qName );
		}


	}



}
