package edu.ncssm.iwplib;

import java.util.*;

public class XMLProblemNode
{
    public static final String TOP_LEVEL = "_TOP_LEVEL_";

    Collection subNodes = new ArrayList ( );
    String key;
    Hashtable attributes;

    /**
     * The blank constructor should construct a new 'top' node.
     * @author brockman
     */
    public XMLProblemNode ( )
    {
        this.key = TOP_LEVEL;
        attributes = new Hashtable ( );
    }

    public XMLProblemNode ( String key, Map attrMap )
    {
        this.key = key;
        this.attributes = new Hashtable ( attrMap );
    }

    public String getKey ( )
    {
        return key;
    }

    public void addSubNode ( XMLProblemNode node )
    {
        subNodes.add ( node );
    }

    public Collection getSubNodes ( )
    {
        return subNodes;
    }

    public Iterator nodeIterator()
    {
        return subNodes.iterator();
    }

    public Iterator subNodeIterator()
    {
        return subNodes.iterator();
    }


    /**
     * Return the list of children nodes of a tag type
     */
    public Collection nodes ( String key )
    {
        Collection out = new ArrayList ( );
        for ( Iterator i = subNodes.iterator(); i.hasNext(); ) {
            XMLProblemNode scan = (XMLProblemNode) i.next();

            if ( key.equals ( scan.getKey() ) ) {
                out.add ( scan );
            }
        }

        return out;
    }


    // find the node tha matches the key
    public XMLProblemNode node ( String key )
        throws MultipleNodesPresentException, UnknownKeyException
    {
        XMLProblemNode match = null;

        for ( Iterator i = subNodes.iterator(); i.hasNext(); ) {
            XMLProblemNode scan = (XMLProblemNode) i.next();

            if ( key.equals ( scan.getKey() ) ) {
                if ( match == null ) {
                    // correct. one and only one
                    match = scan;
                } else {
                    // whoops- there is at least 1 more w/ the same key
                    throw new MultipleNodesPresentException ( );
                }
            }

        }

        if ( match == null ) { throw new UnknownKeyException ( key ); }

        return match;
    }

    public String attr ( String attrKey )
        throws UnknownKeyException
    {
        return getAttribute ( attrKey );
    }

    public float attrFloat ( String attrKey )
        throws UnknownKeyException
    {
        return Float.parseFloat ( getAttribute ( attrKey ) );
    }

    public boolean attrBoolean ( String attrKey )
        throws UnknownKeyException
    {
        try {

            // return Boolean.parseBoolean ( getAttribute ( attrKey ) );
            // shucks. the above line dseon't exist, so we have to do it ourselves

            String value = getAttribute ( attrKey );
            if ( value.equals ( "true" ) ) { return true; }
            else { return false; }

        } catch ( UnknownKeyException e ) {
            // if not defined, then default to false
            return false;
        }
    }


    public String getAttribute ( String attrKey )
        throws UnknownKeyException
    {
        String out = ( String ) attributes.get ( attrKey );
        if ( out == null ) {
            throw new UnknownKeyException ( attrKey );
        }

        return out;
    }


    public String toString ( )
    {
        StringBuffer sb = new StringBuffer ( );
        sb.append ( "XMLProblemNode {\n" );

        sb.append ( " KEY: " + key + "\n" );

        for ( Enumeration e = attributes.keys(); e.hasMoreElements ( ); ) {

            try {

                String subKey = (String) e.nextElement ( );
                sb.append (" ELEM: " + subKey + " = " + getAttribute ( subKey ) + "\n");
            } catch ( UnknownKeyException ex ) {
                // this should never be thrown
            }
        }


        for ( Iterator i = getSubNodes().iterator(); i.hasNext(); ) {

            XMLProblemNode node = (XMLProblemNode) i.next();
            sb.append (" SUB: " + node.getKey() + "\n" );
        }


        sb.append ( "}\n" );
        return sb.toString ( );
    }

}
