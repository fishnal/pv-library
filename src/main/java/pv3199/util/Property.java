package pv3199.util;

import java.util.HashMap;

import static pv3199.util.PropertyListener.*;

/**
 * Holds an object and allows for functions to be called when an access or write
 * is made to the object.
 *
 * @param <T> the type of the object.
 */
public class Property<T extends Object> {
	/**
	 * A hash map of listeners for quick access (since a PropertyListener is an enumeration)
	 * and invocation of the functions associated with these listeners.
	 */
	private final HashMap<PropertyListener, EmptyFunction> listeners = new HashMap<>();
	
	/**
	 * The value of this property.
	 */
	private T value;
	
	/**
	 * Constructs a Property object with a null value.
	 */
	public Property() {
		this(null);
	}
	
	/**
	 * Constructs a Property object with a given value.
	 * @param value the given value.
	 */
	public Property(T value) {
		this.value = value;
		
		for (PropertyListener pl : PropertyListener.values()) {
			this.listeners.put(pl, EmptyFunction.DEFAULT);
		}
	}
	
	/**
	 * Gets the value of this property, invoking the function tied to the
	 * {@link PropertyListener#BEFORE_ACCESS} listener before the value is
	 * returned.
	 *
	 * @return the value of this property.
	 */
	public T getValue() {
		this.listeners.get(BEFORE_ACCESS).call();
		return this.value;
	}
	
	/**
	 * Sets the value of this property. Invokes the function tied to the
	 * {@link PropertyListener#BEFORE_WRITE} listener before the value is
	 * changed and the function tied to the {@link PropertyListener#AFTER_WRITE}
	 * listener after the value is changed.
	 *
	 * @param newValue the new value for this property.
	 */
	public void setValue(T newValue) {
		this.listeners.get(BEFORE_WRITE).call();
		this.value = newValue;
		this.listeners.get(AFTER_WRITE).call();
	}
	
	/**
	 * Sets the function tied to a {@link PropertyListener listener} for this property.
	 * If the function provided is null, then the {@link EmptyFunction#DEFAULT default function}
	 * is used.
	 *
	 * @param listenerType the property listener type.
	 * @param listenerFunction the listener function.
	 * @throws NullPointerException if the listener type is null.
	 */
	public void setListener(PropertyListener listenerType, EmptyFunction listenerFunction) throws NullPointerException {
		if (listenerType == null) {
			throw new NullPointerException();
		}
		
		if (listenerFunction == null) {
			listenerFunction = EmptyFunction.DEFAULT;
		}
		
		this.listeners.put(listenerType, listenerFunction);
	}
}
