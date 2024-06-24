package io.github.ygojson.application.testutil;

import java.time.Duration;
import java.util.List;

import io.smallrye.mutiny.Multi;

public final class MutinyTestUtil {

	private MutinyTestUtil() {
		// cannot be instantiated
	}

	/**
	 * Collect all the multi items as a list.
	 * <br>
	 * This awaits indefinitely.
	 *
	 * @param multi the multi to collect the items.
	 *
	 * @return all the items collected as a list.
	 *
	 * @param <T> the type of the items.
	 */
	public static <T> List<T> collectAll(final Multi<T> multi) {
		return multi.collect().asList().await().indefinitely();
	}

	/**
	 * Collect all the multi items as a list.
	 * <br>
	 * This awaits at most the timeout.
	 *
	 * @param multi the multi to collect the items.
	 * @param timeout the timeout.
	 *
	 * @return all the items collected as a list.
	 *
	 * @param <T> the type of the items.
	 */
	public static <T> List<T> collectAll(final Multi<T> multi, Duration timeout) {
		return multi.collect().asList().await().atMost(timeout);
	}
}
