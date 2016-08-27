import java.awt.*;
import java.applet.*;
import java.util.ArrayList;
import javax.swing.Timer;
import java.awt.event.*;

public class Parallel_Waves_Demo extends Applet {
	
	double TiMe = 0.0;
	int newWave = 0;
	double defaultSpeed = 50.0;
	double defaultPosition = 0.0;
	int defaultDirection = 1;
	int    defaultWavelength = 20;
	double minimumAmplitude = 0.1;
	
	double boundaryPos1 = 300.0;
	double boundaryPos2 = 600.0;
	double boundaryPos3 = 800.0;
	
	double medium1 = 1.00;
	double medium2 = 0.75;
	double medium3 = 0.75;
	
	double reflectionRatio1 = 0.75;
	double reflectionRatio2 = 0.75;
	
	ArrayList<Wave> waves1 = new ArrayList<Wave>();
	ArrayList<Wave> waves2 = new ArrayList<Wave>();
	ArrayList<Wave> waves3 = new ArrayList<Wave>();
	
	public void init() 
	{
		TiMe = 0.1;
		
		ActionListener taskPerformer = 
			new ActionListener() 
   			{
      			public void actionPerformed(ActionEvent evt) 
      			{
      				
      				// Boundary 1
      				for(int i = 0; i < waves1.size(); i++)
      				{
      					waves1.get(i).position += TiMe*waves1.get(i).speed*waves1.get(i).direction;
      					
      					if(waves1.get(i).direction == 1 && waves1.get(i).position >= boundaryPos1)
      					{
      						// add a new wave to waves2 and reflect a wave back to waves1 in opposite direction
      						waves2.add(new Wave(1,  waves1.get(i).speed * medium2, waves1.get(i).position, waves1.get(i).amplitude * reflectionRatio1));
      						waves1.set(i, new Wave(-1, waves1.get(i).speed, 2*boundaryPos1 - waves1.get(i).position, waves1.get(i).amplitude * (1-reflectionRatio1)));
      						//waves1.remove(i);
      					}
      					
      					if(waves1.get(i).direction == -1 && waves1.get(i).position <= 0.0)
      						waves1.remove(i);
      				}
      				
      				// Boundary 2
      				for(int i = 0; i < waves2.size(); i++)
      				{
      					waves2.get(i).position += TiMe*waves2.get(i).speed*waves2.get(i).direction;
      					
      					if(waves2.get(i).direction == 1 && waves2.get(i).position >= boundaryPos2)
      					{
      						// add a new wave to waves2 and reflect a wave back to waves1 in opposite direction
      						waves3.add(new Wave(1,  waves2.get(i).speed * medium3, waves2.get(i).position, waves2.get(i).amplitude * reflectionRatio2));
      						waves2.set(i, new Wave(-1, waves2.get(i).speed, 2*boundaryPos2 - waves2.get(i).position, waves2.get(i).amplitude * (1-reflectionRatio2)));
      						//waves2.remove(i);
      					}
      					
      					if(waves2.get(i).direction == -1 && waves2.get(i).position <= boundaryPos1)
      					{
      						// add a new wave to waves2 and reflect a wave back to waves1 in opposite direction
      						waves1.add(new Wave(-1,  waves2.get(i).speed * medium1, waves2.get(i).position, waves2.get(i).amplitude * reflectionRatio1));
      						waves2.add(new Wave(1, waves2.get(i).speed, 2*boundaryPos1 - waves2.get(i).position, waves2.get(i).amplitude * (1-reflectionRatio1)));
      						waves2.remove(i);
      					}
      				}
      				
      				// Boundary 3
      				for(int i = 0; i < waves3.size(); i++)
      				{
      					waves3.get(i).position += TiMe*waves3.get(i).speed;
      					
      					if(waves3.get(i).direction == 1 && waves3.get(i).position >= boundaryPos3)
      						waves3.remove(i);
      				}
      				
      				newWave++;
      				
      				if(0 == newWave % defaultWavelength)
      				{
      					waves1.add(new Wave(defaultDirection, defaultSpeed, defaultPosition, 1.0));	
      				}
      				
          			repaint();
      			}
  			};
  			
  		(new Timer(150, taskPerformer)).start();
	}

	public void paint(Graphics g) 
	{
		float n = 0.0f;
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 800, 800);
			
		g.setColor(Color.WHITE);
			
		g.setColor(new Color(1.0f, 0.0f, 0.0f));
		g.drawLine((int)boundaryPos1, 0, (int)boundaryPos1, 400);
		g.drawLine((int)boundaryPos2, 0, (int)boundaryPos2, 400);
		
		for(int i = 0; i < waves1.size(); i++)
		{
			n = (float)waves1.get(i).amplitude;
			
			if(n <= minimumAmplitude)
				waves1.remove(i);
			else
			{
				g.setColor(new Color(n, n, n));
				g.drawLine((int)waves1.get(i).position, 0, (int)waves1.get(i).position, 400);
			}
		}
		
		for(int i = 0; i < waves2.size(); i++)
		{
			n = (float)waves2.get(i).amplitude;
			
			if(n <= minimumAmplitude)
				waves2.remove(i);
			else
			{
				g.setColor(new Color(n, n, n));
				g.drawLine((int)waves2.get(i).position, 0, (int)waves2.get(i).position, 400);
			}
		}
		
		for(int i = 0; i < waves3.size(); i++)
		{
			n = (float)waves3.get(i).amplitude;
			
			if(n <= minimumAmplitude)
				waves3.remove(i);
			else
			{
				g.setColor(new Color(n, n, n));
				g.drawLine((int)waves3.get(i).position, 0, (int)waves3.get(i).position, 400);
			}
		}
			
		g.drawString(String.valueOf(waves1.size()), 400, 400);
		
	}
}