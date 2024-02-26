package io.github.ygojson.tools.dataprovider.domain.repository.set;

import io.github.ygojson.model.data.Set;
import io.github.ygojson.tools.dataprovider.domain.repository.RepositoryException;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface SetRepository extends AutoCloseable {

	long count();

	/**
	 * Save a new set in the repository.
	 *
	 * @param set saves the set in the repository.
	 *
	 * @throws RepositoryException if the same set if the {@link Set#getId()}
	 *                             is {@code null} or already present in the repository.
	 */
	void save(SetEntity entity) throws RepositoryException;

	/**
	 * Search all the sets in the repository.
	 *
	 * @return all the sets
	 */
	Stream<SetEntity> findAll();

	Optional<SetEntity> findById(UUID id);

	/**
	 * Search a set by name.
	 * <br>
	 * Includes {@link Set#getName()} and {@link Set#getNameAlt()}.
	 *
	 * @param name the name to search for.
	 *
	 * @return set if found; {@link Optional#empty()} otherwise.
	 */
	Optional<SetEntity> findByName(String name);

	/**
	 * Search a set by set-code.
	 * <br>
	 * Includes {@link Set#getSetCode()} and {@link Set#getSetCodeAlt()}.
	 *
	 * @param code the set-code to search for.
	 *
	 * @return set if found; {@link Optional#empty()} otherwise.
	 */
	Optional<SetEntity> findBySetCode(String code);

}
