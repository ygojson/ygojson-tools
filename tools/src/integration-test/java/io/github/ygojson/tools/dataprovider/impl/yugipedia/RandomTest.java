package io.github.ygojson.tools.dataprovider.impl.yugipedia;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.ygojson.model.data.Set;
import io.github.ygojson.model.utils.JsonUtils;
import io.github.ygojson.tools.dataprovider.domain.repository.set.SetEntity;
import io.github.ygojson.tools.dataprovider.domain.repository.set.SetRepository;
import io.github.ygojson.tools.dataprovider.impl.repository.nitrite.SetRepositoryImpl;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params.Limit;
import io.github.ygojson.tools.dataprovider.test.dataprovider.yugipedia.YugipediaApiMother;
import io.github.ygojson.tools.dataprovider.test.dataprovider.yugipedia.YugipediaMockServer;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.common.mapper.JacksonMapperModule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class RandomTest {

	private static final Logger log = LoggerFactory.getLogger(RandomTest.class);

	private static YugipediaMockServer MOCK_SERVER;
	private static YugipediaDataProvider MOCK_DATA_PROVIDER;
	private static SetRepository setNitriteRepository;

	@BeforeAll
	static void beforeAll() {
		MOCK_SERVER = new YugipediaMockServer();
		MOCK_DATA_PROVIDER =
			new YugipediaDataProvider(
				YugipediaApiMother.mockTestClient(MOCK_SERVER.getUrl()),
				Limit.getDefault()
			);
		final Nitrite nitrite = Nitrite.builder().loadModule(new JacksonMapperModule(
			new JavaTimeModule()
		)).openOrCreate();
		setNitriteRepository = new SetRepositoryImpl(nitrite);
	}

	@AfterAll
	static void afterAll() throws Exception {
		MOCK_SERVER.close();
		setNitriteRepository.close();
	}

	@Test
	void testSomething() {
		// given
		final Stream<Set> fetchingStream = MOCK_DATA_PROVIDER.fetchSets();
		// when
		try {
			fetchingStream.limit(30)
				.map(set -> new SetEntity(set))
				.forEach(setNitriteRepository::save);
		} catch (Exception e) {
			log.error("Failed to save set", e);
		}
		// then
		final ObjectWriter objectWriter = JsonUtils.getObjectMapper()
				.writerWithDefaultPrettyPrinter();
		assertThat(setNitriteRepository.count()).isEqualTo(20);
		assertThat(setNitriteRepository.findAll().count()).isEqualTo(20);
		setNitriteRepository.findAll()
			.forEach(s -> {
            try {
				log.info(objectWriter.writeValueAsString(s));
            } catch (Exception e) {
				log.error("Failed to write value: {}", s, e);
            }
        });
	}
}
