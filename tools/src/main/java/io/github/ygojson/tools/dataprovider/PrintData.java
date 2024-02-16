package io.github.ygojson.tools.dataprovider;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.Print;
import io.github.ygojson.model.data.Set;

/**
 * Data-transfer object to be returned by {@link DataProvider}.
 * <br>+
 * This model is {@link Print}-based, as it is the driven-model
 * to fetch the data on YGOJSON.
 *
 * @param print the print
 * @param card associated card
 * @param set associated set
 */
public record PrintData(Print print, Card card, Set set) {
}
