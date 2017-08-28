package pv3199.lib.java.math.structures;

/**
 * A matrix holding numbers.
 *
 * @param <T> the type of number for this matrix to hold.
 */
public abstract class NumberMatrix<T extends Number> extends Matrix<T> {
	/**
	 * Constructs a NumberMatrix with a given width and height.
	 *
	 * @param width  the width of the matrix.
	 * @param height the height of the matrix.
	 * @throws IllegalArgumentException if the dimensions are negative.
	 */
	protected NumberMatrix(int width, int height) throws IllegalArgumentException {
		super(width, height, false);
	}
	
	/**
	 * Constructs a NumberMatrix with a given initial 2D array.
	 *
	 * @param initialData the initial data for this matrix.
	 * @throws NullPointerException     if the initial data is null.
	 * @throws IllegalArgumentException if the initial data is not rectangular or contains null values.
	 */
	public NumberMatrix(T[][] initialData) throws NullPointerException, IllegalArgumentException {
		super(initialData, false);
	}
	
	/**
	 * Constructs a NumberMatrix from another NumberMatrix.
	 *
	 * @param matrix the other number matrix.
	 * @throws NullPointerException if the other matrix is null.
	 */
	public NumberMatrix(NumberMatrix<T> matrix) throws NullPointerException {
		super(matrix);
	}
	
	/**
	 * Gets an identity matrix of doubles of the given size.
	 *
	 * @param size the size of the identity matrix.
	 * @return an identity matrix of doubles of a particular size.
	 */
	public static NumberMatrix<Double> identityMatrix(int size) {
		RealMatrix id = new RealMatrix(size, size);
		
		for (int i = 0; i < size; i++) {
			id.setValue(i, i, 1);
		}
		
		return id;
	}
	
	/**
	 * Adds a number to each element in this matrix.
	 *
	 * @param n the number to add.
	 */
	public abstract void add(Number n);
	
	/**
	 * Adds this and another matrix together.
	 *
	 * @param matrix the other matrix.
	 * @return the sum of this and the other matrix.
	 * @throws IllegalArgumentException if this and the other matrix do not have the same dimensions.
	 */
	public abstract NumberMatrix<T> add(NumberMatrix<T> matrix) throws IllegalArgumentException;
	
	/**
	 * Subtracts a number from each element in this matrix.
	 *
	 * @param n the number to subtract.
	 */
	public abstract void subtract(Number n);
	
	/**
	 * Subtracts this matrix from another matrix.
	 *
	 * @param matrix the other matrix.
	 * @return the difference between this matrix and the other matrix.
	 * @throws IllegalArgumentException if this and the other matrix do not have the same dimensions.
	 */
	public abstract NumberMatrix<T> subtract(NumberMatrix<T> matrix) throws IllegalArgumentException;
	
	/**
	 * Multiplies a number to each element in this matrix.
	 *
	 * @param n the number to multiply.
	 */
	public abstract void multiply(Number n);
	
	/**
	 * Multiplies this matrix and another matrix.
	 *
	 * @param matrix the other matrix.
	 * @return the product of this matrix and the other matrix.
	 * @throws IllegalArgumentException if the width of this matrix is not the same as the height of the other matrix.
	 */
	public abstract NumberMatrix<T> multiply(NumberMatrix<T> matrix) throws IllegalArgumentException;
	
	/**
	 * Divides a number from each element in this matrix.
	 *
	 * @param n the number to divide.
	 */
	public abstract void divide(Number n);
	
	/**
	 * "Divides" this matrix from another matrix, or simply multiplies this matrix with the
	 * inverse of the other matrix.
	 *
	 * @param matrix the other matrix.
	 * @return the quotient of this matrix against the other matrix.
	 * @throws IllegalArgumentException if the other matrix is not a square (for it's inverse) or if the width of this
	 *                                  matrix is not same as the height of the other matrix's inverse.
	 */
	public abstract NumberMatrix<T> divide(NumberMatrix<T> matrix) throws IllegalArgumentException, IllegalMatrixException;
	
	/**
	 * Raises this matrix to an integer exponent. Simply multiplies the matrix by itself <code>power</code> times.
	 *
	 * @param power the number to raise the matrix by.
	 * @return this matrix raised to an integer exponent.
	 * @throws IllegalMatrixException if this matrix is not a square
	 */
	public abstract NumberMatrix<T> pow(int power) throws IllegalMatrixException;
	
	/**
	 * Gets the determinant of this matrix.
	 *
	 * @return the determinant of this matrix.
	 * @throws IllegalMatrixException if this matrix is not a square
	 */
	public abstract Number determinant() throws IllegalMatrixException;
	
	
	/**
	 * Gets the determinant of a sub-matrix within this matrix.
	 *
	 * @param startRow    the starting row (inclusive)
	 * @param startColumn the starting column (inclusive)
	 * @param endRow      the ending row (exclusive)
	 * @param endColumn   the ending column (exclusive)
	 * @return the determinant of a sub-matrix.
	 * @throws IndexOutOfBoundsException if the indices are out of bounds
	 * @throws IllegalMatrixException    if the resulting sub-matrix is not a square.
	 */
	public abstract Number subDeterminant(int startRow, int startColumn, int endRow, int endColumn) throws IndexOutOfBoundsException, IllegalMatrixException;
	
	/**
	 * Gets the inverse of this matrix.
	 *
	 * @return the inverse of this matrix.
	 * @throws IllegalMatrixException if this matrix is not a square
	 */
	public abstract NumberMatrix<T> inverse() throws IllegalMatrixException;
	
	/**
	 * Gets the transpose of this matrix.
	 *
	 * @return the transpose of this matrix.
	 */
	public abstract NumberMatrix<T> transpose();
	
	/**
	 * Gets the matrix of minors for this matrix.
	 *
	 * @return the matrix of minors for this matrix.
	 * @throws IllegalMatrixException if this matrix is not a square
	 */
	public abstract NumberMatrix<T> matrixOfMinors() throws IllegalMatrixException;
	
	/**
	 * Gets the matrix of co-factors for this matrix.
	 *
	 * @return the matrix of co-factors for this matrix.
	 * @throws IllegalMatrixException if this matrix is not a square
	 */
	public abstract NumberMatrix<T> matrixOfCoFactors() throws IllegalMatrixException;
}