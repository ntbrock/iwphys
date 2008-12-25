package edu.ncssm.iwp.applets.collision;

public class ConstantMotionVector
{
	float initialDisplacement;
	float velocity;

	public ConstantMotionVector ( float initialDisplacement,
								  float initialVelocity )
	{
		this.initialDisplacement = initialDisplacement;
		this.velocity = initialVelocity;
	}

	public float getVelocity ( int step )
	{
		return velocity;
	}

	public float getDisplacement ( int step )
	{
		return initialDisplacement + velocity * (float)step;
	}

	public void adjustVelocity ( int step, float velocityAdjustment )
	{
		velocity += velocityAdjustment;
	}

}

