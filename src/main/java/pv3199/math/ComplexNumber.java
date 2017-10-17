package pv3199.math;

/**
 * Represents an imaginary number in the form of <code>a+bi</code>
 */
public class ComplexNumber extends Number implements Comparable<Number> {
	/**
	 * Real counter-part.
	 */
	public final double a;
	
	/**
	 * Imaginary counter-part.
	 */
	public final double b;
	
	/**
	 * R-value
	 */
	public final double r;
	
	/**
	 * Theta-value in radians
	 */
	public final double theta;
	
	/**
	 * Constructs a ComplexNumber given the real and imaginary counter-parts.
	 *
	 * @param a the real counter-part.
	 * @param b the imaginary counter-part.
	 */
	public ComplexNumber(double a, double b) {
		if (Double.isNaN(a)) {
			a = 0;
		}
		if (Double.isNaN(b)) {
			b = 0;
		}
		
		this.a = a;
		this.b = b;
		this.r = Math.sqrt(a * a + b * b);
		this.theta = Math.atan(b / a);
	}
	
	/**
	 * Constructs a ComplexNumber given a polar coordinate.
	 *
	 * @param pc the polar coordinate.
	 */
	public ComplexNumber(PolarCoordinate pc) {
		this.a = pc.r * Math.cos(pc.theta);
		this.b = pc.r * Math.sin(pc.theta);
		this.r = pc.r;
		this.theta = pc.theta;
	}
	
	/**
	 * Effectively clones a ComplexNumber.
	 *
	 * @param in the other imaginary number.
	 */
	public ComplexNumber(ComplexNumber in) {
		this(in.a, in.b);
	}
	
	/**
	 * Constructs a ComplexNumber from a Number instance.
	 *
	 * @param n the number.
	 */
	public ComplexNumber(Number n) {
		if (isImaginary(n)) {
			this.a = ((ComplexNumber) n).a;
			this.b = ((ComplexNumber) n).b;
			this.r = Math.sqrt(Math.pow(this.a, 2) + Math.pow(this.b, 2));
			this.theta = Math.atan(this.b / this.a);
		} else {
			this.a = n.doubleValue();
			this.b = 0;
			this.r = this.a;
			this.theta = 0;
		}
	}
	
	/**
	 * If the object is an ComplexNumber instance or not.
	 *
	 * @param o the object.
	 * @return true if the object is an instance of the ComplexNumber class.
	 */
	public final static boolean isImaginary(Object o) {
		return o instanceof ComplexNumber;
	}
	
	/**
	 * Unsupported operation
	 *
	 * @throws UnsupportedOperationException not supported
	 */
	@Override
	public double doubleValue() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Unsupported operation
	 *
	 * @throws UnsupportedOperationException not supported
	 */
	@Override
	public float floatValue() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Unsupported operation
	 *
	 * @throws UnsupportedOperationException not supported
	 */
	@Override
	public int intValue() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Unsupported operation
	 *
	 * @throws UnsupportedOperationException not supported
	 */
	@Override
	public long longValue() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @return the polar coordinate of this complex number.
	 */
	public PolarCoordinate toPolarCoordinate() {
		return new PolarCoordinate(r, theta);
	}
	
	/**
	 * Adds this and another number.
	 *
	 * @param n the number to add.
	 * @return the sum of this and another number.
	 */
	public ComplexNumber add(Number n) {
		if (ComplexNumber.class.isInstance(n)) {
			ComplexNumber in = (ComplexNumber) n;
			return new ComplexNumber(this.a + in.a, this.b + in.b);
		}
		
		return new ComplexNumber(this.a + n.doubleValue(), this.b);
	}
	
	/**
	 * Subtracts this number from another number.
	 *
	 * @param n the number to subtract.
	 * @return the difference between this number and another number.
	 */
	public ComplexNumber subtract(Number n) {
		if (ComplexNumber.class.isInstance(n)) {
			ComplexNumber in = (ComplexNumber) n;
			return new ComplexNumber(this.a - in.a, this.b - in.b);
		}
		
		return new ComplexNumber(this.a - n.doubleValue(), this.b);
	}
	
	/**
	 * Multiplies this and another number.
	 *
	 * @param n the number to multiply.
	 * @return the product of this and another number.
	 */
	public ComplexNumber multiply(Number n) {
		if (ComplexNumber.class.isInstance(n)) {
			ComplexNumber in = (ComplexNumber) n;
			double f = this.a * in.a;
			double o = this.a * in.b;
			double i = this.b * in.a;
			double l = this.b * in.b;
			return new ComplexNumber(f - l, o + i);
		}
		
		return new ComplexNumber(this.a * n.doubleValue(), this.b * n.doubleValue());
	}
	
	/**
	 * Divides this number by another number.
	 *
	 * @param n the number to divide by.
	 * @return the quotient of this number from another number.
	 */
	public ComplexNumber divide(Number n) {
		if (ComplexNumber.class.isInstance(n)) {
			ComplexNumber in = (ComplexNumber) n;
			
			double a = this.a;
			double b = this.b;
			double c = in.a;
			double d = in.b;
			
			if (c == 0 && d == 0) {
				throw new ArithmeticException("divide by 0");
			}
			
			double f = (b * c - a * d) / (c * c + d * d);
			double e = (a + f * d) / c;
			
			return new ComplexNumber(e, f);
		}
		
		return new ComplexNumber(this.a / n.doubleValue(), this.b / n.doubleValue());
	}
	
	/**
	 * @return the inverse of this number.
	 */
	public ComplexNumber inverse() {
		// equivalent to returning new ComplexNumber(1).divide(this);
		
		double d = (-this.b) / (this.b * this.b + this.a * this.a);
		double c = (1 + this.b * d) / a;
		
		return new ComplexNumber(c, d);
	}
	
	/**
	 * Raises this number to a power.
	 *
	 * @param power the power to raise this number by.
	 * @return this number raised to the particular power.
	 */
	public ComplexNumber pow(double power) {
		double rp = Math.pow(this.r, power);
		double ang = this.theta * power;
		
		return new ComplexNumber(rp * Math.cos(ang), rp * Math.sin(ang));
	}
	
	/**
	 * Raises this number to another imaginary number
	 *
	 * @param power the imaginary power to raise this number by
	 * @return this number raised to the imaginary power.
	 */
	public ComplexNumber pow(Number power) {
		// http://mathworld.wolfram.com/ComplexExponentiation.html
		
		// (a+bi)^(c+di) = (a^2+b^2)^(c/2) * exp(-d*atan(b/a)) * (
		// cos(c*atan(b/a)+1/2dln(a^2+b^2)) + isin(c*atan(b/a)+1/2dln(a^2+b^2)))
		
		ComplexNumber pow = new ComplexNumber(power);
		
		double r2 = Math.pow(this.r, 2);
		double out = Math.pow(r2, pow.a / 2) * Math.exp(-pow.b * this.theta);
		double ang = pow.a * this.theta + pow.b / 2 * Math.log(r2);
		double real = out * Math.cos(ang);
		double imag = out * Math.sin(ang);
		
		return new ComplexNumber(real, imag);
	}
	
	/**
	 * @return the absolute value of this number.
	 */
	public double abs() {
		return java.lang.Math.sqrt(this.a * this.a + this.b * this.b);
	}
	
	/**
	 * If the number to compare against is complex, the real values are prioritized first, and then the complex values.
	 * If the number to compare against is real, the real values are prioritized first, and then the sign of this
	 * number's complex value is used.
	 *
	 * @param n the number to compare against.
	 * @return -1, 0, or 1 if this number is less than, equal to, or greater than the other number.
	 */
	@Override
	public int compareTo(Number n) {
		// prioritize real values
		// if real values are equal, compare complex parts
		double real;
		double complex;
		
		if (n instanceof ComplexNumber) {
			ComplexNumber in = (ComplexNumber) n;
			real = this.a - in.a;
			complex = this.b - b;
		} else {
			real = this.a - n.doubleValue();
			complex = b;
		}
		
		int rc = real == 0 ? 0 : real > 0 ? 1 : -1;
		int cc = complex == 0 ? 0 : complex > 0 ? 1 : -1;
		
		return rc == 0 ? cc : rc;
	}
	
	/**
	 * @return a clone of this ComplexNumber
	 */
	@Override
	public ComplexNumber clone() {
		return new ComplexNumber(this);
	}
	
	/**
	 * Checks if this and another ComplexNumber are equal by comparing both the real and complex
	 * parts of both numbers.
	 *
	 * @param obj - the other ComplexNumber.
	 * @return true if the this and the other ComplexNumber have equal parts.
	 * @see #compareTo(Number)
	 */
	@Override
	public boolean equals(Object obj) {
		return this.compareTo((Number) obj) == 0;
	}
	
	@Override
	public String toString() {
		String realStr = (this.a != 0.0 ? (this.a == (int) this.a) ? (int) this.a : this.a : "") + "";
		String complexStr = (this.b != 0.0 ? (this.b == (int) this.b) ? (int) this.b : this.b : "") + "";
		boolean re = realStr.isEmpty(); // real is empty
		boolean ie = complexStr.isEmpty(); // complex is empty
		
		if (re && ie) {
			return "0";
		} else {
			return (re ^ ie ? re ? this.b < 0 ? complexStr.equals("-1") ? "-i" : complexStr + "i" : complexStr.equals("1") ? "i" : complexStr + "i" : realStr : realStr + (this.b < 0 ? complexStr.equals("-1") ? "-i" : complexStr + "i" : complexStr.equals("1") ? "+i" : "+" + complexStr + "i"));
		}
		
		// Keeping this comment block to make it easier to understand how the above ternary operator statements
		// work out
		/*StringBuilder s = new StringBuilder();
		if (re && ie) {
			return "0";
		} else if (re ^ ie) {
			if (re) {
				if (this.b < 0) {
					if (complexStr.equals("-1")) {
						s.append("-i");
					} else {
						s.append(complexStr + "i");
					}
				} else {
					if (complexStr.equals("1")) {
						s.append("i");
					} else {
						s.append(complexStr + "i");
					}
				}
			} else {
				s.append(real);
			}
		} else {
			s.append(realStr);

			if (this.b < 0) {
				if (complexStr.equals("-1")) {
					s.append("-i");
				} else {
					s.append(complexStr + "i");
				}
			} else {
				if (complexStr.equals("1")) {
					s.append("+i");
				} else {
					s.append("+").append(complexStr).append("i");
				}
			}
		}

		return s.toString();*/
	}
}
