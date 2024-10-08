package io.github.ygojson.application.logic.uuid;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.NameBasedGenerator;
import com.fasterxml.uuid.impl.UUIDUtil;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.Print;
import io.github.ygojson.model.data.Set;
import io.github.ygojson.model.data.definition.Identifiers;

/**
 * Generator for IDs for YGOJSON.
 *
 * @deprecated should use the datastore-database entities instead
 * ({@link io.github.ygojson.application.core.datastore.YgoJsonIDGenerator}).
 */
@Deprecated
public class YgojsonIDGenerator {

	private static final NameBasedGenerator NAMESPACE_GENERATOR =
		Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_OID);

	private enum Namespace {
		CARD("ygojon/card"),
		SET("ygojson/set"),
		PRINT("ygojson/print");

		private final NameBasedGenerator generator;

		Namespace(final String name) {
			this.generator =
				Generators.nameBasedGenerator(NAMESPACE_GENERATOR.generate(name));
		}

		private UUID generateV5(final String value) {
			return generator.generate(value);
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
	public UUID generate(final Card card) {
		checkRequirements(card, Card::getId, "Card");
		return generateInternal(
			Namespace.CARD,
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
	public UUID generate(final Set set) {
		checkRequirements(set, Set::getId, "Set");
		return generateInternal(
			Namespace.SET,
			// FIXME: https://github.com/ygojson/ygojson-tools/issues/113 - should handle OCG/TCG exclusive sets with same name
			Stream.of(set.getName(), set.getSetCode(), set.getPrintNumberPrefix())
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
	public UUID generate(final Print print) {
		checkRequirements(print, Print::getId, "Print");
		return generateInternal(
			Namespace.PRINT,
			// FIXME: https://github.com/ygojson/ygojson-tools/issues/112 - should handle problematic prints
			Stream.of(print.getPrintCode(), print.getRarity())
		);
	}

	private <T> Object getOrNull(
		final T object,
		final Function<T, Object> getter
	) {
		return object == null ? null : getter.apply(object);
	}

	private UUID generateInternal(Namespace namespace, Stream<Object> fields) {
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
			return UUIDUtil.nilUUID();
		}
		return namespace.generateV5(String.join("/", fieldsAsString));
	}
}
