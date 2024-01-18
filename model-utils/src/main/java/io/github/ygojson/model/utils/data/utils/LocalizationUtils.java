package io.github.ygojson.model.utils.data.utils;

import java.util.function.BiConsumer;
import java.util.function.Function;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.Set;
import io.github.ygojson.model.data.definition.CardText;
import io.github.ygojson.model.data.definition.SetInfo;
import io.github.ygojson.model.data.definition.localization.CardLocalizedData;
import io.github.ygojson.model.data.definition.localization.Language;
import io.github.ygojson.model.data.definition.localization.SetLocalizedData;

public class LocalizationUtils {

	/**
	 * Gets the localized {@link CardText} for the card.
	 * <br>
	 * Note that for {@link Language#EN} a populated {@link CardText} is returned
	 * where changes will not affect the set.
	 *
	 * @param language language to get.
	 * @param card card to extract the localized information.
	 *
	 * @return the localized {@link CardText}
	 */
	public static CardText getLocalizedData(
		final Language language,
		final Card card
	) {
		final CardLocalizedData localizedData = card.getLocalizedData();
		return switch (language) {
			case EN -> getMainCardText(card);
			case ES -> getLocalizedObject(localizedData, CardLocalizedData::getEs);
			case DE -> getLocalizedObject(localizedData, CardLocalizedData::getDe);
			case FR -> getLocalizedObject(localizedData, CardLocalizedData::getFr);
			case IT -> getLocalizedObject(localizedData, CardLocalizedData::getIt);
			case JA -> getLocalizedObject(localizedData, CardLocalizedData::getJa);
			case KO -> getLocalizedObject(localizedData, CardLocalizedData::getKo);
			case PT -> getLocalizedObject(localizedData, CardLocalizedData::getPt);
			case ZH_HANS -> getLocalizedObject(
				localizedData,
				CardLocalizedData::getZhHans
			);
			case ZH_HANT -> getLocalizedObject(
				localizedData,
				CardLocalizedData::getZhHant
			);
		};
	}

	/**
	 * Gets the localized {@link SetInfo} for the set.
	 * <br>
	 * Note that for {@link Language#EN} a populated {@link SetInfo} is returned
	 * where changes will not affect the set.
	 *
	 * @param language language to get.
	 * @param set set to extract the localized information.
	 * @return the localized {@link SetInfo}
	 */
	public static SetInfo getLocalizedData(
		final Language language,
		final Set set
	) {
		final SetLocalizedData localizedData = set.getLocalizedData();
		return switch (language) {
			case EN -> getMainSetInfo(set);
			case ES -> getLocalizedObject(localizedData, SetLocalizedData::getEs);
			case DE -> getLocalizedObject(localizedData, SetLocalizedData::getDe);
			case FR -> getLocalizedObject(localizedData, SetLocalizedData::getFr);
			case IT -> getLocalizedObject(localizedData, SetLocalizedData::getIt);
			case JA -> getLocalizedObject(localizedData, SetLocalizedData::getJa);
			case KO -> getLocalizedObject(localizedData, SetLocalizedData::getKo);
			case PT -> getLocalizedObject(localizedData, SetLocalizedData::getPt);
			case ZH_HANS -> getLocalizedObject(
				localizedData,
				SetLocalizedData::getZhHans
			);
			case ZH_HANT -> getLocalizedObject(
				localizedData,
				SetLocalizedData::getZhHant
			);
			default -> throw new UnsupportedOperationException(
				"Language not supported: " + language
			);
		};
	}

	public static void setLocalizedData(
		final Card card,
		final CardText cardText,
		final Language language
	) {
		switch (language) {
			case EN -> setAsMainLanguage(card, cardText);
			case ES -> setLocalizedCardText(card, cardText, CardLocalizedData::setEs);
			case DE -> setLocalizedCardText(card, cardText, CardLocalizedData::setDe);
			case FR -> setLocalizedCardText(card, cardText, CardLocalizedData::setFr);
			case IT -> setLocalizedCardText(card, cardText, CardLocalizedData::setIt);
			case JA -> setLocalizedCardText(card, cardText, CardLocalizedData::setJa);
			case KO -> setLocalizedCardText(card, cardText, CardLocalizedData::setKo);
			case PT -> setLocalizedCardText(card, cardText, CardLocalizedData::setPt);
			case ZH_HANS -> setLocalizedCardText(
				card,
				cardText,
				CardLocalizedData::setZhHans
			);
			case ZH_HANT -> setLocalizedCardText(
				card,
				cardText,
				CardLocalizedData::setZhHant
			);
			default -> throw new UnsupportedOperationException(
				"Language not supported: " + language
			);
		}
	}

	public static void setLocalizedData(
		final Set set,
		final SetInfo setInfo,
		final Language language
	) {
		switch (language) {
			case EN -> setAsMainLanguage(set, setInfo);
			case ES -> setLocalizedSetInfo(set, setInfo, SetLocalizedData::setEs);
			case DE -> setLocalizedSetInfo(set, setInfo, SetLocalizedData::setDe);
			case FR -> setLocalizedSetInfo(set, setInfo, SetLocalizedData::setFr);
			case IT -> setLocalizedSetInfo(set, setInfo, SetLocalizedData::setIt);
			case JA -> setLocalizedSetInfo(set, setInfo, SetLocalizedData::setJa);
			case KO -> setLocalizedSetInfo(set, setInfo, SetLocalizedData::setKo);
			case PT -> setLocalizedSetInfo(set, setInfo, SetLocalizedData::setPt);
			case ZH_HANS -> setLocalizedSetInfo(
				set,
				setInfo,
				SetLocalizedData::setZhHans
			);
			case ZH_HANT -> setLocalizedSetInfo(
				set,
				setInfo,
				SetLocalizedData::setZhHant
			);
			default -> throw new UnsupportedOperationException(
				"Language not supported: " + language
			);
		}
	}

	/**
	 * Use the {@link SetInfo} as the main language.
	 *
	 * @param set the set to update
	 * @param setInfo the set info to use
	 */
	public static void setAsMainLanguage(final Set set, final SetInfo setInfo) {
		final SetInfo toSet = setInfo == null ? new SetInfo() : setInfo;
		set.setName(toSet.getName());
		set.setNameAlt(toSet.getNameAlt());
		set.setSetCode(toSet.getSetCode());
		set.setSetCodeAlt(toSet.getSetCodeAlt());
	}

	/**
	 * Use the {@link CardText} as the main language.
	 *
	 * @param card the card to update
	 * @param cardText the card text to use
	 */
	public static void setAsMainLanguage(
		final Card card,
		final CardText cardText
	) {
		final CardText toSet = cardText == null ? new CardText() : cardText;
		card.setName(toSet.getName());
		card.setFlavorText(toSet.getFlavorText());
		card.setEffectText(toSet.getEffectText());
		card.setPendulumEffect(toSet.getPendulumEffect());
		card.setMaterials(toSet.getMaterials());
	}

	private static void setLocalizedSetInfo(
		final Set set,
		final SetInfo setInfo,
		final BiConsumer<SetLocalizedData, SetInfo> setter
	) {
		SetLocalizedData localizedData = set.getLocalizedData();
		if (localizedData == null && setInfo != null) {
			localizedData = new SetLocalizedData();
			set.setLocalizedData(localizedData);
		}
		if (localizedData != null) {
			setter.accept(localizedData, setInfo);
		}
	}

	private static void setLocalizedCardText(
		final Card card,
		final CardText cardText,
		final BiConsumer<CardLocalizedData, CardText> setter
	) {
		CardLocalizedData localizedData = card.getLocalizedData();
		if (localizedData == null && cardText != null) {
			localizedData = new CardLocalizedData();
			card.setLocalizedData(localizedData);
		}
		if (localizedData != null) {
			setter.accept(localizedData, cardText);
		}
	}

	private static <T, R> R getLocalizedObject(
		final T localizationObject,
		final Function<T, R> localizedGetter
	) {
		if (localizationObject == null) {
			return null;
		}
		return localizedGetter.apply(localizationObject);
	}

	/**
	 * Gets the main card text as an object.
	 *
	 * @param card the card to extract the main card text
	 *
	 * @return the main card text; {@code null} if no values
	 */
	public static CardText getMainCardText(final Card card) {
		final CardText cardText = new CardText();
		boolean hasValues = false;
		if (card.getName() != null) {
			cardText.setName(card.getName());
			hasValues = true;
		}
		if (card.getFlavorText() != null) {
			cardText.setFlavorText(card.getFlavorText());
			hasValues = true;
		}
		if (card.getEffectText() != null) {
			cardText.setEffectText(card.getEffectText());
			hasValues = true;
		}
		if (card.getMaterials() != null) {
			cardText.setMaterials(card.getMaterials());
			hasValues = true;
		}
		if (card.getPendulumEffect() != null) {
			cardText.setPendulumEffect(card.getPendulumEffect());
			hasValues = true;
		}
		return hasValues ? cardText : null;
	}

	/**
	 * Gets the main set info as an object.
	 *
	 * @param set the set to extract the main set info
	 *
	 * @return the main set info; {@code null} if the no info is available.
	 */
	public static SetInfo getMainSetInfo(final Set set) {
		final SetInfo info = new SetInfo();
		boolean hasValues = false;
		if (set.getSetCode() != null) {
			info.setSetCode(set.getSetCode());
			hasValues = true;
		}
		if (set.getSetCodeAlt() != null) {
			info.setSetCodeAlt(set.getSetCodeAlt());
			hasValues = true;
		}
		if (set.getName() != null) {
			info.setName(set.getName());
			hasValues = true;
		}
		if (set.getNameAlt() != null) {
			info.setNameAlt(set.getNameAlt());
			hasValues = true;
		}
		return hasValues ? info : null;
	}

	private LocalizationUtils() {
		// Utility class
	}
}
