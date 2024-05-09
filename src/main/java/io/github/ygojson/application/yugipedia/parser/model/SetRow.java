package io.github.ygojson.application.yugipedia.parser.model;

import java.util.List;

/**
 * Model identifying a row-set in Yugipedia.
 *
 * @param cardNumber
 * @param setPageName
 * @param rarities
 */
public record SetRow(
	String cardNumber,
	String setPageName,
	List<String> rarities
) {}
