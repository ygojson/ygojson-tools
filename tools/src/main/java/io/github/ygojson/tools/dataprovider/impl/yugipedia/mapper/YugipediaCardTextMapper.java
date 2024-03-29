package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.definition.CardText;
import io.github.ygojson.model.data.definition.localization.Language;
import io.github.ygojson.model.utils.data.CardUtils;
import io.github.ygojson.model.utils.data.utils.LocalizationUtils;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaException;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.wikitext.MarkupStringMapper;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.CardTable2;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.MarkupString;

/**
 * Mapper for all {@link CardText} fields.
 * <br>
 * Includes named mappers and after-mappings for all main and language-specific fields.
 */
@Mapper
class YugipediaCardTextMapper {

	protected MarkupStringMapper markupStringMapper = Mappers.getMapper(
		MarkupStringMapper.class
	);

	@Named("mainCardText")
	protected CardText mapToMainCardText(final CardTable2 cardTable2) {
		return mapToCardText(
			cardTable2,
			CardTable2::name,
			CardTable2::pendulum_effect
		);
	}

	@Named("esCardText")
	protected CardText mapEsCardText(final CardTable2 cardTable2) {
		return mapToCardText(
			cardTable2,
			CardTable2::es_name,
			CardTable2::es_pendulum_effect
		);
	}

	@Named("deCardText")
	protected CardText mapDeCardText(final CardTable2 cardTable2) {
		return mapToCardText(
			cardTable2,
			CardTable2::de_name,
			CardTable2::de_pendulum_effect
		);
	}

	@Named("frCardText")
	protected CardText mapFrCardText(final CardTable2 cardTable2) {
		return mapToCardText(
			cardTable2,
			CardTable2::fr_name,
			CardTable2::fr_pendulum_effect
		);
	}

	@Named("itCardText")
	protected CardText mapItCardText(final CardTable2 cardTable2) {
		return mapToCardText(
			cardTable2,
			CardTable2::it_name,
			CardTable2::it_pendulum_effect
		);
	}

	@Named("jaCardText")
	protected CardText mapJaCardText(final CardTable2 cardTable2) {
		return mapToCardText(
			cardTable2,
			ct2 -> markupStringMapper.map(ct2.ja_name()),
			CardTable2::ja_pendulum_effect
		);
	}

	@Named("koCardText")
	protected CardText mapKoCardText(final CardTable2 cardTable2) {
		return mapToCardText(
			cardTable2,
			ct2 -> markupStringMapper.map(ct2.ko_name()),
			CardTable2::ko_pendulum_effect
		);
	}

	@Named("ptCardText")
	protected CardText mapPtCardText(final CardTable2 cardTable2) {
		return mapToCardText(
			cardTable2,
			CardTable2::pt_name,
			CardTable2::pt_pendulum_effect
		);
	}

	@Named("zhHansCardText")
	protected CardText mapZhHansCardText(final CardTable2 cardTable2) {
		return mapToCardText(
			cardTable2,
			CardTable2::sc_name,
			CardTable2::sc_pendulum_effect
		);
	}

	@Named("zhHantCardText")
	protected CardText mapZhHantCardText(final CardTable2 cardTable2) {
		return mapToCardText(
			cardTable2,
			CardTable2::tc_name,
			CardTable2::tc_pendulum_effect
		);
	}

	/**
	 * Main method to map to the CardText model,
	 * which can be used independently of the language.
	 *
	 * @param cardTable2 the Yugipedia CardTable2 model.
	 * @param nameGetter getter for the actual name in the mapped language.
	 * @param pendulumEffectGetter getter for the actual pendulum effect in the mapped language.
	 *
	 * @return the CardText with the mapped values (only name and pendulum effect can be mapped).
	 */
	private CardText mapToCardText(
		final CardTable2 cardTable2,
		final Function<CardTable2, String> nameGetter,
		final Function<CardTable2, MarkupString> pendulumEffectGetter
	) {
		if (cardTable2 == null) {
			return null;
		}
		final String name = nameGetter.apply(cardTable2);
		final MarkupString pendulumEffect = pendulumEffectGetter.apply(cardTable2);
		if (name == null && pendulumEffect == null) {
			return null;
		}
		final CardText cardText = new CardText();
		cardText.setName(name);
		cardText.setPendulumEffect(markupStringMapper.map(pendulumEffect));
		return cardText;
	}

	@AfterMapping
	protected void updateMainCardText(
		final CardTable2 cardTable2,
		@MappingTarget final Card card
	) {
		// set the initial card text
		LocalizationUtils.setAsMainLanguage(card, mapToMainCardText(cardTable2));
		// update the cardText and set it
		updateCardTextWithFullLore(card, cardTable2::lore, Language.EN);
	}

	@AfterMapping
	protected void updateEsCardText(
		final CardTable2 cardTable2,
		@MappingTarget final Card card
	) {
		updateCardTextWithFullLore(card, cardTable2::es_lore, Language.ES);
	}

	@AfterMapping
	protected void updateDeCardText(
		final CardTable2 cardTable2,
		@MappingTarget final Card card
	) {
		updateCardTextWithFullLore(card, cardTable2::de_lore, Language.DE);
	}

	@AfterMapping
	protected void updateFrCardText(
		final CardTable2 cardTable2,
		@MappingTarget final Card card
	) {
		updateCardTextWithFullLore(card, cardTable2::fr_lore, Language.FR);
	}

	@AfterMapping
	protected void updateItCardText(
		final CardTable2 cardTable2,
		@MappingTarget final Card card
	) {
		updateCardTextWithFullLore(card, cardTable2::it_lore, Language.IT);
	}

	@AfterMapping
	protected void updateJaCardText(
		final CardTable2 cardTable2,
		@MappingTarget final Card card
	) {
		updateCardTextWithFullLore(card, cardTable2::ja_lore, Language.JA);
	}

	@AfterMapping
	protected void updateKoCardText(
		final CardTable2 cardTable2,
		@MappingTarget final Card card
	) {
		updateCardTextWithFullLore(card, cardTable2::ko_lore, Language.KO);
	}

	@AfterMapping
	protected void updatePtCardText(
		final CardTable2 cardTable2,
		@MappingTarget final Card card
	) {
		updateCardTextWithFullLore(card, cardTable2::pt_lore, Language.PT);
	}

	@AfterMapping
	protected void updateZhHansCardText(
		final CardTable2 cardTable2,
		@MappingTarget final Card card
	) {
		updateCardTextWithFullLore(card, cardTable2::sc_lore, Language.ZH_HANS);
	}

	@AfterMapping
	protected void updateZhHantCardText(
		final CardTable2 cardTable2,
		@MappingTarget final Card card
	) {
		updateCardTextWithFullLore(card, cardTable2::tc_lore, Language.ZH_HANT);
	}

	/**
	 * Main method to update the CardText model,
	 * which can be used independently of the language.
	 *
	 * @param card the already mapped Card.
	 * @param fullLoreSupplier supplier for the lore (coming from the CardTable2 language-specific field).
	 * @param language the language to set the CardText in.
	 */
	private void updateCardTextWithFullLore(
		final Card card,
		final Supplier<MarkupString> fullLoreSupplier,
		final Language language
	) {
		final MarkupString fullLore = fullLoreSupplier.get();
		CardText cardText = LocalizationUtils.getLocalizedData(language, card);
		if (fullLore == null || fullLore.toString() == null) {
			return;
		}
		if (cardText == null) {
			cardText = new CardText();
		}
		doUpdateCardText(fullLoreSupplier.get(), card, cardText);
		LocalizationUtils.setLocalizedData(card, cardText, language);
	}

	/**
	 * Extracts the method to actually update the card text.
	 *
	 * @param loreText the full lore text.
	 * @param card the already mapped Card.
	 * @param cardText the cardText to update.
	 */
	private void doUpdateCardText(
		final MarkupString loreText,
		final Card card,
		final CardText cardText
	) {
		// we assume at the start that the lore is the effect text
		MarkupString materials = null;
		MarkupString effect = loreText;
		MarkupString flavor = null;

		// process extra-deck if necessary
		if (CardUtils.isExtraDeckMonster(card)) {
			final List<MarkupString> materialAndEffect = loreText.splitByBreak(2);
			switch (materialAndEffect.size()) {
				case 2 -> { // material and effect present
					materials = materialAndEffect.get(0);
					effect = materialAndEffect.get(1);
				}
				case 1 -> { // only materials are present (normal extra deck monster)
					materials = materialAndEffect.getFirst();
					effect = null;
				}
				case 0 -> {} // do nothing if the actual string is null/empty
				default -> throw new YugipediaException(
					"should not happen: splitByBreak should return 0-2 items"
				);
			}
		}
		// process flavor if necessary
		if (isFlavorText(effect, card)) {
			flavor = effect;
			effect = null;
		}
		// set using the mapper, which considers null values already
		cardText.setMaterials(markupStringMapper.map(materials));
		cardText.setEffectText(markupStringMapper.map(effect));
		cardText.setFlavorText(markupStringMapper.map(flavor));
	}

	private boolean isFlavorText(final MarkupString effect, final Card card) {
		return (
			effect != null &&
			(
				// if all italic, then it is flavor text
				effect.isAllItalic() ||
				// also check for a normal monster
				CardUtils.isNormalMonster(card) ||
				// otherwise, check if the flavor text was set on the main card
				// (some languages requires this as it is not in italic)
				card.getFlavorText() !=
				null
			)
		);
	}
}
