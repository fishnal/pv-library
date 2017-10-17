package pv3199.util;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Utilities for arrays, such as casting multi-dimensional arrays, sorting
 * multi-dimensional arrays, finding minimum and maximum values, and cloning
 * arrays.
 *
 * @author Vishal Patel
 */
public final class Arrays {
	private final static Random RANDOM = new Random();

	private Arrays() {
	}
	
	/**
	 * Gets the element located at the indices in some dimensional array. If
	 * <code>get(array, new int[]{1, 2, 3})</code> is called, then it
	 * returns the equivalent of <code>array[1][2][3]</code>.
	 *
	 * @param array the array.
	 * @param indices the indices for each dimension.
	 * @return the element located at these indices in the array.
	 * @throws IllegalArgumentException if the array is not actually an array
	 * @throws ArrayIndexOutOfBoundsException if one of the indices is out of bounds for this array.
	 */
	public static Object get(Object array, int... indices) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
		for (int i : indices) {
			array = Array.get(array, i);
		}
		
		return array;
	}
	
	/**
	 * Gets the element located at the indices in some dimensional array. If
	 * <code>get(array, iterator)</code> is called and <code>array</code> is a
	 * valid array and <code>iterator</code> is a valid Iterator consisting of
	 * the Integer objects 1, 2, and 3 in that order, then it virtually returns
	 * <code>array[1][2][3]</code>.
	 *
	 * @param array the array.
	 * @param indices the indices for each dimension.
	 * @return the element located at these indices in the array.
	 * @throws IllegalArgumentException if the array is not actually an array
	 * @throws ArrayIndexOutOfBoundsException if one of the indices is out of bounds for this array.
	 */
	public static Object get(Object array, Iterable<Integer> indices) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
		for (Integer i : indices) {
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
	 * @param array the array.
	 * @param indices  the indices tracing to the array index to change.
	 * @param newValue the new value of the element.
	 * @throws IllegalArgumentException if the array is not actually an array or if the indices
	 * attempt to access another dimension of the array that doesn't exist
	 * @throws ArrayIndexOutOfBoundsException if one of the indices is out of bounds for this array.
	 */
	public static void set(Object array, Object newValue, int... indices) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
		for (int i = 0; i < indices.length; i++) {
			if (i == indices.length - 1) {
				Array.set(array, indices[i], newValue);
			} else {
				array = Array.get(array, indices[i]);
			}
		}
	}
	
	/**
	 * Sets an element in an array to a new value. If
	 * <code>set(array, iterator, null)</code> is called and <code>array</code>
	 * is a valid array and <code>iterator</code> is a valid Iterator consisting
	 * of the Integer objects 1, 2, and 3 in that order, then the method
	 * virtually executes <code>array[1][2][3] = null</code>.
	 *
	 * @param array the array.
	 * @param indices the indices tracing to the array index to change.
	 * @param newValue the new value of the element.
	 * @throws IllegalArgumentException if the array is not actually an array
	 * @throws ArrayIndexOutOfBoundsException if one of the indices is out of bounds for this array.
	 */
	public static void set(Object array, Object newValue, Iterable<Integer> indices) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
		Iterator<Integer> itr = indices.iterator();
		
		while (itr.hasNext()) {
			int i = itr.next();

			if (!itr.hasNext()) {
				Array.set(array, i, newValue);
			} else {
				array = Array.get(array, i);
			}
		}
	}
	
	/**
	 * Randomly deep fills an array with primitives or wrapper-equivalent objects. A check is performed
	 * to make sure the array can be randomly filled based on it's deepest component type and the
	 * random primitive generator provided (an exception is thrown if the check fails). For example, an
	 * int or Integer based array cannot be randomly filled using {@link RandomPrimitive#BOOLEAN}, and
	 * vice-versa. Or even a String based array being randomly filled using {@link RandomPrimitive#CHAR}
	 * as there is no standardized way of determining the string's length.
	 *
	 * @param array the array to randomly fill up
	 * @param generator the {@link RandomPrimitive} enumeration to use when filling the array
	 * @return a reference to the array passed in.
	 * @throws ClassCastException if the deepest component type of the array cannot be casted
	 * from the generated {@link RandomPrimitive} value.
	 * @throws IllegalArgumentException if the array is not actually an array.
	 */
	public static Object randFillArray(Object array, RandomPrimitive generator) throws ClassCastException, IllegalArgumentException {
		return randFillArray0(array, generator, Classes.getDeepestComponent(array.getClass()));
	}
	
	/**
	 * Recursively randomly deep fills an array with primitives or wrapper-equivalent objects.
	 *
	 * @param array the array to randomly fill up.
	 * @param generator the {@link RandomPrimitive} enumeration to use when filling the array.
	 * @param componentType the deepest component type of the array; used to verify that the generated
	 * values can be stored into the array.
	 * @return a reference to the array object passed in
	 * @throws ClassCastException if the deepest component type of the array cannot be casted from the
	 * generated {@link RandomPrimitive} value.
	 */
	private static Object randFillArray0(Object array, RandomPrimitive generator, Class<?> componentType) throws ClassCastException {
		if (array == null) {
			return null;
		}
		
		try {
			int length = Array.getLength(array);
			
			for (int i = 0; i < length; i++) {
				Array.set(array, i, randFillArray0(Array.get(array, i), generator, componentType));
			}
			
			return array;
		} catch (IllegalArgumentException iae) {
			// actual component element of array
			Object generated = generator.generate();
			
			if (!Classes.canCast(componentType, generated.getClass())) {
				throw new ClassCastException(String.format("cannot convert %s to %s", generated.getClass(), componentType));
			}
			
			return Classes.objectToObjectCast(generated, componentType);
		}
	}
	
	/**
	 * Combines multiple arrays into one array.
	 *
	 * @param arrays the arrays to combine.
	 * @param <T> the type of the arrays.
	 * @return the combined array otherwise
	 * @throws IllegalArgumentException if the type of the arrays is not an actual array.
	 */
	public static <T> T joinArrays(final T... arrays) {
		Class<?> clazz = null;
		int len = 0;

		for (T arr : arrays) {
			if (clazz == null) {
				clazz = arr.getClass();
			}
			
			len += Array.getLength(arr);
		}
		
		T combined = (T) Array.newInstance(clazz.getComponentType(), len);
		
		int ind = 0;
		
		for (T array : arrays) {
			int arrLen = Array.getLength(array);
			
			for (int j = 0; j < arrLen; j++) {
				Array.set(combined, ind++, Array.get(array, j));
			}
		}
		
		return combined;
	}
	
	/**
	 * Finds the location of an element in any dimensional array through a
	 * recursive-linear approach. The deepest elements which represent the root
	 *
	 * @param array the array to search through.
	 * @param element the element to look for.
	 * @return null if the element could not be found; otherwise an int array
	 * containing the dimension-indices for the array to lead to the
	 * element.
	 * @throws IllegalArgumentException if the array parameter is not an actual array or if the
	 * component type of array is not the same as the type of the element.
	 */
	public static int[] indexOf(final Object array, final Object element) throws IllegalArgumentException {
		Object o = indexOf0(array, element);
		
		if (o == null) {
			return null;
		}
		
		List<Integer> list = (List<Integer>) o;
		Collections.reverse(list);
		
		int[] inds = new int[list.size()];
		
		for (int i = 0; i < list.size(); i++) {
			inds[i] = list.get(i);
		}
		
		return inds;
	}
	
	/**
	 * Finds the location of an element in any given dimensional array.
	 *
	 * @param array the array to search through.
	 * @param element the element to look for.
	 * @return null if either of the parameters are null or if the element could
	 * not be found; otherwise an integer list containing the indices
	 * for each of the array's dimensions that lead to the element in
	 * reversed order (use ListUtil.reverse(List&lt;?&gt;) to fix the
	 * ordering).
	 */
	private static List<Integer> indexOf0(final Object array, final Object element) {
		if (array == null) {
			if (element == null) {
				return new ArrayList<>();
			} else {
				return null;
			}
		}

		if (element != null && Classes.componentCheck(array.getClass(), element.getClass())) {
			if (array.equals(element)) {
				return new ArrayList<>();
			}
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
	}
	
	/**
	 * Finds the last set of indices of a certain element in a given dimensional
	 * array.
	 *
	 * @param array the array to look through.
	 * @param element the element to find.
	 * @return null if the array is null, is not an array, or if the element
	 * could not be found; otherwise an int array listing the indices at
	 * which the element is located in each dimension.
	 * @throws IllegalArgumentException if the array parameter is not an actual array.
	 */
	public static int[] lastIndexOf(final Object array, final Object element) throws IllegalArgumentException {
		List<Integer> indices = lastIndexOf0(array, element);
		
		if (indices == null) {
			return null;
		}
		
		Collections.reverse(indices);
		
		int[] inds = new int[indices.size()];
		
		for (int i = 0; i < indices.size(); i++) {
			inds[i] = indices.get(i);
		}
		
		return inds;
	}
	
	/**
	 * Finds the last location of an element in any given dimensional array.
	 *
	 * @param array the array to search through.
	 * @param element the element to look for.
	 * @return null if either of the parameters are null or if the element could
	 * not be found; otherwise an integer list containing the indices
	 * for each of the array's dimensions that lead to the element in
	 * reversed order (use ListUtil.reverse(List&lt;?&gt;) to fix the
	 * ordering).
	 */
	private static List<Integer> lastIndexOf0(final Object array, final Object element) {
		if (array == null) {
			if (element == null) {
				return new ArrayList<>();
			} else {
				return null;
			}
		}

		if (element != null && Classes.componentCheck(array.getClass(), element.getClass())) {
			if (array.equals(element)) {
				return new ArrayList<>();
			}
		}
		
		if (array.getClass().isArray()) {
			int length = Array.getLength(array);
			List<Integer> traces;
			
			for (int i = length - 1; i > -1; i--) {
				List<Integer> otherTraces = lastIndexOf0(Array.get(array, i), element);

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
	 * @param array the array to search through.
	 * @param element the element to look for.
	 * @return false if an IllegalArgumentException is caught or if the element
	 * is not found in the array; true otherwise.
	 * @throws IllegalArgumentException if the array parameter is not an actual array.
	 */
	public static boolean contains(final Object array, final Object element) throws IllegalArgumentException {
		try {
			return indexOf(array, element) != null;
		} catch (IllegalArgumentException iae) {
			return false;
		}
	}
	
	/**
	 * Finds the maximum deep value in an array. The maximum value is found by comparing
	 * the deepest elements of the array (null values are ignored). The comparisons
	 * are made using the {@link Comparable} interface. As such, the generic type for a
	 * Comparable object should be the deep component type of this array.
	 *
	 * @param array the array to search through.
	 * @param <T> the generic type for the {@link Comparable} interface, intended to match the array's
	 * deepest component type.
	 * @return the maximum deep value in the array.
	 * @throws ClassCastException if a cast error occurs when attempting to compare two elements
	 * @throws IllegalArgumentException if the object passed in is not an actual array or if the array's
	 * deepest component type does not implement the {@link Comparable} interface.
	 */
	public static <T> Object max(final Object array) throws ClassCastException, IllegalArgumentException {
		if (Comparable.class.isAssignableFrom(Classes.getDeepestComponent(array.getClass()))) {
			return Arrays.<T>maxComparable(array, null);
		}
		
		throw new IllegalArgumentException("no suitable comparator");
	}
	
	/**
	 * Finds the maximum deep value in an array. The maximum value is found by comparing the deepest
	 * elements of the array (null values are ignored). The comparisons are made using the comparator provided.
	 * If the comparator is null, then the {@link Comparable} interface is utilized (see {@link #max(Object)}).
	 *
	 * @param array the array to search through.
	 * @param comparator the comparator used to compare the elements of the array.
	 * @param <T> the generic type of the comparator, intended to match the array's deepest component type.
	 * @return the maximum deep value in the array.
	 * @throws ClassCastException if a cast error occurs when attempting to compare two deep elements in the array.
	 * @throws IllegalArgumentException if the object passed in is not an actual array or if the comparator is null
	 * and the array's deepest component type does not implement the {@link Comparable} interface.
	 * @see #max(Object)
	 */
	public static <T> Object max(final Object array, final Comparator<T> comparator) throws ClassCastException, IllegalArgumentException {
		if (comparator == null) {
			return Arrays.<T>max(array);
		}
		
		return Arrays.maxComparator(array, null, comparator);
	}
	
	/**
	 * Finds the maximum deep value in an array utilizing the {@link Comparable} interface. Null values
	 * are ignored.
	 *
	 * @param array the array to search through.
	 * @param max the current maximum value.
	 * @param <T> the generic type for the {@link Comparable} interface, intended to be the same as the array's
	 * deepest component type.
	 * @return the maximum deep value in the array.
	 * @throws ClassCastException if a cast error occurs when attempting to compare two deep elements in the array.
	 */
	private static <T> Object maxComparable(Object array, T max) throws ClassCastException {
		try {
			int len = Array.getLength(array);
			
			for (int i = 0; i < len; i++) {
				Object arr = Array.get(array, i);
				max = (T) Arrays.maxComparable(arr, max);
			}
			
			return max;
		} catch (IllegalArgumentException iae) {
			if (max == null && array != null) {
				return array;
			} else if (((Comparable<T>) array).compareTo(max) > 0) {
				return array;
			} else {
				return max;
			}
		}
	}
	
	/**
	 * Finds the maximum deep value in an array utilizing a {@link Comparator}. Null values
	 * are ignored.
	 *
	 * @param array the array to search through.
	 * @param max the current maximum value.
	 * @param comparator the comparator to use for comparing the elements in the array.
	 * @param <T> the generic type for the {@link Comparator} interface, intended to be the same as the array's
	 * deepest component type.
	 * @return the maximum deep value in the array.
	 * @throws ClassCastException if a cast error occurs when attempting to compare two deep elements in the array.
	 */
	private static <T> Object maxComparator(Object array, T max, final Comparator<T> comparator) throws ClassCastException {
		try {
			int len = Array.getLength(array);
			
			for (int i = 0; i < len; i++) {
				Object arr = Array.get(array, i);
				max = (T) Arrays.maxComparator(arr, max, comparator);
			}
			
			return max;
		} catch (IllegalArgumentException iae) {
			if (max == null && array != null || comparator.compare((T) array, max) > 0) {
				return array;
			} else {
				return max;
			}
		}
	}
	
	/**
	 * Finds the minimum deep value in an array. The minimum value is found by comparing
	 * the deepest elements of the array (null values are ignored). The comparisons
	 * are made using the {@link Comparable} interface. As such, the generic type for a
	 * Comparable object should be the deep component type of this array.
	 *
	 * @param array the array to search through.
	 * @param <T> the generic type for the {@link Comparable} interface, intended to match the array's
	 * deepest component type.
	 * @return the minimum deep value in the array.
	 * @throws ClassCastException if a cast error occurs when attempting to compare two elements
	 * @throws IllegalArgumentException if the object passed in is not an actual array or if the array's
	 * deepest component type does not implement the {@link Comparable} interface.
	 */
	public static <T> Object min(final Object array) throws ClassCastException {
		if (Comparable.class.isAssignableFrom(Classes.getDeepestComponent(array.getClass()))) {
			return Arrays.<T>minComparable(array, null);
		}
		
		throw new IllegalArgumentException("no suitable comparator");
	}
	
	/**
	 * Finds the minimum deep value in an array. The minimum value is found by comparing the deepest
	 * elements of the array (null values are ignored). The comparisons are made using the comparator provided.
	 * If the comparator is null, then the {@link Comparable} interface is utilized (see {@link #min(Object)}).
	 *
	 * @param array the array to search through.
	 * @param comparator the comparator used to compare the elements of the array.
	 * @param <T> the generic type of the comparator, intended to match the array's deepest component type.
	 * @return the minimum deep value in the array.
	 * @throws ClassCastException if a cast error occurs when attempting to compare two deep elements in the
	 * array.
	 * @throws IllegalArgumentException if the object passed in is not an actual array or if the comparator is null
	 * and the array's deepest component type does not implement the {@link Comparable} interface.
	 * @see #max(Object)
	 */
	public static <T> Object min(final Object array, final Comparator<T> comparator) throws ClassCastException {
		if (comparator == null) {
			return min(array);
		}
		
		return minComparator(array, null, comparator);
	}
	
	/**
	 * Finds the minimum deep value in an array utilizing a {@link Comparable}. Null values
	 * are ignored.
	 *
	 * @param array the array to search through.
	 * @param min the current minimum value.
	 * @param <T> the generic type for the {@link Comparable} interface, intended to be the same as the array's
	 * deepest component type.
	 * @return the minimum deep value in the array.
	 * @throws ClassCastException if a cast error occurs when attempting to compare two deep elements in the array.
	 */
	private static <T> Object minComparable(Object array, T min) throws ClassCastException {
		try {
			int len = Array.getLength(array);
			
			for (int i = 0; i < len; i++) {
				Object arr = Array.get(array, i);
				min = (T) Arrays.minComparable(arr, min);
			}
			
			return min;
		} catch (IllegalArgumentException iae) {
			if (min == null && array != null) {
				return array;
			} else if (((Comparable<T>) array).compareTo(min) < 0) {
				return array;
			} else {
				return min;
			}
		}
	}
	
	/**
	 * Finds the minimum deep value in an array utilizing a {@link Comparator}. Null values
	 * are ignored.
	 *
	 * @param array the array to search through.
	 * @param min the current minimum value.
	 * @param comparator the comparator to use in comparing the elements in the array.
	 * @param <T> the generic type for the {@link Comparator} interface, intended to be the same as the array's
	 * deepest component type.
	 * @return the minimum deep value in the array.
	 * @throws ClassCastException if a cast error occurs when attempting to compare two deep elements in the array.
	 */
	private static <T> Object minComparator(Object array, T min, final Comparator<T> comparator) throws ClassCastException {
		try {
			int len = Array.getLength(array);
			
			for (int i = 0; i < len; i++) {
				Object arr = Array.get(array, i);
				min = (T) Arrays.minComparator(arr, min, comparator);
			}
			
			return min;
		} catch (IllegalArgumentException iae) {
			if (min == null && array != null) {
				return array;
			} else if (comparator.compare((T) array, min) < 0) {
				return array;
			} else {
				return min;
			}
		}
	}
	
	/**
	 * Sorts an array in ascending order comparing the array's deepest elements (null values are considered to be less
	 * than any other value). The comparisons are made using the {@link Comparable} interface. As such, the generic
	 * type for a Comparable object should be the deep component type of this array.
	 *
	 * @param array the array to sort.
	 * @param <T> the generic type for the {@link Comparable} interface, intended to match the array's deepest
	 * component type.
	 * @return the sorted array
	 * @throws IllegalArgumentException if the object passed in is not an actual array or if the array's deepest
	 * component type does not implement the {@link Comparable} interface.
	 */
	public static <T> Object sort(final Object array) throws IllegalArgumentException {
		if (Comparable.class.isAssignableFrom(Classes.getDeepestComponent(array.getClass()))) {
			return Sorter.<T>sort(array, null, Sorter.CompareMethod.COMPARABLE, false);
		}
		
		throw new IllegalArgumentException("no suitable comparator");
	}
	
	/**
	 * Sorts an array in ascending order comparing the array's deepest elements (null values are considered to be less
	 * than any other value). The comparisons are made using a {@link Comparator}. As such, the generic
	 * type for the Comparator object should be the deep component type of this array. If the comparator
	 * provided is null, then the {@link Comparable} interface is utilized (see {@link #sort(Object)}).
	 *
	 * @param array the array to sort.
	 * @param <T> the generic type for the {@link Comparator} interface, intended to match the array's deepest
	 * component type.
	 * @param comparator the comparator to use for sorting
	 * @return the sorted array
	 * @throws IllegalArgumentException if the object passed in is not an actual array or if the comparator is null and
	 * the array's deepest component type does not implement the {@link Comparable} interface.
	 * @see #sort(Object)
	 */
	public static <T> Object sort(final Object array, final Comparator<T> comparator) throws IllegalArgumentException {
		if (comparator == null) {
			return Arrays.<T>sort(array);
		}
		
		return Sorter.sort(array, comparator, Sorter.CompareMethod.COMPARATOR, false);
	}
	
	/**
	 * Sorts an array in descending order comparing the array's deepest elements (null values are considered to be less
	 * than any other value). The comparisons are made using the {@link Comparable} interface. As such, the generic
	 * type for a Comparable object should be the deep component type of this array.
	 *
	 * @param array the array to sort.
	 * @param <T> the generic type for the {@link Comparable} interface, intended to match the array's deepest
	 * component type.
	 * @return the reverse-sorted array
	 * @throws IllegalArgumentException if the object passed in is not an actual array or if the array's deepest
	 * component type does not implement the {@link Comparable} interface.
	 */
	public static <T> Object reverseSort(final Object array) {
		if (Comparable.class.isAssignableFrom(Classes.getDeepestComponent(array.getClass()))) {
			return Sorter.<T>sort(array, null, Sorter.CompareMethod.COMPARABLE, true);
		}
		
		throw new IllegalArgumentException("no suitable comparator");
	}
	
	/**
	 * Sorts an array in descending order comparing the array's deepest elements (null values are considered to be less
	 * than any other value). The comparisons are made using a {@link Comparator}. As such, the generic
	 * type for the Comparator object should be the deep component type of this array. If the comparator
	 * provided is null, then the {@link Comparable} interface is utilized (see {@link #sort(Object)}).
	 *
	 * @param array the array to sort.
	 * @param <T> the generic type for the {@link Comparator} interface, intended to match the array's deepest
	 * component type.
	 * @param comparator the comparator to use in reverse sorting.
	 * @return the reverse-sorted array
	 * @throws IllegalArgumentException if the object passed in is not an actual array or if the comparator is null and
	 * the array's deepest component type does not implement the {@link Comparable} interface.
	 * @see #sort(Object)
	 */
	public static <T> Object reverseSort(final Object array, final Comparator<T> comparator) {
		if (comparator == null) {
			return Arrays.<T>reverseSort(array);
		}
		
		return Sorter.sort(array, comparator, Sorter.CompareMethod.COMPARATOR, true);
	}
	
	/**
	 * Casts an array of one type to another type of array, including primitive
	 * to wrapper and wrapper to primitive casts.
	 *
	 * @param source the array to cast.
	 * @param newType the new data type that the array will be casted to.
	 * @param <T> the type of the new array.
	 * @return null if the array cannot be casted; otherwise the casted array.
	 * @throws IllegalArgumentException if the array parameter is not an actual array.
	 */
	public static <T> T castArray(Object source, Class<T> newType) throws IllegalArgumentException {
		Class<?> at = Classes.getDeepestComponent(source.getClass());
		Class<?> nt = Classes.getDeepestComponent(newType);
		
		// if at is prim/wrap and nt is prim/wrap, make sure that
		// if at is boolean and nt is not boolean or vice-versa
		// then the cast is not possible
		
		if ((at.isPrimitive() || Classes.isWrapper(at)) && (nt.isPrimitive() || Classes.isWrapper(nt))) {
			Class<?> primAT = Classes.wrapperClassToPrimitiveClass(at);
			Class<?> primNT = Classes.wrapperClassToPrimitiveClass(nt);
			
			if (primAT == boolean.class ^ primNT == boolean.class) {
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
	 * @param source the array object to copy elements from.
	 * @param newDeepComponentType the data type to cast the elements from source into.
	 * @return null if the original array is null; the new array object with the
	 * elements from <code>source</code> copied (and possibly casted).
	 * @throws ClassCastException if an element from source cannot be casted into the data type
	 * specified by <code>newType</code>
	 */
	private static Object copyElements(Object source, Class<?> newDeepComponentType) throws ClassCastException {
		if (source == null) {
			return null;
		}
		
		try {
			int length = Array.getLength(source);
			int[] dimensions = getDimensionSizes(source);
			Object newArray = Array.newInstance(newDeepComponentType, dimensions);
			
			for (int i = 0; i < length; i++) {
				// this is part of the array to iterate through
				Object element = copyElements(Array.get(source, i), newDeepComponentType);
				Array.set(newArray, i, element);
			}
			
			return newArray;
		} catch (IllegalArgumentException iae) {
			// this is an actual component element and not an array
			if (!Classes.canCast(source.getClass(), newDeepComponentType)) {
				throw new ClassCastException();
			}
			
			return Classes.objectToObjectCast(source, newDeepComponentType);
		}
	}
	
	/**
	 * Gets the dimension sizes of an array object.
	 *
	 * @param array the array object to retrieve the dimension sizes from.
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

			if (dims[i] == 0) {
				break;
			}

            int j = 0;
            Object temp;
			while ((temp = Array.get(pointer, j)) != null && temp.getClass().isArray() && Array.getLength(temp) == 0 && j < Array.getLength(pointer)) {
				j++;
			}

			if (j == Array.getLength(pointer)) {
				break;
			}

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
	 * <code>a</code> and <code>b</code> are deep component elements.
	 *
	 * @param array the array to clone.
	 * @return the cloned array.
	 * @throws IllegalArgumentException if the array parameter is not an actual array.
	 */
	public static Object clone(Object array) throws IllegalArgumentException {
		return clone0(array, null);
	}
	
	/**
	 * Clones an array such that the bottom-most elements in the original array
	 * are references to the bottom-most elements in the cloned array and
	 * non-bottom-most elements in both arrays are never referenced in the other
	 * array.
	 *
	 * @param array the array to clone.
	 * @param clone the current cloned array.
	 * @return the cloned array.
	 */
	private static Object clone0(Object array, Object clone) {
		if (array.getClass().isArray() && clone == null) {
			clone = Array.newInstance(array.getClass().getComponentType(), Array.getLength(array));
		}
		
		try {
			int len = Array.getLength(array);
			
			for (int i = 0; i < len; i++) {
				Object arr = Array.get(array, i);
				if (arr.getClass().isArray()) {
					Array.set(clone, i, Array.newInstance(arr.getClass().getComponentType(), Array.getLength(arr)));
				}
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
	 * @param a the first array.
	 * @param b the second array.
	 * @param <T> the type of the arrays.
	 * @return true if both arrays are equal on a "deep" or raw component level.
	 */
	public static <T> boolean deepEquals(T[] a, T[] b) {
		return deepEquals0(a, b);
	}
	
	/**
	 * Checks if two byte arrays are equal.
	 *
	 * @param a the first byte array.
	 * @param b the second byte array.
	 * @return true if both byte arrays are equal.
	 */
	public static boolean equals(byte[] a, byte[] b) {
		return deepEquals0(a, b);
	}
	
	/**
	 * Checks if two boolean arrays are equal.
	 *
	 * @param a the first boolean array.
	 * @param b the second boolean array.
	 * @return true if both boolean arrays are equal.
	 */
	public static boolean equals(boolean[] a, boolean[] b) {
		return deepEquals0(a, b);
	}
	
	/**
	 * Checks if two char arrays are equal.
	 *
	 * @param a the first char array.
	 * @param b the second char array.
	 * @return true if both char arrays are equal.
	 */
	public static boolean equals(char[] a, char[] b) {
		return deepEquals0(a, b);
	}
	
	/**
	 * Checks if two short arrays are equal.
	 *
	 * @param a the first short array.
	 * @param b the second short array.
	 * @return true if both short arrays are equal.
	 */
	public static boolean equals(short[] a, short[] b) {
		return deepEquals0(a, b);
	}
	
	/**
	 * Checks if two int arrays are equal.
	 *
	 * @param a the first int array.
	 * @param b the second int array.
	 * @return true if both int arrays are equal.
	 */
	public static boolean equals(int[] a, int[] b) {
		return deepEquals0(a, b);
	}
	
	/**
	 * Checks if two float arrays are equal.
	 *
	 * @param a the first float array.
	 * @param b the second float array.
	 * @return true if both float arrays are equal.
	 */
	public static boolean equals(float[] a, float[] b) {
		return deepEquals0(a, b);
	}
	
	/**
	 * Checks if two long arrays are equal.
	 *
	 * @param a the first long array.
	 * @param b the second long array.
	 * @return true if both long arrays are equal.
	 */
	public static boolean equals(long[] a, long[] b) {
		return deepEquals0(a, b);
	}
	
	/**
	 * Checks if two double arrays are equal.
	 *
	 * @param a the first double array.
	 * @param b the second double array.
	 * @return true if both double arrays are equal.
	 */
	public static boolean equals(double[] a, double[] b) {
		return deepEquals0(a, b);
	}
	
	/**
	 * Performs a deep equality search recursively on two arrays assumed to be
	 * of the same type.
	 *
	 * @param a the first array or element.
	 * @param b the second array or element.
	 * @return true if both arrays are equal on a "deep" or component level or
	 * if both elements are equal.
	 */
	private static boolean deepEquals0(Object a, Object b) {
		try {
			int len = Array.getLength(a);

			if (len != Array.getLength(b)) {
				return false;
			}
			
			for (int i = 0; i < len; i++) {
                if (!deepEquals0(Array.get(a, i), Array.get(b, i))) {
                    return false;
                }
            }
			
			return true;
		} catch (IllegalArgumentException | NullPointerException e) {
			return a == b || a.equals(b);
		}
	}
	
	public static void reverse(Object array) throws IllegalArgumentException {
		int length = Array.getLength(array);
		
		for (int i = 0; i < length / 2; i++) {
			Object o1 = Array.get(array, i);
			Object o2 = Array.get(array, length - 1 - i);
			
			Array.set(array, i, o2);
			Array.set(array, length - 1 - i, o1);
		}
	}
	
	/**
	 * Enumeration for generating a random primitive value (wraps into an object when the generated value
	 * is returned). Each type has a value generated from the following:
	 * <ul>
	 * <li>boolean - true or false</li>
	 * <li>byte - [{@value Byte#MIN_VALUE}, {@value Byte#MAX_VALUE}]</li>
	 * <li>char - [{@value Character#MIN_VALUE}, {@value Character#MAX_VALUE}]</li>
	 * <li>short - [{@value Short#MIN_VALUE}, {@value Short#MAX_VALUE}]</li>
	 * <li>int - [{@value Integer#MIN_VALUE}, {@value Integer#MAX_VALUE}]</li>
	 * <li>long - [{@value Long#MIN_VALUE}, {@value Long#MAX_VALUE}]</li>
	 * <li>float - [0, 1)</li>
	 * <li>double - [0, 1)</li>
	 * </ul>
	 */
	public enum RandomPrimitive {
		BOOLEAN(RANDOM::nextBoolean),
		BYTE(() -> (byte) (RANDOM.nextInt(Byte.MAX_VALUE * 2) - Byte.MAX_VALUE)),
		CHAR(() -> (char) RANDOM.nextInt(Character.MAX_VALUE + 1)),
		SHORT(() -> (short) (RANDOM.nextInt(Short.MAX_VALUE) * 2) - Short.MAX_VALUE),
		INT(RANDOM::nextInt),
		LONG(RANDOM::nextLong),
		FLOAT(RANDOM::nextFloat),
		DOUBLE(RANDOM::nextDouble);
		
		/**
		 * The generator function.
		 */
		private Supplier<?> generator;
		
		/**
		 * Constructs a RandomPrimitive enumeration given a {@link UnaryOperator} generator function.
		 *
		 * @param generator the {@link UnaryOperator} generator function.
		 */
		RandomPrimitive(Supplier<?> generator) {
			this.generator = generator;
		}
		
		/**
		 * @return a random value of the type represented by this RandomPrimitive enumeration.
		 */
		public Object generate() {
			return this.generator.get();
		}
	}
	
	/**
	 * Helper methods for sorting an array in ascending or descending order.
	 *
	 * @author Vishal Patel
	 */
	private final static class Sorter {
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
		 * @param array the array to sort.
		 * @param comparator the comparator to use if the method is to sort the
		 *                   elements based on a comparator; otherwise ignored
		 * @param method the method to sort the array by;
		 *                   <ul>
		 *                   <li>0 - Comparable casting</li>
		 *                   <li>1 - Hash Code comparisons</li>
		 *                   <li>2 - Comparator invocation</li>
		 *                   </ul>
		 * @param descending whether or not this array is sorted in ascending (false) or descending order (true)
		 * @param <T> the component type of the array.
		 * @return null if the method given was not supported or did not exist;
		 * otherwise the sorted array in ascending order.
		 */
		static <T> Object sort(Object array, final Comparator<T> comparator, final CompareMethod method, boolean descending) {
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
		 * @param array the array to sort (only used for cloning on this
		 * method's first stack, afterwards overridden to represent some element in the cloned array).
		 * @param newArray the cloned array that will be sorted; must be null for proper sorting
		 * @param method the method used to compare the elements; only used in
		 * {@link #findKey(Object, CompareMethod, Comparator, boolean)}
		 * @param comparator the comparator used if the method is to compare the
		 * elements using a Comparator instance; ignored otherwise.
		 * @param descending whether the array is being sorted in descending order or not.
		 * @param <T> the component type of the array.
		 * @return the sorted array.
		 */
		private static <T> Object sort0(Object array, Object newArray, final CompareMethod method, final Comparator<T> comparator, final boolean descending) {
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
					Arrays.set(newArray, key, sortTrace);
					Arrays.set(newArray, temp, keyTrace);
					
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
		 * @param array  the array to look through.
		 * @param method  the method used to compare the elements.
		 * @param comparator the comparator used if the method compares the elements
		 * using a comparator instance.
		 * @param descending whether the array is being sorted in descending order or not.
		 * @param <T> the component type of the array.
		 */
		private static <T> void findKey(Object array, final CompareMethod method, final Comparator<T> comparator, final boolean descending) {
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
						
						if (fte < ste) {
							return;
						} else if (fte > ste) {
							break;
						}
					}
					
					boolean changed = false;
					
					if (key == null) {
						changed = true;
					} else if (method == CompareMethod.COMPARABLE) {
						Comparable<T> array_T = (Comparable<T>) array;
						T key_T = (T) key;
						if ((!descending && array_T.compareTo(key_T) < 0) || (descending && array_T.compareTo(key_T) > 0)) {
							changed = true;
						}
					} else if (method == CompareMethod.COMPARATOR) {
						T array_T = (T) array;
						T key_T = (T) key;
						
						if ((!descending && comparator.compare(array_T, key_T) < 0) || (descending && comparator.compare(array_T, key_T) > 0)) {
							changed = true;
						}
					} else {
						// the comparing method isn't recognized
						throw new RuntimeException("unrecognized CompareMethod value");
					}
					
					if (changed) {
						key = array;
						keyTrace.clear();
						keyTrace.addAll(finderTrace);
					}
				}
			}
		}
		
		/**
		 * Types of methods that can be used to compare the elements.
		 *
		 * @author Vishal Patel
		 */
		enum CompareMethod {
			/**
			 * Compares elements by casting the first to a Comparable type and
			 * then invoking {@link java.lang.Comparable#compareTo(Object)},
			 * passing the second in as a parameter.
			 */
			COMPARABLE,
			
			/**
			 * Compares elements using
			 * {@link java.util.Comparator#compare(Object, Object)}.
			 */
			COMPARATOR
		}
	}
}