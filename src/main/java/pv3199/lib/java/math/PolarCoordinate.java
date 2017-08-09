package pv3199.lib.java.math;

/**
 * Represents a polar coordinate with an r-value and theta-value.
 */
public class PolarCoordinate implements java.io.Serializable {
	/**
	 * R-value.
	 */
	public final double r;
	
	/**
	 * Theta-value in radians.
	 */
	public final double theta;
	
	/**
	 * Translated x-coordinate.
	 */
	public final double x;
	
	/**
	 * Translated y-coordinate.
	 */
	public final double y;
	
	/**
	 * Constructs a PolarCoordinate from an r and theta-value.
	 *
	 * @param r     the r-value
	 * @param theta the theta-value
	 */
	public PolarCoordinate(double r, double theta) {
		this.r = r;
		this.theta = theta;
		this.x = r * Math.cos(theta);
		this.y = r * Math.sin(theta);
	}
	
	/**
	 * @return the complex number value of this polar coordinate.
	 */
	public ComplexNumber getValue() {
		return new ComplexNumber(this.r * Math.cos(theta), this.r * Math.sin(theta));
	}
	
	/**
	 * @return (r, theta)
	 */
	@Override
	public String toString() {
		return String.format("(%f,%f)", this.r, this.theta);
	}
}
