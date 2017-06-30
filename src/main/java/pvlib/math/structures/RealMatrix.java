package pvlib.math.structures;

import pvlib.math.Fraction;

public final class RealMatrix extends NumberMatrix<Double> {
	/**
	 * Equality tolerance.
	 */
	private static double epsilon = 1E-10;

	/**
	 * Sets the equality tolerance when comparing values.
	 * @param newTolerance the new tolerance value.
	 */
	public static void setEqualityTolerance(double newTolerance) {
		RealMatrix.epsilon = newTolerance;
	}

	/**
	 * Gets the equality tolerance value.
	 * @return the equality tolerance value.
	 */
	public static double getEqualityTolerance() {
		return RealMatrix.epsilon;
	}

	/**
	 * If the {@link #toString()} method should return the values as fractions or decimals.
	 */
	private boolean displayFractions = false;

	/**
	 * Constructs a RealMatrix with the given width and height.
	 * @param width the width of the matrix.
	 * @param height the height of the matrix.
	 * @throws IllegalArgumentException if the dimensions are negative.
	 */
	public RealMatrix(int width, int height) throws IllegalArgumentException {
		super(width, height);

		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {
				this.setValue(r, c, 0.0);
			}
		}
	}

	/**
	 * Constructs a RealMatrix with initial data.
	 * @param initialData the initial data to store in this matrix.
	 * @throws NullPointerException if the initial data is null
	 * @throws IllegalArgumentException if the initial data is not a rectangular array or has null values
	 * @throws ClassCastException if there's an issue in casting the elements in the initial data to double types.
	 */
	public RealMatrix(Number[][] initialData)
			throws NullPointerException, IllegalArgumentException, ClassCastException {
		// this(pvlib.util.Arrays.castArray(initialData, Double[][].class));
		super(pvlib.util.Arrays.castArray(initialData, Double[][].class));
		
		int vdr = checkData(this.getRawData());

		if (vdr == 1) {
			throw new NullPointerException("null values are not allowed");
		}
	}

	/**
	 * Effectively clones a RealMatrix.
	 * @param matrix - the other matrix.
	 */
	public RealMatrix(RealMatrix matrix) {
		this(matrix.getData());
	}

	/**
	 * Change whether or not the {@link #toString()} prints the values as fractions or decimals.
	 * @param state whether or not to display the object using fractions or decimals.
	 */
	public void displayUsingFractions(boolean state) {
		this.displayFractions = state;
	}

	/**
	 * Gets the identity matrix for this matrix based on its size.
	 * @return the identity matrix for this matrix.
	 */
	public RealMatrix getIdentityMatrix() {
		int size = java.lang.Math.max(width, height);
		RealMatrix id = new RealMatrix(size, size);

		for (int i = 0; i < size; i++)
			id.setValue(i, i, 1.0);

		return id;
	}

	/**
	 * Sets the value at a particular row-column index.
	 * @param row the row index.
	 * @param col the column index.
	 * @param value the new value.
	 */
	public void setValue(int row, int col, Number value) {
		super.setValue(row, col, value.doubleValue());
	}

	@Override
	public void add(Number n) throws NullPointerException {
		if (n == null)
			throw new NullPointerException("null argument");

		for (int r = 0; r < this.height; r++) {
			for (int c = 0; c < this.width; c++) {
				double sum = this.getValue(r, c).doubleValue() + n.doubleValue();
				this.setValue(r, c, sum);
			}
		}
	}

	@Override
	public RealMatrix add(NumberMatrix<Double> matrix) throws NullPointerException, IllegalArgumentException {
		if (matrix == null)
			throw new NullPointerException("null argument");
		if (!RealMatrix.class.isInstance(matrix))
			throw new IllegalArgumentException("must be of type RealMatrix");
		if (!sameDimensions(matrix))
			throw new IllegalArgumentException("matrices must have same dimensions");

		RealMatrix sum = new RealMatrix(this.width, this.height);

		for (int r = 0; r < sum.height; r++)
			for (int c = 0; c < sum.width; c++) {
				Number tn = this.getValue(r, c);
				Number mn = matrix.getValue(r, c);
				sum.setValue(r, c, tn.doubleValue() + mn.doubleValue());
			}

		return sum;
	}

	@Override
	public void subtract(Number n) throws NullPointerException {
		add(-n.doubleValue());
	}

	@Override
	public RealMatrix subtract(NumberMatrix<Double> matrix) throws NullPointerException, IllegalArgumentException {
		if (matrix == null)
			throw new NullPointerException("null argument");
		if (!RealMatrix.class.isInstance(matrix))
			throw new IllegalArgumentException("must be of type RealMatrix");
		if (!sameDimensions(matrix))
			throw new IllegalArgumentException("matrices must have same dimensions");

		RealMatrix diff = new RealMatrix(this.width, this.height);

		for (int r = 0; r < diff.height; r++)
			for (int c = 0; c < diff.width; c++) {
				Number tn = this.getValue(r, c);
				Number mn = matrix.getValue(r, c);
				diff.setValue(r, c, tn.doubleValue() - mn.doubleValue());
			}

		return diff;
	}

	@Override
	public void multiply(Number n) throws NullPointerException {
		if (n == null)
			throw new NullPointerException("null argument");

		for (int r = 0; r < this.height; r++)
			for (int c = 0; c < this.width; c++) {
				double product = this.getValue(r, c).doubleValue() * n.doubleValue();
				this.setValue(r, c, product);
			}
	}

	@Override
	public RealMatrix multiply(NumberMatrix<Double> matrix) throws NullPointerException, IllegalArgumentException {
		if (matrix == null)
			throw new NullPointerException("null argument");
		if (!RealMatrix.class.isInstance(matrix))
			throw new IllegalArgumentException("must be of type RealMatrix");
		if (this.width != matrix.height)
			throw new IllegalArgumentException(
					"number of columns in first matrix must equal number of rows in second matrix");

		RealMatrix product = new RealMatrix(this.height, matrix.width);

		for (int rt = 0; rt < this.height; rt++) {
			for (int cm = 0; cm < matrix.width; cm++) {
				double sum = 0.0;

				for (int ct = 0, rm = 0; ct < this.width; ct++, rm++) {
					sum += this.getValue(rt, ct).doubleValue() * matrix.getValue(rm, cm).doubleValue();
				}

				product.setValue(rt, cm, sum);
			}
		}

		return product;
	}

	@Override
	public void divide(Number n) throws NullPointerException {
		multiply(1 / n.doubleValue());
	}

	@Override
	public RealMatrix divide(NumberMatrix<Double> matrix)
			throws NullPointerException, IllegalArgumentException, IllegalMatrixException {
		if (matrix == null)
			throw new NullPointerException("null argument");

		RealMatrix inv = (RealMatrix) matrix.inverse();
		if (inv == null)
			throw new IllegalMatrixException("second matrix doesn't have an inverse");
		RealMatrix quotient = this.multiply(inv);

		return quotient;
	}

	@Override
	public RealMatrix inverse() throws IllegalMatrixException {
		if (!isSquare(this))
			throw new IllegalMatrixException("matrix must be square");

		double detOfThis = this.determinant();
		RealMatrix mocfTp = matrixOfCoFactors().transpose();

		if (detOfThis == 0.0)
			return null;

		mocfTp.divide(detOfThis);

		return mocfTp;
	}

	@Override
	public RealMatrix transpose() {
		RealMatrix tp = new RealMatrix(this.height, this.width);

		for (int r = 0; r < tp.height; r++) {
			for (int c = 0; c < tp.width; c++) {
				tp.setValue(r, c, this.getValue(c, r));
			}
		}

		return tp;
	}

	@Override
	public RealMatrix matrixOfMinors() throws IllegalMatrixException {
		if (!isSquare(this))
			throw new IllegalMatrixException("matrix must be square");

		// No mom, I won't take out the trash
		RealMatrix mom = this.clone();

		for (int r = 0; r < this.height; r++) {
			for (int c = 0; c < this.width; c++) {
				Double[][] subData = new Double[this.height - 1][this.width - 1];

				for (int r2 = 0, rsd = 0; rsd < subData.length; r2++) {
					if (r2 == r)
						continue;

					for (int c2 = 0, csd = 0; csd < subData[rsd].length; c2++) {
						if (c2 == c)
							continue;

						subData[rsd][csd] = this.getValue(r2, c2);
						csd++;
					}

					rsd++;
				}

				double detOfSubData = determinant0(subData);

				mom.setValue(r, c, detOfSubData);
			}
		}

		return mom;
	}

	@Override
	public RealMatrix matrixOfCoFactors() throws IllegalMatrixException {
		if (!isSquare(this))
			throw new IllegalMatrixException("matrix must be square");

		RealMatrix mom = matrixOfMinors();

		for (int r = 0; r < mom.height; r++) {
			for (int c = r % 2 == 0 ? 1 : 0; c < mom.width; c += 2) {
				mom.setValue(r, c, -mom.getValue(r, c));
			}
		}

		return mom;
	}

	@Override
	public RealMatrix pow(int power) {
		if (power == 0) {
			return this.getIdentityMatrix();
		}

		if (!isSquare(this))
			throw new IllegalMatrixException("matrix must be square");

		RealMatrix powd = power < 0 ? this.inverse() : this.clone();
		RealMatrix multiplyAgainst = power < 0 ? this.inverse() : this.clone();

		for (int i = 1; i < Math.abs(power); i++) {
			powd = powd.multiply(multiplyAgainst);
		}

		return powd;
	}

	@Override
	public Double determinant() throws IllegalMatrixException {
		if (this.height == 0 || this.width == 0)
			throw new IllegalMatrixException("matrix must have non-zero dimensions");
		if (!isSquare(this))
			throw new IllegalMatrixException("matrix must be square");
		return determinant0(this.getData());
	}

	/**
	 * Recursively calculates the determinant of a particular sub-matrix of this matrix.
	 * @param data the sub-matrix
	 * @return an imaginary number representing the determinant of a sub-matrix.
	 */
	private double determinant0(Double[][] data) {
		int size = data.length;

		if (size == 1)
			return data[0][0].doubleValue();

		double det = 0;

		for (int c = 0; c < size; c++) {
			double multiplier = (c % 2 == 0 ? 1 : -1) * data[0][c];

			Double[][] subData = new Double[size - 1][size - 1];
			for (int csd = 0, c2 = 0; c2 < size; c2++) {
				if (c2 == c)
					continue;

				for (int rsd = 0, r2 = 1; r2 < size; r2++, rsd++) {
					subData[rsd][csd] = data[r2][c2];
				}

				csd++;
			}

			det += multiplier * determinant0(subData);
		}

		return det;
	}

	@Override
	public Double[][] getData() {
		Double[][] copyOfData = new Double[this.height][this.width];

		for (int r = 0; r < this.height; r++)
			for (int c = 0; c < this.width; c++)
				copyOfData[r][c] = this.getValue(r, c);

		return copyOfData;
	}

	/**
	 * Converts this matrix to an imaginary matrix.
	 * @return the equivalent imaginary matrix.
	 */
	public ComplexMatrix toImaginaryMatrix() {
		return new ComplexMatrix(this);
	}

	@Override
	public RealMatrix clone() {
		return new RealMatrix(this);
	}

	@Override
	public boolean equals(Object obj) {
		RealMatrix matrix = (RealMatrix) obj;

		if (this.height != matrix.height || this.width != matrix.width)
			return false;

		for (int r = 0; r < this.height; r++)
			for (int c = 0; c < this.width; c++)
				if (Math.abs(this.getValue(r, c) - matrix.getValue(r, c)) > RealMatrix.epsilon)
					return false;

		return true;
	}

	@Override
	public String toString() {
		if (this.height == 0 || this.width == 0)
			return "empty";
		
		String s = "";
		String[] strArr = new String[this.height];

		int longestLength = 0;
		for (int r = 0; r < this.height; r++) {
			strArr[r] = "";
			for (int c = 0; c < this.width; c++) {
				Number d = this.getValue(r, c);
				Fraction f = new Fraction(d.doubleValue(), 1);

				if (displayFractions) {
					f = f.simplify();
					if (f.toString().length() > longestLength)
						longestLength = f.toString().length();
				} else if (!displayFractions && d.toString().length() > longestLength) {
					if (d.doubleValue() == d.longValue())
						d = (Long) d.longValue();
					longestLength = d.toString().length();
				}

				strArr[r] += (displayFractions ? f : d) + (c == this.width - 1 ? "" : "!");
			}
		}

		for (String s2 : strArr) {
			while (s2.contains("!")) {
				int lim = s2.lastIndexOf("!");
				int lim2 = s2.substring(0, lim).lastIndexOf("!");

				int length = lim2 == -1 ? lim : lim - lim2 - 1;

				String spaces = " ";

				for (int i = 0; i < longestLength - length; i++)
					spaces += " ";

				s2 = s2.substring(0, lim2 + 1) + s2.substring(lim2 + 1, lim) + spaces + s2.substring(lim + 1);
			}

			s += s2 + "\n";
		}

		return s.trim();
	}
}
