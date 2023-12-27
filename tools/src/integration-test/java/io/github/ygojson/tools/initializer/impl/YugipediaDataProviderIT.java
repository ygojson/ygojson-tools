package io.github.ygojson.tools.initializer.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.ygojson.model.data.CardPrints;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaApiMother;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaDataProvider;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params.Limit;

class YugipediaDataProviderIT {

	private static final Logger log = LoggerFactory.getLogger(YugipediaDataProviderIT.class);

	private ObjectMapper mapper = new ObjectMapper();
	private YugipediaDataProvider dataProvider;

	@BeforeEach
	void beforeEach() {
		dataProvider =
			new YugipediaDataProvider(
				YugipediaApiMother.productionTestClient(),
				Limit.getDefault()
			);
	}

	@Test
	@Disabled
	void testCardSPrintstream() throws IOException {
		final List<CardPrints> cards = dataProvider
			.getCardPrintsStream()
			.limit(11)
			.toList();
		mapper.writerWithDefaultPrettyPrinter().writeValue(System.out, cards);
		assertThat(cards).hasSize(10);
	}

}
