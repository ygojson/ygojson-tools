package io.github.ygojson.application.logic.mapper;

/**
 * Exception that should be thrown when mapping fails.
 * <br>
 * This would be important cause these exceptions would be catched
 * and logged to prevent the application to crash, but enable to
 * investigate if something was not considered.
 */
public class MappingException extends RuntimeException {

	public MappingException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public MappingException(final String message) {
		super(message);
	}

	public MappingException(final Throwable cause) {
		super(cause);
	}
}
