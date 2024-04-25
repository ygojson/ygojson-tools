package io.github.ygojson.application.yugipedia.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;

class PropertyParserUnitTest {

	private static PropertyParser PARSER;

	@BeforeAll
	static void beforeAll() {
		PARSER = new PropertyParser();
	}

	@ParameterizedTest
	@EnumSource(PropertyParser.Type.class)
	void given_propertyType_when_parseNull_then_returnNull(
		final PropertyParser.Type type
	) {
		// given - type
		// when
		final YugipediaProperty yugipediaProperty = PARSER.parse(type, null);
		// then
		assertThat(yugipediaProperty).isEqualTo(null);
	}

	static Stream<Arguments> commaSeparatedValuesProvider() {
		return Stream.of(
			Arguments.of("", List.of()),
			Arguments.of("single", List.of("single")),
			Arguments.of("first, second", List.of("first", "second")),
			Arguments.of("first, second, third", List.of("first", "second", "third")),
			// skip blank values
			Arguments.of("first, , third", List.of("first", "third")),
			Arguments.of(", ", List.of()),
			Arguments.of(", , , ", List.of()),
			// comma without space is not considered a separator
			Arguments.of("first,second", List.of("first,second"))
		);
	}

	@ParameterizedTest
	@MethodSource("commaSeparatedValuesProvider")
	void given_possiblyCommaSeparatedString_when_parseCommaList_then_correctProperty(
		final String string,
		final List<String> expectedStrings
	) {
		// given - the string
		// when
		final YugipediaProperty yugipediaProperty = PARSER.parse(
			PropertyParser.Type.COMMA_LIST,
			string
		);
		// then
		assertThat(yugipediaProperty)
			.isEqualTo(new YugipediaProperty.ListProp(expectedStrings));
	}

	static Stream<Arguments> bulletedSeparatedValuesProvider() {
		return Stream.of(
			Arguments.of("", List.of()),
			Arguments.of("single", List.of("single")),
			Arguments.of("*single", List.of("single")),
			Arguments.of("* single", List.of("single")),
			Arguments.of("*first\n*second", List.of("first", "second")),
			Arguments.of(
				"*first\n*second\n*third",
				List.of("first", "second", "third")
			),
			Arguments.of("* first\n* second", List.of("first", "second")),
			Arguments.of("*first *second", List.of("first", "second")),
			Arguments.of("* first * second", List.of("first", "second")),
			Arguments.of("*first*second", List.of("first", "second")),
			// skip blank values
			Arguments.of("*first\n\n*third", List.of("first", "third")),
			Arguments.of("****", List.of()),
			Arguments.of("*\n\n*", List.of()),
			Arguments.of("*\n\n*\n\n*", List.of())
		);
	}

	@ParameterizedTest
	@MethodSource("bulletedSeparatedValuesProvider")
	void given_possiblyBulletedSeparatedString_when_parseBulletedList_then_correctProperty(
		final String string,
		final List<String> expectedStrings
	) {
		// given - the string
		// when
		final YugipediaProperty yugipediaProperty = PARSER.parse(
			PropertyParser.Type.BULLETED_LIST,
			string
		);
		// then
		assertThat(yugipediaProperty)
			.isEqualTo(new YugipediaProperty.ListProp(expectedStrings));
	}
}
