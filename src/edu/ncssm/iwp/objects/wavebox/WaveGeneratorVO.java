package edu.ncssm.iwp.objects.wavebox;

import edu.ncssm.iwp.graphicsengine.GColor;
import edu.ncssm.iwp.math.MCalculator;

/**
 * Holds the data parameters for a wave generator.
 * @author brockman
 *
 */

public class WaveGeneratorVO
{
	/* Variables list
	 * x
	 * y
	 * width
	 * height
	 * x_velocity
	 * y_velocity
	 * Doppler
	 * Iterations
	 * numWaves
	 * Frequency
	 * Wavelength
	 * Period
	 * maxAmplitude
	 * phaseShift
	 * leftToRight
	 */
	
	GColor color;
	MCalculator x;
	MCalculator y;
	MCalculator width;
	MCalculator height;
	MCalculator x_velocity;
	MCalculator y_velocity;
	MCalculator Doppler;
	MCalculator waveIterations;
	MCalculator numWaves;
	MCalculator frequency;
	MCalculator wavelength;
	MCalculator period;
	MCalculator maxAmplitude;
	MCalculator phaseShift;
	MCalculator leftToRight;
	
	
	
	public GColor getColor() {
		return color;
	}
	public void setColor(GColor color) {
		this.color = color;
	}
	
	
	public MCalculator getFrequency() {
		return frequency;
	}
	public void setFrequency(MCalculator frequency) {
		this.frequency = frequency;
	}
	
	
	public void setWidth(MCalculator width) {
		this.width = width;
	}
	public MCalculator getWidth() {
		return this.width;
	}
	
	
	public void setHeight(MCalculator height) {
		this.height = height;
	}
	public MCalculator getHeight() {
		return this.height;
	}
	
	
	public void setXVelocity(MCalculator xVelocity) {
		this.x_velocity = xVelocity;
	}
	public MCalculator getXVelocity() {
		return this.x_velocity;
	}
	
	
	public void setYVelocity(MCalculator yVelocity) {
		this.y_velocity = yVelocity;
	}
	public MCalculator getYVelocity() {
		return this.y_velocity;
	}
	
	
	public void setDoppler(MCalculator doppler) {
		this.Doppler = doppler;
	}
	public MCalculator getDoppler() {
		return this.Doppler;
	}
	
	
	public void setWaveIterations(MCalculator waveIterations) {
		this.waveIterations = waveIterations;
	}
	public MCalculator getWaveIterations() {
		return this.waveIterations;
	}
	
	
	public void setNumWaves(MCalculator numWaves) {
		this.numWaves = numWaves;
	}
	public MCalculator getNumWaves() {
		return this.numWaves;
	}
	
	
	public MCalculator getLeftToRight() {
		return leftToRight;
	}
	public void setLeftToRight(MCalculator leftToRight) {
		this.leftToRight = leftToRight;
	}
	
	
	public MCalculator getMaxAmplitude() {
		return maxAmplitude;
	}
	public void setMaxAmplitude(MCalculator maxAmplitude) {
		this.maxAmplitude = maxAmplitude;
	}
	
	
	public MCalculator getPeriod() {
		return period;
	}
	public void setPeriod(MCalculator period) {
		this.period = period;
	}
	
	public MCalculator getPhaseShift() {
		return phaseShift;
	}
	
	public void setPhaseShift(MCalculator phaseShift) {
		this.phaseShift = phaseShift;
	}
	
	
	public MCalculator getWavelength() {
		return wavelength;
	}
	public void setWavelength(MCalculator wavelength) {
		this.wavelength = wavelength;
	}
	
	
	public MCalculator getX() {
		return x;
	}
	public void setX(MCalculator x) {
		this.x = x;
	}
	
	
	public MCalculator getY() {
		return y;
	}
	public void setY(MCalculator y) {
		this.y = y;
	}
}
