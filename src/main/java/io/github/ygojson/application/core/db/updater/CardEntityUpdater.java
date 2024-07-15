package io.github.ygojson.application.core.db.updater;

import io.github.ygojson.application.core.db.card.CardEntity;
import io.github.ygojson.application.logic.AtkDefValues;
import io.github.ygojson.application.logic.parser.AtkDefParser;

/**
 * Basic update functionality for the {@link CardEntity}.
 * <br>
 * This includes logic that should be used from mappers to update certain values of the card
 * and compute certain values.
 */
// TODO: this can have the split-text updater
public class CardEntityUpdater {


	private CardEntityUpdater() {
		// cannot be instantiated
	}

	public static void update(final CardEntity entity) {
		updateAtk(entity);
		updateDef(entity);
		updateLinkRating(entity);
		// TODO: not implemented
		//SplitCardTextUpdater.updateSplitCardText(entity.fullText);
	}

	// assumes that parsers are used for mapping
	private static void updateAtk(final CardEntity entity) {
		// TODO: updater should consider if the undefined is already set?s
		final AtkDefValues values = AtkDefParser.convertParsed(entity.atk);
		entity.atk = values.value();
		entity.atkUndefined = values.undefined();
	}

	private static void updateDef(final CardEntity entity) {
		// TODO: updater should consider if the undefined is already set?s
		final AtkDefValues values = AtkDefParser.convertParsed(entity.def);
		entity.def = values.value();
		entity.defUndefined = values.undefined();
	}

	private static void updateLinkRating(final CardEntity entity) {
		// TODO: updater should consider if the rating is already set?s
		if (entity.linkArrows != null) {
			final int computedLinkRating = entity.linkArrows.size();
			if (computedLinkRating != 0) {
				entity.linkRating = computedLinkRating;
			}
		}
	}


}
