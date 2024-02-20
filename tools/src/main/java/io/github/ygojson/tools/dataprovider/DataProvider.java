package io.github.ygojson.tools.dataprovider;

import java.util.stream.Stream;

import io.github.ygojson.model.data.Set;

/**
 * Data provider for initialize/update YGOJSON data.
 */
public interface DataProvider {
	/**
	 * Fetches lazily the set data.
	 * <br>
	 * This method should be used to get the initial data.
	 * Limiting the stream might not provide the latest data always.
	 *
	 * @return lazy stream of data.
	 * @throws DataProviderException if fetching fails in an unrecoverable way
	 */
	Stream<Set> fetchSets() throws DataProviderException;
}
