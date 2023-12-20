package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params;

import java.util.List;

/**
 * Helper method to join properly multiple parameters with pipes.
 */
public final class PipeSeparated {

	private final List<String> values;

	private PipeSeparated(final List<String> values) {
		this.values = values;
	}

	/**
	 * Creates a multi-value pipe-separated parameter.
	 *
	 * @param values values
	 *
	 * @return new instance witth the pipe separated values
	 * @throws IllegalArgumentException if values are not between the valid range
	 */
	public static PipeSeparated of(final String... values) {
		if (values == null || values.length >= 500) {
			throw new IllegalArgumentException("Titles should be > 1 and <= 500");
		}
		return new PipeSeparated(List.of(values));
	}

	public String toString() {
		return String.join("|", values);
	}
}
