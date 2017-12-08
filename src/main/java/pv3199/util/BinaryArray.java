package pv3199.util;

/**
 * A binary tree stored in the form of a binary tree. This array is NOT self balancing.
 *
 * @param <E> a comparable data type
 */
public final class BinaryArray<E extends Comparable<E>> extends HeapImpl<E> {
	/**
	 * Constructs a BinaryArray with a default capacity of 7.
	 */
	public BinaryArray() {
		super();
	}

	/**
	 * Constructs a BinaryArray with a <i>desired</i> initial capacity. The
	 * given capacity is not always ensured because binary array's capacity must
	 * be some positive power of 2 minus 1. As such, the initial capacity is
	 * "rounded" down to the nearest such size.
	 *
	 * @param initialCapacity the desired initial capacity of the binary array.
	 */
	public BinaryArray(int initialCapacity) {
		super(initialCapacity);
	}

	@Override
	public void add(E e) {
		if (e == null) {
			throw new NullPointerException("nulls not allowed");
		}

		// get the best parent node index
		int parent = bestParent(e, 0);

		// if the parent index is less than 0, then we are adding the root
		// to the tree; otherwise we must insert this element to the left
		// subtree or right subtree of the parent node if the element is
		// less than or equal to the parent or greater than the parent
		// (respectively)
		if (parent < 0) {
			set(0, e);
		} else {
			// if the supposed left index of this parent is out of bounds,
			// the binary array must grow in size (we don't need to check
			// for the right index because the left index is less than the
			// right index
			if (refLeft(parent) >= this.data.length) {
				grow();
			}

			// insert element appropriately
			if (e.compareTo(get(parent)) <= 0) {
				setLeft(parent, e);
			} else {
				setRight(parent, e);
			}
		}
	}

	/**
	 * Gets the index of the best parent node for a given element.
	 *
	 * @param e the element to add.
	 * @param node the current node.
	 * @return the index of the best parent node for an element.
	 */
	private int bestParent(E e, int node) {
		// if the node doesn't exist, then return -1 (this should only
		// happen when the binary array is empty
		if (!exists(node)) {
			return -1;
		}

		// get the compare result, and get the left and right node indices of "node"
		int compare = e.compareTo(get(node));
		int left = refLeft(node);
		int right = refRight(node);

		if (compare <= 0 && exists(left)) {
			// best parent resides in the left subtree
			return bestParent(e, left);
		} else if (compare > 0 && exists(right)) {
			// best parent resides in right subtree
			return bestParent(e, right);
		} else {
			// this is the best parent
			return node;
		}
	}

	/**
	 * Checks if the binary array has an element.
	 *
	 * @param e the element.
	 * @return true if the array has the element.
	 */
	public boolean contains(E e) {
		return get(e, 0) >= 0;
	}

	/**
	 * Gets the index of an element.
	 *
	 * @param e the element.
	 * @param node the current node.
	 * @return the index of the element; -1 if not found
	 */
	private int get(E e, int node) {
		if (!exists(node)) {
			// node doesn't exist aka not found
			return -1;
		} else if (e.compareTo(get(node)) == 0) {
			// this is the node, return this index
			return node;
		} else if (e.compareTo(get(node)) < 0) {
			// node resides in left subtree
			return get(e, refLeft(node));
		} else {
			// node resides in right subtree
			return get(e, refRight(node));
		}
	}

	/**
	 * Removes an element from the binary array.
	 *
	 * @param e the element to remove.
	 * @return true if the element was removed (only possible if the
	 * element existed in the array).
	 */
	public boolean remove(E e) {
		// get the index of the element
		int node = get(e, 0);

		if (node < 0) {
			// node DNE
			return false;
		}

		remove(node);

		return true;
	}

	/**
	 * Removes a node from the binary array. 3 possible cases:
	 * node is a leaf, has only one child, or has two children.
	 *
	 * @param node the node index to remove.
	 */
	private void remove(int node) {
		if (isLeaf(node)) {
			leafRemoval(node);
		} else if (isSingle(node)) {
			singleRemoval(node);
		} else {
			doubleRemoval(node);
		}
	}

	/**
	 * Removes a leaf node.
	 *
	 * @param node the leaf node.
	 */
	private void leafRemoval(int node) {
		this.data[node] = null;

		/*if (node == 0) {
			// node is root, set root to null
			this.data[0] = null;
		} else {
			// get parent of node (it must have one since it's not the root)
			int parent = refParent(node);

			// if the left child of this parent is the node, set that
			// child to be null; similarly if it's the right child
			if (refLeft(parent) == node) {
				setLeft(parent, null);
			} else {
				setRight(parent, null);
			}
		}*/
	}

	/**
	 * Removes a node with only one child.
	 *
	 * @param node the node with only child.
	 */
	private void singleRemoval(int node) {
		// get parent of this node
		int parent = refParent(node);
		// get the only child of this node
		int child = exists(refRight(node)) ? refRight(node) : refLeft(node);

		if (node == 0) {
			// node is root, set root to be node's child
			this.data[0] = get(child);
		} else {
			// if node is right child of parent, set right child of parent
			// to be the only child of node; similarly if it's the left child
			if (refRight(parent) == node) {
				setRight(parent, get(child));
			} else {
				setLeft(parent, get(child));
			}
		}

		remove(child);

		// set the child index of this node to be null
		// this.data[child] = null;
	}

	/**
	 * Removes a node with two children.
	 *
	 * @param node the node with two children.
	 */
	private void doubleRemoval(int node) {
		// getting the smallest node on the right subtree of node
		int minSubRight = refRight(node);

		while (exists(refLeft(minSubRight))) {
			minSubRight = refLeft(minSubRight);
		}

		// set the node index to be it's smallest node on it's right subtree
		set(node, get(minSubRight));

		// remove minSubRight from tree appropriately (if it's leaf or single-child node)
		if (isLeaf(minSubRight)) {
			leafRemoval(minSubRight);
		} else {
			singleRemoval(minSubRight);
		}
	}
}