package pv3199.lib.java.math.structures;

import pv3199.lib.java.math.PVMath;
import pv3199.lib.java.util.Arrays;

/**
 * An immutable Euclidean vector.
 */
public class Vector {
	/**
	 * The number of scalar components in this vector.
	 */
	public final int size;
	/**
	 * The magnitude of this vector.
	 */
	public final double magnitude;
	/**
	 * Scalar components.
	 */
	private double[] components;
	
	/**
	 * Constructs a Vector from an array of doubles, each representing a scalar space.
	 *
	 * @param components the scalar components of the vector.
	 */
	public Vector(double... components) {
		this.components = components.clone();
		this.size = components.length;
		double mag = 0;
		for (double d : components) {
			mag += d * d;
		}
		this.magnitude = Math.sqrt(mag);
	}
	
	/**
	 * Constructs a Vector from two arrays of doubles, the first representing the starting coordinates of
	 * the vector and the second representing the ending coordinates of the vector. These coordinates are
	 * used in determining the scalar components of the vector.
	 *
	 * @param start the starting coordinates of the vector.
	 * @param end   the ending coordinates of the vector.
	 * @throws IllegalVectorException if the coordinate arrays have different lengths
	 */
	public Vector(double[] start, double[] end) throws IllegalVectorException {
		int length;
		if ((length = start.length) != end.length) {
			throw new IllegalVectorException("start and end different lengths");
		}
		
		this.components = new double[length];
		this.size = length;
		double mag = 0;
		for (int i = 0; i < length; i++) {
			this.components[i] = end[i] - start[i];
			mag += this.components[i] * this.components[i];
		}
		this.magnitude = Math.sqrt(mag);
	}
	
	/**
	 * Produces a standard vector given the space index and the number of dimensions of the vector.
	 * For example, <code>standard(0, 2)</code> will produce a 2-dimensional vector that with its
	 * first scalar component set equal to 1 and the rest of the components equal to 0. Similarly,
	 * <code>standard(2, 5)</code> produces a 5-dimensional vector with its third component equal to 1.
	 * All standard vectors are unit, or have a magnitude of 1.
	 *
	 * @param space      the space index.
	 * @param dimensions the size of the vector.
	 * @return the standard vector based on the space index and number of dimensions.
	 */
	public static Vector standard(int space, int dimensions) {
		double[] comps = new double[dimensions];
		comps[space] = 1;
		return new Vector(comps);
	}
	
	/**
	 * Gets the scalar component at the specified index.
	 *
	 * @param index the index of the scalar component.
	 * @return the scalar component at an index.
	 */
	public double get(int index) {
		return this.components[index];
	}
	
	/**
	 * Produces a vector unit to this vector.
	 *
	 * @return this vector's unit vector.
	 */
	public Vector unit() {
		return this.divide(this.magnitude);
	}
	
	/**
	 * Multiplies this vector by a scalar.
	 *
	 * @param d the scalar.
	 * @return this vector multiplied by a scalar.
	 */
	public Vector multiply(double d) {
		double[] comps = this.components.clone();
		
		for (int i = 0; i < this.size; i++) {
			comps[i] *= d;
		}
		
		return new Vector(comps);
	}
	
	/**
	 * Divides this vector by a scalar.
	 *
	 * @param d the scalar.
	 * @return this vector divided by a scalar.
	 */
	public Vector divide(double d) {
		return multiply(1 / d);
	}
	
	/**
	 * Finds the angle, in radians, between this vector and another vector.
	 *
	 * @param v the other vector.
	 * @return the angle, in radians, between this vector and another vector.
	 */
	public double angle(Vector v) {
		return Math.acos(this.dot(v) / (this.magnitude * v.magnitude));
	}
	
	/**
	 * Dots this vector with another vector.
	 *
	 * @param v the other vector.
	 * @return the dot product of this vector and another vector.
	 * @throws IllegalVectorException if the vectors are not the same size.
	 */
	public double dot(Vector v) throws IllegalVectorException {
		if (this.size != v.size) {
			throw new IllegalVectorException("vectors are not the same size");
		}
		
		// guaranteed to return a Double object because we're only operating on doubles
		return PVMath.summation(index -> {
			int i = (int) index;
			return this.components[i] * v.components[i];
		}, 0, this.size - 1).doubleValue();
	}
	
	/**
	 * Crosses this vector with another vector.
	 *
	 * @param v the other vector.
	 * @return the cross product of this vector and another vertex.
	 * @throws IllegalVectorException if the vectors are not the same size
	 */
	public Vector cross(Vector v) throws IllegalVectorException {
		int s1 = this.size;
		int s2 = v.size;
		
		if (s1 < 2 || s1 > 3 || s2 < 2 || s2 > 3) {
			throw new IllegalVectorException("vectors are not 2d or 3d");
		}
		
		if (s1 != s2) {
			throw new IllegalVectorException("vectors are not the same size");
		}
		
		if (s1 == 2) {
			// 2d vector
			return new Vector(0, 0, this.components[0] * v.components[1] - this.components[1] * v.components[0]);
		} else {
			return new Vector(this.components[1] * v.components[2] - this.components[2] * v.components[1], this.components[2] * v.components[0] - this.components[0] * v.components[2], this.components[0] * v.components[1] - this.components[1] * v.components[0]);
		}
	}
	
	/**
	 * Checks if this vector is orthogonal with another vector. Two vectors are said to be orthogonal
	 * when their dot product is 0.
	 *
	 * @param v the other vector.
	 * @return true if this vector is orthogonal with another vector.
	 * @throws IllegalVectorException if the vectors are not the same size.
	 */
	public boolean isOrthogonal(Vector v) throws IllegalVectorException {
		return this.dot(v) == 0;
	}
	
	/**
	 * Compares the sizes and their scalar components of this vector and another vector.
	 *
	 * @param obj the other vector.
	 * @return true if this vector equals another vector based on the sizes and their scalar components
	 */
	@Override
	public boolean equals(Object obj) {
		Vector v = (Vector) obj;
		
		if (this.size != v.size) {
			return false;
		}
		
		return Arrays.equals(this.components, v.components);
	}
}