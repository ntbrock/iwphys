package edu.ncssm.iwp.util;

/**
 * Instead of using System.err, you should use this class to handle all 
 * of your console logging requests. This allows a builder to turn off / 
 * on all logging with a single switch.
 * 
 * @author brockman
 *
 */

public class IWPLog
{
	public static boolean SWITCH_MASTER		= true;  // Turn off all chatter.
	
	public static boolean SWITCH_OUT 		= true;  // Print 'out' lines.
	public static boolean SWITCH_X 			= true;  // Print exceptions
	public static boolean SWITCH_ERROR 		= true;  // Print errors
	public static boolean SWITCH_WARN 		= true;  // Print warnings
	public static boolean SWITCH_INFO 		= true;  // Print Info's
	public static boolean SWITCH_DEBUG 		= true; // Print Debugs.
	


	public static void out ( String message)
	{
		if ( ! SWITCH_MASTER || ! SWITCH_OUT ) { return; }
		System.err.println("(OUT) " + message );
	}

	
	public static void out ( Object caller, String message)
	{
		if ( ! SWITCH_MASTER || ! SWITCH_OUT ) { return; }
		System.err.println("(OUT)[" + shortClassName(caller) + "] " + message );
	}


	public static void x ( Object caller, String message, Exception x )
	{
		if ( ! SWITCH_MASTER || ! SWITCH_X ) { return; }
		System.err.println("(X)[" + shortClassName(caller) + "] " + message );
		System.err.println( x.getClass().getName() + ": " + x.getMessage() );
		x.printStackTrace();
	}


	public static void x ( String message, Exception x )
	{
		if ( ! SWITCH_MASTER || ! SWITCH_X ) { return; }
		System.err.println("(X) " + message );
		System.err.println(x.getClass().getName() + ": " + x.getMessage() );
		x.printStackTrace();
	}

	
	public static void error ( Object caller, String message )
	{
		if ( ! SWITCH_MASTER || ! SWITCH_ERROR ) { return; }
		System.err.println("(ERROR)[" + shortClassName(caller) + "] " + message );
	}

	public static void error ( String message )
	{
		if ( ! SWITCH_MASTER || ! SWITCH_ERROR ) { return; }
		System.err.println("(ERROR) " + message );
	}

	
	public static void warn ( Object caller, String message )
	{
		if ( ! SWITCH_MASTER || ! SWITCH_WARN ) { return; }
		System.err.println("(WARN)[" + shortClassName(caller) + "] " + message );
	}

	public static void warn ( String message )
	{
		if ( ! SWITCH_MASTER || ! SWITCH_WARN ) { return; }
		System.err.println("(WARN) " + message );
	}

	
	public static void info ( Object caller, String message )
	{
		if ( ! SWITCH_MASTER || ! SWITCH_INFO ) { return; }
		
		System.err.println("(INFO)[" + shortClassName(caller) + "] " + message );
	}
	
	public static void info ( String message )
	{
		if ( ! SWITCH_MASTER || ! SWITCH_INFO ) { return; }
		
		System.err.println("(INFO) " + message );
	}
	

	public static void debug ( Object caller, String message )
	{
		if ( ! SWITCH_MASTER || ! SWITCH_DEBUG ) { return; }
		
		System.err.println("(DEBUG)[" + shortClassName(caller) + "] " + message );
	}
	
	public static void debug ( String message )
	{
		if ( ! SWITCH_MASTER || ! SWITCH_DEBUG ) { return; }
		
		System.err.println("(DEBUG) " + message );
	}
	
	
	public static String shortClassName ( Object o )
	{
		
		String className = o.getClass().getName();
		
		// take the last period split token
		int lastIdx = className.indexOf('.',-1);
		if ( lastIdx > 0 ) {
			className = className.substring(lastIdx+1);
		}
		
		return className;
	}
	
	
	
	
	
	
}
