package edu.ncssm.iwp.plugin;

import java.util.Hashtable;
import edu.ncssm.iwp.exceptions.NoPluginObjectX;
import edu.ncssm.iwp.util.IWPLog;


/**
 * IWP3 - This is the place where the Designer gets it's 'new' object list from.
 * 2007-Feb-01
 *
 * This is one of the touchpoints for adding a new object; this makes it appear
 * int the dropdown lists.
 *
 * @author brockman
 *
 */

public final class IWPPluginFactory
{
    //----------------------------------------------------------------------------
    // Add new Objects here. The name/key must be unique.
	// When you add a new object, all you have to do is add it here.
    private static String[] pluggedObjectNamesOrdered = { "Solid", "Input", "Output", "WaveBox", "FloatingText", "Grapher" };

    // And add the class here
    private static Hashtable pluggedObjects;
    static {
        pluggedObjects = new Hashtable();
        pluggedObjects.put ( "Solid", 		"edu.ncssm.iwp.objects.DObject_Solid" );
        pluggedObjects.put ( "Input", 		"edu.ncssm.iwp.objects.DObject_Input" );
        pluggedObjects.put ( "Output", 		"edu.ncssm.iwp.objects.DObject_Output" );
        pluggedObjects.put ( "WaveBox", 	"edu.ncssm.iwp.objects.wavebox.DObject_WaveBox" );
        pluggedObjects.put ( "FloatingText","edu.ncssm.iwp.objects.floatingtext.DObject_FloatingText" );
        pluggedObjects.put ( "Grapher"  ,	"edu.ncssm.iwp.objects.grapher.DObject_Grapher");
    }

    //----------------------------------------------------------------------------

    /**
     * Return the list of all the object names in use. This is used by the
     * 'New' buttons + menus.
     * @return
     */

    public static String[] getPluggedObjectNames()
    {
        return pluggedObjectNamesOrdered;
    }

    
    /**
     * Look up the classname by the strings returned from .getPluggedObjectNames()
     * @param objectName
     * @return
     * @throws NoPluginObjectX
     */
    
    
    public static String findClassNameByObjectName ( String objectName )
		throws NoPluginObjectX
    {
    	String value = (String) pluggedObjects.get(objectName);
    	if ( value == null ) { 
    		IWPLog.error(IWPPluginFactory.class, "No object by common name: " + objectName );
    		throw new NoPluginObjectX("No Object by common name: " + objectName);
    	} else {
    		return value;
    	}
    }
    
    	
    

    /**
     * Instantiate a new designable object using reflection
     * @param objectType
     */
    public static IWPObject newInstanceOfObject ( String className )
    	throws NoPluginObjectX
    {
    	
    	try {
    		Object object = Class.forName ( className ).newInstance();

    		if ( ! ( object instanceof IWPObject ) ) { 
    			throw new NoPluginObjectX ( "class: " + className +" not instance of IWPObject");
    		} else {
    			return (IWPObject) object;
    		}
    		
    	} catch ( ClassNotFoundException x ) { 
    		IWPLog.x(IWPPluginFactory.class, "ClassNotFoundX Class: "+ className, x);
    		throw new NoPluginObjectX(x);
    	} catch ( InstantiationException x ) {
    		IWPLog.x(IWPPluginFactory.class, "InstantiationX Class: "+ className, x);
    		throw new NoPluginObjectX(x);
    	} catch ( IllegalAccessException x ) { 
    		IWPLog.x(IWPPluginFactory.class, "IllegalAccessX Class: "+ className, x);
    		throw new NoPluginObjectX(x);
    	}


    }

    
    
}
