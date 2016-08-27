package edu.ncssm.iwp.util;

import javax.swing.*;

/**
 * This is a class that wraps the functionality of IWPLog with calls to 
 * JDialog to show popup windows for errors and info messages.
 * 
 * 2006-Aug-18 brockman
 * 
 * @author brockman
 *
 */

public class IWPLogPopup
{
	public static boolean SWITCH_MASTER		= true;  // Turn off all chatter.
	
	public static boolean SWITCH_OUT 		= true;  // Print 'out' lines.
	public static boolean SWITCH_X 			= true;  // Print exceptions
	public static boolean SWITCH_ERROR 		= true;  // Print errors
	public static boolean SWITCH_WARN 		= true;  // Print warnings
	public static boolean SWITCH_INFO 		= true;  // Print Info's
	public static boolean SWITCH_DEBUG 		= false; // Print Debugs.
	

	// NOTE: I put the calls to IWPLog before the returns, so I can control
	// each independently.

	public static void out ( String message )
	{
		IWPLog.out( message );
		if ( ! SWITCH_MASTER || ! SWITCH_OUT ) { return; }
		
		popup( message, "IWP Out", JOptionPane.INFORMATION_MESSAGE ); 
	}

	
	public static void out ( Object caller, String message)
	{
		IWPLog.out( caller, message );
		
		if ( ! SWITCH_MASTER || ! SWITCH_OUT ) { return; }
		popup( "(OUT)[" + shortClassName(caller) + "]\n" + message, "IWP Out", JOptionPane.INFORMATION_MESSAGE  ); 
	
	}


	public static void x ( Object caller, String message, Exception x )
	{
		IWPLog.x(caller,message,x);
		
		if ( ! SWITCH_MASTER || ! SWITCH_X ) { return; }
		popup ( "[" + shortClassName(caller) + "] " + shortClassName(x) + "\n" + message,
				"Exception",
				JOptionPane.ERROR_MESSAGE
		);

	}

	public static void x ( String message, Exception x )
	{
		IWPLog.x(message,x);
		
		if ( ! SWITCH_MASTER || ! SWITCH_X ) { return; }

		popup( shortClassName(x) + "\n" + message,
				"Exception",
				JOptionPane.ERROR_MESSAGE
		);

	}

	
	public static void error ( Object caller, String message )
	{
		IWPLog.error(caller, message);
		
		if ( ! SWITCH_MASTER || ! SWITCH_ERROR ) { return; }

		popup("[" + shortClassName(caller) + "]\n" + message,
				"Error",
				JOptionPane.ERROR_MESSAGE
		);
	}

	public static void error ( String message )
	{
		IWPLog.error(message);
		
		if ( ! SWITCH_MASTER || ! SWITCH_ERROR ) { return; }

		popup( message,
				"Error",
				JOptionPane.ERROR_MESSAGE
		);

	}

	
	public static void warn ( Object caller, String message )
	{
		IWPLog.warn(caller,message);
		
		if ( ! SWITCH_MASTER || ! SWITCH_WARN ) { return; }
		
		popup("[" + shortClassName(caller) + "]\n" + message,
				"Warning",
				JOptionPane.ERROR_MESSAGE
		);
	}

	public static void warn ( String message )
	{
		IWPLog.warn(message);
		
		if ( ! SWITCH_MASTER || ! SWITCH_WARN ) { return; }

		popup( message,
				"Warning",
				JOptionPane.ERROR_MESSAGE
		);
	}

	
	public static void info ( Object caller, String message )
	{
		IWPLog.info(caller,message);
		if ( ! SWITCH_MASTER || ! SWITCH_INFO ) { return; }

		popup(	"[" + shortClassName(caller) + "]\n" + message,
				"Info",
				JOptionPane.INFORMATION_MESSAGE
		);
		
	}
	
	public static void info ( String message )
	{
		IWPLog.info(message);
		
		if ( ! SWITCH_MASTER || ! SWITCH_INFO ) { return; }
		

		popup( message,
				"Info",
				JOptionPane.INFORMATION_MESSAGE
		);
	
	}
	

	public static void debug ( Object caller, String message )
	{
		IWPLog.debug(caller,message);
		
		if ( ! SWITCH_MASTER || ! SWITCH_DEBUG ) { return; }
		

		popup("[" + shortClassName(caller) + "]\n" + message,
				"Debug",
				JOptionPane.INFORMATION_MESSAGE
		);
		
	}
	
	public static void debug ( String message )
	{
		IWPLog.debug(message);
		
		if ( ! SWITCH_MASTER || ! SWITCH_DEBUG ) { return; }
		

		popup( message,
				"Info",
				JOptionPane.INFORMATION_MESSAGE
		);
		
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

	
	
	private static void popup( String message, String title, int type )
	{


//		BOOKMARK, fire this popup later.
		class MessageDialog implements Runnable
		{
			String message;
			String title;
			int type;
		
			public MessageDialog( String message, String title, int type )
			{
				this.message = message;
				this.title = title;
				this.type = type;
			}
			
			public void run()
			{
				JOptionPane.showMessageDialog( new JFrame(), message, title, type );
			}
		}

		// Queue call to prevent this from locking up the display
		SwingUtilities.invokeLater(new MessageDialog( message, title, type));		
	}
	
}

