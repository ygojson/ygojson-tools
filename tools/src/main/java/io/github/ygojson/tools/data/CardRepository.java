package io.github.ygojson.tools.data;

import java.util.stream.Stream;

import io.github.ygojson.model.data.Card;

/**
 * Repository for {@link Card}.
 */
public interface CardRepository {

	/**
	 * Saves the card into the repository.
	 *
	 * @param card card to save.
	 *
	 * @throws IllegalArgumentException if the card is already present in the repository.
	 */
	void save(final Card card);

	/**
	 * Retrieves all cards on the repository.
	 *
	 * @return a stream of cards stored in the repository.
	 */
	Stream<Card> findAll();
}
