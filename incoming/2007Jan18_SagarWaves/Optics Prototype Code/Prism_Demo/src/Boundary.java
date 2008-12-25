import java.lang.Math;
import java.util.ArrayList;

class Boundary 
{
	double indexRefraction;
	Math_Vector vec;
	
	double b1;
	double b2;
	double b3;
	
	double c1;
	double c2;
	double c3;
	
	public Boundary(Math_Vector Vec, double IndexOfRefraction, double B1, double B2, double B3,
									double C1, double C2, double C3)
	{
		vec = Vec;
		
		indexRefraction = IndexOfRefraction;
		
		b1 = B1; b2 = B2; b3 = B3;
		c1 = C1; c2 = C2; C3 = c3;
	}
	
	public double Sellmeier(double wavelength)
	{
		double w2 = wavelength * wavelength;
		
		return Math.sqrt(1 + (b1*w2)/(w2-c1) + (b2*w2)/(w2-c2) + (b3*w2)/(w2-c3));
	}
	
	public double angleOfRefraction(Ray light, double iRefract)
	{
		double theta2 = Math.abs(Math.asin(light.iRefract/iRefract*light.vec.angleOfIncidence(vec)));
		
		if(light.vec.slope >= 0.0)
			return theta2;
		else
			return 2*Math.PI - theta2;
	}
}
