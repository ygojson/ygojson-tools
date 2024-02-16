package io.github.ygojson.tools.dataprovider;

import java.util.stream.Stream;

import io.github.ygojson.model.data.Set;

/**
 * Data provider to obtain the initial data for YGOJSON.
 * <br>
 * Currently, YGOJSON initial data is based on prints.
 * Each print is associated with a card and a set that
 * also might be retrieved in the initial process.
 */
public interface InitialDataProvider {

	/**
	 * Fetches the initial data as a Stream.
	 *
	 * @return the initial data.
	 */
	Stream<InitialDataEntry> fetchInitialData();

	/**
	 * Checks if this initial data-provider requires a set re-fetch.
	 * <br>
	 * When this method returns {@code true}, {@link #fetchInitialData()}
	 * MUST be called to fetch some set-data not included in the first
	 * fetch.
	 *
	 * @return {@code true} if a set re-fetch is required.
	 */
	boolean requiresSetFetch();

	/**
	 * Re-fetches the sets associated with the initial data.
	 * <br>
	 * Consumers of this method are expected to collect the set data
	 * provided by {@link #fetchInitialData()} uniquely to be provided to
	 * this method.
	 * <br>
	 * This method MUST be only called if {@link #requiresSetFetch()} is {@code true}.
	 *
	 * @return the stream with the re-fetched sets (containing missing information).
	 * @throws IllegalStateException if the {@link #requiresSetFetch()} is {@code false}.
	 */
	Stream<Set> fetchSets(Stream<Set> sets);
}
