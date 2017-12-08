package pv3199.util;

/**
 * Represents an abstract "heap" implementation. Each heap has an underlying array that mimics a binary tree.
 * The array's size is always <code>2<sup>n</sup> - 1</code>, where <code>n</code> is some positive integer.
 * Although heaps are implemented for priority queues or as min/max heaps, a <code>HeapImpl</code> is intended to
 * provide abstraction for accessing an array as if it were a binary tree.
 *
 * @param <E> the data type for this heap implementation.
 */
abstract class HeapImpl<E> {
	/**
	 * The default capacity is actually 7. The constructor computes the size as 2 to the power of
	 * (log base 2 of desired capacity) less 1. In this scenario, the log is 3, the exponential is 8,
	 * and less 1 is 7.
	 */
	private final static int DEFAULT_CAPACITY = 8;

	/**
	 * The array data.
 	 */
	protected Object[] data;

	/**
	 * Constructs a HeapImpl with a capacity of 7.
	 */
	protected HeapImpl() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Constructs a HeapImpl that <i>fits</i> the initial capacity. Note that this capacity is NOT guaranteed.
	 * This is because the size of the array must be <code>2<sup>n</sup> - 1</code> for n as any positive integer.
	 * In order to achieve this, the log base 2 of the capacity must be taken. 2 is raised to the power of this
	 * logarithm and then 1 is taken away from it. This results in values starting from 1, 3, 7, 15, 31, etc.
	 *
	 * @param initialCapacity the <i>desired</i> initial capacity.
	 * @throws IllegalArgumentException if the capacity is negative.
	 */
	protected HeapImpl(int initialCapacity) throws IllegalArgumentException {
		if (initialCapacity <= 0) {
			throw new IllegalArgumentException("non-positive capacity");
		}

		int log2 = (int) (Math.log(initialCapacity) / Math.log(2));
		int size = (int) Math.pow(2, log2) - 1;

		this.data = new Object[size];
	}

	/**
	 * Gets the size of this heap.
	 *
	 * @return the size of this heap.
	 */
	public int size() {
		return size0(0);
	}

	/**
	 * Gets the size of this node.
	 * @param i the node index.
	 * @return the size of the node.
	 */
	private int size0(int i) {
		if (!exists(i)) {
			return 0;
		} else {
			return 1 + size0(refLeft(i)) + size0(refRight(i));
		}
	}

	/**
	 * Grows the underlying array. This should only be called when appropriate and necessary
	 * by subclasses.
	 */
	protected final void grow() {
		int size = (this.data.length + 1) * 2 - 1;

		Object[] newData = new Object[size];

		System.arraycopy(this.data, 0, newData, 0, this.data.length);

		this.data = newData;
	}

	/**
	 * Shrinks the underlying array. This should only be called when appropriate and necessary
	 * by subclasses. The array can only be shrunk if the heap's height is greater than 1. The
	 * array <i>should</i> only be shrunk under two conditions:
	 * <ol>
	 *     <li>Heap's height > 1</li>
	 *     <li>Heap's last level is filled with null elements</li>
	 * </ol>
	 */
	protected final void shrink() {
		if (height() <= 1) {
			return;
		}

		int size = (this.data.length + 1) / 2 - 1;

		Object[] newData = new Object[size];

		System.arraycopy(this.data, 0, newData, 0, size);

		this.data = newData;
	}

	/**
	 * Gets the height of this heap.
	 *
	 * @return the height of the heap.
	 */
	public int height() {
		return height0(0);
	}

	/**
	 * Gets the height of this node.
	 *
	 * @param i the node index.
	 * @return the height of the node.
	 */
	private int height0(int i) {
		if (!exists(i)) {
			return 0;
		} else {
			return 1 + Math.max(height0(refLeft(i)), height0(refRight(i)));
		}
 	}

	/**
	 * Gets the element data at the index.
	 *
	 * @param i the index.
	 * @return the element data at the index.
	 */
 	@SuppressWarnings("unchecked")
	protected final E get(int i) {
		return (E) this.data[i];
	}

	/**
	 * Checks if the node at the specified index exists.
	 *
	 * @param i the index.
	 * @return true if th node exists at the index.
	 */
	protected final boolean exists(int i) {
		return i >= 0 && i < this.data.length && this.data[i] != null;
	}

	/**
	 * Checks if a node is a leaf.
	 *
	 * @param i the index of the node.
	 * @return true if the node is a leaf.
	 */
	protected final boolean isLeaf(int i) {
		return !exists(refLeft(i)) && !exists(refRight(i));
	}

	/**
	 * Checks if a node has only one child.
	 *
	 * @param i the index of the node.
	 * @return true if the node has only one child.
	 */
	protected final boolean isSingle(int i) {
		return exists(refLeft(i)) ^ exists(refRight(i));
	}

	/**
	 * Sets the index of the array.
	 *
	 * @param i the index.
	 * @param e the element data.
	 */
	protected final void set(int i, E e) {
		this.data[i] = e;
	}

	/**
	 * Gets the index of the parent of this node.
	 *
	 * @param i the index of the node.
	 * @return the parent index of the node.
	 */
	protected final int refParent(int i) {
		return (int) Math.floor((i - 1) / 2d);
	}

	/**
	 * Gets the element data of the parent of this node.
	 *
	 * @param i the index of the node.
	 * @return the parent data of the node.
	 */
	@SuppressWarnings("unchecked")
	protected final E getParent(int i) {
		return (E) this.data[refParent(i)];
	}

	/**
	 * Gets the index of the left child of this node.
	 * @param i the index of the node.
	 * @return the left child index of the node.
	 */
	protected final int refLeft(int i) {
		return 2 * i + 1;
	}

	/**
	 * Gets the element data of the left child of this node.
	 *
	 * @param i the index of the node.
	 * @return the left child data of the node.
	 */
	protected final E getLeft(int i) {
		return get(refLeft(i));
	}

	/**
	 * Sets the left child of this node.
	 *
	 * @param i the index of the node.
	 * @param e the new element data.
	 */
	protected final void setLeft(int i, E e) {
		set(refLeft(i), e);
	}

	/**
	 * Gets the index of the right child of this node.
	 *
	 * @param i the index of the node.
	 * @return the right child index of the node.
	 */
	protected final int refRight(int i) {
		return 2 * i + 2;
	}

	/**
	 * Gets the element data of the right child of this node.
	 *
	 * @param i the index of the node.
	 * @return the right child data of the node.
	 */
	protected final E getRight(int i) {
		return get(refRight(i));
	}

	/**
	 * Sets the right child of this node.
	 *
	 * @param i the index of the node.
	 * @param e the new element data.
	 */
	protected final void setRight(int i, E e) {
		set(refRight(i), e);
	}

	/**
	 * Adds an element to the heap.
	 *
	 * @param e the element to add.
	 */
	public abstract void add(E e);
}