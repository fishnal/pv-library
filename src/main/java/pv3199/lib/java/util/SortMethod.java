package pv3199.lib.java.util;

import java.util.Comparator;
import java.util.function.Function;

/**
 * Valid sorting methods for sorting {@link DataStructure} instances
 *
 * @author Vishal Patel
 */
public enum SortMethod {
	/**
	 * Selection Sort algorithm
	 */
	SELECTION(arg -> {
		// retrieve the structure
		DataStructure<Object> ds = (DataStructure<Object>) arg[0];
		// retrieve the comparator
		Comparator<Object> comparator = (Comparator<Object>) arg[1];
		
		for (int i = 0; i < ds.size() - 1; i++) {
			int smallest = i;
			Object smallestValue = ds.get(i);
			
			for (int j = i + 1; j < ds.size(); j++) {
				if (comparator.compare(smallestValue, ds.get(j)) > 0) {
					smallest = j;
					smallestValue = ds.get(j);
				}
			}
			
			if (i != smallest) ds.swap(i, smallest);
		}
		
		return null;
	}),
	
	/**
	 * Insertion Sort algorithm
	 */
	INSERTION(arg -> {
		// retrieve the structure
		DataStructure<Object> ds = (DataStructure<Object>) arg[0];
		// retrieve the comparator
		Comparator<Object> comparator = (Comparator<Object>) arg[1];
		
		for (int i = 1; i < ds.size(); i++) {
			Object key = ds.get(i);
			
			int j = i;
			while (j > 0 && comparator.compare(key, ds.get(j - 1)) < 0) {
				ds.swap(j, j - 1);
				j--;
			}
		}
		
		return null;
	}),
	
	/**
	 * Bubble Sort algorithm
	 */
	BUBBLE(arg -> {
		// retrieve the structure
		DataStructure<Object> ds = (DataStructure<Object>) arg[0];
		// retrieve the comparator
		Comparator<Object> comparator = (Comparator<Object>) arg[1];
		
		for (int i = 0; i < ds.size(); i++) {
			for (int j = 1; j < ds.size() - i; j++) {
				int compareResult = comparator.compare(ds.get(j - 1), ds.get(j));
				
				if (compareResult > 0) ds.swap(j - 1, j);
			}
		}
		
		return null;
	}),
	
	/**
	 * Quick Sort algorithm
	 */
	QUICK(arg -> {
		// retrieve the structure
		DataStructure<Object> ds = (DataStructure<Object>) arg[0];
		// retrieve the comparator
		Comparator<Object> comparator = (Comparator<Object>) arg[1];
		
		Function<int[], Integer> partition = args -> {
			int low = args[0];
			int high = args[1];
			
			Object pivot = ds.get(high);
			int i = low - 1;
			for (int j = low; j <= high - 1; j++) {
				Object element = ds.get(j);
				
				if (comparator.compare(element, pivot) <= 0) {
					i++;
					ds.swap(i, j);
				}
			}
			
			ds.swap(i + 1, high);
			
			return i + 1;
		};
		
		RecursiveCall<int[], Void> qsort = new RecursiveCall<>();
		qsort.func = args -> {
			int low = args[0];
			int high = args[1];
			
			if (low < high) {
				int pivot = partition.apply(args);
				qsort.func.apply(new int[]{low, pivot - 1});
				qsort.func.apply(new int[]{pivot + 1, high});
			}
			
			return null;
		};
		
		qsort.func.apply(new int[]{0, ds.size() - 1});
		
		return null;
	}),
	
	/**
	 * Merge Sort algorithm
	 */
	MERGE(arg -> {
		// retrieve the structure
		DataStructure<Object> ds = (DataStructure<Object>) arg[0];
		// retrieve the comparator
		Comparator<Object> comparator = (Comparator<Object>) arg[1];
		
		DataStructure<Object> empty = ds.clone();
		empty.clear();
		
		Function<DataStructure<?>[], DataStructure<Object>> merge = args -> {
			DataStructure<Object> first = (DataStructure<Object>) args[0];
			DataStructure<Object> second = (DataStructure<Object>) args[1];
			DataStructure<Object> merged = empty.clone();
			
			while (!first.isEmpty() || !second.isEmpty()) {
				Object firstElement = first.isEmpty() ? null : first.get(0);
				Object secondElement = second.isEmpty() ? null : second.get(0);
				int compareResult = firstElement == null ? 1 : secondElement == null ? -1 : comparator.compare(firstElement, secondElement);
				
				if (compareResult <= 0) {
					merged.add(firstElement);
					first.remove(0);
				} else {
					merged.add(secondElement);
					second.remove(0);
				}
			}
			
			return merged;
		};
		
		RecursiveCall<DataStructure<Object>, DataStructure<Object>> divide = new RecursiveCall<>();
		divide.func = ds2 -> {
			if (ds2.size() == 1) return ds2;
			
			DataStructure<Object> right = ds2.split(0, ds2.size() / 2);
			DataStructure<Object> left = ds2.split(ds2.size() / 2);
			
			right = divide.func.apply(right);
			left = divide.func.apply(left);
			
			return merge.apply(new DataStructure<?>[]{right, left});
		};
		
		ds.set(divide.func.apply(ds));
		
		return null;
	}),
	
	/**
	 * Cocktail Sorting algorithm.
	 */
	COCKTAIL(arg -> {
		// retrieve the structure
		DataStructure<Object> ds = (DataStructure<Object>) arg[0];
		// retrieve the comparator
		Comparator<Object> comparator = (Comparator<Object>) arg[1];
		
		boolean swapped;
		
		do {
			swapped = false;
			
			for (int i = 0; i < ds.size() - 1; i++) {
				if (comparator.compare(ds.get(i), ds.get(i + 1)) > 0) {
					ds.swap(i, i + 1);
					swapped = true;
				}
			}
			
			if (!swapped) break;
			
			swapped = false;
			for (int i = ds.size() - 2; i > -1; i--) {
				if (comparator.compare(ds.get(i), ds.get(i + 1)) > 0) {
					ds.swap(i, i + 1);
					swapped = true;
				}
			}
		} while (swapped);
		
		return null;
	}),
	
	/**
	 * Shell Sorting algorithm. Uses a gap sequence of <code>(3^k - 1) / 2 &lt;= ceil(n / 3)</code>.
	 * Number of gaps is determined by solving for k, and n represents the size of
	 * the data structure.
	 */
	SHELL(arg -> {
		// retrieve the structure
		DataStructure<Object> ds = (DataStructure<Object>) arg[0];
		// retrieve the comparator
		Comparator<Object> comparator = (Comparator<Object>) arg[1];
		
		// Using (3^k - 1) / 2 <= ceil(n/3) gap sequence
		// number of gaps is determined by solving for k
		// and truncating that value:
		// k = log(2 * ceil(n / 3) + 1) / log(3) where log(x) is the base 10
		// logarithm of x.
		// k = (int) k <=> k = floor(k)
		int n = ds.size();
		int[] gaps = new int[(int) (Math.log10(2 * Math.ceil(n / 3) + 1) / Math.log10(3))];
		for (int i = 0; i < gaps.length; i++) {
			gaps[i] = (int) (Math.pow(3, i + 1) - 1) / 2;
		}
		
		for (int gapIndex = gaps.length - 1; gapIndex > -1; gapIndex--) {
			int gap = gaps[gapIndex];
			for (int i = gap; i < n; i++) {
				Object temp = ds.get(i);
				
				int j = i;
				while (j >= gap && comparator.compare(ds.get(j - gap), temp) > 0) {
					ds.set(j, ds.get(j - gap));
					j -= gap;
				}
				
				ds.set(j, temp);
			}
		}
		
		return null;
	});
	
	/**
	 * A void function that is responsible for performing the sort algorithm.
	 * Takes in an array of objects.
	 */
	private Function<Object[], Void> algorithm;
	
	/**
	 * Initializes a SortMethod
	 *
	 * @param algorithm - the algorithm function used for sorting the data.
	 */
	SortMethod(Function<Object[], Void> algorithm) {
		this.algorithm = algorithm;
	}
	
	/**
	 * Sorts a data structure based on this SorthMethod's {@link #algorithm}.
	 *
	 * @param ds         - the data structure being sorted.
	 * @param comparator - the comparator being used to sort elements.
	 * @param <T>        - the type of the data structure.
	 */
	public <T> void apply(DataStructure<T> ds, Comparator<T> comparator) {
		this.algorithm.apply(new Object[]{ds, comparator});
	}
	
	/**
	 * Allows for a recursive call for {@linkplain java.util.function.Function}
	 *
	 * @param <T> - the input type.
	 * @param <R> - the return type.
	 * @author Vishal Patel
	 */
	private static class RecursiveCall<T, R> {
		Function<T, R> func;
	}
}