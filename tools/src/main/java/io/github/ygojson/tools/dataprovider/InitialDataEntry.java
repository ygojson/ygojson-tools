package io.github.ygojson.tools.dataprovider;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.Print;
import io.github.ygojson.model.data.Set;

/**
 * Representation of the initial data ({@link Print} based).
 *
 * @param print the initial data print.
 * @param card  the initial card.
 * @param set   the initial set.
 */
public record InitialDataEntry(Print print, Card card, Set set) {
}
