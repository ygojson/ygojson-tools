package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.definition.CardText;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.CardTable2;
import io.github.ygojson.tools.dataprovider.test.CardTable2Mother;

class YugipediaCardTextMapperUnitTest {

	private static final YugipediaCardTextMapper MAPPER = Mappers.getMapper(
		YugipediaCardTextMapper.class
	);

	@Test
	void given_nullNameAndPendulumEffect_when_mapToMainCardText_then_returnNull() {
		// given
		final CardTable2 cardTable2 = CardTable2Mother.withMainCardText(
			null,
			null,
			"Lore Ipsum"
		);
		// when
		final CardText result = MAPPER.mapToMainCardText(cardTable2);
		// then
		assertThat(result).isNull();
	}

	@Test
	void given_nullLore_when_updateCardTextOnEmptyCard_then_cardTextIsNull() {
		// given
		final CardTable2 cardTable2 = CardTable2Mother.withMainCardText(
			null,
			null,
			null
		);
		final Card emptyCard = new Card();
		// when
		MAPPER.updateMainCardText(cardTable2, emptyCard);
		// then
		assertThat(emptyCard).isEqualTo(new Card());
	}

	@Test
	void given_Lore_when_updateCardTextOnEmptyCard_then_cardTextIsPresentWithLore() {
		// given
		final CardTable2 cardTable2 = CardTable2Mother.withMainCardText(
			null,
			null,
			"Lore Ipsum"
		);
		final Card emptyCard = new Card();
		// when
		MAPPER.updateMainCardText(cardTable2, emptyCard);
		// then
		final Card expectedCard = new Card();
		expectedCard.setEffectText("Lore Ipsum");
		assertThat(emptyCard).isEqualTo(expectedCard);
	}
}
