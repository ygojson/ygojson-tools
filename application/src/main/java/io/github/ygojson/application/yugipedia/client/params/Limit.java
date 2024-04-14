package io.github.ygojson.application.yugipedia.client.params;

/**
 * Limit paramater to enforce rules.
 */
public final class Limit {

	private static final int DEFAULT = 10;
	private static final int MAX = 50;

	private final int value;

	/**
	 * Get the maximum limit to be used on the API.
	 *
	 * @return maximum limit.
	 */
	public static Limit getMax() {
		return new Limit(MAX);
	}

	/**
	 * Get the default limit to be used on the API.
	 * <br>
	 * Note that this value must be used for test/debug
	 * to prevent too many requests in short time.
	 *
	 * @return default limit.
	 */
	public static Limit getDefault() {
		return new Limit(DEFAULT);
	}

	/**
	 * Creates a new limit from a given value.
	 *
	 * @param limit value.
	 *
	 * @return the limit.
	 * @throws IllegalArgumentException if the limit is out of range
	 */
	public static Limit of(final int limit) {
		if (limit < 0 || limit > MAX) {
			throw new IllegalArgumentException("Limit out of range: 1-" + MAX);
		}
		return new Limit(limit);
	}

	private Limit(final int limit) {
		this.value = limit;
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}
}
