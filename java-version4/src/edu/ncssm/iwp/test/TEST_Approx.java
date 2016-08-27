package edu.ncssm.iwp.test;

// For use with console-based streams
import java.io.*;

// IWP classes
import edu.ncssm.iwp.math.*;
import edu.ncssm.iwp.objects.*;
import edu.ncssm.iwp.problemdb.*;
import edu.ncssm.iwp.exceptions.*;

import edu.ncssm.iwp.util.*;

// Class that offers user menu and controls when calculations are made
public class TEST_Approx
{
	// Constants for display mode
	private static final int DISPLAY_ALL = 0;
	private static final int DISPLAY_LAST = 1;
	private static final int DISPLAY_INTERVAL = 2;

	// Physical variables
	private double t0;	// Initial time
	private double t;	// Final time
	private double dt;	// Timestep
	
	// More physical variables
	private double x0;	// Initial position
	private double v0;	// Initial velocity
	
	// To make the calculations
	private Calculator oCalc;
	
	// Calculator settings
	private String sCodeBase;
	private String sApproxMethod;
	
	// For stream input
	BufferedReader oIn;
	
	// Program starts here
	public static void main(String[] args)
	{
		// Create a class variable
		TEST_Approx oApprox = new TEST_Approx();

		// Begin and close on any fatal input errors
		try
		{
			oApprox.Start();
		}
		catch ( UnknownVariableException e ) { 
			IWPLog.x("ERROR: UnknownVariable: " + e, e );
		}
		catch ( InvalidEquationException e ) { 
			IWPLog.x("ERROR: InvalidEquationException: " + e, e );
		}

		catch(IOException e)
		{
			IWPLog.x("Fatal error reading keyboard input.\n", e);
		}
	}
	
	// Constructor
	TEST_Approx()
	{
		// Set up physical variables
		t0 = 0;
		t = 10;
		dt = .01;
		
		x0 = 0;
		v0 = 0;
		
		// Set up calculator options
		sCodeBase = "Test";
		sApproxMethod = "Euler";
		
		oCalc = new Calculator(sCodeBase, sApproxMethod);
		
		// Set up buffer reader for input
		oIn = new BufferedReader(new InputStreamReader(System.in));
	}
	
	// Here's where the program really starts
	public void Start()
		throws IOException, UnknownVariableException, InvalidEquationException
	{
		int nChoice; // User input
		
		// Menu loop
		do {
			// Get input
			nChoice = GetChoice();
			
			// Execute the appropriate methods
			try
			{
				if (nChoice >= 1 && nChoice <= 5)
				{
					if (nChoice == 1)
					{
						t0 = SetVar("initial time");
					}
					else if (nChoice == 2)
					{
						t = SetVar("final time");
					}
					else if (nChoice == 3)
					{
						dt = SetVar("time step");
					}
					else if (nChoice == 4)
					{
						x0 = SetVar("initial position");
					}
					else if (nChoice == 5)
					{
						v0 = SetVar("initial velocity");
					}
					System.out.println("\nValue changed.\n");
				}
			}
			catch (NumberFormatException e)
			{
				System.out.println("\nError parsing number, value not changed.\n");
			}
			
			if (nChoice == 6)
			{
				SetCodeBase();
			}
			else if (nChoice == 7)
			{
				SetApproxMethod();
			}
			else if (nChoice == 8)
			{
				Calculate(DISPLAY_ALL);
			}
			else if (nChoice == 9)
			{
				Calculate(DISPLAY_LAST);
			}
			else if (nChoice == 10)
			{
				Calculate(DISPLAY_INTERVAL);
			}
		} while (nChoice != 0);
	}
	
	// Display user menu
	private void DisplayMenu()
	{
		System.out.print("\n");
		System.out.print("\n");
		System.out.print("Differential Approximations\n");
		System.out.print("------------ --------------\n");
		System.out.print("\n");
		System.out.print("Acceleration equation: " + oCalc.GetAccEqn() + "\n");
		System.out.print("\n");
		System.out.print("Choose an option:\n");
		System.out.print("\n");
		System.out.print(" 1) Set initial time      ( t[0] = " + t0 + " )\n");
		System.out.print(" 2) Set final time        ( t    = " + t  + " )\n");
		System.out.print(" 3) Set time step         ( dt   = " + dt + " )\n");
		System.out.print(" 4) Set initial position  ( x[0] = " + x0 + " )\n");
		System.out.print(" 5) Set initial velocity  ( v[0] = " + v0 + " )\n");
		System.out.print("\n");
		System.out.print(" 6) Set code base (Currently " + sCodeBase + ")\n");
		System.out.print(" 7) Set approximation method (Currently " + sApproxMethod + ")\n");
		System.out.print("\n");
		System.out.print(" 8) Calculate and display values for each timestep\n");
		System.out.print(" 9) Calculate and display value at final time\n");
		System.out.print("10) Calculate and display values for a given interval\n");
		System.out.print("\n");
		System.out.print("0) Exit\n");
		System.out.print("\n");
		System.out.print("Your choice: ");
	}
	
	// Get user input, with input validation
	private int GetChoice() throws IOException
	{
		int nChoice;
		boolean fDone;
	
		fDone = false;
	
		do {
			DisplayMenu();
			
			try
			{
				nChoice = Integer.parseInt(oIn.readLine());
			}
			catch (NumberFormatException e)
			{
				nChoice = -1;
			}
			
			System.out.print("\n");
			if (nChoice < 0 || nChoice > 10)
			{
				System.out.print("That is an invalid choice.\n");
			}
			else 
			{
				fDone = true;
			}
		} while (fDone == false);
		
		return nChoice;
	}
	
	// Change a physical value on user request
	private double SetVar(String sVarName) throws NumberFormatException, IOException
	{
		System.out.print("New " + sVarName + ": ");
		return Double.parseDouble(oIn.readLine());
	}
	
	// Set the code base used with the calculator
	private void SetCodeBase() throws IOException
	{
		int nChoice; // User input
	
		System.out.print("Select code base:\n");
		System.out.print("\n");
		System.out.print("1) Test\n");
		System.out.print("2) IWP\n");
		System.out.print("3) Leave it how it is\n");
		System.out.print("\n");
		System.out.print("Choice: ");

		try
		{
			nChoice = Integer.parseInt(oIn.readLine());
		}
		catch (NumberFormatException e)
		{
			nChoice = -1;
		}
		
		System.out.print("\n");
		if (nChoice == 1)
		{
			sCodeBase = "Test";
			System.out.print("Code base set to Test.\n");
		}
		else if (nChoice == 2)
		{
			sCodeBase = "IWP";
			System.out.print("Code base set to IWP.\n");
		}
		else
		{
			System.out.print("Code base unchanged.\n");
		}
	}

	// Set the approximation method used with the calculator
	private void SetApproxMethod() throws IOException
	{
		int nChoice; // User input
	
		System.out.print("Select approximation method:\n");
		System.out.print("\n");
		System.out.print("1) Euler\n");
		System.out.print("2) RK2\n");
		System.out.print("3) RK4\n");
		System.out.print("4) Leave it how it is\n");
		System.out.print("\n");
		System.out.print("Choice: ");

		try
		{
			nChoice = Integer.parseInt(oIn.readLine());
		}
		catch (NumberFormatException e)
		{
			nChoice = -1;
		}
		
		System.out.print("\n");
		if (nChoice == 1)
		{
			sApproxMethod = "Euler";
			System.out.print("Approximation method set to Euler.\n");
		}
		else if (nChoice == 2)
		{
			sApproxMethod = "RK2";
			System.out.print("Approximation method set to RK2.\n");
		}
		else if (nChoice == 3)
		{
			sApproxMethod = "RK4";
			System.out.print("Approximation method set to RK4.\n");
		}
		else
		{
			System.out.print("Approximation method unchanged.\n");
		}
	}
	
	// Set up calculator and display values it returns
	private void Calculate(int nOutputAmount)
		throws IOException,	UnknownVariableException, InvalidEquationException
	{
		int nLines = 0;		// Number of lines before pausing
		int nCurLine = 0;	// Number of lines since last pause
		
		// A user-selected interval to display
		double nStart = 0;
		double nEnd = 0;

		// Holds whether we are within the given interval
		boolean fWithinInt = false;
		
		String sInline = ""; // User input
	
		// If needed, get a display interval from the user
		if (nOutputAmount == DISPLAY_INTERVAL)
		{
			System.out.print("Start time: ");
			try
			{
				nStart = Double.parseDouble(oIn.readLine());
			}
			catch (NumberFormatException e)
			{
				nStart = t0;
			}

			System.out.print("End time: ");
			try
			{
				nEnd = Double.parseDouble(oIn.readLine());
			}
			catch (NumberFormatException e)
			{
				nEnd = t;
			}

			System.out.print("\n");
			
			// Validate the interval
			if (nStart < t0) nStart = t0;
			if (nEnd > t) nEnd = t;
			
			if (nEnd < nStart)
			{
				nStart = t + dt;
			}
		}
	
		// Get number of lines to display until pause
		if (nOutputAmount == DISPLAY_ALL || nOutputAmount == DISPLAY_INTERVAL)
		{
			System.out.print("Number of lines before each pause: ");
			try
			{
				nLines = Integer.parseInt(oIn.readLine());
			}
			catch (NumberFormatException e)
			{
				nLines = 20;
			}
			System.out.print("\n");
			
			nCurLine = 0;
		}
	
		// Set up calculator with options
		oCalc.sCodeBase = sCodeBase;
		oCalc.sApproxMethod = sApproxMethod;
		
		// Set up calculator with physical variables
		oCalc.t = t0;
		oCalc.x = x0;
		oCalc.v = v0;
		
		// Start calculator over
		oCalc.ResetSteps();
		while (oCalc.t <=t && !sInline.equals("q") && !sInline.equals("Q"))
		{
			// Check if it's in the display interval, if necessary
			if (nOutputAmount == DISPLAY_INTERVAL)
			{
				if (oCalc.t >= nStart && oCalc.t <= nEnd)
				{
					fWithinInt = true;
				}
				else
				{
					fWithinInt = false;
				}
			}
		
			if (
				(nOutputAmount == DISPLAY_ALL) ||
				(nOutputAmount == DISPLAY_INTERVAL && fWithinInt == true) ||
				(nOutputAmount == DISPLAY_LAST && oCalc.t + dt > t)
			) {
				// Display current values held in calculator
				System.out.print(
					"t=" + Float.parseFloat(oCalc.t + "") +
				 	" x=" + Float.parseFloat(oCalc.x + "") +
				 	" v=" + Float.parseFloat(oCalc.v + "") +
				 	" a=" + Float.parseFloat(oCalc.GetAcc() + "") + "\n"
				);
				nCurLine++;
			}
			
			// Move time forward a step
			oCalc.StepTime(dt);
			
			// Display pause message if it's time
			if ((nOutputAmount == DISPLAY_ALL || nOutputAmount == DISPLAY_INTERVAL) && oCalc.t <= t)
			{
				if (nCurLine == nLines)
				{
					nCurLine = 0;
					System.out.print("\n");
					System.out.print("Enter 'q' to break, anything else to continue: ");
					sInline = oIn.readLine();
					System.out.print("\n");
				}
			}
		}

		// Pause when calculations are finished
		if (
			!(
				(nOutputAmount == DISPLAY_ALL || nOutputAmount == DISPLAY_INTERVAL) &&
				(sInline.equals("q") || sInline.equals("Q"))
			)
		) {
			System.out.print("\n");
			System.out.print("Calculations complete. Press enter for main menu.");
			oIn.readLine();
			System.out.print("\n");
		}
	}
}

// Does TEST calculations or calls IWP methods to get values
class Calculator
{
	public double t;	// Current time
	public double x;	// Current position
	public double v;	// Current velocity
	private double a;	// Current acceleration
	
	private int nSteps;	// Number of timesteps executed
	
	public String sCodeBase;	// IWP or TEST
	public String sApproxMethod;	// Euler, RK2, or RK4
	
	// Set up super class calculator and time/brain objects
	MCalculator oCalc;
	DProblemState zeroThought, oCurrentThought;
	DObject_Time oCurrentTime;
	String[] oParams = new String[3];

	// Constructor, set up variables and clear timesteps executed
	Calculator(String isCodeBase, String isApproxMethod)
	{
		sCodeBase = isCodeBase;
		sApproxMethod = isApproxMethod;
		
		t = 0;
		x = 0;
		v = 0;
		
		ResetSteps();
	}
	
	// ACCELERATION EQUATION BEGINS HERE
	
	// Returns a string representation of the acceleration equation
	public String GetAccEqn()
	{
		return "a = -sin(x)";
		//return "a = -x";
	}
	
	// Based on given physical values, calculates and returns acceleration
	public double CalcAcc(double inT, double inX, double inV)
	{
		// return -Math.sin(inX);
		return -inX;
	}

	// ACCELERATION EQUATION ENDS HERE
	
	// Calculates and returns acceleration using current variables
	public double GetAcc()
	{
		a = CalcAcc(t, x, v);
		return a;
	}
	
	// Same as CalcAcc()
	public double GetAcc(double inT, double inX, double inV)
	{
		return CalcAcc(inT, inX, inV);
	}
	
	// Start over timesteps
	public void ResetSteps()
	{
		nSteps = 0;
	}
	
	// Advance time one step by performing calculations
	public void StepTime(double dt)
		throws UnknownVariableException, InvalidEquationException
	{
		// Calculate based on calculator options
		if (sCodeBase == "Test")
		{
			if (sApproxMethod == "Euler")
			{
				// TEST Euler

				GetAcc();
				t += dt;
				if (nSteps == 0) v += .5 * a * dt; else v += a * dt;
				x += v * dt;
			}
			else if (sApproxMethod == "RK2")
			{
				// TEST RK2

		   		double kx0, kx1;
		   		double kv0, kv1;
		   		
	   			kx0 = dt * (v      ); kv0 = dt * GetAcc(t     , x      , v      );
	   			kx1 = dt * (v + kv0); kv1 = dt * GetAcc(t + dt, x + kx0, v + kv0);
	   			
	   			x += (kx0 + kx1) / 2;
	   			v += (kv0 + kv1) / 2;
	   			t += dt;
			}
			else if (sApproxMethod == "RK4")
			{
				// TEST RK4

		   		double kx0, kx1, kx2, kx3;
		   		double kv0, kv1, kv2, kv3;
   			
   				kx0 = dt * (v          ); kv0 = dt * GetAcc(t         , x          , v          );
   				kx1 = dt * (v + kv0 / 2); kv1 = dt * GetAcc(t + dt / 2, x + kx0 / 2, v + kv0 / 2);
   				kx2 = dt * (v + kv1 / 2); kv2 = dt * GetAcc(t + dt / 2, x + kx1 / 2, v + kv1 / 2);
   				kx3 = dt * (v + kv2    ); kv3 = dt * GetAcc(t + dt    , x + kx2    , v + kv2    );
   				
   				x+= ((kx0) + (2 * kx1) + (2 * kx2) + (kx3)) / 6;
   				v+= ((kv0) + (2 * kv1) + (2 * kv2) + (kv3)) / 6;
   				t += dt;
   			}
   		}
		else if (sCodeBase == "IWP")
		{
			// Set up for first step
			if (nSteps == 0)
			{
				// Make a new IWP calculator based on the approximation method
				if (sApproxMethod == "Euler")
				{
					oCalc = new MCalculator_Euler( new MEquation(x+""),
								new MEquation(v+""),
								new MEquation("-d" ) );
				}
				else if (sApproxMethod == "RK2")
				{
					oCalc = new MCalculator_RK2( new MEquation(x+""),
												 new MEquation(v+""),
												 new MEquation("-d" ) );
				}
				else // RK4
				{
					oCalc = new MCalculator_RK4( new MEquation(x+""),
							 new MEquation(v+""),
							 new MEquation("-d" ) );
				}
			
				// Set up time variable
				oCurrentTime = new DObject_Time(t);
				oCurrentTime.setChange(dt);
				// BROCKMAN 2006-Apr-29 There used to be a 'forward' here.
			}

			// Start the calculations with a dummy problem.
			oCurrentThought = new DProblemState( new DProblem() ); // tick = 0 on construction.
			
			try { 
				// this really is a weird hack. 
				oCalc.calculate ( oCurrentThought.vars() );
			} catch ( UnknownTickException x ) { 
				IWPLog.x(this, "Calculate UnknownTick", x );
			}
			
			// Advance physical variables
			t = oCurrentTime.getTime();
			x = oCalc.getDisp();
			v = oCalc.getVel();
			a = oCalc.getAccel();

			if (Double.isNaN(a)) { a = 0.0; }
		}
		
		nSteps++;
	}
}

