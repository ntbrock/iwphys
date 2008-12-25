package edu.ncssm.iwplib;

import java.util.*;

import edu.ncssm.iwp.util.*;

public class XMLNodeKey
{
	String key;
	NodeKeyExploded exploded;

	public XMLNodeKey ( String key )
	{
		this.key = key;
		this.exploded = new NodeKeyExploded ( this.key );
	}

	public Iterator nodeIterator ( )
	{
		return exploded.nodes.iterator();
	}


	public String getAttributeName ( )
	{
		return exploded.attribute;
	}
	
}


class NodeKeyExploded
{
	public String attribute;
	public Collection nodes;
	
	public static final String DELIM = ".";

	public NodeKeyExploded ( String key )
	{
		parse ( key );
	}

	private void parse ( String key ) 
	{
		nodes = new ArrayList ( );

		if ( key.indexOf ( DELIM ) >= 0 ) { 

			for ( StringTokenizer st = new StringTokenizer ( key, DELIM );
				  st.hasMoreTokens(); ) { 

				String nodeName = st.nextToken();
				
				if ( st.hasMoreTokens() ) { 
					nodes.add ( nodeName );
				} else { 
					attribute = nodeName;
				}
			}
		} else { 
			// accomidate for no DELIM's.
			nodes.add ( key );
		}

		IWPLog.info(this,"[XMLNodeKeyExploded] nodes.size(): " + nodes.size() );
	}
		
}
