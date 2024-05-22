package io.github.ygojson.application.core.db.set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.instancio.Select.field;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.assertj.core.api.ThrowableAssert;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
			.ignore(field(PanacheEntity.class, "id"))
			.create();
		// when
		final Long result = repository.save(entity);
		// when
		assertSoftly(softly -> {
			softly.assertThat(result).isEqualTo(1).isNotNull();
			softly.assertThat(repository.findById(1L)).isNotNull();
		});
	}

	@Test
	void given_entityInstanceWithId_when_save_then_throwsException() {
		// given
		final SetEntity entity = Instancio.of(SetEntity.class).create();
		// when
		final ThrowableAssert.ThrowingCallable call = () -> repository.save(entity);
		// when
		assertThatThrownBy(call);
	}
}
