package edu.ncssm.iwp.problemdb;

import java.io.CharArrayWriter;
import java.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import edu.ncssm.iwp.util.*;


/**
 * This is the base class of the new Piccolo Sax2 xml parser
 * implementation that Brockman did on 2006-Apr-28. I replaced
 * Brian Sweeney's SAX1 MinML parser rig.
 * 
 * TODO: I could override StartElement to do some things like
 * clearContents, and tune the params that get sent below. 
 * This would reduce the total amount of code in the system -
 * there's a lot of repeats inside of each parser. Not into that
 * right now. A good low-priority TODO for a future developer.
 * 
 * I use piccolo-1.04-bin.zip, which is Sax2 Ready.
 * 
 * @author brockman
 *
 */
	
public abstract class IWPDefaultXmlHandler extends DefaultHandler
{
	// All the contents are collected here, so the overriding
	// classes don't have to worry about it.
	private CharArrayWriter collectedChars = new CharArrayWriter();

	
	public IWPDefaultXmlHandler()
	{
    }

    
    //--------------------------------------------------------------
    // These must be overidden for each object.
    
    public abstract void startElement ( String namespaceURI, String localName,
			   String qName, Attributes attr )
		throws SAXException;
    
    
    public abstract void endElement(String namespaceURI, String localName,
     String qName)
	throws SAXException;
    
    

    /*
      This was added to ease integration back to SAX1. Isn't it
      easier to get keys from a hashtable than go through a linked
      list? That's how it was done anyways. 
    */
    public Hashtable attrListToHash ( Attributes al) {
    	Hashtable goingBack = new Hashtable();
    	for(int i=0;i<al.getLength();i++) {
    		goingBack.put(al.getQName(i),al.getValue(i));
    	}
    	return goingBack;
    }

    
    //--------------------------------------------------------------

    public void startDocument() throws SAXException
    {
    	IWPLog.debug(this, "[default] Document Start");
    }
    
    public void endDocument() throws SAXException
    {		 
    	IWPLog.debug(this, "[default] Document End");
    }
    
    public void characters(char[] chars, int start, int length) 
		throws SAXException
	{ 
    	IWPLog.debug(this, "[default] characters: " + new String(chars,start,length) + " (len="+ length + ")");
    	
    	collectedChars.write ( chars, start, length );
    }

    protected String getContents()
    {
    	return collectedChars.toString();
    }
    
    protected void resetContents()
    {
    	collectedChars.reset();
    }
    
    
    public void setDocumentLocator (Locator loc)
    {
    	IWPLog.debug(this, "[default] setDocumentLocator: " + loc.toString() );
    }
    
    public void ignorableWhitespace(char[] ch, int start, int length)
    	throws SAXException
    {
    	IWPLog.debug(this, "[default] ignoreableWhitespace: " + ch.length );
    	
    }
    
    public void processingInstruction(String target, String data)
    	throws SAXException
    {
    	IWPLog.debug(this, "[default] processingInstruction: " + target );
    }



	public void skippedEntity(String name) throws SAXException
	{
    	IWPLog.debug(this, "[default] skippedEntity: " + name );
	}

	public void startPrefixMapping(String prefix, String uri) throws SAXException 
	{
    	IWPLog.debug(this, "[default] startPrefixMapping: " + prefix );
	}
    
	public void endPrefixMapping(String prefix) throws SAXException
	{
    	IWPLog.debug(this, "[default] endPrefixMapping: " + prefix );
	}
    

}



