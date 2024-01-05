package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import io.github.ygojson.model.data.Print;
import io.github.ygojson.model.data.definition.localization.Language;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaException;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.CardTable2;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.MarkupString;

@Mapper
public abstract class YugipediaPrintMapper {

	private static final int EXPECTED_FIELD_NUMBER = 3;

	// 1st field: cardNumber - split into setPrefix and printNumber
	private static final int CARD_NUMBER_FIELD = 0;
	// 2nd field: set_name - currently ignored
	private static final int SET_NAME_FIELD = 1;
	// 3rd filed: rarity list
	private static final int RARITY_FIELD = 2;

	// TODO: this is not the correct split
	private static final String CARD_NUMBER_SEPARATOR = "-";

	/**
	 * Pattern to split cardNumber into languageCode and printNumber.
	 * <br>
	 * The languageCode corresponds to {@code LANG_GROUP}
	 * and {@code NUMBER_GROUP} to the printNumber.
	 */
	private static final Pattern LANGUAGE_NUMBER_PATTERN = Pattern.compile(
		"([a-zA-Z]+)(\\d+)"
	);

	private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile(
		"([a-zA-Z0-9]+)-([a-zA-Z]+)?([a-zA-Z]?\\d+K?)"
	);

	private static final int LANG_GROUP = 1;
	private static final int NUMBER_GROUP = 2;

	private final GeneralMapper generalMapper = Mappers.getMapper(
		GeneralMapper.class
	);

	public List<Print> mapToPrints(final CardTable2 cardTable2) {
		if (cardTable2 == null) {
			return null;
		}
		return Stream
			.of(
				cardTable2SetsToPrints(cardTable2.en_sets(), Language.EN),
				cardTable2SetsToPrints(cardTable2.na_sets(), Language.EN),
				cardTable2SetsToPrints(cardTable2.eu_sets(), Language.EN),
				cardTable2SetsToPrints(cardTable2.au_sets(), Language.EN),
				cardTable2SetsToPrints(cardTable2.ae_sets(), Language.EN),
				cardTable2SetsToPrints(cardTable2.de_sets(), Language.DE),
				cardTable2SetsToPrints(cardTable2.sp_sets(), Language.ES),
				cardTable2SetsToPrints(cardTable2.fr_sets(), Language.FR),
				cardTable2SetsToPrints(cardTable2.fc_sets(), Language.FR),
				cardTable2SetsToPrints(cardTable2.it_sets(), Language.IT),
				cardTable2SetsToPrints(cardTable2.ja_sets(), Language.JA),
				cardTable2SetsToPrints(cardTable2.jp_sets(), Language.JA),
				cardTable2SetsToPrints(cardTable2.kr_sets(), Language.KO),
				cardTable2SetsToPrints(cardTable2.pt_sets(), Language.PT),
				cardTable2SetsToPrints(cardTable2.sc_sets(), Language.ZH_HANS),
				cardTable2SetsToPrints(cardTable2.tc_sets(), Language.ZH_HANT)
			)
			.flatMap(i -> i)
			.toList();
	}

	private Stream<Print> cardTable2SetsToPrints(
		final List<MarkupString> markupString,
		final Language language
	) {
		if (markupString == null) {
			return Stream.empty();
		}
		return markupString
			.stream()
			.flatMap(line -> singleLineToRarityPrints(line, language))
			.filter(Objects::nonNull);
	}

	private Stream<Print> singleLineToRarityPrints(
		final MarkupString cardTable2PrintLine,
		final Language language
	) {
		if (cardTable2PrintLine == null) {
			return null;
		}
		final List<MarkupString> fields = cardTable2PrintLine
			.splitBySemicolon()
			.toList();
		if (fields.size() != EXPECTED_FIELD_NUMBER) {
			// TODO: should we really throw here or just ignore?
			throw new YugipediaException("Expected 3 fields per card-set print");
		}
		final String cardNumber = fields.get(CARD_NUMBER_FIELD).toString();
		final String setName = fields.get(SET_NAME_FIELD).toString();
		final PrintInfo printInfo = toPrintInfo(cardNumber);

		return fields
			.get(RARITY_FIELD)
			.splitByComma()
			.map(rarity -> mapPrint(printInfo, setName, rarity, language));
	}

	private PrintInfo toPrintInfo(final String cardNumberField) {
		if (cardNumberField == null || cardNumberField.isEmpty()) {
			return null;
		}
		final Matcher cardNumberPatternMatcher = CARD_NUMBER_PATTERN.matcher(cardNumberField);
		if (cardNumberPatternMatcher.matches()) {
			String setCode = cardNumberPatternMatcher.group(1);
			String regionCode = cardNumberPatternMatcher.group(2);
			String setNumber = cardNumberPatternMatcher.group(3);
			// TODO: handle special cases here?

			return new PrintInfo(setCode, regionCode, setNumber);
		}
		// cannot identify the print-info
		return null;
	}

	private record PrintInfo(
		String setCode,
		String regionCode,
		String setNumber
	) {}

	private Print mapPrint(
		final PrintInfo info,
		final String setName,
		final MarkupString rarity,
		final Language language
	) {
		// default card fields
		final Print print = new Print();
		print.setRarity(generalMapper.mapToLowerCase(rarity.toString()));
		print.setLanguage(language);
		// if there is no info, it is a first-series set
		if (info == null) {
			print.setFirstSeriesSet(setName);
			return print;
		}
		print.setSetCode(generalMapper.mapBlankStringToNull(info.setCode()));
		print.setRegionCode(generalMapper.mapBlankStringToNull(info.regionCode()));
		print.setSetNumber(generalMapper.mapBlankStringToNull(info.setNumber()));
		return print;
	}
}
