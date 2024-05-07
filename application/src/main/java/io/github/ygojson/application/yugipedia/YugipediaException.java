package io.github.ygojson.application.yugipedia;

/**
 * Exception thrown by Yugipedia processing/provider logic.
 */
public class YugipediaException extends RuntimeException {

	public YugipediaException(String message) {
		super(message);
	}

	public YugipediaException(String message, Throwable cause) {
		super(message, cause);
	}
}
