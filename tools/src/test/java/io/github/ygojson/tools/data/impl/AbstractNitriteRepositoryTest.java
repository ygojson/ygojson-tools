package io.github.ygojson.tools.data.impl;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.common.mapper.JacksonMapper;
import org.dizitart.no2.common.mapper.JacksonMapperModule;
import org.dizitart.no2.index.IndexType;
import org.dizitart.no2.repository.EntityDecorator;
import org.dizitart.no2.repository.EntityId;
import org.dizitart.no2.repository.EntityIndex;
import org.dizitart.no2.repository.ObjectRepository;
import org.dizitart.no2.repository.annotations.Index;
import org.dizitart.no2.repository.annotations.Indices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.utils.JsonUtils;

@Disabled("TODO")
class AbstractNitriteRepositoryTest {

	private Nitrite nitriteDb;


	@BeforeEach
	public void beforeEach() {
		// TODO: we should extract this logic
		final JacksonMapper jacksonMapper = new JacksonMapper() {
			@Override
			public ObjectMapper getObjectMapper() {
				// TODO: check a new object-mapper with FAIL_ON_UNKNOWN_PROPERTIES = false
				// TODO: otherwise it fails with _id cannot be serialized
				return JsonUtils.getObjectMapper();
			}
		};
		// database
		nitriteDb = Nitrite.builder()
			.loadModule(new JacksonMapperModule())
			//.loadModule(new JacksonMapperModule(new JavaTimeModule()))
			//.loadModule(NitriteModule.module(jacksonMapper))
			.openOrCreate();
	}


	@Test
	void given_emptyEntity_when_saveEntity_then_isSaved() {
		// given
		final TestEntity entity = new TestEntity();
		final ObjectRepository<TestEntity> testRepository = nitriteDb.getRepository(new TestEntityDecorator());
		// when
		testRepository.insert(entity);
		// then
		//final List<TestEntity> savedCards = testRepository.findAll().toList();
		//assertThat(savedCards)
		//	.singleElement()
		//	.isEqualTo(entity);
	}

	@Test
	void testCardRepo() {
		final ObjectRepository<NitriteWrapper> cardObjectRepository = nitriteDb.getRepository(NitriteWrapper.class);
		final Card card = new Card();
		cardObjectRepository.insert(new NitriteWrapper() {{
			wrapped = card;
		}});
		cardObjectRepository.insert(new NitriteWrapper() {{
			wrapped = new Card();
		}});
		cardObjectRepository.find().toList().stream().forEach(x -> System.out.println(x));
	}

	@Indices(
		@Index(fields = "wrapped.id", type = IndexType.UNIQUE)
	)
	public static class NitriteWrapper {

		//@Id(fieldName = "card", embeddedFields = "id")
		public UUID id;

		public Card wrapped;

		@Override
		public String toString() {
			return wrapped.toString();
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof NitriteWrapper that)) return false;
            return Objects.equals(wrapped, that.wrapped);
		}

		@Override
		public int hashCode() {
			return Objects.hash(wrapped);
		}
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class TestEntity {
		private UUID id = null;
		private String name = null;

		public UUID getId() {
			return id;
		}

		public void setId(UUID id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	public static class TestEntityDecorator implements EntityDecorator<TestEntity> {

		@Override
		public Class<TestEntity> getEntityType() {
			return TestEntity.class;
		}

		@Override
		public EntityId getIdField() {
			return new EntityId("_id");
		}

		@Override
		public List<EntityIndex> getIndexFields() {
			return null;
		}
	}

}
