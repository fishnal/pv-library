package pv3199.math;

import static pv3199.math.BitOps.*;

/**
 * An unsigned integer represented by an arbitrary number of bits. The underlying
 * structure of the integer is a boolean array whose size represents the number of
 * bits this integer has.
 */
public class BinaryInteger implements Cloneable {
	/**
	 * Adds two bits together.
	 *
	 * @param a the first bit.
	 * @param b the second bit.
	 * @return the sum of two bits, the first element is the carry bit and
	 * the second is the sum bit.
	 */
	private static boolean[] halfAdder(boolean a, boolean b) {
		boolean carry = and(a, b);
		boolean sum = xor(a, b);

		return new boolean[]{ carry, sum };
	}
	
	/**
	 * Adds three bits together.
	 *
	 * @param a the first bit.
	 * @param b the second bit.
	 * @param c the third bit.
	 * @return the sum of three bits, the first element is the carry bit and
	 * the second is the sum bit.
	 */
	private static boolean[] fullAdder(boolean a, boolean b, boolean c) {
		boolean[] ha1 = halfAdder(a, b);
		
		boolean sum1 = ha1[1];
		boolean carry1 = ha1[0];

		boolean[] ha2 = halfAdder(sum1, c);

		boolean sum = ha2[1];
		boolean carry2 = ha2[0];

		boolean carry = or(carry1, carry2);

		return new boolean[]{ carry, sum };
	}
	
	/**
	 * The bits of this integer.
	 */
	private final boolean[] bits;
	
	/**
	 * The number of bits in this integer.
	 */
	private final int length;
	
	/**
	 * Constructs a BinaryInteger from a boolean array of bits. False corresponds
	 * to 0 and true corresponds to 1.
	 *
	 * @param bits the boolean bit array.
	 * @throws IllegalArgumentException if the length of the array is 0.
	 */
	public BinaryInteger(boolean[] bits) throws IllegalArgumentException {
		if (bits.length == 0) {
			throw new IllegalArgumentException("empty bit array");
		}

		this.bits = bits.clone();
		this.length = this.bits.length;
	}
	
	/**
	 * Constructs a BinaryInteger from a string. Character values of
	 * "0" correspond to a 0 bit and "1" to a 1 bit. Any other character
	 * value is considered invalid.
	 *
	 * @param bitString the bit string.
	 * @throws IllegalArgumentException if any character in the string is
	 * not "0" or "1".
	 */
	public BinaryInteger(String bitString) throws IllegalArgumentException {
		if (bitString.isEmpty()) {
			throw new IllegalArgumentException("empty bit string");
		}

		this.bits = new boolean[bitString.length()];
		this.length = this.bits.length;
		
		for (int i = 0; i < this.bits.length; i++) {
			char c = bitString.charAt(i);
			
			if (c == '1') {
				this.bits[i] = true;
			} else if (c != '0') {
				throw new IllegalArgumentException();
			}
		}
	}
	
	/**
	 * Constructs a BinaryInteger from a long value (converts
	 * the long to an unsigned string).
	 *
	 * @param l the long value.
	 */
	public BinaryInteger(long l) {
		this(Long.toUnsignedString(l, 2));
	}
	
	/**
	 * Constructs a BinaryInteger from another BinaryInteger instance,
	 * effectively cloning the original instance.
	 *
	 * @param bi the binary integer instance.
	 */
	public BinaryInteger(BinaryInteger bi) {
		this.bits = bi.bits.clone();
		this.length = bi.length;
	}
	
	/**
	 * Adds this and another BinaryInteger instance together. Note that
	 * the number of bits in the sum will ALWAYS be 1 more than the
	 * greatest number of bits amongst this and the other integer.
	 * This is planned to be optimized in the future.
	 *
	 * @param bi the binary integer instance.
	 * @return the sum of this and another binary integer.
	 */
	public BinaryInteger add(BinaryInteger bi) {
		boolean[] bits1;
		boolean[] bits2;
		int bits;
		
		if (this.length < bi.length) {
			bits1 = new boolean[bits = bi.length];
			bits2 = bi.bits;
			System.arraycopy(this.bits, 0, bits1, bits - this.length, this.length);
		} else if (bi.length < this.length) {
			bits1 = this.bits;
			bits2 = new boolean[bits = this.length];
			System.arraycopy(bi.bits, 0, bits2, bits - bi.length, bi.length);
		} else {
			bits1 = this.bits;
			bits2 = bi.bits;
			bits = this.length;
		}

		boolean[] newBits = new boolean[bits + 1];

		boolean carry = false;

		// for an n-bit adder, you have 1 HA and n-1 FAs
		for (int i = bits - 1; i > -1; i--) {
			boolean a = bits1[i];
			boolean b = bits2[i];
			
			if (i == bits - 1) {
				boolean[] ha = halfAdder(a, b);
				
				newBits[i + 1] = ha[1];
				carry = ha[0];
			} else {
				boolean[] fa = fullAdder(a, b, carry);
				
				newBits[i + 1] = fa[1];
				carry = fa[0];
			}
		}

		newBits[0] = carry;
		
		return new BinaryInteger(newBits);
	}

	/**
	 * Adds a binary integer with a long value.
	 *
	 * @param l - the long
	 * @return the sum of the binary integer and long
	 */
	public BinaryInteger add(long l) {
		return this.add(new BinaryInteger(l));
	}
	
	/**
	 * Clones a BinaryInteger through {@link #BinaryInteger(BinaryInteger)}.
	 *
	 * @return a clone of this binary integer.
	 */
	@Override
	public BinaryInteger clone() {
		return new BinaryInteger(this);
	}
	
	/**
	 * The string representation of the bits. Trailing zeroes are removed
	 * from the returned string.
	 *
	 * @return the binary string of this binary integer.
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		for (boolean b : this.bits) {
			sb.append(b ? 1 : 0);
		}
		
		String s = sb.toString();
		
		if (s.startsWith("0")) {
			int nextOne = s.indexOf('1');
			
			if (nextOne == -1) {
				return "0";
			} else {
				return s.substring(nextOne);
			}
		}
		
		return s;
	}
}
