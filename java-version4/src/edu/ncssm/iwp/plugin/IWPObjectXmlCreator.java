package edu.ncssm.iwp.plugin;

import edu.ncssm.iwp.toolkit.xml.XMLElement;


/**
 * Just a placeholder for the new IWP 3 generic object loader + saver.
 * 
 */
	
public interface IWPObjectXmlCreator
{	
	
	/**
	 * Get an XML Element to be written to the outgoing stream.
	 * 
	 * Note that XMLElement here is an IWP class, not a generic type. Makes the
	 * XML Library interchangeable.
	 * 
	 * @return
	 */
	public XMLElement getElement ( );
	
}
