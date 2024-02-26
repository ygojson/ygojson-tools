package io.github.ygojson.tools.dataprovider.domain.uuid;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

import com.github.f4b6a3.uuid.alt.GUID;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.Print;
import io.github.ygojson.model.data.Set;
import io.github.ygojson.model.data.definition.Identifiers;

/**
 * Generator for IDs for YGOJSON.
 */
public class YgojsonIDGenerator {

	/**
	 * Container for the result of the generation of the ID.
	 * <br>
	 * This is required to inform the caller if the generated ID is random
	 * or based on data.
	 *
	 * @param id the generated ID
	 * @param isRandom if the ID is randomly generated instead of using the defined IDs.
	 */
	public record YgojsonID(UUID id, boolean isRandom) {}

	private enum Namespace {
		CARD("ygojon/card"),
		SET("ygojson/set"),
		PRINT("ygojson/print"),
		TEMPORARY("ygojson/temp");

		private final GUID namespaceGuid;

		Namespace(final String name) {
			this.namespaceGuid = GUID.v5(GUID.NAMESPACE_OID, name);
		}

		private UUID generateV5(final String value) {
			return GUID.v5(this.namespaceGuid, value).toUUID();
		}
	}

	private <T> void checkRequirements(
		final T object,
		Function<T, UUID> idGetter,
		final String name
	) {
		if (object == null) {
			throw new IllegalArgumentException(name + " shouldn't be null");
		}
		if (idGetter.apply(object) != null) {
			throw new IllegalArgumentException(name + " shouldn't have an ID");
		}
	}

	/**
	 * Generates the ID of a card.
	 *
	 * @param card object to generate the ID for.
	 *
	 * @return the generated ID for a card.
	 * @throws IllegalArgumentException if the card is {@code null} or the ID is already present.
	 */
	public YgojsonID generate(final Card card) {
		checkRequirements(card, Card::getId, "Card");
		return generateInternal(
			Namespace.CARD,
			// TODO: should we consider other fields too?
			Stream.of(
				getOrNull(card.getIdentifiers(), Identifiers::getKonamiId),
				getOrNull(card.getIdentifiers(), Identifiers::getPassword)
			)
		);
	}

	/**
	 * Generates the ID of a set.
	 *
	 * @param set object to generate the ID for.
	 *
	 * @return the generated ID for a set.
	 * @throws IllegalArgumentException if the set is {@code null} or the ID is already present.
	 */
	public YgojsonID generate(final Set set) {
		checkRequirements(set, Set::getId, "Set");
		return generateInternal(
			Namespace.SET,
			// TODO: should we consider other fields too?
			// TODO: maybe we need to consider if it is TCG/OCG only
			Stream.of(set.getName(), set.getSetCode())
		);
	}

	/**
	 * Generates the ID of a print.
	 *
	 * @param print object to generate the ID for.
	 *
	 * @return the generated ID for a print.
	 * @throws IllegalArgumentException if the print is {@code null} or the ID is already present.
	 */
	public YgojsonID generate(final Print print) {
		checkRequirements(print, Print::getId, "Print");
		return generateInternal(
			Namespace.PRINT,
			// TODO: should we consider other fields too?
			Stream.of(print.getPrintCode(), print.getCardId(), print.getSetId())
		);
	}

	private <T> Object getOrNull(
		final T object,
		final Function<T, Object> getter
	) {
		return object == null ? null : getter.apply(object);
	}

	private YgojsonID generateInternal(
		Namespace namespace,
		Stream<Object> fields
	) {
		final AtomicInteger nullFields = new AtomicInteger(0);
		final List<String> fieldsAsString = fields
			.peek(field -> {
				if (field == null) {
					nullFields.incrementAndGet();
				}
			})
			.map(Objects::toString)
			.toList();
		// random UUID on the namespace if there is no information at all
		if (nullFields.get() == fieldsAsString.size()) {
			return new YgojsonID(UUID.randomUUID(), true);
		}
		final UUID id = namespace.generateV5(String.join("/", fieldsAsString));
		return new YgojsonID(id, false);
	}
}
