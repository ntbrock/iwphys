package edu.ncssm.iwp.plugin;

import edu.ncssm.iwp.exceptions.InvalidObjectNameX;
import edu.ncssm.iwp.ui.GAccessor_designer;


/**
 * TODO Implement the interface to make an object designable.
 * @author brockman
 *
 */

public interface IWPDesigned
{
    //public DObject_designer getDesigner ( ) {
    public GAccessor_designer getDesigner();


    /**
     * Return the name of the icon for this designable object.
     * @return
     */
    public String getIconFilename();


    /**
     * Get the name of the object.
     * 2007-may-23 brockman - Added for designer clone fix.
     */
    public String getName();

    /**
     * Set the name of the object.
     * 2007-may-23 brockman - Added for designer clone fix.
     */
    public void setName(String name)
    	throws InvalidObjectNameX;
    
}
