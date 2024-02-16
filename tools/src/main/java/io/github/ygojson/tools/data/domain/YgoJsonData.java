package io.github.ygojson.tools.data.domain;

import java.util.UUID;
import java.util.stream.Stream;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.Print;
import io.github.ygojson.model.data.Set;
import io.github.ygojson.tools.data.CardRepository;
import io.github.ygojson.tools.data.PrintRepository;
import io.github.ygojson.tools.data.SetRepository;
import io.github.ygojson.tools.dataprovider.PrintData;
import io.github.ygojson.tools.dataprovider.domain.UUIDGenerator;

/**
 * Domain model representing the YGOJSON data that is
 * currently considered on the tooling.
 */
public class YgoJsonData {

	private final UUIDGenerator uuidGenerator = new UUIDGenerator();
	private final CardRepository cardRepository;
	private final PrintRepository printRepository;
	private final SetRepository setRepository;


	public YgoJsonData(final CardRepository cardRepository, final PrintRepository printRepository, final SetRepository setRepository) {
		this.cardRepository = cardRepository;
		this.printRepository = printRepository;
		this.setRepository = setRepository;
	}

	public void addInitialData(final Print print, final Card card, final Set set) {
		if (print ==  null || card == null || set == null) {
			throw new IllegalArgumentException("Cannot add null initial data");
		}
		if (print.getCardId() != null || print.getSetId() != null) {
			throw new IllegalArgumentException("Cannot add initial data with already existing IDs");
		}
		computeInitialIds(print, card, set);
		printRepository.save(print);
		cardRepository.save(card);
		setRepository.save(set);
	}

	public void computeInitialIds(final Print print, final Card card, final Set set) {
		final UUIDGenerator.PrintDataUUIDs printDataUUIDs = uuidGenerator.generate(new PrintData(print, card, set));
		final UUID printId = printDataUUIDs.printId();
		final UUID cardId = printDataUUIDs.cardId();
		final UUID setId = printDataUUIDs.setId();
		// set self IDs
		card.setId(cardId);
		set.setId(setId);
		print.setId(printId);
		// set print IDs
		print.setCardId(cardId);
		print.setSetId(setId);
	}

	public Stream<Set> getSets() {
		// TODO: should we paginate instead?
		return setRepository.findAll();
	}

}
