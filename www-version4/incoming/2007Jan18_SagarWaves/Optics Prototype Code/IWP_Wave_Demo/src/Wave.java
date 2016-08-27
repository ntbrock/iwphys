import java.awt.*;
import java.applet.*;
import javax.swing.Timer;
import java.awt.event.*;

public class Wave 
{
	double frequency;
	double wavelength;
	double period;
	double maxAmplitude;
	double phaseShift;
	boolean forwards;
	
	public Wave(double Frequency, double Wavelength, double MaximumAmplitude, double PhaseShift, boolean Forward)
	{
		wavelength = Wavelength;
		maxAmplitude = MaximumAmplitude;
		frequency = Frequency;
		period = 1.0/frequency;
		phaseShift = PhaseShift;
		
		forwards = Forward;
	}
	
	double getValue(double x, double t)
	{
		if(forwards)
			return maxAmplitude*Math.cos(2*Math.PI*frequency*t - 2*Math.PI*x/wavelength + phaseShift);
		else
			return maxAmplitude*Math.cos(2*Math.PI*frequency*t + 2*Math.PI*x/wavelength + phaseShift);
	}
	
	double getMaxWaveGivenTime(int waveNumber, double t)
	{		
		double x = t/period*wavelength;
		
		if(x > 2.0*Math.PI)
			return (x - (int)(x/wavelength)*wavelength + waveNumber*wavelength);
		
		return x;
	}
	
	public void DrawDopplerWave(Graphics g, int numWaves, double t, int x, int y, double vx, double vy)
	{
		double radius = getMaxWaveGivenTime(0, t);
		
		for(int i = 0; i < numWaves; i++)
		{
			g.drawOval((int)(x-radius) - (int)(period*vx*i), (int)(y-radius) - (int)(period*vy*i), (int)(2.0*radius), (int)(2.0*radius));
			radius += wavelength;
		}
	}
	
	public void DrawSinusoidalWave(Graphics g, double t, int iterations, double range, int x, int y, int width, int height)
	{
		g.drawRect(x, y, width, height);
		
		int zero_y = (int)((double)(height)/2.0) + y;
		double curVal = getValue(0.0, t);
		
		for(int i = 0; i < iterations; i++)
		{
			double nextVal = getValue((i+1)*(double)range/iterations, t);
			g.drawLine((int)(x + (double)i*(double)width/iterations),    zero_y - (int)curVal, 
					   (int)(x +(double)(i+1)*(double)width/iterations), zero_y - (int)nextVal);
					   
			curVal = nextVal;
		}
	}
	
	public void DrawAddedSinusoidalWaves(Wave n,Graphics g, double t, int iterations, double range, int x, int y, int width, int height)
	{
		int zero_y = (int)((double)(height)/2.0) + y;
		double curVal = getValue(0.0, t) + n.getValue(0.0,t);
		
		for(int i = 0; i < iterations; i++)
		{
			double nextVal = getValue((i+1)*(double)range/iterations, t) + n.getValue((i+1)*(double)range/iterations, t);
			
			g.drawLine((int)(x + (double)i*(double)width/iterations),    zero_y - (int)curVal, 
					   (int)(x +(double)(i+1)*(double)width/iterations), zero_y - (int)nextVal);
					   
			curVal = nextVal;
		}
		
		g.drawRect(x, y, width, height);
	}
}
