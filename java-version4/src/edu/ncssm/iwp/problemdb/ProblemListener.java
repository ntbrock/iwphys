package edu.ncssm.iwp.problemdb;

// 2009-Jan-11 brockman, added file loading begin inidicator

public interface ProblemListener 
{
	public void indicateProblemLoadingBegin ( String filename );
	
	public void loadProblem ( DProblem problem );

	public void saveProblem ( ProblemWriter output );

}
