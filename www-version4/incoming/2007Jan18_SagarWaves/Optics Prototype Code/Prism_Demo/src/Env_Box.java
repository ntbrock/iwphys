import java.lang.Math;
import java.util.ArrayList;
import java.awt.*;

class Env_Box 
{
	ArrayList<Boundary> boundaries;
	ArrayList<Ray> light;
	
	int width;
	int height;
	
	public Env_Box(int Width, int Height)
	{
		width = Width;
		height = Height;
		
		boundaries = new ArrayList<Boundary>();
		light = new ArrayList<Ray>();
		
		boundaries.add(new Boundary(new Math_Vector(10.0, 10.0), 1.0, 0.0, 0.0, 0.0,0.0, 0.0, 0.0));
	}
	
	public void addBoundary(Boundary b)
	{
		boundaries.add(b);
	}
	
	public void process(Ray r)
	{
		int z = 0;
		
		light.add(r);
		
		while(z != light.size())
		{
			Ray curRay = light.get(z);
			
			if(curRay.boundaryOrigin < boundaries.size())
			{
				// Compute the intersection for next boundary
				Boundary b = boundaries.get(light.get(z).boundaryOrigin + 1);
				
				Math_Vector temp = curRay.vec.Intersection(b.vec);
				
				if(curRay.Mono)
				{
					// Get's complicated here, we need to do a lot of different wavelengths
					for(double i = 0.0000004; i <= 0.0000007; i += 0.00000005)
					{
						double theta = (b.vec.theta - Math.PI/4.0) - b.angleOfRefraction(curRay, b.Sellmeier(i));
						
						light.add(new Ray(new Math_Vector(temp.x, temp.y, temp.x + 10*Math.cos(theta), temp.y + 10*Math.sin(theta)), false, i, light.get(z).boundaryOrigin + 1));
					}
				}
				else
				{
					double theta = (b.vec.theta - Math.PI/4.0) - b.angleOfRefraction(curRay, b.Sellmeier(curRay.wavelength));
						
					light.add(new Ray(new Math_Vector(temp.x, temp.y, temp.x + 10*Math.cos(theta), temp.y + 10*Math.sin(theta)), false, curRay.wavelength, light.get(z).boundaryOrigin + 1));
				}
			}
			
		}
	}
	
	public void Draw(Graphics g)
	{
		// Draw all the boundaries
		for(int i = 0; i < boundaries.size(); i++)
			g.drawLine((int)(boundaries.get(i).vec.x1), (int)boundaries.get(i).vec.y1, (int)boundaries.get(i).vec.x2, (int)boundaries.get(i).vec.y2);
	}
	
}