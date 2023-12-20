package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params;

/**
 * Limit paramater to enforce rules.
 */
public final class Limit {

	private static final int DEFAULT = 10;
	private static final int MAX = 50;

	private final int value;

	public static Limit getMax() {
		return new Limit(MAX);
	}

	public static Limit getDefault() {
		return new Limit(DEFAULT);
	}

	public Limit(final int limit) {
		if (limit < 0 || limit > MAX) {
			throw new IllegalArgumentException("Limit out of range: 1-" + MAX);
		}
		this.value = limit;
	}

	public String toString() {
		return Integer.toString(value);
	}
}
