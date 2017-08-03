package pvlib.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents an abstract data structure, whose elements can be read, modified,
 * and removed. The structure can be split into smaller sizes.
 * 
 * @author Vishal Patel
 *
 * @param <E>
 *            the data type this structure will hold.
 */
public interface DataStructure<E> extends java.io.Serializable {
	/**
	 * Adds an element to the data structure.
	 * 
	 * @param element - the element to add.
	 */
	void add(E element);

	/**
	 * Gets an element from the data structure at the given index.
	 * 
	 * @param index
	 *            - the location of the element.
	 * @return the element from the structure at the given index.
	 */
	E get(int index);

	/**
	 * Sets the value of a data structure at a given index. If the index is
	 * equal to the size of the data structure, the element is added to the
	 * structure.
	 * 
	 * @param index
	 *            - the index reference to modify.
	 * @param newValue
	 *            - the new value for the data structure's reference at the
	 *            given index.
	 */
	void set(int index, E newValue);

	/**
	 * Copies the elements from another data structure to this structure.
	 * 
	 * @param ds
	 *            - the data structure whose elements are to be copied over.
	 */
	default void set(DataStructure<E> ds) {
		this.clear();

		for (int i = 0; i < ds.size(); i++)
			this.add(ds.get(i));
	}

	/**
	 * Removes an element from the data structure at the given index.
	 * 
	 * @param index
	 *            - the index reference to remove.
	 */
	void remove(int index);

	/**
	 * Removes an element from the data structure if it exists.
	 * 
	 * @param element
	 *            - the element to remove.
	 * @return true if the element was removed.
	 */
	boolean remove(E element);

	/**
	 * Clears the data structure, removing all elements. Default implementation
	 * calls {@link #remove(int)}, removing the first or 0th indexed element,
	 * until there are no more elements left in the structure.
	 */
	default void clear() {
		while (this.size() > 0)
			this.remove(0);
	}

	/**
	 * Swaps to indices in the data structure.
	 * 
	 * @param first
	 *            - the first index.
	 * @param second
	 *            - the second index.s
	 */
	void swap(int first, int second);

	/**
	 * Retrieves the index of an element in the data structure.
	 * 
	 * @param element
	 *            - the element to look for.
	 * @return a nonnegative value representing the index of the element;
	 *         otherwise -1 if the element was not found.
	 */
	int indexOf(E element);

	/**
	 * Checks if the data structure has a specified element. Equivalent to
	 * {@linkplain DataStructure#indexOf(Object) >= 0}.
	 * 
	 * @param element
	 *            - the element to look for.
	 * @return true if the data structure has the specified element.
	 */
	default boolean contains(E element) {
		return indexOf(element) >= 0;
	}

	/**
	 * @return the size of the data structure.
	 */
	int size();

	/**
	 * Checks if the data structure is empty. Equivalent to
	 * {@linkplain DataStructure#size() == 0}.
	 * 
	 * @return true if the data structure is empty.
	 */
	default boolean isEmpty() {
		return this.size() == 0;
	}

	/**
	 * Splits the data structure from a given index up to another index.
	 * 
	 * @param from
	 *            - the starting index.
	 * @param to
	 *            - the ending index (excluded)
	 * @return the sub portion of the data structure on the domain [from, to).
	 */
	DataStructure<E> split(int from, int to);

	/**
	 * Splits the data structure from a given index to the end of the data
	 * structure (inclusive).
	 * 
	 * @param from
	 *            - the starting index.
	 * @return the sub portion of the data structure on the domain [from,
	 *         {@linkplain DataStructure#size()}).
	 */
	default DataStructure<E> split(int from) {
		return this.split(from, this.size());
	}

	/**
	 * Constructs an array copy of the elements inside this data structure,
	 * casting each element to the type defined by <b>T</b>. Not intended to be
	 * overridden.
	 * 
	 * @param arr
	 *            - the initial array for data storage; modified to fit the
	 *            number of elements in the structure.
	 * @return an array of type <b>T</b> holding the elements of this data
	 *         structure
	 * @param <T>
	 *            - the base component type of the array
     * @throws NullPointerException if the given array is null
	 */
	default <T> T[] toArray(T[] arr) {
	    if (arr == null)
	        throw new NullPointerException();
		if (arr.length != this.size())
			arr = java.util.Arrays.copyOf(arr, this.size());

		for (int i = 0; i < arr.length; i++)
			arr[i] = (T) this.get(i);

		return arr;
	}

	/**
	 * Constructs a list copy of type <b>E</b> of the elements inside this data
	 * structure.
	 * 
	 * @return a list of type <b>E</b> holding the elements of this data
	 *         structure.
	 */
	default List<E> asList() {
		List<E> list = new ArrayList(this.size());

		for (int i = 0; i < this.size(); i++)
			list.add(this.get(i));

		return list;
	}

	/**
	 * @return a copy of the data structure.
	 */
	DataStructure<E> clone();

	/**
	 * Checks if the data structures have equal elements in parallel locations.
	 * This implies that for any index in the structure, <i>n</i>,
	 * {@code ds1.get(n).equals(ds2.get(n))} returns true.
	 * 
	 * @param obj
	 *            - the other data structure.
	 * @return true if the structures are equal.
	 */
	boolean equals(Object obj);
	
	/**
	 * Iterates through each element in the data structure, performing a consumer operation
	 * on each element.
	 * @param action - the consumer operation to apply to each element.
	 */
	default void forEach(Consumer<E> action) {
		for (int i = 0; i < this.size(); i++) {
			action.accept(this.get(i));
		}
	}

	/**
	 * Sorts the data structure, modifying its elements and indices, by
	 * utilizing a comparator. If the given comparator is null, then the data
	 * structure's elements are assumed to have implemented the
	 * {@link java.lang.Comparable} interface.
	 * 
	 * @param method
	 *            - the sorting algorithm used.
	 * @param ds
	 *            - the structure to sort.
	 * @param comparator
	 *            - the comparator used in sorting the elements.
	 * 
	 */
	static <T extends Object> void sort(SortMethod method, DataStructure<T> ds, Comparator<T> comparator) {
		if (comparator == null)
			comparator = (o1, o2) -> ((Comparable<T>) o1).compareTo(o2);
		method.apply(ds, comparator);
	}

	/**
	 * Sorts a copy of the data structure, modifying its elements and indices,
	 * by utilizing a comparator. If the given comparator is null, then the data
	 * structure's elements are assumed to have implemented the
	 * {@link java.lang.Comparable} interface.
	 * 
	 * @param method
	 *            - the sorting algorithm used.
	 * @param ds
	 *            - the structure to sort.
	 * @param comparator
	 *            - the comparator used in sorting the elements.
	 * @return a sorted copy of the data structure.
	 */
	static <T extends Object> DataStructure<T> safeSort(SortMethod method, DataStructure<T> ds,
			Comparator<T> comparator) {
		DataStructure<T> clone = ds.clone();
		sort(method, clone, comparator);
		return clone;
	}

	/**
	 * Constructs a data structure from a Collection object.
	 * 
	 * @param collection
	 *            - the Collection object to form the structure with.
	 * @return a data structure holding the contents of the Collection object.
	 */
	static <T> DataStructure<T> fromCollection(Collection<T> collection) {
		return new AbstractStructure<>(collection);
	}

	/**
	 * Constructs a data structure from an array.
	 * 
	 * @param arr
	 *            - the array to form the structure with.
	 * @return a data structure holding the contents of the array.
	 */
	static <T> DataStructure<T> fromArray(T[] arr) {
		AbstractStructure<T> as = new AbstractStructure<>();

		for (int i = 0; i < arr.length; i++)
			as.add(arr[i]);

		return as;
	}
}
