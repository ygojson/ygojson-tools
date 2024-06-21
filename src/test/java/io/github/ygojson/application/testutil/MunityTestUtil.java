package io.github.ygojson.application.testutil;

import java.util.List;

import io.smallrye.mutiny.Multi;

public class MunityTestUtil {

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
	public static final <T> List<T> collectAll(final Multi<T> multi) {
		return multi.collect().asList().await().indefinitely();
	}
}
