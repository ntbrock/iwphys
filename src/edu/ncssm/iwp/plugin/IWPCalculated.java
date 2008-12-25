package edu.ncssm.iwp.plugin;

import java.util.Collection;

import edu.ncssm.iwp.exceptions.InvalidEquationException;


/**
 * IWP Object Plugin Interface: IWPCalculated
 *
 * Implement this interface to take advantage of the new IWP 3 calculation ordering
 * features and Equation parsing + validation in the Desiger.
 *
 * 2007-Jan-29 brockman
 *
 * @author brockman
 *
 */

public interface IWPCalculated
{
    /**
     * New method as of IWP 3.
     * 2007-Jan-29. Brockman - Proper Calculation Order Feature
     * @author brockman
     *
     * This method returns a collection of Strings of object names that must be present
     * in the MVariables before this equation can properly tick. If a variable
     * is missing in the CURRENT time, should throw an unknown variable X.
     *
     * NOTE: I pass in the array here instead of constructing it everytime
     * to try and save constructions.
     *
     * @return The object names (Strings) that are required to calculate. Null ok.
     * NOte that this return array doesn't have to be unique.
     */

    public Collection getRequiredSymbols()
        throws InvalidEquationException;



    /**
     * New Method as of IWP 3
     * 2007-Jan-29 brockman - Proper Calculation Order Feature
     *
     * This method returns a collection of Strings of variable names that are
     * provided back into MVariables after the tick method on this object has been
     * run. This is usually just as simple as objectname.disp, objectname.vel,
     * objectname.accel. I have decided to start being explicit about which variables
     * are in the problem and not.
     *
     *
     * @return
     * @throws InvalidEquationException
     */

    public Collection getProvidedSymbols()
        throws InvalidEquationException;


}
