package io.github.ygojson.application.core.datastore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

import io.github.ygojson.application.core.datastore.db.RawBaseEntity;
import io.github.ygojson.application.core.datastore.db.card.RawCard;
import io.github.ygojson.application.core.datastore.db.card.RawCardRepository;
import io.github.ygojson.application.core.datastore.db.set.RawSetRepository;

@QuarkusTest
class RawDataStoreUnitTest {

	@Inject
	RawCardRepository cardRepository;

	@Inject
	RawSetRepository setRepository;

	RawDataStore dataStore;

	@BeforeEach
	@Transactional
	void beforeEach() {
		createDataStoreIfMissing();
		cleanup();
	}

	@AfterEach
	@Transactional
	void afterAll() {
		cleanup();
	}

	private void createDataStoreIfMissing() {
		if (dataStore == null) {
			dataStore =
				new RawDataStore(
					new YgoJsonIDGenerator(),
					setRepository,
					cardRepository
				);
		}
	}

	private void cleanup() {
		cardRepository.deleteAll();
		setRepository.deleteAll();
	}

	@Test
	void given_randomCard_when_addNewCard_then_containsEntityWithYgjsonId() {
		// given
		final RawCard entity = Instancio
			.of(RawCard.class)
			.ignore(field(RawBaseEntity.class, "id"))
			.ignore(field(RawBaseEntity.class, "ygojsonId"))
			.create();
		// when
		dataStore.addNewCard(entity);
		// when
		final UUID result = entity.ygojsonId;
		assertThat(dataStore.getCardByYgojsonId(result)).isPresent();
	}

	@Test
	void given_randomCardWithYgoJsonId_when_addNewCard_then_fails() {
		// given
		final RawCard entity = Instancio
			.of(RawCard.class)
			.ignore(field(RawBaseEntity.class, "id"))
			.create();
		// when
		final ThrowableAssert.ThrowingCallable callable = () ->
			dataStore.addNewCard(entity);
		// then
		assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
	}
}
