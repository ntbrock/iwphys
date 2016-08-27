/**
 * @(#)IWP_Wave_Demo.java
 *
 * IWP_Wave_Demo Applet application
 *
 * @author 
 * @version 1.00 2006/12/6
 */
 
import java.awt.*;
import java.applet.*;
import javax.swing.Timer;
import java.awt.event.*;

public class IWP_Wave_Demo extends Applet {
	
	Wave w;
	Wave v;
	
	Timer timer;
	double TiMe;
	
	int winters_demo;
	
	public void init() 
	{
		TiMe = 0.0;
		
		winters_demo = 0;
		
		w = new Wave(1.0, 10.0, 20.0, 0.0, true);
		v = new Wave(1.0, 10.0, 20.0, 0.0, true);
		
		if(winters_demo == 2)
		{
			w = new Wave(3.0, 20.0, 20.0, 0.0, true);
			v = new Wave(3.0, 20.0, 20.0, 0.0, true);
		}
		
		//Set up the timer that will perform the animation.
   		
   		ActionListener taskPerformer = new ActionListener() 
   		{
      		public void actionPerformed(ActionEvent evt) 
      		{
      			TiMe += 0.01;
          		repaint();
      		}
  		};
 	 
   		(new Timer(150, taskPerformer)).start();
			
	}

	public void paint(Graphics g) 
	{
		if(winters_demo == 0) // Sinusoidal Demo
		{
			w.DrawSinusoidalWave(g, TiMe, 40, 40, 200, 100, 500, 200);
			v.DrawSinusoidalWave(g, TiMe, 40, 40, 200, 350, 500, 200);
		
			w.DrawAddedSinusoidalWaves(v, g, TiMe, 40, 40, 200, 600, 500, 200);
		}
		else if(winters_demo == 1) // Doppler Effect Demo #1
		{
			w.DrawDopplerWave(g, 20, TiMe, 300, 300, 15.0, 15.0);
		}
		else // Doppler Effect Demo #2
		{
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 800, 800);
			
			g.setColor(Color.WHITE);
			
			w.DrawDopplerWave(g, 20, TiMe, 300, 300, 0.0, 0.0);
			v.DrawDopplerWave(g, 20, TiMe, 375, 300, 0.0, 0.0);
		}
		
	}
}