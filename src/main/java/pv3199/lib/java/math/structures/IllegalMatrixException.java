package pv3199.lib.java.math.structures;

/**
 * Indicates an illegal matrix.
 */
public class IllegalMatrixException extends RuntimeException {

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

	public IllegalMatrixException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
