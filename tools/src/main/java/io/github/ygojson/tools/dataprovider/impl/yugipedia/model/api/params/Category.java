package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params;

import lombok.RequiredArgsConstructor;

/**
 * Contains only categories used within YGOJSON.
 */
@RequiredArgsConstructor
public enum Category {
	CARDS("Category:Duel_Monsters_cards");

	private final String value;

	@Override
	public String toString() {
		return value;
	}
}