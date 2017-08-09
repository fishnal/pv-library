package pv3199.lib.java.math;

import static java.lang.Math.abs;

/**
 * An immutable fraction that supports elementary mathematical operations,
 * exponentiation, and simplification.
 * 
 * @author Vishal Patel
 *
 */
public class Fraction implements java.io.Serializable {
	/**
	 * Fast, but less accurate, method for simplifying a Fraction.
	 */
	public final static int FAST_PRECISION = 10000;

	/**
	 * Default method for simplifying a Fraction.
	 */
	public final static int DEFAULT_PRECISION = 100000;

	/**
	 * Slow, but more accurate, method for simplifying a Fraction.
	 */
	public final static long ACCURATE_PRECISION = (long) 1E10;

	/**
	 * Numerator of the fraction.
	 */
	protected Number numerator;

	/**
	 * Denominator of the fraction.
	 */
	protected Number denominator;

	/**
	 * Constructs a Fraction object from a given value. The fraction is
	 * not simplified, and the numerator is set equal to decimal and denominator
	 * to 1.
	 * 
	 * @param value
	 *            - the value to construct this Fraction object from.
	 */
	public Fraction(Number value) {
		this(value, 1);
	}

	/**
	 * Constructs a Fraction from a numerator number and denominator number. The fraction
	 * is not simplified
	 * @param numerator - the numerator
	 * @param denominator - the denominator
	 * @throws ArithmeticException if the denominator is 0
	 */
	public Fraction(Number numerator, Number denominator) throws ArithmeticException {
		if (!(numerator instanceof ComplexNumber) && !numerator.getClass().getPackage().getName().equals("java.lang"))
			throw new IllegalArgumentException("unsupported class type: " + numerator.getClass().getName());
		if (!(denominator instanceof ComplexNumber) && !denominator.getClass().getPackage().getName().equals("java.lang"))
			throw new IllegalArgumentException("unsupported class type: " + denominator.getClass().getName());
		if (denominator.equals(0.0))
			throw new ArithmeticException("divide by 0");
		this.numerator = numerator;
		this.denominator = denominator;
	}

	/**
	 * @return the value of this fraction.
	 */
	public Number getValue() {
		return PVMath.divide(this.numerator, this.denominator);
	}

	/**
	 * Adds this fraction with another.
	 *
	 * @param f -
	 *            the other fraction to add with this fraction.
	 * @return the sum of this fraction and the other fraction.
	 */
	public Fraction add(Fraction f) {
		Number commonDenom = PVMath.multiply(this.denominator, f.denominator);
		Number left = PVMath.multiply(this.numerator, f.denominator);
		Number right = PVMath.multiply(f.numerator, this.denominator);
		Number num = PVMath.add(left, right);

		return new Fraction(num, commonDenom);
	}

    /**
     * Adds a number to this fraction.
     * @param n - the number to add
     * @return the sum of this fraction and the number.
     */
	public Fraction add(Number n) {
		return this.add(new Fraction(n));
    }

	/**
	 * Subtracts another fraction from this fraction.
	 *
	 * @param f -
	 *            the other fraction to subtract from this fraction.
	 * @return the difference between this fraction and the other fraction.
	 */
	public Fraction subtract(Fraction f) {
		return this.add(f.multiply(-1));
	}

    /**
     * Subtracts a number from this fraction.
     * @param n - the number to subtract by
     * @return the difference of this fraction and the number.
     */
    public Fraction subtract(Number n) {
    	return this.subtract(new Fraction(n));
    }

	/**
	 * Multiplies this fraction with another.
	 *
	 * @param f
	 *            - the other fraction to multiply with this fraction.
	 * @return the product of this fraction and the other fraction.
	 */
	public Fraction multiply(Fraction f) {
		return new Fraction(PVMath.multiply(this.numerator, f.numerator), PVMath.multiply(this.denominator, f.denominator));
	}

    /**
     * Multiplies a number to this fraction.
     * @param n - the number to multiply by
     * @return the product of this fraction and the number.
     */
    public Fraction multiply(Number n) {
        return this.multiply(new Fraction(n));
    }

	/**
	 * Divides this fraction by another.
	 *
	 * @param f
	 *            - the other fraction to divide this fraction by.
	 * @return the quotient of this fraction and the other fraction.
	 */
	public Fraction divide(Fraction f) {
		return this.multiply(f.inverse());
	}

    /**
     * Multiplies a number to this fraction.
     * @param n - the number to multiply by
     * @return the product of this fraction and the number.
     */
    public Fraction divide(Number n) {
        return this.divide(new Fraction(n));
    }

	/**
	 * @return this fraction's denominator over this fraction's numerator.
	 */
	public Fraction inverse() {
		return new Fraction(this.denominator, this.numerator);
	}

	/**
	 * Raises this fraction to a power.
	 *
	 * @param power
	 *            - the value to raise this fraction to.
	 * @return this fraction raised to some power.
	 */
	public Fraction pow(Number power) {
		return new Fraction(PVMath.pow(this.numerator, power), PVMath.pow(this.denominator, power));
	}

	public Fraction pow(Fraction f) {
	    return this.pow(f.getValue());
    }

	/**
	 * Simplifies this fraction such that the numerator and denominator are both
	 * integers.
	 *
	 * @return the simplified fraction using {@linkplain #DEFAULT_PRECISION
	 *         default precision}.
	 */
	public Fraction simplify() {
		return simplify(DEFAULT_PRECISION);
	}

	/**
	 * Simplifies this fraction such that the numerator and denominator are both
	 * integers. The precision passed in determines what the maximum value of
	 * the denominator will be. The larger the precision, the more accurate the
	 * simplification is. Simplification immediately stops when the simplified
	 * fraction's decimal value is equal to the original fraction.
	 * 
	 * @param precision - the precision value.
	 *
	 * @return the simplified fraction using a given precision value.
	 */
	public Fraction simplify(long precision) {
		// TODO simplify complex numbers
		try {
		    this.numerator.doubleValue();
		    this.denominator.doubleValue();
		    // both values are real
            long[] result = simplifyDecimal(this.numerator.doubleValue() / this.denominator.doubleValue(), precision);
            return new Fraction(result[0], result[1]);
        } catch (UnsupportedOperationException uoe) {
            // at least one of the numbers is complex
            ComplexNumber cn1 = this.numerator instanceof ComplexNumber ? (ComplexNumber) this.numerator : new ComplexNumber(this.numerator);
            ComplexNumber cn2 = this.denominator instanceof ComplexNumber ? (ComplexNumber) this.denominator : new ComplexNumber(this.denominator);

            long[] cn1A = simplifyDecimal(cn1.a, precision);
            long[] cn1B = simplifyDecimal(cn1.b, precision);
            long[] cn2A = simplifyDecimal(cn2.a, precision);
            long[] cn2B = simplifyDecimal(cn2.b, precision);

            ComplexNumber cn1Num = new ComplexNumber(cn1A[0] * cn1B[1], cn1A[1] * cn1B[0]);
            long cn1CF = PVMath.gcf(abs((long) cn1Num.a), abs((long) cn1Num.b));
            cn1Num = cn1Num.divide(cn1CF);
            ComplexNumber cn2Num = new ComplexNumber(cn2A[0] * cn2B[1], cn2A[1] * cn2B[0]);
            long cn2CF = PVMath.gcf(abs((long) cn2Num.a), abs((long) cn2Num.b));
            cn2Num = cn2Num.divide(cn2CF);
            double cn1Denom = cn1A[1] * cn1B[1] * cn2CF;
            double cn2Denom = cn2A[1] * cn2B[1] * cn1CF;

            long[] sim = simplifyDecimal(cn2Denom / cn1Denom, precision);
            long n = sim[0];
            long d = sim[1];
            return new Fraction(cn1Num.multiply(n), cn2Num.multiply(d));
        }
	}

	/**
	 * Simplifies a decimal given a precision value. Larger precision values sacrifice time for accuracy.
	 * @param decimal - the decimal to simplify.
	 * @param precision - the precision value
	 * @return  the simplified decimal using the given precision value.
	 */
	private static long[] simplifyDecimal(double decimal, long precision) {
        long bestDen = 1;
        long bestNum = 0;
        double bestDec = (double) bestNum / bestDen;

        for (long den = 1; den < precision && abs(bestDec - decimal) != 0.0; den++) {
            long num1 = (long) java.lang.Math.floor(den * decimal);
            long num2 = (long) java.lang.Math.ceil(den * decimal);

            double dec1 = (double) num1 / den;
            double dec2 = (double) num2 / den;

            double dec = abs(dec1 - decimal) < abs(dec2 - decimal) ? dec1 : dec2;
            long num = dec == dec1 ? num1 : num2;

            if (abs(dec - decimal) < abs(bestDec - decimal)) {
                bestDen = den;
                bestNum = num;
                bestDec = dec;
            }
        }

        return new long[]{bestNum, bestDen};
    }

	/**
	 * Makes a copy of this fraction.
	 */
	@Override
	public Fraction clone() {
		return new Fraction(this.numerator, this.denominator);
	}

	/**
	 * Compares the values of this and another fraction for equality.
	 *
	 * @return true if this fraction's value is equal to the other fraction's value.
	 */
	@Override
	public boolean equals(Object obj) {
	    Fraction f = (Fraction) obj;
	    return PVMath.equals(this.getValue(), f.getValue());
    }

	/**
	 * @return {@link #numerator} / {@link #denominator}.
	 */
	@Override
	public String toString() {
		return this.getValue().toString();
	}
}
