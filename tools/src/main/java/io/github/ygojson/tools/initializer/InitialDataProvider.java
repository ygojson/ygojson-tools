package io.github.ygojson.tools.initializer;

import java.util.stream.Stream;

import io.github.ygojson.model.data.CardPrints;
import io.github.ygojson.model.data.Set;

/**
 * Defines the initial data provider.
 */
public interface InitialDataProvider {
	/**
	 * Initializes the data provider.
	 */
	public void init();

	public Stream<CardPrints> getCardPrintsStream();

	public Stream<Set> getSetStream();
}
