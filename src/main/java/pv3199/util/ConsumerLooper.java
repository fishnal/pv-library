package pv3199.util;

/**
 * A function that takes in an object and an arbitrary number of integers
 * representing indices.
 */
@FunctionalInterface
public interface ConsumerLooper<T> {
	/**
	 * The void function with an object and arbitrary number of integers
	 * as parameters. The integers serve as indices when looping through
	 * a structure.
	 *
	 * @param t the object value.
	 * @param indices the indices.
	 */
	void accept(T t, int... indices);
}
