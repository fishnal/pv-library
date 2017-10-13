package pv3199.math;


/**
 * Bit-wise operations.
 */
public class BitOps {
	/**
	 * AND logical operation.
	 *
	 * @param a first bit.
	 * @param b second bit.
	 * @return a AND b.
	 */
	public static boolean and(boolean a, boolean b) {
		return a & b;
	}
	
	/**
	 * OR logical operation.
	 *
	 * @param a first bit.
	 * @param b second bit.
	 * @return a OR b.
	 */
	public static boolean or(boolean a, boolean b) {
		return a | b;
	}
	
	/**
	 * NOT logical operation.
	 *
	 * @param a a bit.
	 * @return NOT a.
	 */
	public static boolean not(boolean a) {
		return !a;
	}
	
	/**
	 * NAND logical operation.
	 *
	 * @param a first bit.
	 * @param b second bit.
	 * @return (a NAND b) or (NOT (a AND b))
	 */
	public static boolean nand(boolean a, boolean b) {
		return not(and(a, b));
	}
	
	/**
	 * NOR logical operation.
	 *
	 * @param a first bit.
	 * @param b second bit.
	 * @return (a NOR b) or (NOT (a OR b))
	 */
	public static boolean nor(boolean a, boolean b) {
		return not(or(a, b));
	}
	
	/**
	 * XAND logical operation.
	 *
	 * @param a first bit.
	 * @param b second bit.
	 * @return a XAND b
	 */
	public static boolean xand(boolean a, boolean b) {
		return a == b;
	}
	
	/**
	 * XOR logical operation.
	 *
	 * @param a first bit.
	 * @param b second bit.
	 * @return a XOR b
	 */
	public static boolean xor(boolean a, boolean b) {
		return a & !b | !a & b;
	}
}