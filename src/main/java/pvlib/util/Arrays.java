package pvlib.util;

import java.lang.reflect.Array;

import java.util.*;
import java.util.function.UnaryOperator;

/**
 * Utilities for arrays, such as casting multi-dimensional arrays, sorting
 * multi-dimensional arrays, finding minimum and maximum values, and cloning
 * arrays.
 * 
 * @author Vishal Patel
 *
 */
public final class Arrays {
	private final static Random RANDOM = new Random();

	public enum RandomPrimitive {
		BOOLEAN((Boolean b) -> RANDOM.nextBoolean()),
		BYTE((Byte b) -> (byte) (RANDOM.nextInt(256) - 128)),
		CHAR((Character c) -> (char) (RANDOM.nextInt(Character.MAX_VALUE + 1))),
		SHORT((Short s) -> (short) (RANDOM.nextInt(65536) - 32768)),
		INT((Integer i) -> RANDOM.nextInt()),
		LONG((Long l) -> RANDOM.nextLong()),
		FLOAT((Float f) -> RANDOM.nextFloat()),
		DOUBLE((Double d) -> RANDOM.nextDouble());

		private UnaryOperator<?> generator;

		RandomPrimitive(UnaryOperator<?> generator) {
			this.generator = generator;
		}

		public Object generate() {
			return this.generator.apply(null);
		}
	}

	private Arrays() {
	}

	/**
	 * Gets the element located at the indices in some dimensional array. If
	 * <code>get(array, new int[]{1, 2, 3})</code> is called, then it
	 * returns the equivalent of <code>array[1][2][3]</code>.
	 * 
	 * @param array
	 *            - the array.
	 * @param indices
	 *            - the indices for each dimension.
	 * @return the element located at these indices in the array.
	 * @throws NullPointerException
	 *             if any of the arguments are null
	 * @throws IllegalArgumentException
	 *             if the array is not actually an array
	 * @throws ArrayIndexOutOfBoundsException
	 *             if one of the indices is out of bounds for this array.
	 * @throws NullPointerException
	 *             if either the array or indices parameter is null.
	 * @throws IllegalArgumentException
	 *             if the array parameter is not actually an array.
	 */
	public static Object get(Object array, int[] indices)
			throws NullPointerException, IllegalArgumentException, ArrayIndexOutOfBoundsException {
		if (array == null || indices == null)
			throw new NullPointerException();
		if (!array.getClass().isArray())
			throw new IllegalArgumentException("must pass in an actual array");

		for (int i : indices)
			array = Array.get(array, i);

		return array;
	}

	/**
	 * Gets the element located at the indices in some dimensional array. If
	 * <code>get(array, iterator)</code> is called and <code>array</code> is a
	 * valid array and <code>iterator</code> is a valid Iterator consisting of
	 * the Integer objects 1, 2, and 3 in that order, then it virtually returns
	 * <code>array[1][2][3]</code>.
	 * 
	 * @param array
	 *            - the array.
	 * @param indices
	 *            - the indices for each dimension.
	 * @return the element located at these indices in the array.
	 * @throws NullPointerException
	 *             if any of the arguments are null
	 * @throws IllegalArgumentException
	 *             if the array is not actually an array
	 * @throws ArrayIndexOutOfBoundsException
	 *             if one of the indices is out of bounds for this array.
	 */
	public static Object get(Object array, Iterator<Integer> indices)
			throws NullPointerException, IllegalArgumentException, ArrayIndexOutOfBoundsException {
		if (array == null)
			throw new NullPointerException();
		if (indices == null)
			throw new NullPointerException();
		if (!array.getClass().isArray())
			throw new IllegalArgumentException("must pass in an actual array");

		while (indices.hasNext()) {
			int i = indices.next();
			array = Array.get(array, i);
		}

		return array;
	}

	/**
	 * Sets an element in an array to a new value. If
	 * <code>set(array, new int[]{1, 2, 3}, null)</code> is called and
	 * <code>array</code> is a valid array, then the method virtually executes
	 * <code>array[1][2][3] = null</code>.
	 * 
	 * @param array
	 *            - the array.
	 * @param indices
	 *            - the indices tracing to the array index to change.
	 * @param newValue
	 *            - the new value of the element.
	 * @throws NullPointerException
	 *             if the array or indices parameter is null
	 * @throws IllegalArgumentException
	 *             if the array is not actually an array or if the indices
	 *             attempt to access an array when the
	 * @throws ArrayIndexOutOfBoundsException
	 *             if one of the indices is out of bounds for this array.
	 */
	public static void set(Object array, int[] indices, Object newValue)
			throws NullPointerException, IllegalArgumentException, ArrayIndexOutOfBoundsException {
		if (array == null)
			throw new NullPointerException();
		if (indices == null)
			throw new NullPointerException();
		if (!array.getClass().isArray())
			throw new IllegalArgumentException("must pass in an actual array");

		for (int i = 0; i < indices.length; i++) {
			if (i == indices.length - 1)
				Array.set(array, i, newValue);
			else
				array = Array.get(array, i);
		}
	}

	/**
	 * Sets an element in an array to a new value. If
	 * <code>set(array, iterator, null)</code> is called and <code>array</code>
	 * is a valid array and <code>iterator</code> is a valid Iterator consisting
	 * of the Integer objects 1, 2, and 3 in that order, then the method
	 * virtually executes <code>array[1][2][3] = null</code>.
	 * 
	 * @param array
	 *            - the array.
	 * @param indices
	 *            - the indices tracing to the array index to change.
	 * @param newValue
	 *            - the new value of the element.
	 * @throws NullPointerException
	 *             if the array or indices are null
	 * @throws IllegalArgumentException
	 *             if the array is not actually an array
	 * @throws ArrayIndexOutOfBoundsException
	 *             if one of the indices is out of bounds for this array.
	 */
	public static void set(Object array, Iterator<Integer> indices, Object newValue)
			throws NullPointerException, IllegalArgumentException, ArrayIndexOutOfBoundsException {
		if (array == null)
			throw new NullPointerException();
		if (indices == null)
			throw new NullPointerException();
		if (!array.getClass().isArray())
			throw new IllegalArgumentException("must pass in an actual array");

		while (indices.hasNext()) {
			int i = indices.next();
			if (!indices.hasNext())
				Array.set(array, i, newValue);
			else
				array = Array.get(array, i);
		}
	}

	public static Object randFillArray(Object array, RandomPrimitive generator) {
		if (array == null || generator == null)
			throw new NullPointerException();
		if (!array.getClass().isArray())
			throw new IllegalArgumentException("must pass in an actual array");
		return randFillArray0(array, generator, Classes.getDeepestComponent(array.getClass()));
	}

	private static Object randFillArray0(Object array, RandomPrimitive generator, Class<?> componentType) {
		if (array == null)
			return null;

		try {
			int length = Array.getLength(array);
			for (int i = 0; i < length; i++) {
				Array.set(array, i, randFillArray0(Array.get(array, i), generator, componentType));
			}
			return array;
		} catch (IllegalArgumentException iae) {
			// actual component element of array
			Object generated = generator.generate();
			if (!Classes.canCast(componentType, generated.getClass()))
				throw new ClassCastException(String.format("cannot convert %s to %s", generated.getClass(), componentType));
			return Classes.objectToObjectCast(generated, componentType);
		}
	}

	/**
	 * Combines multiple arrays into one array.
	 * 
	 * @param arrays
	 *            - the arrays to combine.
	 * @return the combined array otherwise
	 * @throws NullPointerException
	 *             if the arrays parameter is null.
	 */
	public static <T> T[] joinArrays(final T[]... arrays) throws NullPointerException {
		if (arrays == null)
			throw new NullPointerException();
		int len = 0;

		for (T[] arr : arrays)
			len += arr.length;

		Object combined = Array.newInstance(Classes.getDeepestComponent(arrays.getClass()), len);

		int ind = 0;

        for (T[] array : arrays) {
            int arrLen = Array.getLength(array);

            for (int j = 0; j < arrLen; j++, ind++)
                Array.set(combined, ind, array[j]);
        }

		return (T[]) combined;
	}

	/**
	 * Finds the location of an element in any dimensional array through a
	 * recursive-linear approach. The deepest elements which represent the root
	 * 
	 * @param array
	 *            - the array to search through.
	 * @param element
	 *            - the element to look for.
	 * @return null if the element could not be found; otherwise an int array
	 *         containing the dimension-indices for the array to lead to the
	 *         element.
	 * @throws NullPointerException
	 *             if the array parameter is null
	 * @throws IllegalArgumentException
	 *             if the array parameter is not an actual array or if the
	 *             component type of array is not the same as the type of the
	 *             element.
	 */
	public static int[] indexOf(final Object array, final Object element)
			throws NullPointerException, IllegalArgumentException {
		if (array == null)
			throw new NullPointerException();
		if (!array.getClass().isArray())
			throw new IllegalArgumentException("must pass in an actual array");

		Object o = indexOf0(array, element);

		if (o == null)
			return null;

		List<Integer> list = (List<Integer>) o;
		Collections.reverse(list);

		int[] inds = new int[list.size()];

		for (int i = 0; i < list.size(); i++)
			inds[i] = list.get(i);

		return inds;
	}

	/**
	 * Finds the location of an element in any given dimensional array.
	 *
	 * @param array
	 *            - the array to search through.
	 * @param element
	 *            - the element to look for.
	 * @return null if either of the parameters are null or if the element could
	 *         not be found; otherwise an integer list containing the indices
	 *         for each of the array's dimensions that lead to the element in
	 *         reversed order (use ListUtil.reverse(List<?>) to fix the
	 *         ordering).
	 * @throws IllegalArgumentException
	 *             if the component type of array is not the same as the type of
	 *             the element.
	 */
	private static List<Integer> indexOf0(final Object array, final Object element) throws IllegalArgumentException {
		if (array == null)
			if (element == null)
				return new ArrayList<>();
			else
				return null;
		if (Classes.componentCheck(array.getClass(), element.getClass())) {
			if (array.equals(element))
				return new ArrayList<>();
		}

		if (array.getClass().isArray()) {
			int length = Array.getLength(array);
			List<Integer> traces;

			for (int i = 0; i < length; i++) {
				List<Integer> otherTraces = indexOf0(Array.get(array, i), element);
				if (otherTraces != null) {
					traces = new ArrayList<>(otherTraces);
					traces.add(i);
					return traces;
				}
			}
		}

		return null;

		// TODO NEEDS TO BE REVIEWED

		// Class<?> componentType =
		// Classes.getDeepestComponent(array.getClass());
		// if (componentType.isPrimitive())
		// componentType = Classes.primitiveClassToWrapperClass(componentType);
		// if (componentType != element.getClass())
		// throw new IllegalArgumentException("component type of array must
		// match type of element");
		//
		// try {
		// int length = Array.getLength(array);
		// List<Integer> traces = null;
		//
		// for (int i = 0; i < length; i++) {
		// List<Integer> otherTraces = indexOf0(Array.get(array, i), element);
		// if (otherTraces != null) {
		// traces = new ArrayList<Integer>(otherTraces);
		// traces.add(i);
		// return traces;
		// }
		// }
		//
		// return null;
		// } catch (IllegalArgumentException iae) {
		// if (element == null)
		// return new ArrayList<Integer>();
		// if (array.equals(element))
		// return new ArrayList<Integer>();
		// return null;
		// }
	}

	/**
	 * Finds the last set of indices of a certain element in a given dimensional
	 * array.
	 *
	 * @param array
	 *            - the array to look through.
	 * @param element
	 *            - the element to find.
	 *
	 * @return null if the array is null, is not an array, or if the element
	 *         could not be found; otherwise an int array listing the indices at
	 *         which the element is located in each dimension.
	 * @throws NullPointerException
	 *             if the array parameter is null.
	 * @throws IllegalArgumentException
	 *             if the array parameter is not an actual array.
	 */
	public static int[] lastIndexOf(final Object array, final Object element)
			throws NullPointerException, IllegalArgumentException {
		if (array == null)
			throw new NullPointerException();
		if (!array.getClass().isArray())
			throw new IllegalArgumentException("must pass in an actual array");

		List<Integer> indices = lastIndexOf0(array, element);

		if (indices == null)
			return null;

		Collections.reverse(indices);

		int[] inds = new int[indices.size()];

		for (int i = 0; i < indices.size(); i++)
			inds[i] = indices.get(i);

		return inds;
	}

	/**
	 * Finds the last location of an element in any given dimensional array.
	 *
	 * @param array
	 *            - the array to search through.
	 * @param element
	 *            - the element to look for.
	 * @return null if either of the parameters are null or if the element could
	 *         not be found; otherwise an integer list containing the indices
	 *         for each of the array's dimensions that lead to the element in
	 *         reversed order (use ListUtil.reverse(List<?>) to fix the
	 *         ordering).
	 * @throws IllegalArgumentException
	 *             if the component type of array is not the same as the type of
	 *             the element.
	 */
	private static List<Integer> lastIndexOf0(final Object array, final Object element) {
		if (array == null)
			if (element == null)
				return new ArrayList<>();
			else
				return null;
		if (Classes.componentCheck(array.getClass(), element.getClass())) {
			if (array.equals(element))
				return new ArrayList<>();
		}

		if (array.getClass().isArray()) {
			int length = Array.getLength(array);
			List<Integer> traces;

			for (int i = length - 1; i > -1; i--) {
				List<Integer> otherTraces = indexOf0(Array.get(array, i), element);
				if (otherTraces != null) {
					traces = new ArrayList<>(otherTraces);
					traces.add(i);
					return traces;
				}
			}
		}

		return null;
	}

	/**
	 * Checks whether an array contains an element.
	 * 
	 * @param array
	 *            - the array to search through.
	 * @param element
	 *            - the element to look for.
	 * @return false if an IllegalArgumentException is caught or if the element
	 *         is not found in the array; true otherwise.
	 * @throws NullPointerException
	 *             if the array parameter is null
	 * @throws IllegalArgumentException
	 *             if the array parameter is not an actual array.
	 */
	public static boolean contains(final Object array, final Object element)
			throws NullPointerException, IllegalArgumentException {
		if (array == null)
			throw new NullPointerException();
		if (!array.getClass().isArray())
			throw new IllegalArgumentException("must pass in an actual array");

		try {
			return indexOf(array, element) != null;
		} catch (IllegalArgumentException iae) {
			return false;
		}
	}

	public static <T> Object max(final Object array) throws ClassCastException, IllegalArgumentException {
		if (array.getClass().isArray()) {
			if (Comparable.class.isAssignableFrom(Classes.getDeepestComponent(array.getClass())))
				return Arrays.<T>maxComparable(array, null);
			throw new IllegalArgumentException("could not be compared; consider using <T> pvlib.util.Arrays#get(Object, Comparator<T>)");
		}

		return null;
	}

	public static <T> Object max(final Object array, final Comparator<T> comparator) throws ClassCastException {
		if (array != null && array.getClass().isArray()) {
			if (comparator == null)
				return Arrays.<T>max(array);
			return Arrays.maxComparator(array, null, comparator);
		}

		return null;
	}

	private static <T> Object maxComparable(Object array, T max) throws ClassCastException {
		try {
			int len = Array.getLength(array);

			for (int i = 0; i < len; i++) {
				Object arr = Array.get(array, i);
				max = (T) Arrays.maxComparable(arr, max);
			}

			return max;
		} catch (IllegalArgumentException iae) {
			if (max == null && array != null)
				return array;
			if (((Comparable<T>) array).compareTo(max) > 0)
				return array;
			return max;
		}
	}

	private static <T> Object maxComparator(Object array, T max, final Comparator<T> comparator)
			throws ClassCastException {
		try {
			int len = Array.getLength(array);

			for (int i = 0; i < len; i++) {
				Object arr = Array.get(array, i);
				max = (T) Arrays.maxComparator(arr, max, comparator);
			}

			return max;
		} catch (IllegalArgumentException iae) {
			if (max == null && array != null)
				return array;
			if (comparator.compare((T) array, max) > 0)
				return array;
			return max;
		}
	}

	public static <T> Object min(final Object array) throws ClassCastException {
		if (array != null && array.getClass().isArray()) {
			if (Comparable.class.isAssignableFrom(Classes.getDeepestComponent(array.getClass())))
				return Arrays.<T>minComparable(array, null);
			throw new IllegalArgumentException("could not be compared; consider using <T> pvlib.util.Arrays#get(Object, Comparator<T>)");
		}

		return null;
	}

	public static <T> Object min(final Object array, final Comparator<T> comparator) throws ClassCastException {
		if (array != null && array.getClass().isArray()) {
			if (comparator == null)
				return min(array);
			return minComparator(array, null, comparator);
		}

		return null;
	}

	private static <T> Object minComparable(Object array, T min) {
		try {
			int len = Array.getLength(array);

			for (int i = 0; i < len; i++) {
				Object arr = Array.get(array, i);
				min = (T) Arrays.minComparable(arr, min);
			}

			return min;
		} catch (IllegalArgumentException iae) {
			if (min == null && array != null)
				return array;
			if (((Comparable<T>) array).compareTo(min) < 0)
				return array;
			return min;
		}
	}

	private static <T> Object minComparator(Object array, T min, final Comparator<T> comparator) {
		try {
			int len = Array.getLength(array);

			for (int i = 0; i < len; i++) {
				Object arr = Array.get(array, i);
				min = (T) Arrays.minComparator(arr, min, comparator);
			}

			return min;
		} catch (IllegalArgumentException iae) {
			if (min == null && array != null)
				return array;
			if (comparator.compare((T) array, min) < 0)
				return array;
			return min;
		}
	}

	/**
	 * Sorts an array in ascending order with an undefined amount of dimensions
	 * based on either the Comparator implemented with the array's component
	 * type or otherwise the hash code of the elements. Uses the selection sort
	 * algorithm resulting in <code>O(n)</code> for best case and
	 * <code>O(n<sup>2</sup>)</code> for worst case.
	 * 
	 * @param array
	 *            - the array to sort.
	 * @return null if the array is null or if the given array is null;
	 *         otherwise the sorted array in ascending order.
	 */
	@Deprecated
	public static <T> Object sort(final Object array) {
		if (array != null && array.getClass().isArray()) {
			if (Comparable.class.isAssignableFrom(Classes.getDeepestComponent(array.getClass())))
				return Sorter.<T>sort(array, null, Sorter.CompareMethod.COMPARABLE, false);
			return Sorter.<T>sort(array, null, Sorter.CompareMethod.HASHCODE, false);
		}

		return null;
	}

	/**
	 * Sorts an array in ascending order with an undefined amount of dimensions
	 * given a comparator. If the comparator is null then elements are compared
	 * based on their {@link Classes#naturalComparator() natural ordering}. Uses
	 * the selection sort algorithm resulting in <code>O(n)</code> for best case
	 * and <code>O(n<sup>2</sup>)</code> for worst case.
	 * 
	 * @param array
	 *            - the array to sort.
	 * @param comparator
	 *            - the comparator to use in comparisons.
	 * @return null if the array is null or if the given array is null;
	 *         otherwise the sorted array in ascending order.
	 * @see Classes#naturalComparator()
	 */
	@Deprecated
	public static <T> Object sort(final Object array, final Comparator<T> comparator) {
		if (array != null && array.getClass().isArray()) {
			if (comparator == null)
				return Sorter.sort(array, Classes.naturalComparator(),
						Sorter.CompareMethod.COMPARATOR, false);
			return Sorter.sort(array, comparator, Sorter.CompareMethod.COMPARATOR, false);
		}

		return null;
	}

	/**
	 * Sorts an array in descending order with an undefined amount of dimensions
	 * based on either the Comparator implemented with the array's component
	 * type or otherwise the hash code of the elements.
	 * 
	 * @param array
	 *            - the array to sort.
	 * @return null if the array is null or if the given array is null;
	 *         otherwise the sorted array in descending order.
	 */
	@Deprecated
	public static <T> Object reverseSort(final Object array) {
		if (array != null && array.getClass().isArray()) {
			if (Comparable.class.isAssignableFrom(Classes.getDeepestComponent(array.getClass())))
				return Sorter.<T>sort(array, null, Sorter.CompareMethod.COMPARABLE, true);
			return Sorter.<T>sort(array, null, Sorter.CompareMethod.HASHCODE, true);
		}

		return null;
	}

	/**
	 * Sorts an array in descending order with an undefined amount of dimensions
	 * given a comparator. If the comparator is null, then elements are compared
	 * based on their {@link Classes#naturalComparator() natural ordering}.
	 * 
	 * @param array
	 *            - the array to sort.
	 * @param comparator
	 *            - the comparator to use in comparisons.
	 * @return null if the array is null or if the given array is null;
	 *         otherwise the sorted array in descending order.
	 * @see Classes#naturalComparator()
	 */
	@Deprecated
	public static <T> Object reverseSort(final Object array, final Comparator<T> comparator) {
		if (array != null && array.getClass().isArray()) {
			if (comparator == null)
				return Sorter.sort(array, Classes.naturalComparator(),
						Sorter.CompareMethod.COMPARATOR, true);
			return Sorter.sort(array, comparator, Sorter.CompareMethod.COMPARATOR, true);
		}

		return null;
	}

	/**
	 * Helper methods for sorting an array in ascending or descending order.
	 * 
	 * @author Vishal Patel
	 *
	 */
	@Deprecated
	private final static class Sorter {
		/**
		 * Types of methods that can be used to compare the elements.
		 * 
		 * @author Vishal Patel
		 *
		 */
		enum CompareMethod {
			/**
			 * Compares elements by casting the first to a Comparable type and
			 * then invoking {@link java.lang.Comparable#compareTo(Object)},
			 * passing the second in as a parameter.
			 */
			COMPARABLE,

			/**
			 * Compares elements using their hash codes. An object whose hash
			 * code is larger than another object's is considered greater than
			 * the other object, one with a hash code smaller than another's is
			 * considered smaller, and one with an equal hash code to another's
			 * is considered equal.
			 */
			@Deprecated
			HASHCODE,

			/**
			 * Compares elements using
			 * {@link java.util.Comparator#compare(Object, Object)}.
			 */
			COMPARATOR
		}

		/**
		 * Current minimum value in the original array
		 */
		private static Object key;

		/**
		 * Index trace for sort methods
		 */
		private static List<Integer> sortTrace = new ArrayList<>();

		/**
		 * Index trace for trace methods
		 */
		private static List<Integer> finderTrace = new ArrayList<>();

		/**
		 * Index trace for minimum value
		 */
		private static List<Integer> keyTrace = new ArrayList<>();

		/**
		 * Sorts an array in ascending order using a certain method.
		 * 
		 * @param array
		 *            - the array to sort.
		 * @param comparator
		 *            - the comparator to use if the method is to sort the
		 *            elements based on a comparator; otherwise ignored
		 * @param method
		 *            - the method to sort the array by;
		 *            <ul>
		 *            <li>0 - Comparable casting</li>
		 *            <li>1 - Hash Code comparisons</li>
		 *            <li>2 - Comparator invocation</li>
		 *            </ul>
		 * @return null if the method given was not supported or did not exist;
		 *         otherwise the sorted array in ascending order.
		 */
		static <T> Object sort(Object array, final Comparator<T> comparator, final CompareMethod method,
				boolean descending) {
			key = null;
			sortTrace.clear();
			finderTrace.clear();
			keyTrace.clear();

			Object newArray = Sorter.sort0(array, null, method, comparator, descending);

			key = null;
			sortTrace.clear();
			finderTrace.clear();
			keyTrace.clear();
			return newArray;
		}

		/**
		 * Sorts an array into ascending order using recursion and selection
		 * sort utilizing the {@link java.lang.Comparable} interface.
		 * 
		 * @param array
		 *            - the array to sort (only used for cloning on this
		 *            method's first stack, afterwards overridden to represent
		 *            some element in the cloned array).
		 * @param newArray
		 *            - the cloned array that will be sorted; must be null for
		 *            proper sorting
		 * @param method
		 *            - the method used to compare the elements; only used in
		 *            {@link #findKey(Object, CompareMethod, Comparator, boolean)}
		 * @param comparator
		 *            - the comparator used if the method is to compare the
		 *            elements using a Comparator instance; ignored otherwise.
		 * @param descending
		 *            - whether the array is being sorted in descending order or
		 *            not.
		 * @return the sorted array.
		 */
		private static <T> Object sort0(Object array, Object newArray, final CompareMethod method,
				final Comparator<T> comparator, final boolean descending) {
			if (newArray == null) {
				newArray = Arrays.clone(array);
				array = newArray;
			}

			int len = Array.getLength(array);

			if (!array.getClass().getComponentType().isArray()) {
				for (int i = 0; i < len; i++) {
					sortTrace.add(i);

					// update min to the minimum of the elements past this index
					key = null;
					keyTrace.clear();
					Sorter.findKey(newArray, method, comparator, descending);

					// swap the elements
					Object temp = Array.get(array, i);
					Arrays.set(newArray, sortTrace.iterator(), key);
					Arrays.set(newArray, keyTrace.iterator(), temp);

					sortTrace.remove(sortTrace.size() - 1);
				}
			} else {
				for (int i = 0; i < len; i++) {
					Object newArr = Array.get(newArray, i);
					sortTrace.add(i);
					Sorter.sort0(newArr, newArray, method, comparator, descending);
					sortTrace.remove(sortTrace.size() - 1);
				}
			}

			return newArray;
		}

		/**
		 * Finds the minimum or maximum value in an array after the indices
		 * specified by {@link #sortTrace} depending on whether the array is
		 * being sorted in ascending or descending order.
		 * 
		 * @param array
		 *            - the array to look through.
		 * @param method
		 *            - the method used to compare the elements.
		 * @param comparator
		 *            - the comparator used if the method compares the elements
		 *            using a comparator instance.
		 * @param descending
		 *            - whether the array is being sorted in descending order or
		 *            not.
		 */
		private static <T> void findKey(Object array, final CompareMethod method, final Comparator<T> comparator,
				final boolean descending) {
			try {
				int len = Array.getLength(array);

				for (int i = 0; i < len; i++) {
					Object arr = Array.get(array, i);
					finderTrace.add(i);
					Sorter.findKey(arr, method, comparator, descending);
					finderTrace.remove(finderTrace.size() - 1);
				}
			} catch (IllegalArgumentException iae) {
				if (finderTrace.size() == sortTrace.size()) {
					// verify that this bottom-most element is after
					// the sortTrace

					for (int i = 0; i < sortTrace.size(); i++) {
						int fte = finderTrace.get(i);
						int ste = sortTrace.get(i);

						if (fte < ste)
							return;
						else if (fte > ste)
							break;
					}

					boolean changed = false;

					if (key == null)
						changed = true;
					else if (method == CompareMethod.COMPARABLE) {
						Comparable<T> array_T = (Comparable<T>) array;
						T key_T = (T) key;
						if ((!descending && array_T.compareTo(key_T) < 0)
								|| (descending && array_T.compareTo(key_T) > 0))
							changed = true;
					} else if (method == CompareMethod.HASHCODE) {
						if ((!descending && array.hashCode() < key.hashCode())
								|| (descending && array.hashCode() > key.hashCode()))
							changed = true;
					} else if (method == CompareMethod.COMPARATOR) {
						T array_T = (T) array;
						T key_T = (T) key;

						if ((!descending && comparator.compare(array_T, key_T) < 0)
								|| (descending && comparator.compare(array_T, key_T) > 0))
							changed = true;
					}

					if (changed) {
						key = array;
						keyTrace.clear();
						keyTrace.addAll(finderTrace);
					}
				}
			}
		}
	}

	/**
	 * Casts an array of one type to another type of array, including primitive
	 * to wrapper and wrapper to primitive casts.
	 * 
	 * @param source
	 *            - the array to cast.
	 * @param newType
	 *            - the new data type that the array will be casted to.
	 * 
	 * @return null if the array cannot be casted; otherwise the casted array.
	 * @throws NullPointerException
	 *             if the array parameter is null.
	 * @throws IllegalArgumentException
	 *             if the array parameter is not an actual array.
	 */
	public static <T> T castArray(Object source, Class<T> newType) {
		if (source == null)
			throw new NullPointerException("source array is null");
		if (newType == null)
			throw new NullPointerException("new class type is null");
		if (!source.getClass().isArray())
			throw new IllegalArgumentException("must pass an actual array");

		Class<?> at = Classes.getDeepestComponent(source.getClass());
		Class<?> nt = Classes.getDeepestComponent(newType);

		// if at is prim/wrap and nt is prim/wrap, make sure that
		// if at is boolean and nt is not boolean or vice-versa
		// then the cast is not possible

		if ((at.isPrimitive() || Classes.isWrapper(at)) && (nt.isPrimitive() || Classes.isWrapper(nt))) {
			Class<?> primAT = Classes.wrapperClassToPrimitiveClass(at);
			Class<?> primNT = Classes.wrapperClassToPrimitiveClass(nt);

			if (primAT == boolean.class) {
				if (primNT != boolean.class)
					return null;
			}
		}

		return (T) copyElements(source, nt);
	}

	/**
	 * Copies the elements from one array object into another array object,
	 * casting all elements from the original array object into the new array
	 * object. If no data type is specified (as in null is passed through), then
	 * the elements from the array are just simply copied over.
	 * 
	 * @param source
	 *            - the array object to copy elements from.
	 * @param newdDeepComponentType
	 *            - the data type to cast the elements from source into.
	 * @return null if the original array is null; the new array object with the
	 *         elements from <code>source</code> copied (and possibly casted).
	 * @throws ArrayIndexOutOfBoundsException
	 *             if an attempt to access data from the source array throws an
	 *             exception
	 * @throws ClassCastException
	 *             if an element from source cannot be casted into the data type
	 *             specified by <code>newType</code>
	 */
	private static Object copyElements(Object source, Class<?> newdDeepComponentType)
			throws ArrayIndexOutOfBoundsException, ClassCastException {
		if (source == null)
			return null;

		try {
			int length = Array.getLength(source);
			int[] dimensions = getDimensionSizes(source);
			Object newArray = Array.newInstance(newdDeepComponentType, dimensions);

			for (int i = 0; i < length; i++) {
				// this is part of the array to iterate through
				Object element = copyElements(Array.get(source, i), newdDeepComponentType);
				Array.set(newArray, i, element);
			}

			return newArray;
		} catch (IllegalArgumentException iae) {
			// this is an actual component element and not an array
			if (!Classes.canCast(source.getClass(), newdDeepComponentType))
				throw new ClassCastException();
            return Classes.objectToObjectCast(source, newdDeepComponentType);
		}
	}

	/**
	 * Gets the dimension sizes of an array object.
	 * 
	 * @param array
	 *            - the array object to retrieve the dimension sizes from.
	 * @return an int array of the array's dimension sizes.
	 */
	public static int[] getDimensionSizes(Object array) {
		int dimCt = 0;

		Class<?> clazz = array.getClass();

		while (clazz.isArray()) {
			dimCt++;
			clazz = clazz.getComponentType();
		}

		int[] dims = new int[dimCt];

		Object pointer = array;

		for (int i = 0; i < dimCt; i++) {
			dims[i] = Array.getLength(pointer);
			int j = 0;
			if (Array.getLength(pointer) == 0)
				break;
			while (Array.get(pointer, j).getClass().isArray() && Array.getLength(Array.get(pointer, j)) == 0
					&& j < Array.getLength(pointer))
				j++;
			if (j == Array.getLength(pointer))
				break;
			pointer = Array.get(pointer, j);
		}

		return dims;
	}

	/**
	 * Clones an array such that <code>a == b</code> returns true, where
	 * <code>a</code> is a bottom-most element in the original array and
	 * <code>b</code> is a bottom-most element in the same position as in the
	 * original array but referenced from the cloned array; however,
	 * <code>c == d</code> returns false, where <code>c</code> is not a
	 * bottom-most element in the original array and <code>d</code> is not a
	 * bottom-most element in the same position as in the original array as
	 * <code>c</code> is but referenced from the cloned array. In other terms,
	 * both <code>c</code> and <code>d</code> are some array object, while
	 * <code>
	 * a</code> and <code>b</code> are deep component elements.
	 * 
	 * @param array
	 *            - the array to clone.
	 * @return the cloned array.
	 * @throws NullPointerException
	 *             if the array parameter is null.
	 * @throws IllegalArgumentException
	 *             if the array parameter is not an actual array.
	 */
	public static Object clone(Object array) throws NullPointerException, IllegalArgumentException {
		if (array == null)
			throw new NullPointerException();
		if (!array.getClass().isArray())
			throw new IllegalArgumentException("must pass in an actual array");

		return clone0(array, null);
	}

	/**
	 * Clones an array such that the bottom-most elements in the original array
	 * are references to the bottom-most elements in the cloned array and
	 * non-bottom-most elements in both arrays are never referenced in the other
	 * array.
	 * 
	 * @param array
	 *            - the array to clone.
	 * @param clone
	 *            - the current cloned array.
	 * @return the cloned array.
	 */
	private static Object clone0(Object array, Object clone) {
		if (array.getClass().isArray() && clone == null)
			clone = Array.newInstance(array.getClass().getComponentType(), Array.getLength(array));

		try {
			int len = Array.getLength(array);

			for (int i = 0; i < len; i++) {
				Object arr = Array.get(array, i);
				if (arr.getClass().isArray())
					Array.set(clone, i, Array.newInstance(arr.getClass().getComponentType(), Array.getLength(arr)));
				Object clonedResult = clone0(arr, Array.get(clone, i));
				if (!clonedResult.getClass().isArray()) {
					Array.set(clone, i, clonedResult);
				}
			}

			return clone;
		} catch (IllegalArgumentException iae) {
			return array;
		}
	}

	/**
	 * Performs a deep equality search on two arrays of the same type. Checks
	 * equality of each component element through instance equality
	 * (<code>==</code>) and through {@link Object#equals(Object)} if instance
	 * equality fails.
	 * 
	 * @param a
	 *            - the first array.
	 * @param b
	 *            - the second array.
	 * @return true if both arrays are equal on a "deep" or raw component level.
	 */
	public static <T> boolean deepEquals(T[] a, T[] b) {
		return deepEquals0(a, b);
	}

	/**
	 * Performs a deep equality search recursively on two arrays assumed to be
	 * of the same type.
	 * 
	 * @param a
	 *            - the first array or element.
	 * @param b
	 *            - the second array or element.
	 * @return true if both arrays are equal on a "deep" or component level or
	 *         if both elements are equal.
	 */
	private static boolean deepEquals0(Object a, Object b) {
		try {
			int len = Array.getLength(a);
			if (len != Array.getLength(b))
				return false;

			for (int i = 0; i < len; i++)
				if (!deepEquals0(Array.get(a, i), Array.get(b, i)))
					return false;

			return true;
		} catch (IllegalArgumentException | NullPointerException iae) {
			return a == b || a.equals(b);
		}
    }
}