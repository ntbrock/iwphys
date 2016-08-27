package edu.ncssm.iwp.test;

import java.util.Date;
import edu.ncssm.iwp.math.*;
import edu.ncssm.iwp.exceptions.*;

/**
 * Races two MVariables implemetnations
 * @author brockman 2007-Jan-18
 */

public class TEST_MVariables_Race
{
    public static void main(String args[])
    {
        MDataHistory[] racers = new MDataHistory[1];

        if ( args.length < 5 ) {
            System.err.println("Usage: TEST_MVariables_Race <maxTicks> <maxVariables> <getAtRecallPasses> <getPrevRecallPasses> <class>");
            System.exit(1);
        }

        int maxTicks = Integer.parseInt(args[0]);
        int maxVariables = Integer.parseInt(args[1]);
        int getAtRecallPasses = Integer.parseInt(args[2]);
        int getPrevRecallPasses = Integer.parseInt(args[3]);
        String className = args[4];

        // TODO: when I have another class to test

        if ( className.equalsIgnoreCase("MVariablesImpl") ) {
            racers[0] = new MDataHistoryArrayListImpl();
        } else if ( className.equalsIgnoreCase("MVariablesArrays") ) {
           
        } else {
            System.err.println("Error: Unknown Class: " + className + ". Try MVariablesImpl or.... ");
            System.exit(1);
        }


        for ( int i = 0; i < racers.length; i++ ) {

            System.out.println("Running racer[" + i + "], Class = " + racers[i].getClass().getName() );

            long startTime = nowTime();

            System.out.println("Step 1: Populating: " + (nowTime()-startTime) + " msec" );

            // populate
            for ( int tick = 0; tick < maxTicks; tick++ ) {

                // need a better way to do this, should be via the interface?
                if ( racers[i] instanceof MDataHistoryArrayListImpl ) {
                    ((MDataHistoryArrayListImpl)racers[i]).setCurrentTick(tick);
              
                }

                for ( int varNum = 0; varNum < maxVariables; varNum++ ) {
                    racers[i].setAtCurrentTick( "var" + varNum, ((double)tick)*(double)tick);
                }

                // System.out.println("Tick: " + racers[i].getCurrentTick() );
            }


            try {

                System.out.println("Step 2: Recall PrevTick: " + (nowTime()-startTime) + " msec" );

                // also test getAtPrevTick?
                // also test getAtPrevTick?
                for ( int recall = 0; recall < getAtRecallPasses; recall++ ) {
                    for ( int tick = 0; tick < maxTicks; tick++ ) {
                        for ( int varNum = 0; varNum < maxVariables; varNum++ ) {

                            if ( racers[i].getAtTick( "var" + varNum, tick ) !=
                                 (double)tick*(double)tick ) {
                                System.err.println("ERROR: Data mismatch in getAt, tick: " + tick );
                            }
                        }
                    }
                }

                System.out.println("Step 3: Recall AtTick: " + (nowTime()-startTime) + " msec" );

                // Prev Tick Test
                for ( int recall = 0; recall < getPrevRecallPasses; recall++ ) {
                    for ( int tick = 1; tick < maxTicks; tick++ ) {

                        if ( racers[i] instanceof MDataHistoryArrayListImpl ) {
                            ((MDataHistoryArrayListImpl)racers[i]).setCurrentTick(tick);
                      
                        }

                        for ( int varNum = 0; varNum < maxVariables; varNum++ ) {
                            racers[i].getAtCurrentTick( "var" + varNum );
                        }
                    }
                }

            } catch ( UnknownVariableException x ) {
                System.err.println("Unknown Variable Exception: " + x.getMessage() );
                x.printStackTrace();

            } catch ( UnknownTickException x ) {
                System.err.println("Unknown Tick Exception: " + x.getMessage() );
                x.printStackTrace();

            }

            System.out.println("Step 4: Done: " + (nowTime()-startTime) + " msec" );
        }

    }


    public static long nowTime()
    {
        return new Date().getTime();
    }

}

