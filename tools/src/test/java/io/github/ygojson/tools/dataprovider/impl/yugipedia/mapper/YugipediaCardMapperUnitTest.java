package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaException;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.CardTable2;
import io.github.ygojson.tools.dataprovider.test.CardTable2Mother;

class YugipediaCardMapperUnitTest {

	private static final YugipediaCardMapper MAPPER = Mappers.getMapper(
		YugipediaCardMapper.class
	);

	@Test
	void given_cardTable2WithInvalidCardType_when_mapToCard_then_throwYugipediaException() {
		// given - i.e., https://yugipedia.com/wiki/Command_Duel-Use_Card
		final CardTable2 cardTable2 = CardTable2Mother.withCardType("Command");
		// when
		final ThrowableAssert.ThrowingCallable throwingCallable = () ->
			MAPPER.mapToCard(cardTable2);
		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(YugipediaException.class);
	}
}
