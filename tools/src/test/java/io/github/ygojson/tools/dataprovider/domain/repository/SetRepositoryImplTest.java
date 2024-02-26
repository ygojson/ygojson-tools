package io.github.ygojson.tools.dataprovider.domain.repository;

import io.github.ygojson.model.data.Set;
import io.github.ygojson.tools.dataprovider.domain.repository.set.SetEntity;
import io.github.ygojson.tools.dataprovider.domain.repository.set.SetRepository;
import io.github.ygojson.tools.dataprovider.impl.repository.nitrite.NitriteBaseBuilderFactory;
import io.github.ygojson.tools.dataprovider.impl.repository.nitrite.SetRepositoryImpl;
import org.assertj.core.api.ThrowableAssert;
import org.dizitart.no2.Nitrite;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.instancio.Select.field;

class SetRepositoryImplTest {

	private static final Logger log = LoggerFactory.getLogger(SetRepositoryImplTest.class);

	private SetRepository setNitriteRepository;

	@BeforeEach
	void beforeEach() {
		final Nitrite nitrite = NitriteBaseBuilderFactory.create().openOrCreate();
		setNitriteRepository = new SetRepositoryImpl(nitrite);
	}

	@AfterEach
	void afterEach() {
		try {
			setNitriteRepository.close();
		} catch (Exception e) {
			log.error("Failed to close test nitrite repository", e);
		}
	}

	@Test
	void given_emptySetRepository_when_saveSet_thenRepositoryCountIsOne() {
		// given
		final SetEntity entity = Instancio.create(SetEntity.class);
		// when
		setNitriteRepository.save(entity);
		// then
		assertThat(setNitriteRepository.count()).isEqualTo(1);
	}

	@Test
	void given_emptySetRepository_when_saveSetWithoutInternalUUID_then_throwsRepositoryException() {
		// given
		final SetEntity entity = Instancio.of(SetEntity.class)
			.set(field("_uuid"), null)
			.create();
		// when
		final ThrowableAssert.ThrowingCallable callable = () -> setNitriteRepository.save(entity);
		// then
		assertThatThrownBy(callable).isInstanceOf(RepositoryException.class);
	}


	@Test
	void given_setWasSaved_when_saveSameSet_then_throwsRepositoryException() {
		// given
		final SetEntity entity = Instancio.create(SetEntity.class);
		setNitriteRepository.save(entity);
		// when
		final ThrowableAssert.ThrowingCallable callable = () -> setNitriteRepository.save(entity);
		// then
		assertThatThrownBy(callable).isInstanceOf(RepositoryException.class);
	}

	@Test
	void given_setWasSaved_when_saveSetWithSameInternalUUID_then_throwsRepositoryException() {
		// given
		final SetEntity entity = Instancio.create(SetEntity.class);
		setNitriteRepository.save(entity);
		final SetEntity withSameInternalUUID = new SetEntity(entity._uuid(), new Set(), ZonedDateTime.now(ZoneOffset.UTC));
		// when
		final ThrowableAssert.ThrowingCallable callable = () -> setNitriteRepository.save(withSameInternalUUID);
		// then
		assertThatThrownBy(callable).isInstanceOf(RepositoryException.class);
	}

	@Test
	void given_setWasSaved_when_saveSameSetWithDifferentUUID_then_throwsRepositoryException() {
		// given
		final Set set = Instancio.create(Set.class);
		setNitriteRepository.save(new SetEntity(set));
		// when
		final ThrowableAssert.ThrowingCallable callable = () -> setNitriteRepository.save(new SetEntity(set));
		// then
		assertThatThrownBy(callable).isInstanceOf(RepositoryException.class);
	}

	@Test
	void given_setWasSaved_when_findByNameWithName_then_returnCorrectSet() {
		// given
		final SetEntity entity = Instancio.create(SetEntity.class);
		setNitriteRepository.save(entity);
		// when
		final Optional<SetEntity> foundSet = setNitriteRepository.findByName(entity.set().getName());
		// then
		assertThat(foundSet)
			.get()
			.isEqualTo(entity);
	}

	@Test
	void given_setWasSaved_when_findByNameWithAltName_then_returnCorrectSet() {
		// given
		final SetEntity entity = Instancio.create(SetEntity.class);
		setNitriteRepository.save(entity);
		// when
		final Optional<SetEntity> foundSet = setNitriteRepository.findByName(entity.set().getNameAlt());
		// then
		assertThat(foundSet)
			.get()
			.isEqualTo(entity);
	}

	@Test
	void given_setWasSaved_when_findByNameWithNotPresent_then_returnEmpty() {
		// given
		final SetEntity entity = Instancio.create(SetEntity.class);
		setNitriteRepository.save(entity);
		// when
		final Optional<SetEntity> foundSet = setNitriteRepository.findByName("test");
		// then
		assertThat(foundSet)
			.isEmpty();
	}

	@Test
	void given_setWasSaved_when_findBySetCodeWithSetCode_then_returnCorrectSet() {
		// given
		final SetEntity entity = Instancio.create(SetEntity.class);
		setNitriteRepository.save(entity);
		// when
		final Optional<SetEntity> foundSet = setNitriteRepository.findBySetCode(entity.set().getSetCode());
		// then
		assertThat(foundSet)
			.get()
			.isEqualTo(entity);
	}

	@Test
	void given_setWasSaved_when_findBySetCodeWithAltSetCode_then_returnCorrectSet() {
		// given
		final SetEntity entity = Instancio.create(SetEntity.class);
		setNitriteRepository.save(entity);
		// when
		final Optional<SetEntity> foundSet = setNitriteRepository.findBySetCode(entity.set().getSetCodeAlt());
		// then
		assertThat(foundSet)
			.get()
			.isEqualTo(entity);
	}

	@Test
	void given_setWasSaved_when_findBySetCodeWithNotPresentSetCode_then_returnEmpty() {
		// given
		final SetEntity entity = Instancio.create(SetEntity.class);
		setNitriteRepository.save(entity);
		// when
		final Optional<SetEntity> foundSet = setNitriteRepository.findBySetCode("test");
		// then
		assertThat(foundSet)
			.isEmpty();
	}

}
