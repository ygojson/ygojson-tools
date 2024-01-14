package io.github.ygojson.model.data.definition.localization;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.github.ygojson.model.data.property.LanguageProperties;

/**
 * Helper to create localized data models.
 * <br>
 * Note: this class does not support the {@link Language#EN}
 * as the it is the main language of YGOJSON.
 *
 * @param <T> type of the localized model.
 */
@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
abstract class AbstractLocalizedData<T> {

	private final Map<Language, T> languageMap = new HashMap<>();

	@JsonIgnore
	public final Map<Language, T> asMap() {
		return Collections.unmodifiableMap(languageMap);
	}

	@JsonIgnore
	public final T getData(final Language language) {
		return languageMap.get(language);
	}

	public final void setData(final Language language, final T data) {
		languageMap.put(language, data);
	}

	/**
	 * Translation for german
	 *
	 * @return the translation
	 */
	@JsonProperty(value = LanguageProperties.DE)
	public T getDe() {
		return getData(Language.DE);
	}

	/**
	 * Translation for spanish
	 *
	 * @return the translation
	 */
	@JsonProperty(value = LanguageProperties.ES)
	public T getEs() {
		return getData(Language.ES);
	}

	/**
	 * Translation for french
	 *
	 * @return the translation
	 */
	@JsonProperty(value = LanguageProperties.FR)
	public T getFr() {
		return getData(Language.FR);
	}

	/**
	 * Translation for italian
	 *
	 * @return the translation
	 */
	@JsonProperty(value = LanguageProperties.IT)
	public T getIt() {
		return getData(Language.IT);
	}

	/**
	 * Translation for japanese
	 *
	 * @return the translation
	 */
	@JsonProperty(value = LanguageProperties.JA)
	public T getJa() {
		return getData(Language.JA);
	}

	/**
	 * Translation for korean
	 *
	 * @return the translation
	 */
	@JsonProperty(value = LanguageProperties.KO)
	public T getKo() {
		return getData(Language.KO);
	}

	/**
	 * Translation for portuguese
	 *
	 * @return the translation
	 */
	@JsonProperty(value = LanguageProperties.PT)
	public T getPt() {
		return getData(Language.PT);
	}

	/**
	 * Translation for chinese
	 *
	 * @return the translation
	 */
	@JsonProperty(value = LanguageProperties.ZH_HANS)
	public T getZhHans() {
		return getData(Language.ZH_HANS);
	}

	/**
	 * Translation for chinese
	 *
	 * @return the translation
	 */
	@JsonProperty(value = LanguageProperties.ZH_HANT)
	public T getZhHant() {
		return getData(Language.ZH_HANT);
	}

	/**
	 * Sets the German translation
	 *
	 * @param data the data for the language
	 */
	public void setDe(final T data) {
		setData(Language.DE, data);
	}

	/**
	 * Sets the Spanish translation
	 *
	 * @param data the data for the language
	 */
	public void setEs(final T data) {
		setData(Language.ES, data);
	}

	/**
	 * Sets the French translation
	 *
	 * @param data the data for the language
	 */
	public void setFr(final T data) {
		setData(Language.FR, data);
	}

	/**
	 * Sets the Italian translation
	 *
	 * @param data the data for the language
	 */
	public void setIt(final T data) {
		setData(Language.IT, data);
	}

	/**
	 * Sets the Japanese translation
	 *
	 * @param data the data for the language
	 */
	public void setJa(final T data) {
		setData(Language.JA, data);
	}

	/**
	 * Sets the Korean translation
	 * @param data the data for the language
	 */
	public void setKo(final T data) {
		setData(Language.KO, data);
	}

	/**
	 * Sets the Portuguese translation
	 *
	 * @param data the data for the language
	 */
	public void setPt(final T data) {
		setData(Language.PT, data);
	}

	/**
	 * Sets the Simplified Chinese translation
	 *
	 * @param data the data for the language
	 */
	public void setZhHans(final T data) {
		setData(Language.ZH_HANS, data);
	}

	/**
	 * Sets the Traditional Chinese translation
	 *
	 * @param data the data for the language
	 */
	public void setZhHant(final T data) {
		setData(Language.ZH_HANT, data);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AbstractLocalizedData<?> that = (AbstractLocalizedData<?>) o;
		return Objects.equals(languageMap, that.languageMap);
	}

	@Override
	public int hashCode() {
		return Objects.hash(languageMap);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
			.add("languageMap=" + languageMap)
			.toString();
	}
}
