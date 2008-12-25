package edu.ncssm.iwp.problemdb;

import edu.ncssm.iwp.exceptions.*;

import edu.ncssm.iwp.util.*;


public class TEST_DProblemManager_HTTP
{

    public static void main ( String args[] )
    {

        if ( args.length < 1 )
        {
            IWPLog.out("Usage: TEST_DProblemManager_HTTP [problem_url]");
            System.exit(-1);
        }

        try
        {
            String problemURL = args[0];

            DProblemManager_HTTP probManager = new DProblemManager_HTTP ( );

            DProblem problem = probManager.loadProblem ( problemURL );

            System.out.println ( problem );

        }
        catch ( DataStoreException e )
        {
            IWPLog.x("Problem with the datastore: " + e, e );
            e.printStackTrace ();
        }

    }

}
