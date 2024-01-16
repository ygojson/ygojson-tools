package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import io.github.ygojson.model.data.definition.localization.Region;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.CardNumber;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.YugipediaLanguageRegion;

@Mapper
public abstract class CardNumberMapper {

	/**
	 * Pattern to split the set-code from the rest of the print-number/set-suffix.
	 */
	private static final Pattern SET_CODE_SPLIT_PATTERN = Pattern.compile("-");

	/**
	 * Pattern to divide the print-code to prefix, number and suffix.
	 */
	private static final Pattern PRINT_CODE_PATTERN = Pattern.compile(
		"(\\D*)(\\d*)(\\D*)"
	);

	/**
	 * Group on {@link #PRINT_CODE_PATTERN} identifying the prefix.
	 */
	private static final int PRINT_CODE_PREFIX_GROUP = 1;
	/**
	 * Group on {@link #PRINT_CODE_PATTERN} identifying the number.
	 */
	private static final int PRINT_CODE_NUMBER_GROUP = 2;
	/**
	 * Group on {@link #PRINT_CODE_PATTERN} identifying the suffix.
	 */
	private static final int PRINT_CODE_SUFFIX_GROUP = 3;

	private final GeneralMapper generalMapper = Mappers.getMapper(
		GeneralMapper.class
	);

	@Named("unreleasedToNull")
	protected String mapUnreleasedToNull(final String value) {
		final String mappedValue = generalMapper.mapBlankStringToNull(value);
		if (mappedValue != null && mappedValue.contains("?")) {
			return null;
		}
		return mappedValue;
	}

	@Mapping(
		target = "stringValue",
		source = "stringValue",
		qualifiedByName = "unreleasedToNull"
	)
	@Mapping(
		target = "setCode",
		source = "setCode",
		qualifiedByName = "unreleasedToNull"
	)
	@Mapping(
		target = "printNumberPrefix",
		source = "printNumberPrefix",
		qualifiedByName = "unreleasedToNull"
	)
	@Mapping(
		target = "printNumberSuffix",
		source = "printNumberSuffix",
		qualifiedByName = "unreleasedToNull"
	)
	protected abstract CardNumber copyWithoutUnreleasedFields(
		final CardNumber cardNumber
	);

	@Mapping(target = "printNumber", ignore = true)
	protected abstract CardNumber mapWithUnreleasedPrintNumber(
		final CardNumber cardNumber
	);

	@AfterMapping
	protected CardNumber updateCardNumber(
		final CardNumber originalCardNumber,
		@MappingTarget final CardNumber updatedCardNumber
	) {
		if (originalCardNumber.equals(updatedCardNumber)) {
			return originalCardNumber;
		}
		// this indicates an unreleased print/set
		if (updatedCardNumber.stringValue() == null) {
			final Integer printNumber = updatedCardNumber.printNumber();
			if (printNumber != null && printNumber == 0) {
				return mapWithUnreleasedPrintNumber(updatedCardNumber);
			}
		}
		return updatedCardNumber;
	}

	/**
	 * Maps the set-prefix to a CardNumber.
	 *
	 * @param setPrefix the set prefix is the one that doesn't include print-number or suffix.
	 * @param region the language-region on Yugipedia
	 *
	 * @return the mapped object
	 */
	public CardNumber mapSetPrefixToCardNumber(
		final String setPrefix,
		final YugipediaLanguageRegion region
	) {
		final CardNumber cardNumber = mapInternalToCardNumber(setPrefix, region);
		return copyWithoutUnreleasedFields(cardNumber);
	}

	private CardNumber mapInternalToCardNumber(
		final String value,
		final YugipediaLanguageRegion region
	) {
		if (value == null || value.isBlank()) {
			return CardNumber.NONE;
		}
		final String[] setCodeSplit = SET_CODE_SPLIT_PATTERN.split(value, 2);
		final String setCode = generalMapper.mapBlankStringToNull(setCodeSplit[0]);
		Region regionCode = Region.NONE;
		String printNumberPrefix = null;
		if (setCodeSplit.length == 2) {
			printNumberPrefix = setCodeSplit[1];
			regionCode = toRegionCode(printNumberPrefix, region);
			if (regionCode != null && regionCode != Region.NONE) {
				printNumberPrefix =
					printNumberPrefix.substring(regionCode.value().length());
			}
		}
		printNumberPrefix = generalMapper.mapBlankStringToNull(printNumberPrefix);
		return new CardNumber(
			value,
			setCode,
			regionCode,
			printNumberPrefix,
			null,
			null
		);
	}

	/**
	 * Maps the print-code to a CardNumber.
	 *
	 * @param cardNumber the actual card-number on a print.
	 * @param region the language-region on Yugipedia
	 *
	 * @return the mapped object
	 */
	public CardNumber mapPrintCodeToCardNumber(
		final String cardNumber,
		final YugipediaLanguageRegion region
	) {
		// map first as a set-prefix to re-use the functionality
		final CardNumber initialMapping = mapInternalToCardNumber(
			cardNumber,
			region
		);
		if (initialMapping == CardNumber.NONE) {
			return initialMapping;
		}
		// the prefix is considered a set-prefix
		final String printNumber = initialMapping.printNumberPrefix();
		final Matcher matcher = PRINT_CODE_PATTERN.matcher(printNumber);
		final String prefix;
		final String number;
		final String suffix;
		if (matcher.matches()) {
			prefix = matcher.group(PRINT_CODE_PREFIX_GROUP);
			number = matcher.group(PRINT_CODE_NUMBER_GROUP);
			suffix = matcher.group(PRINT_CODE_SUFFIX_GROUP);
		} else {
			prefix = null;
			number = printNumber;
			suffix = null;
		}
		final CardNumber mapped = new CardNumber(
			initialMapping.stringValue(),
			initialMapping.setCode(),
			initialMapping.regionCode(),
			generalMapper.mapBlankStringToNull(prefix),
			generalMapper.mapToNullableInteger(number),
			generalMapper.mapBlankStringToNull(suffix)
		);
		return copyWithoutUnreleasedFields(mapped);
	}

	private static Region toRegionCode(
		final String value,
		final YugipediaLanguageRegion languageRegion
	) {
		if (value == null) {
			return null;
		}
		Region regionCode = Region.NONE;
		for (final Region region : languageRegion.getAcceptedRegions()) {
			final String codeString = region.name();
			if (value.startsWith(codeString)) {
				regionCode = region;
				break;
			}
		}
		return regionCode;
	}
}
