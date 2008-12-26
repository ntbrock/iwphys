// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
//
// For full copyright information, see main source file,
// "iwp.java"
//
// 2008-Dec-25 brockman, touching this file so that the CVS HEAD tag will apply

package edu.ncssm.iwp.graphicsengine;

import java.awt.Image;
import java.awt.Toolkit;


import edu.ncssm.iwp.exceptions.InvalidEquationException;
import edu.ncssm.iwp.exceptions.MagicFileNotFoundX;
import edu.ncssm.iwp.exceptions.UnknownTickException;
import edu.ncssm.iwp.exceptions.UnknownVariableException;
import edu.ncssm.iwp.objects.DObject_Solid;
import edu.ncssm.iwp.problemdb.DProblemState;
import edu.ncssm.iwp.util.IWPMagicFile;


public class GShape_Bitmap extends GShape
{
    private Image img;
    protected String fileString;

    public GShape_Bitmap ( DObject_Solid solid )
    {
        super( solid, "1", "1", "0" ); // default is a 1x1 circle, with no rotation

    }

    public GShape_Bitmap ( DObject_Solid solid,
                              String widthStr, String heightStr, String angleStr )
    {
        super( solid, widthStr, heightStr, angleStr );
    }

    public String getType() { return GShape.STRING_BITMAP; }
    public String getParams() { return ""; }
    public String getFile() {return fileString; }
    public void setFile(String _file) { makeFilename(_file);}

    public void makeFilename(String fName) {
        fileString = fName;



        IWPMagicFile file = new IWPMagicFile(fileString);
        //MediaTracker m = new MediaTracker(new JPanel());
        //System.out.println(fileString);
        try { img = Toolkit.getDefaultToolkit().createImage(file.readBytes());
        } catch ( MagicFileNotFoundX e ) {
            System.err.println("GShape_Bitmap: Bitmap Does Not Exist: " + fileString );
        }


        //Image has been created, now make sure it is loaded by thread pausing
        while(img.getWidth(null)<0) {
            try {Thread.sleep(100);} catch (InterruptedException e){};
        }



    }

    public void iwpDraw ( IWPDrawer g,
                          DProblemState state )
        throws InvalidEquationException, UnknownVariableException, UnknownTickException
    {

        double w = super.getSolidWidth(state);
        double h = super.getSolidHeight(state);
        double a = super.getSolidAngle(state);
        double x = super.getSolidX(state);
        double y = super.getSolidY(state);



        g.drawImage(img,x,y,w,h,a);


        // super call. This shows object trails if selected.
        commonDraw ( g, state);
    }

}

