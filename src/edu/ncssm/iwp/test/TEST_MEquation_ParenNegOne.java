package edu.ncssm.iwp.test;

import java.util.ArrayList;
import java.util.Iterator;

import edu.ncssm.iwp.exceptions.InvalidEquationException;
import edu.ncssm.iwp.exceptions.UnknownVariableException;
import edu.ncssm.iwp.math.MEquation_Parser;
import edu.ncssm.iwp.math.MVariables;
import edu.ncssm.iwp.math.MVariablesFactory;
import edu.ncssm.iwp.util.IWPLog;

public class TEST_MEquation_ParenNegOne
{

    public static void main ( String args[] )
    {
        int count = 0;
        ArrayList array = new ArrayList();

        double correctAnswer = -2;

        // n = -1
        array.add ( "-1-1" );
        array.add ( "n-1" );
        array.add ( "-1+n" );
        array.add ( "(-1)-1" );
        array.add ( "(-1-1)" );
        array.add ( "-1+(-1)" );
        array.add ( "(-1)+(-1)" );
        array.add ( "-1-(1)" );
        array.add ( "-1+-1" );

        MVariables vars = MVariablesFactory.newInstance();
        vars.set("n",-1 );

        try {

            for ( Iterator i = array.iterator(); i.hasNext(); ) {

                count++;

                System.out.println("--[ Case " + count + " ]-----------------------------------");

                String eqn = i.next().toString();
                MEquation_Parser parser = new MEquation_Parser ( eqn );

                double nResult = parser.calculate ( vars );

                System.out.println( eqn + " = " + nResult );

                if ( nResult != correctAnswer ) {
                    IWPLog.error("BROKEN: " + eqn + " = " + nResult );
                }

            }

        } catch ( UnknownVariableException e ) {
            IWPLog.x("UnknownVariable: " + e, e );
        
        } catch ( InvalidEquationException e ) {
            IWPLog.x("InvalidEquation: " + e, e );
        }
    }

}
