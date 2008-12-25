import java.lang.Math;
import java.util.ArrayList;

class Ray 
{
	boolean Mono;
	double wavelength;
	Math_Vector vec;
	double iRefract;
	int boundaryOrigin;
	
	public Ray(Math_Vector Vec, boolean Monochromatic, double Wavelength, int boundary)
	{
		vec = Vec;
		
		Mono = Monochromatic;
		
		if(!Mono)
			wavelength = Wavelength;
			
		boundaryOrigin = boundary;
	}
	
}
