package edu.ncssm.iwp.plugin;


/**
 * TODO Write the Xmlable interface. Should include handler and parsers.
 * @author brockman
 *
 */

public interface IWPXmlable
{

	/**
	 * Return an instance of the DObjectXmlCreator
	 * @return
	 */
	
	public IWPObjectXmlCreator newXmlObjectCreator();
	
	

	/**
	 * Return an instance of the DObjectXmlCreator
	 * @return
	 */
	
	public IWPObjectXmlHandler newXmlObjectHandler();
	
}
