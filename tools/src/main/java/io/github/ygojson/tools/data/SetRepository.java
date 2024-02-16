package io.github.ygojson.tools.data;

import java.util.stream.Stream;

import io.github.ygojson.model.data.Set;

/**
 * Repository for {@link Set}.
 */
public interface SetRepository {

	/**
	 * Saves the set into the repository.
	 *
	 * @param set set to save.
	 */
	void save(final Set set);

	/**
	 * Retrieves all sets on the repository.
	 *
	 * @return a stream of sets stored in the repository.
	 */
	Stream<Set> findAll();
}
