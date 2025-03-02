package io.github.ygojson.application.core.db.print;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.instancio.Select.field;

import java.util.UUID;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.ygojson.application.core.db.RuntimeBaseEntity;
import io.github.ygojson.application.core.db.card.CardEntity;
import io.github.ygojson.application.core.db.set.SetEntity;

@QuarkusTest
public class PrintRepositoryWithRefsTest {

	@Inject
	PrintRepository repository;

	private CardEntity existingCard;
	private SetEntity existingSet;

	@BeforeEach
	@Transactional
	void beforeEach() {
		createCard();
		createSet();
		repository.deleteAll();
	}

	@Transactional
	void createCard() {
		existingCard = new CardEntity();
		existingCard.id = UUID.randomUUID();
		existingCard.persist();
	}

	@Transactional
	void createSet() {
		existingSet = new SetEntity();
		existingSet.id = UUID.randomUUID();
		existingSet.persist();
	}

	@AfterEach
	@Transactional
	void afterAll() {
		repository.deleteAll();
		if (existingCard != null) {
			existingCard.delete();
		}
		if (existingSet != null) {
			existingSet.delete();
		}
	}

	// creates a PrintEntity without references
	private InstancioApi<PrintEntity> withExistingRefs() {
		return Instancio
			.of(PrintEntity.class)
			.set(field(PrintEntity.class, "card"), existingCard)
			.set(field(PrintEntity.class, "set"), existingSet);
	}

	@Test
	void given_entityInstanceWithRefs_when_save_then_repositoryContainsEntity() {
		// given
		final PrintEntity entity = withExistingRefs()
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
	void given_entityInstanceWithRefs_when_save_then_retrievedEntityContainsRefs() {
		// given
		final PrintEntity entity = withExistingRefs()
			.ignore(field(RuntimeBaseEntity.class, "id"))
			.create();
		// when
		final UUID result = repository.save(entity);
		final PrintEntity persistedEntity = repository.findById(result);
		// when
		assertSoftly(softly -> {
			softly
				.assertThat(persistedEntity.card)
				.describedAs("card reference")
				.isNotNull();
			softly
				.assertThat(persistedEntity.set)
				.describedAs("set reference")
				.isNotNull();
		});
	}
}
