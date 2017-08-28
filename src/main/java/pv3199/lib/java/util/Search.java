package pv3199.lib.java.util;

import java.util.Comparator;
import java.util.List;

public final class Search {
	private Search() {
	}
	
	/**
	 * Binary searches for an element in an array sorted in ascending order
	 * using a given comparator. If the array is not sorted, then an attempt is
	 * made to sort the array. If this fails, then a linear search is performed
	 * on the array as specified by {@linkplain #search(Object[], Object)}.
	 *
	 * @param array      - the sorted array to look through.
	 * @param target     - the element to look for.
	 * @param comparator - the comparator used for comparing the elements; if null then
	 *                   a "default" comparator is used, casting the elements to
	 *                   Comparable types.
	 * @param <T>        - the type of the array.
	 * @return the index of the element in the array; -1 if the element is not
	 * located in the array.
	 * @see #search(Object[], Object)
	 */
	public static <T> int binarySearch(T[] array, T target, Comparator<T> comparator) {
		return binarySearch(java.util.Arrays.asList(array), target, comparator);
	}
	
	/**
	 * Binary searches for an element in an list sorted in ascending order using
	 * a given comparator. If the list is not sorted, then an attempt is made to
	 * sort the list. If this fails, then a linear search is performed on the
	 * list as specified by {@linkplain #search(List, Object)}.
	 *
	 * @param list       - the sorted list to look through.
	 * @param target     - the element to look for.
	 * @param comparator - the comparator used for comparing the elements
	 * @param <T>        - the type of the array.
	 * @return the index of the element in the list; -1 if the element is not
	 * located in the list.
	 * @see #search(List, Object)
	 */
	public static <T> int binarySearch(List<T> list, T target, Comparator<T> comparator) {
		if (comparator == null) {
			if (list.size() == 0) {
				return -1;
			} else if (!Comparable.class.isAssignableFrom(list.get(0).getClass())) {
				return search(list, target);
			}
			comparator = (o1, o2) -> ((Comparable<T>) o1).compareTo(o2);
		}
		
		int l = 0;
		int h = list.size() - 1;
		int m;
		
		while (l <= h) {
			m = (l + h) / 2;
			
			int res = comparator.compare(target, list.get(m));
			
			if (res == 0) {
				return m;
			} else if (res < 0) {
				h = m - 1;
			} else {
				l = m + 1;
			}
		}
		
		return -1;
	}
	
	/**
	 * Linearly searches an array for the first occurrence of a specified
	 * element. Should be avoided if the array can be sorted.
	 *
	 * @param array  - the array to look through.
	 * @param target - the target to look for.
	 * @param <T>    - the type of the array.
	 * @return the index of the element in the array; -1 if the element could
	 * not be found.
	 */
	public static <T> int search(T[] array, T target) {
		for (int i = 0; i < array.length; i++) {
			if (target == null && array[i] == null || array[i].equals(target)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Linearly searches an list for the first occurrence of a specified
	 * element. Should be avoided if the list can be sorted.
	 *
	 * @param list   - the list to look through.
	 * @param target - the target to look for.
	 * @param <T>    - the type of the list.
	 * @return the index of the element in the list; -1 if the element could not
	 * be found.
	 */
	public static <T> int search(List<T> list, T target) {
		for (int i = 0; i < list.size(); i++) {
			if (target == null && list.get(i) == null || list.get(i).equals(target)) {
				return i;
			}
		}
		return -1;
	}
}