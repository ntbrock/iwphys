package edu.ncssm.iwp.plugin;

import edu.ncssm.iwp.exceptions.InvalidEquationException;

import java.util.Collection;


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

public interface IWPCalculationOrder
{
    /**
     * IWP 4.5 - Each Object has the calculation ordering that's determined by 4.5 for the 5.0 web code to use.
     * @return
     */
    public int getCalculatorOrder();

    public void setCalculatorOrder(int order);

}
