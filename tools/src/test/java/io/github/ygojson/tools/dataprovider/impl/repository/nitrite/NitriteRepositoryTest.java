package io.github.ygojson.tools.dataprovider.impl.repository.nitrite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.instancio.Select.field;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.assertj.core.api.ThrowableAssert;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.common.mapper.JacksonMapperModule;
import org.dizitart.no2.index.IndexType;
import org.dizitart.no2.repository.EntityDecorator;
import org.dizitart.no2.repository.EntityId;
import org.dizitart.no2.repository.EntityIndex;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.ygojson.tools.dataprovider.domain.repository.RepositoryException;

class NitriteRepositoryTest {

	private static final Logger log = LoggerFactory.getLogger(NitriteRepositoryTest.class);

	public NitriteRepository<TestEntity> nitriteRepository;

	@BeforeEach
	void beforeEach() {
		final Nitrite nitrite = Nitrite.builder().loadModule(new JacksonMapperModule()).openOrCreate();
		nitriteRepository = new NitriteRepository<>(nitrite, new TestEntityDecorator());
	}

	@AfterEach
	void afterEach() {
        try {
            nitriteRepository.close();
        } catch (Exception e) {
            log.error("Failed to close test nitrite repository", e);
        }
    }


	@Test
	void given_newEntity_when_insert_then_entityIsPersisted() {
		// given
		final TestEntity testEntity = Instancio.create(TestEntity.class);
		// when
		nitriteRepository.insert(testEntity);
		// then
		final List<TestEntity> testEntities = nitriteRepository.findAll().toList();
		assertThat(testEntities)
			.singleElement()
			.isEqualTo(testEntity);
	}

	@Test
	void given_entityWithSameId_when_insert_then_exceptionIsThrown() {
		// given
		final TestEntity testEntity = Instancio.create(TestEntity.class);
		nitriteRepository.insert(testEntity);
		final TestEntity sameIdEntity = Instancio
			.of(TestEntity.class)
			.set(field("id"), testEntity.id)
			.create();
		// when
		final ThrowableAssert.ThrowingCallable callable = () -> nitriteRepository.insert(sameIdEntity);
		// then
		assertThatThrownBy(callable)
			.isInstanceOf(RepositoryException.class);
	}

	@Test
	void given_filledRepository_when_size_then_countIsCorrect() {
		// given
		IntStream.range(0, 10).forEach(i -> nitriteRepository.insert(Instancio.create(TestEntity.class)));
		// when
		final long size = nitriteRepository.size();
		// then
		assertThat(size).isEqualTo(10);
	}

	@Test
	void given_existingEntity_when_update_then_entityIsPersisted() {
		// given
		final TestEntity testEntity = Instancio.create(TestEntity.class);
		final TestEntity testEntityUpdated = Instancio.of(TestEntity.class)
				.set(field("id"), testEntity.id)
					.create();
		nitriteRepository.insert(testEntity);
		// when
		nitriteRepository.update(testEntityUpdated);
		// then
		final List<TestEntity> testEntities = nitriteRepository.findAll().toList();
		assertThat(testEntities)
			.singleElement()
			.isNotEqualTo(testEntity)
			.isEqualTo(testEntityUpdated);
	}

	@Test
	void given_existingEntity_when_findFirstById_then_entityIsReturned() {
		// given
		final TestEntity testEntity = Instancio.create(TestEntity.class);
		nitriteRepository.insert(testEntity);
		// when
		final Optional<TestEntity> testEntityFound = nitriteRepository.findFirstBy("id", testEntity.id);
		// then
		assertThat(testEntityFound)
			.get()
			.isEqualTo(testEntity);
	}

	@Test
	void given_nonExistingEntity_when_findFirstById_then_emptyOptionalIsReturned() {
		// given
		final TestEntity testEntity = Instancio.create(TestEntity.class);
		nitriteRepository.insert(testEntity);
		// when
		final Optional<TestEntity> testEntityFound = nitriteRepository.findFirstBy("id", UUID.randomUUID());
		// then
		assertThat(testEntityFound).isEmpty();
	}

	@Test
	void given_severalExistingEntities_when_findBy_then_entityIsReturned() {
		// given
		final String stringValue = "Test String To Find";
		IntStream.range(0, 10)
				.mapToObj(i -> Instancio.of(TestEntity.class)
					.set(field("stringValue"), stringValue).create())
			   .forEach(nitriteRepository::insert);
		// when
		final Stream<TestEntity> testEntityFound = nitriteRepository.findBy("stringValue", stringValue);
		// then
		assertThat(testEntityFound)
			.hasSize(10)
			.extracting(TestEntity::stringValue)
			.allMatch(stringValue::equals);
	}

	@Test
	void given_existingEntity_when_delete_then_entityIsRemoved() {
		// given
		final TestEntity testEntity = Instancio.create(TestEntity.class);
		nitriteRepository.insert(testEntity);
		// when
		nitriteRepository.delete(testEntity);
		// then
		final List<TestEntity> testEntities = nitriteRepository.findAll().toList();
		assertThat(testEntities).isEmpty();
	}

	@Test
	void given_severalEntities_when_deleteAll_then_repositoryIsEmpty() {
		// given
		Instancio.createList(TestEntity.class).forEach(nitriteRepository::insert);
		// when
		nitriteRepository.deleteAll();
		// then
		final List<TestEntity> testEntities = nitriteRepository.findAll().toList();
		assertThat(testEntities).isEmpty();
	}

	private static class TestEntityDecorator implements EntityDecorator<TestEntity> {

		@Override
		public Class<TestEntity> getEntityType() {
			return TestEntity.class;
		}

		@Override
		public EntityId getIdField() {
			return new EntityId("id");
		}

		@Override
		public List<EntityIndex> getIndexFields() {
			return List.of(
				new EntityIndex(IndexType.UNIQUE, "id"),
				new EntityIndex(IndexType.FULL_TEXT, "stringValue"),
				new EntityIndex(IndexType.NON_UNIQUE, "stringValue"),
				new EntityIndex(IndexType.NON_UNIQUE, "integerValue")
			);
		}
	}

	record TestEntity(
		UUID id,
		String stringValue,
		Integer integerValue
	) {
	}

}
