package io.github.ygojson.tools.dataprovider.impl.yugipedia;

/**
 * Exception thrown by Yugipedia parsing/processing logic.
 */
public class YugipediaException extends RuntimeException {

	public YugipediaException(String message) {
		super(message);
	}

	public YugipediaException(String message, Throwable cause) {
		super(message, cause);
	}
}
