package io.github.ygojson.model.utils.data.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.all;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.Set;
import io.github.ygojson.model.data.definition.CardText;
import io.github.ygojson.model.data.definition.SetInfo;
import io.github.ygojson.model.data.definition.localization.CardLocalizedData;
import io.github.ygojson.model.data.definition.localization.Language;
import io.github.ygojson.model.data.definition.localization.SetLocalizedData;

class LocalizationUtilsUnitTest {

	@ParameterizedTest
	@EnumSource(Language.class)
	void given_languageAndEmptyCard_when_getLocalizedCardText_then_returnNull(
		final Language language
	) {
		// given
		final Card card = new Card();
		// when
		final CardText localizedCardText = LocalizationUtils.getLocalizedData(
			language,
			card
		);
		// then
		assertThat(localizedCardText).isNull();
	}

	@ParameterizedTest
	@EnumSource(Language.class)
	void given_languageAndPopulatedCard_when_getLocalizedCardText_then_returnPopulatedCardText(
		final Language language
	) {
		// given
		final Card card = Instancio.create(Card.class);
		// when
		final CardText localizedCardText = LocalizationUtils.getLocalizedData(
			language,
			card
		);
		// then
		assertThat(localizedCardText).isNotNull();
	}

	@ParameterizedTest
	@EnumSource(Language.class)
	void given_languageAndEmptySetInfo_when_getLocalizedSetInfo_then_returnNull(
		final Language language
	) {
		// given
		final Set set = new Set();
		// when
		final SetInfo localizedSetInfo = LocalizationUtils.getLocalizedData(
			language,
			set
		);
		// then
		assertThat(localizedSetInfo).isNull();
	}

	@ParameterizedTest
	@EnumSource(Language.class)
	void given_languageAndPopulatedSetInfo_when_getLocalizedSetInfo_then_returnPopulatedSetInfo(
		final Language language
	) {
		// given
		final Set set = Instancio.create(Set.class);
		// when
		final SetInfo localizedSetInfo = LocalizationUtils.getLocalizedData(
			language,
			set
		);
		// then
		assertThat(localizedSetInfo).isNotNull();
	}

	@ParameterizedTest
	@EnumSource(Language.class)
	void given_languageAndPopulatedSet_when_setNullSetInfo_then_localizationIsNull(
		final Language language
	) {
		// given
		final Set set = Instancio.create(Set.class);
		// when
		LocalizationUtils.setLocalizedData(set, null, language);
		// then
		final SetInfo localizedSetInfo = LocalizationUtils.getLocalizedData(
			language,
			set
		);
		assertThat(localizedSetInfo).isNull();
	}

	@ParameterizedTest
	@EnumSource(
		value = Language.class,
		mode = EnumSource.Mode.EXCLUDE,
		names = "EN"
	)
	void given_languageAndPopulatedSet_when_setEmptySetInfo_then_localizationIsNull(
		final Language language
	) {
		// given
		final Set set = Instancio.create(Set.class);
		// when
		LocalizationUtils.setLocalizedData(set, new SetInfo(), language);
		// then
		final SetInfo localizedSetInfo = LocalizationUtils.getLocalizedData(
			language,
			set
		);
		assertThat(localizedSetInfo).isEqualTo(new SetInfo());
	}

	@ParameterizedTest
	@EnumSource(value = Language.class)
	void given_languageAndPopulatedCard_when_setNullCardText_then_localizationIsNull(
		final Language language
	) {
		// given
		final Card card = Instancio.create(Card.class);
		// when
		LocalizationUtils.setLocalizedData(card, null, language);
		// then
		final CardText localizedCardText = LocalizationUtils.getLocalizedData(
			language,
			card
		);
		assertThat(localizedCardText).isNull();
	}

	@ParameterizedTest
	@EnumSource(
		value = Language.class,
		mode = EnumSource.Mode.EXCLUDE,
		names = "EN"
	)
	void given_languageAndPopulatedCard_when_setEmptyCardText_then_localizationIsNull(
		final Language language
	) {
		// given
		final Card card = Instancio.create(Card.class);
		// when
		LocalizationUtils.setLocalizedData(card, new CardText(), language);
		// then
		final CardText localizedCardText = LocalizationUtils.getLocalizedData(
			language,
			card
		);
		assertThat(localizedCardText).isEqualTo(new CardText());
	}

	@Test
	void given_nullCardLocalizedData_when_setNonNullCardText_then_localizedDataIsSet() {
		// given
		final Card card = Instancio
			.of(Card.class)
			.ignore(all(CardLocalizedData.class))
			.create();
		final CardText cardText = Instancio.create(CardText.class);
		// when
		LocalizationUtils.setLocalizedData(card, cardText, Language.ES);
		// then
		assertThat(card.getLocalizedData().getEs()).isEqualTo(cardText);
	}

	@Test
	void given_nullSetLocalizedData_when_setNonNullSetInfo_then_localizedDataIsSet() {
		// given
		final Set set = Instancio
			.of(Set.class)
			.ignore(all(SetLocalizedData.class))
			.create();
		final SetInfo setInfo = Instancio.create(SetInfo.class);
		// when
		LocalizationUtils.setLocalizedData(set, setInfo, Language.ES);
		// then
		assertThat(set.getLocalizedData().getEs()).isEqualTo(setInfo);
	}
}
