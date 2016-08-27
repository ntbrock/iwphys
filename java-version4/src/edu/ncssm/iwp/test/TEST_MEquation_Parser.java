package edu.ncssm.iwp.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import edu.ncssm.iwp.exceptions.InvalidEquationException;
import edu.ncssm.iwp.exceptions.UnknownVariableException;
import edu.ncssm.iwp.math.MEquation_Parser;
import edu.ncssm.iwp.math.MVariables;
import edu.ncssm.iwp.math.MVariablesFactory;
import edu.ncssm.iwp.util.IWPLog;

public class TEST_MEquation_Parser
{

    static String sEquation;
    static MVariables oVariables;

    static boolean showPrompts = true;

    public static void main ( String args[] )
    {
        if ( args.length > 1 ) {
            showPrompts = false;
        }


        System.out.println("[TEST_MEquation_ParenNegOne]");
        System.out.println("Enter your Equation first, and then var / val pairs (ex: x=2)");
        System.out.println("");

        oVariables = readVariables ();


        System.out.println("");
        System.out.println("Calculating...");

        try {
            MEquation_Parser parser = new MEquation_Parser ( sEquation );
            double nResult = parser.calculate ( oVariables );
            System.out.println("Result: "+nResult );
        } catch ( InvalidEquationException e ) {
            IWPLog.x ("InvalidEquation: " + e, e );
        } catch ( UnknownVariableException e ) {
            IWPLog.x ("UnknownVariableException: " + e, e );
        }



    }


    public static MVariables readVariables ()
    {
        String sLine;
        MVariables oVars = MVariablesFactory.newInstance();

        BufferedReader stdin;

        try {

            stdin = new BufferedReader ( new InputStreamReader ( System.in ));


            if ( showPrompts ) {
                System.out.print("Equation> ");
                System.out.flush();
            }

            sEquation = stdin.readLine();

            if ( showPrompts ) {
                System.out.print ( "Var> " );
            }

            while ( ( sLine = stdin.readLine() ) != null ) {

                String sVar = null;
                String sValue = null;

                try {

                    sVar = sLine.substring ( 0, sLine.lastIndexOf ( '=' ));
                    sValue = sLine.substring ( sLine.lastIndexOf ( '=' ) + 1);
                    double dValueOf = Double.valueOf ( sValue ).doubleValue();

                    System.out.println ( "Var: "+sVar+"   doubleVal: "+dValueOf );

                    /* set the variables / double values into the hash */
                    oVars.set ( sVar, dValueOf );

                } catch ( StringIndexOutOfBoundsException e ) {

                } catch (NumberFormatException e) {
                    System.out.println("Number format exception! "+ sValue );
                } catch (NullPointerException e ) {
                    System.out.println("Null Pointer Exception! "+ sVar +", "+sValue );
                }



                System.out.print ( "Var> " );
            }


        } catch ( IOException e ) {
            System.err.println ( e.getMessage() );
        }


        return oVars;
    }

}












