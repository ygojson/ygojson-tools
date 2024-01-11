package io.github.ygojson.model.data;

import java.util.Objects;
import java.util.StringJoiner;

import com.fasterxml.jackson.annotation.*;

/**
 * Model describing the properties of a Print with
 * all associated information({@link Card} and {@link Set}).
 */
@JsonClassDescription(
	"""
		Describes the information for a Print with all associated information.

		This model contains the same properties as the Print Model with the inclusion of corresponding Card and Set."""
)
public class FullPrint {
	public static final String CARD_PROPERTY = "card";
	public static final String SET_PROPERTY = "set";

	private Card card;

	private Set set;

	private Print print;

	/**
	 * Card associated to the print.
	 * <br>
	 * Note that the {@link Print#getCardId()} form {@link #getPrint()}
	 * MUST be the {@link Card#getId()} from the returned card.
	 */
	@JsonPropertyDescription("Card associated to the print")
	@JsonProperty(value = CARD_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public Card getCard() {
		return card;
	}

	/**
	 * Set associated to the print.
	 * <br>
	 * Note that the {@link Print#getSetId()} form {@link #getPrint()}
	 * MUST be the {@link Set#getId()} from the returned set.
	 */
	@JsonPropertyDescription("Set associated to the print")
	@JsonProperty(value = SET_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public Set getSet() {
		return set;
	}

	/**
	 * Representation of the Print specific data.
	 */
	@JsonUnwrapped
	public Print getPrint() {
		return print;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public void setSet(Set set) {
		this.set = set;
	}

	public void setPrint(Print print) {
		this.print = print;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof FullPrint that)) return false;
        return Objects.equals(card, that.card) && Objects.equals(set, that.set) && Objects.equals(print, that.print);
	}

	@Override
	public int hashCode() {
		return Objects.hash(card, set, print);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", FullPrint.class.getSimpleName() + "[", "]")
			.add("card=" + card)
			.add("set=" + set)
			.add("print=" + print)
			.toString();
	}
}
