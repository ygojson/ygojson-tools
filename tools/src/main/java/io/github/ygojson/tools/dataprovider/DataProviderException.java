package io.github.ygojson.tools.dataprovider;

/**
 * Exception while getting data from provider.
 */
public class DataProviderException extends RuntimeException {

	public DataProviderException(String message) {
		super(message);
	}

	public DataProviderException(String message, Throwable cause) {
		super(message, cause);
	}
}
