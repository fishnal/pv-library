package pv3199.util;

/**
 * A function that takes in a {@link ForEachHolder} instance and returns nothing.
 */
@FunctionalInterface
public interface ConsumerHolder<T> {
	/**
	 * The void function with a {@link ForEachHolder} instance.
	 *
	 * @param forEachHolder the ForEachHolder instance.
	 */
	void accept(ForEachHolder<T> forEachHolder);
}
