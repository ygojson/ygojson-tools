package io.github.magicdgs.nitrite_test;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.common.mapper.JacksonMapperModule;
import org.dizitart.no2.filters.FluentFilter;
import org.dizitart.no2.index.IndexType;
import org.dizitart.no2.repository.ObjectRepository;
import org.dizitart.no2.repository.annotations.Index;
import org.dizitart.no2.repository.annotations.Indices;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

public class NitritePlaygroundTests {

	@Test
	public void testUnique() {
		try (final Nitrite nitrite = Nitrite.builder()
				.loadModule(new JacksonMapperModule())
				.openOrCreate()) {
			final ObjectRepository<EntityUniqueFullText> repository = nitrite.getRepository(EntityUniqueFullText.class);
			testRepository(repository, EntityUniqueFullText::new);
		}
	}

	@Test
	public void testNoUnique() {
		try (final Nitrite nitrite = Nitrite.builder()
				.loadModule(new JacksonMapperModule())
				.openOrCreate()) {
			final ObjectRepository<EntityNoUniqueFullText> repository = nitrite.getRepository(EntityNoUniqueFullText.class);
			testRepository(repository, EntityNoUniqueFullText::new);
		}
	}

	private <T> void testRepository(ObjectRepository<T> repository,
									Function<String, T> constructorFunction) {
		final String prefix = "test";
		final String entityValue1 =  prefix + " 1";
		final String entityValue2 = prefix + " 2";

		repository.insert(constructorFunction.apply(entityValue1));
		repository.insert(constructorFunction.apply(entityValue2));

		try {
			System.out.println("Equals search:");
			repository.find(FluentFilter.where("value").eq(entityValue1)).forEach(System.out::println);
			repository.find(FluentFilter.where("value").eq(entityValue2)).forEach(System.out::println);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			System.out.println("\nText search:");
			repository.find(FluentFilter.where("value").text(prefix)).forEach(System.out::println);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Indices({
		@Index(fields = "value", type = IndexType.FULL_TEXT),
		@Index(fields = "value", type = IndexType.UNIQUE)
	})
	public record EntityUniqueFullText(String value) {

	}

	@Indices({
		@Index(fields = "value", type = IndexType.FULL_TEXT),
		@Index(fields = "value", type = IndexType.NON_UNIQUE)
	})
	public record EntityNoUniqueFullText(String value) {

	}

}
