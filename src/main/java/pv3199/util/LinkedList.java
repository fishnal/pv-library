package pv3199.util;

/**
 * Elements are stored in a double linked list structure. Provides methods
 * defined in the {@link DataStructure} interface.
 *
 * @param <E> the generic type of the elements to store.
 */
public class LinkedList<E> implements DataStructure<E> {
	/**
	 * Head of the list.
	 */
	private Link head;
	
	/**
	 * Tail of the list.
	 */
	private Link tail;
	
	/**
	 * Size of the list.
	 */
	private int size;
	
	/**
	 * Current link in the list. Used for efficient traversal.
	 */
	private Link currLink;
	
	/**
	 * Constructs a LinkedList from a set of elements, if any. If null is provided
	 * as the arbitrary amount of elements, then it is ignored (this constructor is
	 * nullable).
	 *
	 * @param elements the initial set of elements.
	 */
	public LinkedList(E... elements) {
		if (elements != null) {
			for (E element : elements) {
				this.add(element);
			}
		}
	}
	
	@Override
	public void add(E element) {
		if (this.head == null) {
			this.set(this.size, element);
		} else {
			this.tail = this.tail.next = new Link(element, this.tail, null);
		}
		
		this.size++;
	}
	
	@Override
	public E get(final int index) {
		if (index < 0 || index > this.size) {
			throw new IndexOutOfBoundsException();
		}
		
		return get0(index).value;
		
	}
	
	private Link get0(final int index) {
		this.currLink = head;
		
		for (int i = 0; i < this.size; i++, this.currLink = this.currLink.next) {
			if (i == index) {
				return this.currLink;
			}
		}
		
		return null;
	}
	
	@Override
	public void set(final int index, E newValue) {
		if (index < 0 || index > this.size) {
			throw new IndexOutOfBoundsException();
		} else if (this.head == null) {
			this.tail = this.head = new Link(newValue, null, null);
			return;
		} else if (index == this.size) {
			this.add(newValue);
			return;
		}
		
		Link dl = get0(index);
		
		dl.value = newValue;
	}
	
	@Override
	public void clear() {
		this.head = this.tail = null;
		this.size = 0;
	}
	
	@Override
	public void remove(int index) {
		if (index < 0 || index > this.size) {
			throw new IndexOutOfBoundsException();
		}
		
		Link dl = get0(index);
		
		Link dlPrev = dl.prev;
		Link dlNext = dl.next;
		
		if (dlPrev != null) {
			dlPrev.next = dlNext;
		}
		
		if (dlNext != null) {
			dlNext.prev = dlPrev;
		}
		
		if (index == 0) {
			head = dlNext;
		}
		
		this.size--;
	}
	
	@Override
	public boolean remove(E element) {
		int index = indexOf(element);
		
		if (index == -1) {
			return false;
		}
		
		this.remove(index);
		
		return true;
	}
	
	@Override
	public int indexOf(E element) {
		Link dl = head;
		
		for (int i = 0; i < this.size; i++, dl = dl.next) {
			if (dl.value.equals(element) || dl.value == element) {
				return i;
			}
		}
		
		return -1;
	}
	
	@Override
	public int size() {
		return this.size;
	}
	
	@Override
	public void swap(int first, int second) {
		if (first < 0 || first >= this.size || second < 0 || second >= this.size) {
			throw new IndexOutOfBoundsException();
		}
		
		Link firstLink = get0(first);
		Link secondLink = get0(second);
		
		E tempValue = firstLink.value;
		firstLink.value = secondLink.value;
		secondLink.value = tempValue;
	}
	
	@Override
	public LinkedList<E> split(int from, int to) {
		LinkedList<E> split = new LinkedList<E>();
		
		for (int i = from; i < to; i++) {
			split.add(this.get(i));
		}
		
		return split;
	}
	
	@Override
	public LinkedList<E> clone() {
		LinkedList<E> clone = new LinkedList<>();
		
		for (int i = 0; i < this.size; i++) {
			clone.add(this.get(i));
		}
		
		return clone;
	}
	
	@Override
	public boolean equals(Object obj) {
		LinkedList<E> dll = (LinkedList<E>) obj;
		
		if (this.size != dll.size) {
			return false;
		}
		
		Link thisLink = this.head;
		Link dllLink = dll.head;
		for (int i = 0; i < this.size; i++, thisLink = thisLink.next, dllLink = dllLink.next) {
			if (!thisLink.value.equals(dllLink.value)) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		
		Link dl = head;
		
		while (dl != null) {
			s.append(dl.value).append(" ");
			dl = dl.next;
		}
		
		return s.toString().trim();
	}
	
	/**
	 * Doubly linked list node.
	 */
	private class Link {
		/**
		 * Value of node.
		 */
		private E value;
		
		/**
		 * Previous link.
		 */
		private Link prev;
		
		/**
		 * Next link.
		 */
		private Link next;
		
		/**
		 * Constructs a Link given a value and its previous and next
		 * link references.
		 * @param value the value.
		 * @param prev the previous link.
		 * @param next the next link.
		 */
		Link(E value, Link prev, Link next) {
			this.value = value;
			this.prev = prev;
			this.next = next;
		}
		
		/**
		 * @return previous link's value pointing to this value pointing to
		 * next link's value.
		 */
		@Override
		public String toString() {
			String s = "";
			
			if (prev != null) {
				s += prev.value + " -> ";
			} else {
				s += "null -> ";
			}
			
			s += this.value + " -> ";
			
			if (next != null) {
				s += next.value;
			} else {
				s += "null";
			}
			
			return s;
		}
	}
}
