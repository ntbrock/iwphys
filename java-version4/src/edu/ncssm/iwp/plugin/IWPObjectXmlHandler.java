package edu.ncssm.iwp.plugin;

import javax.xml.parsers.SAXParser;
import org.xml.sax.*;

import edu.ncssm.iwp.problemdb.IWPDefaultXmlHandler;

/**
 * Extends the IWPDefault handler just to implement 
 * the collect object method.
 * 
 */
	
public interface IWPObjectXmlHandler
{	
	/**
	 * 2007-Jan-19 Part of the IWP 3 xml loader generaliztion.
	 * @param parser
	 * @param parent
	 * @param object
	 * @throws SAXException
	 */
	public abstract void collectObject ( SAXParser parser, IWPDefaultXmlHandler parent,
				    			IWPXmlable object )
		throws SAXException;
}



