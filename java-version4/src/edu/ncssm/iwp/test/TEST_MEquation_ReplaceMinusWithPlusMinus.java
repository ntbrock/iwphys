
package edu.ncssm.iwp.test;

import edu.ncssm.iwp.math.*;

import edu.ncssm.iwp.util.*;

public class TEST_MEquation_ReplaceMinusWithPlusMinus
{

    public static void main ( String args[] )
    {
        if ( args.length < 1 ) {
            IWPLog.out("[TEST_MEquation_ReplaceMinusWithPlusMinus] Need Argument of an Equation");
            System.exit(1);
        }

        String eqn = args[0];

        System.out.println("BEFORE: " + eqn );

        eqn = MEquation_Parser.replaceMinusWithPlusMinus ( eqn );

        System.out.println("AFTER: " + eqn );
    }
}
