package edu.ncssm.iwp.plugin;

import edu.ncssm.iwp.exceptions.UnknownTickException;
import edu.ncssm.iwp.exceptions.UnknownVariableException;
import edu.ncssm.iwp.graphicsengine.GColor;
import edu.ncssm.iwp.graphicsengine.GShape_GraphPropertySelector;
import edu.ncssm.iwp.graphicsengine.IWPGrapher;
import edu.ncssm.iwp.problemdb.DProblemState;


public interface IWPGraphable
{
    public void iwpGraph(IWPGrapher drawer, DProblemState state)
		throws UnknownVariableException, UnknownTickException;
    
    //public void IWPGraph(IWPDrawer drawer);
    //need to think about the Graph method a bit more & probably model after IWPDraw
    public GShape_GraphPropertySelector getGraphOptionPanel();
    
    public GColor getGColor();
    
}
