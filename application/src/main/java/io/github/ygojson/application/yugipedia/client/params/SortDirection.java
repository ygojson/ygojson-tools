package io.github.ygojson.application.yugipedia.client.params;

/**
 * Contains only sort directions used on YGOJSON.
 */
public enum SortDirection {
	OLDER("older"),
	NEWER("newer");

	private final String value;

	SortDirection(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
