package io.github.ygojson.application.yugipedia.client.params;

/**
 * Contains only categories used within YGOJSON.
 */
public enum Category {
	CARDS("Category:Duel_Monsters_cards");

	private final String value;

	Category(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
