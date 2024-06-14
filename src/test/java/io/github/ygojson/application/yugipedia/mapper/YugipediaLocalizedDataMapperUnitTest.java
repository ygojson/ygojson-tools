package io.github.ygojson.application.yugipedia.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.ygojson.application.core.db.set.SetEntity;
import io.github.ygojson.application.logic.mapper.MappingException;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;

class YugipediaLocalizedDataMapperUnitTest {

	private static YugipediaLocalizedDataMapper MAPPER;

	@BeforeAll
	static void beforeAll() {
		MAPPER = new YugipediaLocalizedDataMapper();
	}

	@Test
	void given_moreThanThreeEnSetCodes_when_addLocalizedData_then_throwsMappingException() {
		// given
		final SetEntity entity = new SetEntity();
		final Map<String, YugipediaProperty> properties = Map.of(
			"prefix",
			YugipediaProperty.text("SRL"),
			"en_prefix",
			YugipediaProperty.text("MRL"),
			"eu_prefix",
			YugipediaProperty.text("EUMRL")
		);
		// when
		final ThrowableAssert.ThrowingCallable throwingCallable = () ->
			MAPPER.addLocalizedData(entity, properties);
		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(MappingException.class);
	}

	@Test
	void given_threeRepeatedEnSetCodesWithSamePrefix_when_addLocalizedData_then_throwsMappingException() {
		// given
		final SetEntity entity = new SetEntity();
		final Map<String, YugipediaProperty> properties = Map.of(
			"prefix",
			YugipediaProperty.text("SRL"),
			"en_prefix",
			YugipediaProperty.text("SRL"),
			"eu_prefix",
			YugipediaProperty.text("SRL")
		);
		// when
		MAPPER.addLocalizedData(entity, properties);
		// then
		assertSoftly(softly -> {
			softly.assertThat(entity.en).isNotNull();
			softly
				.assertThat(entity.en)
				.extracting(localizedValues -> localizedValues.setCode)
				.isEqualTo("SRL");
			softly
				.assertThat(entity.en)
				.extracting(localizedValues -> localizedValues.setCodeAlt)
				.isNull();
		});
	}

	static Stream<Arguments> twoRepeatedEnSetCodesCorrect() {
		return Stream.of(
			Arguments.of(List.of("SRL", "MRL", "MRL"), "SRL", "MRL"),
			Arguments.of(List.of("SRL", "SRL", "MRL"), "SRL", "MRL"),
			Arguments.of(List.of("SRL", "MRL", "SRL"), "SRL", "MRL")
		);
	}

	@ParameterizedTest
	@MethodSource("twoRepeatedEnSetCodesCorrect")
	void given_twoRepeatedEnSetCodes_when_addLocalizedData_then_entityIsCorrect(
		final List<String> threePrefixes,
		final String expectedFirst,
		final String expectedSecond
	) {
		// given
		final SetEntity entity = new SetEntity();
		final Map<String, YugipediaProperty> properties = Map.of(
			"prefix",
			YugipediaProperty.text(threePrefixes.get(0)),
			"en_prefix",
			YugipediaProperty.text(threePrefixes.get(1)),
			"eu_prefix",
			YugipediaProperty.text(threePrefixes.get(2))
		);
		// when
		MAPPER.addLocalizedData(entity, properties);
		// then
		assertThat(entity.en)
			.isNotNull()
			.extracting(
				localizedValues -> localizedValues.setCode,
				localizedValues -> localizedValues.setCodeAlt
			)
			.isEqualTo(List.of(expectedFirst, expectedSecond));
	}
}
