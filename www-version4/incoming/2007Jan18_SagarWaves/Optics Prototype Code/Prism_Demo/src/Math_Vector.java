import java.lang.Math;
import java.util.ArrayList;

class Math_Vector 
{
	double x1;
	double y1;
	double x2;
	double y2;
	double x;
	double y;
	
	double yIntercept;
	
	double theta; // In Radian's of course, and with respect to vector <1,0>
	double slope;
	double magnitude;
	
	public Math_Vector(double X, double Y)
	{
		x1 = y1 = 0.0;
		x2 = X;
		y2 = Y;
	}
	
	public Math_Vector(double X1, double Y1, double X2, double Y2)
	{
		x1 = X1;
		y1 = Y1;
		x2 = X2;
		y2 = Y2;
	}
	
	public void Update()
	{
		x = (x2-x1);
		y = (y2-y1);
		
		slope = y/x;
		magnitude = Math.hypot(y,x);
		
		theta = Math.atan(slope);
		
		if(y < 0.0)
			theta += Math.PI/2.0;
			
		yIntercept = y1 - x1*(slope);
	}
	
	public boolean almostEqual(double a, double b)
	{
		if (Math.abs(a-b) < 0.0001)
			return true;
		return false;
	}
	
	public double DotProduct(Math_Vector b)
	{
		return x*b.x + y*b.y;
	}
	
	public Math_Vector Intersection(Math_Vector b)
	{		
		if(!almostEqual(slope, b.slope))
			return (new Math_Vector((yIntercept - b.yIntercept)/(b.slope - slope), slope*(yIntercept - b.yIntercept)/(b.slope - slope) + yIntercept));
		return null;
	}
	
	public double angleOfIncidence(Math_Vector vecInterface)
	{
		double angle = Math.acos(DotProduct(vecInterface)/(magnitude*vecInterface.magnitude));
		
		if(Math.abs(Math.PI/2.0 - angle) < angle)
			angle = Math.PI/2.0 - angle;
			
		return Math.PI/4.0 - angle;
	}
}
