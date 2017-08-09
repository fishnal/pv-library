package pv3199.lib.java.util;

public class LinkedList<E> implements DataStructure<E> {
	private Link root;
	private Link end;
	private int size;
	private Link currLink;
	
	public LinkedList(E... elements) {
		for (E element : elements) this.add(element);
	}
	
	@Override
	public void add(E element) {
		if (this.root == null) this.set(this.size, element);
		else this.end = this.end.next = new Link(element, this.end, null);
		this.size++;
	}
	
	@Override
	public E get(final int index) {
		if (index < 0 || index > this.size) throw new IndexOutOfBoundsException();
		
		return get0(index).value;
		
	}
	
	private Link get0(final int index) {
		this.currLink = root;
		for (int i = 0; i < this.size; i++, this.currLink = this.currLink.next)
			if (i == index) return this.currLink;
		
		return null;
	}
	
	@Override
	public void set(final int index, E newValue) {
		if (index < 0 || index > this.size) throw new IndexOutOfBoundsException();
		
		if (this.root == null) {
			this.end = this.root = new Link(newValue, null, null);
			return;
		}
		
		if (index == this.size) {
			this.add(newValue);
			return;
		}
		
		Link dl = get0(index);
		
		dl.value = newValue;
	}
	
	@Override
	public void clear() {
		this.root = this.end = null;
		this.size = 0;
	}
	
	@Override
	public void remove(int index) {
		if (index < 0 || index > this.size) throw new IndexOutOfBoundsException();
		
		Link dl = get0(index);
		
		Link dlPrev = dl.prev;
		Link dlNext = dl.next;
		
		if (dlPrev != null) dlPrev.next = dlNext;
		
		if (dlNext != null) dlNext.prev = dlPrev;
		
		if (index == 0) root = dlNext;
		
		this.size--;
	}
	
	@Override
	public boolean remove(E element) {
		int index = indexOf(element);
		
		if (index == -1) return false;
		
		this.remove(index);
		
		return true;
	}
	
	@Override
	public int indexOf(E element) {
		Link dl = root;
		
		for (int i = 0; i < this.size; i++, dl = dl.next) {
			if (dl.value.equals(element) || dl.value == element) return i;
		}
		
		return -1;
	}
	
	@Override
	public int size() {
		return this.size;
	}
	
	@Override
	public void swap(int first, int second) {
		if (first < 0 || first >= this.size || second < 0 || second >= this.size) throw new IndexOutOfBoundsException();
		
		Link firstLink = get0(first);
		Link secondLink = get0(second);
		
		E tempValue = firstLink.value;
		firstLink.value = secondLink.value;
		secondLink.value = tempValue;
	}
	
	@Override
	public LinkedList<E> split(int from, int to) {
		LinkedList<E> split = new LinkedList<E>();
		
		for (int i = from; i < to; i++)
			split.add(this.get(i));
		
		return split;
	}
	
	@Override
	public LinkedList<E> clone() {
		LinkedList<E> clone = new LinkedList<>();
		
		for (int i = 0; i < this.size; i++)
			clone.add(this.get(i));
		
		return clone;
	}
	
	@Override
	public boolean equals(Object obj) {
		LinkedList<E> dll = (LinkedList<E>) obj;
		
		if (this.size != dll.size) return false;
		
		Link thisLink = this.root;
		Link dllLink = dll.root;
		for (int i = 0; i < this.size; i++, thisLink = thisLink.next, dllLink = dllLink.next)
			if (!thisLink.value.equals(dllLink.value)) return false;
		
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		
		Link dl = root;
		
		while (dl != null) {
			s.append(dl.value).append(" ");
			dl = dl.next;
		}
		
		return s.toString().trim();
	}
	
	private class Link {
		private E value;
		private Link prev;
		private Link next;
		
		Link(E value, Link prev, Link next) {
			this.value = value;
			this.prev = prev;
			this.next = next;
		}
		
		@Override
		public String toString() {
			String s = "";
			
			if (prev != null) s += prev.value + " -> ";
			else s += "null -> ";
			
			s += this.value + " -> ";
			
			if (next != null) s += next.value;
			else s += "null";
			
			return s;
		}
	}
}
