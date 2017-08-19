package pv3199.lib.java.math;

import java.util.*;
import java.util.function.DoubleFunction;

/**
 * Intended to be an extension of the {@link java.lang.Math} class. Supports the Java SDK's {@link java.lang.Byte},
 * {@link java.lang.Short}, {@link java.lang.Integer}, {@link java.lang.Long}, {@link java.lang.Float}, and
 * {@link java.lang.Double} classes, in addition to this library's {@link pv3199.lib.java.math.ComplexNumber} class. All
 * operations are performed using the Java SDK's Math class implementation if the input is valid for such method
 * signatures and bounds. For example, calling <code>PVMath.acos(1)</code> will call <code>Math.acos(1)</code> and
 * return a value of <code>0.0</code>; however <code>PVMath.acos(2)</code> will not call <code>Math.acos(2)</code>
 * as that will {@link Double#NaN not be a real number}. Instead PVMath's implementation of the inverse cosine is
 * utilized.
 */
public final class PVMath {
	/**
	 * Constant of Math.log(10)
	 */
	public final static double LOG10 = Math.log(10);
	/**
	 * Constant of Math.log(2)
	 */
	public final static double LOG2 = Math.log(2);
	/**
	 * Acceptable data types for performing mathematical operations.
	 */
	private final static Class<? extends Number>[] ACCEPTED_TYPES = new Class[]{Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, ComplexNumber.class};
	/**
	 * Comparator used to sort the classes; sorts based on the name of the class defined by {@link Class#getName()}.
	 * Used to binary search the array of accepted types for checking if a data type is accepted by this Math class.
	 *
	 * @see #isAccepted(Class)
	 */
	private final static Comparator<Class<? extends Number>> ACCEPTED_TYPES_COMPARATOR = (c1, c2) -> c1.getName().compareTo(c1.getName());
	
	/**
	 * Static initilization; sort {@link #ACCEPTED_TYPES} using {@link #ACCEPTED_TYPES_COMPARATOR}.
	 */
	static {
		java.util.Arrays.sort(ACCEPTED_TYPES, ACCEPTED_TYPES_COMPARATOR);
	}
	
	/**
	 * @return an array of Number sub-classes that are accepted by this Math class.
	 */
	public static Class<? extends Number>[] getAcceptedTypes() {
		return ACCEPTED_TYPES == null ? null : java.util.Arrays.copyOf(ACCEPTED_TYPES, ACCEPTED_TYPES.length);
	}
	
	/**
	 * Checks if a Number sub-class is accepted by this Math class.
	 *
	 * @param numberSubClass - the sub-class to check if it is accepted.
	 * @return true if the sub-class is accepted; false otherwise
	 */
	public static boolean isAccepted(Class<? extends Number> numberSubClass) {
		return Arrays.binarySearch(ACCEPTED_TYPES, numberSubClass, ACCEPTED_TYPES_COMPARATOR) > 0;
	}
	
	/**
	 * Adds two numbers together.
	 *
	 * @param n1 - the first operand.
	 * @param n2 - the second operand.
	 * @return the sum of the two numbers (possibly a {@link ComplexNumber}).
	 */
	public static Number add(Number n1, Number n2) {
		if (n1 instanceof ComplexNumber) return ((ComplexNumber) n1).add(n2);
		else if (n2 instanceof ComplexNumber) return ((ComplexNumber) n2).add(n1);
		else return n1.doubleValue() + n2.doubleValue();
	}
	
	/**
	 * Subtracts two numbers from each other.
	 *
	 * @param n1 - the first operand.
	 * @param n2 - the second operand.
	 * @return - the difference between the two numbers (possibly a {@link ComplexNumber}).
	 */
	public static Number subtract(Number n1, Number n2) {
		if (n1 instanceof ComplexNumber) return ((ComplexNumber) n1).subtract(n2);
		else if (n2 instanceof ComplexNumber) return ((ComplexNumber) n2).multiply(-1).add(n1);
		else return n1.doubleValue() - n2.doubleValue();
	}
	
	/**
	 * Multiplies two numbers against each other.
	 *
	 * @param n1 - the first operand.
	 * @param n2 - the second operand.
	 * @return the product of the two numbers (possibly a {@link ComplexNumber}).
	 */
	public static Number multiply(Number n1, Number n2) {
		if (n1 instanceof ComplexNumber) return ((ComplexNumber) n1).multiply(n2);
		else if (n2 instanceof ComplexNumber) return ((ComplexNumber) n2).multiply(n1);
		else return n1.doubleValue() * n2.doubleValue();
	}
	
	/**
	 * Divides one number from another.
	 *
	 * @param n1 - the first operand.
	 * @param n2 - the second operand.
	 * @return the quotient of the two numbers (possibly a {@link ComplexNumber}).
	 */
	public static Number divide(Number n1, Number n2) {
		if (n1 instanceof ComplexNumber) return ((ComplexNumber) n1).divide(n2);
		else if (n2 instanceof ComplexNumber) return ((ComplexNumber) n2).inverse().multiply(n1);
		else return n1.doubleValue() / n2.doubleValue();
	}
	
	/**
	 * @param x - a number.
	 * @return the absolute value of the number (complex if input is complex).
	 */
	public static Number abs(Number x) {
		if (x instanceof ComplexNumber) {
			ComplexNumber cn = (ComplexNumber) x;
			return new ComplexNumber(Math.abs(cn.a), Math.abs(cn.b));
		}
		
		return Math.abs(x.doubleValue());
	}
	
	/**
	 * Takes the inverse cosine of a number. The inverse cosine of a number is obtained by the following:
	 * <code>acos(x) = pi / 2 + iln(ix + sqrt(1 - x^2))</code>. The result is complex when the input is
	 * complex or if the input is not in the range [-1, 1].
	 *
	 * @param x - a number.
	 * @return the inverse cosine of the number (possibly a {@link ComplexNumber}).
	 */
	public static Number acos(Number x) {
		if (!(x instanceof ComplexNumber) && Math.abs(x.doubleValue()) <= 1.0) {
			return Math.acos(x.doubleValue());
		}
		
		// acos(x) = pi/2 + iln(ix + sqrt(1 - xx))
		// a = ix
		// b = sqrt(1 - xx)
		// c = ln(a + b)
		Number a = new ComplexNumber(0, 1).multiply(x);
		Number b = sqrt(subtract(1, pow(x, 2)));
		Number c = log(add(a, b));
		return add(Math.PI / 2, new ComplexNumber(0, 1).multiply(c));
	}
	
	/**
	 * Takes the inverse hyperbolic cosine of a number. The inverse hyperbolic cosine of a number is obtained by the
	 * following: <code>acosh(x) = ln(sqrt(x-1) * sqrt(x+1) + x)</code>. The result is complex when the input
	 * is complex or if the input is not in the range [1, infinity].
	 *
	 * @param x - a number.
	 * @return the inverse hyperbolic cosine of the number (possibly a {@link ComplexNumber}).
	 */
	public static Number acosh(Number x) {
		// acosh(x) = ln(sqrt(x-1) * sqrt(x+1) + x)
		// a = sqrt(x - 1)
		// b = sqrt(x + 1)
		Number a = sqrt(subtract(x, 1));
		Number b = sqrt(add(x, 1));
		return log(add(multiply(a, b), x));
	}
	
	/**
	 * Takes the inverse sine of a number. The inverse sine of a number is obtained by the following:
	 * <code>asin(x) = -i * ln(ix + sqrt(1 - x^2))</code>. The result is complex when the input is complex
	 * or if the input is not in the range [-1, 1].
	 *
	 * @param x - a number.
	 * @return the inverse sine of the number (possibly a {@link ComplexNumber}).
	 */
	public static Number asin(Number x) {
		if (!(x instanceof ComplexNumber) && Math.abs(x.doubleValue()) <= 1.0) {
			return Math.asin(x.doubleValue());
		}
		
		// asin(x) = -i * ln(ix + sqrt(1 - xx))
		// a = ix
		// b = sqrt(1 - xx)
		// c = ln(a + b)
		Number a = new ComplexNumber(0, 1).multiply(x);
		Number b = sqrt(subtract(1, pow(x, 2)));
		Number c = log(add(a, b));
		return new ComplexNumber(0, -1).multiply(c);
	}
	
	/**
	 * Takes the inverse hyperbolic sine of a number. The inverse hyperbolic sine of a number is obtained by the
	 * following: <code>asinh(x) = ln(x + sqrt(x^2 + 1))</code>. The result is complex when the input is complex.
	 *
	 * @param x - a number.
	 * @return the inverse hyperbolic sine of the number (possibly a {@link ComplexNumber}).
	 */
	public static Number asinh(Number x) {
		// asinh(x) = ln(x + sqrt(xx + 1))
		// a = sqrt(xx + 1)
		Number a = sqrt(add(pow(x, 2), 1));
		return log(add(x, a));
	}
	
	/**
	 * Takes the inverse tangent of a number. The inverse tangent of a number is obtained by the following:
	 * <code>atan(x) = i/2 * ln((1-xi)/(1+xi))</code>. The result is complex when the input is complex.
	 *
	 * @param x - a number.
	 * @return the inverse tangent of the number (possibly a {@link ComplexNumber}).
	 */
	public static Number atan(Number x) {
		if (!(x instanceof ComplexNumber)) {
			return Math.atan(x.doubleValue());
		}
		
		// atan(x) = i/2 * ln((1-xi)/(1+xi))
		// a = xi
		// b = 1 - a
		// c = 1 + a
		// d = ln(b / c)
		Number a = new ComplexNumber(0, 1).multiply(x);
		Number b = subtract(1, a);
		Number c = add(1, a);
		Number d = log(divide(b, c));
		return new ComplexNumber(0, .5).multiply(d);
	}
	
	/**
	 * Takes the inverse tangent of two numbers composing a vector.
	 *
	 * @param y - the y component.
	 * @param x - the x component.
	 * @return the inverse tangent of the vector provided (possibly a {@link ComplexNumber}).
	 * @see #atan(Number)
	 */
	public static Number atanh2(Number y, Number x) {
		return atan(divide(y, x));
	}
	
	/**
	 * Takes the inverse hyperbolic tangent of a number. The inverse hyperbolic tangent of a number is obtained
	 * by the following: <code>atanh(x) = 1/2 * ln((1+x)/(1-x))</code>. The result is complex when the input is
	 * complex or not in the range (-1, 1).
	 *
	 * @param x - a number.
	 * @return the inverse hyperbolic tangent of the number (possibly a {@link ComplexNumber}).
	 */
	public static Number atanh(Number x) {
		// atanh(x) = 1/2 * ln((1+x)/(1-x))
		// a = 1 + x
		// b = 1 - x
		Number a = add(1, x);
		Number b = subtract(1, x);
		return divide(log(divide(a, b)), 2);
	}
	
	/**
	 * Takes the cube root a number. The result is complex if the input is complex.
	 *
	 * @param x - a number.
	 * @return the cube root of the number (possibly a {@link ComplexNumber}).
	 */
	public static Number cbrt(Number x) {
		return pow(x, 1.0 / 3);
	}
	
	/**
	 * Performs a ceiling operation on a number. For complex values, the ceiling operation is performed
	 * on both the real and complex parts of the value.
	 *
	 * @param x - a number.
	 * @return the ceiling value of the number (possibly a {@link ComplexNumber}).
	 */
	public static Number ceil(Number x) {
		if (x instanceof ComplexNumber) {
			ComplexNumber cn = (ComplexNumber) x;
			return new ComplexNumber(Math.ceil(cn.a), Math.ceil(cn.b));
		}
		
		return Math.ceil(x.doubleValue());
	}
	
	/**
	 * Takes the cosine of a number. The cosine of a number is obtained by the following:
	 * <code>cos(a+bi) = cos(a)cosh(b) - isin(a)sinh(b)</code>. The result is complex if the input
	 * is complex.
	 *
	 * @param x - a number.
	 * @return the cosine of the number (possibly a {@link ComplexNumber}).
	 */
	public static Number cos(Number x) {
		if (x instanceof ComplexNumber) {
			// cos(a+bi) = cos(a)cosh(b) - isin(a)sinh(b)
			ComplexNumber cn = (ComplexNumber) x;
			return new ComplexNumber(Math.cos(cn.a) * Math.cosh(cn.b), -Math.sin(cn.a) * Math.sinh(cn.b));
		}
		
		return Math.cos(x.doubleValue());
	}
	
	/**
	 * Takes the hyperbolic cosine of a number. The result is complex if the input is complex.
	 *
	 * @param x - a number.
	 * @return the hyperbolic cosine of the number (possibly a {@link ComplexNumber}).
	 */
	public static Number cosh(Number x) {
		return divide(add(exp(x), exp(multiply(x, -1))), 2);
	}
	
	/**
	 * Raises Euler's number by a number. The result is complex if the input is complex.
	 *
	 * @param x - a number.
	 * @return Euler's number raised by the number (possibly a {@link ComplexNumber}).
	 */
	public static Number exp(Number x) {
		if (x instanceof ComplexNumber) {
			ComplexNumber cn = (ComplexNumber) x;
			return new ComplexNumber(Math.cos(cn.b), Math.sin(cn.b)).multiply(Math.exp(cn.a));
		}
		
		return Math.exp(x.doubleValue());
	}
	
	/**
	 * Raises Euler's number by a number and subtracts that result by 1. The result is complex if the input
	 * is complex.
	 *
	 * @param x - a number.
	 * @return Euler's number raised by the number and subtracted by 1 (possibly a {@link ComplexNumber}).
	 */
	public static Number expmp1(Number x) {
		return subtract(exp(x), 1);
	}
	
	/**
	 * Performs a flooring operation on a number. For complex values, the flooring operation is performed on both
	 * the real and complex parts of the value.
	 *
	 * @param x - a value.
	 * @return the flooring value of the number (possibly a {@link ComplexNumber}).
	 */
	public static Number floor(Number x) {
		if (x instanceof ComplexNumber) {
			ComplexNumber cn = (ComplexNumber) x;
			return new ComplexNumber(Math.floor(cn.a), Math.floor(cn.b));
		}
		
		return Math.floor(x.doubleValue());
	}
	
	/**
	 * Calculates the hypotenuse given two side values; <code>sqrt(a^2, b^2)</code>. The result is complex
	 * if one of the inputs is complex.
	 *
	 * @param a - the first value.
	 * @param b - the second value.
	 * @return the hypotenuse of the resulting right triangle formed by the two values provided (possibly a
	 * {@link ComplexNumber}).
	 */
	public static Number hypot(Number a, Number b) {
		return sqrt(add(pow(a, 2), pow(b, 2)));
	}
	
	/**
	 * Takes the natural logarithm of a number and is obtained by the following:
	 * <code>log(x) = ln(||x||) + arg(x) * i</code>. The result is complex if the input is complex
	 * or is not in the range of (0, infinity).
	 *
	 * @param x - a number.
	 * @return the natural logarithm of the number (possibly a {@link ComplexNumber}).
	 */
	public static Number log(Number x) {
		if (x instanceof ComplexNumber || x.doubleValue() <= 0) {
			// log(x) = ln(||x||) + arg(x)i
			ComplexNumber cn = (ComplexNumber) x;
			return new ComplexNumber(Math.log(cn.r), cn.theta);
		}
		
		return Math.log(x.doubleValue());
	}
	
	/**
	 * Takes the natural logarithm of the sum of a number and 1. The result is complex if the input is complex or if
	 * the sum of the input and 1 is not in the range of (0, infinity).
	 *
	 * @param x - a number.
	 * @return the natural logarithm of the sum of a number and 1 (possibly a {@link ComplexNumber}).
	 */
	public static Number log1p(Number x) {
		return log(add(x, 1));
	}
	
	/**
	 * Takes the base-2 logarithm of a number. The result is complex if the input is complex or is not in the range
	 * (0, infinity).
	 *
	 * @param x - a number.
	 * @return the base-2 logarithm of a number (possibly a {@link ComplexNumber}).
	 */
	public static Number log2(Number x) {
		return divide(log(x), LOG2);
	}
	
	/**
	 * Takes the base-10 logarithm of a number. The result is complex if the input is complex or is not in the range
	 * (0, infinity).
	 *
	 * @param x - a number.
	 * @return the base-10 logarithm of a number (possibly a {@link ComplexNumber}).
	 */
	public static Number log10(Number x) {
		return divide(log(x), LOG10);
	}
	
	/**
	 * Raises a number by another number.
	 *
	 * @param n     - the base.
	 * @param power - the exponent.
	 * @return the base raised to the power of the exponent (possibly a {@link ComplexNumber}).
	 */
	public static Number pow(Number n, Number power) {
		return new ComplexNumber(n).pow(power);
	}

	/**
	 * Inverts a number.
	 * 
	 * @param n the number to invert.
	 * @return the number inverted.
	 */
	public static Number invert(Number n) {
		if (n instanceof ComplexNumber) {
			return ((ComplexNumber)n).inverse();
		}

		return 1 / n.doubleValue();
	}
	
	/**
	 * @return a random number that can be either real or complex.
	 */
	public static Number random() {
		double a = Math.random();
		double b = Math.random();
		
		if (b == 0) return a;
		return new ComplexNumber(a, b);
	}
	
	/**
	 * Rounds a number. If the value is complex, then both the real and complex parts are rounded.
	 *
	 * @param x - a number.
	 * @return the rounded number (possibly a {@link ComplexNumber}).
	 */
	public static Number round(Number x) {
		if (x instanceof ComplexNumber) {
			ComplexNumber cn = (ComplexNumber) x;
			return new ComplexNumber(Math.round(cn.a), Math.round(cn.b));
		}
		
		return Math.round(x.doubleValue());
	}
	
	/**
	 * Takes the sine of a number and is obtained by the following:
	 * <code>sin(a+bi) = sin(a)cosh(b) - icos(a)sinh(b)</code>. The result is complex if the input is complex.
	 *
	 * @param x - a number.
	 * @return the sine of the number (possibly a {@link ComplexNumber}).
	 */
	public static Number sin(Number x) {
		if (x instanceof ComplexNumber) {
			// sin(a+bi) = sin(a)cosh(b) - icos(a)sinh(b)
			ComplexNumber cn = (ComplexNumber) x;
			return new ComplexNumber(Math.sin(cn.a) * Math.cosh(cn.b), -Math.cos(cn.a) * Math.sinh(cn.b));
		}
		
		return Math.sin(x.doubleValue());
	}
	
	/**
	 * Takes the hyperbolic sine of a number. The result is complex if the input is complex.
	 *
	 * @param x - a number.
	 * @return the hyperbolic sine of a number (possibly a {@link ComplexNumber}).
	 */
	public static Number sinh(Number x) {
		return divide(subtract(exp(x), exp(multiply(x, -1))), 2);
	}
	
	/**
	 * Takes the square root of a number. The result is complex if the input is less than 0.
	 *
	 * @param x - a number.
	 * @return the square root of the number (possibly a {@link ComplexNumber}).
	 */
	public static Number sqrt(Number x) {
		return pow(x, .5);
	}
	
	/**
	 * Takes the tangent of a number. The result is complex if the input is complex.
	 *
	 * @param x - a number.
	 * @return the tangent of the number (possibly a {@link ComplexNumber}).
	 */
	public static Number tan(Number x) {
		return divide(sin(x), cos(x));
	}
	
	/**
	 * Takes the hyperbolic tangent of a number. The result is complex if the input is complex.
	 *
	 * @param x - a number.
	 * @return the hyperbolic tangent of a number (possibly a {@link ComplexNumber}).
	 */
	public static Number tanh(Number x) {
		return divide(sinh(x), cosh(x));
	}
	
	/**
	 * @param n1 - the first number.
	 * @param n2 - the second number.
	 * @return true if the two numbers are equal (complex numbers are equal if both the real and
	 * complex parts are equal).
	 */
	public static boolean equals(Number n1, Number n2) {
		if (n1 instanceof ComplexNumber) return n1.equals(n2);
		else if (n2 instanceof ComplexNumber) return n2.equals(n1);
		return new ComplexNumber(n1).equals(n2);
	}

	/**
	 * Checks if two numbers are coprime.
	 * 
	 * @param a the first number
	 * @param b the second number
	 * @return true if the two numbers are coprime.
	 */
	public static boolean coprimeWith(long a, long b) {
		List<Long> ad = divisors(a);
		List<Long> bd = divisors(b);

		ad.retainAll(bd);
		return ad.size() == 1;
	}

	/**
	 * Gets the factorial of a number.
	 * 
	 * @param l a number.
	 * @return the factorial of a number.
	 * @throws IllegalArgumentException if the number is negative.
	 */
	public static long factorial(long l) throws IllegalArgumentException {
		if (l < 0) {
			throw new IllegalArgumentException("negative value");
		}

		long res = 1;

		for (long i = l; i >= 2; i--) {
			res *= i;
		}

		return res;
	}
	
	/**
	 * Gets the greatest common divisor between two integers.
	 *
	 * @param a - the first number.
	 * @param b - the second number.
	 * @return the greatest common divisor between two numbers.
	 */
	public static Long gcd(long a, long b) {
		long r = a % b;
		return r == 0 ? b : gcd(b, r);
	}

	/**
	 * Gets the least common multiple between two integers.
	 * 
	 * @param a the first number.
	 * @param b the second number.
	 * @return the least common multiply between two integers.
	 */
	public static Long lcm(long a, long b) {
		return a * b / gcd(a, b);
	}
	
	/**
	 * Gets the divisors of an integer.
	 *
	 * @param l - the integer.
	 * @return a list of divisors for this number.
	 */
	public static List<Long> divisors(long l) {
		List<Long> list = new ArrayList<>();
		for (long i = l; i > 0; i--)
			if (l % i == 0) list.add(i);
		Collections.reverse(list);
		return list;
	}
	
	/**
	 * Takes the average of a set of numbers.
	 *
	 * @param values - a set of numbers
	 * @return the average of a set of numbers.
	 */
	public static Number average(Number... values) {
		Number avg = 0;
		
		for (Number n : values) {
			avg = add(avg, n);
		}
		
		return divide(avg, values.length);
	}

	/**
	 * Generates a list of prime numbers in the range [2,n) modeled after the Sieve
	 * of Eratosthenes algorithm.
	 * 
	 * @param n the maximum possible prime number.
	 * @return a list of prime numbers in the range [2,n)
	 */
	public static List<Integer> primes(int n) {
		boolean[] isComposite = new boolean[n];
		List<Integer> primes = new ArrayList<>();
		double limit = Math.sqrt(n);

		for (int i = 2; i <= limit; i++) {
			if (!isComposite[i]) {
				for (int j = i * i; j < n; j += i) {
					isComposite[j] = true;
				}
			}
		}

		for (int i = 2; i < isComposite.length; i++) {
			if (!isComposite[i]) {
				primes.add(i);
			}
		}

		return primes;
	}
	
	/**
	 * Sums up a sequence of numbers; essentially mimics summation functions. Each iteration
	 * is incremented by exactly 1, so calling <code>summation(function, 0, 3)</code> is equivalent
	 * to <code>summation(function, 0, 3.4)</code>. Kahan's Summation Algorithm is utilized to reduce
	 * numerical errors.
	 *
	 * @param function - the function applied to each summation iteration.
	 * @param start    - the starting bound (inclusive).
	 * @param end      - the ending bound (inclusive).
	 * @return the sum of the sequence of numbers provided by a function and two bounds.
	 */
	public static Number summation(DoubleFunction<Number> function, double start, double end) {
		// Kahan Summation Algorithm
		Number sum = 0;
		Number c = 0;
		
		for (double d = start; d <= end; d++) {
			Number y = subtract(function.apply(d), c);
			Number t = add(sum, y);
			c = subtract(subtract(t, sum), y);
			sum = t;
		}
		
		return sum;
	}
	
	/**
	 * Finds the standard deviation amongst a set of numbers. Utilizes the Discrete Random Variable method
	 * (see <a href=https://en.wikipedia.org/wiki/Standard_deviation#Discrete_random_variable>this wikipedia page
	 * for more information</a>)
	 *
	 * @param values - the set of numbers.
	 * @return the standard deviation amongst the set of numbers
	 */
	public static Number stdDeviation(Number... values) {
		// do not use #summation(DoubleFunction, double, double), it's significantly slower
		double inv = 1 / values.length;
		
		Number mu = 0;
		for (Number n : values) {
			mu = add(n, mu);
		}
		mu = multiply(mu, inv);
		
		Number sqrtPart = 0;
		for (Number n : values) {
			sqrtPart = add(sqrtPart, pow(subtract(n, mu), 2));
		}
		sqrtPart = multiply(sqrtPart, inv);
		
		return sqrt(sqrtPart);
	}
}
