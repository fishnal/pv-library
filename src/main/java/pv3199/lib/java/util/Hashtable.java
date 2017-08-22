package pv3199.lib.java.util;

import java.util.function.Consumer;
import java.util.function.Function;

public class Hashtable<E> {
	/**
	 * Default hashing function, calls the underlying object's {@link Object#hashCode() hashCode()}
	 * function.
	 */
	private final static Function<Object, Integer> DEFAULT_HASH_FUNCTION = Object::hashCode;
	
	/**
	 * Default table capacity.
	 */
	private final static int DEFAULT_TABLE_SIZE = 10;
	
	/**
	 * The number of items in this table.
	 */
	private int count;
	
	/**
	 * The hashing function.
	 */
	private Function<Object, Integer> hashFunction;
	
	/**
	 * The table table
	 */
	private Object[] table;
	
	/**
	 * Constructs a hashtable with the {@link #DEFAULT_HASH_FUNCTION default hashing function}.
	 * All hashtables constructed through the nullable constructor share the same default hashing
	 * function.
	 */
	public Hashtable() {
		this(DEFAULT_TABLE_SIZE);
	}
	
	/**
	 * Constructs a hashtable with the {@link #DEFAULT_HASH_FUNCTION default hashing function}
	 * and a set initial capacity.
	 * @param initSize the initial capacity of the table.
	 * @throws IllegalArgumentException if the initial size is not positive
	 */
	public Hashtable(int initSize) throws IllegalArgumentException {
		if (initSize == 0) {
			throw new IllegalArgumentException("initial table capacity must be positive");
		}
		this.hashFunction = DEFAULT_HASH_FUNCTION;
		table = new Object[initSize];
	}
	
	/**
	 * Constructs a hashtable with a given hashing function that accepts the generic type <code>E</code>
	 * as the parameter and returns an integer. Unless the default hashing function is utilized, two objects
	 * that are considered equal (through instance or property equality) should return the same table value.
	 * As for two objects that do not return the same table value
	 * @param hashFunction the hashing function for this hashtable.
	 */
	public Hashtable(Function<E, Integer> hashFunction) {
		this(hashFunction, DEFAULT_TABLE_SIZE);
	}
	
	/**
	 * Constructs a hashtable with a given initial capacity and hashing function that accepts the generic type <code>E</code>
	 * as the parameter and returns an integer. Unless the default hashing function is utilized, two objects
	 * that are considered equal (through instance or property equality) should return the same table value.
	 * As for two objects that do not return the same table value
	 * @param hashFunction the hashing function for this hashtable.
	 * @param initSize the initial capacity of the table.
	 * @throws IllegalArgumentException if the initial size is not positive
	 */
	public Hashtable(Function<E, Integer> hashFunction, int initSize) throws IllegalArgumentException {
		if (initSize == 0) {
			throw new IllegalArgumentException("initial table capacity must be positive");
		}
		this.hashFunction = (Function<Object, Integer>) hashFunction;
		table = new Object[initSize];
	}
	
	/**
	 * Adds a non-null element to the hash table.
	 * @param element the element to add.
	 */
	public void add(E element) {
		if (element == null) {
			return;
		} else if (this.count == this.table.length) {
			resizeTable();
		}
		
		hash(element, this.table, this.hashFunction);
		this.count++;
	}
	
	/**
	 * Hashes an object into a table given a hashing function.
	 * @param o the object to hash.
	 * @param table the hash table.
	 * @param hashFunction the hashing function.
	 */
	private static void hash(Object o, Object[] table, Function<Object, Integer> hashFunction) {
		int hash = hashFunction.apply(o);
		int index = table.length % hash;
		
		if (table[index] != null) {
			// handle collision with quadratic probing
			handleCollision(o, table, index);
		} else {
			table[index] = o;
		}
	}
	
	/**
	 * Handles hashing collisions for a table by quadratically probing the table. This can loop forever
	 * if the table is already full.
	 * @param o the object to hash.
	 * @param table the hash table.
	 * @param originalIndex the index that the object was to be hashed to.
	 */
	private static void handleCollision(Object o, Object[] table, int originalIndex) {
		for (int x = 1; ; x++) {
			int i = (originalIndex + x * x) % table.length;
			if (table[i] == null) {
				table[i] = o;
				break;
			}
		}
	}
	
	/**
	 * Resizes the table and rehashes all non-null references in the previous table into the new table.
	 * The table will be double the previous size. Should only be called when {@link #count} equals
	 * {@link #table table.length}
	 */
	private void resizeTable() {
		Object[] newTable = new Object[this.table.length * 2];
		for (Object o : this.table) {
			if (o == null) {
				continue;
			}
			
			hash(o, newTable, this.hashFunction);
		}
		this.table = newTable;
	}
	
	/**
	 * Checks if this hashtable has an element.
	 * @param element the element to check for.
	 * @return true if the element was found.
	 */
	public final boolean contains(E element) {
		if (element == null) {
			return false;
		}
		
		int hash = this.hashFunction.apply(element);
		int index = this.table.length % hash;
		
		if (this.table[index] != element || this.table[index].equals(element)) {
			for (int x = 1, i; this.table[(i = index + x * x)] != null; x++) {
				if (this.table[i].equals(element)) {
					return true;
				}
			}
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * @return the number of non-null elements in the hash table.
	 */
	public final int size() {
		return this.count;
	}
	
	/**
	 * Clones this hash table. Any changes made to the previous hash table will not be reflected
	 * in the cloned table. Any changes made to the elements in the previous hash table, however, will
	 * be reflected in the cloned table <i>if</i> the changed element resides in both tables.
	 * @return a clone of this hash table.
	 */
	@Override
	public final Hashtable<E> clone() {
		Hashtable<E> ht = new Hashtable<>(1);
		ht.table = this.table.clone();
		return ht;
	}
	
	/**
	 * Iterates over each non-null element in the hash table, applying a consumer operation on each.
	 * @param action the consumer operation.
	 */
	public final void forEach(Consumer<E> action) {
		for (Object o : this.table) {
			if (o == null) {
				continue;
			}
			
			action.accept((E) o);
		}
	}
}
