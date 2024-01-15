package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import io.github.ygojson.model.data.Print;
import io.github.ygojson.model.data.definition.localization.Language;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.CardNumber;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.YugipediaLanguageRegion;
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

	private final CardNumberMapper cardNumberMapper = Mappers.getMapper(
		CardNumberMapper.class
	);

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
			.of(YugipediaLanguageRegion.values())
			.flatMap(langRegion ->
				cardTable2SetsToPrints(
					langRegion.getCardTable2Prints(cardTable2),
					langRegion
				)
			)
			.toList();
	}

	private Stream<Print> cardTable2SetsToPrints(
		final List<MarkupString> markupString,
		final YugipediaLanguageRegion langRegion
	) {
		if (markupString == null) {
			return Stream.empty();
		}
		return markupString
			.stream()
			.flatMap(line -> singleLineToRarityPrints(line, langRegion))
			.filter(Objects::nonNull);
	}

	private Stream<Print> singleLineToRarityPrints(
		final MarkupString cardTable2PrintLine,
		final YugipediaLanguageRegion langRegion
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
		final String cardNumberString = fields.get(CARD_NUMBER_FIELD).toString();
		final String setName = fields.get(SET_NAME_FIELD).toString();
		final Stream<MarkupString> rarities = fields
			.get(RARITY_LIST_FIELD)
			.splitByComma();
		final Language language = langRegion.getLanguage();
		// card without known print-number
		final CardNumber cardNumber;
		if (cardNumberString == null || cardNumberString.isBlank()) {
			// in this case, the card-number is just having the code as the setName
			cardNumber = new CardNumber(null, setName, null, null, null, null);
		} else {
			cardNumber =
				cardNumberMapper.mapPrintCodeToCardNumber(cardNumberString, langRegion);
		}

		return rarities.map(rarity ->
			mapToPrint(cardNumber, language, rarity.toString())
		);
	}

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "cardId", ignore = true)
	@Mapping(target = "setId", ignore = true)
	@Mapping(target = "printCode", source = "cardNumber.stringValue")
	@Mapping(target = "setCode", source = "cardNumber.setCode")
	@Mapping(
		target = "printNumberPrefix",
		source = "cardNumber.printNumberPrefix"
	)
	@Mapping(target = "printNumber", source = "cardNumber.printNumber")
	@Mapping(
		target = "printNumberSuffix",
		source = "cardNumber.printNumberSuffix"
	)
	@Mapping(target = "regionCode", source = "cardNumber.regionCode")
	@Mapping(target = "language", source = "language")
	@Mapping(
		target = "rarity",
		source = "rarity",
		qualifiedByName = "toLowerCase"
	)
	protected abstract Print mapToPrint(
		final CardNumber cardNumber,
		final Language language,
		final String rarity
	);
}
