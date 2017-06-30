package pvlib.util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A "abstract" implementation of {@link DataStructure}. Scope is package-private as the underlying data is
 * simply an {@link ArrayList}. This is used for when an abstract data structure is needed rather than, say
 * a LinkedList, to store data and utilize sorting methods from {@link SortMethod}.
 * @param <E> - the type that this data structure will hold.
 */
class AbstractStructure<E> implements DataStructure<E> {
	private ArrayList<E> data = new ArrayList<>();
	
	AbstractStructure() {
	}
	
	AbstractStructure(Collection<E> data) {
		this.data.addAll(data);
	}

	@Override
	public void add(E element) {
		this.data.add(element);
	}

	@Override
	public E get(int index) {
		return this.data.get(index);
	}

	@Override
	public void set(int index, E newValue) {
		this.data.set(index, newValue);
	}

	@Override
	public void remove(int index) {
		this.data.remove(index);
	}

	@Override
	public boolean remove(E element) {
		return this.data.remove(element);
	}

	@Override
	public void swap(int first, int second) {
		E temp = this.data.get(first);
		this.data.set(first, this.data.get(second));
		this.data.set(second, temp);
	}

	@Override
	public int indexOf(E element) {
		return this.data.indexOf(element);
	}

	@Override
	public int size() {
		return this.data.size();
	}

	@Override
	public AbstractStructure<E> split(int from, int to) {
		return new AbstractStructure<>(this.data.subList(from, to));
	}

	@Override
	public AbstractStructure<E> clone() {
		return new AbstractStructure<>(this.data);
	}

}
