package edu.ncssm.iwp.bin;

import edu.ncssm.iwp.util.buildversion.BuildVersion;

/**
 * Simple command line utility to print the version of a jar.
 */

public class Version
{

    public static void main(String args[])
    {
        System.out.println( BuildVersion.VERSION );
    }
}
