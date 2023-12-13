package io.github.ygojson.tools.dataprovider.impl.yugipedia.params;

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
