package io.github.ygojson.application.core.datastore;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.NameBasedGenerator;
import com.fasterxml.uuid.impl.UUIDUtil;
import jakarta.inject.Singleton;

import io.github.ygojson.application.core.datastore.db.card.RawCard;

@Singleton
public class YgoJsonIDGenerator {

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
			throw new IllegalArgumentException(
				name + " shouldn't have already a YGOJSON-ID"
			);
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
	public UUID generate(final RawCard card) {
		checkRequirements(card, c -> c.ygojsonId, "Card");
		return generateInternal(
			YgoJsonIDGenerator.Namespace.CARD,
			Stream.of(
				getOrNull(card.identifiers, i -> i.konamiId),
				getOrNull(card.identifiers, i -> i.password)
			)
		);
	}

	private <T> Object getOrNull(
		final T object,
		final Function<T, Object> getter
	) {
		return object == null ? null : getter.apply(object);
	}

	private UUID generateInternal(
		YgoJsonIDGenerator.Namespace namespace,
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
			return UUIDUtil.nilUUID();
		}
		return namespace.generateV5(String.join("/", fieldsAsString));
	}
}
