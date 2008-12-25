/**
 * This Canvas is specificallly designed for drawing animated 2 Dimensional
 * scaled graphics
 *
 * @author brockman
 */

package edu.ncssm.iwplib;

import java.awt.*;
import java.util.*;

import edu.ncssm.iwp.util.*;

public class GridGraphics extends Canvas
{
    public boolean DEBUG = false;
    public void debug ( String message ) { if(DEBUG) IWPLog.info(this,message); }


    // buffer ? What kind of object for double buffering?
    // we also need another one that is for blitting.

    // NOTE: this is not really 'double buffered' per say.

    GridWindowOptions options;

    boolean bufferReady = false;

    ArrayList objectsToDraw = new ArrayList ( );

    String infoString = null; // an optional text string that appars at the lower left


    // --------------------------------------------------------------------
    // constructor

    public GridGraphics ( GridWindowOptions options,
                          String infoString )
    {
        this.options = options;
        this.infoString = infoString;
    }

    public GridWindowOptions getOptions ( ) { return options; }



    private void baseCanvas ( Graphics g )
    {
        Color gridColor = Color.lightGray;

        g.setColor ( Color.white );
        g.fillRect ( 0, 0, getWidth(), getHeight() );


        if ( options.showGrid ) {

            // draw the X grid
            for ( float i = options.minX; i < options.maxX; i += options.stepX ) {

                Color useColor = gridColor;
                if ( i == 0 ) { useColor = Color.black; }

                _drawLine ( g,
                            new DrawLine ( new GridPointImpl ( i, options.minY ),
                                           new GridPointImpl ( i, options.maxY ),
                                           useColor, 1 ) );
            }


            // draw the Y grid
            for ( float i = options.minY; i < options.maxY; i += options.stepY ) {

                Color useColor = gridColor;
                if ( i == 0 ) { useColor = Color.black; }

                _drawLine ( g,
                            new DrawLine ( new GridPointImpl ( options.minX, i ),
                                           new GridPointImpl ( options.maxX, i ),
                                           useColor, 1 ) );
            }
        }


        // draw some text?
        g.setColor ( Color.black );

        if ( infoString != null ) {
            g.drawString ( infoString, 0+5, getHeight() - 10 );
        }


    }



    // --------------------------------------------------------------------
    // double buffering code

    public void blitDoubleBuffer ( Graphics g )
    {
        bufferReady = false;

        // set the canvas to white
        // also, draw the grid lines
        baseCanvas ( g );


        // todo: draw the grid lines!

        for ( Iterator i = objectsToDraw.iterator(); i.hasNext(); ) {

            Object o = i.next();

            if ( o instanceof DrawLine ) {
                _drawLine ( g, (DrawLine) o );
            }
        }

        objectsToDraw.clear ( );
    }

    public void readyDoubleBuffer ( ) { bufferReady = true; }
    public void resetDoubleBuffer ( )
    {
        objectsToDraw.clear ( );
    }


    // --------------------------------------------------------------------
    // paint toolkit

    public void paint ( Graphics g )
    {
        debug ("[GridGraphics.paint] now");

        if ( bufferReady ) {
            blitDoubleBuffer ( g );
            debug ( "[GridGraphics.paint] blitDoubleBuffer" );
        }

    }


    // here we need to implement the drawing toolkit of the Graphics object
    // except, every point we take in, we nee to convert to our own scale
    // or make up our own? bah


    /**
     * Draw a simple, solid line from p1 to p2.
     */
    public void drawLine ( GridPoint p1, GridPoint p2, Color color, int width )
    {
        debug("[GridGraphics.drawLine] objectsToDraw.size: " + objectsToDraw.size() );
        objectsToDraw.add ( new DrawLine ( p1, p2, color, width ) );
    }


    private void _drawLine ( Graphics g, DrawLine line )
    {
        // need to translate the gridPoints

        Point tp1 = translate ( line.p1, options );
        Point tp2 = translate ( line.p2, options );

        g.setColor ( line.color );
        g.drawLine ( tp1.x, tp1.y,
                     tp2.x, tp2.y );


        // debug("[GridGraphics.drawLine] tp1.x: " + tp1.x + "  tp1.y: " + tp1.y + "  tp2.x: " + tp2.x + " tp2.y: " + tp2.y );
        debug("[GridGraphics.drawLine] p1.x: " + line.p1.getX() + "  p1.y: " + line.p1.getY() + "  p2.x: " + line.p2.getX() + " p2.y: " + line.p2.getY() );
    }


    /**
     * @param fillColor can be null
     */
    public void drawCircle ( GridPoint center, float radius, Color borderColor,
                             int width, Color fillColor )
    {


    }


    private Point translate ( GridPoint point, GridWindowOptions window )
    {

        // simple translation algorithim
        Point out = new Point ( translateDimension ( (float) point.getX(),
                                                     window.minX, window.maxX,
                                                     (float) 0, (float) getWidth() ),
                                translateDimension ( (float) point.getY(),
                                                     window.minY, window.maxY,
                                                     (float) getHeight(), (float) 0 ) ); // note the reversal of the x dimension. CS and Physics Y axis coordinates are different.

        return out;
    }


    private int translateDimension ( float pointVal,
                                     float pointMin, float pointMax,
                                     float destMin, float destMax )
    {
        return (int) ( ( ( pointVal - pointMin ) / ( pointMax - pointMin ) ) * ( destMax - destMin ) + destMin );
    }
}



class DrawLine
{
    public GridPoint p1;
    public GridPoint p2;
    public Color color;
    public int width;

    public DrawLine ( GridPoint p1, GridPoint p2, Color color, int width )
    {
        this.p1 = p1;
        this.p2 = p2;
        this.color = color;
        this.width = width;
    }
}
