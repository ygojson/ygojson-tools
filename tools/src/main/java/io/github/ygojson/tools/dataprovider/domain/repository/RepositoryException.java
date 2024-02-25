package io.github.ygojson.tools.dataprovider.domain.repository;

public class RepositoryException extends RuntimeException {

	public RepositoryException(String message) {
		super(message);
	}

	public RepositoryException(String message, Throwable cause) {
		super(message, cause);
	}
}
