package edu.ncssm.iwp.util;

public class TEST_IWPLog
{

	public static void main ( String args[] )
	{
		IWPLog_TestImpl impl = new IWPLog_TestImpl();
		impl.test();
	}
}


class IWPLog_TestImpl
{
	
	public IWPLog_TestImpl() {}
	
	public void test ( )
	{
		IWPLog.debug(this, "I am a debug message");
		IWPLog.info(this, "I am an info message");
		IWPLog.error(this, "I am an error message");
		try { 
			Object a = null;
			String b = a.toString();
			if ( b == null ) { } // get rid of warning.
			
		} catch ( NullPointerException x ) { 
			IWPLog.x(this, "I am an exception message", x );
		}
	}
	
}