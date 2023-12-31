package io.github.ygojson.model.data;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import com.fasterxml.jackson.annotation.*;

/**
 * Model describing the properties of a Card with all associated prints.
 */
@JsonClassDescription(
	"""
		Describes the information for a Card with associated prints.

		This model contains the same properties as the Card Model with the inclusion of the print list."""
)
// TODO: fix property order (see https://github.com/ygojson/ygojson-tools/issues/28)
public class CardPrints {

	public static final String PRINTS_PROPERTY = "prints";

	private Card card;

	private List<Print> prints;

	@JsonUnwrapped
	public Card getCard() {
		return card;
	}

	@JsonPropertyDescription("List of prints for the card")
	@JsonProperty(value = PRINTS_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public List<Print> getPrints() {
		return prints;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public void setPrints(List<Print> prints) {
		this.prints = prints;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CardPrints that)) return false;
		return (
			Objects.equals(card, that.card) && Objects.equals(prints, that.prints)
		);
	}

	@Override
	public int hashCode() {
		return Objects.hash(card, prints);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", CardPrints.class.getSimpleName() + "[", "]")
			.add("card=" + card)
			.add("prints=" + prints)
			.toString();
	}
}
