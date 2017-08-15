package pv3199.lib.java.math.structures;

import java.util.function.Consumer;

import pv3199.lib.java.math.ComplexNumber;
import pv3199.lib.java.math.PVMath;
import pv3199.lib.java.util.Arrays;
import pv3199.lib.java.util.ConsumerHolder;
import pv3199.lib.java.util.ForEachHolder;

/**
 * An immutable Euclidean vector. Vectors can be in the real or complex plane depending on how they are
 * instantiated.
 */
public class Vector {
	/**
	 * The number of scalar components in this vector.
	 */
	public final int size;
	/**
	 * The magnitude of this vector.
	 */
	public final Number magnitude;
	/**
	 * Scalar components.
	 */
	private Number[] components;
	/**
	 * Whether or not this vector is in the complex plane.
	 */
	public final boolean isComplex;
	
	/**
	 * Constructs a Vector from an array of doubles, each representing a scalar space.
	 * The resulting vector will be in the real plane.
	 *
	 * @param components the scalar components of the vector.
	 */
	public Vector(double... components) {
		this(Arrays.castArray(components, Double[].class));
	}

	/**
	 * Constructs a Vector from an array of Numbers, each representing a scalar space.
	 * The resulting vector is not guaranteed to be in the real plane (unless called
	 * from {@link Vector#Vector(double...)})
	 * 
	 * @param components the scalar components of the vector.
	 */
	public Vector(Number... components) {
		this.components = components.clone();
		this.size = components.length;
		Number mag = 0;
		for (Number n : components) {
			mag = PVMath.add(mag, PVMath.pow(n, 2));
		}
		this.magnitude = PVMath.sqrt(mag);
		this.isComplex = this.magnitude instanceof ComplexNumber;
	}
	
	/**
	 * Constructs a Vector from two arrays of doubles, the first representing the starting coordinates of
	 * the vector and the second representing the ending coordinates of the vector. These coordinates are
	 * used in determining the scalar components of the vector. The resulting vector will be in the real plane.
	 *
	 * @param start the starting coordinates of the vector.
	 * @param end   the ending coordinates of the vector.
	 * @throws IllegalVectorException if the coordinate arrays have different lengths
	 */
	public Vector(double[] start, double[] end) throws IllegalVectorException {
		this(Arrays.castArray(start, Double[].class), Arrays.castArray(end, Double[].class));
	}

	/**
	 * Constructs a Vector from two arrays of Numbers, the first representing the starting coordinates of
	 * the vector and the second representing the ending coordinates of the vector. These coordinates are
	 * used in determining the scalar components of the vector. The resulting vector is not guaranteed
	 * to be in the real plane (unless called from {@link Vector#Vector(double[], double[])})
	 *
	 * @param start the starting coordinates of the vector.
	 * @param end   the ending coordinates of the vector.
	 * @throws IllegalVectorException if the coordinate arrays have different lengths
	 */
	public Vector(Number[] start, Number[] end) throws IllegalVectorException {
		int length;
		if ((length = start.length) != end.length) {
			throw new IllegalVectorException("start and end different lengths");
		}
		
		this.components = new Number[length];
		this.size = length;
		Number mag = 0;
		for (int i = 0; i < length; i++) {
			this.components[i] = PVMath.subtract(end[i], start[i]);
			mag = PVMath.add(mag, PVMath.pow(this.components[i], 2));
		}
		this.magnitude = PVMath.sqrt(mag);
		this.isComplex = this.magnitude instanceof ComplexNumber;
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
	public Number get(int index) {
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
	 * @param n the scalar.
	 * @return this vector multiplied by a scalar.
	 */
	public Vector multiply(Number n) {
		Number[] comps = this.components.clone();
		
		for (int i = 0; i < this.size; i++) {
			comps[i] = PVMath.multiply(comps[i], n);
		}
		
		return new Vector(comps);
	}
	
	/**
	 * Divides this vector by a scalar.
	 *
	 * @param n the scalar.
	 * @return this vector divided by a scalar.
	 */
	public Vector divide(Number n) {
		return multiply(PVMath.invert(n));
	}
	
	/**
	 * Finds the angle, in radians, between this vector and another vector.
	 *
	 * @param v the other vector.
	 * @return the angle, in radians, between this vector and another vector.
	 */
	public Number angle(Vector v) {
		return PVMath.acos(PVMath.divide(this.dot(v), PVMath.multiply(this.magnitude, v.magnitude)));
	}

	/**
	 * Adds this and another vector together.
	 * 
	 * @param v the other vector.
	 * @return the resulting sum vector from adding this and another vector together.
	 * @throws IllegalVectorException if the vectors are not the same size.
	 */
	public Vector add(Vector v) throws IllegalVectorException {
		if (this.size != v.size) {
			throw new IllegalVectorException("vectors are not the same size");
		}

		Number[] sum = new Number[this.size];
		for (int i = 0; i < this.size; i++) {
			sum[i] = PVMath.add(this.components[i], v.components[i]);
		}
		return new Vector(sum);
	}

	/**
	 * Subtracts this from another vector.
	 * 
	 * @param v the other vector.
	 * @return the resulting difference vector from subtracts this from another vector.
	 * @throws IllegalVectorException if the vectors are not the same size.
	 */
	public Vector subtract(Vector v) throws IllegalVectorException {
		return this.add(v.multiply(-1));
	}
	
	/**
	 * Dots this vector with another vector.
	 *
	 * @param v the other vector.
	 * @return the dot product of this vector and another vector.
	 * @throws IllegalVectorException if the vectors are not the same size.
	 */
	public Number dot(Vector v) throws IllegalVectorException {
		if (this.size != v.size) {
			throw new IllegalVectorException("vectors are not the same size");
		}
		
		return PVMath.summation(index -> {
			int i = (int) index;
			return PVMath.multiply(this.components[i], v.components[i]);
		}, 0, this.size - 1);
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
			return new Vector(
				0, 0,
				PVMath.subtract(PVMath.multiply(this.components[0], v.components[1]), PVMath.multiply(this.components[1], v.components[0]))
			);
		} else {
			return new Vector(
				PVMath.subtract(PVMath.multiply(this.components[1], v.components[2]), PVMath.multiply(this.components[2], v.components[1])),
				PVMath.subtract(PVMath.multiply(this.components[2], v.components[0]), PVMath.multiply(this.components[0], v.components[2])),
				PVMath.subtract(PVMath.multiply(this.components[0], v.components[1]), PVMath.multiply(this.components[1], v.components[0]))
			);
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
		return PVMath.equals(this.dot(v), 0);
	}

	/**
	 * Iterates through each scalar component in this vector, applying a consumer that accepts a
	 * {@link ForEachHolder} containing the current scalar component value and current iterating index.
	 * If the consumer implementation does not need to access the indices for each iteration, it is advised
	 * to utilize {@link #forEach(Consumer)}.
	 * 
	 * @param action the consumer action to apply on each scalar component value.
	 */
	public void forEach(ConsumerHolder<Number> action) {
		for (int i = 0; i < this.size; i++) {
			action.accept(new ForEachHolder<>(this.components[i], i));
		}
	}
	
	/**
	 * Iterates through each scalar component in this vector, applying a consumer operation on each
	 * number. This is a more efficient alternative to {@link #forEach(ConsumerHolder)}.
	 *
	 * @param action the consumer action to apply on each scalar component value.
	 */
	public void forEach(Consumer<Number> action) {
		for (int i = 0; i < this.size; i++) {
			action.accept(this.components[i]);
		}
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
		
		return Arrays.deepEquals(this.components, v.components);
	}
}