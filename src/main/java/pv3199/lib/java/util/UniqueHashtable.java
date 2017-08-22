package pv3199.lib.java.util;

import java.util.function.Function;

public class UniqueHashtable<E> extends Hashtable<E> {
	public UniqueHashtable() {
		super();
	}
	
	public UniqueHashtable(int initSize) {
		super(initSize);
	}
	
	public UniqueHashtable(Function<E, Integer> hashFunction) {
		super(hashFunction);
	}
	
	public UniqueHashtable(Function<E, Integer> hashFunction, int initSize) {
		super(hashFunction, initSize);
	}
	
	public void add(E element) {
		if (this.contains(element)) {
			return;
		}
		
		super.add(element);
	}
}
