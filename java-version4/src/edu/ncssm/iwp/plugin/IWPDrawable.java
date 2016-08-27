package edu.ncssm.iwp.plugin;

import edu.ncssm.iwp.exceptions.InvalidEquationException;
import edu.ncssm.iwp.exceptions.UnknownTickException;
import edu.ncssm.iwp.exceptions.UnknownVariableException;
import edu.ncssm.iwp.problemdb.DProblemState;
import edu.ncssm.iwp.graphicsengine.GColor;
import edu.ncssm.iwp.graphicsengine.IWPDrawer;

public interface IWPDrawable {
    public void iwpDraw(IWPDrawer drawer, DProblemState state)
		throws UnknownVariableException, UnknownTickException, InvalidEquationException;

    /**
     * Note: this was taken away. If an object is drawable, then it purely
     * implements the drawable interface.
     * 
     * IWP 3.
     * @return
     */
    // public boolean isDrawable();
 
    
    /**
     * Return the base color of the drawer.
     */
    public GColor getGColor();
}
