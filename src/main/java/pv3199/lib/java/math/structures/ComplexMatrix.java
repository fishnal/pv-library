package pv3199.lib.java.math.structures;

import pv3199.lib.java.math.ComplexNumber;

/**
 * Holds complex numbers in the form of a mathematical matrix.
 */
public class ComplexMatrix extends NumberMatrix<ComplexNumber> {
	/**
	 * Converts a 2D array of real numbers to complex numbers.
	 * @param realData the 2D array of real numbers to convert.
	 * @return a 2D array of complex numbers equivalent to the original 2D array.
	 */
	private static ComplexNumber[][] realDataToImaginaryData(Number[][] realData) {
		ComplexNumber[][] imagData = new ComplexNumber[realData.length][realData[0].length];
		
		for (int r = 0; r < imagData.length; r++)
			for (int c = 0; c < imagData[0].length; c++)
				imagData[r][c] = new ComplexNumber(realData[r][c]);
		
		return imagData;
	}

	/**
	 * Constructs a new ComplexMatrix with given width and height dimensions.
	 * @param width the width of the matrix.
	 * @param height the height of the matrix.
	 * @throws IllegalArgumentException if the dimensions are negative.
	 */
	public ComplexMatrix(int width, int height) throws IllegalArgumentException {
		super(width, height);

		// zero all values
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {
				this.setValue(r, c, new ComplexNumber(0, 0));
			}
		}
	}

	/**
	 * Constructs a ComplexMatrix from a 2D array of complex numbers.
	 * @param initialData the initial data that this matrix will hold.
	 * @throws NullPointerException if any of the elements in the initial data array are null
	 * @throws IllegalArgumentException if the initial data array is not rectangular
	 */
	public ComplexMatrix(ComplexNumber[][] initialData) throws NullPointerException, IllegalArgumentException {
		super(initialData);

		int cdr = checkData(this.getRawData());

		if (cdr == -1)
			throw new NullPointerException("null values are not allowed");
	}

	/**
	 * Effectively clones an ComplexMatrix.
	 * @param matrix the complex matrix to clone
	 */
	public ComplexMatrix(ComplexMatrix matrix) {
		this(matrix.getData());
	}

	/**
	 * Constructs a ComplexMatrix from data stored in a RealMatrix.
	 * @param matrix the real matrix data to store into this complex matrix.
	 */
	public ComplexMatrix(RealMatrix matrix) {
		this(ComplexMatrix.realDataToImaginaryData(matrix.getData()));
	}

	/**
	 * Gets the identity matrix based on this matrix's size.
	 * @return an identity matrix in the form of a complex matrix.
	 */
	public ComplexMatrix getIdentityMatrix() {
		int size = java.lang.Math.max(width, height);
		ComplexMatrix id = new ComplexMatrix(size, size);

		for (int i = 0; i < size; i++)
			id.setValue(i, i, new ComplexNumber(1, 0));

		return id;
	}
	
	@Override
	public void add(Number n) {
		for (int r = 0; r < this.height; r++)
			for (int c = 0; c < this.width; c++) {
				this.setValue(r, c, this.getValue(r, c).add(n));
			}
	}

	@Override
	public ComplexMatrix add(NumberMatrix<ComplexNumber> matrix) throws IllegalArgumentException {
		if (matrix == null)
			throw new NullPointerException("null argument");
		if (!ComplexMatrix.class.isInstance(matrix))
			throw new IllegalArgumentException("must be of type ComplexMatrix");
		if (!sameDimensions(matrix))
			throw new IllegalArgumentException("matrices must have same dimensions");

		ComplexMatrix sum = new ComplexMatrix(this.width, this.height);

		for (int r = 0; r < sum.height; r++)
			for (int c = 0; c < sum.width; c++) {
				ComplexNumber tn = this.getValue(r, c);
				ComplexNumber mn = matrix.getValue(r, c);
				sum.setValue(r, c, tn.add(mn));
			}

		return sum;
	}

	@Override
	public void subtract(Number n) {
		for (int r = 0; r < this.height; r++)
			for (int c = 0; c < this.width; c++) {
				this.setValue(r, c, this.getValue(r, c).subtract(n));
			}
	}

	@Override
	public ComplexMatrix subtract(NumberMatrix<ComplexNumber> matrix)
			throws IllegalArgumentException {
		if (matrix == null)
			throw new NullPointerException("null argument");
		if (!ComplexMatrix.class.isInstance(matrix))
			throw new IllegalArgumentException("must be of type ComplexMatrix");
		if (!sameDimensions(matrix))
			throw new IllegalArgumentException("matrices must have same dimensions");

		ComplexMatrix sum = new ComplexMatrix(this.width, this.height);

		for (int r = 0; r < sum.height; r++)
			for (int c = 0; c < sum.width; c++) {
				ComplexNumber tn = this.getValue(r, c);
				ComplexNumber mn = matrix.getValue(r, c);
				sum.setValue(r, c, tn.subtract(mn));
			}

		return sum;
	}

	@Override
	public void multiply(Number n) {
		for (int r = 0; r < this.height; r++)
			for (int c = 0; c < this.width; c++) {
				this.setValue(r, c, this.getValue(r, c).multiply(n));
			}
	}

	@Override
	public ComplexMatrix multiply(NumberMatrix<ComplexNumber> matrix)
			throws IllegalArgumentException {
		if (matrix == null)
			throw new NullPointerException("null argument");
		if (!ComplexMatrix.class.isInstance(matrix))
			throw new IllegalArgumentException("must be of type ComplexMatrix");
		if (this.width != matrix.height)
			throw new IllegalArgumentException(
					"number of columns in first matrix must equal number of rows in second matrix");

		ComplexMatrix product = new ComplexMatrix(matrix.width, this.height);

		for (int rt = 0; rt < this.height; rt++) {
			for (int cm = 0; cm < matrix.width; cm++) {
				ComplexNumber sum = new ComplexNumber(0, 0);

				for (int ct = 0, rm = 0; ct < this.width; ct++, rm++) {
					ComplexNumber tn = this.getValue(rt, ct);
					ComplexNumber mn = matrix.getValue(rm, cm);
					sum = sum.add(tn.multiply(mn));
				}

				product.setValue(rt, cm, sum);
			}
		}

		return product;
	}

	@Override
	public void divide(Number n) {
		for (int r = 0; r < this.height; r++)
			for (int c = 0; c < this.width; c++) {
				this.setValue(r, c, this.getValue(r, c).divide(n));
			}
	}

	@Override
	public ComplexMatrix divide(NumberMatrix<ComplexNumber> matrix)
			throws IllegalArgumentException, IllegalMatrixException {
		if (matrix == null)
			throw new NullPointerException("null argument");

		ComplexMatrix inv = (ComplexMatrix) matrix.inverse();
		if (inv == null)
			throw new IllegalMatrixException("second matrix doesn't have an inverse");

		return this.multiply(inv);
	}

	@Override
	public ComplexMatrix pow(int power) throws IllegalMatrixException {
		if (power == 0)
			return this.getIdentityMatrix();
		
		if (!isSquare(this))
			throw new IllegalMatrixException("matrix must be square");
		
		ComplexMatrix powd = power < 0 ? this.inverse() : this.clone();
		ComplexMatrix multiplyAgainst = powd.clone();
		
		for (int i = 1; i < Math.abs(power); i++) {
			powd = powd.multiply(multiplyAgainst);
		}

		return powd;
	}

	@Override
	public ComplexNumber determinant() throws IllegalMatrixException {
		if (this.height == 0 || this.width == 0)
			throw new IllegalMatrixException("matrix must have non-zero dimensions");
		if (!isSquare(this))
			throw new IllegalMatrixException("matrix must be square");
		return determinant0(this.getData());
	}

	/**
	 * Recursively calculates the determinant of a particular sub-matrix of this matrix.
	 * @param data the sub-matrix
	 * @return a complex number representing the determinant of a sub-matrix.
	 */
	private ComplexNumber determinant0(ComplexNumber[][] data) {
		int size = data.length;
		
		if (size == 1)
			return data[0][0];

		ComplexNumber det = new ComplexNumber(0, 0);

		for (int c = 0; c < size; c++) {
			ComplexNumber multiplier = data[0][c].multiply(c % 2 == 0 ? 1 : -1);

			ComplexNumber[][] subData = new ComplexNumber[size - 1][size - 1];
			for (int csd = 0, c2 = 0; c2 < size; c2++) {
				if (c2 == c)
					continue;

				for (int rsd = 0, r2 = 1; r2 < size; r2++, rsd++) {
					subData[rsd][csd] = data[r2][c2];
				}

				csd++;
			}

			det = det.add(multiplier.multiply(determinant0(subData)));
		}

		return det;
	}

	@Override
	public ComplexMatrix inverse() throws IllegalMatrixException {
		if (!isSquare(this))
			throw new IllegalMatrixException("matrix must be square");

		ComplexNumber detOfThis = this.determinant();
		ComplexMatrix mocfTp = matrixOfCoFactors().transpose();

		if (detOfThis.equals(0))
			return null;

		mocfTp.divide(detOfThis);

		return mocfTp;
	}

	@Override
	public ComplexMatrix transpose() {
		ComplexMatrix tp = new ComplexMatrix(this.height, this.width);

		for (int r = 0; r < tp.height; r++) {
			for (int c = 0; c < tp.width; c++) {
				tp.setValue(r, c, this.getValue(c, r));
			}
		}

		return tp;
	}

	@Override
	public ComplexMatrix matrixOfMinors() throws IllegalMatrixException {
		if (!isSquare(this))
			throw new IllegalMatrixException("matrix must be square");

		ComplexMatrix mom = this.clone();

		for (int r = 0; r < this.height; r++) {
			for (int c = 0; c < this.width; c++) {
				ComplexNumber[][] subData = new ComplexNumber[this.height - 1][this.width - 1];

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

				ComplexNumber detOfSubData = determinant0(subData);

				mom.setValue(r, c, detOfSubData);
			}
		}

		return mom;
	}

	@Override
	public ComplexMatrix matrixOfCoFactors() throws IllegalMatrixException {
		if (!isSquare(this))
			throw new IllegalMatrixException("matrix must be square");

		ComplexMatrix mom = matrixOfMinors();

		for (int r = 0; r < mom.height; r++) {
			for (int c = r % 2 == 0 ? 1 : 0; c < mom.width; c += 2) {
				mom.setValue(r, c, mom.getValue(r, c).multiply(-1));
			}
		}

		return mom;
	}

	@Override
	public ComplexNumber[][] getData() {
		ComplexNumber[][] copyOfData = new ComplexNumber[this.height][this.width];
		
		for (int r = 0; r < this.height; r++)
			for (int c = 0; c < this.width; c++)
				copyOfData[r][c] = this.getValue(r, c);
		
		return copyOfData;
	}

	@Override
	public ComplexMatrix clone() {
		return new ComplexMatrix(this);
	}

	@Override
	public boolean equals(Object obj) {
		ComplexMatrix matrix = (ComplexMatrix) obj;

		if (this.height != matrix.height || this.width != matrix.width)
			return false;

		for (int r = 0; r < this.height; r++)
			for (int c = 0; c < this.width; c++)
				if (!this.getValue(r, c).equals(matrix.getValue(r, c)))
					return false;

		return true;
	}

	@Override
	public String toString() {
		if (this.height == 0 || this.width == 0) {
			return "empty";
		}
		
		StringBuilder s = new StringBuilder();
		StringBuilder[] strArr = new StringBuilder[this.height];

		int longestLength = 0;
		for (int r = 0; r < this.height; r++) {
			strArr[r] = new StringBuilder();
			for (int c = 0; c < this.width; c++) {
				ComplexNumber d = this.getValue(r, c);
				
				if (d.toString().length() > longestLength) {
					longestLength = d.toString().length();
				}
				
				strArr[r].append(d + (c == this.width - 1 ? "" : "!"));
			}
		}

		for (StringBuilder s2 : strArr) {
			while (s2.indexOf("!") >= 0) {
				int lim = s2.lastIndexOf("!");
				int lim2 = s2.substring(0, lim).lastIndexOf("!");

				int length = lim2 == -1 ? lim : lim - lim2 - 1;

				StringBuilder spaces = new StringBuilder(" ");

				for (int i = 0; i < longestLength - length; i++) {
					spaces.append(" ");
				}

				s2 = new StringBuilder(s2.substring(0, lim2 + 1) + s2.substring(lim2 + 1, lim) + spaces + s2.substring(lim + 1));
			}

			s.append(s2).append("\n");
		}

		return s.toString().trim();

	}
}
