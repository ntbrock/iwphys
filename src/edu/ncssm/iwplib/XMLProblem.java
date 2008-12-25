package edu.ncssm.iwplib;

import java.util.*;

import edu.ncssm.iwp.util.*;

public class XMLProblem
{
    XMLProblemNode top;
    String url;

    /**
     * We need a parser (SAX Handler - integrated w/ Microsax) to handle
     * arbitrary XML problem formats coming in.
     */
    public XMLProblem ( XMLProblemNode top, String url )
    {
        this.top = top;
        this.url = url;
    }

    public XMLProblem ( String url )
    {
        this.url = url;
        this.top = new XMLProblemNode ( );
    }


    public String getUrl ()
    {
        return url;
    }

    /**
     * This method divides up a key by the format, then delves into the
     * nodetree contained within the XMLProblem object to find a matching
     * data note and attribute
     *
     * The key should be in the form: node.node.node.attribute
     *
     * @param key The key to search for
     * @exception UnknownKeyException key was nowehere to be found
     * @author brockman
     */
    public String getValue ( String key )
        throws UnknownKeyException
    {
        XMLNodeKey nodeKey = new XMLNodeKey ( key );
        XMLProblemNode node = _getNode ( nodeKey );
        return node.attr ( nodeKey.getAttributeName() );
    }

    public XMLProblemNode getNode ( String key )
        throws UnknownKeyException
    {
        return _getNode ( new XMLNodeKey ( key ) );
    }


    /**
     * the real workhorse of the scan method.
     * note the simplicity. it should retain that property.
     */

    private XMLProblemNode _getNode ( XMLNodeKey nodeKey )
        throws UnknownKeyException
    {
        XMLProblemNode travelNode = top;

        for ( Iterator i = nodeKey.nodeIterator(); i.hasNext(); ) {
            String nodeName = i.next().toString();
            travelNode = travelNode.node ( nodeName );

            IWPLog.info(this,"[XMLProblem._getNode] nodeName: " + nodeName );
        }

        return travelNode;
    }


    /**
     * A utilitity method for easily reading out window options that are
     * formed by the below schema
     */

    public GridWindowOptions getWindowOptions ( )
        throws UnknownKeyException
    {
        // check for the window option standard tags.

        GridWindowOptions o = new GridWindowOptions ( );
        XMLProblemNode window = top.node("window");

        o.minX = window.node("minX").attrFloat("value");
        o.minY = window.node("minY").attrFloat("value");
        o.maxX = window.node("maxX").attrFloat("value");
        o.maxY = window.node("maxY").attrFloat("value");
        o.stepX = window.node("gridX").attrFloat("value");
        o.stepY = window.node("gridY").attrFloat("value");

        o.showGrid = window.attrBoolean("showGrid");

        return o;
    }


    /**
     * A utilitity method for easily reading out window options that are
     * formed by the below schema
     */

    public TimeParameters getTimeParameters ( )
        throws UnknownKeyException
    {
        // TODO: check for the window option standard tags.

        TimeParameters o = new TimeParameters ( );
        XMLProblemNode window = top.node("time");

        o.start = window.node("start").attrFloat("value");
        o.stop = window.node("stop").attrFloat("value");
        o.step = window.node("step").attrFloat("value");
        o.frameDelayMS = window.node("frameDelayMS").attrFloat("value");

        return o;
    }

}
