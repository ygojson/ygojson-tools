package io.github.ygojson.application.core.datastore;

import java.util.Optional;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import io.github.ygojson.application.core.datastore.db.card.RawCard;
import io.github.ygojson.application.core.datastore.db.card.RawCardRepository;
import io.github.ygojson.application.core.datastore.db.set.RawSetRepository;

/**
 * YGOJSON Raw Data-Store.
 * <br>
 * This class is the main entry point to interact with the data-store.
 */
@ApplicationScoped
public class RawDataStore {

	private final YgoJsonIDGenerator idGenerator;
	private final RawSetRepository setRepository;
	private final RawCardRepository cardRepository;

	@Inject
	public RawDataStore(
		final YgoJsonIDGenerator idGenerator,
		final RawSetRepository setRepository,
		final RawCardRepository cardRepository
	) {
		this.idGenerator = idGenerator;
		this.setRepository = setRepository;
		this.cardRepository = cardRepository;
	}

	/**
	 * Add a new card to the data-store.
	 * <br>
	 * The card requires that the YGOJSON-ID is not yet set,
	 * as it should be a new card.
	 *
	 * @param card the card to add to the data-store.
	 */
	public void addNewCard(final RawCard card) {
		card.ygojsonId = idGenerator.generate(card);
		cardRepository.save(card);
	}

	/**
	 * Gets the card by the YGOJSON-ID.
	 *
	 * @param ygojsonId the YGOJSON-ID
	 *
	 * @return the card.
	 */
	public Optional<RawCard> getCardByYgojsonId(final UUID ygojsonId) {
		return cardRepository.find("ygojsonId", ygojsonId).firstResultOptional();
	}
}
