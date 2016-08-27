package edu.ncssm.iwp.ui;

/**
 * Sets sub-designers alert messages back up to their parents.
 * 
 * Used by the MCalculators. New in IWP3
 * 
 * @author brockman
 *
 */

public interface AlertListener
{
	public static final int ERROR = 2;
	public static final int WARN = 1;
	public static final int INFO = 0;
	
	public void displayAlert ( int level, String message );
		
	public void resetAlert();
	
}
