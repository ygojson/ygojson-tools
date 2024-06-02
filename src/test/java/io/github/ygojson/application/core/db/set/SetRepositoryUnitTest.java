package io.github.ygojson.application.core.db.set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.instancio.Select.field;

import java.util.UUID;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.assertj.core.api.ThrowableAssert;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.ygojson.application.core.db.RuntimeBaseEntity;

@QuarkusTest
class SetRepositoryUnitTest {

	@Inject
	SetRepository repository;

	@BeforeEach
	@Transactional
	void beforeEach() {
		repository.deleteAll();
	}

	@Test
	void given_entityInstanceWithoutId_when_save_then_repositoryContainsEntity() {
		// given
		final SetEntity entity = Instancio
			.of(SetEntity.class)
			.ignore(field(RuntimeBaseEntity.class, "id"))
			.create();
		// when
		final UUID result = repository.save(entity);
		// when
		assertSoftly(softly -> {
			softly
				.assertThat(result)
				.isNotNull()
				.extracting(UUID::version)
				.isEqualTo(7);
			softly.assertThat(repository.findById(result)).isNotNull();
		});
	}

	@Test
	void given_entityInstanceWithId_when_save_then_isSaved() {
		// given - entity with ID (cached value for assert)
		final SetEntity entity = Instancio.of(SetEntity.class).create();
		final String id = entity.id.toString();
		// when
		final UUID result = repository.save(entity);
		// then
		assertThat(result).isNotNull().extracting(UUID::toString).isEqualTo(id);
	}

	@Test
	void given_entityAlreadySaved_when_save_then_fails() {
		// given
		final SetEntity entity = Instancio
			.of(SetEntity.class)
			.ignore(field(RuntimeBaseEntity.class, "id"))
			.create();
		final UUID savedId = repository.save(entity);
		final SetEntity duplicated = Instancio
			.of(SetEntity.class)
			.set(field(RuntimeBaseEntity.class, "id"), savedId)
			.create();
		// when
		final ThrowableAssert.ThrowingCallable callable = () ->
			repository.save(duplicated);
		// then
		// assert without specific exception so far
		assertThatThrownBy(callable).isNotNull();
	}

	@Test
	void given_entityWithSameYgojsonId_when_save_then_fails() {
		// given
		final SetEntity entity1 = Instancio
			.of(SetEntity.class)
			.ignore(field(RuntimeBaseEntity.class, "id"))
			.create();
		final UUID savedId = repository.save(entity1);
		final SetEntity entity2 = Instancio
			.of(SetEntity.class)
			.ignore(field(RuntimeBaseEntity.class, "id"))
			.set(field(RuntimeBaseEntity.class, "ygojsonId"), entity1.ygojsonId)
			.create();
		// when
		final ThrowableAssert.ThrowingCallable callable = () ->
			repository.save(entity2);
		// then
		// assert without specific exception so far
		assertThatThrownBy(callable).isNotNull();
	}
}
