package edu.ncssm.iwp.toolkit.xml;

import java.io.Writer;
import java.io.IOException;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Enumeration;

import edu.ncssm.iwp.util.*;

public class XMLElement
{

	final String tab = new String ("\t");
	final String newLine = new String ("\n");


	Hashtable attributes = new Hashtable();
	ArrayList subElements = new ArrayList();
	String name;
	String text = null;
	boolean setText = false;

	/*--------------------------------------------------------------------*/

	public XMLElement ( String name ) 
	{
		this.name = name;
	}

	public XMLElement ( String name, String text ) 
	{
		this.name = name;
		setText ( text );
	}


	/*--------------------------------------------------------------------*/

	public void addAttribute ( String name, String value )
	{
		attributes.put ( name, value );
	}

	public void addElement ( XMLElement element )
	{
		subElements.add ( element );
	}

	public void setText ( String text )
	{
		this.setText = true;
		this.text = text;
	}

	/*--------------------------------------------------------------------*/

	public String getAttrString ( )
	{
		StringBuffer sb;
		Enumeration e = attributes.keys();

		if ( e.hasMoreElements() ) {
			sb = new StringBuffer ();
		} else {
			return new String("");
		}


		while ( e.hasMoreElements() ) {
			String key = (String) e.nextElement();
			String value = (String) attributes.get ( key );
			
			sb.append ( " " + key + "=\"" + value + "\"" );
		}

		return sb.toString();
	}
		

	public void toXML ( Writer out ) 
		throws IOException
	{
		toXML ( out, 0 );
	}

	public void toXML ( Writer out, int depth ) 
		throws IOException
	{

		String outerPad = makePad ( depth );
		String attrs = getAttrString ( );

		if ( text == null && subElements.size() > 0 ) { 
			out.write ( outerPad + "<" + name + attrs + ">" );
			out.write ( newLine );
			
			for ( int i=0; i < subElements.size(); i++ ) { 

				XMLElement elem = (XMLElement) subElements.get(i);

				if ( elem != null ) { 
					elem.toXML ( out, depth + 1 );
				} else { 
					IWPLog.info(this,"[XMLElement] ERROR: Child Element Null in tag: " + name );
				}

			}

			out.write ( outerPad + "</" + name + ">" + newLine );

		} else { 

			if ( setText == false ) { 
				out.write ( outerPad + "<" + name + attrs + "/>" + newLine );

			} else { 
				
				out.write ( outerPad + "<" + name + attrs + ">" );

				/* just leave nulls blank, don't write text */
				if ( text != null ) { out.write ( text ); }
					
				out.write ( "</" + name + ">" + newLine );
			}
		}


	}


	/*--------------------------------------------------------------------*/

	private String makePad ( int depth )
	{
		StringBuffer sb = new StringBuffer("");
		for ( int i=0; i < depth; i++ ) {
			sb.append ( tab );
		}
		return sb.toString();
	}


}
