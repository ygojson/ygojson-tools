package io.github.ygojson.tools.dataprovider;

import java.time.ZonedDateTime;
import java.util.stream.Stream;

import io.github.ygojson.model.data.Set;

/**
 * Data provider for initialize/update YGOJSON {@link PrintData}.
 * <br>
 * The data provider should be used as following:
 * <ul>
 *     <li>Fetch the data (all or partial)</li>
 *     <li>
 *         Collect the data  ensuring that the models are uniquely stored
 *         (depends on the caller - i.e., java-collections or database).
 *     </li>
 *     <li>
 *         Call the {@code finalize*} methods (if the {@code shouldFinalize*}
 *         returns {@code true}) to ensure the completion of those models.
 *     </li>
 *     <li>
 *         Update storage with the returned finalized data.
 *     </li>
 * </ul>
 *
 * @see PrintData
 */
public interface DataProvider {

	/**
	 * Fetches lazily the data.
	 * <br>
	 * This method should be used to get the initial data.
	 * Limiting the stream might not provide the latest data always.
	 *
	 * @return lazy stream of data.
	 */
	Stream<PrintData> fetchData();

	/**
	 * Fetches lazily the data up the last updated time.
	 * <br>
	 * This method should be used to update the data.
	 * Using this method might not provide all the data.
	 *
	 * @param lastUpdated the time of the last update.
	 * @return lazy stream of data.
	 */
	Stream<PrintData> fetchData(ZonedDateTime lastUpdated);

	/**
	 * Checks if the sets should be finalized.
	 *
	 * @return {@code true} if the sets should be finalized; {@code false} otherwise.
	 */
	boolean shouldFinalizeSets();

	/**
	 * Finalize fetching of data.
	 * <br>
	 * This method might enforce the iteration over the full stream.
	 *
	 * @param data sets to finalize
	 * @return finalized sets
	 * @throws IllegalStateException if {@code shouldFinalizeSets()} returns {@code false}.
	 */
	Stream<Set> finalizeSets(Stream<Set> data) throws IllegalStateException;

}
