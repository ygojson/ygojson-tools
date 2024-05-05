package io.github.ygojson.application.yugipedia.mapper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.mapstruct.*;

import io.github.ygojson.application.logic.mapper.CardNumber;
import io.github.ygojson.application.logic.mapper.CardNumberParser;
import io.github.ygojson.application.yugipedia.parser.model.SetRow;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;
import io.github.ygojson.model.data.Print;
import io.github.ygojson.model.data.definition.localization.Language;
import io.github.ygojson.model.data.definition.localization.Region;

/**
 * Mapper for the YGOJSON {@link Print} from {@link YugipediaProperty} map.
 */
// TODO: use CDI for injection as we will use it with quarkus
@Mapper(uses = YugipediaPropertyBaseMapper.class)
public abstract class YugipediaPrintMapper {

	protected static final String TO_QUESTION_MARK_AS_NULL =
		"toQuestionMarkAsNull";

	public List<Print> toPrints(final Map<String, YugipediaProperty> properties) {
		return Arrays
			.stream(YugipediaLanguageProps.values())
			.flatMap(langProps -> toPrints(langProps, properties))
			.filter(Objects::nonNull)
			.toList();
	}

	Stream<Print> toPrints(
		YugipediaLanguageProps langProps,
		Map<String, YugipediaProperty> properties
	) {
		final YugipediaProperty.SetsProp prop = langProps.getProperty(properties);
		if (prop == null) {
			return Stream.empty();
		}
		return prop.rows().stream().flatMap(setRow -> toPrints(langProps, setRow));
	}

	Stream<Print> toPrints(
		final YugipediaLanguageProps langProps,
		final SetRow setRow
	) {
		final CardNumber cardNumber = toCardNumber(
			setRow,
			langProps.getPossibleRegion()
		);
		if (setRow.rarities() == null) {
			return Stream.of(
				toPrint(cardNumber, setRow.setPageName(), langProps.getLanguage(), null)
			);
		}
		return setRow
			.rarities()
			.stream()
			.map(rarity ->
				toPrint(
					cardNumber,
					setRow.setPageName(),
					langProps.getLanguage(),
					rarity
				)
			);
	}

	protected CardNumber toCardNumber(
		SetRow setRow,
		final List<Region> possibleRegions
	) {
		final CardNumber cardNumber = CardNumberParser.parseCardNumber(
			setRow.cardNumber(),
			possibleRegions
		);
		if (cardNumber == CardNumber.NONE) {
			return new CardNumber(null, setRow.setPageName(), null, null, null, null);
		}
		return cardNumber;
	}

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "cardId", ignore = true)
	@Mapping(target = "setId", ignore = true)
	@Mapping(target = "printCode", source = "cardNumber.stringValue")
	@Mapping(
		target = "setCode",
		source = "cardNumber.setCode",
		qualifiedByName = TO_QUESTION_MARK_AS_NULL
	)
	@Mapping(
		target = "printNumberPrefix",
		source = "cardNumber.printNumberPrefix",
		qualifiedByName = TO_QUESTION_MARK_AS_NULL
	)
	@Mapping(
		target = "printNumberSuffix",
		source = "cardNumber.printNumberSuffix",
		qualifiedByName = TO_QUESTION_MARK_AS_NULL
	)
	@Mapping(
		target = "rarity",
		source = "rarity",
		qualifiedByName = YugipediaPropertyBaseMapper.TO_LOWER_CASE
	)
	protected abstract Print toPrint(
		CardNumber cardNumber,
		String setName,
		Language language,
		String rarity
	);

	@AfterMapping
	protected void updateSetName(@MappingTarget Print print, String setName) {
		if (print.getPrintCode() == null) {
			print.setSetCode(setName);
		}
	}

	@AfterMapping
	protected void updatePrintNumber(@MappingTarget Print print) {
		if (print.getSetCode() == null && print.getPrintNumber() == 0) {
			print.setPrintNumber(null);
		}
	}

	// TODO: not sure if we should keep it
	// TODO: see https://github.com/ygojson/ygojson-tools/issues/5
	@AfterMapping
	protected void removePrintCodeIfUnknown(@MappingTarget Print print) {
		print.setPrintCode(toQuestionMarkAsNull(print.getPrintCode()));
	}

	@Named(TO_QUESTION_MARK_AS_NULL)
	protected String toQuestionMarkAsNull(final String value) {
		if (value == null) {
			return null;
		}
		if (value.contains("?")) {
			return null;
		}
		return value;
	}
}
