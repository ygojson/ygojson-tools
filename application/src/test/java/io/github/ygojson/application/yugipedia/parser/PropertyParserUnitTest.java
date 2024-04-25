package io.github.ygojson.application.yugipedia.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.ygojson.application.yugipedia.parser.model.SetRow;
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

	static Stream<Arguments> setRowsProvider() {
		return Stream.of(
			Arguments.of(
				"Single String",
				List.of(new SetRow("Single String", null, List.of()))
			),
			Arguments.of(
				"; Vol.1; Ultra Rare",
				List.of(new SetRow(null, "Vol.1", List.of("Ultra Rare")))
			),
			Arguments.of(
				"Card number 1; Set page name 1; Rarity 1",
				List.of(
					new SetRow("Card number 1", "Set page name 1", List.of("Rarity 1"))
				)
			),
			Arguments.of(
				"Card number 2; Set page name 2; Rarity 2",
				List.of(
					new SetRow("Card number 2", "Set page name 2", List.of("Rarity 2"))
				)
			),
			Arguments.of(
				"CHIM-JP010; Chaos Impact; Super Rare, Secret Rare, 20th Secret Rare",
				List.of(
					new SetRow(
						"CHIM-JP010",
						"Chaos Impact",
						List.of("Super Rare", "Secret Rare", "20th Secret Rare")
					)
				)
			),
			Arguments.of(
				"????-EN0??; Unknown Set; ",
				List.of(new SetRow("????-EN0??", "Unknown Set", List.of()))
			),
			Arguments.of("; ; ", List.of(new SetRow(null, null, List.of()))),
			Arguments.of(
				"CHIM-JP010; Chaos Impact; Super Rare, Secret Rare, 20th Secret Rare",
				List.of(
					new SetRow(
						"CHIM-JP010",
						"Chaos Impact",
						List.of("Super Rare", "Secret Rare", "20th Secret Rare")
					)
				)
			),
			Arguments.of(
				"; Legend of Blue Eyes White Dragon; Common\nSDK-001; Starter Deck: Kaiba; \nCHIM-EN0??; Chaos Impact; \n????-EN???; Eternity Code; ",
				List.of(
					new SetRow(
						null,
						"Legend of Blue Eyes White Dragon",
						List.of("Common")
					),
					new SetRow("SDK-001", "Starter Deck: Kaiba", List.of()),
					new SetRow("CHIM-EN0??", "Chaos Impact", List.of()),
					new SetRow("????-EN???", "Eternity Code", List.of())
				)
			),
			Arguments.of(
				"MS-EN001; My set; Rare; New column",
				List.of(new SetRow("MS-EN001", "My set", List.of("Rare")))
			)
		);
	}

	@ParameterizedTest
	@MethodSource("setRowsProvider")
	void given_setRowsString_when_parseSetsProp_then_correctProperty(
		final String string,
		final List<SetRow> expected
	) {
		// given - string
		// when
		final YugipediaProperty actual = PARSER.parse(
			PropertyParser.Type.SET_ROWS,
			string
		);
		// then
		final YugipediaProperty expectedProperty = new YugipediaProperty.SetsProp(
			expected
		);
		assertThat(actual).isEqualTo(expectedProperty);
	}
}
