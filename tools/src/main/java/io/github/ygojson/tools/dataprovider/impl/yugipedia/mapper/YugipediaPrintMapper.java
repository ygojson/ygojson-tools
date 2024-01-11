package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import io.github.ygojson.model.data.Print;
import io.github.ygojson.model.data.definition.localization.Language;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.CardTable2;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.MarkupString;

/**
 * Mapper for the YGOJSON {@link Print} from yugipedia {@link CardTable2} model.
 */
@Mapper(
	uses = { GeneralMapper.class },
	unmappedTargetPolicy = ReportingPolicy.ERROR
)
public abstract class YugipediaPrintMapper {

	private static final int EXPECTED_FIELD_NUMBER = 3;

	// 1st field: cardNumber - out printCode
	private static final int CARD_NUMBER_FIELD = 0;
	// 2nd field: setName (useful for the firstSeries cards withut printCode)
	private static final int SET_NAME_FIELD = 1;
	// 3rd field: rarity list
	private static final int RARITY_LIST_FIELD = 2;

	/**
	 * Maps to the YGOJSON Print model the yugipedia relevant information.
	 *
	 * @param cardTable2 the CardTable2 model
	 *
	 * @return list of prints contained in the CardTable2 model;
	 * 		   {@code null} if cardTable2 is {@code null};
	 * 		   empty list if no prints are present
	 */
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
		if (fields.size() < EXPECTED_FIELD_NUMBER) {
			throw new IllegalStateException(
				"Expected at least 3 fields per card-set print but found: " + fields
			);
		}
		final String cardNumber = fields.get(CARD_NUMBER_FIELD).toString();
		final String setName = fields.get(SET_NAME_FIELD).toString();
		final Stream<MarkupString> rarities = fields
			.get(RARITY_LIST_FIELD)
			.splitByComma();

		return rarities
			.map(rarity ->
				new PrintInfo(cardNumber, setName, language, rarity.toString())
			)
			.map(this::mapPrint);
	}

	/**
	 * Container to store the relevant information about the card print from {@link CardTable2}.
	 *
	 * @param cardNumber card number printed on the card
	 * @param setName the name of the set
	 * @param language the language of the card
	 * @param rarity the rarity of the card
	 */
	protected record PrintInfo(
		String cardNumber,
		String setName,
		Language language,
		String rarity
	) {
		boolean isFirstSeries() {
			return cardNumber == null || cardNumber.isBlank();
		}
	}

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "cardId", ignore = true)
	@Mapping(target = "setId", ignore = true)
	@Mapping(
		target = "printCode",
		source = "cardNumber",
		qualifiedByName = "blankStringToNull"
	)
	@Mapping(
		target = "firstSeriesSet",
		source = "setName",
		conditionExpression = "java(info.isFirstSeries())"
	)
	@Mapping(
		target = "rarity",
		source = "rarity",
		qualifiedByName = "toLowerCase"
	)
	protected abstract Print mapPrint(final PrintInfo info);
}
