package io.github.ygojson.application.core.db.card;

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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.ygojson.application.core.db.RuntimeBaseEntity;

@QuarkusTest
class CardRepositoryUnitTest {

	@Inject
	CardRepository repository;

	@BeforeEach
	@Transactional
	void beforeEach() {
		repository.deleteAll();
	}

	@AfterEach
	@Transactional
	void afterAll() {
		repository.deleteAll();
	}

	@Test
	void given_emptyEntity_when_save_then_repositoryContainsEntity() {}

	@Test
	void given_entityInstanceWithoutId_when_save_then_repositoryContainsEntity() {
		// given
		final CardEntity entity = Instancio
			.of(CardEntity.class)
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
		final CardEntity entity = Instancio.of(CardEntity.class).create();
		final String id = entity.id.toString();
		// when
		final UUID result = repository.save(entity);
		// then
		assertThat(result).isNotNull().extracting(UUID::toString).isEqualTo(id);
	}

	@Test
	void given_entityAlreadySaved_when_save_then_fails() {
		// given
		final CardEntity entity = Instancio.of(CardEntity.class).create();
		final CardEntity duplicated = Instancio
			.of(CardEntity.class)
			.set(field(RuntimeBaseEntity.class, "id"), entity.id)
			.create();
		// when
		repository.save(entity);
		final ThrowableAssert.ThrowingCallable callable = () ->
			repository.save(duplicated);
		// then
		// assert without specific exception so far
		assertThatThrownBy(callable).isNotNull();
	}

	@Test
	void given_entityWithSameYgojsonId_when_save_then_fails() {
		// given
		final CardEntity entity1 = Instancio.create(CardEntity.class);
		final CardEntity entity2 = Instancio
			.of(CardEntity.class)
			.set(field(RuntimeBaseEntity.class, "ygojsonId"), entity1.ygojsonId)
			.create();
		// when
		repository.save(entity1);
		final ThrowableAssert.ThrowingCallable callable = () ->
			repository.save(entity2);
		// then
		// assert without specific exception so far
		assertThatThrownBy(callable).isNotNull();
	}

	@Test
	void given_entityWithSameKonamiId_when_save_then_fails() {
		// given
		final CardEntity entity1 = Instancio.create(CardEntity.class);
		final CardEntity entity2 = Instancio.of(CardEntity.class).create();
		entity2.identifiers.konamiId = entity1.identifiers.konamiId;
		// when
		repository.save(entity1);
		final ThrowableAssert.ThrowingCallable callable = () ->
			repository.save(entity2);
		// then
		// assert without specific exception so far
		assertThatThrownBy(callable).isNotNull();
	}

	@Test
	void given_entityWithSamePassword_when_save_then_fails() {
		// given
		final CardEntity entity1 = Instancio.create(CardEntity.class);
		final CardEntity entity2 = Instancio.of(CardEntity.class).create();
		entity2.identifiers.password = entity1.identifiers.password;
		// when
		repository.save(entity1);
		final ThrowableAssert.ThrowingCallable callable = () ->
			repository.save(entity2);
		// then
		// assert without specific exception so far
		assertThatThrownBy(callable).isNotNull();
	}

	@Test
	void given_entityWithSamePasswordAlt_when_save_then_fails() {
		// given
		final CardEntity entity1 = Instancio.create(CardEntity.class);
		final CardEntity entity2 = Instancio.of(CardEntity.class).create();
		entity2.identifiers.passwordAlt = entity1.identifiers.passwordAlt;
		// when
		repository.save(entity1);
		final ThrowableAssert.ThrowingCallable callable = () ->
			repository.save(entity2);
		// then
		// assert without specific exception so far
		assertThatThrownBy(callable).isNotNull();
	}

	@Test
	void given_entityWithSameYugipediaPageId_when_save_then_fails() {
		// given
		final CardEntity entity1 = Instancio.create(CardEntity.class);
		final CardEntity entity2 = Instancio.of(CardEntity.class).create();
		entity2.identifiers.yugipediaPageId = entity1.identifiers.yugipediaPageId;
		// when
		repository.save(entity1);
		final ThrowableAssert.ThrowingCallable callable = () ->
			repository.save(entity2);
		// then
		// assert without specific exception so far
		assertThatThrownBy(callable).isNotNull();
	}
}
