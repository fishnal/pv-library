package pv3199.math.structures;

/**
 * Indicates that a vector in some operation was illegal.
 */
public class IllegalVectorException extends IllegalArgumentException {
	public IllegalVectorException() {
		super();
	}
	
	public IllegalVectorException(String s) {
		super(s);
	}
	
	public IllegalVectorException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public IllegalVectorException(Throwable cause) {
		super(cause);
	}
}
