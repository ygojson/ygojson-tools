package io.github.ygojson.model.data;

import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.Pattern;

import io.github.ygojson.model.data.definition.localization.Language;
import io.github.ygojson.model.data.propertie.CommonProperties;
import io.github.ygojson.model.data.propertie.LanguageProperties;
import io.github.ygojson.model.data.propertie.PrintProperties;

/**
 * Model describing the properties of a Print (atomic).
 */
@JsonClassDescription(
	"""
		Describes the information for a print.

		This model contains the properties of the printed card that will not be common to all of them."""
)
@JsonPropertyOrder(
	{
		CommonProperties.SELF_ID,
		CommonProperties.CARD_ID,
		CommonProperties.SET_ID,
		PrintProperties.SET_PREFIX,
		PrintProperties.PRINT_NUMBER,
		PrintProperties.RARITY,
		LanguageProperties.LANGUAGE,
	}
)
public class Print {

	private UUID id;
	private UUID cardId;
	private UUID setId;
	private String setPrefix;
	private String printNumber;
	private String rarity;
	private Language language;

	/**
	 * UUID (v5) for the Print (atomic) generated by YGOJSON.
	 */
	@JsonPropertyDescription(
		"UID (v5) for the Print (atomic) generated by YGOJSON."
	)
	@JsonProperty(value = CommonProperties.SELF_ID, required = true)
	@JsonInclude(JsonInclude.Include.ALWAYS)
	public UUID getId() {
		return id;
	}

	/**
	 * UUID (v5) generated by YGOJSON of the card that is associated to this print.
	 * </br>
	 * This associates the print with the {@link Card}.
	 *
	 * @see Card#getId()
	 */
	@JsonPropertyDescription(
		"UID (v5) for the Card (atomic) associated with this print (generated by YGOJSON)."
	)
	@JsonProperty(value = CommonProperties.CARD_ID, required = true)
	@JsonInclude(JsonInclude.Include.ALWAYS)
	public UUID getCardId() {
		return cardId;
	}

	/**
	 * UUID (v5) generated by YGOJSON of the set that is associated to this print.
	 * </br>
	 * This associates the print with the {@link Set}.
	 *
	 * @see Set#getId()
	 */
	@JsonPropertyDescription(
		"UID (v5) for the Set (atomic) associated with this print (generated by YGOJSON)."
	)
	@JsonProperty(value = CommonProperties.SET_ID, required = true)
	@JsonInclude(JsonInclude.Include.ALWAYS)
	public UUID getSetId() {
		return setId;
	}

	/**
	 * Set prefix associated with this print.
	 * </br>
	 * Together with the {@link #getPrintNumber()} generates the full print-code.
	 * </br>
	 * This property might differ from the {@link Set#getPrefix()} associated for this
	 * print, as prints-codes are often localized.
	 */
	@JsonPropertyDescription(
		"""
			Set prefix associated with this print.

			This property might differ with the prefix on the Set Model as the print-codes are often localized."""
	)
	@JsonProperty(value = PrintProperties.SET_PREFIX)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@Pattern(regexp = "[a-zA-Z0-9]+-[a-zA-Z0-9]+")
	public String getSetPrefix() {
		return setPrefix;
	}

	/**
	 * Number of the print within the set.
	 * </br>
	 * Together with the {@link #getSetPrefix()} generates the full printCode.
	 * </br>
	 * Although this property is usually a numeric with left-padded zeroes,
	 * there are few cases where a letter is at the end (i.e., "K-Series").
	 */
	@JsonPropertyDescription(
		"""
			Number of the print within the set.

			It is represented as a string with left-padded zeroes, although sometimes might contain a letter."""
	)
	@JsonProperty(value = PrintProperties.PRINT_NUMBER)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@Pattern(regexp = "[a-zA-Z0-9]+")
	public String getPrintNumber() {
		return printNumber;
	}

	/**
	 * Rarity of the print (lower-case).
	 * </br>
	 * The rarity of the card print.
	 */
	@JsonPropertyDescription("Rarity of the print")
	@JsonProperty(value = PrintProperties.RARITY)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@Pattern(regexp = "[a-z]+")
	public String getRarity() {
		return rarity;
	}

	/**
	 * Language of the print.
	 * </br>
	 * The language of the card print.
	 */
	@JsonPropertyDescription("Language of the print")
	@JsonProperty(value = LanguageProperties.LANGUAGE)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public Language getLanguage() {
		return language;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public void setCardId(UUID cardId) {
		this.cardId = cardId;
	}

	public void setSetId(UUID setId) {
		this.setId = setId;
	}

	public void setSetPrefix(String setPrefix) {
		this.setPrefix = setPrefix;
	}

	public void setPrintNumber(String printNumber) {
		this.printNumber = printNumber;
	}

	public void setRarity(String rarity) {
		this.rarity = rarity;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Print print)) return false;
		return (
			Objects.equals(id, print.id) &&
			Objects.equals(cardId, print.cardId) &&
			Objects.equals(setId, print.setId) &&
			Objects.equals(setPrefix, print.setPrefix) &&
			Objects.equals(printNumber, print.printNumber) &&
			Objects.equals(rarity, print.rarity) &&
			Objects.equals(language, print.language)
		);
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			id,
			cardId,
			setId,
			setPrefix,
			printNumber,
			rarity,
			language
		);
	}
}
