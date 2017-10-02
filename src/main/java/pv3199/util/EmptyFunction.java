package pv3199.util;

/**
 * A function that takes no arguments and returns nothing.
 */
@FunctionalInterface
public interface EmptyFunction {
	/**
	 * The default empty function. Literally does nothing.
	 */
	EmptyFunction DEFAULT = () -> {};
	
	/**
	 * Invokes a function that has no arguments and is void.
	 */
	void call();
}
