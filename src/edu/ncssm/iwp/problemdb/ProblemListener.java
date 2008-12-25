package edu.ncssm.iwp.problemdb;

public interface ProblemListener 
{
	public void loadProblem ( DProblem problem );

	public void saveProblem ( ProblemWriter output );

}
