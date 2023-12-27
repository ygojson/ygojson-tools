package io.github.ygojson.tools.dataprovider;

import java.util.stream.Stream;

import io.github.ygojson.model.data.CardPrints;

/**
 * Defines a card provider for YGOJSON tools.
 */
public interface CardProvider {
	/**
	 * Gets the cards as a stream.
	 * <br>
	 * The stream should be lazy loaded.
	 *
	 * @return the stream of cards from the initial card provider.
	 */
	Stream<CardPrints> getCardPrintsStream();
}
