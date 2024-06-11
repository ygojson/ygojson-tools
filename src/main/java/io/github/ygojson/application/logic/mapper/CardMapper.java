package io.github.ygojson.application.logic.mapper;

import org.mapstruct.*;

import io.github.ygojson.application.core.db.card.CardEntity;
import io.github.ygojson.application.core.db.card.CardLocalizedValues;
import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.definition.CardText;
import io.github.ygojson.model.data.definition.CardType;
import io.github.ygojson.model.data.definition.LinkArrow;
import io.github.ygojson.model.utils.LocalizationUtils;

/**
 * Mapper between the {@link Card} model and the {@link CardEntity}.
 */
@Mapper(
	injectionStrategy = InjectionStrategy.CONSTRUCTOR,
	unmappedTargetPolicy = ReportingPolicy.ERROR,
	componentModel = MappingConstants.ComponentModel.JAKARTA_CDI
)
public abstract class CardMapper {

	private static final CardText EMPTY = new CardText();

	@Mapping(target = "id", source = "ygojsonId")
	@Mapping(target = "identifiers", source = "identifiers")
	@Mapping(target = "name", source = "en.name")
	@Mapping(target = "effectText", source = "en.effectText")
	@Mapping(target = "flavorText", source = "en.flavorText")
	@Mapping(target = "materials", source = "en.materialsText")
	@Mapping(target = "pendulumEffect", source = "en.pendulumEffectText")
	@Mapping(target = "localizedData.de", source = "de")
	@Mapping(target = "localizedData.es", source = "es")
	@Mapping(target = "localizedData.fr", source = "fr")
	@Mapping(target = "localizedData.it", source = "it")
	@Mapping(target = "localizedData.ja", source = "ja")
	@Mapping(target = "localizedData.ko", source = "ko")
	@Mapping(target = "localizedData.pt", source = "pt")
	@Mapping(target = "localizedData.zhHans", source = "zhHans")
	@Mapping(target = "localizedData.zhHant", source = "zhHant")
	public abstract Card toModel(CardEntity entity);

	@Mapping(target = "materials", source = "materialsText")
	@Mapping(target = "pendulumEffect", source = "pendulumEffectText")
	protected abstract CardText toCardText(CardLocalizedValues values);

	@InheritInverseConfiguration
	@Mapping(target = "id", ignore = true)
	public abstract CardEntity toEntity(Card model);

	@InheritInverseConfiguration
	protected abstract CardLocalizedValues toCardLocalizedValues(
		CardText cardText
	);

	/**
	 * Updates the model by deleting the empty localized data.
	 *
	 * @param model the model to update.
	 */
	@AfterMapping
	protected void deleteEmptyLocalizedData(@MappingTarget Card model) {
		LocalizationUtils
			.getNonMainLanguages()
			.forEach(language -> {
				final CardText info = LocalizationUtils.getLocalizedData(
					language,
					model
				);
				if (EMPTY.equals(info)) {
					LocalizationUtils.setLocalizedData(model, null, language);
					return;
				}
			});
	}

	// required as we have to use the fromValue mapping
	// (as we should store in the entity already with the proper format)
	protected final CardType toCardTypeEnum(final String cardType) {
		try {
			return CardType.fromValue(cardType);
		} catch (final IllegalArgumentException e) {
			throw new MappingException("Unknown card type: " + cardType);
		}
	}

	protected final String cardTypeToString(final CardType cardType) {
		if (cardType == null) {
			return null;
		}
		return cardType.value();
	}

	// required as we have to use the fromValue mapping
	// (as we should store in the entity already with the proper format)
	protected final LinkArrow toLinkArrow(final String linkArrow) {
		try {
			return LinkArrow.fromValue(linkArrow);
		} catch (final IllegalArgumentException e) {
			throw new MappingException("Unknown link arrow: " + linkArrow);
		}
	}

	protected final String linkArrowToString(final LinkArrow linkArrow) {
		if (linkArrow == null) {
			return null;
		}
		return linkArrow.value();
	}
}
