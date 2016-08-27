package edu.ncssm.iwp.applets.collision;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.net.*;
import javax.swing.*;

import edu.ncssm.iwplib.*;
import edu.ncssm.iwplib.Polygon;
import edu.ncssm.iwp.util.IWPLog;
import edu.ncssm.iwp.exceptions.InvalidEquationException;

public class Collision extends JApplet
{
	private static final long serialVersionUID = 1L;
    BumperProblem problem;
    GridGraphics graphics;
    ProblemTimer timer;

    public void init ( )
    {

        try {

            String url = getParameter("problem");

            if ( url == null ) {
                IWPLog.error(this, "no 'problem' parameter defined");
                return;
            }

            // read the problem in + setup the graphics display
            problem = ProblemReader.readProblem ( url );
            graphics = new GridGraphics ( problem.getWindowOptions ( ), "Collision Applet - brockman 2003" );
            timer = new ProblemTimer ( problem.getTimeParameters ( ) );

            getContentPane().setLayout ( new BorderLayout ( ) );
            getContentPane().add ( BorderLayout.CENTER, graphics );

        } catch ( MalformedURLException e ) {
        	IWPLog.error(this, "[MalformedURLException] " + e );
            e.printStackTrace ();
        } catch ( InvalidProblemException e ) {
        	IWPLog.error(this, "[InvalidProblemException] " + e );
            e.printStackTrace ();
        } catch ( IOException e ) {
        	IWPLog.error(this, "[IOException] " + e );
            e.printStackTrace ();
        }

    }


    public void start ( )
    {
        // play the simulation

        // do the time iteration here -- possibly in a side thread?
        // STOPPING FOR THE NIGHT. GOOD STUFF. 01.16.03

        graphics.resetDoubleBuffer ( );
        problem.paint ( graphics, timer );

        graphics.readyDoubleBuffer ( );

        repaint ( );

        CollisionThread thread = new CollisionThread ( this );
        thread.start ( );

        // run!
    }

    public void stop ( )
    {

    }



    public void timeStep ( )
    {
        graphics.resetDoubleBuffer ( );

        IWPLog.info(this, "TimeStep: " + timer.getTimeStep() );
        timer.stepTime ( );
        problem.paint ( graphics, timer );

        graphics.readyDoubleBuffer ( );

        graphics.repaint();
        repaint ( );

    }



}


class CollisionThread extends Thread
{
    Collision applet = null;

    public CollisionThread ( Collision applet )
    {
        this.applet = applet;
    }

    public void run ( )
    {
        boolean keepRunning = true;
        while ( keepRunning ) {

            applet.timeStep ( );

            try {
                Thread.sleep ( 500 );
            } catch ( InterruptedException e ) {
                keepRunning = false;
            }

        }
    }
}



class ProblemReader
{
    /**
     * Download the contents of the URL, parse them, and create a CollState
     * object based on the problem file definition
     * @param url The location (http) of the file to read in as the problem
     * @exception IOException thrown from the HTTP access
     * @author brockman
     */
    public static BumperProblem readProblem ( String url )
        throws IOException, MalformedURLException, InvalidProblemException
    {
        try {
            BumperProblem problem = new BumperProblem ( );

            // Interface to the IWP
            XMLProblem xml = XMLProblemParser.loadProblem ( url );

            // read the window size
            problem.setWindowOptions ( xml.getWindowOptions () );

            // read the window size
            problem.setTimeParameters ( xml.getTimeParameters () );


            // now digest the bumpers
            XMLProblemNode bumpers = xml.getNode ( "bumpers" );
            IWPLog.debug("[Collision.ProblemReader] bumpers = " + bumpers );

            for ( Iterator i = bumpers.nodeIterator(); i.hasNext(); ) {
                XMLProblemNode bumper = (XMLProblemNode) i.next();

                if ( bumper.getKey ().equals ( PolyBumper.XML_NAME ) ) {
                    problem.addBumper ( PolyBumper.xmlConstruct ( bumper ) );
                } else if ( bumper.getKey().equals ( CircleBumper.XML_NAME ) ) {
                    problem.addBumper ( CircleBumper.xmlConstruct ( bumper ) );
                } else {
                    throw new InvalidProblemException ("Unknown bumper type: " + bumper.getKey() );
                }
            }

            return problem;
        } catch ( InvalidEquationException e ) {
            throw new InvalidProblemException ( e );
        } catch ( XMLParserException e ) {
            throw new InvalidProblemException ( e );
        } catch ( UnknownKeyException e ) {
            throw new InvalidProblemException ( e );
        }
    }
}



class BumperProblem
{
    TimeParameters time;
    GridWindowOptions window;
    Collection bumpers = new ArrayList ( );


    public void setWindowOptions ( GridWindowOptions window )
    {
        this.window = window;
    }

    public GridWindowOptions getWindowOptions ( )
    {
        return window;
    }


    public void setTimeParameters ( TimeParameters time )
    {
        this.time = time;
    }

    public TimeParameters getTimeParameters ( )
    {
        return time;
    }


    public void addBumper ( Bumper bumper )
    {
        bumpers.add ( bumper );
    }


    public void paint ( GridGraphics g, ProblemTimer t )
    {
        // paint all sub-objects
        for ( Iterator i = bumpers.iterator(); i.hasNext(); ) {
            Bumper bumper = (Bumper) i.next();
            bumper.paint ( g, t );
        }
    }
}




abstract class Bumper
{
    public abstract void paint ( GridGraphics g, ProblemTimer t );

    public abstract Polygon getPolygon ( );
}

class PolyBumper extends Bumper
{
    public static String XML_NAME = "polyBumper";

    int LINE_WIDTH = 3;

    Polygon polygon;

    Color color;
    float mass;
    BumperState state;

    public PolyBumper ( Collection points,
                        MotionVector2D motion,
                        float mass,
                        Color color )
    {
        this.polygon = new Polygon ( points );
        this.mass = mass;
        this.color = color;

        this.state = new BumperState ( motion );
    }

    public boolean hasCollidedWith ( Bumper other )
    {
        // geometrical collision math. Ooh, this would be a great
        // thing to export into a geom collision library!

        return Geometry.shapesOverlap ( polygon, other.getPolygon() );
    }

    public Polygon getPolygon ( ) { return polygon; }

    public BumperState getState ( ) { return state; }
    public float getMass ( ) { return mass; }


    public MotionPoint2D calculatePosition ( int step )
    {
        MotionPoint2D newPosition = state.motion.calculate ( step );
        return newPosition;
    }


    public void paint ( GridGraphics g, ProblemTimer t )
    {

        // draw this guy's data on the graph, based on his state information

        int step = t.getTimeStep();

        MotionPoint2D position = calculatePosition ( step );

        MotionPoint2D firstPoint = null;
        MotionPoint2D point = null;

        Collection points = polygon.getPoints ( );

        IWPLog.debug(this, "[PolyBumper.paint] points.size: " + points.size() + "   color: " + color );
        IWPLog.debug(this, "[PolyBumper.paint] position: " + position );


        for ( Iterator i = points.iterator(); i.hasNext(); ) {

            MotionPoint2D newPoint = (MotionPoint2D) i.next();

            if ( point != null ) {

                g.drawLine ( MotionPoint2D.addPoints ( point, position ),
                             MotionPoint2D.addPoints ( newPoint, position ),
                             color, LINE_WIDTH );
            } else {
                firstPoint = newPoint;
            }
            point = newPoint;
        }

        // close the loop up
        g.drawLine ( MotionPoint2D.addPoints ( point, position ),
                     MotionPoint2D.addPoints ( firstPoint, position ),
                     color, LINE_WIDTH );

    }


    public static PolyBumper xmlConstruct ( XMLProblemNode node )
        throws UnknownKeyException, InvalidEquationException
    {
        // mass
        float mass = node.attrFloat("mass");

        // color
        Color color = Color.decode ( node.attr("color") );

        // get the points
        Collection points = new ArrayList();
        for ( Iterator i = node.node ( "boundary" ).nodes("point").iterator(); i.hasNext(); ) {
            XMLProblemNode point = (XMLProblemNode) i.next();
            points.add ( new MotionPoint2D ( point.attrFloat("x"),
                                             point.attrFloat("y") ) );
        }

        // motion
        MotionVector xVector =
            new MotionVector ( node.node("motionX").attrFloat("initDisp"),
                               node.node("motionX").attrFloat("initVelocity"),
                               node.node("motionX").attr("accel") );

        MotionVector yVector =
            new MotionVector ( node.node("motionY").attrFloat("initDisp"),
                               node.node("motionY").attrFloat("initVelocity"),
                               node.node("motionY").attr("accel") );

        MotionVector2D motion = new MotionVector2D ( xVector, yVector );

        return new PolyBumper ( points, motion, mass, color );
    }
}



class BumperState
{
    public MotionVector2D motion;

    public BumperState ( MotionVector2D motion )
    {
        this.motion = motion;
    }

}




class CircleBumper
{
    public static final String XML_NAME = "circleBumper";


    public static PolyBumper xmlConstruct ( XMLProblemNode node )
        throws UnknownKeyException
    {
        throw new UnknownKeyException ("METHOD NOT DONE");
    }

}

