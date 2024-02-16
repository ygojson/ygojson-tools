package io.github.ygojson.tools.data.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.common.mapper.JacksonMapper;
import org.dizitart.no2.common.mapper.JacksonMapperModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.utils.JsonUtils;

@Disabled("TODO")
class CardRepositoryImplUnitTest {

	private CardRepositoryImpl cardRepository;

	@BeforeEach
	public void beforeEach() {
		// TODO: we should extract this logic
		final JacksonMapper jacksonMapper = new JacksonMapper() {
			@Override
			public ObjectMapper getObjectMapper() {
				return JsonUtils.getObjectMapper();
			}
		};
		final Nitrite db = Nitrite.builder()
			.loadModule(new JacksonMapperModule())
			//.loadModule(NitriteModule.module(jacksonMapper))
			.openOrCreate();
		cardRepository = new CardRepositoryImpl(db);
	}

	@AfterEach
	public void afterEach() {
		//cardRepository.close();
	}


	@Test
	void given_emptyCard_when_saveCard_then_isSaved() {
		// given
		final Card card = new Card();
		// when
		/*
		cardRepository.save(card);
		// then
		final List<Card> savedCards = cardRepository.findAll().toList();
		assertThat(savedCards)
			.singleElement()
			.isEqualTo(card);
		 */
	}

}
