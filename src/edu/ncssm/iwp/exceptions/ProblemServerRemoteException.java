package edu.ncssm.iwp.exceptions;


public class ProblemServerRemoteException extends Exception
{
    private String friendlyMessage = "no friendly message specified";

	public ProblemServerRemoteException ( ) { }
	public ProblemServerRemoteException ( String message )
	{
		super ( message );
	}

	public ProblemServerRemoteException ( Throwable t )
	{
		super ( t.getMessage() );
	}
	public ProblemServerRemoteException ( Throwable t ,String happyMessage)
	{
		super ( t.getMessage() );
		friendlyMessage=happyMessage;
	}
    public String getFriendlyMessage() {return friendlyMessage;}

}
