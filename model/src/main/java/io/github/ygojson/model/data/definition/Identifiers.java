package io.github.ygojson.model.data.definition;

import java.util.Objects;

import com.fasterxml.jackson.annotation.*;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.propertie.IdentifierProperties;

/**
 * Identifiers associated to the {@link Card}.
 * </br>
 * Note that this model is associated with an atomic card and not a print,
 * except {@link #getPasswordAlt()}, which makes sense as it should also
 * identify the atomic card.
 */
@JsonClassDescription(
	"""
		Identifiers associated to the Card (atomic).

		This model is a container of third-party IDs associated with the Card (atomic)."""
)
@JsonPropertyOrder(
	{
		IdentifierProperties.KONAMI_ID,
		IdentifierProperties.PASSWORD,
		IdentifierProperties.PASSWORD_ALT,
		IdentifierProperties.YUGIPEDIA_PAGE_ID,
	}
)
public class Identifiers {

	private Long konamiId;
	private Long password;
	private Long passwordAlt;
	private Long yugipediaPageId;

	/**
	 * ID on the <a href="https://www.db.yugioh-card.com/yugiohdb/">Yu-Gi-Oh official database</a>.
	 * </br>
	 * The ID might not appear on the official database for some languages if not
	 * released yet on that location.
	 * Also, cards might not have an ID if not released at all.
	 */
	@JsonPropertyDescription(
		"""
			ID on the <a href="https://www.db.yugioh-card.com/yugiohdb/">Yu-Gi-Oh official database</a>.

			The ID might not appear on the official database for some languages if it was not released yet on that location.
			Also, cards might not have an ID if nor released at all."""
	)
	@JsonProperty(value = IdentifierProperties.KONAMI_ID)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public Long getKonamiId() {
		return konamiId;
	}

	/**
	 * Unique 8-digit number printed in the bottom of cards, which is the same in all languages and prints.
	 * </br>
	 * Certain cards does not have a password.
	 * </br>
	 * Only few cards have more than one password for different prints (see also {@link #getPasswordAlt()}
	 * for those.
	 */
	@JsonPropertyDescription(
		"""
			Unique 8-digit number printed in the bottom of cards, which is the same in all languages and prints.

			Note that certain cards does not have a password."""
	)
	@JsonProperty(value = IdentifierProperties.PASSWORD)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public Long getPassword() {
		return password;
	}

	/**
	 * Unique 8-digit identifier assigned to a different print of a card on some special cases.
	 * </br>
	 * Examples of these are the <em>Dark Magician (Arkana)</em> and
	 * the alternative art of <em>Polymerization</em>.
	 */
	@JsonPropertyDescription(
		"""
			Unique 8-digit number printed in the bottom of different prints of a card on some special cases.

			Examples of these are the <em>Dark Magician (Arkana)</em> and the alternative art of <em>Polymerization</em>"""
	)
	@JsonProperty(value = IdentifierProperties.PASSWORD_ALT)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public Long getPasswordAlt() {
		return passwordAlt;
	}

	/**
	 * Page ID on <a href="https://yugipedia.com">Yugipedia</a>.
	 * </br>
	 * This ID can be used on the <a href="https://yugipedia.com/api.php">Yugipedia API</a>
	 * as the {@code pageId} or {@code pageids} parameters.
	 */
	@JsonPropertyDescription(
		"""
			Page ID on <a href="https://yugipedia.com">Yugipedia</a>.

			This ID can be used on the <a href="https://yugipedia.com/api.php">Yugipedia API</a> as the <em>pageId</em> or <em>pageids</em> parameters."""
	)
	@JsonProperty(value = IdentifierProperties.YUGIPEDIA_PAGE_ID)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public Long getYugipediaPageId() {
		return yugipediaPageId;
	}

	public void setKonamiId(Long konamiId) {
		this.konamiId = konamiId;
	}

	public void setPassword(Long password) {
		this.password = password;
	}

	public void setPasswordAlt(Long passwordAlt) {
		this.passwordAlt = passwordAlt;
	}

	public void setYugipediaPageId(Long yugipediaPageId) {
		this.yugipediaPageId = yugipediaPageId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Identifiers that)) return false;
		return (
			Objects.equals(konamiId, that.konamiId) &&
			Objects.equals(password, that.password) &&
			Objects.equals(passwordAlt, that.passwordAlt) &&
			Objects.equals(yugipediaPageId, that.yugipediaPageId)
		);
	}

	@Override
	public int hashCode() {
		return Objects.hash(konamiId, password, passwordAlt, yugipediaPageId);
	}
}
