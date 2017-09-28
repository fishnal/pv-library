package pv3199.math.structures;

/**
 * Indicates an illegal matrix.
 */
public class IllegalMatrixException extends IllegalArgumentException {
	
	public IllegalMatrixException() {
	}
	
	public IllegalMatrixException(String arg0) {
		super(arg0);
	}
	
	public IllegalMatrixException(Throwable arg0) {
		super(arg0);
	}
	
	public IllegalMatrixException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}
