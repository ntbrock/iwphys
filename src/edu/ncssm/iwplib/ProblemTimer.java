package edu.ncssm.iwplib;


public class ProblemTimer
{
	TimeParameters timeParams = null;
	int timeStep;

	public ProblemTimer ( TimeParameters timeParams )
	{
		this.timeParams = timeParams;
		this.timeStep = 0;
	}

	public int getTimeStep ( )
	{
		return timeStep;
	}


	public void stepTime ( )
	{
		timeStep++;
	}

}
