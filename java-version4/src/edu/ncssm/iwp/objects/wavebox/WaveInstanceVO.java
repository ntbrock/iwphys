package edu.ncssm.iwp.objects.wavebox;

import java.awt.Color;

import edu.ncssm.iwp.exceptions.InvalidEquationException;
import edu.ncssm.iwp.exceptions.UnknownTickException;
import edu.ncssm.iwp.exceptions.UnknownVariableException;
import edu.ncssm.iwp.graphicsengine.IWPDrawer;
import edu.ncssm.iwp.problemdb.DProblemState;

/*
 * Holds the Sinusoidal and Doppler Effect waves
 */

public class WaveInstanceVO 
{
	WaveGeneratorVO storage;
	DProblemState dp;
	
	WaveInstanceVO(WaveGeneratorVO s, DProblemState problemState)
	{
		storage = s;
		dp = problemState;
	}
	
	// y = A*cos(w*t - k*x + theta)
	// w = angular velocity = 2*PI*f
	// k = 2*PI/wavelength
	double getValue(double x, double t)  throws UnknownVariableException, UnknownTickException, InvalidEquationException
	{
		if(storage.leftToRight.calculate(dp.vars()) != 1.0)
			t *= -1.0;
		
		double w = 2.0*Math.PI*storage.frequency.calculate(dp.vars());
		double k = 2.0*Math.PI/storage.wavelength.calculate(dp.vars());
		double A = storage.maxAmplitude.calculate(dp.vars());
		
		return A*Math.cos(w*t - k*x + storage.phaseShift.calculate(dp.vars()));
	} 
	
	double getMaxWaveGivenTime(int waveNumber, double t)  throws UnknownVariableException, UnknownTickException, InvalidEquationException
	{	
		while(t >= 1.0/storage.frequency.calculate(dp.vars()))
			t -= 1.0/storage.frequency.calculate(dp.vars());
		
		return t/(1.0/storage.frequency.calculate(dp.vars()))*storage.wavelength.calculate(dp.vars());
	}
	
	public void DrawDopplerWave(IWPDrawer drawer, int numWaves, double vx, double vy, double time)  throws UnknownVariableException, UnknownTickException, InvalidEquationException
	{
		
		double period = 1.0/(storage.frequency.calculate(dp.vars()));
		double t = time;
		
		double maxRadius = numWaves*storage.wavelength.calculate(dp.vars());
		
		while(t >= 0.0)
		{
			// Draw a circle with:
			//	- center at (time - t)*<vx, vy> + <x0, y0>
			//  - radius of (time - t)*wavelength*frequency
			
			double centerX = (time - t)*vx + storage.x.calculate(dp.vars());
			double centerY = (time - t)*vy + storage.y.calculate(dp.vars());
			
			double radius  = t*storage.wavelength.calculate(dp.vars())*storage.frequency.calculate(dp.vars());
			
			if(radius <= maxRadius)
				drawer.drawOval(centerX - radius, centerY + radius, radius*2.0, radius*2.0);
			
			t -= period;
		}
	}
	
	public void DrawSinusoidalWave(IWPDrawer drawer, int iterations, double range, double x, double y, double w, double h, double time)  throws UnknownVariableException, UnknownTickException, InvalidEquationException
	{
		// Draw Sinusoidal Wave in a box with:
		// (x, y) = top left corner
		
		drawer.setColor(Color.black);
		drawer.drawRect(x, y, w, h);
		
		drawer.setColor(Color.gray);
		drawer.drawLine(x, y - h/2.0, x + w, y - h/2.0);
		
		drawer.setColor(Color.black);
		for(int i = 0; i < iterations; i++)
		{
			double x1 = x + i*(w/((double)iterations));
			double x2 = x + (i+1)*(w/((double)iterations));
			
			double y1 = y - h/2.0 + getValue(x1, time);
			double y2 = y - h/2.0 + getValue(x2, time);
			
			drawer.drawLine(x1, y1, x2, y2);
		}
	}
	
	public void DrawAddedSinusoidalWaves(WaveInstanceVO n, IWPDrawer drawer, int iterations, double range, double x, double y, double w, double h)  throws UnknownVariableException, UnknownTickException, InvalidEquationException
	{
		int zero_y = (int)(h/2.0 + y);
		double curVal = getValue(0.0, dp.problem.getTime().getTime()) + n.getValue(0.0,dp.problem.getTime().getTime());
		
		for(int i = 0; i < iterations; i++)
		{
			double nextVal = getValue((i+1)*(double)range/iterations, dp.problem.getTime().getTime()) + n.getValue((i+1)*(double)range/iterations, dp.problem.getTime().getTime());
			
			drawer.drawLine((int)(x + (double)i*w/iterations),    zero_y - (int)curVal - h, 
					   (int)(x + (double)(i+1)*w/iterations), zero_y - (int)nextVal - h);
					   
			curVal = nextVal;
		}
		
		drawer.drawRect(x, y, w, h);
	}
}
