package pv3199.util;

/**
 * Enumeration of valid {@link Property} listener types.
 */
public enum PropertyListener {
	/**
	 * Listener for before the property returns its value.
	 */
	BEFORE_ACCESS,
	
	/**
	 * Listener for before the property's value changes.
	 */
	BEFORE_WRITE,
	
	/**
	 * Listener for after the property's value changes.
	 */
	AFTER_WRITE
}
