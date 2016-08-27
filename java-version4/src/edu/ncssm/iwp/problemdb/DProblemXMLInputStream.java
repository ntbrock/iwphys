package edu.ncssm.iwp.problemdb;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import edu.ncssm.iwp.util.IWPLog;


/**
 * IWP3 - Written to remove the Strange Unicode chatacters from the xml files
 * and replace them with xml entities.
 * 
 * This overrides the read function of an input stream. When the reader 
 * detects a bad byte it will inject some characters into the stream and
 * eliminate the bad byte.
 * 
 * @author brockman
 *
 */

public class DProblemXMLInputStream extends InputStream
{
	int lineNumber = 0;
	
	public static final int ASCII_MAXGOOD = 123;
	
	public static final int ASCII_NEWLINE = 13;

	public static final int ASCII_MICRO = 181;
	public static final String ENTITY_MICRO = "u";
	
	public static final int ASCII_DEGREE = 176;
	public static final String ENTITY_DEGREE = "deg";
	
	public static final int ASCII_POWER2 = 178;
	public static final String ENTITY_POWER2 = "^2";
		
	
	InputStream source;
	ArrayList charsToInject = new ArrayList();	
	
	public DProblemXMLInputStream(InputStream source)
	{
		this.source = source;
	}
	
	public int read() throws IOException
	{
		int b;
		
		if ( charsToInject.size() > 0 ) {
			
			b = nextCharToInject();
			
		} else {
					
			b = source.read();
	
			if ( b == ASCII_NEWLINE ) { 
				lineNumber++;
			}
			
			if ( b > ASCII_MAXGOOD ) { 
				IWPLog.warn(this, "Possibly Bad Byte: " + (b+"(" + ((char)b) +"), "));
			}
			
			if ( b == ASCII_DEGREE ) {
				injectString(ENTITY_DEGREE);
				b = nextCharToInject();
			} else if ( b == ASCII_POWER2 ) {
				injectString(ENTITY_POWER2);
				b = nextCharToInject();
			} else if ( b == ASCII_MICRO ) {
				injectString(ENTITY_MICRO);
				b = nextCharToInject();
			}
		}
				
		return b;
	}

	
	
	private void injectString ( String s )
	{
		char chars[] = s.toCharArray();
		
		for ( int i = 0; i < chars.length; i++ ) {
			charsToInject.add(new Integer(chars[i]));	
		}

	}
	
	private int nextCharToInject()
	{
		Integer i = (Integer) charsToInject.get(0);
		charsToInject.remove(i);
		return i.intValue();
	}
	
}
