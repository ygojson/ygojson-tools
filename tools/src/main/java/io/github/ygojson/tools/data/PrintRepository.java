package io.github.ygojson.tools.data;

import java.util.stream.Stream;

import io.github.ygojson.model.data.Print;

/**
 * Repository for {@link Print}.
 */
public interface PrintRepository {


	/**
	 * Saves the print into the repository.
	 *
	 * @param print print to save.
	 *
	 *                 @throws IllegalArgumentException if the print is already present in the repository.
	 */
	void save(final Print print);

	/**
	 * Retrieves all prints on the repository.
	 *
	 * @return a stream of prints stored in the repository.
	 */
	Stream<Print> findAll();
}
