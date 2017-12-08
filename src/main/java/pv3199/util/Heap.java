package pv3199.util;

import java.util.Comparator;

/**
 * An abstract heap data structure.
 * @param <E>
 */
public abstract class Heap<E extends Comparable<E>> extends HeapImpl<E> {
	protected Comparator<E> comparator = Comparable::compareTo;

	public Heap() {
		super();
	}

	public Heap(int initialCapacity) {
		super(initialCapacity);
	}

	public Heap(Comparator<E> comparator) {
		super();

		this.comparator = comparator;
	}

	public Heap(Comparator<E> comparator, int initialCapacity) {
		super(initialCapacity);

		this.comparator = comparator;
	}

	public abstract E peek();

	public abstract E pop();
}