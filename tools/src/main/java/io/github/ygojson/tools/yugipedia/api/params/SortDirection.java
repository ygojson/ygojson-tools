package io.github.ygojson.tools.yugipedia.api.params;

import lombok.RequiredArgsConstructor;

/**
 * Contains only sort directions used on YGOJSON.
 */
@RequiredArgsConstructor
public enum SortDirection {
	OLDER("older"),
	NEWER("newer");

	private final String value;

	@Override
	public String toString() {
		return value;
	}
}
