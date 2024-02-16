package io.github.ygojson.tools.dataprovider.domain;

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
import io.github.ygojson.tools.dataprovider.PrintData;

/**
 * Generator for UUIDs.
 */
 public class UUIDGenerator {

	 public enum Namespace {
		 CARD("ygojon/card"),
		 SET("ygojson/set"),
		 PRINT("ygojson/print"),
		 TEMPORARY("ygojson/temp");

		 private final GUID namespaceGuid;

		 Namespace(final String name) {
			 this.namespaceGuid = GUID.v5(GUID.NAMESPACE_OID, name);
		 }

		 UUID generateV5(final String value) {
			 return GUID.v5(this.namespaceGuid, value).toUUID();
		 }

	 }

	public record PrintDataUUIDs (UUID printId, UUID cardId, UUID setId) {
	}

	public PrintDataUUIDs generate(final PrintData printData) {
		return new PrintDataUUIDs(
			generate(printData.print()),
			generate(printData.card()),
			generate(printData.set())
		);
	}

	public UUID generate(final Card card) {
		if (card.getId() != null) {
			throw new IllegalArgumentException("Card already has an ID");
		}
		return uuidV5(Namespace.CARD,
			Stream.of(
				getNullableNested(card.getIdentifiers(), Identifiers::getKonamiId),
				getNullableNested(card.getIdentifiers(), Identifiers::getPassword),
				card.getName()
			)
		);
	}

	private <T> Object getNullableNested(final T object, final Function<T, Object> getter) {
		return object == null ? null : getter.apply(object);
	}

	public UUID generate(final Set set) {
		if (set.getId() != null) {
			throw new IllegalArgumentException("Set already has an ID");
		}
		if (set.getSetCode() == null && set.getName() == null) {
			// cannot compute only with prefix/type/series
			return null;
		}
		return uuidV5(Namespace.SET,
			Stream.of(
				set.getSetCode(),
				set.getPrintNumberPrefix(),
				set.getType(),
				set.getSeries(),
				set.getName()
			)
		);
	}

	public UUID generate(final Print print) {
		if (print.getId() != null) {
			throw new IllegalArgumentException("Print already has an ID");
		}
		// only print-code is necessary here
		return uuidV5(Namespace.PRINT, Stream.of(print.getPrintCode()));
	}

	private static UUID uuidV5(final Namespace namespace, final Stream<Object> fields) {
		 final AtomicInteger nullFields = new AtomicInteger(0);
		 final List<String> asString = fields
			 .peek(field -> {
				 if (field == null) {
					 nullFields.incrementAndGet();
				 }
			 })
			 .map(Objects::toString)
			 .toList();
		 // null only when there is no information to get an ID
		 if (nullFields.get() == asString.size()) {
			 // TODO: generate random instead?
			 return null;
		 }
		return namespace.generateV5(String.join("", asString));
	}

}
