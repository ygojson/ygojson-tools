package io.github.ygojson.tools.common;

/**
 * Runtime exception for the YGOJSON tools.
 */
public class YgoJsonToolException extends RuntimeException {

	public YgoJsonToolException(String message) {
		super(message);
	}

	public YgoJsonToolException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Exception thrown when the inputs of a tool are incorrect.
	 */
	public static final class InputException extends YgoJsonToolException {

		public InputException(String message) {
			super(message);
		}

		public InputException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
