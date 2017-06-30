package pvlib.util;

public class BinaryTree<E extends Comparable<E>> extends Tree<E> {
	protected class BinaryNode extends Node {
		/**
		 * Right child.
		 */
		protected BinaryNode right;

		/**
		 * Left child.
		 */
		protected BinaryNode left;

		/**
		 * Constructs a BinaryNode given a parent and value.
		 * 
		 * @param parent
		 *            - the parent of this BinaryNode; if null, then this is
		 *            considered the root.
		 * @param value
		 *            - the value.
		 */
		BinaryNode(Node parent, E value) {
			super(parent, value);
		}

		/**
		 * @return true if both right and left nodes of this node are null
		 */
		public boolean isLeaf() { return right == null && left == null; }

		/**
		 * @return {@inheritDoc}; and the children associated with this binary
		 *         node.
		 */
		@Override
		public String toString() {
			StringBuilder s = new StringBuilder(super.toString());

			s.append(String.format(",left=%s", left == null ? null : left.value));
			s.append(String.format(",right=%s", right == null ? null : right.value));

			return s.toString();
		}
	}

	/**
	 * Constructs a BinaryTree with a null root.
	 */
	public BinaryTree() {
		root = null;
	}

	/**
	 * Constructs a BinaryTree, initializing it's root with a given value.
	 * 
	 * @param initialRoot
	 *            - value of the root.
	 */
	public BinaryTree(E initialRoot) {
		root = new BinaryNode(null, initialRoot);
	}

	/**
	 * Inserts a {@linkplain BinaryNode} given a value. If the root of this tree
	 * is null, then the BinaryNode to be inserted will become the root.
	 */
	@Override
	public void add(E value) {
		if (root == null)
			root = new BinaryNode(null, value);
		else {

			BinaryNode node = get(value, (BinaryNode) root);
			if (node == null) {

				BinaryNode parent = bestParent(value, (BinaryNode) root);
				node = new BinaryNode(parent, value);
				if (value.compareTo(parent.value) < 0)
					parent.left = node;
				else
					parent.right = node;
			} else
				node.value = value;
		}
	}

	public void add(E... values) {
		for (E e : values) {
			this.add(e);
		}
	}
	
	@Override
	public boolean contains(E value) {
		return get(value, (BinaryNode) root) != null;
	}

	@Override
	public boolean remove(E value) {
		// TODO make this more efficient
		BinaryNode node = get(value, (BinaryNode) root);
		if (node == null)
			return false;

		// find the least value of the right hand side tree of this node
		// if right does not exist, then greatest value of left hand side of this node
		// if left does not exist, just null and return

		BinaryNode currNode;

		if (node.right != null) {
			currNode = node.right;

			while (currNode.left != null) {
				currNode = currNode.left;
			}
			// leftmost node on right hand side of tree found
			// unlink it from the parent


			BinaryNode currNodeParent = (BinaryNode) currNode.parent;

			BinaryNode nodeParent = (BinaryNode) node.parent;

			if (currNodeParent.right == currNode)
				currNodeParent.right = null;
			else
				currNodeParent.left = null;

			if (nodeParent.left == node)
				nodeParent.left = currNode;
			else
				nodeParent.right = currNode;
			currNode.parent = node.parent;
			// !!!!!
			// ((BinaryNode) currNode.parent).right = currNode;
			node.right.parent = currNode;
			if (node.left != null)
				node.left.parent = currNode;
			else
				node.left = currNode.left;

			if (currNode != node.right)
				currNode.right = node.right;
			if (currNode != node.left)
				currNode.left = node.left;
		} else if (node.left != null) {
			currNode = node.left;

			BinaryNode currNodeParent = (BinaryNode) currNode.parent;

			BinaryNode nodeParent = (BinaryNode) node.parent;

			if (currNodeParent.right == currNode)
				currNodeParent.right = null;
			else
				currNodeParent.left = null;

			if (nodeParent.left == node)
				nodeParent.left = currNode;
			else
				nodeParent.right = currNode;
			currNode.parent = node.parent;
			// !!!!!
			// currNodeParent.left = currNode;
			node.right = currNode.right;
			node.left.parent = currNode;

			if (currNode != node.right)
				currNode.right = node.right;
		} else {
			BinaryNode nodeParent = (BinaryNode) node.parent;

			if (nodeParent.right == node)
				nodeParent.right = null;
			else
				nodeParent.left = null;
		}

		return true;
	}

	/**
	 * Recursively searches for a binary node based on its key.
	 * 
	 * @param value
	 *            - the value of the binary node to look for.
	 * @param node
	 *            - the binary node that is currently being evaluated.
	 * @return null if there exists no such binary node in the tree whose key
	 *         matches the one passed in; otherwise the binary node instance
	 *         whose key does match the one passed in.
	 */
	private BinaryNode get(E value, BinaryNode node) {
		if (node == null || value.compareTo(node.value) == 0) {
			return node;
		} else if (value.compareTo(node.value) > 0) {
			return get(value, node.right);
		} else {
			return get(value, node.left);
		}
	}

	/**
	 * Recursively searches through the binary tree for the best possible parent
	 * to hold a key.
	 * 
	 * @param value
	 *            - the key for the parent to hold.
	 * @param node
	 *            - the current binary node that is being evaluated.
	 * @return the best possible binary node to hold a child whose key is the
	 *         one passed in.
	 */
	private BinaryNode bestParent(E value, BinaryNode node) {
		int compare = value.compareTo(node.value);
		// if this entire if statement is false, then node has no null children
		if (node.right == null && node.left == null || node.right == null && compare > 0
				|| node.left == null && compare < 0) {
			return node;
		} else if (compare < 0) {
			return bestParent(value, node.left);
		} else {
			return bestParent(value, node.right);
		}
	}

	@Override
	public int size() {
		return nodeCount0((BinaryNode) root);
	}

	/**
	 * Helper method for {@link #size()}. Recursively traverses the tree
	 * and counts the nodes as it does.
	 * 
	 * @param n
	 *            - the current node.
	 * @return the amount of nodes counted so far.
	 */
	private int nodeCount0(BinaryNode n) {
		if (n == null)
			return 0;

		int rc = nodeCount0(n.right);
		int lc = nodeCount0(n.left);
		return 1 + rc + lc;
	}

	@Override
	public int internalPathLength() {
		return internalPathLength0((BinaryNode) root, 0);
	}

	/**
	 * Recursively computes the internal path length of a node.
	 * @param node - the node.
	 * @param level - the current level in the tree.
	 * @return the internal path length from the current node.
	 */
	private int internalPathLength0(BinaryNode node, int level) {
		return node != null ? level + internalPathLength0(node.left, level + 1) + internalPathLength0(node.right, level + 1) : 0;
	}

	@Override
	public int internalNodes() {
		return internalNodes0((BinaryNode) root);
	}

	/**
	 * Recursively computes the number of internal nodes from a node.
	 * @param node - the node.
	 * @return the number of internal nodes from the current node.
	 */
	private int internalNodes0(BinaryNode node) {
		return node == null || node.isLeaf() ? 0 : 1 + internalNodes0(node.left) + internalNodes0(node.right);
	}

	@Override
	public int leafCount() {
		return leafCount0((BinaryNode) root);
	}

	/**
	 * Recursively searches for leaves in a node.
	 * @param node - the current node.
	 * @return the number of leaves in the current node.
	 */
	private int leafCount0(BinaryNode node) {
		if (node == null) {
			return 0;
		}

		return node.isLeaf() ? 1 : leafCount0(node.left) + leafCount0(node.right);
	}

	@Override
	public boolean equals(Object obj) {
		try {
			BinaryTree<E> bt = (BinaryTree<E>) obj;

			if (this.size() != bt.size()) {
				return false;
			}

			BinaryNode n1 = (BinaryNode) root;
			BinaryNode n2 = (BinaryNode) bt.root;

			return equals0(n1, n2);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Performs a recursive "deep" equality search on two nodes (each of which
	 * should belong to separate trees of the same generic argument types). The
	 * first comparison is if <i>both</i> children of <i>both</i> nodes are
	 * null, in which case, the nodes themselves are compared. If not, then a
	 * bitwise XOR operation is performed on the conditions of the left children
	 * and right children of both nodes being null. If either operation results
	 * in true, then false is returned (if the right child of n1 is null but the
	 * right child of n2 isn't, then there's no reliable way to compare them,
	 * and as such invalidates the equality of the comparing trees). If false,
	 * then a recursive approach is taken for comparing the children. Starting
	 * with the right child and then moving towards the left child. If both of
	 * these comparisons result in true, then the nodes themselves are finally
	 * compared.
	 * 
	 * @param n1
	 *            - the node from the first comparing tree.
	 * @param n2
	 *            - the node from the second comparing tree.
	 * @return true if both trees are completely equal in terms of their nodes;
	 *         false otherwise.
	 */
	private boolean equals0(BinaryNode n1, BinaryNode n2) {
		boolean eq = n1.equals(n2);

		// if both children of n1 and n2 are null, then compare n1 and n2
		if (n1.right == null && n2.right == null && n1.left == null && n2.left == null) {
			return eq;
		}

		// if n1.right == null XOR n2.right == null (same thing with left
		// child), then return false so if n1.right is null but n2.right isn't,
		// then we can't make comparisons and must return false (these nodes are
		// no longer equal, and thus the two trees that n1 and n2 belong to are
		// no longer equal
		if ((n1.right == null ^ n2.right == null) || (n1.left == null ^ n2.left == null)) {
			return false;
		}

		// now if one child is null from one node, then the same-sided child on
		// the other node is null as well
		if (n1.right != null && !equals0(n1.right, n2.right) || n1.left != null && !equals0(n1.left, n2.left)) {
			return false;
		}

		return eq;
	}

	/**
	 * @return the root, size, internal and external path length, and number of internal nodes and leaves.
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		// add basic info of root
		s.append(String.format("[root={%s}]", root == null ? "empty" : root));
		// add number of children
		s.append(String.format(",[size=%d]", size()));
		// add IPL, EPL, and internal nodes
		int ipl = internalPathLength();
		int in = internalNodes();
		int epl = ipl + 2 * in;
		s.append(String.format(",[internalPathLength=%d],[externalPathLength=%d],[internalNodes=%d]", ipl, epl, in));
		// add add number of leaves
		int lc = leafCount();
		s.append(String.format(",[leaves=%d]", lc));
		return s.toString();
	}
}
