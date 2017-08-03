package pvlib.util;

/**
 * Utility class used for iterations in a data structure, such as matrices.
 * Provides the value and indices at the current iteration.
 *
 * @param <T> - the generic type of the value
 */
public final class ForEachHolder<T> {
	/**
	 * The indices of the current iteration.
	 */
	private int[] indices;
	
	/**
	 * The value of the current iteration.
	 */
	private T data;
	
	/**
	 * Constructs a ForEachHolder given a value and a set of integers for the indices.
	 *
	 * @param data    - the value.
	 * @param indices - the indices.
	 */
	public ForEachHolder(T data, int... indices) {
		this.data = data;
		this.indices = indices;
	}
	
	/**
	 * @return the value of the iteration.
	 */
	public T data() {
		return this.data;
	}
	
	/**
	 * @return null or an empty array of indices (indicating that there's no definitive indexed
	 * iteration); otherwise an non-empty array of integers indicating the indices of the iteration.
	 */
	public int[] indices() {
		return this.indices.clone();
	}
	
	/**
	 * @return {@link #data}.toString()
	 */
	@Override
	public String toString() {
		return this.data.toString();
	}
}
