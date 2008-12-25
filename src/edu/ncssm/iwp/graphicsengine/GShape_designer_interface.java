package edu.ncssm.iwp.graphicsengine;

import edu.ncssm.iwp.exceptions.InvalidEquationException;

public interface GShape_designer_interface {
    public GShape get(GShape in)
    	throws InvalidEquationException;
    public void setShape(GShape in);
}
