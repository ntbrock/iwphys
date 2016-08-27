import java.util.Vector;
import java.awt.*;
import java.applet.*;

public class WaveObject 
{
	Vector waves;
	
	double maxRadius;
	double x;
	double y;
	
	double period;
	double wavelength;
	double birthTime;
	
	double ratio;
	
	public WaveObject(double initial_X, double initial_Y, double MaximumRadius, double Period, double Wavelength, double creationTime)
	{
		x = initial_X;
		y = initial_Y;
		maxRadius = MaximumRadius;
		period = Period;
		wavelength = Wavelength;
		
		waves = new Vector();
		waves.add(new SingleWave(x, y, 0.0));
	}
	
	public void Update(double time)
	{
		 ratio = (time - birthTime)/period;
		 
		 if(ratio > 1.0)
		 	ratio = ratio - (int)ratio;
		 	
		 if(ratio < 0.0 || ratio > 1.0)
		 	return;
		 	
		 for(int i = 0; i < waves.size(); i++)
		 	((SingleWave)waves.get(i)).radius += ratio*wavelength;
		 	
		 if(maxRadius < ((SingleWave)waves.get(0)).radius)
		 {
		 	waves.remove(0);
		 }
		 
		 if(ratio < 0.0005)
		 	waves.add(new SingleWave(x, y, 0.0));
		 
	}
	
	public void DrawMyself(Graphics g)
	{
		for(int i = 0; i < waves.size(); i++)
		{
			SingleWave p = (SingleWave)(waves.get(i));
			g.drawOval((int)(p.originX-p.radius), (int)(p.originY-p.radius), (int)(2*p.radius), (int)(2*p.radius));
		}	
	}
}
