package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params;

import java.util.List;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Helper method to join properly multiple parameters with pipes.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class PipeSeparated {

	private final List<String> values;

	public PipeSeparated(final String... values) {
		if (values == null || values.length >= 500) {
			throw new IllegalArgumentException("Titles should be > 1 and <= 500");
		}
		this.values = List.of(values);
	}

	public String toString() {
		return String.join("|", values);
	}
}
