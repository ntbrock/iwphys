package edu.ncssm.iwp.graphicsengine;

import edu.ncssm.iwp.objects.DObject_Window;

import java.awt.*;

/* This class will provide the mapping between
 * IWP's coordinates and Graphic's coordinates.
 *
 * 2007-May-14 Brockman : Added the drawRect and drawOval methods, refactored.
 *
 * TODO: Extract the coordinate conversion logic to a single point in this file.
 * Better yet, figure out how to do swing canvas coordinate translations.
 *
 */

public class IWPDrawer
{
    protected Graphics g;
    //protected Graphics2D g2;
    protected DObject_Window window;

    public IWPDrawer(Graphics inG, DObject_Window inW) {
    g=inG;
    
    //g2 = (Graphics2D)g.create();
    //g2.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY));
    //g2.addRenderingHints(new RenderingHints(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC));
    
    window=inW;
    }

	public Color getColor() { return g.getColor(); }
	public void setColor(Color c) { g.setColor(c); }

	/**
	 * Fill a rectangle in w/ the current color.
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	
    public void fillRect(double x, double y, double w, double h, double a)
    {
    	_internalRect (x,y,w,h,a, true);
    }

    /**
     * Draw the outer edge of a rectangle with the current color.
     * @param x
     * @param y
     * @param w
     * @param h
     */
    
    
    /*Only used by wavebox?*/
    public void drawRect ( double x, double y, double w, double h )
    {
    	_internalRect (x,y,w,h,0, false);
    }
    
    private void _internalRect ( double x, double y, double w, double h, double a, boolean fill )
    {
   		y=-y; //b/c the coords are mapped upside down.
   		int tX=window.getDrawX(x);
   		int tY=window.getDrawY(y);
   		int tW=window.getDrawX( x+w/2 ) - window.getDrawX(x-w/2);
   		int tH=(window.getDrawY((y+h/2) ) - window.getDrawY((y-h/2)));
   		
   		//New instance of g2d
   		Graphics2D g2 = (Graphics2D)g.create();
   		
   		g2.setColor(g.getColor()); // set rotaty's color to same at the actual object.
   		
   		g2.rotate(-a,tX+(tW/2),tY+(tH/2));

   		if ( fill ) { 
   			g2.fillRect(tX,tY,tW,tH);
   		} else {
   			g2.drawRect(tX,tY,tW,tH);
   		}
    }
    
    /**
     * Fill an oval in with the current color.
     * @param x
     * @param y
     * @param w
     * @param h
     */
    
    public void fillOval(double x, double y, double w, double h)
    {
    	_internalOval ( x, y, w, h, true );
    }

    /**
     * Draw the outer edge of an oval with the current color.
     * @param x
     * @param y
     * @param w
     * @param h
     */
    
    public void drawOval(double x, double y, double w, double h)
    {
    	_internalOval ( x, y, w, h, false );
    }

    
    private void _internalOval ( double x, double y, double w, double h, boolean fill )
    {

        //g.fillOval(window.getDrawX( x ),window.getDrawY( y ),
        //   window.getDrawX(w), window.getDrawY(h));
        y=-y;
        
        int tX = window.getDrawX(x);
        int tY = window.getDrawY(y);
        int tW = window.getDrawX( x+w/2 ) - window.getDrawX(x-w/2);
        int tH =(window.getDrawY((y+h/2) ) - window.getDrawY((y-h/2)));
        
        if ( fill ) { 
        	g.fillOval( tX, tY , tW, tH );
        } else { 
        	g.drawOval( tX, tY, tW, tH );
        }            
    }
    
    
    
    public void drawString( String text, int fontPointSize, double x, double y )
    {
    	g.setFont(new Font("", Font.BOLD, fontPointSize));
    	g.drawString(text, window.getDrawX(x), window.getDrawY(-y));
    }
    
    
    public void drawLine(double x, double y, double xx, double yy)
    {
    	//IWPLog.info(this,"[IWPDrawer] in: "+x+":"+y+":"+xx+":"+yy);
    	//IWPLog.info(this,"[IWPDrawer] out: "+x+":"+y+":"+xx+":"+yy);
    	g.drawLine(window.getDrawX( x ),window.getDrawY( -y ),
    			   window.getDrawX(xx), window.getDrawY(-yy));

	}
    
    //Cory: 4/22/2008
    //Overload for stroke size, used by the graphing plugin
    public void drawLine(double x, double y, double xx, double yy, int stroke)
    {
    	for(int b=-stroke/2; b<stroke; b++) {
    		g.drawLine(window.getDrawX( x ),window.getDrawY( -y )+b,
    				window.getDrawX(xx), window.getDrawY(-yy)+b);
    	}
    }
    

    public void fillPolygon(double[] x,double[] y,int num)
    {
    	int[] xDraw=new int[num];
    	int[] yDraw=new int[num];
    	for(int i=0;i<num;i++) {
    		xDraw[i]=mapX(x[i]);
    		yDraw[i]=mapY(-y[i]);
    	}
    	g.fillPolygon(xDraw,yDraw,num);
    }


    // Re-added this because it left us
    public void drawImage(Image img, double x, double y, double w, double h, double a) {
  		y=-y; //b/c the coords are mapped upside down.
  		
 
  		double tW=((window.getDrawX( x+w/2 ) - window.getDrawX(x-w/2))*2.3);
   		double tH=((window.getDrawY((y+h/2) ) - window.getDrawY((y-h/2)))*2.3);
  		
   		  		
   		double tX=(window.getDrawX(x)-tW/2);
   		double tY=(window.getDrawY(y)-tH/2);

   	
   		//New instance of g2d
   		Graphics2D g2 = (Graphics2D)g.create();
   		
   		g2.rotate(-a,tX+tW/2,tY+tH/2);
   		

   		//make sure the image is loaded.  Pause 100ms until it is.
   		
   		
   		
   		while(!g2.drawImage(img,(int)Math.floor(tX),(int)Math.floor(tY),(int)Math.floor(tW),(int)Math.floor(tH),null)) {
   			try {Thread.sleep(100);} catch (InterruptedException e){};
   		}
    }

    
    public Graphics getGraphics() {return g;}
    public DObject_Window getDOject_Window() {return window;}

    protected int mapX(double in) {return window.getDrawX(in);}
    protected int mapY(double in) {return window.getDrawY(in);}
}
