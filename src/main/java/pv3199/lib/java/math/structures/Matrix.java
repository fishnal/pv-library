package pv3199.lib.java.math.structures;

import pv3199.lib.java.util.ConsumerHolder;
import pv3199.lib.java.util.ForEachHolder;

import java.lang.reflect.Array;
import java.util.function.Consumer;

/**
 * A matrix structure intended to hold elements in a rectangular 2D array.
 *
 * @param <T> the data this matrix structure holds.
 */
public abstract class Matrix<T> implements java.io.Serializable {
	/**
	 * If this matrix allows null values.
	 */
	public final boolean allowsNull;
	/**
	 * The height of this matrix.
	 */
	public final int height;
	/**
	 * The width of this matrix.
	 */
	public final int width;
	/**
	 * The raw, backing Object data of this matrix.
	 */
	private final Object[][] data;
	
	/**
	 * Constructs a Matrix with a boolean indicating whether it will accept null values or not and width
	 * and height dimensions.
	 *
	 * @param width      the width of the matrix.
	 * @param height     the height of the matrix.
	 * @param allowsNull if this matrix will accept null values or not.
	 * @throws IllegalArgumentException if the dimensions are negative.
	 */
	public Matrix(int width, int height, boolean allowsNull) throws IllegalArgumentException {
		if (width < 0 || height < 0) {
			throw new IllegalArgumentException("dimensions must be non-negative");
		}
		
		this.data = new Object[this.height = height][this.width = width];
		this.allowsNull = allowsNull;
	}
	
	/**
	 * Constructs a Matrix with a boolean indicating whether it will accept null values or not and initial data
	 * to store into this matrix.
	 *
	 * @param initialData the initial data for this matrix to hold.
	 * @param allowsNull  if this matrix will accept null values or not.
	 * @throws NullPointerException     if the initial data is null
	 * @throws IllegalArgumentException if the initial data array is not rectangular or if the initial data contains
	 *                                  null values and this matrix is not intended to accept null values.
	 */
	public Matrix(T[][] initialData, boolean allowsNull) throws NullPointerException, IllegalArgumentException {
		if (initialData == null) {
			throw new NullPointerException();
		} else if (!isRectangle(initialData)) {
			throw new IllegalArgumentException("initialData must be a rectangular array");
		}
		
		this.allowsNull = allowsNull;
		int cdr = checkData(initialData);
		if (cdr == -1) {
			throw new IllegalArgumentException("initialData contains null values and matrix does" + "not accept null values");
		}
		
		this.data = new Object[this.height = initialData.length][];
		
		for (int r = 0; r < this.data.length; r++) {
			this.data[r] = initialData[r].clone();
		}

		this.width = this.height == 0 ? 0 : this.data[0].length;
	}
	
	/**
	 * Constructs a Matrix from another matrix.
	 *
	 * @param matrix the other matrix.
	 * @throws NullPointerException if the initial matrix is null.
	 */
	public Matrix(Matrix<T> matrix) throws NullPointerException {
		if (matrix == null) {
			throw new NullPointerException();
		}
		
		this.data = matrix.getRawData();
		this.height = matrix.height;
		this.width = matrix.width;
		this.allowsNull = matrix.allowsNull;
	}
	
	/**
	 * Checks if a 2D array is rectangular (the lengths of each array in the 2D array are equal).
	 *
	 * @param array the 2D array.
	 * @return true if the array is rectangular; false otherwise.
	 */
	protected final static boolean isRectangle(Object[][] array) {
		for (int r = 1; r < array.length; r++) {
			if (array[r - 1] == null || array[r] == null || array[r - 1].length != array[r].length) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Checks if a 2D array is a square (the lengths of each array in the 2D array are equal to the
	 * height of the 2D array).
	 *
	 * @param array the 2D array.
	 * @return true if the array is square; false otherwise.
	 */
	protected final static boolean isSquare(Object[][] array) {
		return array != null && array.length == 0 ? true : array.length == array[0].length;
	}
	
	/**
	 * Checks if a matrix is a square.
	 *
	 * @param matrix the matrix.
	 * @return true if the matrix is a square; false otherwise.
	 */
	protected final static boolean isSquare(Matrix<?> matrix) {
		return matrix.width == matrix.height;
	}
	
	/**
	 * Checks a 2D array for null values.
	 *
	 * @param data the 2D array.
	 * @return if the 2D array has null values.
	 */
	protected final static int checkData(Object[][] data) {
		for (int r = 0; r < data.length; r++) {
			for (int c = 0; c < data[r].length; c++) {
				if (data[r][c] == null) return -1;
			}
		}
		
		return 0;
	}
	
	/**
	 * Sets the value at a particular row-column index.
	 *
	 * @param row   the row index.
	 * @param col   the column.
	 * @param value the new value.
	 * @throws NullPointerException if this matrix does not accept null values and the new value is null.
	 */
	public final void setValue(int row, int col, T value) throws NullPointerException {
		if (!allowsNull && value == null) {
			throw new NullPointerException("null values are illegal");
		}

		data[row][col] = value;
	}
	
	/**
	 * Gets the value at a particular row-column index.
	 *
	 * @param row the row index.
	 * @param col the column index.
	 * @return the value at the row-column index.
	 */
	public final T getValue(int row, int col) {
		return (T) data[row][col];
	}
	
	/**
	 * Gets the raw data of this matrix.
	 *
	 * @return a 2D object array containing the raw data of this matrix.
	 */
	public final Object[][] getRawData() {
		Object[][] copy = new Object[this.data.length][];
		
		for (int r = 0; r < copy.length; r++) {
			copy[r] = this.data[r].clone();
		}
		
		return copy;
	}
	
	/**
	 * Gets the elements in a row of this matrix.
	 *
	 * @param row   - the row index
	 * @param clazz - the type this matrix holds.
	 * @return an array of the elements in a row.
	 */
	public final T[] getRow(int row, Class<T> clazz) {
		Object data = Array.newInstance(clazz, this.width);
		for (int c = 0; c < this.width; c++) {
			Array.set(data, c, this.getValue(row, c));
		}
		return (T[]) data;
	}
	
	/**
	 * Gets the elements in a column of this matrix.
	 *
	 * @param column - the column index
	 * @param clazz  - the type this matrix holds.
	 * @return an array of the elements in a column.
	 */
	public final T[] getColumn(int column, Class<T> clazz) {
		Object data = Array.newInstance(clazz, this.height);
		for (int r = 0; r < this.height; r++) {
			Array.set(data, r, this.getValue(r, column));
		}
		return (T[]) data;
	}
	
	/**
	 * Gets the casted data of this matrix.
	 *
	 * @return a 2D T-array containing the casted values of this matrix.
	 */
	public abstract T[][] getData();
	
	/**
	 * Checks if this and another matrix have the same width and height.
	 *
	 * @param other the other matrix.
	 * @return true if this and the other matrix have the same dimensions.
	 */
	protected final boolean sameDimensions(Matrix<T> other) {
		return this.height == other.height && this.width == other.width;
	}
	
	/**
	 * Iterates through each element in the matrix. Instead of using a {@link Consumer}
	 * that accepts the generic type of this matrix, the consumer accepts a {@link ForEachHolder}
	 * that has the same generic type of this matrix. The {@code ForEachHolder} allows for the
	 * consumer implementation to access what indices the matrix is currently iterating at.
	 * The iteration goes from rows to columns (top to bottom, left to right), similar to a
	 * 2D-array. As such, the {@code ForEachHolder}'s indices will have the row index in the
	 * first element, column index in the second element, and the matrix element as it's data.
	 * If the implementation does not need to access the indices of each iteration, then it is
	 * advised to utilize {@link #forEach(Consumer)} for efficiency.
	 *
	 * @param action - the consumer to utilize when iterating through each element.
	 * @see ForEachHolder
	 * @see Consumer
	 * @see ConsumerHolder
	 */
	public final void forEach(ConsumerHolder<T> action) {
		for (int r = 0; r < this.height; r++) {
			for (int c = 0; c < this.width; c++) {
				action.accept(new ForEachHolder<>(this.getValue(r, c), r, c));
			}
		}
	}
	
	/**
	 * Iterates through each element in the matrix, performing a consumer operation on each.
	 * Iteration goes from rows to columns (top to bottom, left to right), similar to a 2D-array.
	 * This is a more efficient alternative to {@link #forEach(ConsumerHolder)}.
	 *
	 * @param action the consumer to utilize when iterating through each element.
	 */
	public final void forEach(Consumer<T> action) {
		for (int r = 0; r < this.height; r++) {
			for (int c = 0; c < this.width; c++) {
				action.accept(this.getValue(r, c));
			}
		}
	}
	
	/**
	 * Iterates through each row in the matrix. Instead of using a {@link Consumer} that accepts
	 * the generic type of this matrix, the consumer accepts a {@link ForEachHolder}
	 * that has the same generic type of this matrix. The {@code ForEachHolder} allows for the consumer
	 * implementation to access the row-index the matrix is currently iterating at. The iteration
	 * goes from top to bottom. As such the {@code ForEachHolder}'s indices will have the row index
	 * in the first element, and will contain a {@code T} array, representing the current row, as it's data.
	 * If the implementation does not need to access the indices of each iteration, then it is
	 * advised to utilize {@link #forEach(Consumer)} for efficiency.
	 *
	 * @param action - the consumer to utilize when iterating through each row.
	 * @param clazz  - the type the matrix holds.
	 * @see ForEachHolder
	 * @see Consumer
	 * @see ConsumerHolder
	 */
	public final void forEachRow(ConsumerHolder<T[]> action, Class<T> clazz) {
		for (int r = 0; r < this.height; r++) {
			action.accept(new ForEachHolder<>(this.getRow(r, clazz), r));
		}
	}
	
	/**
	 * Iterates through each row in the matrix, performing a consumer operation on each.
	 * Iteration goes from top to bottom. This is a more efficient alternative to
	 * {@link #forEachRow(ConsumerHolder, Class)}.
	 *
	 * @param action the consumer to utilize when iterating through each row.
	 * @param clazz the type the matrix holds.
	 */
	public final void forEachRow(Consumer<T[]> action, Class<T> clazz) {
		for (int r = 0; r < this.height; r++) {
			action.accept(this.getRow(r, clazz));
		}
	}
	
	/**
	 * Iterates through each column in the matrix. Instead of using a {@link Consumer} that accepts
	 * the generic type of this matrix, the consumer accepts a {@link ForEachHolder}
	 * that has the same generic type of this matrix. The {@code ForEachHolder} allows for the consumer
	 * implementation to access the column-index the matrix is currently iterating at. The iteration
	 * goes from left to right. As such the {@code ForEachHolder}'s indices will have the column index
	 * in the first element, and will contain a {@code T} array, representing the current column, as it's data.
	 * If the implementation does not need to access the indices of each iteration, then it is
	 * advised to utilize {@link #forEach(Consumer)} for efficiency.
	 *
	 * @param action - the consumer to utilize when iterating through each column.
	 * @param clazz  - the type the matrix holds.
	 * @see ForEachHolder
	 * @see Consumer
	 * @see ConsumerHolder
	 */
	public final void forEachColumn(ConsumerHolder<T[]> action, Class<T> clazz) {
		for (int c = 0; c < this.width; c++) {
			action.accept(new ForEachHolder<>(this.getColumn(c, clazz), c));
		}
	}
	
	/**
	 * Iterates through each column in the matrix, performing a consumer operation on each.
	 * Iteration goes from left to right. This is a more efficient alternative to
	 * {@link #forEachColumn(ConsumerHolder, Class)}.
	 *
	 * @param action the consumer to utilize when iterating through each column.
	 * @param clazz the type the matrix holds.
	 */
	public final void forEachColumn(Consumer<T[]> action, Class<T> clazz) {
		for (int c = 0; c < this.width; c++) {
			action.accept(this.getColumn(c, clazz));
		}
	}
	
	/**
	 * Clones this matrix such that this matrix's backing 2D object array is not deeply-equal to the clone's backing
	 * 2D object array.
	 *
	 * @return a clone of this matrix.
	 */
	public abstract Matrix<T> clone();
	
	/**
	 * Checks if this and another matrix are equal to each other.
	 *
	 * @param obj the other matrix.
	 * @return true if this and the other matrix are equal to each other.
	 */
	public abstract boolean equals(Object obj);
}
