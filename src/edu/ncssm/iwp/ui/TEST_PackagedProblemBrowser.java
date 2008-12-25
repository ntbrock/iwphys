package edu.ncssm.iwp.ui;

import edu.ncssm.iwp.problemdb.*;

public class TEST_PackagedProblemBrowser
{


	public static void main ( String args[] )
	{
		
		DummyProblemListener problemListener = new DummyProblemListener();
		
		GWindow_PackagedProblemBrowser test = new GWindow_PackagedProblemBrowser ( problemListener );
	
		test.openDialog();
		
		if ( test == null ) { }
	}
	
	public TEST_PackagedProblemBrowser() {
		super();
	}

}


class DummyProblemListener implements ProblemListener
{

	public void loadProblem(DProblem problem) {

		
	}

	public void saveProblem(ProblemWriter output) {
		
	}
	
	
}
