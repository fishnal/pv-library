package pvlib.util;

public abstract class Tree<E> implements java.io.Serializable {
	/**
	 * Abstract node for the tree that is comprised of a parent node and a value
	 * for said key. Should be implemented in classes that extend
	 * {@linkplain Tree}
	 * 
	 * @author Vishal Patel
	 *
	 */
	protected abstract class Node {
		/**
		 * The value of this Node.
		 */
		protected E value;

		/**
		 * The parent of this Node. Null value indicates that this Node is a
		 * root of the tree.
		 */
		protected Node parent;

		/**
		 * Constructs a Node object given a parent Node (null indicates this
		 * Node is the root) and value object for said key.
		 * 
		 * @param parent
		 *            - the parent of this node.
		 * @param value
		 *            - the value for this node.
		 */
		protected Node(Node parent, E value) {
			this.parent = parent;
			this.value = value;
		}

		/**
		 * @return the value associated with this node.
		 */
		public final E getValue() {
			return value;
		}

		/**
		 * @return the parent of this node.
		 */
		public final Node getParent() {
			return parent;
		}

		/**
		 * Determines whether or not this node is or can be the root of a tree
		 * given that it's parent is null.
		 * 
		 * @return true if the parent of this node is null; false otherwise.
		 */
		public final boolean isRoot() {
			return parent == null;
		}

		/**
		 * Compares the keys and values of the two nodes. Any exception thrown
		 * should be caught, have it's stack trace printed, and return false as
		 * a result.
		 * 
		 * @return the equality of the keys and values.
		 */
		@Override
		public final boolean equals(Object obj) {
			try {
				Node n = (Node) obj;

				return this.value.equals(n.value);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		/**
		 * @return the parent of this node, and then the key and value
		 *         associated with this node
		 */
		@Override
		public String toString() {
			StringBuilder s = new StringBuilder();

			s.append(String.format("parent=%s", parent == null ? null : parent.value));
			s.append(String.format(",this=%s", this.value));

			return s.toString();
		}
	}

	/**
	 * The root of the tree. If there is at least one entry in the tree, then
	 * the root is guaranteed not to be null if and only if the sub-classes do
	 * not allow null values for their values.
	 */
	protected Node root;

	/**
	 * Adds a value to the tree. Where it is placed is determined by
	 * implementing classes.
	 * 
	 * @param value
	 *            - the value to add.
	 */
	public abstract void add(E value);

	/**
	 * Checks if the tree has a given value.
	 * 
	 * @param value
	 *            - the value to search for.
	 * @return true if the value is found in the tree; false otherwise.
	 */
	public abstract boolean contains(E value);

	/**
	 * Removes a value from this tree if it exists.
	 * 
	 * @param value
	 *            - the value to remove
	 * @return true if the value was successfully removed; false otherwise.
	 */
	public abstract boolean remove(E value);

	/**
	 * @return number of values in the tree.
	 */
	public abstract int size();

	/**
	 * @return the internal path length of this tree.
	 */
	public abstract int internalPathLength();

	/**
	 * @return the number of internal nodes in this tree.
	 */
	public abstract int internalNodes();

	/**
	 * @return the external path length of the tree.
	 */
	public final int externalPathLength() {
		return internalPathLength() + 2 * internalNodes();
	}

	/**
	 * @return the number of leafs in the tree.
	 */
	public abstract int leafCount();

	/**
	 * Performs a "deep" equality search on the two trees. Checks if both trees
	 * have the same number of nodes, and if each node in the tree is equal to
	 * one another. Each node in one tree must be a "mirrored" visual placement
	 * of another node in the other tree.
	 * 
	 * @return true if the trees are deeply equal; false otherwise.
	 */
	@Override
	public abstract boolean equals(Object obj);
}	